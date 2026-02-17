package de.tosox.zonerelay.util;

import de.tosox.zonerelay.AppConfig;
import de.tosox.zonerelay.logging.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class ExtractionUtils {
	private final AppConfig config;
	private final Logger logger;

	public ExtractionUtils(AppConfig config, Logger logger) {
		this.config = config;
		this.logger = logger;
	}

	public void extract(File archive, Path destination) throws Exception {
		logger.info("Extracting %s to %s", archive.getPath(), destination);

		ProcessBuilder builder = new ProcessBuilder(
				config.getSevenZipPath(), "-bso0",  "x", archive.getPath(), "-o" + destination.toString(), "-y"
		);
		builder.redirectErrorStream(true);
		Process process = builder.start();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				logger.info(line);
			}
		}

		int exitCode = process.waitFor();
		if (exitCode != 0) {
			throw new IOException("Extraction process failed with exit code " + exitCode);
		}
	}
}
