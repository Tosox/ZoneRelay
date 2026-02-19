package de.tosox.zonerelay.downloader;

import de.tosox.zonerelay.util.ProgressListener;

import java.io.File;

public interface DownloadStrategy {
	File download(String resolvedUrl, File archive, ProgressListener listener) throws Exception;
}
