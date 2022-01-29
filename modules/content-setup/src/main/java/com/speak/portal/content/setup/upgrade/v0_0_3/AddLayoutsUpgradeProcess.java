package com.speak.portal.content.setup.upgrade.v0_0_3;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import com.speak.portal.content.setup.constants.ContentSetupConstants;
import com.speak.portal.content.setup.helper.ContentSetupHelper;

import java.util.Locale;

/**
 * @author Alexandre de Souza Jr.
 */
public class AddLayoutsUpgradeProcess extends UpgradeProcess {

	/**
	 * Method that initializes the upgrade process for this specific class.
	 * It has the logic for adding different pages into the portal.
	 *
	 * @throws Exception    Generic Java Exception that is thrown if something were to fail.
	 */
	@Override
	protected void doUpgrade() throws Exception {
		long companyId = PortalUtil.getDefaultCompanyId();

		Group group = GroupLocalServiceUtil.loadFetchGroup(companyId, ContentSetupConstants.SPEAK_PORTAL);

		long userId = UserLocalServiceUtil.getDefaultUserId(companyId);

		Locale locale = LocaleUtil.getDefault();

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setCompanyId(companyId);
		serviceContext.setScopeGroupId(group.getGroupId());
		serviceContext.setUserId(userId);

		ContentSetupHelper.createLayout(
			group.getGroupId(), userId, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			ContentSetupConstants.LAYOUT_NAME_LOGIN, StringPool.BLANK, ContentSetupConstants.LAYOUT_FRIENDLY_URL_LOGIN,
			Boolean.TRUE, Boolean.FALSE, LayoutConstants.TYPE_CONTENT, locale, serviceContext);

		ContentSetupHelper.createLayout(
			group.getGroupId(), userId, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			ContentSetupConstants.LAYOUT_NAME_REGISTRATION, StringPool.BLANK,
			ContentSetupConstants.LAYOUT_FRIENDLY_URL_REGISTRATION, Boolean.TRUE, Boolean.FALSE,
			LayoutConstants.TYPE_CONTENT, locale, serviceContext);

		ContentSetupHelper.createLayout(
			group.getGroupId(), userId, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			ContentSetupConstants.LAYOUT_NAME_FORGOT_PASSWORD, StringPool.BLANK,
			ContentSetupConstants.LAYOUT_FRIENDLY_URL_FORGOT_PASSWORD, Boolean.TRUE, Boolean.FALSE,
			LayoutConstants.TYPE_CONTENT, locale, serviceContext);

		ContentSetupHelper.createLayout(
			group.getGroupId(), userId, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			ContentSetupConstants.LAYOUT_NAME_HOME, StringPool.BLANK, ContentSetupConstants.LAYOUT_FRIENDLY_URL_HOME,
			Boolean.FALSE, Boolean.FALSE, LayoutConstants.TYPE_CONTENT, locale, serviceContext);

		ContentSetupHelper.createLayout(
			group.getGroupId(), userId, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			ContentSetupConstants.LAYOUT_NAME_COURSES, StringPool.BLANK,
			ContentSetupConstants.LAYOUT_FRIENDLY_URL_COURSES, Boolean.FALSE, Boolean.FALSE,
			LayoutConstants.TYPE_CONTENT, locale, serviceContext);

		ContentSetupHelper.createLayout(
			group.getGroupId(), userId, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			ContentSetupConstants.LAYOUT_NAME_BLOG, StringPool.BLANK, ContentSetupConstants.LAYOUT_FRIENDLY_URL_BLOG,
			Boolean.FALSE, Boolean.FALSE, LayoutConstants.TYPE_CONTENT, locale, serviceContext);

		ContentSetupHelper.createLayout(
			group.getGroupId(), userId, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			ContentSetupConstants.LAYOUT_NAME_CONTACT_US, StringPool.BLANK,
			ContentSetupConstants.LAYOUT_FRIENDLY_URL_CONTACT_US, Boolean.FALSE, Boolean.FALSE,
			LayoutConstants.TYPE_CONTENT, locale, serviceContext);

		ContentSetupHelper.createLayout(
			group.getGroupId(), userId, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			ContentSetupConstants.LAYOUT_NAME_MY_DASHBOARD, StringPool.BLANK,
			ContentSetupConstants.LAYOUT_FRIENDLY_URL_MY_DASHBOARD, Boolean.FALSE, Boolean.FALSE,
			LayoutConstants.TYPE_CONTENT, locale, serviceContext);

		ContentSetupHelper.createLayout(
			group.getGroupId(), userId, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			ContentSetupConstants.LAYOUT_NAME_PLACEMENT_TEST, StringPool.BLANK,
			ContentSetupConstants.LAYOUT_FRIENDLY_URL_PLACEMENT_TEST, Boolean.TRUE, Boolean.FALSE,
			LayoutConstants.TYPE_CONTENT, locale, serviceContext);

		ContentSetupHelper.createLayout(
			group.getGroupId(), userId, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			ContentSetupConstants.LAYOUT_NAME_PAGE_NOT_FOUND, StringPool.BLANK,
			ContentSetupConstants.LAYOUT_FRIENDLY_URL_PAGE_NOT_FOUND, Boolean.TRUE, Boolean.FALSE,
			LayoutConstants.TYPE_CONTENT, locale, serviceContext);
	}

}