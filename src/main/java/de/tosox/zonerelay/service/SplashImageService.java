package de.tosox.zonerelay.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.tosox.zonerelay.AppConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Singleton
public class SplashImageService {
	private final AppConfig config;

	@Inject
	public SplashImageService(AppConfig config) {
		this.config = config;
	}

	public void copySplashImage() {
		Path splashImage = Path.of(config.getModlistSplashPath());
		Path mo2DirSplash = Path.of(config.getMo2Directory(), "splash.png");

		if (Files.exists(splashImage)) {
			try {
				Files.copy(splashImage, mo2DirSplash, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new RuntimeException("Failed to copy splash image", e);
			}
		}
	}
}
