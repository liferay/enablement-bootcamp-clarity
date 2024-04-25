package com.liferay.enablement.bootcamp.clarity.configuration.admin.display;

import com.liferay.configuration.admin.constants.ConfigurationAdminPortletKeys;
import com.liferay.configuration.admin.display.ConfigurationScreen;
import com.liferay.enablement.bootcamp.clarity.configuration.settings.ClaritySiteInitializerSystemConfiguration;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.language.Language;
import com.liferay.site.initializer.task.api.SiteInitializerTask;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ClarityThemePortalSettingsConfigurationScreenContributor.java
 * <p>
 * ConfigurationScreen contributor service that is used to render a custom configuration screen for the Archetype Theme site
 * initializer task settings.
 * </p>
 *
 * @author andrew jardine | liferay inc.
 */
@Component(
        immediate = true,
        service = ConfigurationScreen.class
)
public class ClarityThemePortalSettingsConfigurationScreenContributor implements ConfigurationScreen
{

    private BundleContext _bundleContext;

    private static final String _CATEGORY_ICON = "emoji";

    private static final String _CATEGORY_KEY = "clarity-site-initializer";

    private static final String _CATEGORY_SECTION = "liferay-enablement-bootcamp";

    @Reference
    private Language _language;

    @Reference
    private ConfigurationProvider _configurationProvider;

    @Reference(target = "(osgi.web.symbolicname=com.liferay.enablement.bootcamp.clarity.site.initializer)")
    private ServletContext _servletContext;

    /**
     * Gets the key to reference the Configuration screen.
     *
     * @return String with the configuration category key
     */
    @Override
    public String getCategoryKey()
    {
        return "clarity-site-initializer";
    }

    /**
     * Gets the configuration screen category
     *
     * @return String with the configuration screen category
     */
    @Override
    public String getKey()
    {
        return "clarity";
    }

    /**
     * Returns the name of the category.
     *
     * @param locale {@link Locale} with the language requirements to be used for the label
     * @return String with the name of the category with the settings
     */
    @Override
    public String getName(Locale locale)
    {
        return _language.get(locale, "clarity-theme");
    }

    /**
     * Return the scope for the setting.
     *
     * @return String with the configuration scope
     */
    @Override
    public String getScope()
    {
        return ExtendedObjectClassDefinition.Scope.SYSTEM.toString();
    }

    /**
     * Handler for the RENDER phase of the custom configuration screen.
     *
     * @param httpServletRequest  {@link HttpServletRequest}
     * @param httpServletResponse {@link HttpServletResponse}
     * @throws IOException
     */
    @Override
    public void render(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException
    {
        try
        {
            RequestDispatcher requestDispatcher = _servletContext.getRequestDispatcher(getJspPath());

            // get the list of available options
            ServiceTrackerList<SiteInitializerTask> serviceTrackerList = ServiceTrackerListFactory.open(_bundleContext, SiteInitializerTask.class);
            ConcurrentLinkedQueue taskList = new ConcurrentLinkedQueue<>();
            serviceTrackerList.iterator().forEachRemaining(taskList::add);

            // get the items that are currently configured
            ClaritySiteInitializerSystemConfiguration currentConfiguration = _configurationProvider.getSystemConfiguration(ClaritySiteInitializerSystemConfiguration.class);

            List<String> currentConfigurationTaskList = new LinkedList<>();

            for ( String s : currentConfiguration.taskList() )
                currentConfigurationTaskList.add(s);

            // pass params to UI
            httpServletRequest.setAttribute("enabled", currentConfiguration.enabled());
            httpServletRequest.setAttribute("taskList", taskList);
            httpServletRequest.setAttribute("currentConfigurationTaskList", currentConfigurationTaskList);
            httpServletRequest.setAttribute("systemSettingsPortlet", ConfigurationAdminPortletKeys.SYSTEM_SETTINGS);


            requestDispatcher.include(httpServletRequest, httpServletResponse);
        }
        catch ( Exception exception )
        {
            throw new IOException(
                    "Unable to render " + getJspPath(), exception);
        }
    }

    /**
     * Path to the JSP used to render the customs view.
     *
     * @return String with the path + filename
     */
    public String getJspPath()
    {
        return "/configuration.jsp";
    }

    /**
     * Runs when the component is activated.
     *
     * @param bundleContext {@link BundleContext} with details for the bundle this service resides in
     * @param properties    {@link Map} of properties from the component definition
     */
    @Activate
    public void activate(BundleContext bundleContext, Map<String, String> properties)
    {
        _bundleContext = bundleContext;
    }
}


