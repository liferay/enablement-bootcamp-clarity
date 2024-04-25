package com.liferay.enablement.bootcamp.clarity.configuration.settings;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author andrew jardine | liferay inc.
 */
@ExtendedObjectClassDefinition(
        category = "clarity-site-initializer",
        scope = ExtendedObjectClassDefinition.Scope.SYSTEM,
        generateUI = false
)
@Meta.OCD(
        id = "com.liferay.enablement.bootcamp.clarity.configuration.settings.ClaritySiteInitializerSystemConfiguration",
        localization = "content/Language",
        name = "clarity-site-initializer-system-configuration-name",
        description = "clarity-site-initializer-system-configuration-name-description"

)
public interface ClaritySiteInitializerSystemConfiguration
{
    @Meta.AD(name = "enabled",deflt = "true", required = false)
    boolean enabled();

    @Meta.AD(name = "task-list", required = false)
    String[] taskList();
}

