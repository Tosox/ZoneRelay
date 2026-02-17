package de.tosox.zonerelay.installer;

import de.tosox.zonerelay.AppConfig;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logging.LogManager;
import de.tosox.zonerelay.model.ConfigEntry;
import de.tosox.zonerelay.model.Separator;
import de.tosox.zonerelay.service.MetaIniService;
import de.tosox.zonerelay.util.ProgressListener;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class SeparatorInstaller implements ModInstaller {
	private final AppConfig config;
	private final LogManager logManager;
	private final Localizer localizer;
	private final MetaIniService metaIniService;

	public SeparatorInstaller(AppConfig config, LogManager logManager,
	                          Localizer localizer, MetaIniService metaIniService) {
		this.config = config;
		this.logManager = logManager;
		this.localizer = localizer;
		this.metaIniService = metaIniService;
	}

	@Override
	public void install(ConfigEntry entry, File archive, ProgressListener progressListener) throws Exception {
		if (!(entry instanceof Separator separator)) {
			throw new IllegalArgumentException("Expected Separator, got " + entry.getClass().getSimpleName());
		}
		progressListener.onProgressUpdate(0, 1);

		Path modDir = Path.of(config.getMo2ModsDirectory()).resolve(separator.getName() + "_separator");

		logManager.getUiLogger().info(localizer.translate("MSG_CREATE_SEPARATOR", modDir));
		logManager.getFileLogger().info("Creating separator: %s", separator.getName());
		Files.createDirectories(modDir);

		metaIniService.generate(separator, modDir);
		progressListener.onProgressUpdate(1, 1);
	}
}
