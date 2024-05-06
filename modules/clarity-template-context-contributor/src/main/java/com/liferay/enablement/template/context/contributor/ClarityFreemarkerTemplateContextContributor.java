package com.liferay.enablement.template.context.contributor;

import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.enablement.template.context.contributor.util.DisplayPageURLHelper;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.TemplateContextContributor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ClarityFreemarkerTemplateContextContributor.java
 * <p>
 *     Template Context Contributor that is used to pass additional data and services into the
 *     templating engine to allow for necessary template development while still maintaining the
 *     SaaS approach.
 * </p>
 * @author andrew jardine | liferay inc
 */
@Component(
        property = "type=" + TemplateContextContributor.TYPE_GLOBAL,
        service = TemplateContextContributor.class
)
public class ClarityFreemarkerTemplateContextContributor implements TemplateContextContributor {

    private static final Log _log = LogFactoryUtil.getLog(ClarityFreemarkerTemplateContextContributor.class);

    public static final String ASSET_DISPLAY_PAGE_FRIENDLY_URL_PROVIDER = "assetDisplayPageFriendlyURLProvider";
    public static final String DISPLAY_PAGE_URL_HELPER = "displayPageURLHelper";

    @Reference
    private AssetDisplayPageFriendlyURLProvider _assetDisplayPageFriendlyURLProvider;

    @Reference
    private DisplayPageURLHelper _displayPageURLHelper;

    /**
     * Prepare the context map with the data and services that need to be passed into the template engine.
     * @param map {@link Map} with the data and services to be passed in
     * @param httpServletRequest {@link HttpServletRequest}
     */
    @Override
    public void prepare(Map<String, Object> map, HttpServletRequest httpServletRequest) {

        map.put(ASSET_DISPLAY_PAGE_FRIENDLY_URL_PROVIDER, _assetDisplayPageFriendlyURLProvider);
        map.put(DISPLAY_PAGE_URL_HELPER, _displayPageURLHelper);
    }
}