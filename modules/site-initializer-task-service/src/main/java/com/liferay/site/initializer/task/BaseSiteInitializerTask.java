package com.liferay.site.initializer.task;

import com.liferay.dynamic.data.mapping.kernel.DDMStructure;
import com.liferay.journal.model.JournalArticle;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;
import com.liferay.site.initializer.task.api.SiteInitializerTask;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

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

//    protected String _replace(String s, Map<String, String> stringUtilReplaceValues) {
//
//        HashMap<String, String> aggregatedStringUtilReplaceValues = HashMapBuilder
//                .putAll(
//                        _getClassNameIdStringUtilReplaceValues()
//                ).putAll(
//                        _getReleaseInfoStringUtilReplaceValues()
//                ).putAll(
//                        stringUtilReplaceValues
//                ).build();
//
//        s = StringUtil.replace(s, "\"[#", "#]\"", aggregatedStringUtilReplaceValues);
//
//        return StringUtil.replace(s, "[$", "$]", aggregatedStringUtilReplaceValues);
//    }
//
//    private String _replace(String s, ServiceContext serviceContext)
//            throws Exception {
//
//        Group group = serviceContext.getScopeGroup();
//
//        return StringUtil.replace(
//                s,
//                new String[] {
//                        "[$COMPANY_ID$]", "[$GROUP_FRIENDLY_URL$]", "[$GROUP_ID$]",
//                        "[$GROUP_KEY$]", "[$PORTAL_URL$]"
//                },
//                new String[] {
//                        String.valueOf(group.getCompanyId()), group.getFriendlyURL(),
//                        String.valueOf(serviceContext.getScopeGroupId()),
//                        group.getGroupKey(), serviceContext.getPortalURL()
//                });
//    }
//
//    private String _replace(String s, String oldSub, String newSub) {
//        return StringUtil.replace(s, oldSub, newSub);
//    }
//
//    private Map<String, String> _getClassNameIdStringUtilReplaceValues() {
//        Map<String, String> map = new HashMap<>();
//
//        Class<?>[] classes = {DDMStructure.class, JournalArticle.class};
//
//        for (Class<?> clazz : classes) {
//            map.put(
//                    "CLASS_NAME_ID:" + clazz.getName(),
//                    String.valueOf(PortalUtil.getClassNameId(clazz)));
//        }
//
//        return map;
//    }
//
//    private Map<String, String> _getReleaseInfoStringUtilReplaceValues() {
//        Map<String, String> map = new HashMap<>();
//
//        Object[] entries = {
//                "BUILD_DATE", ReleaseInfo.getBuildDate(), "BUILD_NUMBER",
//                ReleaseInfo.getBuildNumber(), "CODE_NAME",
//                ReleaseInfo.getCodeName(), "NAME", ReleaseInfo.getName(),
//                "PARENT_BUILD_NUMBER", ReleaseInfo.getParentBuildNumber(),
//                "RELEASE_INFO",
//                _replace(ReleaseInfo.getReleaseInfo(), StringPool.OPEN_PARENTHESIS, "<br>("),
//                "SERVER_INFO", ReleaseInfo.getServerInfo(), "VENDOR",
//                ReleaseInfo.getVendor(), "VERSION", ReleaseInfo.getVersion(),
//                "VERSION_DISPLAY_NAME", ReleaseInfo.getVersionDisplayName()
//        };
//
//        for (int i = 0; i < entries.length; i += 2) {
//            String entryKey = String.valueOf(entries[i]);
//            String entryValue = String.valueOf(entries[i + 1]);
//
//            map.put("RELEASE_INFO:" + entryKey, entryValue);
//        }
//
//        return map;
//    }
}
