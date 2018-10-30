package edu.uw.bothell.css.dsl.MASS.prov.utils;

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.Main;
import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledAgent;
import edu.uw.bothell.css.dsl.MASS.prov.core.Agent;
import edu.uw.bothell.css.dsl.MASS.prov.core.MASS;
import edu.uw.bothell.css.dsl.MASS.prov.core.Place;
import edu.uw.bothell.css.dsl.MASS.prov.store.Granularity;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.store.StoreManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A series of helper methods for provenance components of MASS
 *
 * @author Delmar B. Davis
 */
public class ProvUtils {

    private static StringBuffer hostName = null;

    /**
     * Finds "prov=on" in an array of arguments to a driver method and
     * selectively prints the result to standard error
     *
     * @param args - Array of command-line arguments
     * @param print - indicates whether or not to print. If true the result of
     * the search will be printed along with the assumption that provenance is
     * being turned on or left off
     * @return true if "prov=on" is found in the specified array, otherwise
     * false
     */
    public static boolean provSetInArgs(String[] args, boolean print) {
        boolean provOn = false;
        for (int i = 0, im = args.length; i < im; i++) {
            if (args[i].equals("prov=on")) {
                provOn = true;
                break;
            } else if (args[i].equals("prov=off")) {
                break;
            }
        }
        if (print) {
            System.err.print("Provenance Capture is turned ");
            if (provOn) {
                System.err.println("on");
            } else {
                System.err.println("off");
            }
        }
        return provOn;
    }

    /**
     * Provides the name of the direct calling method
     *
     * @param defaultName - Name provided when a calling class cannot be found
     * @return If a calling method exists, the name of the method is returned.
     * If there is no calling method (e.g. this method was used as the entry
     * point of a thread, or process if this was called from the command line).
     *
     */
    public static String methodName(String defaultName) {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        String methodName = ste.length > 2 ? ste[2].getMethodName() : defaultName;
        return methodName;
    }

    /**
     * Provides the name of the direct calling class
     *
     * @param defaultName - Name provided when a calling class cannot be found
     * @return If a calling class exists, the name of the method is returned. If
     * there is no calling class (e.g. this method was used as the entry point
     * of a thread, or process if this was called from the command line).
     *
     */
    public static String className(String defaultName) {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        String methodName = ste.length > 2 ? ste[2].getClassName() : defaultName;
        return methodName;
    }

    /**
     * Provides the name of the indirect calling method. The indirect calling
     * method consists of the method which called the method which called this
     * method.
     *
     * @param defaultName - Name provided when a calling class cannot be found
     * @return If a calling method exists, the name of the method is returned.
     * If there is no calling method (e.g. this method was used as the entry
     * point of a thread, or process if this was called from the command line,
     * or this method was called from the entry point class).
     *
     */
    public static String callingMethodName(String defaultName) {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        String methodName = ste.length > 3 ? ste[3].getMethodName() : defaultName;
        return methodName;
    }

    /**
     * Provides the name of the indirect calling class. The indirect calling
     * class consists of the class of the method that called the method that
     * called this method.
     *
     * @param defaultName - Name provided when a calling class cannot be found
     * @return If an indirect calling class exists, the name of the method is
     * returned. If there is no calling class (e.g. this method was used as the
     * entry point of a thread, or process if this was called from the command
     * line, or this method was called from the entry point class) defaultName
     * is returned.
     *
     */
    public static String callingClassName(String defaultName) {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        String methodName = ste.length > 3 ? ste[3].getClassName() : defaultName;
        return methodName;
    }

