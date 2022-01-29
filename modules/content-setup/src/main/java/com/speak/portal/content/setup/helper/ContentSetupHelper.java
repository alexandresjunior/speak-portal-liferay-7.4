package com.speak.portal.content.setup.helper;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Locale;
import java.util.Objects;

/**
 * @author Alexandre de Souza Jr.
 */
public class ContentSetupHelper {

	/**
	 * Helper method that is used to create a Layout within the database.
	 *
	 * @throws Exception    Generic Java Exception that is thrown if something were to fail.
	 */
	public static Layout createLayout(
			long groupId, long userId, long parentLayoutId, String layoutName, String layoutDescription,
			String friendlyUrl, boolean hidden, boolean privateLayout, String type, Locale locale,
			ServiceContext serviceContext)
		throws Exception {

		Layout layout = LayoutLocalServiceUtil.fetchLayoutByFriendlyURL(groupId, privateLayout, friendlyUrl);

		if (Objects.isNull(layout)) {
			layout = LayoutLocalServiceUtil.addLayout(
				userId, groupId, privateLayout, parentLayoutId, layoutName, layoutName, layoutDescription, type, hidden,
				friendlyUrl, serviceContext);

			if (type.equals(LayoutConstants.TYPE_CONTENT)) {
				Layout draftLayout = layout.fetchDraftLayout();

				publishContentLayout(draftLayout, layout);
			}

			if (_log.isInfoEnabled()) {
				_log.info("Created layout: " + layout.getName(locale));
			}
		}

		return layout;
	}

	/**
	 * Helper method that is used to publish a content pages. Content pages require de-caching of the model and updates
	 * to the Draft Layout that it is associated with. It updates the two existing rows inside the database after
	 * the Layout has been created. Content pages when created are automatically marked in the "Draft" state. This
	 * method publishes them.
	 *
	 * @param draftLayout   The draft Layout that is associated with the actual Layout of the content page. The row
	 *                      references the actual Layout through the classPK field of the Layout model and is always
	 *                      one id iteration from the Layout it is based off of.
	 * @param layout        The actual Layout of the content page.
	 * @throws Exception    Generic Java Exception that is thrown if something were to fail.
	 */
	public static void publishContentLayout(Layout draftLayout, Layout layout) throws Exception {
		UnicodeProperties publishedTypeSettingsUnicodeProperties = new UnicodeProperties();

		// Set the 'typeSettings' inside the database to 'published=true'

		publishedTypeSettingsUnicodeProperties.setProperty("published", StringPool.TRUE);

		// Set the Layout metadata to move it to the 'approved' state

		layout.setStatus(WorkflowConstants.STATUS_APPROVED);
		layout.setCachedModel(false);
		layout.setTypeSettingsProperties(publishedTypeSettingsUnicodeProperties);
		layout.setLayoutPrototypeUuid(null);

		LayoutLocalServiceUtil.updateLayout(layout);

		draftLayout = LayoutLocalServiceUtil.getLayout(draftLayout.getPlid());

		// Set the draft Layout metadata to move it to the 'approved' state

		draftLayout.setStatus(WorkflowConstants.STATUS_APPROVED);
		draftLayout.setCachedModel(false);
		draftLayout.setTypeSettingsProperties(publishedTypeSettingsUnicodeProperties);

		LayoutLocalServiceUtil.updateLayout(draftLayout);
	}

	/**
	 * Class Logger.
	 */
	private static final Log _log = LogFactoryUtil.getLog(ContentSetupHelper.class);

}