package com.liferay.enablement.bootcamp.clarity.action;


import com.liferay.configuration.admin.constants.ConfigurationAdminPortletKeys;
import com.liferay.enablement.bootcamp.clarity.configuration.settings.ClaritySiteInitializerSystemConfiguration;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.settings.SystemSettingsLocator;
import com.liferay.portal.kernel.util.ParamUtil;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * SaveSettingsMVCActionCommand.java
 * <p>
 * Class used to define the MVCActionCommand that is used to save the settings.
 * </p>
 *
 * @author andrew jardine | liferay inc.
 */
@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + ConfigurationAdminPortletKeys.SYSTEM_SETTINGS,
                "mvc.command.name=/system/settings/clarity/save"
        },
        service = MVCActionCommand.class
)
public class SaveSettingsMVCActionCommand extends BaseMVCActionCommand {
    private static final Log _log = LogFactoryUtil.getLog(SaveSettingsMVCActionCommand.class);

    @Reference
    private PortletURLFactory _portletURLFactory;

    @Reference
    private ConfigurationProvider _configurationProvider;

    /**
     * Handler for the SAVE action from the form (update) button.
     *
     * @param actionRequest  {@link ActionRequest}
     * @param actionResponse {@link ActionResponse}
     * @throws Exception
     */
    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        if (_log.isInfoEnabled())
            _log.info("updating custom Clarity theme site initializer task settings");

        try {
            boolean enabled = ParamUtil.getBoolean(actionRequest, "enabled");
            String[] tasks = ParamUtil.getStringValues(actionRequest, "task");

            Dictionary<String, Object> properties = new Hashtable<>();
            properties.put("taskList", tasks);
            properties.put("enabled", enabled);

            _configurationProvider.saveSystemConfiguration(ClaritySiteInitializerSystemConfiguration.class, properties);

        } catch (Exception e) {
            _log.error(MessageFormat.format("error occurred while saving Clarity site initializer task configuration :: error => {0}", e.getMessage()));

            if (_log.isDebugEnabled())
                _log.debug(e);
        }

        actionResponse.getRenderParameters().setValue("configurationScreenKey", "clarity");
        actionResponse.getRenderParameters().setValue("mvcRenderCommandName", "/configuration_admin/view_configuration_screen");

    }
}