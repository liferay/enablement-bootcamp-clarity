package com.liferay.enablement.template.context.contributor.util;

import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.InfoItemReference;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author andrew jardine
 */
@Component(service = DisplayPageURLHelper.class)
public class DisplayPageURLHelper {

	public ClassPKInfoItemIdentifier getClassPKInfoItemIdentifier(
		long classPK) {

		return new ClassPKInfoItemIdentifier(0L);
	}

	public String getDisplayPageURL(
		String className, long classPK, ThemeDisplay themeDisplay) {

		String url = StringPool.BLANK;

		if (themeDisplay == null) {
			return url;
		}

		try {
			String displayPageURL =
				_assetDisplayPageFriendlyURLProvider.getFriendlyURL(
					new InfoItemReference(className, classPK), themeDisplay);

			if (Validator.isNotNull(displayPageURL)) {
				url = displayPageURL;
			}
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}

		return url;
	}

	public InfoItemReference getInfoItemReference(
		String className, long classPK) {

		return new InfoItemReference(null, 0L);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DisplayPageURLHelper.class);

	@Reference
	private AssetDisplayPageFriendlyURLProvider
		_assetDisplayPageFriendlyURLProvider;

}