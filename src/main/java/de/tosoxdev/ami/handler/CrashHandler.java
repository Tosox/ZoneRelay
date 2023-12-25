package de.tosoxdev.ami.handler;

import javax.swing.*;

public class CrashHandler {
    private CrashHandler() {}

    public static void showErrorDialogAndExit(String message) {
        JOptionPane.showMessageDialog(null, message, "Fatal error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
