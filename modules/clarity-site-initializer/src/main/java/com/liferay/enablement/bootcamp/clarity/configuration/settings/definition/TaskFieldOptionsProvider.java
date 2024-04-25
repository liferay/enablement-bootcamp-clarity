package com.liferay.enablement.bootcamp.clarity.configuration.settings.definition;

import com.liferay.configuration.admin.definition.ConfigurationFieldOptionsProvider;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.site.initializer.task.api.SiteInitializerTask;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author andrew jardine | liferay inc.
 */
@Component(
        immediate = true,
        property = {
                "configuration.field.name=task",
                "configuration.pid=com.liferay.cx.cos030.theme.archetype.site.initializer.internal.configuration.settings.ArchetypeThemeInitializerSystemConfiguration"
        },
        service = ConfigurationFieldOptionsProvider.class
)
public class TaskFieldOptionsProvider implements ConfigurationFieldOptionsProvider
{
    private BundleContext _bundleContext;

    /**
     * Build and returns the list of options to be rendered for the field.
     *
     * @return {@link List} of {@link Option}s available for configuration
     */
    @Override
    public List<Option> getOptions()
    {
        ServiceTrackerList<SiteInitializerTask> serviceTrackerTasksList = ServiceTrackerListFactory.open(_bundleContext, SiteInitializerTask.class);

        List<Option> optionList = new ArrayList<Option>();

        serviceTrackerTasksList.iterator().forEachRemaining( t -> {
            optionList.add(_toOption(t));
        });

        return optionList;
    }

    /**
     * Converts the {@link InitializerTask} into a field option.
     *
     * @param task {@link InitializerTask} being converted
     * @return {@link Option} for the field
     */
    private Option _toOption(SiteInitializerTask task)
    {
        return new Option()
        {

            @Override
            public String getLabel(Locale locale)
            {
                return task.getName();
            }

            @Override
            public String getValue()
            {
                return task.getClass().getCanonicalName();
            }

        };
    }
    /**
     * Runs when the component is activated.
     *
     * @param bundleContext {@link BundleContext}
     */
    @Activate
    public void activate(BundleContext bundleContext)
    {
        _bundleContext = bundleContext;
    }
}

