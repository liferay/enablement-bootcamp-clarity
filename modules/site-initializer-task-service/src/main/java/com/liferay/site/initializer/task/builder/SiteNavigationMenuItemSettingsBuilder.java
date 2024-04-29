package com.liferay.site.initializer.task.builder;

import java.util.HashMap;
import java.util.Map;

public class SiteNavigationMenuItemSettingsBuilder {

    private Map<String, SiteNavigationMenuItemSetting> _siteNavigationMenuItemSettings = new HashMap<>();

    public Map<String, SiteNavigationMenuItemSetting> build() {
        return _siteNavigationMenuItemSettings;
    }

    public void put(String key, SiteNavigationMenuItemSetting siteNavigationMenuItemSetting) {
        _siteNavigationMenuItemSettings.put(key, siteNavigationMenuItemSetting);
    }
}