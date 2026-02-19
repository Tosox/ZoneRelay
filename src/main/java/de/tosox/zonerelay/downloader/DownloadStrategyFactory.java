package de.tosox.zonerelay.downloader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.tosox.zonerelay.logging.Logger;

@Singleton
public class DownloadStrategyFactory {
	private final Logger logger;

	@Inject
	public DownloadStrategyFactory(@Named("file") Logger logger) {
		this.logger = logger;
	}

	public DownloadStrategy getStrategy(String url) {
		// TODO: GitHub cloning?
		return new UrlDownloader(logger);
	}
}
