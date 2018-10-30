package edu.uw.bothell.css.dsl.MASS.prov.IO;

import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deals with output input and output operations for capture code generation
 * operations
 *
 * @author Delmar B. Davis
 */
public class IO {
    
    public static boolean logFlag = false;
    private static boolean init = false;
    private static boolean append = false;
    private static File logFile;
    private static String logPath = ProvUtils.getHostName() + "_captureGenerationLog.txt";

    /**
     * Initializes IO if it hasn't previously been initialized
     */
    private static void init() {
        if (!init) {
            tryChangePathToUserDir();
            IO.logFile = new File(logPath);
            if (IO.logFile.exists() && !append) {
                try {
                    IO.logFile.delete();
                    IO.logFile.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            init = true;
        }
    }

    /**
     * Attempts to change the path of the current log file to stem from the
     * current user's user directory. The operation is ignored if the log path
     * already contains the current user's user directory.
     */
    private static void tryChangePathToUserDir() {
        if (logPath != null) {
            try {
                String userDir = System.getProperty("user.dir");
                if (!logPath.contains(userDir)) {
                    logPath = userDir + File.separatorChar + logPath;
                }
            } catch (Exception e) {
                defaultLog(e);
            }
        }
    }

    /**
     * Gets a PrintWriter for appending
     *
     * @return The PrintWriter to log messages to. Previously written data is
     * maintained.
     */
    public static PrintWriter getLogWriter() {
        init();
        PrintWriter pw;
        try {
            pw = new PrintWriter(new FileOutputStream(
                    logFile, true), true);
        } catch (FileNotFoundException ex) {
            pw = new PrintWriter(System.err);
            ex.printStackTrace(pw);
        }
        return pw;
    }

    /**
     * Cleans the log file, prepping it to be appended to for the remainder of
     * the session, or until it is cleaned again.
     *
     * @throws java.io.IOException Thrown when, the old file could not be
     * deleted and/or when the new file could not be created, at the file path,
     * logPath.
     */
    public static void cleanLog() throws IOException {
        init();
        logFile.delete();
        logFile = new File(logPath);
        logFile.createNewFile();
    }

    /**
     * Logs a message to the logger
     *
     * @param msg - Message to log
     */
    public static void log(String msg) {
        init();
        String lstring = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS").
                format(new Date())
                + " , "
                + Thread.currentThread().getName()
                + " , "
                + msg;
        PrintWriter pw = IO.getLogWriter();
        pw.println(lstring);
        pw.flush();
        pw.close();
    }

    /**
     * Sets the log path
     *
     * @param path - Path to the logger file, used by the the logger
     */
    public static void setLogPath(String path) {
        IO.logPath = path;
    }

    /**
     * Uses the default logger to log an exception, cleanly.
     *
     * @param ex - An exception to log
     */
    public static void defaultLog(Exception ex) {
        Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
    }

    /**
     * Prints an error message
     *
     * @param msg - Message to print
     */
    public static void error(String msg) {
        System.err.println(msg);
    }

    /**
     * Prints a message
     *
     * @param s
     */
    public static void print(String s) {
        System.out.println(s);
    }
}
