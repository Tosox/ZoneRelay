package de.tosox.zonerelay.downloader;

import de.tosox.zonerelay.logging.Logger;
import de.tosox.zonerelay.resolver.UrlResolverFactory;

public class DownloadStrategyFactory {
	private final Logger logger;
	private final UrlResolverFactory urlResolverFactory;
	private final DownloadFilenameResolver filenameResolver;

	public DownloadStrategyFactory(Logger logger, UrlResolverFactory urlResolverFactory,
	                               DownloadFilenameResolver filenameResolver) {
		this.logger = logger;
		this.urlResolverFactory = urlResolverFactory;
		this.filenameResolver = filenameResolver;
	}

	public DownloadStrategy getStrategy(String url) {
		// TODO: GitHub cloning?
		return new UrlDownloader(logger, urlResolverFactory, filenameResolver);
	}
}
