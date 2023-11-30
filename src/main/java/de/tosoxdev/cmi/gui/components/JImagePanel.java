package de.tosoxdev.cmi.gui.components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JImagePanel extends JPanel {
    private BufferedImage image;

    public JImagePanel(String filepath) {
        try {
            image = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            image = null;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_FAST), 0, 0, this);
    }
}
