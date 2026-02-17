package de.tosox.zonerelay.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.tosox.zonerelay.AppConfig;
import de.tosox.zonerelay.installer.InstallerFactory;
import de.tosox.zonerelay.installer.ModInstaller;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logging.LogManager;
import de.tosox.zonerelay.model.ConfigData;
import de.tosox.zonerelay.model.ConfigEntry;
import de.tosox.zonerelay.model.Downloadable;
import de.tosox.zonerelay.model.EntryType;
import de.tosox.zonerelay.service.ModDownloadService;
import de.tosox.zonerelay.service.ProfileSetupService;
import de.tosox.zonerelay.service.ShortcutService;
import de.tosox.zonerelay.service.SplashImageService;
import de.tosox.zonerelay.util.ProgressListener;
import lombok.Setter;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class InstallManager {
	private final AppConfig config;
	private final LogManager logManager;
	private final Localizer localizer;
	private final InstallerFactory installerFactory;
	private final ModDownloadService downloadService;
	private final ProfileSetupService profileSetupService;
	private final SplashImageService splashImageService;
	private final ShortcutService shortcutService;

	private final AtomicBoolean isInstalling = new AtomicBoolean(false);

	@Setter
	private ProgressListener currentProgressListener;

	@Setter
	private ProgressListener totalProgressListener;

	@Inject
	public InstallManager(AppConfig config, LogManager logManager, Localizer localizer,
	                      InstallerFactory installerFactory, ModDownloadService modDownloadService,
	                      ProfileSetupService profileSetupService, SplashImageService splashImageService,
	                      ShortcutService shortcutService) {
		this.config = config;
		this.logManager = logManager;
		this.localizer = localizer;
		this.installerFactory = installerFactory;
		this.downloadService = modDownloadService;
		this.profileSetupService = profileSetupService;
		this.splashImageService = splashImageService;
		this.shortcutService = shortcutService;
	}

	public void startInstallation(ConfigData configData, boolean fullInstall) {
		Thread thread = new Thread(() -> {
			isInstalling.set(true);
			try {
				runInstallation(configData, fullInstall);
			} catch (Exception e) {
				logManager.getFileLogger().error("Failed to install mods: %s", e.getMessage());
				throw new RuntimeException("Failed to install mods", e);
			} finally {
				isInstalling.set(false);
			}
		});
		thread.start();
	}

	public boolean isInstalling() {
		return isInstalling.get();
	}

	private void runInstallation(ConfigData configData, boolean fullInstall) throws Exception {
		currentProgressListener.onProgressUpdate(0, 1);
		totalProgressListener.onProgressUpdate(0, 1);

		int totalMods = configData.getAddons().size()
				+ configData.getPatches().size() + 1; // Count all separators as "1"
		AtomicInteger completedMods = new AtomicInteger(0);

		logManager.getUiLogger().info("\n=================================================================");
		logManager.getUiLogger().info(localizer.translate("MSG_STARTING_INSTALLATION"));
		logManager.getUiLogger().info("=================================================================");
		installEntries(configData.getAddons(), fullInstall, totalMods, completedMods);
		installEntries(configData.getPatches(), fullInstall, totalMods, completedMods);
		installEntries(configData.getSeparators(), fullInstall, totalMods, completedMods);

		logManager.getUiLogger().info("\n=================================================================");
		logManager.getUiLogger().info(localizer.translate("MSG_INSTALLATION_MO2_SETUP"));
		logManager.getUiLogger().info("=================================================================");
		setupMo2Environment();

		logManager.getUiLogger().info("\n=================================================================");
		logManager.getUiLogger().info(localizer.translate("MSG_INSTALLATION_CLEANUP"));
		logManager.getUiLogger().info("=================================================================");

		logManager.getUiLogger().info(localizer.translate("MSG_COMPLETE_INSTALLATION"));
		logManager.getFileLogger().info("Installation completed successfully");

		currentProgressListener.onProgressUpdate(1, 1);
		totalProgressListener.onProgressUpdate(1, 1);
	}

	private void installEntries(List<? extends ConfigEntry> entries, boolean fullInstall,
	                            int totalMods, AtomicInteger completedMods) throws Exception {
		if (entries == null || entries.isEmpty()) {
			return;
		}

		// Sort Addons -> Patches -> Separators
		entries.sort(Comparator.comparingInt((ConfigEntry e) -> {
			return switch (e.getType()) {
				case ADDON -> 0;
				case PATCH -> 1;
				case SEPARATOR -> 2;
			};
		}));

		for (ConfigEntry entry : entries) {
			logManager.getUiLogger().info(localizer.translate("MSG_TITLE_CONFIGENTRY",  entry.getName()));
			logManager.getFileLogger().info("Installing entry: {0}", entry.getId());

			File archive = null;
			if (entry instanceof Downloadable downloadable) {
				logManager.getUiLogger().info(localizer.translate("MSG_DOWNLOADING_TO", entry.getName()));
				logManager.getFileLogger().info("Downloading %s", downloadable.getUrl());
				archive = downloadService.download(entry, downloadable.getUrl(), currentProgressListener);
			}

			ModInstaller installer = installerFactory.getInstaller(entry);
			installer.install(entry, archive, currentProgressListener);

			if (entry.getType() != EntryType.SEPARATOR) {
				totalProgressListener.onProgressUpdate(completedMods.incrementAndGet(), totalMods);
			}
		}
	}

	private void setupMo2Environment() {
		logManager.getUiLogger().info(localizer.translate("MSG_CREATE_CUSTOM_PROFILE"));
		logManager.getFileLogger().info("Setting up MO2 profile");
		profileSetupService.setupProfile();

		logManager.getUiLogger().info(localizer.translate("MSG_COPY", "modlist.txt"));
		logManager.getFileLogger().info("Copying modlist.txt to profile");

		logManager.getUiLogger().info(localizer.translate("MSG_COPY", "splash.png"));
		logManager.getFileLogger().info("Copying splash image");
		splashImageService.copySplashImage();

		logManager.getUiLogger().info(localizer.translate("MSG_CREATE_SHORTCUT"));
		logManager.getFileLogger().info("Creating desktop shortcut");
		shortcutService.createShortcut();
	}
}
