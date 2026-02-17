package de.tosox.zonerelay.resolver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.tosox.zonerelay.logging.Logger;

@Singleton
public class UrlResolverFactory {
	private final Logger logger;

	@Inject
	public UrlResolverFactory(@Named("file") Logger logger) {
		this.logger = logger;
	}

	public UrlResolver getResolver(String url) {
		if (url.contains("moddb.com")) {
			return new ModDBUrlResolver(logger);
		}

		return directUrl -> directUrl;
	}
}
