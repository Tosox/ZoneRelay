package de.tosoxdev.cmi.gui.utils;

import de.tosoxdev.cmi.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageUtils {
    private ImageUtils() {}

    public static BufferedImage getFromPath(URL filepath) {
        try {
            return ImageIO.read(filepath);
        } catch (IOException e) {
            Main.getCrashHandler().showErrorDialogAndExit(String.format("Unable to find resource '%s'", filepath));
            return null;
        }
    }
}
