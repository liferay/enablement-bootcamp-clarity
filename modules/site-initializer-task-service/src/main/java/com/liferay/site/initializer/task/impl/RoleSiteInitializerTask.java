package com.liferay.site.initializer.task.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.NoSuchRoleException;
import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ImageConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
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
                "data.type=Roles",
                "data.format=json",
                "entity.model=com.liferay.portal.kernel.model.Role"
        },
        service = SiteInitializerTask.class
)
public class RoleSiteInitializerTask extends BaseSiteInitializerTask implements SiteInitializerTask {

    private static final Log _log = LogFactoryUtil.getLog(RoleSiteInitializerTask.class);

    public static final String NAME = "Roles";

    private static final String FILENAME = "roles.json";


    @Reference
    private JSONFactory _jsonFactory;

    @Reference
    private RoleLocalService _roleLocalService;

    /**
     * @param groupId
     * @param bundleContext
     * @param context
     * @throws Exception
     */
    @Override
    public void execute(long groupId, BundleContext bundleContext, Map<String, Object> context) throws Exception {

        ServiceContext serviceContext = (ServiceContext) context.get(ContextKeys.SERVICE_CONTEXT);

        String roleFileContents = getResourceFileContents(bundleContext, GetterUtil.getString(context.get(ContextKeys.DEPENDENCIES_PATH)), FILENAME);

        Role role;

        //data was found -- process user list
        if (Validator.isNotNull(roleFileContents)) {

            JSONArray rolesJSONArray = _jsonFactory.createJSONArray(roleFileContents);
            JSONObject roleData = null;

            for (int i = 0; i < rolesJSONArray.length(); ++i) {
                try {
                    roleData = rolesJSONArray.getJSONObject(i);

                    // check if role exists
                    role = _roleLocalService.fetchRole(serviceContext.getCompanyId(), roleData.getString("name"));

                    if (Validator.isNull(role)) {
                        role = _addRole(roleData, context);

                        if ( _log.isInfoEnabled())
                            _log.info(MessageFormat.format("ROLE added to site, name => {0}", role.getName()));
                    }

                } catch (Exception e) {

                    if (_log.isWarnEnabled()) {

                        if (Validator.isNotNull(roleData)) {
                            _log.warn(MessageFormat.format("unexpected error occurred while processing role: {0} :: error => {1}",
                                    roleData.getString("name"),
                                    e.getMessage()));
                        } else {
                            _log.warn(MessageFormat.format("unexpected error occurred while processing user :: error => {0}",
                                    e.getMessage()));
                        }

                        if (_log.isDebugEnabled())
                            _log.debug(e);
                    }
                }
            }
        } else {
            if (_log.isInfoEnabled())
                _log.info("no resources found for roles.");
        }
    }

    /**
     * @param roleData
     * @param context
     * @return
     */
    private Role _addRole(JSONObject roleData, Map<String, Object> context) throws Exception {

        Role role = null;

        ServiceContext serviceContext = (ServiceContext) context.get(ContextKeys.SERVICE_CONTEXT);

        long creatorUserId = GetterUtil.getLong(context.get(ContextKeys.CREATOR_USER_ID));
        String className = null;
        long classPK = 0L;
        String name = roleData.getString("name");
        Map<Locale, String> titleMap = getTranslationMap(roleData.getJSONObject("name_i18n"));
        Map<Locale, String> descriptionMap = getTranslationMap(roleData.getJSONObject("description_i18n"));
        int type = GetterUtil.getInteger(roleData.getInt("type"));
        String subtype = null;

        role = _roleLocalService.addRole(creatorUserId,
                className,
                classPK,
                name,
                titleMap,
                descriptionMap,
                type,
                subtype,
                serviceContext);

        return role;
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return NAME;
    }
}