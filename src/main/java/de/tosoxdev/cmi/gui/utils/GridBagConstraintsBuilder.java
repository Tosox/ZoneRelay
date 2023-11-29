package de.tosoxdev.cmi.gui.utils;

import java.awt.*;

public class GridBagConstraintsBuilder {
    private final GridBagConstraints gbc;

    private GridBagConstraintsBuilder() {
        gbc = new GridBagConstraints();
    }

    public GridBagConstraintsBuilder fill(int fill) {
        gbc.fill = fill;
        return this;
    }

    public GridBagConstraintsBuilder gridX(int gridX) {
        gbc.gridx = gridX;
        return this;
    }

    public GridBagConstraintsBuilder gridY(int gridY) {
        gbc.gridy = gridY;
        return this;
    }

    public GridBagConstraintsBuilder gridWidth(int gridWidth) {
        gbc.gridwidth = gridWidth;
        return this;
    }

    public GridBagConstraintsBuilder gridHeight(int gridHeight) {
        gbc.gridheight = gridHeight;
        return this;
    }

    public GridBagConstraintsBuilder weightX(double weightX) {
        gbc.weightx = weightX;
        return this;
    }

    public GridBagConstraintsBuilder weightY(double weightY) {
        gbc.weighty = weightY;
        return this;
    }

    public GridBagConstraintsBuilder anchor(int anchor) {
        gbc.anchor = anchor;
        return this;
    }

    public GridBagConstraintsBuilder insets(Insets insets) {
        gbc.insets = insets;
        return this;
    }

    public GridBagConstraintsBuilder internalPadX(int internalPadX) {
        gbc.ipadx = internalPadX;
        return this;
    }

    public GridBagConstraintsBuilder internalPadY(int internalPadY) {
        gbc.ipady = internalPadY;
        return this;
    }

    public GridBagConstraints build() {
        return gbc;
    }

    public static GridBagConstraintsBuilder create() {
        return new GridBagConstraintsBuilder();
    }
}
