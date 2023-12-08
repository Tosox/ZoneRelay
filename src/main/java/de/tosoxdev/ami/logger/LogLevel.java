package de.tosoxdev.ami.logger;

public enum LogLevel {
    DEBUG("DBG"),
    INFO("INF"),
    WARNING("WRN"),
    ERROR("ERR"),
    CRITICAL("CRT");

    private final String prefix;

    LogLevel(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
