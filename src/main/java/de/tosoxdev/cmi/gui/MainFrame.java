package de.tosoxdev.cmi.gui;

import de.tosoxdev.cmi.gui.components.JImagePanel;
import de.tosoxdev.cmi.gui.controllers.MainFrameController;
import de.tosoxdev.cmi.utils.Globals;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code MainFrame} class represents the main application window.
 */
public class MainFrame extends JFrame {
    private final MainFrameController mainFrameController = new MainFrameController();

    /**
     * Constructs a new {@code MainFrame} with the specified arguments.
     * Initializes components and sets default window properties.
     *
     * @param title The title of the main frame
     * @param width The width of the main frame
     * @param height The height of the main frame
     * @param opacity The opacity of the main frame (0.0f to 1.0f)
     */
    public MainFrame(String title, int width, int height, float opacity) {
        initializeComponents();

        this.setTitle(title);
        this.setSize(width, height);
        this.setOpacity(opacity);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Makes the main window visible.
     */
    public void showWindow() {
        this.setVisible(true);
    }

    /**
     * This overrides the {@link java.awt.Frame#isUndecorated()} method
     * so the opacity can be set without un-decorating the window.
     *
     * @return true
     */
    @Override
    public boolean isUndecorated() {
        return true;
    }

    /**
     * Initializes the main components of the main frame.
     */
    private void initializeComponents() {
        JPanel pnlMain = new JPanel(null);
        {
            JPanel pnlOptions = new JPanel(null);
            {
                JImagePanel imgLogo = new JImagePanel("./data/assets/logo.png");
                imgLogo.setBounds(10, 10, 160, 60);
                pnlOptions.add(imgLogo);

                JLabel lblVersion = new JLabel(String.format("Installer version: %s", Globals.APP_VERSION), SwingConstants.CENTER);
                lblVersion.setBounds(10, 70, 160, 30);
                pnlOptions.add(lblVersion);

                JSeparator sepLogo = new JSeparator();
                sepLogo.setBounds(20, 110, 140, 2);
                sepLogo.setForeground(Color.WHITE);
                pnlOptions.add(sepLogo);

                JButton btnInstall = new JButton("Install");
                btnInstall.setBounds(10, 130, 160, 80);
                btnInstall.setContentAreaFilled(false);
                btnInstall.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, false));
                btnInstall.addActionListener(a -> mainFrameController.onInstallClick());
                pnlOptions.add(btnInstall);

                JCheckBox cbxFullInstall = new JCheckBox("Full installation");
                cbxFullInstall.setBounds(20, 215, 160, 30);
                pnlOptions.add(cbxFullInstall);

                JSeparator sepInstall = new JSeparator();
                sepInstall.setBounds(20, 255, 140, 2);
                sepInstall.setForeground(Color.WHITE);
                pnlOptions.add(sepInstall);

                JButton btnLaunch = new JButton("Launch");
                btnLaunch.setBounds(10, 275, 160, 80);
                btnLaunch.setContentAreaFilled(false);
                btnLaunch.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, false));
                btnLaunch.addActionListener(a -> mainFrameController.onLaunchClick());
                pnlOptions.add(btnLaunch);

                pnlOptions.setBounds(0, 0, 180, 600);
                pnlMain.add(pnlOptions);
            }

            JPanel pnlOutput = new JPanel(null);
            {
                JScrollPane scpOutput = new JScrollPane();
                pnlOutput.add(scpOutput);

                pnlOutput.setBounds(180, 0, 640, 600);
                pnlMain.add(pnlOutput);
            }

            this.add(pnlMain);
        }
    }
}
