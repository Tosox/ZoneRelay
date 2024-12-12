package de.tosox.zonerelay.gui.controllers;

import de.tosox.zonerelay.Main;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logger.Logger;
import de.tosox.zonerelay.logger.UILogger;
import de.tosox.zonerelay.manager.InstallManager;
import de.tosox.zonerelay.utils.Globals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainFrameController {
    private final Logger logger = Logger.getInstance();
    private final UILogger uiLogger = UILogger.getInstance();
    private final Localizer localizer = Main.getLocalizer();

    private final InstallManager installManager = new InstallManager();

    public void onInstallClick() {
        if (Files.notExists(Paths.get(Globals.PATH_MO2_EXE))) {
            uiLogger.warn(localizer.translate("ERR_INVALID_INSTALL_DIR"));
            logger.warn("Please move the installer into the MO2 directory");
            return;
        }

        if (Files.notExists(Paths.get(Globals.PATH_MO2_CFG))) {
            uiLogger.warn(localizer.translate("ERR_LAUNCH_MO2"));
            logger.warn("Please launch MO2 once first");
            return;
        }

        installManager.startInstallation(Main.getMainFrame().isFullInstall());
    }

    public void onLaunchClick() {
        if (Files.notExists(Paths.get(Globals.PATH_MO2_EXE))) {
            uiLogger.warn(localizer.translate("ERR_INVALID_INSTALL_DIR"));
            logger.warn("Please move the installer into the MO2 directory");
            return;
        }

        try {
            Runtime.getRuntime().exec(Globals.PATH_MO2_EXE, null, new File(Globals.DIR_MO2));
        } catch (IOException e) {
            uiLogger.error(localizer.translate("ERR_LAUNCH_MO2_FAIL"));
            logger.error("An error occurred while trying to run MO2:%n%s", e.getMessage());
        }
    }
}
