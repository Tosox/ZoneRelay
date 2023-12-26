package de.tosoxdev.ami;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import de.tosoxdev.ami.gui.MainFrame;
import de.tosoxdev.ami.localizer.Localizer;
import de.tosoxdev.ami.logger.UIOutputLogger;
import de.tosoxdev.ami.utils.Globals;
import de.tosoxdev.ami.utils.StdOutRedirector;

public class Main {
    private static Localizer localizer = new Localizer(Localizer.Language.EN_US);
    private static MainFrame mainFrame;

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        StdOutRedirector.enable();

        mainFrame = new MainFrame(Globals.APP_NAME, 800, 600, 0.9f);
        mainFrame.showWindow();
    }

    public static UIOutputLogger getDisplayLogger() {
        return mainFrame.getDisplayLogger();
    }

    public static Localizer getLocalizer() {
        return localizer;
    }

    public static MainFrame getMainFrame() {
        return mainFrame;
    }
}
