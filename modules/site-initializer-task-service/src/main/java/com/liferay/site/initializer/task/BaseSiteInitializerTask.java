package com.liferay.site.initializer.task;

import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;
import com.liferay.site.initializer.task.api.SiteInitializerTask;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author andrew jardine | liferay inc.
 */
public abstract class BaseSiteInitializerTask {

    private static final Log _log = LogFactoryUtil.getLog(BaseSiteInitializerTask.class);

    /**
     *
     * @param bundleContext
     * @param path
     * @param resource
     * @return
     */
    protected Enumeration<URL> getResources(BundleContext bundleContext, String path, String resource) {

        Bundle bundle = bundleContext.getBundle();

        return bundleContext.getBundle().findEntries(path, resource, true);
    }

    /**
     *
     * @param bundleContext
     * @param path
     * @param resource
     * @return
     * @throws Exception
     */
    protected String getResourceFileContents(BundleContext bundleContext, String path, String resource) throws Exception{

        String contents = StringPool.BLANK;

        Enumeration<URL> resources = getResources(bundleContext, path, resource);

        if (Validator.isNotNull(resources)) {
            URL element = resources.nextElement();

            InputStream in = element.openStream();
            contents = StringUtil.read(in);
        }

        return contents;
    }

    /**
     * Build a map with the translation values.
     *
     * @param jsonObject {@link JSONObject} with the elements to be converted to a map
     * @return {@link Map} with key/value pairs representing the translation map to create
     */
    protected Map<Locale, String> getTranslationMap(JSONObject jsonObject)
    {
        Map<Locale, String> translationMap = new LinkedHashMap<>();

        Locale locale;

        for (String key : jsonObject.keySet()) {
            try {
                locale = new Locale(key.replace(StringPool.DASH, StringPool.UNDERLINE));

                translationMap.put(locale, jsonObject.getString(key));
            }
            catch (Exception e) {
                if (_log.isWarnEnabled())
                    _log.warn(MessageFormat.format("problem occurred while trying to create language map item :: error => {0}", e.getMessage()));

                if (_log.isDebugEnabled())
                    _log.debug(e);
            }
        }

        return translationMap;
    }
}
