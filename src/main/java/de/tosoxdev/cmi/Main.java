package de.tosoxdev.cmi;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import de.tosoxdev.cmi.gui.MainFrame;
import de.tosoxdev.cmi.utils.Globals;

public class Main {
    public static void main(String[] args) {
        FlatMacDarkLaf.setup();

        MainFrame mainFrame = new MainFrame(Globals.APP_NAME);
        mainFrame.setVisible();
    }
}
