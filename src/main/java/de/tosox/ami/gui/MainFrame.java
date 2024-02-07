package de.tosox.ami.gui;

import de.tosox.ami.logger.UILogger;
import de.tosox.ami.utils.Globals;
import de.tosox.ami.utils.ImageUtils;
import de.tosox.ami.gui.components.JImagePanel;
import de.tosox.ami.gui.controllers.MainFrameController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The {@code MainFrame} class represents the main application window.
 */
public class MainFrame extends JFrame {
    private static final BufferedImage LOGO = ImageUtils.getFromPath(MainFrame.class.getResource("logo.png"));
    private final MainFrameController mainFrameController = new MainFrameController();

    private JCheckBox cbxFullInstall;
    private JTextPane txpOutput;
    private JProgressBar pgbCurrent;
    private JProgressBar pgbTotal;

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
        this.setIconImage(LOGO);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UILogger.getInstance().setTextPane(txpOutput);
    }

    /**
     * Makes the main window visible.
     */
    public void showWindow() {
        this.setVisible(true);
    }

    /**
     * Returns the value of the {@code cbxFullInstall} checkbox component.
     *
     * @return The value of the {@code cbxFullInstall} checkbox component
     */
    public boolean isFullInstall() {
        return cbxFullInstall.isSelected();
    }

    /**
     * Returns the value of the {@code txaOutput} textarea component.
     *
     * @return The value of the {@code txaOutput} textarea component
     */
    public String getDisplayText() {
        return txpOutput.getText();
    }

    /**
     * Returns the value of the {@code pgbCurrent} progressbar component.
     *
     * @return The value of the {@code pgbCurrent} progressbar component
     */
    public int getCurrentProgress() {
        return pgbCurrent.getValue();
    }

    /**
     * Sets the value of the {@code pgbCurrent} progressbar component.
     */
    public void setCurrentProgress(int value) {
        pgbCurrent.setValue(value);
    }

    /**
     * Returns the value of the {@code pgbTotal} progressbar component.
     *
     * @return The value of the {@code pgbTotal} progressbar component
     */
    public int getTotalProgress() {
        return pgbTotal.getValue();
    }

    /**
     * Sets the value of the {@code pgbTotal} progressbar component.
     */
    public void setTotalProgress(int value) {
        pgbTotal.setValue(value);
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
                JImagePanel imgLogo = new JImagePanel(LOGO);
                imgLogo.setBackground(new Color(0x03, 0x03, 0x03));
                imgLogo.setBounds(10, 10, 160, 80);
                pnlOptions.add(imgLogo);

                JLabel lblVersion = new JLabel(String.format("Installer version: %s", Globals.APP_VERSION), SwingConstants.CENTER);
                lblVersion.setForeground(new Color(0xC2, 0xC2, 0xC2));
                lblVersion.setBounds(10, 90, 160, 30);
                pnlOptions.add(lblVersion);

                JSeparator sepLogo = new JSeparator();
                sepLogo.setBounds(20, 115, 140, 2);
                sepLogo.setForeground(Color.WHITE);
                pnlOptions.add(sepLogo);

                JButton btnInstall = new JButton("Install");
                btnInstall.setBounds(10, 135, 160, 80);
                btnInstall.setBackground(new Color(0x03, 0x03, 0x03));
                btnInstall.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, false));
                btnInstall.addActionListener(a -> mainFrameController.onInstallClick());
                pnlOptions.add(btnInstall);

                cbxFullInstall = new JCheckBox("Full installation");
                cbxFullInstall.setBounds(20, 215, 160, 30);
                pnlOptions.add(cbxFullInstall);

                JSeparator sepInstall = new JSeparator();
                sepInstall.setBounds(20, 260, 140, 2);
                sepInstall.setForeground(Color.WHITE);
                pnlOptions.add(sepInstall);

                JButton btnLaunch = new JButton("Launch");
                btnLaunch.setBounds(10, 280, 160, 80);
                btnLaunch.setBackground(new Color(0x03, 0x03, 0x03));
                btnLaunch.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, false));
                btnLaunch.addActionListener(a -> mainFrameController.onLaunchClick());
                pnlOptions.add(btnLaunch);

                pnlOptions.setBackground(new Color(0x03, 0x03, 0x03));
                pnlOptions.setBounds(0, 0, 180, 600);
                pnlMain.add(pnlOptions);
            }

            JPanel pnlOutput = new JPanel(null);
            {
                txpOutput = new JTextPane();
                txpOutput.setBackground(new Color(0x06, 0x06, 0x06));
                txpOutput.setEditable(false);

                JScrollPane scpOutput = new JScrollPane(txpOutput);
                scpOutput.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, false));
                scpOutput.setBounds(10, 10, 580, 480);
                pnlOutput.add(scpOutput);

                JLabel lblCurrent = new JLabel("Current progress");
                lblCurrent.setBounds(10, 495, 90, 30);
                pnlOutput.add(lblCurrent);

                pgbCurrent = new JProgressBar();
                pgbCurrent.setStringPainted(true);
                pgbCurrent.setBounds(110, 500, 480, 20);
                pnlOutput.add(pgbCurrent);

                JLabel lblTotal = new JLabel("Total progress");
                lblTotal.setBounds(10, 525, 90, 30);
                pnlOutput.add(lblTotal);

                pgbTotal = new JProgressBar();
                pgbTotal.setStringPainted(true);
                pgbTotal.setBounds(110, 530, 480, 20);
                pnlOutput.add(pgbTotal);

                pnlOutput.setBackground(new Color(0x03, 0x03, 0x03));
                pnlOutput.setBounds(180, 0, 640, 600);
                pnlMain.add(pnlOutput);
            }

            this.add(pnlMain);
        }
    }
}
