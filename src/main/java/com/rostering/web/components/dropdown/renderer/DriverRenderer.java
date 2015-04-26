package com.rostering.web.components.dropdown.renderer;

import cl.upla.memoria.sam.model.Driver;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class DriverRenderer implements IChoiceRenderer<Driver> {
    @Override
    public Object getDisplayValue(Driver object) {
        return object.getName();
    }

    @Override
    public String getIdValue(Driver object, int index) {
        return String.valueOf(object.getId());
    }
}
