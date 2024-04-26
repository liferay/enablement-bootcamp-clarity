package com.liferay.site.initializer.task.impl;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
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

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author andrew jardine | liferay
 */
@Component(
        property = {
                "data.type=User",
                "data.type=UserGroup",
                "data.format=json",
                "entity.model=com.liferay.portal.kernel.model.User",
                "entity.model=com.liferay.portal.kernel.model.UserGroup",
        },
        service = SiteInitializerTask.class
)
public class UserUserGroupSiteInitializerTask extends BaseSiteInitializerTask implements SiteInitializerTask {

    private static final Log _log = LogFactoryUtil.getLog(UserUserGroupSiteInitializerTask.class);

    public static final String NAME = "User X Users Groups";

    private static final String FILENAME = "user-usergroups.json";

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

        String userUserGroupsFileContents = getResourceFileContents(bundleContext, GetterUtil.getString(context.get(ContextKeys.DEPENDENCIES_PATH)), FILENAME);

        UserGroup userGroup;
        User user;

        if (Validator.isNotNull(userUserGroupsFileContents)) {

            JSONArray userUserGroupJSONArray = _jsonFactory.createJSONArray(userUserGroupsFileContents);
            JSONObject userUserGroupData = null;

            long companyId = GetterUtil.getLong(context.get(ContextKeys.COMPANY_ID));

            for (int i = 0; i < userUserGroupJSONArray.length(); ++i) {
                try {
                    userUserGroupData = userUserGroupJSONArray.getJSONObject(i);

                    userGroup = _userGroupLocalService.fetchUserGroup(
                            companyId,
                            userUserGroupData.getString("name")
                    );

                    JSONArray members = userUserGroupData.getJSONArray("members");

                    if ( Validator.isNotNull(members)) {
                        for (int j = 0; j < members.length(); j++) {
                            user = _userLocalService.getUserByEmailAddress(companyId, GetterUtil.getString(members.get(j)));

                            boolean success = _userGroupLocalService.addUserUserGroup(user.getUserId(), userGroup.getUserGroupId());

                            if ( success ) {
                                if ( _log.isInfoEnabled())
                                    _log.info(MessageFormat.format("USER: {0} has been added to USER GROUP: {1}", user.getEmailAddress(), userGroup.getName()));
                            }
                            else {
                                if ( _log.isWarnEnabled())
                                    _log.warn(MessageFormat.format("failed to add USER: {0} to USER GROUP: {1}", user.getEmailAddress(), userGroup.getName()));
                            }
                        }
                    }

                } catch (Exception e) {

                    if (_log.isWarnEnabled()) {

                        if (Validator.isNotNull(userUserGroupData)) {
                            _log.warn(MessageFormat.format("unexpected error occurred while processing member(s) for user group: {0} :: error => {1}",
                                    userUserGroupData.getString("name"),
                                    e.getMessage()));
                        } else {
                            _log.warn(MessageFormat.format("unexpected error occurred while processing member(s) for user group :: error => {0}",
                                    e.getMessage()));
                        }

                        if (_log.isDebugEnabled())
                            _log.debug(e);
                    }
                }
            }
        } else {
            if (_log.isInfoEnabled())
                _log.info("no resources found for user groups membership.");
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