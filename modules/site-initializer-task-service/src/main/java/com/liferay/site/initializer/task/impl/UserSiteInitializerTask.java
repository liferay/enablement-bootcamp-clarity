package com.liferay.site.initializer.task.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ImageConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.persistence.UserUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.site.initializer.task.BaseSiteInitializerTask;
import com.liferay.site.initializer.task.api.SiteInitializerTask;
import com.liferay.site.initializer.task.constant.ContextKeys;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.InputStream;
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
                "data.type=Users",
                "data.format=json",
                "entity.model=com.liferay.portal.kernel.model.User"
        },
        service = SiteInitializerTask.class
)
public class UserSiteInitializerTask extends BaseSiteInitializerTask implements SiteInitializerTask {

    private static final Log _log = LogFactoryUtil.getLog(UserSiteInitializerTask.class);

    public static final String NAME = "Users";

    private static final String DATA_LOCATION = "site-initializer/users";

    private static final String FILENAME = "users.json";

    private static final String PORTRAIT_IMAGES_FOLDER = DATA_LOCATION + "/portraits/";

    @Reference
    private Language _language;

    @Reference
    private JSONFactory _jsonFactory;

    @Reference
    private UserLocalService _userLocalService;

    /**
     * @param groupId
     * @param bundleContext
     * @param context
     * @throws Exception
     */
    @Override
    public void execute(long groupId, BundleContext bundleContext, Map<String, Object> context) throws Exception {

        ServiceContext serviceContext = (ServiceContext) context.get(ContextKeys.SERVICE_CONTEXT);

        String userFileContents = getResourceFileContents(bundleContext, DATA_LOCATION, FILENAME);

        User user;

        //data was found -- process user list
        if (Validator.isNotNull(userFileContents)) {

            JSONArray usersJSONArray = _jsonFactory.createJSONArray(userFileContents);
            JSONObject userData = null;

            for (int i = 0; i < usersJSONArray.length(); ++i) {
                try {
                    userData = usersJSONArray.getJSONObject(i);

                    // check if user exists
                    user = _userLocalService.getUserByEmailAddress(serviceContext.getCompanyId(), userData.getString("emailAddress"));

                } catch (NoSuchUserException nsue) {
                    // create user
                    user = _addUser(userData, context);

                    // set portrait (if found)
                    byte[] image = _getPotrait(bundleContext, userData);

                    if (Validator.isNotNull(image))
                        _userLocalService.updatePortrait(user.getUserId(), image);

                    if (_log.isInfoEnabled())
                        _log.info(MessageFormat.format("USER added to system, email => {0}", user.getEmailAddress()));
                } catch (Exception e) {

                    if (_log.isWarnEnabled()) {

                        if (Validator.isNotNull(userData)) {
                            _log.warn(MessageFormat.format("unexpected error occurred while processing user: {0} :: error => {1}",
                                    userData.getString("email"),
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
                _log.info("no resources found for users.");
        }
    }

    /**
     * @param userData
     * @param context
     * @return
     */
    private User _addUser(JSONObject userData, Map<String, Object> context) {

        User user = null;

        try {
            ServiceContext serviceContext = (ServiceContext) context.get(ContextKeys.SERVICE_CONTEXT);

            long creatorUserId = GetterUtil.getLong(context.get(ContextKeys.CREATOR_USER_ID));
            long companyId = GetterUtil.getLong(context.get(ContextKeys.COMPANY_ID));
            boolean autoPassword = Boolean.FALSE;
            String password1 = "learn";
            String password2 = "learn";
            boolean autoScreenName = Boolean.FALSE;
            String screenName = userData.getString("screenName");
            String emailAddress = userData.getString("emailAddress");
            Locale locale = serviceContext.getLocale();
            String firstName = userData.getString("firstName");
            String middleName = userData.getString("middleName");
            String lastName = userData.getString("lastName");
            long prefixListTypeId = 0L;
            long suffixListTypeId = 0L;
            boolean male = (userData.getString("gender").equals("M") ? Boolean.TRUE : Boolean.FALSE);
            int birthdayMonth = 0;
            int birthdayDay = 1;
            int birthdayYear = 1970;
            String jobTitle = userData.getString("jobTitle");
            int type = UserConstants.TYPE_REGULAR;
            long[] groupIds = new long[]{};
            long[] organizationIds = new long[]{};
            long[] roleIds = new long[]{};
            long[] userGroupIds = new long[]{};
            boolean sendEmail = Boolean.FALSE;

            user = _userLocalService.addUserWithWorkflow(
                    creatorUserId,
                    companyId,
                    autoPassword,
                    password1,
                    password2,
                    autoScreenName,
                    screenName,
                    emailAddress,
                    locale,
                    firstName,
                    middleName,
                    lastName,
                    prefixListTypeId,
                    suffixListTypeId,
                    male,
                    birthdayMonth,
                    birthdayDay,
                    birthdayYear,
                    jobTitle,
                    type,
                    groupIds,
                    organizationIds,
                    roleIds,
                    userGroupIds,
                    sendEmail,
                    serviceContext);
        } catch (Exception e) {
            _log.error(MessageFormat.format("error occurred while trying to add user: {0} :: error => {1}",
                    userData.getString("email"),
                    e.getMessage()));
        }

        return user;
    }

    /**
     * @param bundleContext
     * @param user
     * @return
     */
    private byte[] _getPotrait(BundleContext bundleContext, JSONObject user) {

        String filename = StringBundler
                .concat(user.getString("firstName"))
                .concat(StringPool.DASH)
                .concat(user.getString("lastName"))
                .concat(StringPool.PERIOD)
                .concat(ImageConstants.TYPE_PNG)
                .trim()
                .toLowerCase();

        try {
            Enumeration<URL> resources = bundleContext.getBundle().findEntries(PORTRAIT_IMAGES_FOLDER, filename, true);

            if (Validator.isNotNull(resources)) {

                URL element = resources.nextElement();
                byte[] image = element.openStream().readAllBytes();

                return image;

            } else {
                _log.info(MessageFormat.format("no image portrait for user: {0}", filename));
            }
        } catch (Exception e) {
            _log.error(MessageFormat.format("error occurred while trying to read portrait image: {0} :: error => {1}", filename, e.getMessage()));

            if (_log.isDebugEnabled())
                _log.debug(e);
        }

        return null;
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return NAME;
    }
}