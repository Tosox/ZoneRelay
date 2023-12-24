package de.tosoxdev.ami.utils;

import de.tosoxdev.ami.Main;
import de.tosoxdev.ami.logger.LoggerEx;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionUtils {
    private static final LoggerEx LOGGER = Main.getLogger();

    private ConnectionUtils() {}

    public static URL createUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            LOGGER.warn("Unable to convert %s into a URL object", url);
            return null;
        }
    }

    public static HttpURLConnection createGetConnectionSimple(URL url) {
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
