package com.liferay.site.initializer.task.impl;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
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
import java.util.Map;

/**
 * @author andrew jardine | liferay
 */
@Component(
        property = {
                "data.type=Group",
                "data.type=User",
                "data.format=json",
                "entity.model=com.liferay.portal.kernel.model.Group",
                "entity.model=com.liferay.portal.kernel.model.Role"
        },
        service = SiteInitializerTask.class
)
public class SiteMembershipsSiteInitializerTask extends BaseSiteInitializerTask implements SiteInitializerTask {

    private static final Log _log = LogFactoryUtil.getLog(SiteMembershipsSiteInitializerTask.class);

    public static final String NAME = "Site Memberships";

    private static final String FILENAME = "site-membership.json";

    @Reference
    private JSONFactory _jsonFactory;

    @Reference
    private GroupLocalService _groupLocalService;

    @Reference
    UserLocalService _userLocalService;

    @Reference
    private UserGroupLocalService _userGroupLocalService;

    @Override
    public void execute(long groupId, BundleContext bundleContext, Map<String, Object> context) throws Exception {

        ServiceContext serviceContext = (ServiceContext) context.get(ContextKeys.SERVICE_CONTEXT);

        long companyId = GetterUtil.getLong(context.get(ContextKeys.COMPANY_ID));

        String siteMembershipFileContents = getResourceFileContents(bundleContext, GetterUtil.getString(context.get(ContextKeys.DEPENDENCIES_PATH)), FILENAME);

        if (Validator.isNotNull(siteMembershipFileContents)) {

            JSONArray membershipsJSONArray = _jsonFactory.createJSONArray(siteMembershipFileContents);
            JSONObject membershipData = null;

            for (int i = 0; i < membershipsJSONArray.length(); ++i) {
                try {

                    membershipData = membershipsJSONArray.getJSONObject(i);

                    String type = membershipData.getString("type");

                    boolean success = Boolean.FALSE;

                    switch (type) {
                        case "user":
                            User user = _userLocalService.getUserByEmailAddress(companyId, membershipData.getString("emailAddress"));

                            success = _userLocalService.addGroupUser(groupId, user.getUserId());

                            if ( success && _log.isInfoEnabled())
                                _log.info(MessageFormat.format("USER: {0} has been added to GROUP: {1}", user.getEmailAddress(), Long.toString(groupId)));
                            else {
                                if ( _log.isInfoEnabled())
                                    _log.info(MessageFormat.format("failed to add USER: {0} to GROUP: {1}", user.getEmailAddress(), Long.toString(groupId)));
                            }
                            break;

                        case "user-group":
                            String name = membershipData.getString("name");

                            UserGroup userGroup = _userGroupLocalService.getUserGroup(companyId, name);
                            success = _groupLocalService.addUserGroupGroup(userGroup.getUserGroupId(), groupId);

                            if ( success && _log.isInfoEnabled())
                                _log.info(MessageFormat.format("USER GROUP: {0} has been added to GROUP: {1}", name, Long.toString(groupId)));
                            else {
                                if ( _log.isInfoEnabled())
                                    _log.info(MessageFormat.format("failed to add USER GROUP: {0} to GROUP: {1}", name, Long.toString(groupId)));
                            }
                            break;

                        default:
                            if (_log.isWarnEnabled())
                                _log.warn(MessageFormat.format("unexpected type: {0} found in membership data", type));
                    }

                } catch (Exception e) {

                    if (_log.isWarnEnabled()) {

                        if (Validator.isNotNull(membershipData)) {
                            _log.warn(MessageFormat.format("unexpected error occurred while processing site membership: {0} :: error => {1}",
                                    membershipData.getString("email"),
                                    e.getMessage()));
                        } else {
                            _log.warn(MessageFormat.format("unexpected error occurred while processing site membership :: error => {0}",
                                    e.getMessage()));
                        }

                        if (_log.isDebugEnabled())
                            _log.debug(e);
                    }
                }
            }

        }  else {
            if (_log.isInfoEnabled())
                _log.info("no resources found for site memberships.");
        }

    }

    @Override
    public String getName() {
        return NAME;
    }
}