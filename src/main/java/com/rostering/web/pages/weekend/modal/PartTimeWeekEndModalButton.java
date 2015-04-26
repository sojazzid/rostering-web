package com.rostering.web.pages.weekend.modal;

import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonType;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.model.Model;

public class PartTimeWeekEndModalButton extends AjaxSubmitLink {

    public PartTimeWeekEndModalButton(String id, String label) {

        super(id);
        setBody(new Model<String>(label));
        add(new AttributeModifier("class", "btn"));
        add(new ButtonBehavior(ButtonType.Default));
        add(new AttributeModifier("data-dismiss", "modal"));

    }


}
