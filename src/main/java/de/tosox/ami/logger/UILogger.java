package de.tosox.ami.logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class UILogger {
    private final Logger logger = Logger.getInstance();

    private static UILogger instance;
    private JTextPane textPane;

    public void setTextPane(JTextPane textPane) {
        this.textPane = textPane;
    }

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
        if (textPane == null) {
            logger.warn("The output component isn't set yet");
            return;
        }

        StyledDocument document = textPane.getStyledDocument();
        Style style = textPane.addStyle("ColorStyle", null);
        StyleConstants.setForeground(style, logLevel.getColor());

        String text = String.format(msg, args) + System.lineSeparator();

        try {
            document.insertString(document.getLength(), text, style);
        } catch (BadLocationException e) {
            logger.warn("An error occurred while trying to append text to document");
        }

        textPane.setCaretPosition(document.getLength());
    }

    public static UILogger getInstance() {
        if (instance == null) {
            instance = new UILogger();
        }
        return instance;
    }
}
