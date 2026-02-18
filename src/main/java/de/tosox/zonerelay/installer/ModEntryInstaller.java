package de.tosox.zonerelay.installer;

import de.tosox.zonerelay.AppConfig;
import de.tosox.zonerelay.config.MO2ConfigReader;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logging.LogManager;
import de.tosox.zonerelay.model.ConfigEntry;
import de.tosox.zonerelay.model.EntryType;
import de.tosox.zonerelay.model.Mod;
import de.tosox.zonerelay.service.MetaIniService;
import de.tosox.zonerelay.util.ExtractionUtils;
import de.tosox.zonerelay.util.ProgressListener;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ModEntryInstaller implements ModInstaller {
	private final LogManager logManager;
	private final Localizer localizer;
	private final ExtractionUtils extractionUtils;
	private final MetaIniService metaIniService;
	private final MO2ConfigReader mo2ConfigReader;

	public ModEntryInstaller(LogManager logManager, Localizer localizer,
	                         ExtractionUtils extractionUtils, MetaIniService metaIniService,
	                         MO2ConfigReader mo2ConfigReader) {
		this.logManager = logManager;
		this.localizer = localizer;
		this.extractionUtils = extractionUtils;
		this.metaIniService = metaIniService;
		this.mo2ConfigReader = mo2ConfigReader;
	}

	@Override
	public void install(ConfigEntry entry, File archive, ProgressListener progressListener) throws Exception {
		if (!(entry instanceof Mod mod)) {
			throw new IllegalArgumentException("Expected Mod, got " + entry.getClass().getSimpleName());
		}
		progressListener.onProgressUpdate(0, 1);

		Path tempDir = Path.of(AppConfig.TEMPORARY_DIRECTORY).resolve(FilenameUtils.removeExtension(archive.getName()));
		Files.createDirectories(tempDir);

		logManager.getUiLogger().info(localizer.translate("MSG_EXTRACT_TO", tempDir));
		logManager.getFileLogger().info("Extracting %s to %s", archive.getPath(), tempDir);
		extractionUtils.extract(archive, tempDir);

		Path targetDir;
		if (mod.getType() == EntryType.MOD) {
			targetDir = Path.of(AppConfig.MO2_MODS_DIRECTORY).resolve(mod.getName());
		} else {
			targetDir = mo2ConfigReader.getGamePath();
		}

		logManager.getUiLogger().info(localizer.translate("MSG_READ_SETUP"));
		logManager.getFileLogger().info("Reading setup instructions");

		List<String> setup = mod.getSetup();
		int total = setup.size();

		for (int i = 0; i < total; i++) {
			String instruction = setup.get(i);
			Path source = tempDir.resolve(instruction);
			Path destination = targetDir.resolve(source.getFileName());

			logManager.getUiLogger().info(localizer.translate("MSG_COPY_TO", instruction, source.getFileName()));
			logManager.getFileLogger().info("Copying %s → %s", source, destination);
			FileUtils.copyDirectory(source.toFile(), destination.toFile());

			progressListener.onProgressUpdate(i + 1, total);
		}

		if (mod.getType() == EntryType.MOD) {
			logManager.getUiLogger().info(localizer.translate("MSG_GENERATE_META"));
			metaIniService.generate(mod, targetDir);
		}
		progressListener.onProgressUpdate(1, 1);
	}
}
