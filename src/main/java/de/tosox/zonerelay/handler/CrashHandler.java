package de.tosox.zonerelay.handler;

import javax.swing.*;

public class CrashHandler {
    private CrashHandler() {}

    public static void showErrorDialog(String message, Object... format) {
        JOptionPane.showMessageDialog(null, String.format(message, format), "Fatal error", JOptionPane.ERROR_MESSAGE);
    }
}
