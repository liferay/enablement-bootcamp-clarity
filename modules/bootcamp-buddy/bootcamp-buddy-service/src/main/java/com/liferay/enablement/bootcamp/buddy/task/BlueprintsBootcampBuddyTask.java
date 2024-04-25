package com.liferay.enablement.bootcamp.buddy.task;

import com.liferay.enablement.bootcamp.buddy.api.BootcampBuddyTask;
import com.liferay.enablement.bootcamp.buddy.constant.LanguageKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

@Component(
        property = {
                "bootcamp.buddy.name=Search Blueprints",
                "bootcamp.buddy.description=Adds the search blueprints",
                "bootcamp.buddy.entity=",
                "service.ranking:Integer=500"
        },
        service = BootcampBuddyTask.class
)
public class BlueprintsBootcampBuddyTask extends BaseBootcampBuddyTask implements BootcampBuddyTask {

    private static final Log _log = LogFactoryUtil.getLog(BlueprintsBootcampBuddyTask.class);

    private Map<String, Object> _properties;


    /**
     * Gets the name of the task.
     *
     * @param locale {@link Locale} representing the language to use when fetching the name
     * @return String with the task name in the specified locale
     */
    public String name(Locale locale) {
        return getText(LanguageKeys.BOOTCAMP_BUDDY_NAME, locale, _properties);
    }

    /**
     * Gets the description of the task.
     *
     * @param locale {@link Locale} representing the language to use when fetching the description
     * @return String with the task description in the specified locale
     */
    public String description(Locale locale) {
        return getText(LanguageKeys.BOOTCAMP_BUDDY_DESCRIPTION, locale, _properties);
    }

    /**
     * Gets the name of the entity the task is using.
     *
     * @return String with the entity class name in the specified locale
     */
    @Override
    public String entity() {
        return GetterUtil.getString(_properties.get(LanguageKeys.BOOTCAMP_BUDDY_ENTITY));
    }

    /**
     * Main entry point for the task execution.
     */
    @Override
    public void run(ServiceContext serviceContext) throws Exception {

        // TODO read collections files and add to the site

    }

    /**
     * Activation method that is run when the service component is started.
     *
     * @param properties {@link Map} of properties specified for the component
     */
    @Activate
    public void activate(Map<String, Object> properties) {
        if (_log.isDebugEnabled())
            _log.debug(MessageFormat.format("activating service component => {0}", this.getClass().getName()));

        _properties = properties;
    }
}
