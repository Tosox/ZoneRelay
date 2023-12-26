package de.tosoxdev.ami.gui.controllers;

import de.tosoxdev.ami.logger.Logger;
import de.tosoxdev.ami.logger.UILogger;
import de.tosoxdev.ami.utils.Globals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainFrameController {
    private final Logger logger = Logger.getInstance();
    private final UILogger uiLogger = UILogger.getInstance();

    public void onInstallClick() {
        if (Files.notExists(Paths.get(Globals.PATH_MO2_EXE))) {
            uiLogger.warn("Please move the installer into the MO2 directory");
            logger.warn("Please move the installer into the MO2 directory");
            return;
        }

        if (Files.notExists(Paths.get(Globals.PATH_MO2_CFG))) {
            uiLogger.warn("Please move the installer into the MO2 directory");
            logger.warn("Please move the installer into the MO2 directory");
            return;
        }


    }

    public void onLaunchClick() {
        if (Files.notExists(Paths.get(Globals.PATH_MO2_EXE))) {
            uiLogger.warn("Please move the installer into the MO2 directory");
            logger.warn("Please move the installer into the MO2 directory");
            return;
        }

        try {
            Runtime.getRuntime().exec(Globals.PATH_MO2_EXE, null, new File(Globals.DIR_MO2));
        } catch (IOException e) {
            uiLogger.error("An error occurred while trying to run Mod Organizer 2");
            logger.error("An error occurred while trying to run Mod Organizer 2:%n%s", e.getMessage());
        }
    }
}
