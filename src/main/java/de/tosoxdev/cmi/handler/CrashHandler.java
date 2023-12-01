package de.tosoxdev.cmi.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CrashHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrashHandler.class);
    private static final Path CRASH_DATA_PATH = Paths.get("crash.dat");

    public void saveCurrentSection(String section) {
        try {
            Files.writeString(CRASH_DATA_PATH, section, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.warn("An error occurred while trying to save the crash data: {}", e.getMessage());
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
            LOGGER.warn("An error occurred while trying to load the crash data: {}", e.getMessage());
            return null;
        }
    }

    public void showErrorDialogAndExit(String message) {
        JOptionPane.showMessageDialog(null, message, "Fatal error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
