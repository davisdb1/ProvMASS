package edu.uw.bothell.css.dsl.MASS.prov;
// PERFORMANCE DOCUMENTED

import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.core.MASS;
import edu.uw.bothell.css.dsl.MASS.prov.filter.AgentFilter;
import edu.uw.bothell.css.dsl.MASS.prov.filter.PlaceFilter;
import edu.uw.bothell.css.dsl.MASS.prov.store.Granularity;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.store.StoreManager;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.NoProvenanceCollectionCollector;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.ProvenanceCollector;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.factory.CollectorFactory;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.factory.SimpleCollectorFactory;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.stationary.StationaryCollector;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 * Provides provenance support for MASS applications.
 *
 * @author Delmar B. Davis
 */
public class MASSProv {

    public static boolean provOn = false;
    private static final CollectorFactory COLLECTOR_FACTORY
            = SimpleCollectorFactory.getInstance();
    private static StoreManager storeManager;
    private static ProvenanceStore applicationProvenanceStore;
    private static boolean initialized = false;
    public static Integer storesAdded = 0;
    public static Integer storesRemoved = 0;
    private static StringBuffer driverRID = null;
    private static String[] argsRIDs = null;
    private static StringBuffer simulatorRID = null;
    private static StringBuffer userRID = null;
    private static StringBuffer argsRID = null;
    private static String[] driverArgs = null;
    private static boolean granularityConfigured = false;
    public static long endnanos = -1L;
    private static long preConnectionNanos = 0L;
    private static long initStart = 0L;
    private static long startTime = 0L;
    public static AgentFilter agentFilter = AgentFilter.ALL;
    public static Object agentFilterCriteria = -1;
    public static PlaceFilter placeFilter = PlaceFilter.ALL;
    public static Object placeFilterCriteria = -1;

    /**
     * Initializes MASS provenance collection.
     *
     * Note: The name of the class calling this method is used in the directory
     * name for the collected provenance. Directories are built based on a
     * thread stack trace. Provenance storage will not be configured properly if
     * this function is called from a class other than the entry point for the
     * MASS application.
     *
     * @param provenanceCollectorClass - The class responsible for generating
     * provenance on behalf of the store, for the MASS classes/methods.
     * @param callingMethod - Name of method used to invoke MASS.init
     * @param args - Arguments specified for the method that invoked this method
     */
    public static void init(Class provenanceCollectorClass, String callingMethod,
            String[] args) {
        StopWatch.start(true);
        provOn = true;
        initStoreManager();
        StoreManager.setProvenanceCollectionClass(
                provenanceCollectorClass == null ? StationaryCollector.class
                        : provenanceCollectorClass);
        if (!storeManager.isBufferAllocated()) {
            StoreManager.initBufferSpace(); // default allocation size
        }
        initialized = true;
        driverRID = MASSProv.documentSimulator(callingMethod, args);
        StopWatch.stop(true);
    }

    public static void init() {
        init(StationaryCollector.class, "main", driverArgs);
    }

    private static void initStoreManager() {
        StopWatch.start(true);
        if (storeManager == null) {
            storeManager = StoreManager.getStoreManager();
        }
        StopWatch.stop(true);
    }

