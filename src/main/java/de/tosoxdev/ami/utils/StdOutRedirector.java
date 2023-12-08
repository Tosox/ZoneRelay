package de.tosoxdev.ami.utils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class StdOutRedirector {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public StdOutRedirector() {
        try {
            Files.createDirectories(Paths.get(Globals.DIR_LOGS));

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String filename = String.format("%s.log", DATE_FORMAT.format(timestamp));
            String filepath = Paths.get(Globals.DIR_LOGS, filename).toString();

            PrintStream printStream = new PrintStream(new FileOutputStream(filepath, true));
            System.setOut(printStream);
            System.setErr(printStream);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, String.format("Unable to instantiate the StdOutRedirector: %s", e.getMessage()), "Fatal error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void setSwingComponentOutput(JTextComponent component) {
        System.setOut(new PrintStream(new JTextComponentOutputStream(System.out, component)));
        System.setErr(new PrintStream(new JTextComponentOutputStream(System.err, component)));
    }

    public void reset() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
    }
}
