package de.tosox.zonerelay.config;

import de.tosox.zonerelay.model.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigValidator {
	public void validate(ConfigData configData) {
		Set<String> seenIds = new HashSet<>();

		validateEntries(configData.getAddons(), seenIds);
		validateEntries(configData.getPatches(), seenIds);
		validateEntries(configData.getSeparators(), seenIds);
	}

	private void validateEntries(List<? extends ConfigEntry> entries, Set<String> seenIds) {
		if (entries == null) return;

		for (ConfigEntry entry : entries) {
			validateEntry(entry, seenIds);
		}
	}

	private void validateEntry(ConfigEntry entry, Set<String> seenIds) {
		if (entry.getId() == null || entry.getId().isBlank()) {
			throw new IllegalArgumentException("Entry missing id: " + entry);
		}

		if (!seenIds.add(entry.getId())) {
			throw new IllegalArgumentException("Duplicate id found: " + entry.getId());
		}

		if (entry instanceof Addon addon) {
			if (addon.getName() == null || addon.getName().isBlank()) {
				throw new IllegalArgumentException("Addon missing name: " + addon.getId());
			}
			validateUrl(addon.getUrl(), addon.getId());
		} else if (entry instanceof Patch patch) {
			if (patch.getName() == null || patch.getName().isBlank()) {
				throw new IllegalArgumentException("Patch missing name: " + patch.getId());
			}
			validateUrl(patch.getUrl(), patch.getId());
		} else if (entry instanceof Separator separator) {
			if (separator.getName() == null || separator.getName().isBlank()) {
				throw new IllegalArgumentException("Separator missing name: " + separator.getId());
			}
		} else {
			throw new IllegalArgumentException("Unknown ConfigEntry type: " + entry.getClass().getSimpleName());
		}
	}

	private void validateUrl(String urlString, String id) {
		if (urlString == null || urlString.isBlank()) {
			throw new IllegalArgumentException("Entry with id '" + id + "' is missing a URL");
		}
		try {
			new URL(urlString);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Entry with id '" + id + "' has invalid URL: " + urlString);
		}
	}

	private void validateSetup(List<String> setupString, String id) {
		if (setupString == null || setupString.isEmpty()) {
			throw new IllegalArgumentException("Entry with id '" + id + "' is missing setup");
		}
		for (String setup : setupString) {
			if (setup == null || setup.isBlank()) {
				throw new IllegalArgumentException("Entry with id '" + id + "' has invalid setup: " + setup);
			}
		}
	}
}
