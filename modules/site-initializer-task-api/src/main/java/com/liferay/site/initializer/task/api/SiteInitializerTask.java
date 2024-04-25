package com.liferay.site.initializer.task.api;

import java.util.Map;

/**
 * @author andrew jardine | liferay inc
 */
public interface SiteInitializerTask {

    public void execute(long groupId, Map<String, Object> parameters) throws Exception;

    public String getName();
//
//    public String getDescription();
//
//    public String getEntity();

}