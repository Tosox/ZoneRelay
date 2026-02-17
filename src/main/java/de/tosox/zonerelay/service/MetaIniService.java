package de.tosox.zonerelay.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.tosox.zonerelay.AppConfig;
import de.tosox.zonerelay.logging.Logger;
import de.tosox.zonerelay.model.Addon;
import de.tosox.zonerelay.model.ConfigEntry;
import de.tosox.zonerelay.model.Separator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class MetaIniService {
	private final Logger logger;
	private final Path addonMetaTemplate;
	private final Path separatorMetaTemplate;

	@Inject
	public MetaIniService(AppConfig config, @Named("file") Logger logger) {
		this.logger = logger;
		this.addonMetaTemplate = Path.of(config.getAddonMetaPath());
		this.separatorMetaTemplate = Path.of(config.getSeparatorMetaPath());
	}

	public void generate(ConfigEntry entry, Path targetDir) throws IOException {
		String content;

		if (entry instanceof Addon) {
			content = fillTemplate(entry, addonMetaTemplate);
		} else if (entry instanceof Separator) {
			content = fillTemplate(entry, separatorMetaTemplate);
		} else {
			throw new IllegalArgumentException("Unsupported entry type for meta.ini: " + entry.getClass());
		}

		Files.createDirectories(targetDir);
		Files.writeString(targetDir.resolve("meta.ini"), content);

		logger.info("Generated meta.ini in %s", targetDir);
	}

	private String fillTemplate(ConfigEntry entry, Path templatePath) throws IOException {
		String template = Files.readString(templatePath);
		return template
				.replace("{id}", entry.getId())
				.replace("{name}", entry.getName());
	}
}
