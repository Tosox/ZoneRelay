package de.tosoxdev.ami;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import de.tosoxdev.ami.gui.MainFrame;
import de.tosoxdev.ami.handler.CrashHandler;
import de.tosoxdev.ami.localizer.Localizer;
import de.tosoxdev.ami.logger.LoggerEx;
import de.tosoxdev.ami.utils.Globals;
import de.tosoxdev.ami.utils.StdOutRedirector;

public class Main {
    private static final LoggerEx LOGGER = new LoggerEx();
    private static final CrashHandler CRASH_HANDLER = new CrashHandler();
    private static final Localizer LOCALIZER = new Localizer(Localizer.Language.EN_US);

    private static MainFrame mainFrame;

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        StdOutRedirector.enable();

        mainFrame = new MainFrame(Globals.APP_NAME, 800, 600, 0.9f);
        mainFrame.showWindow();
    }

    public static LoggerEx getLogger() {
        return LOGGER;
    }

    public static CrashHandler getCrashHandler() {
        return CRASH_HANDLER;
    }

    public static Localizer getLocalizer() {
        return LOCALIZER;
    }

    public static MainFrame getMainFrame() {
        return mainFrame;
    }
}
