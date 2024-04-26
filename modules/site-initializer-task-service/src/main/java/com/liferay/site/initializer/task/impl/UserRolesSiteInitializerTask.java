package com.liferay.site.initializer.task.impl;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.*;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.site.initializer.task.BaseSiteInitializerTask;
import com.liferay.site.initializer.task.api.SiteInitializerTask;
import com.liferay.site.initializer.task.constant.ContextKeys;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

/**
 * @author andrew jardine | liferay
 */
@Component(
        property = {
                "data.type=User",
                "data.type=Roles",
                "data.format=json",
                "entity.model=com.liferay.portal.kernel.model.User",
                "entity.model=com.liferay.portal.kernel.model.Role"
        },
        service = SiteInitializerTask.class
)
public class UserRolesSiteInitializerTask extends BaseSiteInitializerTask implements SiteInitializerTask {

    private static final Log _log = LogFactoryUtil.getLog(UserRolesSiteInitializerTask.class);

    public static final String NAME = "User X Roles";

    private static final String FILENAME = "user-roles.json";

    @Reference
    private JSONFactory _jsonFactory;

    @Reference
    private UserLocalService _userLocalService;

    @Reference
    private RoleLocalService _roleLocalService;

    @Reference
    private UserGroupRoleLocalService _userGroupRoleLocalService;

    /**
     * @param groupId
     * @param bundleContext
     * @param context
     * @throws Exception
     */
    @Override
    public void execute(long groupId, BundleContext bundleContext, Map<String, Object> context) throws Exception {

        ServiceContext serviceContext = (ServiceContext) context.get(ContextKeys.SERVICE_CONTEXT);

        String userRolesFileContents = getResourceFileContents(bundleContext, GetterUtil.getString(context.get(ContextKeys.DEPENDENCIES_PATH)), FILENAME);

        //data was found -- process user list
        if (Validator.isNotNull(userRolesFileContents)) {

            JSONArray userRolesJSONArray = _jsonFactory.createJSONArray(userRolesFileContents);
            JSONObject userRoleData = null;

            for (int i = 0; i < userRolesJSONArray.length(); ++i) {
                try {
                    userRoleData = userRolesJSONArray.getJSONObject(i);

                    String email = GetterUtil.getString(userRoleData.getString("emailAddress"));
                    JSONArray roles = userRoleData.getJSONArray("roles");

                    for (int j = 0; j < roles.length(); ++j) {
                        _assignRole(email, GetterUtil.getString(roles.get(j)), context);
                    }

                } catch (Exception e) {

                    if (_log.isWarnEnabled()) {

                        if (Validator.isNotNull(userRoleData)) {
                            _log.warn(MessageFormat.format("unexpected error occurred while processing role assignment for user: {0} :: error => {1}",
                                    userRoleData.getString("emailAddress"),
                                    e.getMessage()));
                        } else {
                            _log.warn(MessageFormat.format("unexpected error occurred while processing role assignment for user :: error => {0}",
                                    e.getMessage()));
                        }

                        if (_log.isDebugEnabled())
                            _log.debug(e);
                    }
                }
            }
        } else {
            if (_log.isInfoEnabled())
                _log.info("no resources found for user role assignments.");
        }
    }

    /**
     * @param email
     * @param roleName
     * @param context
     * @return
     */
    private void _assignRole(String email, String roleName, Map<String, Object> context) throws Exception {

        long companyId = GetterUtil.getLong(context.get(ContextKeys.COMPANY_ID));
        long groupId = GetterUtil.getLong(context.get(ContextKeys.GROUP_ID));

        User user = _userLocalService.getUserByEmailAddress(companyId, email);
        Role role = _roleLocalService.fetchRole(companyId, roleName);

        if (Validator.isNotNull(role)) {

            switch(role.getType()) {

                case RoleConstants.TYPE_REGULAR:
                    _roleLocalService.addUserRole(user.getUserId(), role.getRoleId());
                    break;
                case RoleConstants.TYPE_SITE:
                    _userGroupRoleLocalService.addUserGroupRole(user.getUserId(), groupId, role.getRoleId());
                    break;
                default:
                    if (_log.isWarnEnabled())
                        _log.warn(MessageFormat.format("unsupported role type found for assignment: {0}", Integer.toString(role.getType())));
            }

            if (_log.isInfoEnabled())
                _log.info(MessageFormat.format("ROLE: {0} assigned to USER: {1}", role.getName(), user.getEmailAddress()));
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