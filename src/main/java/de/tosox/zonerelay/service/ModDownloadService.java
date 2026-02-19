package de.tosox.zonerelay.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.tosox.zonerelay.AppConfig;
import de.tosox.zonerelay.downloader.DownloadFilenameResolver;
import de.tosox.zonerelay.downloader.DownloadStrategy;
import de.tosox.zonerelay.downloader.DownloadStrategyFactory;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logging.LogManager;
import de.tosox.zonerelay.resolver.UrlResolverFactory;
import de.tosox.zonerelay.util.ProgressListener;

import java.io.File;

@Singleton
public class ModDownloadService {
	private final LogManager logManager;
	private final Localizer localizer;
	private final DownloadStrategyFactory downloadStrategyFactory;
	private final UrlResolverFactory urlResolverFactory;
	private final DownloadFilenameResolver filenameResolver;
	private final File downloadDirectory;

	@Inject
	public ModDownloadService(LogManager logManager, Localizer localizer, DownloadStrategyFactory downloadStrategyFactory,
	                          UrlResolverFactory urlResolverFactory, DownloadFilenameResolver filenameResolver) {
		this.logManager = logManager;
		this.localizer = localizer;
		this.downloadStrategyFactory = downloadStrategyFactory;
		this.urlResolverFactory = urlResolverFactory;
		this.filenameResolver = filenameResolver;

		this.downloadDirectory = new File(AppConfig.DOWNLOADS_DIRECTORY);
		if (!downloadDirectory.exists()) {
			downloadDirectory.mkdirs();
		}
	}

	public File download(String url, ProgressListener listener) throws Exception {
		String resolvedUrl = urlResolverFactory.getResolver(url).resolve(url);
		File archive = new File(downloadDirectory, filenameResolver.resolve(resolvedUrl));

		if (archive.isFile()) {
			logManager.getUiLogger().info(localizer.translate("MSG_ADDON_ALREADY_DOWNLOADED"));
			logManager.getFileLogger().info("Archive already downloaded, skipping: %s", archive.getPath());
			return archive;
		}

		logManager.getUiLogger().info(localizer.translate("MSG_DOWNLOADING_ARCHIVE"));
		logManager.getFileLogger().info("Downloading %s", url);

		DownloadStrategy strategy = downloadStrategyFactory.getStrategy(url);
		return strategy.download(resolvedUrl, archive, listener);
	}
}
