package de.tosox.zonerelay.service;

import de.tosox.zonerelay.AppConfig;
import de.tosox.zonerelay.downloader.DownloadStrategy;
import de.tosox.zonerelay.downloader.DownloadStrategyFactory;
import de.tosox.zonerelay.logging.Logger;
import de.tosox.zonerelay.model.ConfigEntry;
import de.tosox.zonerelay.util.ProgressListener;

import java.io.File;

public class ModDownloadService {
	private final AppConfig config;
	private final Logger logger;
	private final DownloadStrategyFactory downloadStrategyFactory;
	private final File downloadDirectory;

	public ModDownloadService(AppConfig config, Logger logger, DownloadStrategyFactory downloadStrategyFactory) {
		this.config = config;
		this.logger = logger;
		this.downloadStrategyFactory = downloadStrategyFactory;

		this.downloadDirectory = new File(config.getDownloadsDirectory());
		if (!downloadDirectory.exists()) {
			downloadDirectory.mkdirs();
		}
	}

	public File download(ConfigEntry entry, String url, ProgressListener listener) throws Exception {
		DownloadStrategy strategy = downloadStrategyFactory.getStrategy(url);
		return strategy.download(entry, downloadDirectory.toPath(), listener);
	}
}
