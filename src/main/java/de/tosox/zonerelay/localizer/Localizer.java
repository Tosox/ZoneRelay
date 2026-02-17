package de.tosox.zonerelay.localizer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tosox.zonerelay.AppConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class Localizer {
	private final Map<String, String> messages = new HashMap<>();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public Localizer(AppConfig config, String languageCode) throws IOException {
		loadLocale(Path.of(config.getLocalesDirectory()), languageCode);
	}

	private void loadLocale(Path localeDir, String languageCode) throws IOException {
		Path filePath = localeDir.resolve(languageCode + ".json");
		if (!Files.exists(filePath)) {
			throw new IOException("Localization file not found: " + filePath);
		}

		try {
			Map<String, String> loaded = objectMapper.readValue(filePath.toFile(), new TypeReference<>() {});
			messages.clear();
			messages.putAll(loaded);
		} catch (IOException e) {
			throw new IOException("Failed to load locale file: " + filePath, e);
		}
	}

	public String translate(String key, Object... args) {
		String message = messages.getOrDefault(key, key);
		if (args.length == 0) {
			return message;
		}
		return MessageFormat.format(message, args);
	}
}
