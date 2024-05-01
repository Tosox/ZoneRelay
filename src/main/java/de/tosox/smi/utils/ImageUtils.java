package de.tosox.smi.utils;

import de.tosox.smi.handler.CrashHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageUtils {
    private static BufferedImage fallbackImage;

    private ImageUtils() {}

    public static BufferedImage getFromPath(URL filepath) {
        if (fallbackImage == null) {
            fallbackImage = getFallbackImage();
        }

        if (filepath == null) {
            return fallbackImage;
        }

        try {
            BufferedImage image = ImageIO.read(filepath);
            return image != null ? image : fallbackImage;
        } catch (IOException e1) {
            return fallbackImage;
        }
    }

    private static BufferedImage getFallbackImage() {
        URL resource = ImageUtils.class.getResource("missing_texture.png");
        if (resource == null) {
            CrashHandler.showErrorDialogAndExit("Couldn't determine the URL of the resource of the fallback image");
            return null; // Unreachable
        }

        BufferedImage image;
        try {
            image = ImageIO.read(resource);
        } catch (IOException e) {
            CrashHandler.showErrorDialogAndExit("An exception occurred while trying to read the contents of the fallback image:%n%s", e.getMessage());
            return null; // Unreachable
        }

        if (image == null) {
            CrashHandler.showErrorDialogAndExit("Couldn't read the contents of the fallback image");
            return null; // Unreachable
        }

        return image;
    }
}
