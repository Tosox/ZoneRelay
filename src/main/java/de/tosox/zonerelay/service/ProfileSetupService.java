package de.tosox.zonerelay.service;

import com.google.inject.Singleton;
import de.tosox.zonerelay.AppConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Singleton
public class ProfileSetupService {

	public void setupProfile() {
		Path newProfilePath = Paths.get(AppConfig.MO2_DIRECTORY, "profiles", "VIP"); // TODO: Make profile name configurable

		try {
			FileUtils.copyDirectory(new File(AppConfig.MO2_PROFILES_DIRECTORY), newProfilePath.toFile());
			FileUtils.copyFile(
					Paths.get(AppConfig.MO2_MODLIST_PATH).toFile(),
					newProfilePath.resolve("modlist.txt").toFile()
			);
		} catch (IOException e) {
			throw new RuntimeException("Failed to setup MO2 profile", e);
		}
	}
}
