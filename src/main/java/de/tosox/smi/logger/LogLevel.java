package de.tosox.smi.logger;

import java.awt.*;

public enum LogLevel {
    DEBUG("DBG", Color.GRAY),
    INFO("INF", Color.WHITE),
    WARNING("WRN", Color.YELLOW),
    ERROR("ERR", Color.RED),
    CRITICAL("CRT", Color.RED.darker());

    private final String prefix;
    private final Color color;

    LogLevel(String prefix, Color color) {
        this.prefix = prefix;
        this.color = color;
    }

    public String getPrefix() {
        return prefix;
    }

    public Color getColor() {
        return color;
    }
}
