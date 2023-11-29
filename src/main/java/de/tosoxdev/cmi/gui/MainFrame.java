package de.tosoxdev.cmi.gui;

import de.tosoxdev.cmi.gui.utils.GridBagConstraintsBuilder;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(String title, int width, int height, float opacity) {
        initializeComponents();

        this.setTitle(title);
        this.setSize(width, height);
        this.setOpacity(opacity);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setVisible() {
        this.setVisible(true);
    }

    /**
     * This overrides the {@code java.awt.Frame.isUndecorated} method so the opacity can be set without un-decorating the window
     *
     * @return true
     */
    @Override
    public boolean isUndecorated() {
        return true;
    }

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
