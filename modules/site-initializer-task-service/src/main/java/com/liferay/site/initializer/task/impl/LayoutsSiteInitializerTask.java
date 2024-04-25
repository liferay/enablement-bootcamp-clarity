package com.liferay.site.initializer.task.impl;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.site.initializer.task.api.SiteInitializerTask;
import com.liferay.site.initializer.task.constant.ContextKeys;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

/**
 * @author andrew jardine | liferay
 */
@Component(
        property = {
                "data.type=Layouts",
                "data.format=json",
                "entity.model=com.liferay.portal.kernel.model.Layout"
        },
        service = SiteInitializerTask.class
)
public class LayoutsSiteInitializerTask implements SiteInitializerTask {

    private static final Log _log = LogFactoryUtil.getLog(LayoutsSiteInitializerTask.class);

    @Override
    public void execute(long groupId, Map<String, Object> context) {

        JSONArray layouts = (JSONArray)context.get(ContextKeys.LAYOUTS);

        // create/import layouts here
    }

    @Override
    public String getName() {
        return "Pages (Layouts)";
    }
}