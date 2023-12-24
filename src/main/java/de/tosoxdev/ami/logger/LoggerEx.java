package de.tosoxdev.ami.logger;

import javax.swing.text.JTextComponent;

public class LoggerEx {
    private final Logger logger = new Logger();

    private JTextComponent textComponent;

    public void setTextComponent(JTextComponent textComponent) {
        this.textComponent = textComponent;
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
        String message = String.format(msg, args);

        if (textComponent != null) {
            textComponent.setText(textComponent.getText() + message + "%n");
            textComponent.setCaretPosition(textComponent.getDocument().getLength());
        }

        logger.log(logLevel, message);
    }
}
