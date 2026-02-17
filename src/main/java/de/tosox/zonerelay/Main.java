package de.tosox.zonerelay;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class Main {
    public static void main(String[] args) {
        FlatMacDarkLaf.setup();

        Application app = new Application();
        app.start();
    }
}
