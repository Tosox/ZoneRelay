package de.tosox.zonerelay.gui;

import de.tosox.zonerelay.AppConfig;
import de.tosox.zonerelay.config.ConfigLoader;
import de.tosox.zonerelay.config.ConfigValidator;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logging.LogManager;
import de.tosox.zonerelay.manager.InstallManager;
import de.tosox.zonerelay.model.ConfigData;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainFrameController {
	private final AppConfig config;
	private final Localizer localizer;
	private final LogManager logManager;
	private final MainFrame mainFrame;
	private final InstallManager installManager;

	public MainFrameController(AppConfig config, Localizer localizer,
	                           LogManager logManager, MainFrame mainFrame, InstallManager installManager) {
		this.config = config;
		this.localizer = localizer;
		this.logManager = logManager;
		this.mainFrame = mainFrame;
		this.installManager = installManager;
	}

	public void onInstallClick() {
		if (installManager.isInstalling()) {
			logManager.getUiLogger().warn(localizer.translate("ERR_ALREADY_INSTALLING"));
			logManager.getFileLogger().warn("Installation already in progress");
			return;
		}

		if (Files.notExists(Paths.get(config.getMo2ExePath()))) {
			logManager.getUiLogger().warn(localizer.translate("ERR_INVALID_INSTALL_DIR"));
			logManager.getFileLogger().warn("Please move the installer into the MO2 directory");
			return;
		}

		if (Files.notExists(Paths.get(config.getMo2ConfigPath()))) {
			logManager.getUiLogger().warn(localizer.translate("ERR_LAUNCH_MO2"));
			logManager.getFileLogger().warn("Please launch MO2 once first");
			return;
		}

		ConfigData configData;
		try {
			ConfigLoader configLoader = new ConfigLoader();
			ConfigValidator configValidator = new ConfigValidator();
			configData = configLoader.load(new File(config.getModlistConfigPath()));
			configValidator.validate(configData);
		} catch (Exception e) {
			logManager.getUiLogger().error(localizer.translate("ERR_CONFIG_INVALID"));
			logManager.getFileLogger().error("Failed to load or validate config: " + e.getMessage());
			return;
		}

		installManager.setCurrentProgressListener((current, total) ->
				mainFrame.setCurrentProgress((current * 100) / total)
		);
		installManager.setTotalProgressListener((current, total) ->
				mainFrame.setTotalProgress((current * 100) / total)
		);

		try {
			installManager.startInstallation(configData, mainFrame.isFullInstallSelected());
		} catch (Exception e) {
			logManager.getUiLogger().error(localizer.translate("ERR_INSTALLATION_FAILED"));
			logManager.getFileLogger().error("Installation failed: " + e.getMessage());
		}
	}

	public void onLaunchClick() {
		if (Files.notExists(Paths.get(config.getMo2ExePath()))) {
			logManager.getUiLogger().warn(localizer.translate("ERR_INVALID_INSTALL_DIR"));
			logManager.getFileLogger().warn("Please move the installer into the MO2 directory");
			return;
		}

		try {
			Runtime.getRuntime().exec(config.getMo2ExePath(), null, new File(config.getMo2Directory()));
		} catch (IOException e) {
			logManager.getUiLogger().error(localizer.translate("ERR_LAUNCH_MO2_FAIL"));
			logManager.getFileLogger().error("Failed to launch MO2: " + e.getMessage());
		}
	}
}
