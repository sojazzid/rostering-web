package com.rostering.web.components.dropdown;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

import java.util.List;

public class GenericDropDown<T> extends DropDownChoice<T> {

    public GenericDropDown(String id, List<T> list, IChoiceRenderer<T> choiceRenderer) {

        super(id, list, choiceRenderer);

    }

}
