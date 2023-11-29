package de.tosoxdev.cmi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionUtils.class);

    private ConnectionUtils() {}

    public static URL createUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            LOGGER.warn("Unable to convert {} into a URL object", url);
            return null;
        }
    }

    public static HttpURLConnection createGetConnectionSimple(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                LOGGER.warn("Couldn't create connection: {} {}", connection.getResponseCode(), connection.getResponseMessage());
                return null;
            }
            return connection;
        } catch (Exception e) {
            LOGGER.warn("Couldn't create connection: {}", e.getMessage());
            return null;
        }
    }
}
