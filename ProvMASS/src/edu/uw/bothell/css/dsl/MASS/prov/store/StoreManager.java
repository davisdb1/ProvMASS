package edu.uw.bothell.css.dsl.MASS.prov.store;
// PERFORMANCE DOCUMENTED

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.filter.AgentFilter;
import edu.uw.bothell.css.dsl.MASS.prov.filter.PlaceFilter;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.NoProvenanceCollectionCollector;
import edu.uw.bothell.css.dsl.MASS.prov.utils.FileUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stores information related to preprocessed Provenance files, needed for
 * post-processing. This information includes unique directory names and
 * prefixes.
 *
 * @author Delmar B. Davis
 */
public class StoreManager {

    // <editor-fold defaultstate="collapsed" desc="Instance">
    private static StoreManager instance = null; // instance of this singleton class
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Members">
    private static boolean remoteFieldsInitialized = false; // indicates config data set prior to construction
    /* config data (possibly set prior to construction) */
    private static String appName; // simple name of the MASS application
    private static String canonicalAppClass; // fully qualified class name of the MASS application
    private static Class provenanceCollectorClass = NoProvenanceCollectionCollector.class; // default provenance collector (provenance collection turned off)
    private static String workingdir = System.getProperty("user.home");
    private static String provDir = "MASSProvData"; // parent directory for all presisted provenance
    private static final String UUID = java.util.UUID.randomUUID().toString(); // UUID for this process (differentiates runs, as well as computing nodes and individual processes on same computing node, from each other
    private static String preprocessedDirName; // simple name for directory that is the parent to persisted preprocessed provenance
    private static String metaDir; // simple name for directory containing persisted annotations
    private static String relationalProvDirName;  // simple name for directory containing persisted processed relational provenance
    private static String processedDirName; // absolute path to parent of persisted processed provenance directories
    private static String preprocessedFullPath; // absolute path to persisted preprocessed provenance
    private static String metaFullPath; // absolute path to annotations
    private static String relationalFullPath; // absolute path to persisted relational provenance
    private static int maxProvStoreSize = 512; // Maximum provenance stores supported
    private static int linesPerBuffer = ProvStaging.MAX_LINES; // lines per individual buffer
    private static int charsPerLine = ProvStaging.MAX_CHAR_PER_LINE; // characrters per line within the buffer
    private static int buffersPerStore = 1; // amount of buffers 
    private static boolean charLengthLocked;
    private static Granularity granularityLevel = Granularity.NONE;
    public static boolean postProcess = true;

    /* state data */
    private StringBuffer[][] provBuffers; // prepared space to divy out to stores
    private final HashMap<Object, ProvenanceStore> ownersStores; // owner to store map
    private final HashMap<ProvenanceStore, Object> storeOwners; // store to owner map
    private final List<ProvenanceStore> recycledStores;
    private static boolean bufferAllocated = false;
    private final Object MAPS_LOCK; // Synchronize on this to lock multiple of the maps listed above, simnultaneously
    private static final Object MGR_LOCK = new Object(); // Synchronize on this to lock the state of the store manager class
    private int nextAvailableBufferIdx = 0; // index of next unnassigned buffer
    private boolean[] availableBuffers; // element is true if buffer buffer is available for provision to newly registered store, otherwise false
    private int maxIntEncountered; // used to count total stores ever constructed: totalStoresConstructed = maxIntEncountered * ((MAX_INT - provBufs.length()) / bufsPerStore) + provBufs.length() 
    private int totalStores = 0;
    /* debug */
    private boolean outOfBuffersExceptionLogged = false;
    private static String lastThreadWithoutRegisteredStore = null;
    private static final Object DEBUG_LOCK = new Object();
// </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Constructors/InstanceGetters">
    private StoreManager() {
        StopWatch.start(true);
        //initProvBuffers(defaultBufferSize);
        ownersStores = new HashMap<>();
        storeOwners = new HashMap<>();
        recycledStores = new ArrayList<>();
        MAPS_LOCK = new Object();
        if (!remoteFieldsInitialized) {
            StoreManager.provenanceCollectorClass = NoProvenanceCollectionCollector.class;
        }
        StopWatch.stop(true);
    }

    public static StoreManager getStoreManager() {
        StopWatch.start(true);
        synchronized (MGR_LOCK) {
            if (instance == null) {
                instance = new StoreManager();
                init();
            }
            StopWatch.stop(true);
            return instance;
        }
    }

    public static StoreManager getStoreManager(Class appClass) {
        StopWatch.start(true);
        synchronized (MGR_LOCK) {
            if (MASSProv.provOn && instance == null) {
                instance = new StoreManager();
                init(appClass);
            }
            StopWatch.stop(true);
            return instance;
        }
    }

