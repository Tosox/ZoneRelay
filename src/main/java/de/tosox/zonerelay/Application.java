package de.tosox.zonerelay;

import de.tosox.zonerelay.config.MO2ConfigReader;
import de.tosox.zonerelay.config.UserSettings;
import de.tosox.zonerelay.downloader.DownloadFilenameResolver;
import de.tosox.zonerelay.downloader.DownloadStrategyFactory;
import de.tosox.zonerelay.gui.MainFrame;
import de.tosox.zonerelay.gui.MainFrameController;
import de.tosox.zonerelay.handler.CrashHandler;
import de.tosox.zonerelay.installer.InstallerFactory;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logging.LogManager;
import de.tosox.zonerelay.manager.InstallManager;
import de.tosox.zonerelay.resolver.UrlResolverFactory;
import de.tosox.zonerelay.service.MetaIniService;
import de.tosox.zonerelay.service.ModDownloadService;
import de.tosox.zonerelay.util.ExtractionUtils;
import de.tosox.zonerelay.util.ImageLoader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Application {
	private MainFrame mainFrame;

	public Application() {
		AppConfig config = new AppConfig();
		JTextPane outputPane = new JTextPane();
		LogManager logManager = new LogManager(config, outputPane);
		CrashHandler crashHandler = new CrashHandler(logManager.getFileLogger());
		UserSettings settings = UserSettings.load(new File("user_config.yaml"));

		Localizer localizer;
		try {
			localizer = new Localizer(config, settings.getLanguage());
		} catch (IOException e) {
			crashHandler.fatal("An error occurred while trying to initialize the Localizer", e);
			return; // unreachable
		}

		ImageLoader imageLoader;
		try {
			imageLoader = new ImageLoader();
		} catch (Exception e) {
			crashHandler.fatal("An error occurred while trying to initialize the ImageLoader", e);
			return; // unreachable
		}

		InstallManager installmanager = createInstallManager(config, logManager, localizer);
		this.mainFrame = new MainFrame(config, localizer, imageLoader, outputPane);
		MainFrameController mainFrameController = new MainFrameController(
				config,
				localizer,
				logManager,
				mainFrame,
				installmanager
		);

		mainFrame.setController(mainFrameController);
		logManager.getUiLogger().info(localizer.translate("MSG_WELCOME_MESSAGE", config.getAppName()));
	}

	private InstallManager createInstallManager(AppConfig config, LogManager logManager, Localizer localizer) {
		ExtractionUtils extractionUtils = new ExtractionUtils(config, logManager.getFileLogger());
		MO2ConfigReader mo2ConfigReader = new MO2ConfigReader(Path.of(config.getMo2ConfigPath()));
		MetaIniService metaIniService = new MetaIniService(config, logManager.getFileLogger());
		DownloadFilenameResolver downloadFilenameResolver = new DownloadFilenameResolver(logManager.getFileLogger());
		UrlResolverFactory urlResolverFactory = new UrlResolverFactory(logManager.getFileLogger());
		DownloadStrategyFactory downloadStrategyFactory = new DownloadStrategyFactory(logManager.getFileLogger(), urlResolverFactory, downloadFilenameResolver);
		InstallerFactory installerFactory = new InstallerFactory(config, logManager, localizer, extractionUtils, mo2ConfigReader, metaIniService);
		ModDownloadService modDownloadService = new ModDownloadService(config, logManager.getFileLogger(), downloadStrategyFactory);
		return new InstallManager(config, logManager, localizer, installerFactory, modDownloadService);
	}

	public void start() {
		mainFrame.showWindow();
	}
}
