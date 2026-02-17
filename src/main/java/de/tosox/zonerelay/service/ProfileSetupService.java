package de.tosox.zonerelay.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.tosox.zonerelay.AppConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Singleton
public class ProfileSetupService {
	private final AppConfig config;

	@Inject
	public ProfileSetupService(AppConfig config) {
		this.config = config;
	}

	public void setupProfile() {
		Path newProfilePath = Paths.get(config.getMo2Directory(), "profiles", "VIP"); // TODO: Make profile name configurable

		try {
			FileUtils.copyDirectory(new File(config.getMo2ProfilesDirectory()), newProfilePath.toFile());
			FileUtils.copyFile(
					Paths.get(config.getMo2ModlistPath()).toFile(),
					newProfilePath.resolve("modlist.txt").toFile()
			);
		} catch (IOException e) {
			throw new RuntimeException("Failed to setup MO2 profile", e);
		}
	}
}
