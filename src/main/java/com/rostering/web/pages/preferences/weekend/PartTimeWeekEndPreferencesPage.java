package com.rostering.web.pages.preferences.weekend;

import cl.upla.memoria.sam.business.rostering.RosteringService;
import cl.upla.memoria.sam.dao.DriverDao;
import cl.upla.memoria.sam.dao.PreferenceDao;
import cl.upla.memoria.sam.dao.ShiftDao;
import cl.upla.memoria.sam.dao.exception.PersistenceException;
import cl.upla.memoria.sam.model.*;
import com.rostering.web.components.dropdown.GenericDropDown;
import com.rostering.web.components.dropdown.renderer.DriverRenderer;
import com.rostering.web.components.dropdown.renderer.ShiftRenderer;
import com.rostering.web.pages.preferences.weekend.modal.PartTimeWeekEndEditModal;
import com.rostering.web.pages.preferences.weekend.modal.PartTimeWeekEndEditModalFilter;
import com.rostering.web.pages.template.BasePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//@AuthorizeInstantiation(Roles.ADMIN)
public class PartTimeWeekEndPreferencesPage extends BasePage {

    @SpringBean(name = "rosteringService")
    RosteringService rosteringService;

    @SpringBean(name = "driverDao")
    DriverDao driverDao;

    @SpringBean(name = "shiftDao")
    ShiftDao shiftDao;

    @SpringBean(name = "preferenceDao")
    PreferenceDao preferenceDao;

    private Form editForm;
    private PartTimeWeekEndEditModal editModal;

    private WebMarkupContainer tableContainer;

    private Form formFilter;
    private CheckGroup<Driver> preferencesGroup;
    private AjaxButton searchButton;

    private GenericDropDown<Driver> driverField;
    private GenericDropDown<Shift> shiftField;

    private ListView<Preference> preferencesDataView;

    public PartTimeWeekEndPreferencesPage() {

        formFilter = createForm("formFilter", new PartTimeWeekEndPreferencesFormFilter());
        add(formFilter);

        tableContainer = new WebMarkupContainer("tableContainer");
        tableContainer.setOutputMarkupId(true);
        tableContainer.setRenderBodyOnly(false);
        formFilter.add(tableContainer);


        driverField = new GenericDropDown<Driver>("driverField", getDriverList(), new DriverRenderer());
        driverField.setOutputMarkupId(true);
        formFilter.add(driverField);

        shiftField = new GenericDropDown<Shift>("shiftField", getShiftList(), new ShiftRenderer());
        shiftField.setOutputMarkupId(true);
        formFilter.add(shiftField);


        editForm = createForm("editModalForm", new PartTimeWeekEndEditModalFilter());
        editForm.setOutputMarkupId(true);
        add(editForm);

        editModal = new PartTimeWeekEndEditModal("editModal", "Editar preferencia", this);
        editModal.setOutputMarkupId(true);
        editForm.add(editModal);

        searchButton = new AjaxButton("searchButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);

                target.add(tableContainer);

                Driver driver = driverField.getModelObject();
                Shift shift = shiftField.getModelObject();

                List<Preference> preferences = getPreferences(driver, shift);

                preferencesDataView.setDefaultModelObject(preferences);
                preferencesDataView.iterator();
            }
        };

        formFilter.add(searchButton);


        preferencesGroup = createCheckGroupMaquinista("preferencesGroup");
        preferencesGroup.setOutputMarkupId(true);
        tableContainer.add(preferencesGroup);


        try {
            preferencesDataView = loadPreferences();
            preferencesDataView.setOutputMarkupId(true);

        } catch (PersistenceException e) {
            //TODO handle exception
            e.printStackTrace();
        }

        preferencesGroup.add(preferencesDataView);




        /*step2*/


    }

    private List<Preference> getPreferences(Driver driver, Shift shift) {

        List<Preference> preferences = new ArrayList<Preference>(0);

        try {

            if (null != driver && null == shift) {
                preferences.addAll(preferenceDao.getPreferences(driver));

            } else if (null == driver && null != shift) {
                preferences.addAll(preferenceDao.getPreferences(shift));

            } else if (null != driver) {
                preferenceDao.find(driver, shift);

            } else {
                //preferences.addAll(preferenceDao.getPreferences(ShiftType.PART_TIME_WEEKEND));
            }
        } catch (Exception e) {
            error(e.getMessage());
        }

        return preferences;

    }

    private List<Driver> getDriverList() {

        List<Driver> driverList = new ArrayList<Driver>(0);

        try {

            driverList.addAll(driverDao.getByType(ShiftType.FULL_TIME));

        } catch (PersistenceException ignored) {

        }
        return driverList;
    }

    private List<Shift> getShiftList() {

        List<Shift> shiftList = new ArrayList<Shift>(0);

        try {

            shiftList.addAll(shiftDao.getByType(ShiftType.FULL_TIME));

        } catch (PersistenceException ignored) {

        }
        return shiftList;
    }


    private CheckGroup<Driver> createCheckGroupMaquinista(String name) {

        CheckGroup<Driver> checkGroup = new CheckGroup<Driver>(name, new ArrayList<Driver>());
        checkGroup.setOutputMarkupId(true);

        return checkGroup;
    }


    private ListView<Preference> loadPreferences() throws PersistenceException {

        return new ListView<Preference>("preferencesDataView", new ArrayList<Preference>(0)) {
            @Override
            protected void populateItem(ListItem<Preference> item) {

                final Preference preference = item.getModelObject();
                Shift shift = preference.getShift();
                Driver driver = preference.getDriver();

                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");

                item.add(new Label("maquinsta", driver.getName()));
                item.add(new Label("turno", shift.getDescription()));

                item.add(new Label("preferencia", preference.getValue()));

                item.add(new Label("hrInicio", shift.getStartTime()));
                Station startStation = shift.getStartStation();
                item.add(new Label("estinicio", null == startStation ? "" : startStation.getDescription()));

                item.add(new Label("hrFin", shift.getEndTime()));
                Station endStation = shift.getEndStation();
                item.add(new Label("estFin", null == endStation ? "" : endStation.getDescription()));

                item.add(new Label("HrsTrabajo", formatter.format(shift.getWorkingHours())));
                item.add(new Label("HrsDescanso", formatter.format(shift.getRestingHours())));

                DayTime jornada = shift.getDayTime();
                item.add(new Label("jornada", null == jornada ? "" : jornada.getDescription()));

                item.add(getAjaxEditButtonComponents(preference));


            }


        };

    }


    @Override
    protected String getPageTitle() {
        return "Ingresar Preferencias Part-time Fin de Semana";
    }

    private AjaxLink<String> getAjaxEditButtonComponents(final Preference object) {
        return new AjaxLink<String>("edit") {

            @Override
            public void onClick(AjaxRequestTarget target) {

                target.add(feedbackPanel);
                target.add(editModal);
                target.add(tableContainer);

                editModal.setValuesToModal(target, object);

                editModal.show(true);
                editModal.setPreference(object);
                editModal.setFeedbackPanel(feedbackPanel);

                clearFeedBackPanel(target);
            }
        };
    }


}