    public static StoreManager getStoreManager(String appClass) {
        StopWatch.start(true);
        synchronized (MGR_LOCK) {
            if (instance == null) {
                instance = new StoreManager();
                init(appClass);
            }
            StopWatch.stop(true);
            return instance;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Initializers">
    /**
     * Sets members to their initial state.
     *
     * Note: Purposely, no modifier. This is not meant to be private, it is
     * meant to be protected without allowing subclasses to modify it.
     */
    private static void init() {
        StopWatch.start(true);
        if (!remoteFieldsInitialized) {
            StoreManager.appName = determineAppName();
        }
        commonInit();
        StopWatch.stop(true);
    }

    private static void init(Class appClass) {
        StopWatch.start(true);
        if (!remoteFieldsInitialized) {
            StoreManager.appName = appClass.getSimpleName();
        }
        commonInit();
        StopWatch.stop(true);
    }

    private static void init(String appClass) {
        StopWatch.start(true);
        if (!remoteFieldsInitialized) {
            StoreManager.appName = appClass;
        }
        commonInit();
        StopWatch.stop(true);
    }

    private static void commonInit() {
        StopWatch.start(true);
        if (!remoteFieldsInitialized) {
            StoreManager.provDir = "MASSProvData";
            StoreManager.preprocessedDirName = "Preprocessed";
            StoreManager.processedDirName = "Processed";
            StoreManager.metaDir = "Annotations";
            StoreManager.relationalProvDirName = "RelationalProv";
            initDirs();
        }
        if (!instance.isBufferAllocated()) {
            initBufferSpace();
        } else {
            System.out.println("Buffer space was previously allocated within StoreManager.commonInit");
        }
        StopWatch.stop(true);
    }

    private static void initDirs() {
        StopWatch.start(true);
        initPreprocDir();
        String processeddir = initProcessedDir();
        initAnnotationsDir(processeddir);
        initRelationalDir(processeddir);
        StopWatch.stop(true);
    }

    private static void initRelationalDir(String processeddir) {
        StopWatch.start(true);
        String relationalDir = genRelationalDir(processeddir);
        File file = new File(relationalDir);
        if (!file.exists()) {
            file.mkdirs();
            try {
                StoreManager.relationalFullPath = file.getCanonicalPath();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        StopWatch.stop(true);
    }

    private static String genRelationalDir(String processeddir) {
        StopWatch.start(true);
        StopWatch.stop(true);
        return new StringBuilder(processeddir).
                append(File.separator).
                append(StoreManager.relationalProvDirName).toString();
    }

    private static void initAnnotationsDir(String processeddir) {
        StopWatch.start(true);
        String annotationsDir = genAnnotationsDir(processeddir);
        File file = new File(annotationsDir);
        if (!file.exists()) {
            file.mkdirs();
            try {
                StoreManager.metaFullPath = file.getCanonicalPath();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        StopWatch.stop(true);
    }

    private static String initProcessedDir() {
        StopWatch.start(true);
        String processeddir = genProcessedDir();
        File file = new File(processeddir);
        if (!file.exists()) {
            file.mkdirs();
        }
        StopWatch.stop(true);
        return processeddir;
    }

    private static String genProcessedDir() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return String.join(File.separator, System.getProperty("user.home"),
                StoreManager.provDir, StoreManager.appName, ProvUtils.getHostName()
                + "_" + StoreManager.UUID,
                StoreManager.processedDirName);
    }

    public static String getFinalDirectoryForThisRun() {
        return String.join(File.separator, System.getProperty("user.home"),
                StoreManager.provDir, StoreManager.appName, ProvUtils.getHostName()
                + "_" + StoreManager.UUID);
    }

    private static void initPreprocDir() {
        StopWatch.start(true);
        String preprocdir = genPreprocDir();
        File file = new File(preprocdir);
        if (!file.exists()) {
            file.mkdirs();
            try {
                StoreManager.preprocessedFullPath = file.getCanonicalPath();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        StopWatch.stop(true);
    }

    private static String genPreprocDir() {
        StopWatch.start(true);
        StopWatch.stop(true);
        IO.log("generating preproc dir with the following workingdir: " + workingdir);
        IO.log("preprocdir is: " + String.join(File.separator, workingdir,
                StoreManager.provDir, StoreManager.appName, ProvUtils.getHostName()
                + "_" + StoreManager.UUID,
                StoreManager.preprocessedDirName));
        return String.join(File.separator, workingdir,
                StoreManager.provDir, StoreManager.appName, ProvUtils.getHostName()
                + "_" + StoreManager.UUID,
                StoreManager.preprocessedDirName);
    }

    private static String determineAppName() {
        StopWatch.start(true);
        String appName = new StringBuilder("UnknownMASSApp_").
                append(java.util.UUID.randomUUID().toString()).toString();
        try {
            // get all the stack trace elements
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            //ProvUtils.printst(ste); // for evaluation
            // find the second element that doesn't refer to this class
            int i = 1;
            for (; i < ste.length; i++) {
                if (!ste[i].getClassName().equals(instance.getClass().getName())
                        && !ste[i].getClassName().equals(MASSProv.class.getName())) {
                    break;
                }
            }
            String fullCallingClass;
            if (ste.length - 2 >= 0) {
                //if (i < ste.length) {
                // store the full class name of the first external calling class
//                fullCallingClass = ste[i].getClassName();
                fullCallingClass = ste[ste.length - 2].getClassName(); // moded for evaluation
            } else {
                fullCallingClass = ste[ste.length - 1].getClassName();
            }
//            System.err.println("fullCallingClass: " + fullCallingClass);
            StoreManager.canonicalAppClass = fullCallingClass;
            int startOfSimpleClassName = fullCallingClass.lastIndexOf(".") + 1;
            if (startOfSimpleClassName < fullCallingClass.length()) {
                String tempAppName;
                if (startOfSimpleClassName < 0 && !fullCallingClass.isEmpty()) {
                    tempAppName = ProvUtils.isValidFilename(fullCallingClass)
                            ? fullCallingClass : appName;
                } else {
                    tempAppName = fullCallingClass.substring(
                            fullCallingClass.lastIndexOf(".") + 1);
                }
                appName = ProvUtils.isValidFilename(tempAppName)
                        ? tempAppName : appName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StopWatch.stop(true);
        return appName;
    }

    private static String getDateTimeFilename() {
        StopWatch.start(true);
        ZonedDateTime datetime = new GregorianCalendar(TimeZone.getDefault()).
                toZonedDateTime();
        // consider escaping with datetime.replaceAll("[^a-zA-Z0-9.-]", "_");
        // build filename (basically escape datetime.toString())
        String dateTimeFilename = new StringBuilder("Y").
                append(datetime.getYear()).append('M').
                append(datetime.getMonthValue()).append('D').
                append(datetime.getDayOfMonth()).append("H").
                append(datetime.getHour()).append('M').
                append(datetime.getMinute()).append('S').
                append(datetime.getSecond()).append('.').
                append(datetime.getNano() / 1000000).
                append(datetime.getOffset().toString().replace(':', 'H')).
                append("M[").
                append(datetime.getZone().toString().
                        replaceAll("/", "-")).
                append(']').toString();
        StopWatch.stop(true);
        return dateTimeFilename;
    }

    /**
     * Allocates provenance buffers that will be provided to provenance stores
     * during the store-construction.
     */
    public static void initBufferSpace() {
        StopWatch.start(true);
        if (instance == null) {
            return;
        }
        if (StoreManager.bufferAllocated) {
            IO.log("WARNING: Attempting to reallocate provenance buffer space!"
                    + "\n\tMake sure that the configuration methods are called"
                    + " prior to MASSProv.initLocal in application MASSProv"
                    + " initializing method.");
        }
        // make sure manager is instantiated
        StoreManager mgr = StoreManager.getStoreManager();
        // get sizes
        int stores = StoreManager.maxProvStoreSize;
        int bufsPerStore = StoreManager.buffersPerStore;
        int lines = StoreManager.linesPerBuffer;
        int chars = StoreManager.charsPerLine;
        IO.log("Buffer Allocation: stores=" + stores + " bufsPerStore=" + bufsPerStore + "linesPerBuffer=" + lines + " charsPerLine=" + chars);
        // allocate array of buffers
        mgr.initProvBuffers(stores, bufsPerStore, lines);
        StringBuffer[][] bufs = mgr.provBuffers; // loop ref
        // loop ref
        boolean[] avails = instance.availableBuffers;
        for (int i = 0, im = bufs.length; i < im; i++) {
            instance.availableBuffers[i] = true;
            for (int j = 0, jm = bufs[i].length; j < jm; j++) {
                bufs[i][j] = new StringBuffer(chars);
            }
        }
        StoreManager.bufferAllocated = true;
        charLengthLocked = true;
        StopWatch.stop(true);
    }

    /**
     * Initializes the buffer container based on the provided dimension details
     *
     * @param storesMaintained Maximum number of managed stores
     * @param bufsPerStore Buffers provided to each store
     * @param lines Lines of provenance data maintained within each buffer
     * @param chars Characters maintained within each line
     */
    private void initProvBuffers(int storesMaintained, int bufsPerStore, int lines) {
        StopWatch.start(true);
        provBuffers = new StringBuffer[storesMaintained * bufsPerStore][lines];
        availableBuffers = new boolean[storesMaintained * bufsPerStore];
        StopWatch.stop(true);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Setters">
    public static void setGranularityLevel(Granularity level) {
        StopWatch.start(true);
        StoreManager.granularityLevel = level;
        StopWatch.stop(true);
    }

    public static void setWorkingDir(String workingDir) {
        StopWatch.start(true);
        workingdir = workingDir;
        StopWatch.stop(true);
    }

    public static void setProvenanceCollectionClass(Class collectionClass) {
        StopWatch.start(true);
        StoreManager.provenanceCollectorClass = collectionClass;
        StopWatch.stop(true);
    }

    /**
     * Assigns the amount of buffers that should be provided to each newly
     * registered provenance store
     *
     * @param size Amount of buffers that should be provided to each newly
     * registered provenance store
     */
    public static void setBuffersPerStore(int size) {
        StopWatch.start(true);
        StoreManager.buffersPerStore = size;
        StopWatch.stop(true);
    }

    static void setCharLength(int i) {
        StopWatch.start(true);
        if (!charLengthLocked) {
            charsPerLine = i;
            bufferAllocated = false;
        }
        StopWatch.stop(true);
    }

    public static void setMaxProvStoreSize(int maxProvStoreSize) {
        StopWatch.start(true);
        StoreManager.maxProvStoreSize = maxProvStoreSize;
        bufferAllocated = false;
        StopWatch.stop(true);
    }

    public static void setLinesPerBuffer(int linesPerBuffer) {
        StopWatch.start(true);
        StoreManager.linesPerBuffer = linesPerBuffer;
        bufferAllocated = false;
        StopWatch.stop(true);
    }

    public static void setCharsPerLine(int charsPerLine) {
        StopWatch.start(true);
        if (!charLengthLocked) {
            StoreManager.charsPerLine = charsPerLine;
            bufferAllocated = false;
        }
        StopWatch.stop(true);
    }

    public void setDebug(boolean debug) {
        StopWatch.start(true);
        System.out.println("Debug " + (debug ? "on..." : "off..."));
        this.debug = debug;
        StopWatch.stop(true);
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters">    
    public static boolean isInitialized() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return instance != null;
    }

    public static String getWorkingDir() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return workingdir;
    }

    public static Granularity getGranularityLevel() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return granularityLevel;
    }

    /**
     * Provides a count of the total amount of stores ever registered.
     *
     * @return number of stores registered by this manager since it was
     * initialized
     */
    public int totalStoresRegistered() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return maxIntEncountered * ((Integer.MAX_VALUE - provBuffers.length) / buffersPerStore) + provBuffers.length;
    }

    /**
     * Provides the class of the provenance collector
     *
     * @return the class of the provenance collector
     */
    public Class getProvenanceCollectionClass() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return provenanceCollectorClass;
    }

    private static String genAnnotationsDir(String processeddir) {
        StopWatch.start(true);
        StopWatch.stop(true);
        return new StringBuilder(processeddir).
                append(File.separator).append(StoreManager.metaDir).toString();
    }

    public String getPathPreprocessedProv() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return preprocessedFullPath;
    }

    public String getPathAnnotations() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return metaFullPath;
    }

    public String getPathRelational() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return relationalFullPath;
    }

    public static String getAppName() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return appName;
    }

    public static StringBuffer getAppNameBuffer() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return new StringBuffer(appName);
    }

    public static String getCanonicalAppClass() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return canonicalAppClass;
    }

