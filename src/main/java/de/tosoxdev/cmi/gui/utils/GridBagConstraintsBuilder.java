package de.tosoxdev.cmi.gui.utils;

import java.awt.*;

/**
 * The {@code GridBagConstraintsBuilder} class provides a fluent
 * API for creating a {@link java.awt.GridBagConstraints} object.
 */
public class GridBagConstraintsBuilder {
    private final GridBagConstraints gbc;

    /**
     * Private constructor to create an instance of {@code GridBagConstraintsBuilder}.
     * Initializes the underlying {@link java.awt.GridBagConstraints} object.
     */
    private GridBagConstraintsBuilder() {
        gbc = new GridBagConstraints();
    }

    /**
     * Sets the {@code fill} attribute of the builder object
     *
     * @see java.awt.GridBagConstraints#fill
     * @param fill The desired {@code fill} attribute value
     * @return The new builder
     */
    public GridBagConstraintsBuilder fill(int fill) {
        gbc.fill = fill;
        return this;
    }

    /**
     * Sets the {@code gridx} attribute of the builder object
     *
     * @see java.awt.GridBagConstraints#gridx
     * @param gridX The desired {@code gridx} attribute value
     * @return The new builder
     */
    public GridBagConstraintsBuilder gridX(int gridX) {
        gbc.gridx = gridX;
        return this;
    }

    /**
     * Sets the {@code gridy} attribute of the builder object
     *
     * @see java.awt.GridBagConstraints#gridy
     * @param gridY The desired {@code gridy} attribute value
     * @return The new builder
     */
    public GridBagConstraintsBuilder gridY(int gridY) {
        gbc.gridy = gridY;
        return this;
    }

    /**
     * Sets the {@code gridwidth} attribute of the builder object
     *
     * @see java.awt.GridBagConstraints#gridwidth
     * @param gridWidth The desired {@code gridwidth} attribute value
     * @return The new builder
     */
    public GridBagConstraintsBuilder gridWidth(int gridWidth) {
        gbc.gridwidth = gridWidth;
        return this;
    }

    /**
     * Sets the {@code gridheight} attribute of the builder object
     *
     * @see java.awt.GridBagConstraints#gridheight
     * @param gridHeight The desired {@code gridheight} attribute value
     * @return The new builder
     */
    public GridBagConstraintsBuilder gridHeight(int gridHeight) {
        gbc.gridheight = gridHeight;
        return this;
    }

    /**
     * Sets the {@code weightx} attribute of the builder object
     *
     * @see java.awt.GridBagConstraints#weightx
     * @param weightX The desired {@code weightx} attribute value
     * @return The new builder
     */
    public GridBagConstraintsBuilder weightX(double weightX) {
        gbc.weightx = weightX;
        return this;
    }

    /**
     * Sets the {@code weighty} attribute of the builder object
     *
     * @see java.awt.GridBagConstraints#weighty
     * @param weightY The desired {@code weighty} attribute value
     * @return The new builder
     */
    public GridBagConstraintsBuilder weightY(double weightY) {
        gbc.weighty = weightY;
        return this;
    }

    /**
     * Sets the {@code anchor} attribute of the builder object
     *
     * @see java.awt.GridBagConstraints#anchor
     * @param anchor The desired {@code anchor} attribute value
     * @return The new builder
     */
    public GridBagConstraintsBuilder anchor(int anchor) {
        gbc.anchor = anchor;
        return this;
    }

    /**
     * Sets the {@code insets} attribute of the builder object
     *
     * @see java.awt.GridBagConstraints#insets
     * @param insets The desired {@code insets} attribute value
     * @return The new builder
     */
    public GridBagConstraintsBuilder insets(Insets insets) {
        gbc.insets = insets;
        return this;
    }

    /**
     * Sets the {@code ipadx} attribute of the builder object
     *
     * @see java.awt.GridBagConstraints#ipadx
     * @param internalPadX The desired {@code ipadx} attribute value
     * @return The new builder
     */
    public GridBagConstraintsBuilder internalPadX(int internalPadX) {
        gbc.ipadx = internalPadX;
        return this;
    }

    /**
     * Sets the {@code ipady} attribute of the builder object
     *
     * @see java.awt.GridBagConstraints#ipady
     * @param internalPadY The desired {@code ipady} attribute value
     * @return The new builder
     */
    public GridBagConstraintsBuilder internalPadY(int internalPadY) {
        gbc.ipady = internalPadY;
        return this;
    }

    /**
     * Builds and returns the {@link java.awt.GridBagConstraints} object.
     *
     * @return The constructed {@code GridBagConstraints} object
     */
    public GridBagConstraints build() {
        return gbc;
    }

    /**
     * Creates a new instance of a {@code GridBagConstraintsBuilder}.
     *
     * @return A new {@code GridBagConstraintsBuilder} object
     */
    public static GridBagConstraintsBuilder create() {
        return new GridBagConstraintsBuilder();
    }
}
