package com.liferay.site.initializer.task;

import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;
import com.liferay.site.initializer.task.api.SiteInitializerTask;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * @author andrew jardine | liferay inc.
 */
public abstract class BaseSiteInitializerTask {

    protected Enumeration<URL> getResources(BundleContext bundleContext, String path, String resource) {

        Bundle bundle = bundleContext.getBundle();

        return bundleContext.getBundle().findEntries(path, resource, true);
    }

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
}