    /**
     * Performs a check on string representing a filename for adherence to
     * Windows filename conventions. Windows need not be the platform that this
     * function is concerned with, it just happens to have the most restrictions
     * on filename characters
     *
     * @param fileName - string representing a filename
     * @return True if the filename specified was valid, False if not
     */
    public static boolean isValidFilename(String fileName) {
        boolean isMatch = false;
        if (fileName != null) {
            Pattern pattern = Pattern.compile(
                    "# Match a valid Windows filename (unspecified file system).          \n"
                    + "^                                # Anchor to start of string.        \n"
                    + "(?!                              # Assert filename is not: CON, PRN, \n"
                    + "  (?:                            # AUX, NUL, COM1, COM2, COM3, COM4, \n"
                    + "    CON|PRN|AUX|NUL|             # COM5, COM6, COM7, COM8, COM9,     \n"
                    + "    COM[1-9]|LPT[1-9]            # LPT1, LPT2, LPT3, LPT4, LPT5,     \n"
                    + "  )                              # LPT6, LPT7, LPT8, and LPT9...     \n"
                    + "  (?:\\.[^.]*)?                  # followed by optional extension    \n"
                    + "  $                              # and end of string                 \n"
                    + ")                                # End negative lookahead assertion. \n"
                    + "[^<>:\"/\\\\|?*\\x00-\\x1F]*     # Zero or more valid filename chars.\n"
                    + "[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]  # Last char is not a space or dot.  \n"
                    + "$                                # Anchor to end of string.            ",
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
                    | Pattern.COMMENTS);
            Matcher matcher = pattern.matcher(fileName);
            isMatch = matcher.matches();
        }
        return isMatch;
    }

    /**
     * Gets the simple name from a class name. Example example.package.Test
     * results in Test, example/package/Test.java results in java. So don't use
     * this to get the simple file name from a path.
     *
     * @param className
     * @param defaultName
     * @return
     */
    public static String getSimpleClassName(String className, String defaultName) {
        String simpleClassName = defaultName;
        int startOfSimpleClassName = className.lastIndexOf(".") + 1;
        if (startOfSimpleClassName < className.length()) {
            String tempSimpleClassName;
            if (startOfSimpleClassName < 0 && !className.isEmpty()) {
                tempSimpleClassName = ProvUtils.isValidFilename(className)
                        ? className : simpleClassName;
            } else {
                tempSimpleClassName
                        = className.substring(className.lastIndexOf(".") + 1);
            }
            simpleClassName = ProvUtils.isValidFilename(tempSimpleClassName)
                    ? tempSimpleClassName : simpleClassName;
        }
        return simpleClassName;
    }

    /**
     * Provides a map of entity names and values of entities used during method
     * execution, which is used to determine retrospective provenance by a
     * provenance collector class.
     *
     * @param entities - names and final values of entities used during method
     * execution, in the form: entity name 1, value 1, ..., entity name N, value
     * N
     * @return map of names and final values of entities used during method
     * execution
     */
    public static HashMap<String, Object> getEntityMap(Object... entities) {
        HashMap<String, Object> map = new HashMap<>();
        int length = entities.length;
        if (entities.length % 2 == 0) {
            for (int i = 0; i < length - 1; i += 2) {
                if (entities[i] != null && entities[i + 1] != null
                        && entities[i] instanceof String) {
                    map.put((String) entities[i], entities[i + 1]);
                } else {
                    break;
                }
            }
        }
        return map;
    }

    /**
     * Returns an array of objects from an unspecified amount of objects.
     *
     * @param objects - objects to add to the array
     * @return an array of objects
     */
    public static Object[] getObjectArray(Object... objects) {
        return objects;
    }

    public static String translateToProvcollectionPackage(String className) {
        String[] split;
        split = className.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (int i = 0, im = split.length; i < im; i++) {
            if (i < im - 1) {
                sb.append(split[i]).append('.');
            } else {
                sb.append("provcollection.").append(split[i]);
            }
        }
        return sb.toString();
    }

    public static String translateToProvcollectionFolder(String javaFilepath) {
        String[] split;
        split = javaFilepath.split(File.pathSeparator);
        StringBuilder sb = new StringBuilder();
        for (int i = 0, im = split.length; i < im; i++) {
            if (i < im - 1) {
                sb.append(split[i]).append('/');
            } else {
                sb.append("provcollection/").append(split[i]);
            }
        }
        return sb.toString();
    }

