package com.liferay.cx.cos083.web.internal.portlet.configuration;

import com.liferay.configuration.admin.category.ConfigurationCategory;
import org.osgi.service.component.annotations.Component;

/**
 * @author andrew jardine | liferay inc.
 */
@Component(
        immediate = true,
        service = ConfigurationCategory.class
)
public class BootcampConfigurationSection implements ConfigurationCategory
{
    // https://clayui.com/docs/components/icon.html for the options to choose from
    private static final String _CATEGORY_ICON = "emoji";

    private static final String _CATEGORY_KEY = "clarity-site-initializer";

    private static final String _CATEGORY_SECTION = "liferay-enablement-bootcamp";

    /**
     * Gets the category key
     *
     * @return String with the key used to represent the category
     */
    @Override
    public String getCategoryKey()
    {
        return _CATEGORY_KEY;
    }

    /**
     * Gets the category section
     *
     * @return String used to represent the section for the category to  be placed in
     */
    @Override
    public String getCategorySection()
    {
        return _CATEGORY_SECTION;
    }

    /**
     * Gets the icon
     *
     * @return String representing the css (lexicon) class used to identify the category
     */
    @Override
    public String getCategoryIcon()
    {
        return _CATEGORY_ICON;
    }
}
