package de.tosoxdev.cmi.gui;

import de.tosoxdev.cmi.gui.utils.GridBagConstraintsBuilder;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code MainFrame} class represents the main application window.
 */
public class MainFrame extends JFrame {
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
        JPanel pnlMain = new JPanel(new GridBagLayout());
        {
            JPanel pnlOptions = new JPanel();
            {
                pnlOptions.setBackground(Color.BLUE);

                pnlMain.add(pnlOptions, GridBagConstraintsBuilder.create()
                        .fill(GridBagConstraints.BOTH)
                        .gridX(0)
                        .weightX(1.0)
                        .weightY(1.0)
                        .build());
            }

            JPanel pnlOutput = new JPanel();
            {
                pnlOutput.setBackground(Color.PINK);

                pnlMain.add(pnlOutput, GridBagConstraintsBuilder.create()
                        .fill(GridBagConstraints.BOTH)
                        .gridX(1)
                        .weightX(3.6)
                        .weightY(1.0)
                        .build());
            }

            this.add(pnlMain);
        }
    }
}
