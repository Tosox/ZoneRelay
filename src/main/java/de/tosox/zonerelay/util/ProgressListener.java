package de.tosox.zonerelay.util;

@FunctionalInterface
public interface ProgressListener {
	void onProgressUpdate(int current, int total);
}
