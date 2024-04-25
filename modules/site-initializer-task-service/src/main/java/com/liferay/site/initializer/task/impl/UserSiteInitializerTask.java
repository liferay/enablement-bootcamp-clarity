package com.liferay.site.initializer.task.impl;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.site.initializer.task.api.SiteInitializerTask;
import com.liferay.site.initializer.task.constant.ContextKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Map;

/**
 * @author andrew jardine | liferay
 */
@Component(
        property = {
                "data.type=Users",
                "data.format=json",
                "entity.model=com.liferay.portal.kernel.model.User"
        },
        service = SiteInitializerTask.class
)
public class UserSiteInitializerTask implements SiteInitializerTask {

    private static final Log _log = LogFactoryUtil.getLog(UserSiteInitializerTask.class);

    @Reference
    private Language _language;

    @Reference
    private JSONFactory _jsonFactory;

    @Override
    public void execute(long groupId, Map<String, Object> context) throws Exception {

        String users = GetterUtil.getString(context.get(ContextKeys.USERS));

        JSONArray usersJSONArray = _jsonFactory.createJSONArray(users);

        JSONObject user;

        for ( int i = 0; i < usersJSONArray.length(); ++i) {
            user = usersJSONArray.getJSONObject(i);

            _log.info("email => " + user.getString("email"));
        }

        _log.info(users);

        // import users here
    }

    @Override
    public String getName() {
        return "Users";
    }
}