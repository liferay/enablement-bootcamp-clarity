package com.liferay.site.initializer.task;

import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;
import com.liferay.site.initializer.task.api.SiteInitializerTask;

import java.util.Locale;
import java.util.Map;

/**
 * @author andrew jardine | liferay inc.
 */
public abstract class BaseSiteInitializerTask implements SiteInitializerTask {

    @Override
    public abstract void execute(long groupId, Map<String, Object> parameters);
}
