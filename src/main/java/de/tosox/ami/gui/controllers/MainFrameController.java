package de.tosox.ami.gui.controllers;

import de.tosox.ami.Main;
import de.tosox.ami.localizer.Localizer;
import de.tosox.ami.logger.Logger;
import de.tosox.ami.logger.UILogger;
import de.tosox.ami.manager.InstallationManager;
import de.tosox.ami.utils.Globals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainFrameController {
    private final Logger logger = Logger.getInstance();
    private final UILogger uiLogger = UILogger.getInstance();
    private final Localizer localizer = Main.getLocalizer();

    private final InstallationManager installationManager = new InstallationManager();

    public void onInstallClick() {
        if (Files.notExists(Paths.get(Globals.PATH_MO2_EXE))) {
            uiLogger.warn(localizer.translate("err_invalid_install_dir"));
            logger.warn("Please move the installer into the MO2 directory");
            return;
        }

        if (Files.notExists(Paths.get(Globals.PATH_MO2_CFG))) {
            uiLogger.warn(localizer.translate("err_launch_mo2"));
            logger.warn("Please launch MO2 once first");
            return;
        }

        if (installationManager.isInstalling()) {
            uiLogger.warn(localizer.translate("err_already_installing"));
            logger.warn("An installation is currently already running");
            return;
        }

        installationManager.startInstallation(Main.getMainFrame().isFullInstall());
    }

    public void onLaunchClick() {
        if (Files.notExists(Paths.get(Globals.PATH_MO2_EXE))) {
            uiLogger.warn(localizer.translate("err_invalid_install_dir"));
            logger.warn("Please move the installer into the MO2 directory");
            return;
        }

        try {
            Runtime.getRuntime().exec(Globals.PATH_MO2_EXE, null, new File(Globals.DIR_MO2));
        } catch (IOException e) {
            uiLogger.error(localizer.translate("err_launch_mo2_fail"));
            logger.error("An error occurred while trying to run MO2:%n%s", e.getMessage());
        }
    }
}
