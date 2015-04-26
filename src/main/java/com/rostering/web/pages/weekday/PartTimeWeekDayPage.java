package com.rostering.web.pages.weekday;

import cl.upla.memoria.sam.business.rostering.RosteringService;
import cl.upla.memoria.sam.business.rostering.Util;
import cl.upla.memoria.sam.business.rostering.exception.RosteringException;
import cl.upla.memoria.sam.dao.DriverDao;
import cl.upla.memoria.sam.dao.PreferenceDao;
import cl.upla.memoria.sam.dao.ShiftDao;
import cl.upla.memoria.sam.dao.exception.PersistenceException;
import cl.upla.memoria.sam.model.*;
import com.rostering.web.pages.template.BasePage;
import com.rostering.web.pages.weekday.modal.PartTimeWeekDayModalSave;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

//@AuthorizeInstantiation(Roles.ADMIN)
public class PartTimeWeekDayPage extends BasePage {

    @SpringBean(name = "rosteringService")
    RosteringService rosteringService;

    @SpringBean(name = "driverDao")
    DriverDao driverDao;

    @SpringBean(name = "shiftDao")
    ShiftDao shiftDao;

    @SpringBean(name = "preferenceDao")
    PreferenceDao preferenceDao;

    private WebMarkupContainer step1;
    private WebMarkupContainer step2;

    private Form formFilter;
    private CheckGroup<Driver> maquinistaGroup;
    private CheckGroup<Shift> turnoGroup;
    private WebMarkupContainer asignacionGroup;
    private ListView<Allocation> asignacionDataView;

    private PartTimeWeekDayModalSave partTimeWeekDayModalSave;

    public PartTimeWeekDayPage() {

        formFilter = createForm("formFilter", new PartTimeWeekDayFormFilter());
        add(formFilter);

        /*step 1*/
        step1 = new WebMarkupContainer("step1");
        step1.setOutputMarkupId(true);
        formFilter.add(step1);

        AjaxButton buttonAsignar = createAsignarButton("buttonAsignar");
        step1.add(buttonAsignar);

        maquinistaGroup = createCheckGroupMaquinista("maquinistaGroup");
        step1.add(maquinistaGroup);

        turnoGroup = createCheckGroupTurno("turnoGroup");
        step1.add(turnoGroup);

        try {
            loadMaquinistas(driverDao.getByType(ShiftType.PART_TIME_WEEKDAY));
        } catch (PersistenceException e) {
            //TODO handle exception
            e.printStackTrace();
        }
        try {
            loadTurnos(shiftDao.getByType(ShiftType.PART_TIME_WEEKDAY));
        } catch (PersistenceException e) {
            //TODO handle exception
            e.printStackTrace();
        }

        /*step2*/
        step2 = new WebMarkupContainer("step2");
        step2.setOutputMarkupId(true);
        formFilter.add(step2);
        step2.setVisible(false);

        partTimeWeekDayModalSave = new PartTimeWeekDayModalSave("modalSave", "Nombre proceso asignación");
        step2.add(partTimeWeekDayModalSave);

        asignacionGroup = new WebMarkupContainer("asignacionGroup");
        asignacionGroup.setOutputMarkupId(true);
        createAsignacionesList(new ArrayList<Allocation>(0));
        asignacionGroup.add(asignacionDataView);
        step2.add(asignacionGroup);

        AjaxButton buttonCancelar = createCancelarButton("buttonCancelar");
        step2.add(buttonCancelar);

        AjaxButton buttonGuardar = createGuardarButton("buttonGuardar");
        step2.add(buttonGuardar);

    }

    private CheckGroup<Allocation> createCheckGroupAsignacion(String name) {

        CheckGroup<Allocation> checkGroup = new CheckGroup<Allocation>(name, new ArrayList<Allocation>());
        checkGroup.setOutputMarkupId(true);

        return checkGroup;
    }

    private CheckGroup<Driver> createCheckGroupMaquinista(String name) {

        CheckGroup<Driver> checkGroup = new CheckGroup<Driver>(name, new ArrayList<Driver>());
        checkGroup.setOutputMarkupId(true);

        return checkGroup;
    }

    private CheckGroup<Shift> createCheckGroupTurno(String name) {

        CheckGroup<Shift> checkGroup = new CheckGroup<Shift>(name, new ArrayList<Shift>());
        checkGroup.setOutputMarkupId(true);

        return checkGroup;
    }

