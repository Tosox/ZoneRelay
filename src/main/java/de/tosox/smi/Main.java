package de.tosox.smi;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import de.tosox.smi.gui.MainFrame;
import de.tosox.smi.utils.Globals;
import de.tosox.smi.utils.StdOutRedirector;
import de.tosox.smi.localizer.Localizer;

public class Main {
    private static Localizer localizer = new Localizer(Localizer.Language.EN_US);
    private static MainFrame mainFrame;

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        StdOutRedirector.enable();

        mainFrame = new MainFrame(Globals.APP_NAME, 800, 600, 0.9f);
        mainFrame.showWindow();
    }

    public static Localizer getLocalizer() {
        return localizer;
    }

    public static MainFrame getMainFrame() {
        return mainFrame;
    }
}