    /**
     * Invoke a provenance collection method on a provenance collection class
     *
     * @param collectionMethodSuffix - This gets appended to the original method
     * name (for which provenance is being collected). For example
     * main_retrospective is a method that collects provenance for the "main"
     * function
     * @param className - Provenance collection class (same name as observed
     * class but in a child package called provcollection.
     * @param methodName - Name of the method being monitored with provenance
     * collection
     * @param args - Objects used to identify the state of the
     * @throws Exception
     */
    public static void collect(String collectionMethodSuffix, String className,
            String methodName, Object... args) throws Exception {
        // get the parameter types of the args
        Class[] paramTypes = null;
        if (args != null) {
            paramTypes = new Class[args.length];
            for (int i = 0, im = args.length; i < im; i++) {
                if (i < im - 1) {
                    paramTypes[i] = args[i].getClass();
                } else {
                    paramTypes[i] = Object[].class;
                }
            }
        }

        try {
            String collectionMethodName;
            Class collectionClass;
            // !!!NOTE: ADD CODE TO POSSIBLY USE STATIC METHODS
            // load the class
            collectionClass = Class.forName(ProvUtils.
                    translateToProvcollectionPackage(className));
            Object provcollectionInstance = collectionClass.newInstance();
            collectionMethodName = new StringBuilder(methodName).append(
                    collectionMethodSuffix).toString();
            // call collection method associated with given method
            Method method = collectionClass.getDeclaredMethod(
                    collectionMethodName, paramTypes);
            method.invoke(provcollectionInstance, args);
        } catch (Exception ex) {
            ex.printStackTrace(IO.getLogWriter());
        }
    }

    public static String getHostName() {
        return getHostNameBuffer().toString();
    }

    public static StringBuffer getHostNameBuffer() {
        if (hostName == null) {
            try {
                hostName = new StringBuffer(java.net.InetAddress.getLocalHost().getHostName());
            } catch (UnknownHostException ex) {
                hostName = new StringBuffer("UNKNOWN_HOST");
                ex.printStackTrace(IO.getLogWriter());
            }
        }
        return hostName;
    }

    public static String getGlobalUserName() {
        return new StringBuilder("User#location=[").
                append(ProvUtils.getHostName()).append("]#account_name=\"").
                append(System.getProperty("user.name")).append("\"").toString();
    }

    public static String getUniversalResourceID(String id) {
        return new StringBuilder(id).append("#id=").
                append(java.util.UUID.randomUUID().toString()).toString();
    }

    public static StringBuffer getUniversalResourceID(StringBuffer id) {
        return new StringBuffer(id).append("#id=").
                append(java.util.UUID.randomUUID().toString());
    }

    public static String getGlobalResourceID(String id) {
        return getGlobalResourceID(new StringBuffer(id)).toString();
    }

    public static StringBuffer getGlobalResourceID(StringBuffer id) {
        return new StringBuffer(id).append("#id=[").append("location#").
                append(ProvUtils.getHostNameBuffer()).append("]");
    }

    public static String getLocationAwareURID(String id) {
        return getLocationAwareURID(new StringBuffer(id)).toString();
    }

    public static StringBuffer getLocationAwareURID(StringBuffer id) {
        return new StringBuffer(id).append("#id=[").append("location#").
                append(ProvUtils.getHostName()).append("][").
                append(java.util.UUID.randomUUID().toString()).append("]");
    }

    public static String getRelativePath(File file) {
        String relativePath = null;
        if (file != null) {
            File base = new File("./");
            relativePath = file.toURI().relativize(base.toURI()).getPath();
        }
        return relativePath;
    }

    /**
     * Gets the simple filename from a path containing parent directories
     *
     * @param uri Path to file
     * @return The last name of the path string that was provided
     */
    public static String getSimpleFilename(String uri) {
        String sfn = null;
        try {
            sfn = new File(uri).getName();
        } catch (Exception e) {
            IO.log("Exception thrown while constructing file for: " + uri);
            e.printStackTrace(IO.getLogWriter());
        }
        return sfn;
    }

    public static String getMASShome(String NODE_FILE) throws ParserConfigurationException, SAXException, IOException {
        /* Load the Document */
        File file = new File(NODE_FILE);
        Document doc = DocumentBuilderFactory.newInstance().
                newDocumentBuilder().parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodes = doc.getElementsByTagName("masshome");
        if (nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        } else {
            return "";
        }
    }

    public static ProvenanceStore getStore(Object owner) {
        StoreManager sm = StoreManager.getStoreManager();
        ProvenanceStore store = (ProvenanceStore) sm.getStore(owner);
        return store;
    }

