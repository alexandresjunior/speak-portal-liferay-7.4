package com.speak.portal.content.setup.upgrade.v0_0_2;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.TreeMapBuilder;

import com.speak.portal.content.setup.constants.ContentSetupConstants;

import java.util.TreeMap;

/**
 * @author Alexandre de Souza Jr.
 */
public class SetVirtualHostUpgradeProcess extends UpgradeProcess {

	/**
	 * Method that initializes the upgrade process for this specific class.
	 * It has the logic to set the virtual host name for the portal.
	 * Its value must be set within 'portal-ext.properties' file
	 * for the 'public.virtual.host' attribute.
	 *
	 * @throws Exception    Generic Java Exception that is thrown if something were to fail.
	 */
	@Override
	protected void doUpgrade() throws Exception {
		String virtualHostName = PropsUtil.get(ContentSetupConstants.PUBLIC_VIRTUAL_HOST_PROPERTY);

		TreeMap<String, String> virtualHostnames = TreeMapBuilder.put(
			virtualHostName, StringPool.BLANK
		).build();

		long companyId = PortalUtil.getDefaultCompanyId();

		Group group = GroupLocalServiceUtil.loadFetchGroup(companyId, ContentSetupConstants.SPEAK_PORTAL);

		LayoutSetLocalServiceUtil.updateVirtualHosts(group.getGroupId(), false, virtualHostnames);

		if (_log.isInfoEnabled()) {
			_log.info("Assigned domain " + virtualHostName + " to public layout set.");
		}
	}

	/**
	 * Class Logger.
	 */
	private static final Log _log = LogFactoryUtil.getLog(SetVirtualHostUpgradeProcess.class);

}