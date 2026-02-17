package de.tosox.zonerelay.downloader;

import de.tosox.zonerelay.model.ConfigEntry;
import de.tosox.zonerelay.util.ProgressListener;

import java.io.File;
import java.nio.file.Path;

public interface DownloadStrategy {
	File download(ConfigEntry entry, Path directory, ProgressListener listener) throws Exception;
}
