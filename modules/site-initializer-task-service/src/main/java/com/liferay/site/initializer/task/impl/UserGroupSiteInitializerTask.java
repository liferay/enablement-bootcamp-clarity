package com.liferay.site.initializer.task.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ImageConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.site.initializer.task.BaseSiteInitializerTask;
import com.liferay.site.initializer.task.api.SiteInitializerTask;
import com.liferay.site.initializer.task.constant.ContextKeys;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * @author andrew jardine | liferay
 */
@Component(
        property = {
                "data.type=UserGroup",
                "data.format=json",
                "entity.model=com.liferay.portal.kernel.model.UserGroup"
        },
        service = SiteInitializerTask.class
)
public class UserGroupSiteInitializerTask extends BaseSiteInitializerTask implements SiteInitializerTask {

    private static final Log _log = LogFactoryUtil.getLog(UserGroupSiteInitializerTask.class);

    public static final String NAME = "Users Groups";

    private static final String FILENAME = "user-groups.json";

    @Reference
    private Language _language;

    @Reference
    private JSONFactory _jsonFactory;

    @Reference
    private UserLocalService _userLocalService;

    @Reference
    private UserGroupLocalService _userGroupLocalService;

    /**
     * @param groupId
     * @param bundleContext
     * @param context
     * @throws Exception
     */
    @Override
    public void execute(long groupId, BundleContext bundleContext, Map<String, Object> context) throws Exception {

        ServiceContext serviceContext = (ServiceContext) context.get(ContextKeys.SERVICE_CONTEXT);

        String userGroupsFileContents = getResourceFileContents(bundleContext, GetterUtil.getString(context.get(ContextKeys.DEPENDENCIES_PATH)), FILENAME);

        UserGroup userGroup;

        if (Validator.isNotNull(userGroupsFileContents)) {

            JSONArray usersJSONArray = _jsonFactory.createJSONArray(userGroupsFileContents);
            JSONObject userGroupData = null;

            for (int i = 0; i < usersJSONArray.length(); ++i) {
                try {
                    userGroupData = usersJSONArray.getJSONObject(i);

                    userGroup = _userGroupLocalService.addOrUpdateUserGroup(
                            userGroupData.getString("externalReferenceCode"),
                            GetterUtil.getLong(context.get(ContextKeys.CREATOR_USER_ID)),
                            GetterUtil.getLong(context.get(ContextKeys.COMPANY_ID)),
                            userGroupData.getString("name"),
                            userGroupData.getString("description"),
                            serviceContext
                    );

                    if ( _log.isInfoEnabled())
                        _log.info(MessageFormat.format("USER GROUP added to system, name => {0}", userGroup.getName()));

                } catch (Exception e) {

                    if (_log.isWarnEnabled()) {

                        if (Validator.isNotNull(userGroupData)) {
                            _log.warn(MessageFormat.format("unexpected error occurred while processing user group: {0} :: error => {1}",
                                    userGroupData.getString("name"),
                                    e.getMessage()));
                        } else {
                            _log.warn(MessageFormat.format("unexpected error occurred while processing user group :: error => {0}",
                                    e.getMessage()));
                        }

                        if (_log.isDebugEnabled())
                            _log.debug(e);
                    }
                }
            }
        } else {
            if (_log.isInfoEnabled())
                _log.info("no resources found for user groups.");
        }
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return NAME;
    }
}