    public ProvenanceStore getStore(Object owner) {
        StopWatch.start(true);
        StopWatch.stop(true);
        return ownersStores.get(owner);
    }

    public static int getBuffersPerStore() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return buffersPerStore;
    }

    public ProvenanceStore getStoreOfCurrentThread() {
        StopWatch.start(true);
        ProvenanceStore store = null;
        if (MASSProv.provOn) {
            Thread thread = Thread.currentThread();
            store = getStore(thread);
            if (store == null) {
                if (totalStores < StoreManager.maxProvStoreSize) {
                    store = new ProvenanceStore(thread);
                    if (!store.isRegistered()) {
                        store = null;
                    }
                }
                if (store == null) {
                    synchronized (recycledStores) {
                        // try to use a recycled thread last
                        if (recycledStores.size() > 0) {
                            store = recycledStores.remove(0);
                            store.setParent(thread);
                            store.setRegistered(addProvenanceStore(store, thread));
                        }
                    }
                }
                if (store == null || !store.isRegistered()) {
                    store = null;
                    lastThreadWithoutRegisteredStore = Thread.currentThread().getName();
                    //IO.log("Store for thread not registered!");
                } else if (ProvenanceRecorder.granularityLevel()
                        >= Granularity.PROCEDURE.getValue()) { // this will ensure a call-chain
                    ResourceMatcher matcher = ResourceMatcher.getMatcher();
                    matcher.pushActivityID(ProvUtils.getUniversalResourceID(
                            "callChainAnchor_for_" + Thread.currentThread().getName()));
                }
            }
        }
        StopWatch.stop(true);
        return store;
    }

    public static String getLastThreadWithoutRegisteredStore() {
        return lastThreadWithoutRegisteredStore;
    }

    public Object getOwner(ProvenanceStore store) {
        StopWatch.start(true);
        StopWatch.stop(true);
        return storeOwners.get(store);
    }

    public boolean isRegistered(ProvenanceStore store) {
        StopWatch.start(true);
        boolean foundStoreInStoreOwnersMap = storeOwners.containsKey(store);
        boolean foundStoreInOwnersStoresMap = ownersStores.containsValue(store);
        StopWatch.stop(true);
        return foundStoreInStoreOwnersMap && foundStoreInOwnersStoresMap;
    }

    /**
     * Indicates whether or not the provenance buffer space has been allocated
     *
     * @return True if the provenance buffers are ready to be referenced by
     * provenance stores.
     */
    public boolean isBufferAllocated() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return StoreManager.bufferAllocated;
    }

    public static int getMaxProvStoreSize() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return maxProvStoreSize;
    }

    public static int getLinesPerBuffer() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return StoreManager.linesPerBuffer;
    }

    public static int getCharsPerLine() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return StoreManager.charsPerLine;
    }

    public boolean isDebug() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return debug;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Manage">
    /**
     * Adds a provenance store. The store directory (keyed by owner) and owner
     * directory (keyed by store), are automatically updated.
     *
     * NOTE: If the store has already been added, it may be assigned a new
     * owner, both internally and within the store manager directories. This
     * occurs when the store is found, but its indexed owner is different than
     * the one found in the directory. The owner of the store is meant to be a
     * lookup device.
     *
     * @param store - The provenance store to add to the manager's directories
     * @param owner - Object that currently maintains the store to be added
     * (default key for the new store owner in the provStores map and
     * storeOwners map)
     * @return
     */
    public boolean addProvenanceStore(ProvenanceStore store, Object owner) {
        StopWatch.start(true);
        if (debug) {
            debug_addProvenanceStore_SystemOut(store, owner);
        }
        boolean added = false;
        logInitErrorsAddProvenanceStore(store, owner);
        // do not add a null provenance store or one with a null owner
        if (store != null && owner != null) {
            // USE THIS TO TEST FOR MYSTERIOUSLY VANISHING STORES
            //debugStoreAdditionInitData(store, owner);

            // only add the store if provenance collection is turned on
            if (!store.getCollectorClass()
                    .equals(NoProvenanceCollectionCollector.class)) {
                // sync on all maps since more than one is modified
                synchronized (MAPS_LOCK) {
                    /* determine scenario */
                    // find store
                    boolean foundStoreInStoreOwnersMap = storeOwners.containsKey(store);
                    boolean foundStoreInOwnersStoresMap = ownersStores.containsValue(store);
                    // find owner
                    boolean foundOwnerInStoreOwnersMap = storeOwners.containsValue(owner);
                    boolean foundOwnerInOwnersStoresMap = ownersStores.containsKey(owner);
                    boolean removedStore = false;
                    boolean removedOwner = false;
                    // 1?
                    if (foundStoreInStoreOwnersMap) {
                        // remove 1
                        storeOwners.remove(store);
                        if (!removedStore) {
                            removedStore = true;
                        }
                    }
                    // 2?
                    if (foundStoreInOwnersStoresMap) {
                        // remove 2
                        removeEntriesByValue(ownersStores, store);
                        if (!removedStore) {
                            removedStore = true;
                        }
                    }
                    // 3?
                    if (foundOwnerInStoreOwnersMap) {
                        // remove 3
                        ownersStores.remove(owner);
                        if (!removedOwner) {
                            removedOwner = true;
                        }
                    }
                    // 4?
                    if (foundOwnerInOwnersStoresMap) {
                        // remove 4
                        removeEntriesByValue(storeOwners, owner);
                        if (!removedOwner) {
                            removedOwner = true;
                        }
                    }
                    // fresh addition to store maps
                    uncheckedAddProvenanceStore(store, owner);
                    // count it
                    synchronized (MASSProv.storesAdded) {
                        MASSProv.storesAdded++;
                        added = true;
                    }
                    synchronized (MASSProv.storesRemoved) {
                        if (removedStore) {
                            MASSProv.storesRemoved++;
                        }
                    }
                }
            }
        }
        StopWatch.stop(true);
        return added;
    }

    private void debug_addProvenanceStore_SystemOut(ProvenanceStore store, Object owner) {
        StopWatch.start(true);
        System.out.println("store null: " + (store == null));
        System.out.println("owner null: " + (owner == null));
        System.out.println("storeOwners.size(): " + storeOwners.size());
        System.out.println("StoreManager.maxProvStoreSize: " + StoreManager.maxProvStoreSize);
        System.out.println("supportsMoreStores: " + (storeOwners.size()
                < StoreManager.maxProvStoreSize));
        System.out.println("store.getCollectorClass(): " + store.getCollectorClass().getSimpleName());
        StopWatch.stop(true);
    }

    private void uncheckedAddProvenanceStore(ProvenanceStore store, Object owner) {
        StopWatch.start(true);
        /* SCENARIO 1: Unmapped store added for unmapped owner */
        // add store with owner to <store,owner> map storeOwners
        storeOwners.put(store, owner);
        ownersStores.put(owner, store);
        StopWatch.stop(true);
    }

