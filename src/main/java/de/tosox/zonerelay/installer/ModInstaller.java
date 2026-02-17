package de.tosox.zonerelay.installer;

import de.tosox.zonerelay.model.ConfigEntry;
import de.tosox.zonerelay.util.ProgressListener;

import java.io.File;

public interface ModInstaller {
	void install(ConfigEntry entry, File archive, ProgressListener progressListener) throws Exception;
}
