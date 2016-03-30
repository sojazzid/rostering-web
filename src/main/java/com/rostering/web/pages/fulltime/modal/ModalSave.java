package com.rostering.web.pages.fulltime.modal;


import com.rostering.dao.AllocationDao;
import com.rostering.dao.exception.PersistenceException;
import com.rostering.model.Allocation;
import com.rostering.web.components.ModalButton;
import de.agilecoders.wicket.markup.html.bootstrap.dialog.Modal;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ModalSave extends Modal {

    @SpringBean(name = "allocationDao")
    AllocationDao allocationDao;

    private TextField<String> nameToSaveField;
    private List<Allocation> asignaciones;
    private FeedbackPanel feedbackPanel;

    public ModalSave(String markupId, String title) {
        super(markupId);
        setOutputMarkupId(true);

        nameToSaveField = new TextField<String>("nameToSaveField");
        nameToSaveField.setOutputMarkupId(true);
        add(nameToSaveField);

        header(new Model<String>(title), true);

        addButton(getButton("Guardar"));
    }

    public ModalButton getButton(String label) {
        return new ModalButton("button", label) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                target.add(nameToSaveField);
                target.add(feedbackPanel);
                String name = nameToSaveField.getModelObject();

                if (isValidListAsginaciones(asignaciones)) {

                    if (isValidName(name)) {

                        for (Allocation allocation : asignaciones) {

                            allocation.setName(name);
                        }

                        try {
                            allocationDao.save(asignaciones);
                        } catch (PersistenceException e) {
                            error("No se pudo guardar la asignación. " + e.getMessage());
                        }
                        success("Asignación guardada con éxito");
                    } else {
                        error("Nombre no válido");
                    }
                } else {
                    error("No hay asignaciones desponibles");
                }

                close(target);
            }

        };
    }

    private boolean isValidName(String name) {

        if (null != name) {
            if (!name.trim().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidListAsginaciones(List<Allocation> asignaciones) {

        if (null != asignaciones) {
            if (!asignaciones.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private void close(AjaxRequestTarget target) {
        target.appendJavaScript("$('.modal-backdrop').fadeOut('slow');");
        show(false);
        appendCloseDialogJavaScript(target);
    }

    public void setAsignaciones(List<Allocation> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public void setFeedbackPanel(FeedbackPanel feedbackPanel) {
        this.feedbackPanel = feedbackPanel;
    }
}
