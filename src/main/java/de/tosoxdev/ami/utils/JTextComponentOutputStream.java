package de.tosoxdev.ami.utils;

import javax.swing.text.JTextComponent;
import java.io.OutputStream;
import java.io.PrintStream;

public class JTextComponentOutputStream extends OutputStream {
    private final JTextComponent component;
    private final PrintStream originalPrintStream;

    public JTextComponentOutputStream(PrintStream originalPrintStream, JTextComponent component) {
        this.originalPrintStream = originalPrintStream;
        this.component = component;
    }

    @Override
    public void write(int b) {
        originalPrintStream.write(b);
        component.setText(component.getText() + (char) b);
        component.setCaretPosition(component.getDocument().getLength());
    }
}
