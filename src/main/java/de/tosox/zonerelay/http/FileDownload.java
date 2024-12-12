package de.tosox.zonerelay.http;

import de.tosox.zonerelay.logger.Logger;
import de.tosox.zonerelay.utils.Globals;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDownload {

    private static final Pattern FILENAME_FROM_CONTENT_DISPOSITION = Pattern.compile("filename\\*?=['\"]?(?:UTF-\\d['\"]*)?([^;\"']*)['\"]?;?");
    private static final Pattern BASE_DOMAIN_FROM_URL = Pattern.compile("(?:https?://)?(?:www\\.)?(?:[\\w-]+\\.)*([\\w-]+\\.\\w+)");
    private static final OkHttpClient CLIENT = new OkHttpClient();

    private final Logger logger = Logger.getInstance();
    private final URL directUrl;

    public FileDownload(URL url) {
        this.directUrl = resolveDirectUrl(url);
    }

    public String getFilename() {
        Request request = buildHeadRequest(directUrl);
        try (Response response = CLIENT.newCall(request).execute()) {
            return extractFilenameFromResponse(response);
        } catch (IOException e) {
            logger.warn("Failed to fetch filename from URL %s: %s", directUrl, e);
        }
        return "unknown_file";
    }

    private String extractFilenameFromResponse(Response response) {
        String contentDisposition = response.header("Content-Disposition");
        if (contentDisposition != null) {
            Matcher matcher = FILENAME_FROM_CONTENT_DISPOSITION.matcher(contentDisposition);
            if (matcher.find()) {
                logger.info("Filename extracted from Content-Disposition header");
                return matcher.group(1);
            }
        }

        logger.info("No Content-Disposition header found. Attempting to extract filename from URL");
        String requestUrl = response.request().url().toString();
        return extractFilenameFromUrl(requestUrl);
    }

    private String extractFilenameFromUrl(String url) {
        try {
            URL parsedUrl = new URL(url);
            String path = parsedUrl.getPath();
            if (path == null || path.isEmpty()) {
                logger.warn("URL path is empty: %s", url);
                return "unknown_file";
            }
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            logger.info("Filename extracted from URL: %s", fileName);
            return fileName;
        } catch (MalformedURLException e) {
            logger.error("Malformed URL while extracting filename: %s", e);
            return "unknown_file";
        }
    }

    private URL resolveDirectUrl(URL url) {
        Matcher domainMatcher = BASE_DOMAIN_FROM_URL.matcher(url.toString().toLowerCase());
        if (domainMatcher.find()) {
            String domain = domainMatcher.group(1);
	        return switch (domain) {
		        case "moddb.com" -> resolveModDBDownload(url);
		        case "github.com" -> resolveGithubDownload(url);
		        default -> url;
	        };
        }
        return url;
    }

    private URL resolveGithubDownload(URL url) {
        return url;
    }

    public URL resolveModDBDownload(URL url) {
        try {
            Document addonPage = fetchPage(url);
            Element downloadElem = addonPage.getElementById("downloadmirrorstoggle");
            if (downloadElem == null) {
                logger.error("Download element not found on ModDB page.");
                return null;
            }

            String relDownloadUrl = downloadElem.attr("href");
            URL downloadPageUrl = new URL(Globals.URL_MODDB + relDownloadUrl);
            Document downloadPage = fetchPage(downloadPageUrl);

            Element downloadLinkElement = downloadPage.selectFirst("a[href]");
            if (downloadLinkElement == null) {
                logger.error("Download link not found on ModDB download page.");
                return null;
            }

            String relDownloadLink = downloadLinkElement.attr("href");
            return new URL(Globals.URL_MODDB + relDownloadLink);
        } catch (IOException e) {
            logger.error("Error resolving ModDB download link: %s", e);
            return null;
        }
    }

    private Document fetchPage(URL url) throws IOException {
        Request request = buildGetRequest(url);
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

    public byte[] getContent() {
        Request request = buildGetRequest(directUrl);
        try (Response response = CLIENT.newCall(request).execute()) {
            ResponseBody body = response.body();
            if (body != null) {
                return body.byteStream().readAllBytes();
            }
        } catch (IOException e) {
            logger.warn("Failed to fetch content from URL %s: %s", directUrl, e);
        }
        return new byte[0];
    }

    private Request buildHeadRequest(URL url) {
        return new Request.Builder()
                .url(url)
                .head()
                .header("User-Agent", "Mozilla/5.0 (X11; Linux i686; rv:57.0) Gecko/20100101 Firefox/57.0")
                .build();
    }

    private Request buildGetRequest(URL url) {
        return new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                .build();
    }
}
