package de.tosox.zonerelay.resolver;

import de.tosox.zonerelay.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class ModDBUrlResolver implements UrlResolver {
	private static final OkHttpClient CLIENT = new OkHttpClient();
	private static final String MODDB_URL = "https://moddb.com/";
	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux i686; rv:57.0) Gecko/20100101 Firefox/57.0";

	private final Logger logger;

	public ModDBUrlResolver(Logger logger) {
		this.logger = logger;
	}

	@Override
	public String resolve(String url) throws Exception {
		try {
			Document addonPage = fetchPage(url);
			Element downloadElem = addonPage.getElementById("downloadmirrorstoggle");
			if (downloadElem == null) {
				logger.error("Download element not found on ModDB page.");
				return null;
			}

			String relDownloadUrl = downloadElem.attr("href");
			String downloadPageUrl = MODDB_URL + relDownloadUrl;
			Document downloadPage = fetchPage(downloadPageUrl);

			Element downloadLinkElement = downloadPage.selectFirst("a[href]");
			if (downloadLinkElement == null) {
				logger.error("Download link not found on ModDB download page.");
				return null;
			}

			String relDownloadLink = downloadLinkElement.attr("href");
			return MODDB_URL + relDownloadLink;
		} catch (Exception e) {
			logger.error("Error resolving ModDB download link: %s", e.getMessage());
			throw e;
		}
	}

	private Document fetchPage(String url) throws IOException {
		Request request = new Request.Builder()
				.url(url)
				.header("User-Agent", USER_AGENT)
				.build();

		try (Response response = CLIENT.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Failed to load page: " + url + " (Status code: " + response.code() + ")");
			}

			ResponseBody body = response.body();
			if (body == null) {
				throw new IOException("Empty response body for URL: " + url);
			}

			return Jsoup.parse(body.string());
		}
	}
}
