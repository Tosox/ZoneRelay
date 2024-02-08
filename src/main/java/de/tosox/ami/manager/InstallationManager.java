package de.tosox.ami.manager;

import de.tosox.ami.Main;
import de.tosox.ami.handler.CrashHandler;
import de.tosox.ami.handler.ModListParser;
import de.tosox.ami.localizer.Localizer;
import de.tosox.ami.logger.Logger;
import de.tosox.ami.logger.UILogger;
import de.tosox.ami.models.ModList;
import de.tosox.ami.utils.Globals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InstallationManager {
    private final UILogger uiLogger = UILogger.getInstance();
    private final Logger logger = Logger.getInstance();
    private final Localizer localizer = Main.getLocalizer();

    private static String ADDON_META = "";
    private static String SEPARATOR_META = "";

    private boolean isInstalling;

    public InstallationManager() {
        try {
            ADDON_META = String.join("\n", Files.readAllLines(Paths.get(Globals.PATH_ADDON_META)));
            SEPARATOR_META = String.join("\n", Files.readAllLines(Paths.get(Globals.PATH_SEPARATOR_META)));
        } catch (IOException e) {
            CrashHandler.showErrorDialogAndExit("Unable to read the meta data:%n%s", e.getMessage());
        }
    }

    public void startInstallation(boolean fullInstallation) {
        isInstalling = true;
        new Thread(() -> {
            uiLogger.info(localizer.translate("msg_read_mod-list_cfg"));
            ModList modList = ModListParser.parse(Globals.PATH_MOD_LIST_CFG);
            if (modList == null) {
                isInstalling = false;
                return;
            }

            uiLogger.info("\n=================================================================");
            uiLogger.info(localizer.translate("msg_create_separators"));
            uiLogger.info("=================================================================");
            modList.getSeparatorList().forEach(separator -> {
                String separatorName = separator.getName();
                String folderName = separatorName + "_separator";
                String folderPath = Globals.DIR_MO2_MODS + "/" + folderName;

                uiLogger.info(localizer.translate("msg_title_separator", separatorName));
                uiLogger.info(localizer.translate("msg_create_separator", folderPath));
                logger.info("Creating separator: %s", separatorName);

                try {
                    Files.createDirectories(Paths.get(folderPath));
                    generateMetadata(folderPath, SEPARATOR_META);
                } catch (IOException e) {
                    // TODO: logger warning
                }
            });

            isInstalling = false;
        }).start();
    }

    public void generateMetadata(String path, String data) throws IOException {
        uiLogger.info(localizer.translate("msg_generate_meta"));
        Path metaPath = Paths.get(Globals.DIR_MO2_MODS, path, "meta.ini");
        Files.writeString(metaPath, data);
    }

    public boolean isInstalling() {
        return isInstalling;
    }
}
