package de.tosox.zonerelay.installer;

import de.tosox.zonerelay.AppConfig;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logging.LogManager;
import de.tosox.zonerelay.model.Addon;
import de.tosox.zonerelay.model.ConfigEntry;
import de.tosox.zonerelay.service.MetaIniService;
import de.tosox.zonerelay.util.ExtractionUtils;
import de.tosox.zonerelay.util.ProgressListener;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AddonInstaller implements ModInstaller {
	private final AppConfig config;
	private final LogManager logManager;
	private final Localizer localizer;
	private final ExtractionUtils extractionUtils;
	private final MetaIniService metaIniService;

	public AddonInstaller(AppConfig config, LogManager logManager, Localizer localizer,
	                      ExtractionUtils extractionUtils, MetaIniService metaIniService) {
		this.config = config;
		this.logManager = logManager;
		this.localizer = localizer;
		this.extractionUtils = extractionUtils;
		this.metaIniService = metaIniService;
	}

	@Override
	public void install(ConfigEntry entry, File archive, ProgressListener progressListener) throws Exception {
		if (!(entry instanceof Addon addon)) {
			throw new IllegalArgumentException("Expected Addon, got " + entry.getClass().getSimpleName());
		}
		progressListener.onProgressUpdate(0, 1);

		Path tempDir = Path.of(config.getTemporaryDirectory()).resolve(FilenameUtils.removeExtension(archive.getName()));
		Files.createDirectories(tempDir);
		extractionUtils.extract(archive, tempDir);

		Path modTargetDir = Path.of(config.getMo2ModsDirectory()).resolve(addon.getName());

		List<String> setup = addon.getSetup();
		int total = setup.size();

		for (int i = 0; i < total; i++) {
			String instruction = setup.get(i);
			Path source = tempDir.resolve(instruction);
			Path destination = modTargetDir.resolve(source.getFileName());

			logManager.getFileLogger().info("Copying %s → %s", source, destination);
			FileUtils.copyDirectory(source.toFile(), destination.toFile());

			progressListener.onProgressUpdate(i + 1, total);
		}

		metaIniService.generate(addon, modTargetDir);
		progressListener.onProgressUpdate(1, 1);
	}
}
