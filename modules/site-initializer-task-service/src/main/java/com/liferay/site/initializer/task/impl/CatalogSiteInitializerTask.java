package com.liferay.site.initializer.task.impl;

import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
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
 * @author Erica Gnajek | liferay
 */
@Component(property = { "data.type=CommerceCatalog", "data.format=json",
		"entity.model=com.liferay.commerce.product.model.CPInstance" }, service = SiteInitializerTask.class)
public class CatalogSiteInitializerTask extends BaseSiteInitializerTask implements SiteInitializerTask {

	private static final Log _log = LogFactoryUtil.getLog(CatalogSiteInitializerTask.class);

	public static final String NAME = "Catalogs";

	private static final String FILENAME = "catalogs.json";

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private CommerceCatalogLocalService _commerceCatalogLocalService;
	
	@Reference
	private CommerceCurrencyLocalService _commerceCurrencyLocalService;
	

	/**
	 * @param groupId
	 * @param bundleContext
	 * @param context
	 * @throws Exception
	 */
	@Override
	public void execute(long groupId, BundleContext bundleContext, Map<String, Object> context) throws Exception {

		ServiceContext serviceContext = (ServiceContext) context.get(ContextKeys.SERVICE_CONTEXT);

		String catalogFileContents = getResourceFileContents(bundleContext,
				GetterUtil.getString(context.get(ContextKeys.DEPENDENCIES_PATH)), FILENAME);
		
		CommerceCatalog catalog;

		// data was found -- process catalogs list
		if (Validator.isNotNull(catalogFileContents)) {

			JSONArray catalogsJSONArray = _jsonFactory.createJSONArray(catalogFileContents);
			JSONObject catalogData = null;

			for (int i = 0; i < catalogsJSONArray.length(); ++i) {
				try {
					catalogData = catalogsJSONArray.getJSONObject(i);
					
					// check if catalog exists
					catalog = _commerceCatalogLocalService.fetchByExternalReferenceCode(catalogData.getString("externalReferenceCode"), serviceContext.getCompanyId());
					
					if (Validator.isNull(catalog)) {
						catalog = _addCatalog(catalogData, serviceContext);

					if (_log.isInfoEnabled())
						_log.info(MessageFormat.format("Catalog added to site, name => {0}",
								catalogData.getString("name")));
			 }

				} catch (Exception e) {

					if (_log.isWarnEnabled()) {

						if (Validator.isNotNull(catalogData)) {
							_log.warn(MessageFormat.format(
									"unexpected error occurred while processing catalog: {0} :: error => {1}",
									catalogData.getString("name"), e.getMessage()));
						} else {
							_log.warn(MessageFormat.format(
									"unexpected error occurred while processing catalog :: error => {0}",
									e.getMessage()));
						}

						if (_log.isDebugEnabled())
							_log.debug(e);
					}
				}
			}
		} else {
			if (_log.isInfoEnabled())
				_log.info("no resources found for catalogs.");
		}
	}

	
	/**
	 * @param catalogData
	 * @param context
	 * @return
	 */
	
	private CommerceCatalog _addCatalog(JSONObject catalogData, ServiceContext serviceContext)
			throws Exception {

		CommerceCurrency commerceCurrency =
			_commerceCurrencyLocalService.getCommerceCurrency(
				serviceContext.getCompanyId(), catalogData.getString("currencyCode"));

		return _commerceCatalogLocalService.addCommerceCatalog(
			catalogData.getString("externalReferenceCode"), catalogData.getString("name"),
			commerceCurrency.getCode(), catalogData.getString("defaultLanguageId"),
			serviceContext);
	}
	  
	 /**
		* @return name
	 */
	@Override
	public String getName() {
		return NAME;
	}
}