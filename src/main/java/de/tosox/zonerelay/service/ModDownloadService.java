package de.tosox.zonerelay.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.tosox.zonerelay.AppConfig;
import de.tosox.zonerelay.downloader.DownloadStrategy;
import de.tosox.zonerelay.downloader.DownloadStrategyFactory;
import de.tosox.zonerelay.logging.Logger;
import de.tosox.zonerelay.model.ConfigEntry;
import de.tosox.zonerelay.util.ProgressListener;

import java.io.File;

@Singleton
public class ModDownloadService {
	private final Logger logger;
	private final DownloadStrategyFactory downloadStrategyFactory;
	private final File downloadDirectory;

	@Inject
	public ModDownloadService(@Named("file") Logger logger, DownloadStrategyFactory downloadStrategyFactory) {
		this.logger = logger;
		this.downloadStrategyFactory = downloadStrategyFactory;

		this.downloadDirectory = new File(AppConfig.DOWNLOADS_DIRECTORY);
		if (!downloadDirectory.exists()) {
			downloadDirectory.mkdirs();
		}
	}

	public File download(ConfigEntry entry, String url, ProgressListener listener) throws Exception {
		DownloadStrategy strategy = downloadStrategyFactory.getStrategy(url);
		return strategy.download(entry, downloadDirectory.toPath(), listener);
	}
}
