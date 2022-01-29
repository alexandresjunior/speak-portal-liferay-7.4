package com.speak.portal.content.setup.upgrade.v0_0_1;

import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import com.speak.portal.content.setup.constants.ContentSetupConstants;

import java.util.Objects;

/**
 * @author Alexandre de Souza Jr.
 */
public class CreateAdminUserUpgradeProcess extends UpgradeProcess {

	/**
	 * Method that initializes the upgrade process for this specific class.
	 * It has the logic for creating and adding the Admin User into the portal.
	 * Also, it assigns the Administrator role to that user.
	 *
	 * @throws Exception    Generic Java Exception that is thrown if something were to fail.
	 */
	@Override
	protected void doUpgrade() throws Exception {
		long companyId = PortalUtil.getDefaultCompanyId();

		User user = UserLocalServiceUtil.fetchUserByEmailAddress(
			companyId, ContentSetupConstants.ADMIN_USER_EMAIL_ADDRESS);

		if (Objects.isNull(user)) {
            User adminUser = UserLocalServiceUtil.addUser(
                    UserLocalServiceUtil.getDefaultUserId(companyId), // creatorUserId
                    companyId, // companyId
                    true, // autoPassword
                    null, // password1
                    null, // password2
                    false, // autoScreenName
                    "data.admin", // screenName
                    ContentSetupConstants.ADMIN_USER_EMAIL_ADDRESS, // emailAddress
                    LocaleUtil.getDefault(), // locale
                    "Data", // firstName
                    "", // middleName
                    "Admin", // lastName
                    0, // prefixId
                    0, // suffixId
                    true, // male
                    0, // birthdayMonth
                    1, // birthdayDay
                    1970, // birthdayYear
                    null, // jobTitle
                    null, // groupIds
                    null, // organizationIds
                    null, // roleIds
                    null, // userGroupIds
                    false, // sendEmail
                    null); // serviceContext

			Role role = RoleLocalServiceUtil.fetchRole(companyId, "Administrator");

			RoleLocalServiceUtil.addUserRole(adminUser.getUserId(), role);
		}
	}

}