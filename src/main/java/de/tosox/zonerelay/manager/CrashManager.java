package de.tosox.zonerelay.manager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CrashManager {
	private static final String CRASH_FILE = "crash.dat";

	/**
	 * Saves the crash data to a file.
	 *
	 * @param id the ID of the current section to save.
	 */
	public void saveCrashData(String id) {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CRASH_FILE), StandardCharsets.UTF_8))) {
			writer.write(id);
		} catch (IOException e) {
			System.err.println("Failed to save crash data: " + e.getMessage());
		}
	}

	/**
	 * Checks if crash data exists.
	 *
	 * @return true if crash data exists, false otherwise.
	 */
	public boolean hasCrashData() {
		return Files.exists(Path.of(CRASH_FILE));
	}

	/**
	 * Loads the crash ID if available.
	 *
	 * @return the last saved ID, or null if no crash data exists.
	 */
	public String loadCrashData() {
		if (!hasCrashData()) {
			return null;
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CRASH_FILE), StandardCharsets.UTF_8))) {
			return reader.readLine();
		} catch (IOException e) {
			System.err.println("Failed to read crash data: " + e.getMessage());
		}

		return null;
	}

	/**
	 * Deletes the crash data file.
	 */
	public void clearCrashData() {
		try {
			Files.deleteIfExists(Path.of(CRASH_FILE));
		} catch (IOException e) {
			System.err.println("Failed to delete crash file: " + e.getMessage());
		}
	}
}
