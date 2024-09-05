package com.liferay.enablement.sleeper.context.contributor;

import com.liferay.portal.kernel.template.TemplateContextContributor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author dnebinger
 */
@Component(
	property = "type=" + TemplateContextContributor.TYPE_GLOBAL,
	service = TemplateContextContributor.class
)
public class SleepingTemplateContextContributor
	implements TemplateContextContributor {

	@Override
	public void prepare(
		Map<String, Object> contextObjects, HttpServletRequest request) {

		contextObjects.put("sleeper", new Sleeper());
	}

	public class Sleeper {

		public void sleep(final int seconds) {
			try {
				Thread.sleep(seconds * 1000);
			}
			catch (Exception exception) {
			}
		}

	}

}