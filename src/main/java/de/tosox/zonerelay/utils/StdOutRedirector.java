package de.tosox.zonerelay.utils;

import de.tosox.zonerelay.handler.CrashHandler;

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

    public static void enable() {
        try {
            Files.createDirectories(Paths.get(Globals.DIR_LOGS));

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String filename = String.format("%s.log", DATE_FORMAT.format(timestamp));
            String filepath = Paths.get(Globals.DIR_LOGS, filename).toString();

            PrintStream printStream = new PrintStream(new FileOutputStream(filepath, true));
            System.setOut(printStream);
            System.setErr(printStream);
        } catch (IOException e) {
            CrashHandler.showErrorDialogAndExit("Unable to activate the StdOutRedirector: %n%s", e.getMessage());
        }
    }

    public static void disable() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
    }
}