    public static ProvenanceStore getStoreSafely(Object owner) {
        ProvenanceStore store = null;
        if (MASSProv.provOn) {
            StoreManager sm = StoreManager.getStoreManager();
            store = sm.getStore(owner);
        }
        return store;
    }

    public static String[] getArray(String... strings) {
        return strings;
    }

    public static Object[] getArray(Object... objects) {
        return objects;
    }

    /**
     * Returns the lowest value from provided integers
     *
     * @param values integers to compare
     * @return the lowest provided integer from values
     */
    public static int getLowest(int... values) {
        int lowest;
        if (values != null && values.length > 0) {
            lowest = values[0];
            for (int i = 1, im = values.length; i < im; i++) {
                if (values[i] < lowest) {
                    lowest = values[i];
                }
            }
        } else {
            lowest = 0;
        }
        return lowest;
    }

    public static ProvenanceStore getStoreOfCurrentThread() {
        ProvenanceStore store = null;
        if (MASSProv.isInitialized()) {
            store = StoreManager.getStoreManager().getStoreOfCurrentThread();
        }
        return store;
    }

    public static ProvenanceStore getStoreOfCurrentThread(boolean provOn) {
        ProvenanceStore store = null;
        if (MASSProv.isInitialized() && provOn) {
            store = getStoreOfCurrentThread();
        }
        return store;
    }

