package com.liferay.enablement.template.context.contributor;

import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.enablement.template.context.contributor.util.DisplayPageURLHelper;
import com.liferay.portal.kernel.template.TemplateContextContributor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author andrew jardine
 */
@Component(
	property = "type=" + TemplateContextContributor.TYPE_GLOBAL,
	service = TemplateContextContributor.class
)
public class ClarityFreemarkerTemplateContextContributor
	implements TemplateContextContributor {

	public static final String ASSET_DISPLAY_PAGE_FRIENDLY_URL_PROVIDER =
		"assetDisplayPageFriendlyURLProvider";

	public static final String DISPLAY_PAGE_URL_HELPER = "displayPageURLHelper";

	@Override
	public void prepare(
		Map<String, Object> map, HttpServletRequest httpServletRequest) {

		map.put(
			ASSET_DISPLAY_PAGE_FRIENDLY_URL_PROVIDER,
			_assetDisplayPageFriendlyURLProvider);
		map.put(DISPLAY_PAGE_URL_HELPER, _displayPageURLHelper);
	}

	@Reference
	private AssetDisplayPageFriendlyURLProvider
		_assetDisplayPageFriendlyURLProvider;

	@Reference
	private DisplayPageURLHelper _displayPageURLHelper;

}