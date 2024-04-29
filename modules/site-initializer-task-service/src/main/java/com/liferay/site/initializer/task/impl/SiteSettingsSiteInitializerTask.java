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
        /*
        	public Group addOrUpdateGroup(
			String externalReferenceCode, long userId, long parentGroupId,
			String className, long classPK, long liveGroupId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			int type, boolean manualMembership, int membershipRestriction,
			String friendlyURL, boolean site, boolean inheritContent,
			boolean active, ServiceContext serviceContext)
		throws Exception {

		User user = _userLocalService.getUser(userId);

		Group group = groupPersistence.fetchByERC_C(
			externalReferenceCode, user.getCompanyId());

		if (group == null) {
			group = addGroup(
				userId, parentGroupId, className, classPK, liveGroupId, nameMap,
				descriptionMap, type, manualMembership, membershipRestriction,
				friendlyURL, site, inheritContent, active, serviceContext);

			group.setExternalReferenceCode(externalReferenceCode);

			group = groupPersistence.update(group);
		}
		else {
			group = updateGroup(
				group.getGroupId(), parentGroupId, nameMap, descriptionMap,
				type, manualMembership, membershipRestriction, friendlyURL,
				inheritContent, active, serviceContext);
		}

		return group;
	}
         */
    }

    @Override
    public String getName() {
        return "Site Settings";
    }
}