package com.rostering.web.components.dropdown.renderer;

import cl.upla.memoria.sam.model.Driver;
import cl.upla.memoria.sam.model.Shift;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class ShiftRenderer implements IChoiceRenderer<Shift> {
    @Override
    public Object getDisplayValue(Shift object) {
        return object.getDescription();
    }

    @Override
    public String getIdValue(Shift object, int index) {
        return String.valueOf(object.getId());
    }
}
