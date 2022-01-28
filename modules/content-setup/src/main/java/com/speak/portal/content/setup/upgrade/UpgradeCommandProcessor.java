package com.speak.portal.content.setup.upgrade;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.util.Validator;

import java.io.InputStream;
import java.io.PrintStream;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;
import org.apache.felix.service.command.Converter;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alexandre de Souza Jr.
 */
@Component(immediate = true, service = UpgradeCommandProcessor.class)
public class UpgradeCommandProcessor {

	public void upgradeBundle(final String bundleSymbolicName) throws PortalException {
		CommandSession commandSession = null;

		try (InputStream emptyInputStream = new UnsyncByteArrayInputStream(new byte[0]);
			UnsyncByteArrayOutputStream outputUnsyncByteArrayOutputStream = new UnsyncByteArrayOutputStream();
			UnsyncByteArrayOutputStream errorUnsyncByteArrayOutputStream = new UnsyncByteArrayOutputStream();
			PrintStream outputPrintStream = new PrintStream(outputUnsyncByteArrayOutputStream);
			PrintStream errorPrintStream = new PrintStream(errorUnsyncByteArrayOutputStream)) {

			commandSession = _commandProcessor.createSession(emptyInputStream, outputPrintStream, errorPrintStream);

			// invoke the gogo upgrade:execute command and provide the BSN

			Object result = commandSession.execute("upgrade:execute " + bundleSymbolicName);

			if (result != null) {
				outputPrintStream.print(commandSession.format(result, Converter.INSPECT));
			}

			errorPrintStream.flush();
			outputPrintStream.flush();

			String errorContent = errorUnsyncByteArrayOutputStream.toString();

			if (Validator.isNotNull(errorContent)) {
				throw new Exception(errorContent);
			}

			String outputContent = outputUnsyncByteArrayOutputStream.toString();

			if (Validator.isNotNull(outputContent)) {
				_log.info(outputContent);
			}
		}
		catch (Exception exception) {
			_log.error("Error upgrading bundle: " + exception.getMessage(), exception);

			throw new PortalException(exception);
		}
	}

	@Reference(target = ModuleServiceLifecycle.SYSTEM_CHECK, unbind = "-")
	protected void setModuleServiceLifecycle(ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	private static final Log _log = LogFactoryUtil.getLog(UpgradeCommandProcessor.class);

	@Reference
	private CommandProcessor _commandProcessor;

}