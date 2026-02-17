package de.tosox.zonerelay;

import lombok.Getter;

@Getter
public class AppConfig {
	private final String appVersion;
	private final String appName;

	private final String mo2Directory;
	private final String localesDirectory;
	private final String logsDirectory;
	private final String mo2ModsDirectory;
	private final String mo2ProfilesDirectory;
	private final String downloadsDirectory;
	private final String temporaryDirectory;

	private final String mo2ExePath;
	private final String mo2ConfigPath;

	private final String modlistConfigPath;
	private final String mo2ModlistPath;
	private final String modlistIconPath;
	private final String modlistSplashPath;

	private final String profileFilesDirectory;
	private final String sevenZipPath;
	private final String addonMetaPath;
	private final String separatorMetaPath;

	public AppConfig() {
		this.appVersion = "0.1.0";
		this.appName = "ZoneRelay";

		this.mo2Directory = "../";
		this.localesDirectory = "./locales";
		this.logsDirectory = "./logs";
		this.mo2ModsDirectory = "../mods";
		this.mo2ProfilesDirectory = "../profiles";
		this.downloadsDirectory = "../downloads";
		this.temporaryDirectory = "./temp";

		this.mo2ExePath = "../ModOrganizer.exe";
		this.mo2ConfigPath = "../ModOrganizer.ini";

		this.modlistConfigPath = "./data/modlist.yaml";
		this.mo2ModlistPath = "./data/modlist.txt";
		this.modlistIconPath = "./data/assets/icon.ico";
		this.modlistSplashPath = "./data/assets/splash.png";

		this.profileFilesDirectory = "./resources/profile_files";
		this.sevenZipPath = "./resources/7zip/7z.exe";
		this.addonMetaPath = "./resources/addon-meta.ini-template.txt";
		this.separatorMetaPath = "./resources/separator-meta.ini-template.txt";
	}
}