    /**
     * Finds a specified argument within an array of arguments
     *
     * @param toFind - argument to find
     * @param args - array of arguments to search
     *
     * @return True if the specified argument was found, otherwise false
     */
    public static boolean findArg(String toFind, String[] args) {
        boolean found = false;
        for (int i = 0, im = args.length; i < im; i++) {
            if (args[i].equals(toFind)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static String getArgRemainder(String startsWith, String[] args) {
        String remainder = null;
        String arg;
        for (int i = 0, im = args.length; i < im; i++) {
            arg = args[i].toLowerCase();
            if (arg.startsWith(startsWith)) {
                try {
                    remainder = arg.substring(startsWith.length());
                    break;
                } catch (StringIndexOutOfBoundsException e) {
                    remainder = null;
                    e.printStackTrace(IO.getLogWriter());
                }
            }
        }
        return remainder;
    }

    public static Granularity granularityFromArgs(String[] args) {
        Granularity granularity = null;
        String remainder = getArgRemainder("granularity=", args);
        int granularityNumber = 0;
        if (remainder != null) {
            try {
                granularityNumber = Integer.valueOf(remainder);
                granularity = Granularity.valueOf(granularityNumber);
            } catch (NumberFormatException ex1) {
                try {
                    granularity = Granularity.valueOf(remainder.toUpperCase());
                } catch (IllegalArgumentException ex2) {
                    granularity = null;
                }
            }
        }
        return granularity;
    }

    public static boolean postProcessingFromArgs(String[] args, boolean defaultFlagValue) {
        boolean flag = defaultFlagValue;
        String flagValue = ProvUtils.getArgRemainder("postprocess=", args);
        if (flagValue != null && flagValue.toLowerCase().equals("true")) {
            flag = true;
        } else if (flagValue != null && flagValue.toLowerCase().equals("false")) {
            flag = false;
        }
        return flag;
    }

    public static void stageEvaluation(String[] args) {
        // this scans args to turn prov on, set granularity, and save args for process documentation
        MASSProv.configure(args);
        // this, on the other hand, can be removed in production code
        if (!MASSProv.granularityConfigured()) {
            // set for evaluation, in case cmd line args didnt set granularity
            // this is to ensure that the evaluation of MASSProv is done consistently
            MASSProv.setGranularity(Granularity.PROCESS, false);
        }
        // this sets the masshome before MASS.init does, in order to 
        // fix slow preprocessing in UDrive 
        // (sets raw prov dir to a physical hd location base on contents of node file)
        try {
            MASSProv.setWorkingDir(ProvUtils.getMASShome(Main.NODE_FILE));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace(IO.getLogWriter());
        }
    }

    public static void printst(StackTraceElement[] ste) {
        for (int i = 0, im = ste.length; i < im; i++) {
            System.err.println("ste[" + i + "] class: " + ste[i]);
        }
    }

    public static int maxStoresFromArgs(String[] args) {
        int size = StoreManager.getMaxProvStoreSize();
        // try for size argument
        String maxstores = ProvUtils.getArgRemainder("maxstores=", args);
        if (maxstores != null) {
            try {
                size = Integer.valueOf(maxstores);
            } catch (NumberFormatException e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }
        return size;
    }

    public static int buffersPerStoreFromArgs(String[] args) {
        int size = StoreManager.getBuffersPerStore();
        // try for size argument
        String buffersPerStore = ProvUtils.getArgRemainder("storebuffers=", args);
        if (buffersPerStore != null) {
            try {
                size = Integer.valueOf(buffersPerStore);
            } catch (NumberFormatException e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }
        return size;
    }

    public static int linesPerBufferFromArgs(String[] args) {
        int size = StoreManager.getLinesPerBuffer();
        // try for size argument
        String linesPerBuffer = ProvUtils.getArgRemainder("bufferlines=", args);
        if (linesPerBuffer != null) {
            try {
                size = Integer.valueOf(linesPerBuffer);
            } catch (NumberFormatException e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }
        return size;
    }

    public static int charsPerLineFromArgs(String[] args) {
        int size = StoreManager.getCharsPerLine();
        // try for size argument
        String charsPerLine = ProvUtils.getArgRemainder("linechars=", args);
        if (charsPerLine != null) {
            try {
                size = Integer.valueOf(charsPerLine);
            } catch (NumberFormatException e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }
        return size;
    }

    public static void releaseThreadStore() {
        if (MASSProv.isInitialized()) {
            if (StoreManager.isInitialized()) {
                StoreManager sm = StoreManager.getStoreManager();
                ProvenanceStore store = getStoreOfCurrentThread();
                if (store != null) {
                    sm.releaseStore(store);
                }
            }
        }
    }

    public static void startSlice() {
//        if (MASSProv.isInitialized()) { // end slice
////            System.err.println("turning prov back on");
//            StoreManager sm = StoreManager.getStoreManager();
//            sm.reanchorAllStoreActivityInfluence();
//            MASS.toggleProvenanceCapture(true); // PROVENANCE CAPTURE IS TURNED OFF HERE
//        }
    }

    public static void endSlice() {
//        if (MASSProv.provOn && MASSProv.isInitialized()) { // end slice
////            System.err.println("turning prov off");
////            StoreManager sm = StoreManager.getStoreManager();
////            sm.persistAllStores();
////            sm.recycleAllStores();
//            MASS.toggleProvenanceCapture(false); // PROVENANCE CAPTURE IS TURNED OFF HERE
//        }
    }

    public static void addVisit(Agent agent, Place place, String callingMethodRID) {
        ProvenanceRecorder.documentAgentVisitToPlace(ProvUtils.getStoreOfCurrentThread(agent.isProvOn()), agent, place, callingMethodRID);
    }

    /**
     * Provides the contents of a file as a StringBuilder
     *
     * @param file - The file to extract data from
     * @return The content of the provided file
     */
    public StringBuilder readFile(File file) {
        boolean empty = true;
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace(IO.getLogWriter());
        }
        String lineSeparator = System.getProperty("line.separator");

        if (scanner != null) {
            while (scanner.hasNextLine()) {
                empty = false;
                fileContents.append(scanner.nextLine()).append(lineSeparator);
            }
            scanner.close();
        }
        if (empty) {
            fileContents = null;
        }
        return fileContents;
    }

    /**
     * Provides the names of all files under a given directory, recursively (all
     * files within the directory, its child directories, their child
     * directories, etc.)
     *
     * @param filenames - List of filenames to fill
     * @param Directory - Path to root parent directory
     * @return List of filled filenames
     */
    public static List<String> getFileNamesRecursively(List<String> filenames,
            File Directory) {
        if (Directory.isDirectory()) {
            File[] subFolder = Directory.listFiles();
            for (File file : subFolder) {
                if (!file.isDirectory()) {
                    if (file.isFile()) {
                        filenames.add(new File(file.getAbsolutePath()).getName());
                    }
                } else {
                    // getFileNamesRecursively
                    filenames = getFileNamesRecursively(filenames, file);
                }
            }
        }
        return filenames;
    }
}