//    /** //// IF USED, NEEDS TO RECYCLE STORE TO RELEASE OWNER
//     * Allows garbage collection on objects that maintain a store without losing
//     * the store index. The owner of the store is replaced by a new, empty,
//     * simple java object, and the store and owner tables are updated
//     * accordingly.
//     *
//     * NOTE: If the store was not previously indexed, it will be added along
//     * with the replacement store owner.
//     *
//     * @param store - The store that's owner reference needs to be released
//     * @return The newly created and assigned store owner
//     */
//    public Object releaseStoreOwner(ProvenanceStore store) {
//        StopWatch.start(true);
//        // sync on maps, as more than one is being modified
//        synchronized (MAPS_LOCK) {
//            Object newOwner = new Object();
//            Object oldOwner = storeOwners.get(store);
//            store.setOwner(newOwner);
//            // if the store exists as a key in the store owner directory
//            if (oldOwner != null) {
//                // replace the old owner with the new owner in the owner directory
//                storeOwners.put(store, newOwner);
//                // remove the store from its current position in the store directory
//                ownersStores.remove(oldOwner);
//                // add  store back into  store directory at <newOwner.HashKey()>
//                ownersStores.put(newOwner, store);
//                // replace the reference to the old owner within the store
//                store.setOwner(newOwner);
//            }
//            //throw new java.util.ConcurrentModificationException();
//            StopWatch.stop(true);
//            return newOwner;
//        }
//    }
    /**
     * Removes a store from managed space and mapping. NOTE: Provenance buffers
     * are not cleared or persisted.
     *
     * @param store ProvenanceStore to be removed
     */
    public void releaseStore(ProvenanceStore store) {
        StopWatch.start(true);
        synchronized (MAPS_LOCK) {
            synchronized (recycledStores) {
                recycledStores.add(store); // all unused recycled stores must be persisted later
            }
            // check release logic
            Object owner = storeOwners.get(store);
            storeOwners.remove(store);
            if (owner != null && ownersStores.remove(owner) == null) {
                removeEntriesByValue(ownersStores, store);
            }
            synchronized (MASSProv.storesRemoved) {
                MASSProv.storesRemoved++;
            }
            store.setRegistered(false);
            // REMOVED: Don't clean buffers and make them available
//            int start = store.getManagedBufferStartingIndex();
//            int end = start + StoreManager.buffersPerStore;
//            for (int i = start, im = end; i < im; i++) {
//                availableBuffers[i] = true;
//            }
            //store.persistBuffers();
            // NEW: reuse the store instead, as is (later a new parent is assigned)
        }
        StopWatch.stop(true);
    }

    /**
     * Provides buffersPerStore buffers for the provided provenance store's
     * final registration step.
     *
     * @param store - A ProvenanceStore being registered, that has not yet been
     * assigned buffer space.
     * @return A reference to the section (length: buffersPerStore) of
     * provBuffers assigned to the specified store. A null reference is
     * returned, if all buffers are already assigned to stores. A reference to
     * the previously assigned buffers is returned, if the provided store has
     * already been assigned buffer references prior to calling this method.
     */
    StringBuffer[][] retrieveAssignedBuffers(ProvenanceStore store) {
        StopWatch.start(true);
        StringBuffer[][] bufs = null;
        synchronized (provBuffers) {
            try {
                // the starting index of the buffers in the provided store
                int previouslySetBufferIdx = store.getManagedBufferStartingIndex();
                // the last starting index within the StoreManager managed buffers 
                // that could point to a full set of contiguous buffer space without 
                // causing exceeding the index boundary of the buffer space
                int lastStartingIdx = provBuffers.length - StoreManager.buffersPerStore;
                // determines if the index can be used to safely retrieve 
                // buffersPerStore buffers from provBuffers field
                boolean assignedIndexIsValid = !(previouslySetBufferIdx < 0 || previouslySetBufferIdx > lastStartingIdx);
                int bufferIdx;
                if (assignedIndexIsValid) {
                    bufferIdx = previouslySetBufferIdx;
                } else {
                    bufferIdx = nextAvailableBufferIdx;
                    if (nextAvailableBufferIdx >= provBuffers.length) {
                        bufferIdx = findNextAvailableBufferIdx(0);
                    }
                }
                if (isRegistered(store)) { // prevent stray references by tracking for cleanup
                    bufs = new StringBuffer[StoreManager.buffersPerStore][StoreManager.linesPerBuffer];
                    store.setManagedBufferStartingIndex(bufferIdx);
                    System.arraycopy(provBuffers, bufferIdx, bufs, 0, bufs.length);
                    for (int i = bufferIdx, im = bufferIdx + StoreManager.buffersPerStore; i < im; i++) {
                        availableBuffers[i] = false;
                        for (int j = 0, jm = linesPerBuffer; j < jm; j++) {
                            // buffers are left dirty on store release, so clean it
                            provBuffers[i][j].delete(0, StoreManager.charsPerLine);
                        }
                    }
                    // keep counting even if it will be out of bounds (quick ref 4 numStores total)
                    if (nextAvailableBufferIdx + StoreManager.buffersPerStore < Integer.MAX_VALUE) {
                        nextAvailableBufferIdx += StoreManager.buffersPerStore;
                    } else {
                        maxIntEncountered++;
                        nextAvailableBufferIdx = provBuffers.length;
                    }
                }
            } catch (Exception e) {
                bufs = null;
                if (!outOfBuffersExceptionLogged) {
                    outOfBuffersExceptionLogged = true;
                    e.printStackTrace(IO.getLogWriter());
                }
            }
        }
        if (bufs != null) {
            // used to safeguard future store construction
            totalStores++; // if this method is called for anything but store construction, this increment is erroneous
        } else {
            // assuming store was constructed and added, but buffers are unavailable...
            // get rid of store references
            synchronized (MAPS_LOCK) {
                Object owner = null;
                boolean foundStoreInStoreOwnersMap = storeOwners.containsKey(store);
                boolean foundStoreInOwnersStoresMap = ownersStores.containsValue(store);
                if (foundStoreInStoreOwnersMap) {
                    owner = storeOwners.get(store);
                }
                // find owner
                boolean foundOwnerInStoreOwnersMap = storeOwners.containsValue(owner);
                boolean foundOwnerInOwnersStoresMap = ownersStores.containsKey(owner);
                boolean removedStore = false;
                boolean removedOwner = false;
                // 1?
                if (foundStoreInStoreOwnersMap) {
                    // remove 1
                    storeOwners.remove(store);
                    if (!removedStore) {
                        removedStore = true;
                    }
                }
                // 2?
                if (foundStoreInOwnersStoresMap) {
                    // remove 2
                    removeEntriesByValue(ownersStores, store);
                    if (!removedStore) {
                        removedStore = true;
                    }
                }
                // 3?
                if (foundOwnerInStoreOwnersMap) {
                    // remove 3
                    ownersStores.remove(owner);
                    if (!removedOwner) {
                        removedOwner = true;
                    }
                }
                // 4?
                if (foundOwnerInOwnersStoresMap) {
                    // remove 4
                    removeEntriesByValue(storeOwners, owner);
                    if (!removedOwner) {
                        removedOwner = true;
                    }
                }
                synchronized (MASSProv.storesRemoved) {
                    if (removedStore) {
                        MASSProv.storesRemoved++;
                    }
                }
            }
            store.setRegistered(false);
        }
        StopWatch.stop(true);
        return bufs;
    }

    /**
     * Walks next available buffer indices to find the first open buffers to
     * give to a new store. This relatively slow operation is only used after
     * maxProvStoreSize * buffersPerStore have been given out to new provenance
     * stores. This happens when stores are released and new stores take their
     * old buffer slots. For instance, maxProvStoreSize is 4 with just 1 buffer
     * per store, and 4 stores are assigned buffers, then 2 stores are released
     * and 2 more stores are then assigned buffers. In the example, the first 4
     * stores have exhausted the contiguous buffers in the array, during which
     * time they simply took the next available buffer. When the last 2 buffers
     * were allocated, the pointer to the next available buffer space has
     * exceeded the length of the buffer array. Therefore, it is necessary to
     * find the next available buffer.
     *
     * @param start - Index in availableBuffers to start looking for the next
     * available buffer
     * @return the index of the next available buffer
     */
    private int findNextAvailableBufferIdx(int start) throws Exception {
        StopWatch.start(true);
        int ptr = provBuffers.length;
        for (int i = start, im = availableBuffers.length; i < im; i++) {
            if (availableBuffers[i]) {
                ptr = i;
                break;
            }
        }
        if (ptr == provBuffers.length) {
            throw new Exception("All buffers used");
        }
        StopWatch.stop(true);
        return ptr;
    }

    /**
     * Removes all of the entries from the specified map that have a value
     * matching the specified value
     *
     * @param <T> The key type for map
     * @param <E> The value type for map
     * @param map The map to remove entries from
     * @param value The value that removed entries' values should match
     */
    public static <T, E> void removeEntriesByValue(Map<T, E> map, E value) {
        StopWatch.start(true);
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            if (Objects.equals(value, (E) it.next())) {
                it.remove();
            }
        }
        StopWatch.stop(true);
    }

    /**
     * Provides current state data in a serializable format
     *
     * @return Array of serializeable objects with content corresponding to
     * current state of members in this class
     */
    public static Object[] provideStateDataForMessage() {
        StopWatch.start(true);
        Object[] data = new Object[22];
        data[0] = StoreManager.provenanceCollectorClass; // maybe serializable???
        data[1] = (Integer) StoreManager.maxProvStoreSize;
        data[2] = (Integer) StoreManager.buffersPerStore;
        data[3] = (Integer) StoreManager.linesPerBuffer;
        data[4] = (Integer) StoreManager.charsPerLine;
        data[5] = StoreManager.appName;
        data[6] = StoreManager.canonicalAppClass;
        data[7] = StoreManager.metaDir;
        data[8] = StoreManager.metaFullPath;
        data[9] = StoreManager.preprocessedDirName;
        data[10] = StoreManager.preprocessedFullPath;
        data[11] = StoreManager.processedDirName;
        data[12] = StoreManager.provDir;
        data[13] = StoreManager.relationalFullPath;
        data[14] = StoreManager.relationalProvDirName;
        data[15] = StoreManager.workingdir;
        data[16] = StoreManager.granularityLevel;
        data[17] = (Boolean) StoreManager.postProcess;
        data[18] = MASSProv.agentFilter;
        data[19] = MASSProv.agentFilterCriteria;
        data[20] = MASSProv.placeFilter;
        data[21] = MASSProv.placeFilterCriteria;
        IO.log("storing working directory in message for remote prov init: " + data[15]);
        IO.log("storing preproc directory in message for remote prov init...\n\t(partial): " + data[9] + "\n\t(full): " + data[10]);
        StopWatch.stop(true);
        return data;
    }

    /**
     * Uses argument from MASSProv Initialization Message to set current state
     *
     * @param data - argument (payload) of MASS message
     */
    public static void consumeMessageStateData(Object[] data) {
        StopWatch.start(true);
        if (data.length >= 22) {
            // set fields
            StoreManager.provenanceCollectorClass = (Class) data[0];
            StoreManager.maxProvStoreSize = (Integer) data[1];
            StoreManager.buffersPerStore = (Integer) data[2];
            StoreManager.linesPerBuffer = (Integer) data[3];
            StoreManager.charsPerLine = (Integer) data[4];
            StoreManager.appName = (String) data[5];
            StoreManager.canonicalAppClass = (String) data[6];
            StoreManager.metaDir = (String) data[7];
            StoreManager.metaFullPath = (String) data[8];
            StoreManager.preprocessedDirName = (String) data[9];
            StoreManager.preprocessedFullPath = (String) data[10];
            StoreManager.processedDirName = (String) data[11];
            StoreManager.provDir = (String) data[12];
            StoreManager.relationalFullPath = (String) data[13];
            StoreManager.relationalProvDirName = (String) data[14];
            StoreManager.workingdir = (String) data[15];
            StoreManager.granularityLevel = (Granularity) data[16];
            StoreManager.postProcess = (Boolean) data[17];
            MASSProv.agentFilter = (AgentFilter) data[18];
            MASSProv.agentFilterCriteria = data[19];
            MASSProv.placeFilter = (PlaceFilter) data[20];
            MASSProv.placeFilterCriteria = data[21];
            IO.log("receiving working directory in message from master for remote prov init: " + (String) data[15]);
            IO.log("receiving preproc directory in message from master for remote prov init...\n\t(partial): " + (String) data[9] + "\n\t(full): " + (String) data[10]);
            // use data to initialize directories
            initDirs();
            // remember that these fields were initialized to subvert their default initialization
            remoteFieldsInitialized = true;
        }
        StopWatch.stop(true);
    }

    /**
     * Manages a ProvenanceStore that was allocated and registered with a
     * different StoreManager
     *
     * @param store An existing store with allocated buffer space that
     * references buffers other than those managed here
     * @param owner The object which
     */
    public void adoptForeignStore(ProvenanceStore store, Object owner) {
        StopWatch.start(true);
        StringBuffer[][] bufs = store.getBuffer();
        // walk through all the buffers
        for (int i = 0, im = provBuffers.length; i < im; i += buffersPerStore) {
            // if this index and the next n assigned per store don't overrun the
            // availableBuffers array and the buffer is available at this index 
            // (note: it is assumed i through i + n are also available since 
            //  they are marked together when they are taken or freed up)
            if (i + buffersPerStore < availableBuffers.length
                    && availableBuffers[i]) {
                // if the stores buffer was allocated
                if (bufs != null) {
                    if (bufs.length == buffersPerStore) {
                        try {
                            // copy the buffer references 
                            System.arraycopy(bufs, 0, provBuffers, i,
                                    buffersPerStore);
                            store.setManagedBufferStartingIndex(i);
                            // mark these indices as unavailable
                            for (int j = 0, jm = buffersPerStore; j < jm; j++) {
                                availableBuffers[i + j] = false;
                            }
                            // register in the store directory along with its 
                            // migrated owner
                            addProvenanceStore(store, owner);
                            //IO.log(store.getUUID() + " of " + store.getOriginalOwnerUUID() + " adopted by StoreManager on " + ProvUtils.getHostName());
                        } catch (Exception e) {
                            e.printStackTrace(IO.getLogWriter());
                        }
                        break;
                    } else {
                        IO.log("Store migrated with "
                                + store.getOriginalOwnerUUID()
                                + " has a buffer with length: "
                                + bufs.length
                                + ", while it was expected to have a length of "
                                + buffersPerStore);
                    }
                } else {
                    IO.log("Store migrated with " + store.getOriginalOwnerUUID()
                            + " has a null buffer");
                }
            }
        }
        StopWatch.stop(true);
    }

    /**
     * Manages the abandonment of a store by its owner (e.g. when the store
     * owner leaves the host and wants to reduce its serializable size by
     * leaving the store on the departure host)
     *
     * @param store
     */
    public void orphanStore(ProvenanceStore store) {
        StopWatch.start(true);
        store.persistBuffers();
        releaseStore(store);
        StopWatch.stop(true);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Persistence">
    public void persistAllStores() {
        StopWatch.start(true);
        IO.log("persistAllStores has been called on "
                + ProvUtils.getHostName());
        String msg = ProvUtils.getHostName() + " is persisting "
                + ownersStores.size() + " current and " + recycledStores.size()
                + " recycled provenance stores to disk to "
                + StoreManager.getStoreManager().
                        getPathPreprocessedProv();
        IO.log(msg);
        //System.err.println(msg);
        msg = "Stores have been mapped to new owners "
                + MASSProv.storesAdded.toString()
                + " times, by the StoreManager.";
        IO.log(msg);
        //System.err.println(msg);
        msg = "and " + MASSProv.storesRemoved.toString()
                + " store mappings have been removed.";
        IO.log(msg);
        //System.err.println(msg);
        int highest = 0;
        for (ProvenanceStore store : ownersStores.values()) {
            store.persistBuffers();
            store.close();
            if (store.getHighestSequenceLengthEncountered() > highest) {
                highest = store.getHighestSequenceLengthEncountered();
            }
        }
        for (ProvenanceStore store : recycledStores) {
            store.persistBuffers();
            store.close();
            if (store.getHighestSequenceLengthEncountered() > highest) {
                highest = store.getHighestSequenceLengthEncountered();
            }
        }
        if (highest > 0) {
            IO.log("Tuning Information: The highest length sequence "
                    + "added to any provenance store on "
                    + ProvUtils.getHostName() + " was " + highest);
        }
        StopWatch.stop(true);
    }

    public void postProcessPersistedStores() {
        StopWatch.start(true);
        IO.log(ProvUtils.getHostName()
                + " provenance StoreManager is postProcessing Persisted Stores "
                + "from "
                + preprocessedFullPath + " to " + relationalFullPath
                + File.separatorChar + appName + ".ttl");
        boolean modelEmpty = true;
        File preProcDir = new File(preprocessedFullPath);

        if (preProcDir.isDirectory()) {
            File[] preprocessedFiles = preProcDir.listFiles();
            if (preprocessedFiles.length > 0) {
                ProvModeler pm = ProvModeler.getInstance();
                try {
                    pm.init(relationalFullPath + File.separatorChar
                            + appName + ".ttl");
                } catch (IOException ex) {
                    pm.init();
                    ex.printStackTrace(IO.getLogWriter());
                }
                String line = null;
                String[] resourceIdentifiers = null;
                int idx = -1;
                for (File file : preprocessedFiles) {
                    Scanner scanner = null;
                    try {
                        scanner = new Scanner(file);
                    } catch (FileNotFoundException ex) {
                        System.err.println("Error, file not found, while "
                                + "reading " + file.toString());
                    }
                    if (scanner != null) {
                        while (scanner.hasNextLine()) {
                            line = scanner.nextLine();
                            idx = line.indexOf(">:");
                            if (idx >= 0) {
                                resourceIdentifiers = line.substring(idx + 2)
                                        .split(ProvStaging.RESOURCE_DELIMITER);
                                if (resourceIdentifiers.length == 3) {
                                    if (resourceIdentifiers[2].startsWith("\"")) {
                                        // remove the quotes
                                        resourceIdentifiers[2]
                                                = resourceIdentifiers[2].
                                                        replaceAll("\"", "");
                                        // since there are quotes, its a literal, 
                                        // so add statement ending in literal
                                        pm.addLiteral(resourceIdentifiers[0],
                                                resourceIdentifiers[1],
                                                resourceIdentifiers[2]);
//                                    if(resourceIdentifiers[2].startsWith("\"")) {
//                                        Resource subject = ResourceFactory.createResource(resourceIdentifiers[0]);
//                                        String strippedObjectText = resourceIdentifiers[2].replaceAll("<", "");
//                                        strippedObjectText = resourceIdentifiers[2].replaceAll(">", "");
//                                        
//                                        String strippedPredicateText = resourceIdentifiers[1].replaceAll("<", "");
//                                        strippedPredicateText = resourceIdentifiers[1].replaceAll(">", "");
//                                        Property prop = ResourceFactory.createProperty(strippedPredicateText);
//                                        pm.addLiteral(subject, prop, strippedObjectText);

                                    } else { // triple format was resource property resource
                                        // so add it that way                                        
                                        pm.createStatement(resourceIdentifiers[0],
                                                resourceIdentifiers[1],
                                                resourceIdentifiers[2]);
//                                        Resource subject = ResourceFactory.createResource(resourceIdentifiers[0]);
//                                        Property prop = ResourceFactory.createProperty(resourceIdentifiers[1]);
//                                        pm.addLiteral(subject, prop, resourceIdentifiers[2]);

                                    }
                                    modelEmpty = false;
                                }
                            }
                        }
                        scanner.close();

                    }
                }
                if (!modelEmpty) {
//                    System.err.println("Persisting relational model to: "
//                            + pm.getProvOutputFilePath());
                    if (pm.persist()) {
//                        System.err.println("Successfully persisted");
                    }
                }
            }
        }
        StopWatch.stop(true);
    }

    public void deletePreProcessedProvenanceDirectory() {
        StopWatch.start(true);
        try {
            File dir = new File(preprocessedFullPath);
            IO.log("Deleting " + dir.getAbsolutePath());
            FileUtils.deleteDirectory(dir);
        } catch (IOException ex) {
            ex.printStackTrace(IO.getLogWriter());
        }
        StopWatch.stop(true);
    }

    public void deletePreProcessedProvenanceDirectorysMASSProvParent() {
        StopWatch.start(true);
        File dir = new File(preprocessedFullPath)
                .getParentFile().getParentFile().getParentFile();
        IO.log("Deleting " + dir.getAbsolutePath());
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException ex) {
            ex.printStackTrace(IO.getLogWriter());
        }
        StopWatch.stop(true);
    }

    public String getPreProcessedProvenanceDirectorysMASSProvParent() {
        return new File(preprocessedFullPath)
                .getParentFile().getParentFile().getParentFile().getAbsolutePath();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Utility">
    /**
     * Cleans the MASSProvData directory under the MASSProv working directory
     * (if it exists) and all subdirectories. Do not call this method after
     * directories have been generated, as other methods require the existence
     * of the directories in order to operate correctly.
     */
    public static void deleteMASSProvDataDirectoryWithWorkingDirectoryParent() {
        StopWatch.start(true);
        String dirname = String.join(File.separator, workingdir, StoreManager.provDir);
        File dir = new File(dirname);
        IO.log("Deleting " + dir.getAbsolutePath());
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException ex) {
            ex.printStackTrace(IO.getLogWriter());
        }
        StopWatch.stop(true);
    }

    /**
     * For debugging post processing.... This method reads all the lines from
     * all the preprocessed files and writes it all into the file at the
     * location provided, then copies it to the final location provided.
     *
     * @param outputFilename - Name of file to append all of the preprocessed to
     * @param finalOutputFilename - Name of file to copy the file located at
     * outputFilename to (assumes that outputFilename will be deleted and
     * destination directory of finalOutputFilename will not)
     */
    public void amalgamateAndCopyPreprocessedProvenance(String outputFilename, String finalOutputFilename) {
        StopWatch.start(true);
        File preProcDir = new File(preprocessedFullPath);
        File outputFile = new File(outputFilename);
        File finalOutputFile = new File(finalOutputFilename);
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(StoreManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!finalOutputFile.exists()) {
            try {
                finalOutputFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(StoreManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        IO.log("Amalgamating preprocessed provenance to " + outputFile.getAbsolutePath());
        BufferedWriter writer = null;
        try {
            FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
            writer = new BufferedWriter(fw);
        } catch (IOException ex) {
            ex.printStackTrace(IO.getLogWriter());
        }
        if (writer != null) {
            if (preProcDir.isDirectory()) {
                File[] preprocessedFiles = preProcDir.listFiles();
                if (preprocessedFiles.length > 0) {
                    String line = null;
                    Scanner scanner = null;
                    for (File file : preprocessedFiles) {
                        try {
                            scanner = new Scanner(file);
                        } catch (FileNotFoundException ex) {
                            IO.log("Error, file not found, while reading "
                                    + file.toString());
                        }
                        if (scanner != null) {
                            try {
                                while (scanner.hasNextLine()) {
                                    // add to new file here
                                    writer.append(scanner.nextLine()).append("\n");
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace(IO.getLogWriter());
                            }
                            scanner.close();
                        }
                        try {
                            writer.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace(IO.getLogWriter());
                        }
                    }
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        ex.printStackTrace(IO.getLogWriter());
                    }
                }
            }
            // copy the file to its permanent path
            try {
                IO.log("Copying amalgamated preprocessed provenance to " + finalOutputFile.getAbsolutePath());
                java.nio.file.Files.copy(outputFile.toPath(), finalOutputFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace(IO.getLogWriter());
            }
        }
        StopWatch.stop(true);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Debug">
    private boolean debug = false;

    private void debugStoreAdditionInitData(ProvenanceStore store, Object owner) {
        StopWatch.start(true);
        //// DEBUG ADDITION OF STORE ////////////
        String collectorClassName;
        if (store != null) {
            collectorClassName = store.getCollectorClass() == null ? "null"
                    : store.getCollectorClass().getSimpleName();
        } else {
            collectorClassName = "Store null, collector cannot be retrieved";
        }
        String ownerClassName = "null";
        if (owner != null) {
            ownerClassName = owner.getClass().getSimpleName();
        }
        IO.log("Adding prov store for "
                + ownerClassName
                + " object with collector class, "
                + collectorClassName);
        //// DEBUG ADDITION OF STORE ////////////
        StopWatch.stop(true);
    }

    private void logInitErrorsAddProvenanceStore(ProvenanceStore store, Object owner) {
        StopWatch.start(true);
        // ERROR SCENARIO 1 and 2: store/owner null
        if (store == null || owner == null) {
            debugStoreAdditionInitData(store, owner);
        }
        StopWatch.stop(true);
    }

    /**
     * Log that a store addition was attempted with a null reference specified
     * as the owner.
     *
     * @param store - HashCode used for ID in message. Possibly null if
     * previously added as null.
     * @param owner - Simple class name and Hashcode used for ID in message.
     * However, should be null as that is the error. The only way this is not
     * null is if the reference is changed from a different thread while this
     * error is encountered.
     */
    private void logErrorStoreWithNullOwnerAdded(ProvenanceStore store, Object owner) {
        StopWatch.start(true);
        // shouldn't happen (store exists, prev owner null)
        String storeHashKey = "null";
        String ownerClassNameAndKey = "null";
        if (store != null) {
            storeHashKey = String.valueOf(store.hashCode());
        }
        if (owner != null) {
            ownerClassNameAndKey = owner.getClass().getSimpleName() + "_" + owner.hashCode();
        }
        IO.error("Tried to add store with null owner\n\tstore:" + storeHashKey
                + "\n\towner:" + ownerClassNameAndKey
                + "\nCONCURRENT ERROR DETECTED!!! Owner has already been "
                + "checked for null reference");
        StopWatch.stop(true);
    }

    /**
     * FULL BRANCH COVERAGE WITH STATE LOGGING
     *
     * Adds a provenance store. The store directory (keyed by owner) and owner
     * directory (keyed by store), are automatically updated.
     *
     * NOTE: If the store has already been added, it may be assigned a new
     * owner, both internally and within the store manager directories. This
     * occurs when the store is found, but its indexed owner is different than
     * the one found in the directory. The owner of the store is meant to be a
     * lookup device.
     *
     * @param store - The provenance store to add to the manager's directories
     * @param owner - Object that currently maintains the store to be added
     * (default key for the new store owner in the provStores map and
     * storeOwners map)
     */
    public void debugAddProvenanceStore(ProvenanceStore store, Object owner) {
        StopWatch.start(true);
        logInitErrorsAddProvenanceStore(store, owner);
        // do not add a null provenance store or one with a null owner

        if (store != null && owner != null) {
            // USE THIS TO TEST FOR MYSTERIOUSLY VANISHING STORES
            //debugStoreAdditionInitData(store, owner);

            // only add the store if provenance collection is turned on
            if (!store.getCollectorClass()
                    .equals(NoProvenanceCollectionCollector.class
                    )) {
                // sync on all maps since more than one is modified
                synchronized (MAPS_LOCK) {
                    /* determine scenario */
                    // find store
                    boolean foundStoreInStoreOwnersMap = storeOwners.containsKey(store);
                    boolean foundStoreInOwnersStoresMap = ownersStores.containsValue(store);
                    // find owner
                    boolean foundOwnerInStoreOwnersMap = storeOwners.containsValue(owner);
                    boolean foundOwnerInOwnersStoresMap = ownersStores.containsKey(owner);

                    if (!foundStoreInStoreOwnersMap && !foundStoreInOwnersStoresMap
                            && !foundOwnerInStoreOwnersMap
                            && !foundOwnerInOwnersStoresMap) { // -
                        /* completely fresh add */
                        uncheckedAddProvenanceStore(store, owner);

                    } else if (foundStoreInStoreOwnersMap && !foundStoreInOwnersStoresMap
                            && !foundOwnerInStoreOwnersMap
                            && !foundOwnerInOwnersStoresMap) { // 1
                        /* presumably a previous removal error (not found in owners map) */
                        // owner wasn't found at all, and the store was only 
                        // found in the store->owner map meaning the store was 
                        // never added to owners map or was removed from one and not the other
                        IO.log("SHARED STORE?!? Error detected during add:"
                                + " Store found in store-to-owner mapping, but"
                                + " not owner-to-store mapping!");
                        debugStoreAdditionInitData(store, owner); // display data for debugging
                        // overwrite any previous entries
                        uncheckedAddProvenanceStore(store, owner);

                    } else if (!foundStoreInStoreOwnersMap && foundStoreInOwnersStoresMap
                            && !foundOwnerInStoreOwnersMap
                            && !foundOwnerInOwnersStoresMap) { // 2
                        /* presumably a previous removal error (not found in store map) */
                        IO.log("ORPHANED STORE?!? Error detected during add: "
                                + "Store found in owner-to-store map, but not "
                                + "store-to-owner map!");
                        debugStoreAdditionInitData(store, owner); // display data for debugging
                        // remove matching entries from ownersStores map (release all orphaned owners)                        
                        removeEntriesByValue(ownersStores, store);
                        // fresh add store to maps
                        uncheckedAddProvenanceStore(store, owner);

                    } else if (!foundStoreInStoreOwnersMap && !foundStoreInOwnersStoresMap
                            && foundOwnerInStoreOwnersMap
                            && !foundOwnerInOwnersStoresMap) { // 3 
                        /* presumably a previous removal error (owner found in storeOwners map only) */
                        IO.log("Error detected during add: Owner found in "
                                + "store-to-owner map but not owner-to-store map!");
                        debugStoreAdditionInitData(store, owner); // display data for debugging
                        // remove matching entries from storeOwners map (release all orphaned owners)
                        removeEntriesByValue(storeOwners, owner);
                        // fresh add store to maps
                        uncheckedAddProvenanceStore(store, owner);

                    } else if (!foundStoreInStoreOwnersMap && !foundStoreInOwnersStoresMap
                            && !foundOwnerInStoreOwnersMap
                            && foundOwnerInOwnersStoresMap) { // 4
                        /* presumably a previous removal error (owner found in ownersStores map only) */
                        // POSSIBLE DUPLICATE OWNER -- owner owns more than one store
                        IO.log("SHARED OWNER?!? Possible error detected during"
                                + " add: Owner found in owner-to-store map but"
                                + " not store-to-owner map!");
                        debugStoreAdditionInitData(store, owner); // display data for debugging
                        uncheckedAddProvenanceStore(store, owner); // just overwrite the mapping while adding the other

                    } else if (foundStoreInStoreOwnersMap && foundStoreInOwnersStoresMap
                            && !foundOwnerInStoreOwnersMap
                            && !foundOwnerInOwnersStoresMap) { // 1 2
                        /* new owner for existing store */
                        // remove corresponding entries in both maps
                        storeOwners.remove(store);
                        removeEntriesByValue(ownersStores, store);
                        // fresh add to store maps
                        uncheckedAddProvenanceStore(store, owner);

                        // !!!SHOULD PROBABLY LOG ERROR IF BOTH MAPPINGS DONT MATCH
                    } else if (foundStoreInStoreOwnersMap && !foundStoreInOwnersStoresMap
                            && foundOwnerInStoreOwnersMap
                            && !foundOwnerInOwnersStoresMap) { // 1 3 
                        /* store and owner found in store-to-owner map */
                        IO.log("Possible previous removal error detected during"
                                + " add: Store and Owner found in Store-to-owner"
                                + " map but not owner-to-store map");
                        if (Objects.equals(storeOwners.get(store), owner)) {
                            IO.log("\tFound in single mapping");
                        } else {
                            IO.log("\tFound in different mappings!");
                        }
                        debugStoreAdditionInitData(store, owner); // display data for debugging
                        // both are found in one map, but not necessarily together
                        storeOwners.remove(store); // remove individually
                        removeEntriesByValue(storeOwners, owner); // remove individually
                        // fresh add to store maps
                        uncheckedAddProvenanceStore(store, owner);

                    } else if (foundStoreInStoreOwnersMap && !foundStoreInOwnersStoresMap
                            && !foundOwnerInStoreOwnersMap
                            && foundOwnerInOwnersStoresMap) { // 1 4 
                        // owner found in owner-to-store map only
                        // store found in store-to-owner map only
                        IO.log("Possible error detected during add: Store found"
                                + " in store-to-owner mapping only. Owner found"
                                + " in owner-to-store mapping only");
                        debugStoreAdditionInitData(store, owner);
                        // delete previous owner entry
                        ownersStores.remove(owner);
                        // delete previous store entry
                        storeOwners.remove(store);
                        // fresh add to store maps
                        uncheckedAddProvenanceStore(store, owner);

                    } else if (!foundStoreInStoreOwnersMap && foundStoreInOwnersStoresMap
                            && foundOwnerInStoreOwnersMap
                            && !foundOwnerInOwnersStoresMap) { // 2 3
                        // store found in owner-to-store map
                        // owner found in store-to-owner map
                        IO.log("Possible error detected during add: Store found"
                                + " in owner-to-store mapping only. Owner found "
                                + "in store-to-owner mapping only");
                        debugStoreAdditionInitData(store, owner);
                        // delete previous owner entry
                        removeEntriesByValue(ownersStores, store);
                        // delete previous store entry
                        removeEntriesByValue(storeOwners, owner);
                        // fresh add to store maps
                        uncheckedAddProvenanceStore(store, owner);

                    } else if (!foundStoreInStoreOwnersMap && foundStoreInOwnersStoresMap
                            && !foundOwnerInStoreOwnersMap
                            && foundOwnerInOwnersStoresMap) { // 2 4 
                        /* store and owner found in owner-to-store map */
                        IO.log("Possible previous removal error detected during "
                                + "add: Store and Owner found in owner-to-store "
                                + "map but not store-to-owner map");
                        if (Objects.equals(ownersStores.get(owner), store)) {
                            IO.log("\tFound in single mapping");
                        } else {
                            IO.log("\tFound in different mappings!");
                        }
                        debugStoreAdditionInitData(store, owner); // display data for debugging
                        // both are found in one map, but not necessarily together
                        removeEntriesByValue(ownersStores, store); // remove individually
                        ownersStores.remove(owner); // remove individually
                        // fresh add to store maps
                        uncheckedAddProvenanceStore(store, owner);

                    } else if (!foundStoreInStoreOwnersMap && !foundStoreInOwnersStoresMap
                            && foundOwnerInStoreOwnersMap
                            && foundOwnerInOwnersStoresMap) { // 3 4 
                        /* new store for existing owner */
                        // remove corresponding entries in both maps
                        removeEntriesByValue(storeOwners, owner);
                        ownersStores.remove(owner);
                        // fresh add to store maps
                        uncheckedAddProvenanceStore(store, owner);
                        // !!!SHOULD PROBABLY LOG ERROR IF BOTH MAPPINGS DONT MATCH
                    } else if (foundStoreInStoreOwnersMap && foundStoreInOwnersStoresMap
                            && foundOwnerInStoreOwnersMap
                            && !foundOwnerInOwnersStoresMap) { // 1 2 3 
                        /* entry almost complete, missing owner in owner-to-store map */
                        IO.log("Possible error detected during add. Store and "
                                + "owner mapped in store-to-owner map, but owner"
                                + " not mapped in owner-to-store map");
                        // remove 1
                        storeOwners.remove(store);
                        // remove 2
                        removeEntriesByValue(ownersStores, store);
                        // remove 3
                        ownersStores.remove(owner);
                        // fresh add to store maps
                        uncheckedAddProvenanceStore(store, owner);
                        // !!!SHOULD PROBABLY LOG ERROR IF BOTH MAPPINGS DONT MATCH
                    } else if (foundStoreInStoreOwnersMap && foundStoreInOwnersStoresMap
                            && !foundOwnerInStoreOwnersMap
                            && foundOwnerInOwnersStoresMap) { // 1 2 4 
                        /* entry almost complete, missing owner in owner-to-store map */
                        IO.log("Possible error detected during add. Store and "
                                + "owner mapped in store-to-owner map, but owner"
                                + " not mapped in owner-to-store map");
                        // remove 1
                        storeOwners.remove(store);
                        // remove 2
                        removeEntriesByValue(ownersStores, store);
                        // remove 4
                        removeEntriesByValue(storeOwners, owner);
                        // fresh add to store maps
                        uncheckedAddProvenanceStore(store, owner);
                        // !!!SHOULD PROBABLY LOG ERROR IF BOTH MAPPINGS DONT MATCH                        
                    } else if (foundStoreInStoreOwnersMap && !foundStoreInOwnersStoresMap
                            && foundOwnerInStoreOwnersMap
                            && foundOwnerInOwnersStoresMap) { // 1 3 4 
                        /* entry almost complete, missing owner in owner-to-store map */
                        IO.log("Possible error detected during add. Store and "
                                + "owner mapped in store-to-owner map, but owner"
                                + " not mapped in owner-to-store map");
                        // remove 1
                        storeOwners.remove(store);
                        // remove 3
                        ownersStores.remove(owner);
                        // remove 4
                        removeEntriesByValue(storeOwners, owner);
                        // fresh add to store maps
                        uncheckedAddProvenanceStore(store, owner);
                        // !!!SHOULD PROBABLY LOG ERROR IF BOTH MAPPINGS DONT MATCH
                    } else if (!foundStoreInStoreOwnersMap && foundStoreInOwnersStoresMap
                            && foundOwnerInStoreOwnersMap
                            && foundOwnerInOwnersStoresMap) { // 2 3 4 
                        /* entry almost complete, missing owner in owner-to-store map */
                        IO.log("Possible error detected during add. Store and "
                                + "owner mapped in store-to-owner map, but owner"
                                + " not mapped in owner-to-store map");
                        // remove 2
                        removeEntriesByValue(ownersStores, store);
                        // remove 3
                        ownersStores.remove(owner);
                        // remove 4
                        removeEntriesByValue(storeOwners, owner);
                        // fresh add to store maps
                        uncheckedAddProvenanceStore(store, owner);
                        // !!!SHOULD PROBABLY LOG ERROR IF BOTH MAPPINGS DONT MATCH
                    } else { // 1 2 3 4
                        // duplicate addition
                        IO.log("duplication store addition detected in StoreManager.addProvenanceStore");
                        // not necessarily found in same entries
                        // remove 1
                        storeOwners.remove(store);
                        // remove 2
                        removeEntriesByValue(ownersStores, store);
                        // remove 3
                        ownersStores.remove(owner);
                        // remove 4
                        removeEntriesByValue(storeOwners, owner);
                        // fresh add to store maps
                        uncheckedAddProvenanceStore(store, owner);
                    }
                    // count it
                    synchronized (MASSProv.storesAdded) {
                        MASSProv.storesAdded++;
                    }
                }
            }
        }
        StopWatch.stop(true);
    }
    // </editor-fold>

    /**
     * Call this after provenance support is turned back on, since all the
     * activity influences are going to be outdated
     */
    public void reanchorAllStoreActivityInfluence() {
        synchronized (MAPS_LOCK) {
            ResourceMatcher matcher = ResourceMatcher.getMatcher();
            Thread thread = null;
            // reset all stacks in the resource matcher
            matcher.clear();
            for (ProvenanceStore store : storeOwners.keySet()) {
                thread = (Thread) storeOwners.get(store);
                if (ProvenanceRecorder.granularityLevel()
                        >= Granularity.PROCEDURE.getValue()) { // this will ensure a call-chain
                    matcher.pushActivityID(ProvUtils.getUniversalResourceID(
                            "callChainAnchor_for_" + thread.getName()));
                }
            }
            // this already happens in getStoreOfCurrentThread in the case that the requester gets a recycled store
            // and since the matcher has been emptied
//            for (ProvenanceStore store : recycledStores) {
//                thread = (Thread) storeOwners.get(store);
//                if (ProvenanceRecorder.granularityLevel()
//                        >= Granularity.PROCEDURE.getValue()) { // this will ensure a call-chain
//                    matcher.pushActivityID(ProvUtils.getUniversalResourceID(
//                            "callChainAnchor_for_" + thread.getName()));
//                }
//            }
        }
    }

    public void recycleAllStores() {
        synchronized (MAPS_LOCK) {
            Iterator iter = storeOwners.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry pair = (Map.Entry) iter.next();
                ((ProvenanceStore) pair.getKey()).setRegistered(false);
                iter.remove();
                ownersStores.remove(pair.getValue());
            }
        }
    }
}
