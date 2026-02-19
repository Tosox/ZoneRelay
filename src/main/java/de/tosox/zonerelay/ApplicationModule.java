package de.tosox.zonerelay;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.tosox.zonerelay.config.MO2ConfigReader;
import de.tosox.zonerelay.config.UserSettings;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logging.LogManager;
import de.tosox.zonerelay.logging.Logger;
import de.tosox.zonerelay.util.ImageLoader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ApplicationModule extends AbstractModule {

	@Provides
	@Singleton
	JTextPane provideOutputPane() {
		return new JTextPane();
	}

	@Provides
	@Singleton
	LogManager provideLogManager(JTextPane outputPane) {
		return new LogManager(outputPane);
	}

	@Provides
	@Named("file")
	@Singleton
	Logger provideFileLogger(LogManager logManager) {
		return logManager.getFileLogger();
	}

	@Provides
	@Named("ui")
	@Singleton
	Logger provideUiLogger(LogManager logManager) {
		return logManager.getUiLogger();
	}

	@Provides
	@Singleton
	UserSettings provideUserSettings() {
		return UserSettings.load(new File("user_config.yaml"));
	}

	@Provides
	@Singleton
	Localizer provideLocalizer(UserSettings settings, @Named("file") Logger logger) throws IOException {
		return new Localizer(settings.getLanguage(), logger);
	}

	@Provides
	@Singleton
	MO2ConfigReader provideMO2ConfigReader() {
		return new MO2ConfigReader(Path.of(AppConfig.MO2_CONFIG_PATH));
	}

	@Provides
	@Singleton
	ImageLoader provideImageLoader() throws IOException {
		return new ImageLoader();
	}
}
