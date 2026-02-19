package de.tosox.zonerelay.service;

import com.google.inject.Singleton;
import de.tosox.zonerelay.AppConfig;
import mslinks.ShellLink;

import java.io.IOException;
import java.nio.file.Path;

@Singleton
public class ShortcutService {

	public void createShortcut() {
		try {
			String desktopPath = System.getProperty("user.home") + "/Desktop";
			Path shortcutPath = Path.of(desktopPath, "S.T.A.L.K.E.R. VIP.lnk");

			ShellLink.createLink(Path.of(AppConfig.MO2_EXE_PATH).toAbsolutePath().normalize().toString())
					.setIconLocation(Path.of(AppConfig.MODLIST_ICON_PATH).toAbsolutePath().normalize().toString())
					.saveTo(shortcutPath.toString());
		} catch (IOException e) {
			throw new RuntimeException("Unable to create shortcut", e);
		}
	}
}