    private AjaxButton createAsignarButton(String name) {
        AjaxButton button = new AjaxButton(name) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                clearFeedBackPanel(target);

                target.add(formFilter);

                target.add(step1);
                target.add(step2);
                target.add(asignacionGroup);

                target.add(maquinistaGroup);

                List<Driver> maquinistas = new ArrayList<Driver>(maquinistaGroup.getModelObject());
                List<Shift> turnos = new ArrayList<Shift>(turnoGroup.getModelObject());

                if (maquinistas.isEmpty()) {
                    target.add(feedbackPanel);
                    error("Debe seleccionar al menos un maquinista");
                } else if (turnos.isEmpty()) {
                    target.add(feedbackPanel);
                    error("Debe seleccionar al menos un turno");
                } else if (maquinistas.size() != turnos.size()) {
                    target.add(feedbackPanel);
                    error("Debe seleccionar igual número de maquinistas y turnos");
                } else if (maquinistas.size() == 1) {
                    error("Debe seleccionar al menos 2 maquinistas y 2 turnos");

                } else {

                    String errorMessage = null;

                    List<Preference> preferenceList = Util.toPreference(maquinistas, turnos);

                    for (Preference preference : preferenceList) {

                        int value = 0;
                        try {

                            value = preferenceDao.getPreferenceValue(preference.getDriver(), preference.getShift());

                        } catch (PersistenceException e) {
                            //TODO handle exception
                            errorMessage = "No se pudieron obtener las preferencias. " + e.getMessage();
                            e.printStackTrace();
                            break;
                        }
                        preference.setValue(value);

                    }

                    if (null != errorMessage) {
                        error(errorMessage);
                        return;
                    }

                    List<Allocation> asignaciones = null;

                    try {

                        asignaciones = rosteringService.doRoster(preferenceList);

                    } catch (RosteringException e) {
                        e.printStackTrace();
                    }


                    if (asignaciones != null) {
                        if (!asignaciones.isEmpty()) {
                            asignacionDataView.setDefaultModelObject(Util.justAllocated(asignaciones));

                            step1.setVisible(false);
                            step2.setVisible(true);

                        } else {
                            error("No hay asignaciones disponibles");
                        }
                    } else {
                        error("No hay asignaciones disponibles");
                    }
                }

            }
        };

        button.setOutputMarkupId(true);

        return button;
    }

    private AjaxButton createCancelarButton(String name) {
        AjaxButton button = new AjaxButton(name) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                clearFeedBackPanel(target);

                target.add(formFilter);

                target.add(step1);
                target.add(step2);

                target.add(maquinistaGroup);

                step1.setVisible(true);
                step2.setVisible(false);

            }
        };

        button.setOutputMarkupId(true);

        return button;
    }

    private AjaxButton createGuardarButton(String name) {
        AjaxButton button = new AjaxButton(name) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                clearFeedBackPanel(target);

                target.add(formFilter);
                target.add(partTimeWeekDayModalSave);
                target.add(asignacionGroup);

                List<Allocation> asignaciones = new ArrayList<Allocation>(asignacionDataView.getModelObject());

                partTimeWeekDayModalSave.setAsignaciones(asignaciones);
                partTimeWeekDayModalSave.setFeedbackPanel(feedbackPanel);
                partTimeWeekDayModalSave.show(true);

            }
        };

        button.setOutputMarkupId(true);

        return button;
    }

    private void loadMaquinistas(final List<Driver> maquinistas) {    //, new ListDataProvider<Maquinista>(maquinistas)
        ListView<Driver> maquinistaDataView = new ListView<Driver>("maquinistaDataView", maquinistas) {
            @Override
            protected void populateItem(ListItem<Driver> item) {

                final Driver driver = item.getModelObject();

                item.add(new Label("id", driver.getId()));
                item.add(new Label("name", driver.getName()));
                item.add(new Check<Driver>("incluir", item.getModel()));

            }


        };
        maquinistaDataView.setOutputMarkupId(true);
        maquinistaGroup.add(maquinistaDataView);
    }

    private void createAsignacionesList(final List<Allocation> asignaciones) {
        asignacionDataView = new ListView<Allocation>("asignacionDataView", asignaciones) {
            @Override
            protected void populateItem(ListItem<Allocation> item) {

                final Allocation asignacion = item.getModelObject();

                item.add(new Label("maquinista", asignacion.getDriver().getName()));
                item.add(new Label("turno", asignacion.getShift().getDescription()));

            }


        };
        asignacionDataView.setOutputMarkupId(true);

    }

    private void loadTurnos(final List<Shift> turnos) {    //, new ListDataProvider<Maquinista>(maquinistas)
        ListView<Shift> turnoDataView = new ListView<Shift>("turnoDataView", turnos) {
            @Override
            protected void populateItem(ListItem<Shift> item) {

                final Shift turno = item.getModelObject();

                item.add(new Label("id", turno.getId()));
                item.add(new Label("name", turno.getDescription()));
                item.add(new Check<Shift>("incluir", item.getModel()));

            }


        };
        turnoDataView.setOutputMarkupId(true);
        turnoGroup.add(turnoDataView);
    }


    @Override
    protected String getPageTitle() {
        return "Asignar Maquinistas Part-time Semana";//Rostering;
    }


}