    /**
     * Initializes MASS provenance collection.
     *
     * Note: The simple name of the appClass will be used in building the path
     * to persistent provenance.
     *
     * @param appClass - The class that initialized MASS
     * @param provenanceCollectorClass - The class responsible for generating
     * provenance on behalf of the store, for the MASS classes/methods.
     * @param callingMethod - Name of method used to invoke MASS.init
     * @param args - Arguments specified for the method that invoked this method
     */
//    public static void init(Class appClass, Class provenanceCollectorClass,
//            String callingMethod, String[] args) {
//        StopWatch.start(true);
//        initStoreManager();
//        StoreManager.setProvenanceCollectionClass(provenanceCollectorClass);
//        if (!storeManager.isBufferAllocated()) {
//            StoreManager.initBufferSpace(); // default allocation size
//        }
//        driverRID = MASSProv.documentSimulator(callingMethod, args);
//        initialized = true;
//        StopWatch.stop(true);
//    }
    /**
     * Initializes MASS provenance collection.
     *
     * Note: appClass will be used in building the path to persistent
     * provenance.
     *
     * @param appClass - The name of the class that initialized MASS
     * @param provenanceCollectorClass - The class responsible for generating
     * provenance on behalf of the store, for the MASS classes/methods.
     * @param callingMethod - The name of the method calling this
     * @param args - Arguments specified for the method that invoked this method
     */
//    public static void init(String appClass, Class provenanceCollectorClass,
//            String callingMethod, String[] args) {
//        StopWatch.start(true);
//        initStoreManager();
//        StoreManager.setProvenanceCollectionClass(provenanceCollectorClass);
//        if (!storeManager.isBufferAllocated()) {
//            StoreManager.initBufferSpace(); // default allocation size
//        }
//        driverRID = MASSProv.documentSimulator(callingMethod, args);
//        initialized = true;
//        StopWatch.stop(true);
//    }
    /**
     * Provenance collection is completed. MASS application main method
     * provenance collection is completed, unprocessed provenance is persisted
     * to disk, and persisted raw provenance is post-processed into well-formed
     * provenance that can be queried.
     *
     * @param callingMethod - Name of method calling this method
     * @param appArgs - Arguments of method calling this method
     * @param entities - entities used in main method of MASS application, in
     * the form: entity name 1, value 1, ..., entity name N, value N
     */
    public static void finish(String callingMethod, Object[] appArgs,
            Object... entities) {
        StopWatch.start(true);
        //finishApplicationProvenanceCollection(callingMethod, appArgs, entities);
        if (MASSProv.provOn) {
            ProvenanceStore store = ProvUtils.getStoreOfCurrentThread();
            store.addRelationalProv(driverRID,
                    ProvOntology.getEndedAtTimeStartingPointPropertyFullURIBuffer(),
                    new StringBuffer(String.valueOf(System.nanoTime())));
        }
        finish();
        StopWatch.stop(true);
    }

    /**
     * Completes provenance capture by persisting and translating remaining
     * stored provenanceS
     */
    public static void finish() {
        StopWatch.start(true);
        if (MASSProv.isInitialized()) {
            IO.log("MASSProv is finishing on " + ProvUtils.getHostName());
            persistRemainingRawDataProvenance();
            if (StoreManager.postProcess) {
                postProcessRawDataProvenance();
            }
            if (StoreManager.getLastThreadWithoutRegisteredStore() != null) {
                IO.log("Tuning information: The highest thread not given a "
                        + "provenance store was "
                        + StoreManager.getLastThreadWithoutRegisteredStore());
            }
            //archiveRawDataProvenance();
            endnanos = System.nanoTime();
            storeManager.deletePreProcessedProvenanceDirectorysMASSProvParent();
        }
        StopWatch.stop(true);
        StopWatch.reportPerformanceOverheadEvaluation();
    }

    private static void archiveRawDataProvenance() {
        storeManager.amalgamateAndCopyPreprocessedProvenance("/tmp/MASSProvData/"
                + ProvUtils.getHostName() + "_allPreProcProv.txt",
                ProvUtils.getHostName() + "_allPreProcProv.txt");
    }

    /**
     * All provenance referenced by the local store manager is persisted to disk
     * in preparation for post-processing.
     */
    public static void persistRemainingRawDataProvenance() {
        StopWatch.start(true);
        storeManager.persistAllStores();
        StopWatch.stop(true);
    }

    public static void postProcessRawDataProvenance() {
        StopWatch.start(true);
        storeManager.postProcessPersistedStores();
        StopWatch.stop(true);
    }

