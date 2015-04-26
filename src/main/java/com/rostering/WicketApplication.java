package com.rostering;

import com.rostering.common.SecureAuthenticatedWebSession;
import com.rostering.web.pages.fulltime.FullTimePage;
import com.rostering.web.pages.preferences.fulltime.FullPreferencesPage;
import com.rostering.web.pages.preferences.weekday.PartTimeWeekDayPreferencesPage;
import com.rostering.web.pages.preferences.weekend.PartTimeWeekEndPreferencesPage;
import com.rostering.web.pages.weekday.PartTimeWeekDayPage;
import com.rostering.web.pages.weekend.PartTimeWeekEndPage;
import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.settings.BootstrapSettings;
import org.apache.wicket.Page;
import org.apache.wicket.application.ComponentInstantiationListenerCollection;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.settings.IMarkupSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;


public class WicketApplication extends AuthenticatedWebApplication {

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return SecureAuthenticatedWebSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return FullTimePage.class;
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return FullTimePage.class;
    }

    @Override
    public void init() {

        super.init();

        final ComponentInstantiationListenerCollection componentInstantiationListeners = getComponentInstantiationListeners();

        componentInstantiationListeners.add(new SpringComponentInjector(this));

        BootstrapSettings settings = new BootstrapSettings();

        settings.minify(false);

        settings.useResponsiveCss();

        Bootstrap.install(this, settings);

        final IMarkupSettings markupSettings = getMarkupSettings();

        markupSettings.setStripWicketTags(true);

        mountPage("/fulltime", FullTimePage.class);
        mountPage("/parttimewe", PartTimeWeekEndPage.class);
        mountPage("/parttimewd", PartTimeWeekDayPage.class);
        mountPage("/fullpreferences", FullPreferencesPage.class);
        mountPage("/wepreferences", PartTimeWeekEndPreferencesPage.class);
        mountPage("/wdpreferences", PartTimeWeekDayPreferencesPage.class);


    }
}
