package de.tosox.zonerelay.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.tosox.zonerelay.AppConfig;
import de.tosox.zonerelay.config.ConfigLoader;
import de.tosox.zonerelay.config.ConfigValidator;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logging.LogManager;
import de.tosox.zonerelay.manager.InstallManager;
import de.tosox.zonerelay.model.ConfigData;
import de.tosox.zonerelay.model.ConfigEntry;
import de.tosox.zonerelay.service.InstallProgressStore;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Singleton
public class MainFrameController {
	private final Localizer localizer;
	private final LogManager logManager;
	private final MainFrame mainFrame;
	private final InstallManager installManager;
	private final InstallProgressStore progressStore;

	@Inject
	public MainFrameController(Localizer localizer, LogManager logManager,
	                           MainFrame mainFrame, InstallManager installManager,
	                           InstallProgressStore progressStore) {
		this.localizer = localizer;
		this.logManager = logManager;
		this.mainFrame = mainFrame;
		this.installManager = installManager;
		this.progressStore = progressStore;
	}

	public void onInstallClick() {
		if (installManager.isInstalling()) {
			logManager.getUiLogger().warn(localizer.translate("ERR_ALREADY_INSTALLING"));
			logManager.getFileLogger().warn("Installation already in progress");
			return;
		}

		if (Files.notExists(Paths.get(AppConfig.MO2_EXE_PATH))) {
			logManager.getUiLogger().warn(localizer.translate("ERR_INVALID_INSTALL_DIR"));
			logManager.getFileLogger().warn("Please move the installer into the MO2 directory");
			return;
		}

		if (Files.notExists(Paths.get(AppConfig.MO2_CONFIG_PATH))) {
			logManager.getUiLogger().warn(localizer.translate("ERR_LAUNCH_MO2"));
			logManager.getFileLogger().warn("Please launch MO2 once first");
			return;
		}

		logManager.getUiLogger().info(localizer.translate("MSG_READ_MODLIST_CFG"));
		logManager.getFileLogger().info("Reading modlist configuration");

		ConfigData configData;
		try {
			ConfigLoader configLoader = new ConfigLoader();
			ConfigValidator configValidator = new ConfigValidator();
			configData = configLoader.load(new File(AppConfig.MODLIST_CONFIG_PATH));
			configValidator.validate(configData);
		} catch (Exception e) {
			logManager.getUiLogger().error(localizer.translate("ERR_CONFIG_INVALID"));
			logManager.getFileLogger().error("Failed to load or validate config: " + e.getMessage());
			return;
		}

		installManager.setCurrentProgressListener((current, total) -> {
			if (total <= 0) {
				mainFrame.setCurrentProgressIndeterminate();
			} else {
				mainFrame.setCurrentProgress((int) (current * 100 / total));
			}
		});
		installManager.setTotalProgressListener((current, total) ->
				mainFrame.setTotalProgress(total <= 0 ? 0 : (int) (current * 100 / total))
		);

		String resumeFromId = promptForResume(configData);

		try {
			installManager.startInstallation(configData, mainFrame.isFullInstallSelected(), resumeFromId);
		} catch (Exception e) {
			logManager.getUiLogger().error(localizer.translate("ERR_INSTALLATION_FAILED"));
			logManager.getFileLogger().error("Installation failed: " + e.getMessage());
		}
	}

	public void onLaunchClick() {
		if (Files.notExists(Paths.get(AppConfig.MO2_EXE_PATH))) {
			logManager.getUiLogger().warn(localizer.translate("ERR_INVALID_INSTALL_DIR"));
			logManager.getFileLogger().warn("Please move the installer into the MO2 directory");
			return;
		}

		try {
			Runtime.getRuntime().exec(AppConfig.MO2_EXE_PATH, null, new File(AppConfig.MO2_DIRECTORY));
		} catch (IOException e) {
			logManager.getUiLogger().error(localizer.translate("ERR_LAUNCH_MO2_FAIL"));
			logManager.getFileLogger().error("Failed to launch MO2: " + e.getMessage());
		}
	}

	private String promptForResume(ConfigData configData) {
		if (!progressStore.hasSavedState()) {
			return null;
		}

		String savedId = progressStore.load();
		if (savedId == null) {
			return null;
		}

		String entryName = findEntryNameById(configData, savedId);
		String displayName = (entryName != null) ? entryName : savedId;

		int choice = JOptionPane.showConfirmDialog(
				mainFrame,
				localizer.translate("DLG_RESUME_MESSAGE", displayName),
				localizer.translate("DLG_RESUME_TITLE"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
		);

		if (choice == JOptionPane.YES_OPTION) {
			return savedId;
		}

		progressStore.clear();
		return null;
	}

	private String findEntryNameById(ConfigData configData, String id) {
		return Stream.of(configData.getAddons(), configData.getPatches(), configData.getSeparators())
				.flatMap(List::stream)
				.filter(entry -> entry.getId().equals(id))
				.map(ConfigEntry::getName)
				.findFirst()
				.orElse(null);
	}
}
