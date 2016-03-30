package com.rostering.web.pages.preferences.fulltime.modal;

import com.rostering.dao.PreferenceDao;
import com.rostering.dao.exception.PersistenceException;
import com.rostering.model.Preference;
import com.rostering.web.components.ModalButton;
import com.rostering.web.components.dropdown.GenericDropDown;
import com.rostering.web.pages.preferences.fulltime.FullPreferencesPage;
import de.agilecoders.wicket.markup.html.bootstrap.dialog.Modal;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class FullEditModal extends Modal {

    @SpringBean(name = "preferenceDao")
    PreferenceDao preferenceDao;


    private FeedbackPanel feedbackPanel;
    private Preference preference;

    private Label driverField;
    private Label shiftField;

    private GenericDropDown<Integer> preferenceField;
    private final FullPreferencesPage fullPreferencesPage;


    public FullEditModal(String markupId, String title, FullPreferencesPage fullPreferencesPage) {
        super(markupId);

        this.fullPreferencesPage = fullPreferencesPage;

        setOutputMarkupId(true);

        setUseCloseHandler(true);
        header(new Model<String>(title), true);

        addCloseButton();
        addButton(getButton("Editar"));


        driverField = new Label("driverField");
        driverField.setOutputMarkupId(true);
        add(driverField);

        shiftField = new Label("shiftField");
        shiftField.setOutputMarkupId(true);
        add(shiftField);


        preferenceField = new GenericDropDown<Integer>("preferenceField", getPreferenceList(), new IChoiceRenderer<Integer>() {
            @Override
            public Object getDisplayValue(Integer object) {
                return object;
            }

            @Override
            public String getIdValue(Integer object, int index) {
                return String.valueOf(object);
            }
        });
        preferenceField.setOutputMarkupId(true);
        add(preferenceField);
    }

    private List<Integer> getPreferenceList() {

        List<Integer> integers = new ArrayList<Integer>(10);

        for (int i = 1; i <= 10; i++) {
            integers.add(i);
        }

        return integers;
    }

    public ModalButton getButton(String label) {
        return new ModalButton("button", label) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {


                target.add(feedbackPanel);

                Integer selectedPreference = preferenceField.getModelObject();

                if (null != selectedPreference) {
                    preference.setValue(selectedPreference);

                    try {
                        preferenceDao.update(preference);
                        target.add(fullPreferencesPage);
                    } catch (PersistenceException e) {
                        error(e.getMessage());
                    }

                    close(target);
                }
            }

        };
    }


    public void setValuesToModal(AjaxRequestTarget target, Preference preference) {

        target.add(driverField);
        target.add(shiftField);
        target.add(preferenceField);

        driverField.setDefaultModelObject(preference.getDriver().getName());
        shiftField.setDefaultModelObject(preference.getShift().getDescription());
        preferenceField.setDefaultModelObject(preference.getValue());

    }

    private void close(AjaxRequestTarget target) {
        target.appendJavaScript("$('.modal-backdrop').fadeOut('slow');");
        show(false);
        appendCloseDialogJavaScript(target);
    }

    public void setFeedbackPanel(FeedbackPanel feedbackPanel) {
        this.feedbackPanel = feedbackPanel;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }
}