    /**
     * Sets up provenance collection and collects provenance for the application
     * that initialized MASSProv.
     *
     * @param appArgs - Arguments specified for the method that invoked
     * MASS.init
     */
    private static void beginApplicationProvenanceCollection(String methodName,
            Object[] appArgs) {
        StopWatch.start(true);
        // get the main MASS-application class (e.g. topPackage.packageN.Wave2D)
        String appClass = StoreManager.getCanonicalAppClass();
        // default application instance, for prov store parent constructor arg
        Object applicationObject = new Object();
        // default collector 
        // (this way, no ClassNotFoundException thrown during collection)
        ProvenanceCollector applicationProvenanceCollector
                = new NoProvenanceCollectionCollector();
        // get application provenance collection instances
        // overrides system-wide collector for the MASSProv store
        try {
            String provcollectionPackagedAppClass = ProvUtils.
                    translateToProvcollectionPackage(appClass);
            // name of the collector for the entry-point class (class w/main())
            String collectorClassName
                    = new StringBuilder(provcollectionPackagedAppClass).
                            append("ProvenanceCollector").
                            toString();
            // get a collector
            applicationProvenanceCollector = COLLECTOR_FACTORY.getInstance(
                    collectorClassName,
                    (Object) SimpleCollectorFactory.noParams);
            // get an application for store indexing
            applicationObject = COLLECTOR_FACTORY.getInstance(appClass,
                    (Object) null);
        } catch (Exception e) {
            e.printStackTrace(IO.getLogWriter());
        }
        // get a store for the application 
        applicationProvenanceStore = new ProvenanceStore(applicationObject,
                true);
        IO.log("Overriding the MASSProv system-wide collector \""
                + storeManager.getProvenanceCollectionClass().getSimpleName()
                + "\" with the application provenance collector \""
                + applicationProvenanceCollector.getClass().getSimpleName()
                + "\"");
        // override the MASS provenance collector with the app prov collector
        applicationProvenanceStore.setCollector(
                applicationProvenanceCollector);
        // begin collection
        applicationProvenanceStore.beginCollection(appClass, methodName, appArgs);
        StopWatch.stop(true);
    }

    /**
     * Collects retrospective provenance for the method that invoked MASS.init
     *
     * @param methodName - Name of the method that invoked MASS.init
     * @param appArgs - Arguments passed into the method that invoked MASS.init
     * @param entities - entities used in method application that initialized
     * MASSProv, in the form: entity name 1, value 1, ..., entity name N, value
     * N
     */
    public static void finishApplicationProvenanceCollection(String methodName,
            Object[] appArgs, Object... entities) {
        StopWatch.start(true);
//        if (provOn && StoreManager.getGranularityLevel().getValue()
//                >= Granularity.SIMULATION.getValue()) {
//            // get the main MASS-application class (e.g. topPackage.packageN.Wave2D)
//            String appClass = storeManager.getCanonicalAppClass();
//            applicationProvenanceStore.endCollection(appClass, methodName,
//                    ProvUtils.getEntityMap(entities), appArgs);
//        }
        StopWatch.stop(true);
    }

    /**
     * Initializes provenance capture configuration on remote hosts through
     * MASS.initMASSProv.
     */
    public static void initRemoteHosts() {
        StopWatch.start(true);
        MASS.initMASSProv();
        StopWatch.stop(true);
    }

