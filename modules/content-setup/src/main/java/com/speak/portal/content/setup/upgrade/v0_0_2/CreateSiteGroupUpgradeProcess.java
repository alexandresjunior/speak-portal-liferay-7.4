package com.speak.portal.content.setup.upgrade.v0_0_2;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import com.speak.portal.content.setup.constants.ContentSetupConstants;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Alexandre de Souza Jr.
 */
public class CreateSiteGroupUpgradeProcess extends UpgradeProcess {

	/**
	 * Method that initializes the upgrade process for this specific class.
	 * It has the logic to populate the different Groups for the portal.
	 *
	 * @throws Exception    Generic Java Exception that is thrown if something were to fail.
	 */
	@Override
	protected void doUpgrade() throws Exception {
		long companyId = PortalUtil.getDefaultCompanyId();

		long userId = UserLocalServiceUtil.getDefaultUserId(companyId);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setCompanyId(companyId);
		serviceContext.setUserId(userId);

		Locale locale = LocaleUtil.getDefault();

		HashMap<Locale, String> nameMap = new HashMap<>();

		nameMap.put(locale, ContentSetupConstants.SPEAK_PORTAL);

		Group group = GroupLocalServiceUtil.loadFetchGroup(companyId, ContentSetupConstants.SPEAK_PORTAL);

		if (Objects.isNull(group)) {
			group = GroupLocalServiceUtil.addGroup(
				userId, GroupConstants.DEFAULT_PARENT_GROUP_ID, Group.class.getName(), userId,
				GroupConstants.DEFAULT_LIVE_GROUP_ID, nameMap, null, GroupConstants.TYPE_SITE_OPEN, true,
				GroupConstants.MEMBERSHIP_RESTRICTION_TO_PARENT_SITE_MEMBERS,
				ContentSetupConstants.SPEAK_PORTAL_FRIENDLY_URL, true, false, true, serviceContext);

			if (_log.isInfoEnabled()) {
				_log.info("Create group: " + group.getName(locale));
			}
		}
	}

	/**
	 * Class Logger.
	 */
	private static final Log _log = LogFactoryUtil.getLog(CreateSiteGroupUpgradeProcess.class);

}