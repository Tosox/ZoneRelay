package de.tosox.zonerelay.service;

import com.google.inject.Singleton;
import de.tosox.zonerelay.AppConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Singleton
public class SplashImageService {

	public void copySplashImage() {
		Path splashImage = Path.of(AppConfig.MODLIST_SPLASH_PATH);
		Path mo2DirSplash = Path.of(AppConfig.MO2_DIRECTORY, "splash.png");

		if (Files.exists(splashImage)) {
			try {
				Files.copy(splashImage, mo2DirSplash, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new RuntimeException("Failed to copy splash image", e);
			}
		}
	}
}
