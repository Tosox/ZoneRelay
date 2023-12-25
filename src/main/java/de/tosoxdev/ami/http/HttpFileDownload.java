package de.tosoxdev.ami.http;

import de.tosoxdev.ami.logger.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpFileDownload {
    private static final Pattern FILENAME_FROM_CONTENT_DISPOSITION = Pattern.compile("filename\\*?=['\"]?(?:UTF-\\d['\"]*)?([^;\"']*)['\"]?;?");
    private final Logger logger = Logger.getInstance();

    private final HttpURLConnection connection;

    public HttpFileDownload(HttpURLConnection connection) {
        this.connection = connection;
    }

    public boolean isDownloadable() {
        String contentType = connection.getContentType();
        if (contentType == null) {
            return false;
        }
        return contentType.startsWith("application/");
    }

    public String getFilename() {
        String contentDisposition = connection.getHeaderField("Content-Disposition");
        if (contentDisposition == null) {
            logger.warn("Couldn't retrieve filename: 'Content-Disposition' is not available");
            return null;
        }

        Matcher filenameMatcher = FILENAME_FROM_CONTENT_DISPOSITION.matcher(contentDisposition);
        if (!filenameMatcher.find()) {
            logger.warn("Couldn't retrieve filename: 'Content-Disposition' doesn't contain a filename");
            return null;
        }

        return filenameMatcher.group(1);
    }

    public byte[] getContent() {
        try {
            return connection.getInputStream().readAllBytes();
        } catch (IOException e) {
            logger.warn("Couldn't retrieve content: {}", e.getMessage());
            return null;
        }
    }
}
