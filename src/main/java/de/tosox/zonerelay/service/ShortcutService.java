package de.tosox.zonerelay.service;

import de.tosox.zonerelay.AppConfig;
import mslinks.ShellLink;

import java.io.IOException;
import java.nio.file.Path;

public class ShortcutService {
	private final AppConfig config;

	public ShortcutService(AppConfig config) {
		this.config = config;
	}

	public void createShortcut() {
		try {
			String desktopPath = System.getProperty("user.home") + "/Desktop";
			Path shortcutPath = Path.of(desktopPath, "S.T.A.L.K.E.R. VIP.lnk");

			ShellLink.createLink(Path.of(config.getMo2ExePath()).toAbsolutePath().normalize().toString())
					.setIconLocation(Path.of(config.getModlistIconPath()).toAbsolutePath().normalize().toString())
					.saveTo(shortcutPath.toString());
		} catch (IOException e) {
			throw new RuntimeException("Unable to create shortcut", e);
		}
	}
}
