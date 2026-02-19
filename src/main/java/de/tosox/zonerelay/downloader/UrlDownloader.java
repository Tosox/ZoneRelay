package de.tosox.zonerelay.downloader;

import de.tosox.zonerelay.logging.Logger;
import de.tosox.zonerelay.util.ProgressInputStream;
import de.tosox.zonerelay.util.ProgressListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UrlDownloader implements DownloadStrategy {
	private static final OkHttpClient CLIENT = new OkHttpClient();
	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux i686; rv:57.0) Gecko/20100101 Firefox/57.0";

	private final Logger logger;

	public UrlDownloader(Logger logger) {
		this.logger = logger;
	}

	@Override
	public File download(String resolvedUrl, File archive, ProgressListener listener) throws Exception {
		Request request = new Request.Builder()
				.url(resolvedUrl)
				.header("User-Agent", USER_AGENT)
				.build();

		try (Response response = CLIENT.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Download failed: " + response.code() + " " + response.message());
			}

			ResponseBody body = response.body();
			try (ProgressInputStream inputStream = new ProgressInputStream(body.byteStream(), body.contentLength(), listener);
			    FileOutputStream outputStream = new FileOutputStream(archive)) {
				byte[] buffer = new byte[8192];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
			}
		}

		logger.info("Downloaded to %s", archive.getPath());
		return archive;
	}
}
