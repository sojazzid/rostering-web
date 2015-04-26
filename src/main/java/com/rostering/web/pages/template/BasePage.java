package com.rostering.web.pages.template;

import com.rostering.common.FormFilter;
import com.rostering.web.resurces.WebResources;
import de.agilecoders.wicket.Bootstrap;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.feedback.FeedbackMessages;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.resource.CssResourceReference;

import java.util.Locale;

public abstract class BasePage extends WebPage {

    //private static final AtomicLong LAST_TIME_MS = new AtomicLong();

    protected final FeedbackPanel feedbackPanel;
    protected final WebMarkupContainer loadingPanel;

    private final CssResourceReference consoleCss = new CssResourceReference(WebResources.class, "css/console-ui.css");
    private final CssResourceReference fontsCss = new CssResourceReference(WebResources.class, "css/font-awesome-ie7.min.css");
    //   @SpringBean(name = "dropDownLoadService")

    public BasePage() {

        loadingPanel = new WebMarkupContainer("loadingPanel");
        loadingPanel.setOutputMarkupId(true);
        add(loadingPanel);

        feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        initPage();
    }

    public void initPage() {
        add(new Label("pageTitle", getPageTitle()));
    }

    protected abstract String getPageTitle();


    @Override
    public void renderHead(IHeaderResponse response) {
        Bootstrap.renderHead(response);

        response.render(CssHeaderItem.forReference(fontsCss));
        response.render(CssHeaderItem.forReference(consoleCss));
    }

    @Override
    public Locale getLocale() {
        return new Locale("en");
    }

    public void clearFeedBackPanel(AjaxRequestTarget target) {

        target.add(feedbackPanel);

        FeedbackMessages feedbackMessages = getFeedbackMessages();
        feedbackMessages.clear();
    }

    public static Form<FormFilter> createForm(String id, FormFilter tableFormFilter) {

        CompoundPropertyModel<FormFilter> model = new CompoundPropertyModel<FormFilter>(tableFormFilter);

        Form<FormFilter> form = new Form<FormFilter>(id, model);
        form.setOutputMarkupId(true);

        return form;

    }

     public void showAlert(AjaxRequestTarget target, String message) {
        target.appendJavaScript("alert('" + message + "')");
    }


}
