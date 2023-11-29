package de.tosoxdev.cmi.gui;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame(String title) {
        this.setTitle(title);
        this.setOpacity(0.9f);
        this.setResizable(false);
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setVisible() {
        this.setVisible(true);
    }

    @Override
    public boolean isUndecorated() {
        return true;
    }
}
