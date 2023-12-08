package de.tosoxdev.ami.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");

    public void debug(String msg, Object... args) {
        log(LogLevel.DEBUG, msg, args);
    }

    public void info(String msg, Object... args) {
        log(LogLevel.INFO, msg, args);
    }

    public void warn(String msg, Object... args) {
        log(LogLevel.WARNING, msg, args);
    }

    public void error(String msg, Object... args) {
        log(LogLevel.ERROR, msg, args);
    }

    public void critical(String msg, Object... args) {
        log(LogLevel.CRITICAL, msg, args);
    }

    private void log(LogLevel logLevel, String msg, Object... args) {
        System.out.printf("[%s][%s] %s%s", TIME_FORMATTER.format(new Date()), logLevel.getPrefix(), String.format(msg, args), System.lineSeparator());
    }
}
