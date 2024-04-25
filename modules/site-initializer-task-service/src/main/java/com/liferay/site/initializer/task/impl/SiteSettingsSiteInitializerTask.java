package com.liferay.site.initializer.task.impl;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.site.initializer.task.api.SiteInitializerTask;
import com.liferay.site.initializer.task.constant.ContextKeys;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

/**
 * @author andrew jardine | liferay
 */
@Component(
        property = {
                "data.type=SiteSettings",
                "data.format=json",
        },
        service = SiteInitializerTask.class
)
public class SiteSettingsSiteInitializerTask implements SiteInitializerTask {

    private static final Log _log = LogFactoryUtil.getLog(SiteSettingsSiteInitializerTask.class);

    @Override
    public void execute(long groupId, BundleContext bundleContext, Map<String, Object> context) {

        // add/persist site settings here
    }

    @Override
    public String getName() {
        return "Site Settings";
    }
}