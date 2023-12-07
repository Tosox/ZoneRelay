package de.tosoxdev.cmi;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import de.tosoxdev.cmi.gui.MainFrame;
import de.tosoxdev.cmi.handler.CrashHandler;
import de.tosoxdev.cmi.localizer.Localizer;
import de.tosoxdev.cmi.utils.Globals;
import de.tosoxdev.cmi.utils.Logger;
import de.tosoxdev.cmi.utils.StdOutRedirector;

public class Main {
    private static final StdOutRedirector STD_OUT_REDIRECTOR = new StdOutRedirector();
    private static final CrashHandler CRASH_HANDLER = new CrashHandler();
    private static final Localizer LOCALIZER = new Localizer(Localizer.Language.EN_US);
    private static final Logger LOGGER = new Logger();
    private static MainFrame mainFrame;

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();

        mainFrame = new MainFrame(Globals.APP_NAME, 800, 600, 0.9f);
        mainFrame.showWindow();
    }

    public static MainFrame getMainFrame() {
        return mainFrame;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static StdOutRedirector getStdOutRedirector() {
        return STD_OUT_REDIRECTOR;
    }

    public static CrashHandler getCrashHandler() {
        return CRASH_HANDLER;
    }

    public static Localizer getLocalizer() {
        return LOCALIZER;
    }
}
