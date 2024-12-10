package de.tosox.zonerelay.utils;

import de.tosox.zonerelay.logger.Logger;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionUtils {
    private static final Logger LOGGER = Logger.getInstance();

    private ConnectionUtils() {}

    public static URL createUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            LOGGER.warn("Unable to convert %s into a URL object", url);
            return null;
        }
    }

    public static HttpURLConnection createHeadConnection(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                LOGGER.warn("Couldn't create connection: %d %s", connection.getResponseCode(), connection.getResponseMessage());
                return null;
            }
            return connection;
        } catch (Exception e) {
            LOGGER.warn("Couldn't create connection: %s", e.getMessage());
            return null;
        }
    }
}
