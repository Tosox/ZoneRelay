package de.tosoxdev.ami.handler;

import de.tosoxdev.ami.Main;
import de.tosoxdev.ami.logger.Logger;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CrashHandler {
    private static final Path CRASH_DATA_PATH = Paths.get("crash.dat");
    private final Logger logger = Main.getLogger();

    public void saveCurrentSection(String section) {
        try {
            Files.writeString(CRASH_DATA_PATH, section, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.warn("An error occurred while trying to save the crash data: %s", e.getMessage());
        }
    }

    public String loadCrashSection() {
        if (!Files.exists(CRASH_DATA_PATH)) {
            return null;
        }

        try {
            String section = Files.readString(CRASH_DATA_PATH, StandardCharsets.UTF_8);
            Files.delete(CRASH_DATA_PATH);
            return section;
        } catch (IOException e) {
            logger.warn("An error occurred while trying to load the crash data: %s", e.getMessage());
            return null;
        }
    }

    public void showErrorDialogAndExit(String message) {
        JOptionPane.showMessageDialog(null, message, "Fatal error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
