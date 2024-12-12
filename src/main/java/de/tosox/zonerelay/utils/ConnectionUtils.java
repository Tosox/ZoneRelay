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
}
