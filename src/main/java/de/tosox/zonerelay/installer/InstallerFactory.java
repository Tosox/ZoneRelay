package de.tosox.zonerelay.installer;

import de.tosox.zonerelay.AppConfig;
import de.tosox.zonerelay.config.MO2ConfigReader;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logging.LogManager;
import de.tosox.zonerelay.model.ConfigEntry;
import de.tosox.zonerelay.service.MetaIniService;
import de.tosox.zonerelay.util.ExtractionUtils;

public class InstallerFactory {
	private final AppConfig config;
	private final LogManager logManager;
	private final Localizer localizer;
	private final ExtractionUtils extractionUtils;
	private final MO2ConfigReader mo2ConfigReader;
	private final MetaIniService metaIniService;

	public InstallerFactory(AppConfig config, LogManager logManager, Localizer localizer, ExtractionUtils extractionUtils,
	                        MO2ConfigReader mo2ConfigReader, MetaIniService metaIniService) {
		this.config = config;
		this.logManager = logManager;
		this.localizer = localizer;
		this.extractionUtils = extractionUtils;
		this.mo2ConfigReader = mo2ConfigReader;
		this.metaIniService = metaIniService;
	}

	public ModInstaller getInstaller(ConfigEntry entry) {
		return switch (entry.getType()) {
			case ADDON -> new AddonInstaller(config, logManager, localizer, extractionUtils, metaIniService);
			case PATCH -> new PatchInstaller(config, logManager, localizer, extractionUtils, mo2ConfigReader);
			case SEPARATOR -> new SeparatorInstaller(config, logManager, localizer, metaIniService);
		};
	}
}
