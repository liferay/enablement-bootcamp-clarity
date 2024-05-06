package com.liferay.enablement.template.context.contributor.util;

import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.asset.kernel.model.AssetEntry;
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

@Component(
        service = DisplayPageURLHelper.class
)
public class DisplayPageURLHelper {

    private static final Log _log = LogFactoryUtil.getLog(DisplayPageURLHelper.class);

    @Reference
    private AssetDisplayPageFriendlyURLProvider _assetDisplayPageFriendlyURLProvider;

    public InfoItemReference getInfoItemReference(String className, long classPK) {

         InfoItemReference infoItemReference = new InfoItemReference(null, 0L);

         return infoItemReference;
    }

    public ClassPKInfoItemIdentifier getClassPKInfoItemIdentifier(long classPK) {

        ClassPKInfoItemIdentifier classPKInfoItemIdentifier = new ClassPKInfoItemIdentifier(0L);

        return classPKInfoItemIdentifier;
    }

    public String getDisplayPageURL(String className, long classPK, ThemeDisplay themeDisplay) {

        String url = StringPool.BLANK;

        if (themeDisplay == null) {
            if ( _log.isDebugEnabled())
                _log.debug("theme display has a value of null. DisplayPageURL cannot be calculated");

            return url;
        }

        try {

            if ( _log.isInfoEnabled()) {
                _log.info("className => " + className);
                _log.info("classPK => " + classPK);
            }

            String displayPageURL = _assetDisplayPageFriendlyURLProvider.getFriendlyURL(new InfoItemReference(className, classPK), themeDisplay);

            if (Validator.isNotNull(displayPageURL))
                url = displayPageURL;

            if ( _log.isDebugEnabled()) {
                _log.debug("url => " + url);
            }
        }
        catch (PortalException portalException) {
            if (_log.isDebugEnabled()) {
                _log.debug(portalException);
            }
        }

        return url;
    }
}
