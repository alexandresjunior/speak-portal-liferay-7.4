package com.speak.portal.content.setup.upgrade.v0_0_3;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import com.speak.portal.content.setup.constants.ContentSetupConstants;

import java.util.Locale;
import java.util.Objects;

/**
 * @author Alexandre de Souza Jr.
 */
public class SetLayoutsViewPermissionsUpgradeProcess extends UpgradeProcess {

	/**
	 * Method that initializes the upgrade for this specific class.
	 * It has the logic to set role permissions on a given page in the portal.
	 * Removes guest view permissions to My Dashboard and Placement Test pages.
	 *
	 * @throws Exception Generic Java Exception that is thrown if something were to fail.
	 */
	@Override
	protected void doUpgrade() throws Exception {
		long companyId = PortalUtil.getDefaultCompanyId();

		Locale locale = LocaleUtil.getDefault();

		Group group = GroupLocalServiceUtil.fetchGroup(companyId, ContentSetupConstants.SPEAK_PORTAL);

		if (Objects.isNull(group)) {
			if (_log.isInfoEnabled()) {
				_log.info("No Speak Portal group found. Using the Global group.");
			}

			Group global = GroupLocalServiceUtil.getCompanyGroup(companyId);

			group = GroupLocalServiceUtil.getGroup(global.getGroupId());
		}

		Layout myDashboard = LayoutLocalServiceUtil.getLayoutByFriendlyURL(
			group.getGroupId(), Boolean.FALSE, ContentSetupConstants.LAYOUT_FRIENDLY_URL_MY_DASHBOARD);

		_removeGuestViewPermissions(companyId, myDashboard, locale);

		Layout placementTest = LayoutLocalServiceUtil.getLayoutByFriendlyURL(
			group.getGroupId(), Boolean.FALSE, ContentSetupConstants.LAYOUT_FRIENDLY_URL_PLACEMENT_TEST);

		_removeGuestViewPermissions(companyId, placementTest, locale);
	}

	private void _removeGuestViewPermissions(long companyId, Layout layout, Locale locale) {
		try {
			Role guestRole = RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST);

			ResourcePermissionLocalServiceUtil.removeResourcePermission(
				companyId, Layout.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL, String.valueOf(layout.getPlid()),
				guestRole.getRoleId(), ActionKeys.VIEW);

			if (_log.isInfoEnabled()) {
				_log.info("Removed guest view permissions to layout: " + layout.getName(locale));
			}
		}
		catch (PortalException portalException) {
			_log.error("Unable to remove guest view permissions to layout: " + layout.getName(locale), portalException);
		}
	}

	/**
	 * Class Logger.
	 */
	private static final Log _log = LogFactoryUtil.getLog(SetLayoutsViewPermissionsUpgradeProcess.class);

}