    /**
     * Indicates whether or not this node has been initialized.
     *
     * @return True if this node has been initialized using any of the init
     * methods, otherwise false
     */
    public static boolean isInitialized() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return initialized;
    }

    /**
     * Assigns the maximum amount of provenance stores that the system should
     * support for the duration of application execution. This value supports
     * fault tolerance for provenance buffer assignment to newly instantiated
     * provenance stores.
     *
     * @param size - Maximum amount of provenance stores that the provenance
     * manager should reference at any given time.
     */
    public static void setMaxProvenanceStores(int size) {
        StopWatch.start(true);
        StoreManager.setMaxProvStoreSize(size);
        StopWatch.stop(true);
    }

    /**
     * Assigns the amount of buffers that should be referenced at one time by
     * each provenance store
     *
     * @param size The amount of buffers that should be referenced at a time by
     * a given provenance store
     */
    public static void setBuffersPerStore(int size) {
        StopWatch.start(true);
        StoreManager.setBuffersPerStore(size);
        StopWatch.stop(true);
    }

    /**
     * Sets the number of lines (single StringBuffers) that should be maintained
     * within each buffer (single array of buffers alloted to a store).
     *
     * @param lines Amount of lines of character that should be maintained
     * within each provenance buffer
     */
    public static void setLinesPerBuffer(int lines) {
        StopWatch.start(true);
        StoreManager.setLinesPerBuffer(lines);
        StopWatch.stop(true);
    }

    public static void setCharsPerLine(int i) {
        StopWatch.start(true);
        StoreManager.setCharsPerLine(i);
        StopWatch.stop(true);
    }

    public static void setWorkingDir(String workingDir) {
        StopWatch.start(true);
        StoreManager.setWorkingDir(workingDir);
        StopWatch.stop(true);
    }

    public static void setGranularity(Granularity level, boolean print) {
        StopWatch.start(true);
        if (level != null) {
            if (print) {
                String msg = "Provenance granularity set from "
                        + StoreManager.getGranularityLevel().toString()
                        + " to " + level.toString();
                System.err.println(msg);
                IO.log(msg);
            }
            StoreManager.setGranularityLevel(level);
            granularityConfigured = true;
        }
        StopWatch.stop(true);
    }

    /**
     * Allows remote hosts to indicate that MASSProv has been initialized on the
     * master, on subsequently initialized locally
     */
    public static void remoteInitialized() {
        StopWatch.start(true);
        StopWatch.stop(true);
        initialized = true;

    }

    public static StringBuffer documentSimulator(String callingMethod, String[] args) {
        StopWatch.start(true);
        StringBuffer procRID = null;
        if (MASSProv.provOn && initialized && ProvenanceRecorder.granularityLevel()
                >= Granularity.PROCESS.getValue()) {
            // get the store for the main thread (that was added during MASSProv.initLocal)
            ProvenanceStore store = ProvUtils.getStoreOfCurrentThread();
            // get ID for simulator process as a softare agent as SimClass_uuid
            simulatorRID = ProvUtils.getUniversalResourceID(StoreManager.getAppNameBuffer());
            // document said Agent as SimClass_uuid a prov:SoftwareAgent
            ProvenanceRecorder.documentAgent(store, null, new StringBuffer(simulatorRID),
                    StoreManager.getAppNameBuffer(), new StringBuffer("SIMULATOR"));
            // user based on system login: loginName a prov:Agent
            userRID = new StringBuffer(ProvenanceRecorder.documentUser(store));
            // document arguments as a collection: args_uuid a prov:collection
            argsRID = ProvUtils.getUniversalResourceID(new StringBuffer("args"));
            // resource ID of each argument ends up in this array
            argsRIDs = ProvenanceRecorder.documentCommandLineArguments(
                    store, new StringBuffer(simulatorRID), args, new StringBuffer(argsRID));
            // document the calling function as simulation entry point
            if (callingMethod == null) {
                callingMethod = "main";
            }
            procRID = ProvenanceRecorder.documentSimDriver(store, simulatorRID,
                    null, new StringBuffer(callingMethod), new StringBuffer("SIMULATION_DRIVER"));
            // connect the software to the user: main_uuid prov:actedOnBehalfOf loginName
            store.addRelationalProv(simulatorRID,
                    ProvOntology.getActedOnBehalfOfStartingPointPropertyFullURIBuffer(),
                    userRID);
        }
        StopWatch.stop(true);
        return procRID;
    }

    public static StringBuffer getDriverRID() {
        return driverRID;
    }

    public static String[] getArgsRIDs() {
        return argsRIDs;
    }

    public static StringBuffer getSimulatorRID() {
        return simulatorRID;
    }

    public static StringBuffer getUserRID() {
        return userRID;
    }

    public static void configure(String[] args) {
        if (initStart <= 0) {
            MASSProv.setProcessStartTime();
        }
        boolean print = false; // print set to false for evaluation
        driverArgs = args;
        MASSProv.provOn = ProvUtils.provSetInArgs(args, print);
        setGranularity(ProvUtils.granularityFromArgs(args), print);
        MASSProv.setPostProcess(ProvUtils.postProcessingFromArgs(args, false));
        MASSProv.setMaxProvenanceStores(ProvUtils.maxStoresFromArgs(args));
        MASSProv.setBuffersPerStore(ProvUtils.buffersPerStoreFromArgs(args));
        MASSProv.setLinesPerBuffer(ProvUtils.linesPerBufferFromArgs(args));
        MASSProv.setCharsPerLine(ProvUtils.charsPerLineFromArgs(args));
    }

    public static boolean granularityConfigured() {
        return granularityConfigured;
    }

    public static boolean shouldPostProcess() {
        return StoreManager.postProcess;
    }

    public static void setPostProcess(boolean postProcess) {
        StoreManager.postProcess = postProcess;
    }

    public static void setStartTime() {
        startTime = System.nanoTime() - preConnectionNanos; // currentTime - total amount of time before connecting = all setup - connection time
    }

    public static long getStartTime() {
        return startTime;
    }

    static void setProcessStartTime() {
        initStart = System.nanoTime();
    }

    static long getInitialStartTime() {
        return initStart;
    }

    public static void setInitNanos() {
        preConnectionNanos = System.nanoTime() - initStart; // total amount of time so far
    }

    static long getEndTime() {
        return endnanos;
    }
}
