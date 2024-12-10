package de.tosox.zonerelay.utils;

import de.tosox.zonerelay.handler.CrashHandler;

import javax.imageio.ImageIO;
import java.awt.*;
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

    public static BufferedImage resize(BufferedImage image, int newX, int newY) {
        if (image == null) {
            return null;
        }

        return toBufferedImage(image.getScaledInstance(newX, newY, Image.SCALE_SMOOTH));
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = image.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return image;
    }

    private static BufferedImage getFallbackImage() {
        URL resource = ImageUtils.class.getResource("missing_texture.png");
        if (resource == null) {
            CrashHandler.showErrorDialog("Couldn't determine the patch of the resource for the fallback image");
            throw new RuntimeException("Fallback image resource URL is null");
        }

        BufferedImage image;
        try {
            image = ImageIO.read(resource);
        } catch (IOException e) {
            CrashHandler.showErrorDialog("An exception occurred while trying to read the contents of the fallback image:%n%s", e);
            throw new RuntimeException("Couldn't read the fallback image", e);
        }

        if (image == null) {
            CrashHandler.showErrorDialog("Couldn't read the contents of the fallback image");
            throw new RuntimeException("Fallback image is null");
        }

        return image;
    }
}
