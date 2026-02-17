package de.tosox.zonerelay.resolver;

import de.tosox.zonerelay.logging.Logger;

public class UrlResolverFactory {
	private final Logger logger;

	public UrlResolverFactory(Logger logger) {
		this.logger = logger;
	}

	public UrlResolver getResolver(String url) {
		if (url.contains("moddb.com")) {
			return new ModDBUrlResolver(logger);
		}

		return directUrl -> directUrl;
	}
}
