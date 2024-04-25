package com.liferay.site.initializer.task.api;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.util.Map;

/**
 * @author andrew jardine | liferay inc
 */
public interface SiteInitializerTask {

    public void execute(long groupId, BundleContext bundleContext, Map<String, Object> parameters) throws Exception;

    public String getName();
//
//    public String getDescription();
//
//    public String getEntity();

}