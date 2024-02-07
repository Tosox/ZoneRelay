package de.tosox.ami.handler;

import javax.swing.*;

public class CrashHandler {
    private CrashHandler() {}

    public static void showErrorDialogAndExit(String message, Object... format) {
        JOptionPane.showMessageDialog(null, String.format(message, format), "Fatal error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
