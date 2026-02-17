package de.tosox.zonerelay.handler;

import de.tosox.zonerelay.logging.Logger;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class CrashHandler {
	private final Logger logger;

	public CrashHandler(Logger logger) {
		this.logger = logger;
	}

	public void fatal(String message, Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));

		logger.error(message, stringWriter);

		JOptionPane.showMessageDialog(
				null,
				message,
				"Error",
				JOptionPane.ERROR_MESSAGE
		);

		System.exit(1);
	}
}
