package de.tosox.zonerelay.manager;

import de.tosox.zonerelay.Main;
import de.tosox.zonerelay.handler.CrashHandler;
import de.tosox.zonerelay.handler.ModlistParser;
import de.tosox.zonerelay.http.FileDownload;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logger.Logger;
import de.tosox.zonerelay.logger.UILogger;
import de.tosox.zonerelay.models.Modlist;
import de.tosox.zonerelay.models.components.Addon;
import de.tosox.zonerelay.models.components.Patch;
import de.tosox.zonerelay.models.components.Separator;
import de.tosox.zonerelay.utils.Globals;
import mslinks.ShellLink;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.Properties;

public class InstallManager {
    private final UILogger uiLogger = UILogger.getInstance();
    private final Logger logger = Logger.getInstance();
    private final Localizer localizer = Main.getLocalizer();
    private final CrashManager crashManager = new CrashManager();

    private static final String ADDON_META;
    private static final String SEPARATOR_META;
    private boolean isInstalling;

    static {
        try {
            ADDON_META = Files.readString(Paths.get(Globals.PATH_ADDON_META));
            SEPARATOR_META = Files.readString(Paths.get(Globals.PATH_SEPARATOR_META));
        } catch (IOException e) {
            CrashHandler.showErrorDialog("Failed to load meta data:%n%s", e);
            throw new RuntimeException("Failed to load meta data", e);
        }
    }

    public void startInstallation(boolean fullInstallation) {
        if (isInstalling) {
            uiLogger.warn(localizer.translate("ERR_ALREADY_INSTALLING"));
            logger.warn("Installation already in progress");
            return;
        }

        new Thread(this::runInstallation).start();
    }

    private void runInstallation() {
        String lastCrashId = crashManager.loadCrashData();

        try {
            uiLogger.info(localizer.translate("MSG_READ_MODLIST_CFG"));
            Modlist modList = ModlistParser.parse(Globals.PATH_MOD_LIST_CFG);
            if (modList == null) {
                isInstalling = false;
                return;
            }

            uiLogger.info("\n=================================================================");
            uiLogger.info(localizer.translate("MSG_CREATE_SEPARATORS"));
            uiLogger.info("=================================================================");
            for (Separator separator : modList.separators()) {
                if ((lastCrashId != null) && (!lastCrashId.equals(separator.getId()))) {
                    continue;
                }

                crashManager.saveCrashData(separator.getId());
                handleSeparator(separator);
            }

            uiLogger.info("\n=================================================================");
            uiLogger.info(localizer.translate("MSG_INSTALLING_ADDONS"));
            uiLogger.info("=================================================================");
            for (Addon addon : modList.addons()) {
                if ((lastCrashId != null) && (!lastCrashId.equals(addon.getId()))) {
                    continue;
                }

                crashManager.saveCrashData(addon.getId());
                handleAddon(addon);
            }

            uiLogger.info("\n=================================================================");
            uiLogger.info(localizer.translate("MSG_INSTALLING_PATCHES"));
            uiLogger.info("=================================================================");
            for (Patch patch : modList.patches()) {
                if ((lastCrashId != null) && (!lastCrashId.equals(patch.getId()))) {
                    continue;
                }

                crashManager.saveCrashData(patch.getId());
                handlePatch(patch);
            }

            uiLogger.info("\n=================================================================");
            uiLogger.info(localizer.translate("MSG_INSTALLATION_MO2_SETUP"));
            uiLogger.info("=================================================================");
            setupMO2Profile();
            copySplash();
            createShortcut();

            uiLogger.info("\n=================================================================");
            uiLogger.info(localizer.translate("MSG_INSTALLATION_CLEANUP"));
            uiLogger.info("=================================================================");
            FileUtils.forceDelete(new File(Globals.DIR_TEMP));
            crashManager.clearCrashData();

            uiLogger.info(localizer.translate("MSG_COMPLETE_INSTALLATION"));
            logger.info("Installation complete");
        } catch (Exception e) {
            CrashHandler.showErrorDialog("An error occurred while trying to install the modlist: %n%s", e);
            throw new RuntimeException("An error occurred while trying to install the modlist", e);
        } finally {
            isInstalling = false;
        }
    }

    private void handleAddon(Addon addon) throws MalformedURLException {
        URL addonUrl = new URL(addon.getUrl());
        String addonName = addon.getName();

        uiLogger.info(localizer.translate("MSG_TITLE_ADDON", addonName));
        logger.info("Creating addon: %s", addonName);

        FileDownload downloadFile = new FileDownload(addonUrl);
        String downloadFileName = downloadFile.getFilename();
        if (downloadFileName == null) {
            throw new RuntimeException("Unable to resolve the filename");
        }

        // Initialize variables
        Path archiveOutput = Paths.get(Globals.DIR_TEMP, downloadFileName);
        Path archivePath = Paths.get(Globals.DIR_DOWNLOADS, downloadFileName);
        Path addonPath = Paths.get(Globals.DIR_MO2_MODS, addonName);

        // Download and extract addon
        if (archivePath.toFile().isFile()) {
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
                throw new RuntimeException("An error occurred while trying to download the addon", e);
            }
            extract(archivePath, archiveOutput);
        }

