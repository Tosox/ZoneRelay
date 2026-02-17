package de.tosox.zonerelay;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.tosox.zonerelay.gui.MainFrame;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logging.LogManager;

public class Application {
	private final MainFrame mainFrame;

	public Application() {
		Injector injector = Guice.createInjector(new ApplicationModule());
		this.mainFrame = injector.getInstance(MainFrame.class);

		LogManager logManager = injector.getInstance(LogManager.class);
		Localizer localizer = injector.getInstance(Localizer.class);
		AppConfig config = injector.getInstance(AppConfig.class);

		logManager.getUiLogger().info(localizer.translate("MSG_WELCOME_MESSAGE", config.getAppName()));
		logManager.getUiLogger().info("-------------------------------------------------------------------\n");
	}

	public void start() {
		mainFrame.showWindow();
	}
}
