package com.liferay.enablement.bootcamp.clarity.site.initializer;

import com.liferay.enablement.bootcamp.clarity.configuration.settings.ClaritySiteInitializerSystemConfiguration;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.ThemeLocalService;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;
import com.liferay.site.initializer.task.api.SiteInitializerTask;
import com.liferay.site.initializer.task.constant.ContextKeys;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author andrew jardine | liferay inc
 */
@Component(
        configurationPid = "com.liferay.enablement.bootcamp.clarity.configuration.settings.ClaritySiteInitializerSystemConfiguration",
        property = {
                "site.initializer.key=" + ClaritySiteInitializer.KEY
        },
        service = SiteInitializer.class
)
public class ClaritySiteInitializer extends BundleSiteInitializer  {

    public static final String KEY = "clarity-site-initializer";

    private static final Log _log = LogFactoryUtil.getLog(ClaritySiteInitializer.class);

    private static final String _CLARITY_THEME_ID = "clarity_WAR_claritytheme";

    private static final String _DEPENDENCIES_PATH = "site-initializer/";

    private BundleContext _bundleContext;

    private ClaritySiteInitializerSystemConfiguration _configuration;

    @Reference
    private Language _language;

    /**
     *
     * @param groupId
     * @throws InitializationException
     */
    @Override
    public void initialize(long groupId) throws InitializationException
    {
        try {

            setBundleContext(_bundleContext);
            setBundle(_bundleContext.getBundle());


            super.initialize(groupId);


            if (_log.isInfoEnabled())
                _log.info("Starting Clarity Site Initializer ...");

            // create initializer context that is used to  carry meta data to the supporting services
            Map<String, Object> context = _buildContext(groupId);

            // run through the configured tasks, in sequence, to setup the demo
            _runTasks(groupId, context);

        }
        catch(Exception e)
        {
            _log.error(MessageFormat.format("error occurred while trying to use site initializer to generate demo :: error => {0}", e.getMessage()));

            if ( _log.isDebugEnabled())
                _log.debug(e);
        }
    }

    /**
     *
     * @param groupId
     * @param context
     * @throws Exception
     */
    private void _runTasks(long groupId, Map<String, Object> context) throws Exception
    {
        // get the list of initializer task items

        String[] tasks = _configuration.taskList();

        for( String task : tasks )
        {
            String filter = MessageFormat.format("(component.name={0})", task);

            ServiceTrackerList<SiteInitializerTask> taskTrackerList = ServiceTrackerListFactory.open(_bundleContext, SiteInitializerTask.class, filter);

            for(SiteInitializerTask t : taskTrackerList) {
                try {
                    if ( _log.isInfoEnabled())
                        _log.info(MessageFormat.format("executing initializer task: {0}", t.getClass().getName()));

                    t.execute(groupId, _bundleContext, context);
                }
                catch(Exception e) {
                    _log.error(MessageFormat.format("error occurred while executing task: {0} :: error => {1}", t.getClass().getName(), e.getMessage()));
                }
            }
        }
    }

    /**
     * Builds the context object with details about the site being initialized.
     *
     * @return {@link Map} containing meta data to be used by the initializer
     */
    private Map<String, Object> _buildContext(long groupId) throws InitializationException
    {
        Map<String, Object> context = new HashMap();

        try
        {
            Group group = _groupLocalService.getGroup(groupId);

            context.put(ContextKeys.COMPANY_ID, group.getCompanyId());
            context.put(ContextKeys.GROUP_ID, groupId);
            context.put(ContextKeys.GROUP, group);
            context.put(ContextKeys.CREATOR_USER_ID, group.getCreatorUserId());
            context.put(ContextKeys.SERVICE_CONTEXT, ServiceContextThreadLocal.getServiceContext());
            context.put(ContextKeys.DEPENDENCIES_PATH, _DEPENDENCIES_PATH);

        }
        catch ( Exception e )
        {
            throw new InitializationException(e);
        }

        return context;
    }

    /**
     * Gets the description for the site initializer.
     *
     * @param locale {@link Locale} with the language to be used when looking up the description
     * @return String with the description for the initializer
     */
    @Override
    public String getDescription(Locale locale)
    {
        // TODO read from the bnd
        return "Clarity Description";
    }

    /**
     * Gets the key used to reference the theme.
     *
     * @return String with the key for the theme
     */
    @Override
    public String getKey()
    {
        return KEY;
    }

    /**
     * Get the name of the site initializer.
     *
     * @param locale {@link Locale} with the language to be used when looking up the name
     * @return String with the name of the initializer
     */
    @Override
    public String getName(Locale locale)
    {
        // TODO read from bnd
        return "Clarity";
    }

    /**
     * Gets the thumbnail image representing a screenshot of the site initializer.
     *
     * @return String with the url to the thumbnail image
     */
    @Override
    public String getThumbnailSrc()
    {
        return _servletContext.getContextPath() + "/images/thumbnail.png";
    }

    /**
     * Checks whether or not the site initializer is active (available for use).
     *
     * @param companyId long with the unique identifier for the company
     * @return Boolean value of true if it is available, otherwise false is returned
     */
    @Override
    public boolean isActive(long companyId)
    {
        if (Validator.isNotNull(_configuration) && _configuration.enabled())
            return Boolean.TRUE;

        return Boolean.FALSE;
    }

    /**
     * Activation method that is run when the service component is activated
     * @param bundleContext {@link BundleContext}
     * @param properties {@link Map} with properties from the component definition
     */
    @Activate
    public void activate(BundleContext bundleContext, Map<String, Object> properties) {
        this._bundleContext = bundleContext;

        _configuration = ConfigurableUtil.createConfigurable(ClaritySiteInitializerSystemConfiguration.class, properties);
    }

}