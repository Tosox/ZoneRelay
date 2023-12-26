package de.tosoxdev.ami.manager;

import de.tosoxdev.ami.handler.CrashHandler;
import de.tosoxdev.ami.utils.Globals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InstallationManager {
    private static String ADDON_META = "";
    private static String SEPARATOR_META = "";

    public InstallationManager() {
        try {
            ADDON_META = String.join("", Files.readAllLines(Paths.get(Globals.PATH_ADDON_META)));
            SEPARATOR_META = String.join("", Files.readAllLines(Paths.get(Globals.PATH_SEPARATOR_META)));
        } catch (IOException e) {
            CrashHandler.showErrorDialogAndExit("Unable to retrieve the meta data:%n%s", e.getMessage());
        }
    }
}