        uiLogger.info(localizer.translate("MSG_ADDON_DELETE_OLD_VERSION", addonPath));
        logger.info("Deleting content of previous version in %s", addonPath);
        try {
            FileUtils.deleteDirectory(addonPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while trying to delete the old addon contents", e);
        }

        uiLogger.info(localizer.translate("MSG_READ_SETUP"));
        logger.info("Reading setup instructions");
        for (String instruction : addon.getSetup()) {
            Path sourceDirectory = archiveOutput.resolve(instruction);
            Path destinationDirectory = addonPath.resolve(instruction.substring(instruction.lastIndexOf("/") + 1));

            uiLogger.info(localizer.translate("MSG_COPY_TO", instruction, instruction.substring(instruction.lastIndexOf("/") + 1)));
            logger.info("Copying %s to %s", sourceDirectory, destinationDirectory);
            try {
                FileUtils.copyDirectory(sourceDirectory.toFile(), destinationDirectory.toFile());
            } catch (IOException e) {
                throw new RuntimeException("An error occurred while trying to execute the instructions", e);
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

    private void handlePatch(Patch patch) throws MalformedURLException {
        URL patchUrl = new URL(patch.getUrl());
        String patchName = patch.getName();

        uiLogger.info(localizer.translate("MSG_TITLE_ADDON", patchName));
        logger.info("Creating patch: %s", patchName);

        FileDownload downloadFile = new FileDownload(patchUrl);
        String downloadFileName = downloadFile.getFilename();
        if (downloadFileName == null) {
            throw new RuntimeException("Unable to resolve the filename");
        }

        // Initialize variables
        Path archiveOutput = Paths.get(Globals.DIR_TEMP, downloadFileName);
        Path archivePath = Paths.get(Globals.DIR_DOWNLOADS, downloadFileName);
        Path patchPath = getGamePath();

        // Download and extract patch
        uiLogger.info(localizer.translate("MSG_DOWNLOADING_TO", archivePath));
        logger.info("Downloading to %s", archivePath);
        try {
            Files.createDirectories(archivePath.getParent());
            Files.write(archivePath, downloadFile.getContent());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while trying to download the patch", e);
        }
        extract(archivePath, archiveOutput);

        uiLogger.info(localizer.translate("MSG_READ_SETUP"));
        logger.info("Reading setup instructions");
        for (String instruction : patch.getSetup()) {
            Path sourceDirectory = archiveOutput.resolve(instruction);
            Path destinationDirectory = patchPath.resolve(instruction.substring(instruction.lastIndexOf("/") + 1));

            uiLogger.info(localizer.translate("MSG_COPY_TO", instruction, instruction.substring(instruction.lastIndexOf("/") + 1)));
            logger.info("Copying %s to %s", sourceDirectory, destinationDirectory);
            try {
                FileUtils.copyDirectory(sourceDirectory.toFile(), destinationDirectory.toFile());
            } catch (IOException e) {
                throw new RuntimeException("An error occurred while trying to execute the instructions", e);
            }
        }
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
            throw new RuntimeException("An error occurred while trying to extract an archive", e);
        }
    }

    private void generateMetadata(Path path, String data) {
        uiLogger.info(localizer.translate("MSG_GENERATE_META"));

        try {
            Path metaPath = path.resolve("meta.ini");
            Files.createDirectories(metaPath.getParent());
            Files.writeString(metaPath, data);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while trying to write the meta data", e);
        }
    }

    private Path getGamePath() {
        try (FileReader reader = new FileReader(Globals.PATH_MO2_CFG)) {
            // Load the configuration file
            Properties properties = new Properties();
            properties.load(reader);

            // Retrieve the game path
            String gamePath = properties.getProperty("gamePath");
            if (gamePath != null) {
                // Process and clean the game path
                return Paths.get(gamePath.replace("@ByteArray(", "").replace(")", ""));
            } else {
                throw new IllegalStateException("gamePath not found in the configuration file");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read MO2 configuration file", e);
        }
    }

    public void createShortcut() {
        uiLogger.info(localizer.translate("MSG_CREATE_SHORTCUT"));

        String desktopPath = System.getProperty("user.home") + "/Desktop";
        Path shortcutPath = Paths.get(desktopPath, "S.T.A.L.K.E.R. VIP.lnk"); // TODO: make name customizable

        try {
            ShellLink.createLink(Path.of(Globals.PATH_MO2_EXE).toAbsolutePath().normalize().toString())
                    .setIconLocation(Path.of(Globals.PATH_MOD_LIST_ICO).toAbsolutePath().normalize().toString()) // TODO: make icon optional
                    .saveTo(shortcutPath.toString());
        } catch (IOException e) {
            throw new RuntimeException("Unable to create shortcut", e);
        }
    }

    public void copySplash() {
        Path splashImagePath = Path.of(Globals.PATH_MOD_LIST_SPLASH);
        if (!Files.exists(splashImagePath)) {
            return;
        }

        uiLogger.info(localizer.translate("MSG_COPY", "splash.png"));
        try {
            Files.copy(splashImagePath, Paths.get(Globals.DIR_MO2, "splash.png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy splash screen", e);
        }
    }

    public void setupMO2Profile() {
        // Copy default profile files
        uiLogger.info(localizer.translate("MSG_CREATE_CUSTOM_PROFILE"));
        Path newProfilePath = Paths.get(Globals.DIR_MO2_PROFILES, "VIP"); // TODO: make name customizable
        try {
            FileUtils.copyDirectory(new File(Globals.DIR_PROFILE_FILES), newProfilePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy profile files", e);
        }

        // Copy modlist.txt
        uiLogger.info(localizer.translate("MSG_COPY", "modlist.txt"));
        try {
            FileUtils.copyFile(
                    Paths.get(Globals.PATH_MOD_LIST_MO2).toFile(),
                    newProfilePath.resolve("modlist.txt").toFile()
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy modlist.txt", e);
        }

        // TODO: Change to custom profile in MorOrganizer.ini
    }
}
