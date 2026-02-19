package de.tosox.zonerelay.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.tosox.zonerelay.logging.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class InstallProgressStore {
	private static final Path PROGRESS_FILE = Path.of("install_progress.dat");

	private final Logger logger;

	@Inject
	public InstallProgressStore(@Named("file") Logger logger) {
		this.logger = logger;
	}

	public void save(String entryId) {
		try {
			Files.writeString(PROGRESS_FILE, entryId, StandardCharsets.UTF_8);
		} catch (IOException e) {
			logger.error("Failed to save install progress: %s", e.getMessage());
		}
	}

	public boolean hasSavedState() {
		return Files.exists(PROGRESS_FILE);
	}

	public String load() {
		if (!hasSavedState()) {
			return null;
		}

		try {
			return Files.readString(PROGRESS_FILE, StandardCharsets.UTF_8).trim();
		} catch (IOException e) {
			logger.error("Failed to read install progress: %s", e.getMessage());
		}

		return null;
	}

	public void clear() {
		try {
			Files.deleteIfExists(PROGRESS_FILE);
		} catch (IOException e) {
			logger.error("Failed to delete install progress file: %s", e.getMessage());
		}
	}
}
