package de.tosox.zonerelay.downloader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.tosox.zonerelay.logging.Logger;
import de.tosox.zonerelay.resolver.UrlResolverFactory;

@Singleton
public class DownloadStrategyFactory {
	private final Logger logger;
	private final UrlResolverFactory urlResolverFactory;
	private final DownloadFilenameResolver filenameResolver;

	@Inject
	public DownloadStrategyFactory(@Named("file") Logger logger, UrlResolverFactory urlResolverFactory,
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
