package de.tosox.zonerelay.downloader;

import de.tosox.zonerelay.logging.Logger;
import de.tosox.zonerelay.model.ConfigEntry;
import de.tosox.zonerelay.model.Downloadable;
import de.tosox.zonerelay.resolver.UrlResolver;
import de.tosox.zonerelay.resolver.UrlResolverFactory;
import de.tosox.zonerelay.util.ProgressInputStream;
import de.tosox.zonerelay.util.ProgressListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class UrlDownloader implements DownloadStrategy {
	private static final OkHttpClient CLIENT = new OkHttpClient();
	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux i686; rv:57.0) Gecko/20100101 Firefox/57.0";

	private final Logger logger;
	private final UrlResolverFactory urlResolverFactory;
	private final DownloadFilenameResolver filenameResolver;

	public UrlDownloader(Logger logger, UrlResolverFactory urlResolverFactory,
	                     DownloadFilenameResolver filenameResolver) {
		this.logger = logger;
		this.urlResolverFactory = urlResolverFactory;
		this.filenameResolver = filenameResolver;
	}

	@Override
	public File download(ConfigEntry entry, Path destination, ProgressListener listener) throws Exception {
		if (!(entry instanceof Downloadable downloadable)) {
			throw new IllegalArgumentException("Expected Downloadable, got " + entry.getClass().getSimpleName());
		}

		UrlResolver resolver = urlResolverFactory.getResolver(downloadable.getUrl());
		String directUrl = resolver.resolve(downloadable.getUrl());

		String filename = filenameResolver.resolve(directUrl);
		File archive = destination.resolve(filename).toFile();

		Request request = new Request.Builder()
				.url(directUrl)
				.header("User-Agent", USER_AGENT)
				.build();

		try (Response response = CLIENT.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Download failed: " + response.code() + " " + response.message());
			}

			ResponseBody body = response.body();
			if (body == null) {
				throw new IOException("Empty response body");
			}

			try (ProgressInputStream inputStream = new ProgressInputStream(body.byteStream(), body.contentLength(), listener);
			     FileOutputStream outputStream = new FileOutputStream(archive)) {
				byte[] buffer = new byte[8192];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
			}
		}

		return archive;
	}
}
