package com.speak.portal.content.setup.upgrade;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.DummyUpgradeProcess;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import com.speak.portal.content.setup.upgrade.v0_0_1.CreateAdminUserUpgradeProcess;
import com.speak.portal.content.setup.upgrade.v0_0_2.CreateSiteGroupUpgradeProcess;
import com.speak.portal.content.setup.upgrade.v0_0_2.SetVirtualHostUpgradeProcess;
import com.speak.portal.content.setup.upgrade.v0_0_3.AddLayoutsUpgradeProcess;
import com.speak.portal.content.setup.upgrade.v0_0_3.SetLayoutsViewPermissionsUpgradeProcess;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alexandre de Souza Jr.
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class ContentSetupUpgradeStepRegistrator implements UpgradeStepRegistrator {

	/**
	 * The content setup module activation method.
	 *
	 * @param bundleContext  The BundleContext object that contains
	 *                       a bundle's execution context within the framework.
	 */
	@Activate
	public void activate(BundleContext bundleContext) throws PortalException {
		try {
			Bundle bundle = bundleContext.getBundle();

			_upgradeCommandProcessor.upgradeBundle(bundle.getSymbolicName());
		}
		catch (PortalException portalException) {
			_log.error("Error upgrading bundle: " + portalException.getMessage(), portalException);

			throw new PortalException(portalException);
		}
	}

	/**
	 * The content setup module controller. It runs all the upgrade steps.
	 *
	 * @param registry  The Registry object that taps into Liferay's upgrade process.
	 */
	@Override
	public void register(Registry registry) {
		registry.register("0.0.0", "0.0.1", new DummyUpgradeProcess());
		registry.register("0.0.1", "0.0.2", new CreateAdminUserUpgradeProcess());
		registry.register("0.0.2", "0.0.3", new CreateSiteGroupUpgradeProcess(), new SetVirtualHostUpgradeProcess());
		registry.register("0.0.3", "0.0.4", new AddLayoutsUpgradeProcess());
		registry.register("0.0.4", "0.0.5", new SetLayoutsViewPermissionsUpgradeProcess());
	}

	/**
	 * Class Logger
	 */
	private static final Log _log = LogFactoryUtil.getLog(ContentSetupUpgradeStepRegistrator.class);

	/**
	 * As in Liferay 7.3+ versions, the upgrade processes do not automatically run anymore,
	 * one must trigger the upgrade process manually, running the gogo shell command 'upgrade:execute'
	 * on the bundle itself to trigger the upgrade process.
	 * The UpgradeCommandProcessor class makes that happen.
	 */
	@Reference
	private UpgradeCommandProcessor _upgradeCommandProcessor;

}