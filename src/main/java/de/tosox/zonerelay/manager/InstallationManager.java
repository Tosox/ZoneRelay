package de.tosox.zonerelay.manager;

import de.tosox.zonerelay.Main;
import de.tosox.zonerelay.handler.CrashHandler;
import de.tosox.zonerelay.handler.ModListParser;
import de.tosox.zonerelay.http.HttpFileDownload;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logger.Logger;
import de.tosox.zonerelay.logger.UILogger;
import de.tosox.zonerelay.models.components.Addon;
import de.tosox.zonerelay.models.components.Data;
import de.tosox.zonerelay.models.ModList;
import de.tosox.zonerelay.models.components.Separator;
import de.tosox.zonerelay.utils.ConnectionUtils;
import de.tosox.zonerelay.utils.Globals;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;

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
        new Thread(this::runInstallation).start();
    }

    private void runInstallation() {
        uiLogger.info(localizer.translate("MSG_READ_MODLIST_CFG"));
        ModList modList = ModListParser.parse(Globals.PATH_MOD_LIST_CFG);
        if (modList == null) {
            isInstalling = false;
            return;
        }

        uiLogger.info("\n=================================================================");
        uiLogger.info(localizer.translate("MSG_CREATE_SEPARATORS"));
        uiLogger.info("=================================================================");
        for (Separator separator : modList.getSeparatorList()) {
            handleSeparator(separator);
        }

        uiLogger.info("\n=================================================================");
        uiLogger.info(localizer.translate("MSG_INSTALLING_ADDONS"));
        uiLogger.info("=================================================================");
        for (Addon addon : modList.getAddonList()) {
            handleAddon(addon);
        }

        uiLogger.info("\n=================================================================");
        uiLogger.info(localizer.translate("MSG_INSTALLING_DATA"));
        uiLogger.info("=================================================================");
        for (Data data : modList.getDataList()) {
            handleData(data);
        }

        isInstalling = false;
    }

    private void handleAddon(Addon addon) {
        URL addonUrl = ConnectionUtils.createUrl(addon.getLink());
        if (addonUrl == null) {
            // TODO: Couldn't parse link to URL
            return;
        }

        String addonName = addon.getName();
        List<String> addonSetup = addon.getSetup();

        uiLogger.info(localizer.translate("MSG_TITLE_ADDON", addonName));
        logger.info("Creating addon: %s", addonName);

        HttpURLConnection connection = ConnectionUtils.createHeadConnection(addonUrl);
        if (connection == null) {
            // TODO: Couldn't establish connection
            return;
        }

        HttpFileDownload downloadFile = new HttpFileDownload(connection);
        String downloadFileName = downloadFile.getFilename();
        if (downloadFileName == null) {
            // TODO: Handle
            return;
        }

        // Initialize variables
        Path archiveOutput = Paths.get(Globals.DIR_TEMP, downloadFileName);
        Path archivePath = Paths.get(Globals.DIR_DOWNLOADS, downloadFileName);
        Path addonPath = Paths.get(Globals.DIR_MO2_MODS, addonName);

        // Download and extract addon
        if (archiveOutput.toFile().isDirectory()) {
            uiLogger.info(localizer.translate("MSG_ADDON_ALREADY_EXTRACTED", archiveOutput));
            logger.info("File already extracted: %s", archiveOutput);
        } else if (archivePath.toFile().isFile()) {
            uiLogger.info(localizer.translate("MSG_ADDON_ALREADY_DOWNLOADED", archivePath));
            logger.info("File already downloaded: %s", archivePath);
            extract(archivePath, archiveOutput);
        } else {
            uiLogger.info(localizer.translate("MSG_DOWNLOADING_TO", archivePath));
            logger.info("Downloading to %s", archivePath);
            try {
                Files.createDirectories(archivePath.getParent());
                Files.write(archivePath, downloadFile.getContent());
            } catch (IOException e) {
                logger.info("Failed to download addon: %n%s", e.getMessage());
                return;
            }
            extract(archivePath, archiveOutput);
        }

        uiLogger.info(localizer.translate("MSG_ADDON_DELETE_OLD_VERSION", addonPath));
        logger.info("Deleting content of previous version in %s", addonPath);
        try {
            FileUtils.deleteDirectory(addonPath.toFile());
        } catch (IOException e) {
            // TODO: error
            return;
        }

        uiLogger.info(localizer.translate("MSG_READ_SETUP"));
        logger.info("Reading setup instructions");
        for (String instruction : addonSetup) {
            Path sourceDirectory = archiveOutput.resolve(instruction);
            Path destinationDirectory = addonPath.resolve(instruction.substring(instruction.lastIndexOf("/") + 1));

            uiLogger.info(localizer.translate("MSG_COPY_TO", instruction, instruction.substring(instruction.lastIndexOf("/") + 1)));
            logger.info("Copying %s to %s", sourceDirectory, destinationDirectory);
            try {
                FileUtils.copyDirectory(sourceDirectory.toFile(), destinationDirectory.toFile());
            } catch (IOException e) {
                // TODO: logger warning
                return;
            }
        }

        generateMetadata(addonPath, MessageFormat.format(ADDON_META, downloadFileName, addonUrl));
    }

    private void handleSeparator(Separator separator) {
        String separatorName = separator.getName();
        String folderName = separatorName + "_separator";
        Path folderPath = Paths.get(Globals.DIR_MO2_MODS, folderName);

        uiLogger.info(localizer.translate("MSG_TITLE_SEPARATOR", separatorName));
        uiLogger.info(localizer.translate("MSG_CREATE_SEPARATOR", folderPath));
        logger.info("Creating separator: %s", separatorName);

        generateMetadata(folderPath, SEPARATOR_META);
    }

    private void handleData(Data data) {
        String dataUrl = data.getLink();
        List<String> dataSetup = data.getSetup();

        // uiLogger.info(localizer.translate("msg_title_addon", addonName));
        // logger.info("Creating data: %s", addonName);
    }

    private void extract(Path archivePath, Path destination) {
        uiLogger.info(localizer.translate("MSG_EXTRACT_TO", destination));
        logger.info("Extracting %s to %s", archivePath, destination);

        try {
            ProcessBuilder builder = new ProcessBuilder(
                    Globals.PATH_7Z, "-bso0",  "x", archivePath.toString(), "-o" + destination.toString(), "-y"
            );
            builder.redirectErrorStream(true);
            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Extraction process failed with exit code " + exitCode);
            }
        } catch (Exception e) {
            logger.error("Failed to extract %s: %n%s", archivePath, e.getMessage());
            // TODO: error
        }
    }

    private void generateMetadata(Path path, String data) {
        uiLogger.info(localizer.translate("MSG_GENERATE_META"));

        try {
            Path metaPath = path.resolve("meta.ini");
            Files.createDirectories(metaPath.getParent());
            Files.writeString(metaPath, data);
        } catch (IOException e) {
            // TODO: logger warning
        }
    }

    public boolean isInstalling() {
        return isInstalling;
    }
}
