package de.tosoxdev.cmi;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import de.tosoxdev.cmi.gui.MainFrame;
import de.tosoxdev.cmi.handler.CrashHandler;
import de.tosoxdev.cmi.localizer.Localizer;
import de.tosoxdev.cmi.utils.Globals;

public class Main {
    private static final CrashHandler CRASH_HANDLER = new CrashHandler();
    private static final Localizer LOCALIZER = new Localizer(Localizer.Language.EN_US);

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();

        MainFrame mainFrame = new MainFrame(Globals.APP_NAME, 800, 600, 0.9f);
        mainFrame.showWindow();
    }

    public static CrashHandler getCrashHandler() {
        return CRASH_HANDLER;
    }

    public static Localizer getLocalizer() {
        return LOCALIZER;
    }
}
