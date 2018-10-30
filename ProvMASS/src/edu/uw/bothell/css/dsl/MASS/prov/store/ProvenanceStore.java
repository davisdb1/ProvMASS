package edu.uw.bothell.css.dsl.MASS.prov.store;
// PERFORMANCE DOCUMENTED

import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.ProvOntology;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.NoProvenanceCollectionCollector;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.ProvenanceCollector;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.factory.CollectorFactory;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.factory.SimpleCollectorFactory;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Stores and manages provenance data. Data is stored in memory, and persisted
 * to disk, based on the ProvStaging model.
 *
 * @author Delmar B. Davis
 */
public class ProvenanceStore implements Serializable {

    private static boolean loggedHugeSequenceLengthError = false;

    public HashSet<String> getHostsThatOwnerWasPersistentlyIdentifiedAt() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return hostsThatOwnerWasPersistentlyIdentifiedAt;
    }

    // <editor-fold defaultstate="collapsed" desc="Substructures">
    public enum StorageStatus {

        /**
         * The provenance data was added to the store without issue
         */
        STORED,
        /**
         * The provenance data was added to the store as the last possible index
         * before persistence is required
         */
        FINAL,
        /**
         * The provenance data length exceeded the maximum value of a line of
         * provenance
         */
        OVERFLOW,
        /**
         * The provenance store was full. As a result, the provenance was not
         * stored
         */
        FULL,
        /**
         * The provenance data was persisted
         */
        PERSISTED,
        /**
         * Persistence was attempted and failed
         */
        PERSIST_FAILED,
        /**
         * An unidentified error was encountered. As a result, the provenance
         * was not stored
         */
        ERROR,
        /**
         * Out of Memory
         */
        OOM,
        /**
         * Store was never registered with the Store Manager, therefore no
         * storage occurred, as no buffers were provided to the store
         */
        NOT_REGISTERED
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Members">
    /* Provenance Data */
    private List<String> prov; // old version of provenance data (legacy... unlimited data until OutOfMemoryException)
    private StringBuffer[][] provBufs; // current version of provenance data
    /* Persistence */
    private BufferedWriter writer;
    /* IDs */
    private final String storeUUID; // store ID (doesn't change)
    private String originalOwnerUUID; // to differentiate stores sharing owner
    private Integer ownerLocalHashCode; // for mgr store comparison and lookup
    private String ownerClassName; // part of filename to easily identify owner
    /* Logical Cohesion for Distributed Triples */
    private HashSet<String> hostsThatOwnerWasPersistentlyIdentifiedAt;
    /* Switches */
    private boolean autoPersist; // persist and clear data after
    /* Configuration */
    private ProvenanceCollector collector; // collector to invoke pre- and post-method-execution provenance collection with
    private final CollectorFactory collectorFactory = SimpleCollectorFactory.getInstance(); // factory used to retrieved instance of specified collector
    private long size = 0; // size of provenance contained in this store (in bytes)
    private boolean registered = false; // successfully registered with StoreManager (received buffers for writing to)
    /* Buffer Info */
    private int bufferPositionInManager = -1; // starting index of buffers allotted to this store (within all buffers managed by StoreManager)private int currentBuffer = 0;
    private int currentBufferIdx = 0;
    private int currentLineIdx = 0;
    private int currentCharIdx = 0;
    /* Exception Tracking */
    private static boolean appendExceptionLogged = false;
    /* Logger/Debugging */
    private int highestSequenceLengthEncountered = 0;
    public boolean debug = false;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Responsible for allocating a ProvenanceStore that may automatically
     * persist preprocessed provenance data and initializing members.
     *
     * @param parent - The object that created this store
     * @param autoPersist - Indicates whether of not preprocessed provenance
     * should be automatically persisted when in-memory store is full.
     */
    public ProvenanceStore(Object parent, boolean autoPersist) {
        StopWatch.start(true);
        storeUUID = java.util.UUID.randomUUID().toString();
        init(parent);
        this.autoPersist = autoPersist;
        StopWatch.stop(true);
    }

    /**
     * Responsible for allocating a ProvenanceStore and initializing members.
     *
     * Note: Automatic persistence is turned on by default. -------------------
     * Note: If the parent creates another store, the store will lose track of
     * the parent object and be assigned a new empty parent for indexing by the
     * store manager. Be aware that the parent declared by this store can only
     * be relied upon when a single store is used for all provenance collection
     * on the parent object.
     *
     * @param parent - The object that created this store
     */
    public ProvenanceStore(Object parent) {
        this(parent, true);
        StopWatch.start(true);
        StopWatch.stop(true);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Initializers">
    /**
     * Initializes this store with field default values, an empty provenance
     * list, and a parent.
     *
     * Note: If the parent creates another store, the store will lose track of
     * the parent object and be assigned a new empty parent for indexing by the
     * store manager. Be aware that the parent declared by this store can only
     * be relied upon when a single store is used for all provenance collection
     * on the parent object.
     *
     * @param parent - The object that created this store
     */
    public final void init(Object parent) {
        StopWatch.start(true);
        setParent(parent);
        prov = new ArrayList<>();
        hostsThatOwnerWasPersistentlyIdentifiedAt = new HashSet<>();
        this.autoPersist = true;
        StoreManager sm = StoreManager.getStoreManager();
        if (IO.logFlag) {
            if (sm != null) {
                IO.log("Store manager was successfully retrieved in init(parent)"
                        + " called during store construction on <"
                        + ProvUtils.getHostName()
                        + ">");
            } else {
                IO.log("Store manager was not successfully retrieved in init(parent)"
                        + " called during store construction on <"
                        + ProvUtils.getHostName()
                        + ">");
            }
        }
        // instantiate the collector (type is set in main application class)
        try {
            Class collectorClass = null;
            if (sm != null) {
                collectorClass = sm.getProvenanceCollectionClass();
            }
            if (collectorClass == null) {
                if (IO.logFlag) {
                    IO.log("collectorClass was null in the store manager on "
                            + ProvUtils.getHostName());
                }
            }
            // override default with system-wide default
            setCollector(collectorClass);
            // set collector to system-wide default (for later use)
            if (collectorClass != null) {
                collector = collectorFactory.getInstance(
                        collectorClass.getCanonicalName(),
                        (Object) null);
            } else {
                collector = new NoProvenanceCollectionCollector();
            }
        } catch (Exception e) {
            if (IO.logFlag) {
                IO.log("An error occurred while attempting to set the provenance "
                        + "collector, subsequently preventing store registration "
                        + "with the manager...\n");
                e.printStackTrace(IO.getLogWriter());
                IO.getLogWriter().flush();
                IO.getLogWriter().close();
            }
            // make sure that a non-null provenance collector is set
            collector = new NoProvenanceCollectionCollector();
        }
        if (sm != null) {
            // add this store to the manager
            registerWithStoreManager(parent);
        } else if (IO.logFlag) {
            IO.log("sm was null in provstore.init(parent) on "
                    + ProvUtils.getHostName());
        }
        StopWatch.stop(true);
    }

    protected void setParent(Object parent) {
        ownerClassName = parent.getClass().getSimpleName();
        originalOwnerUUID = ProvUtils.getLocationAwareURID(parent.getClass().getSimpleName());
        ownerLocalHashCode = parent.hashCode(); // for mgr lookup
    }

    void registerWithStoreManager(Object owner) {
        StopWatch.start(true);
        StoreManager sm = StoreManager.getStoreManager();
        // 1st step of registration: map store/owner references bidirectionally
        registered = sm.addProvenanceStore(this, owner);
        if (registered) {
            // 2nd step of registration: retrieve buffer references for storage
            registered = (provBufs = sm.retrieveAssignedBuffers(this)) != null;
        }
        if (!registered) {
            //IO.log("Unable to register store for ");
        }
        StopWatch.stop(true);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Setters">
    public void setOwnerUUID(String ownerUUID) {
        StopWatch.start(true);
        originalOwnerUUID = ownerUUID;
        StopWatch.stop(true);
    }

    /**
     * Sets the index pointers for the provBuffers array. If any provided index
     * lies outside respective bounds of the provenance buffer, or any part of
     * the buffer is uninitialized, then the buffer index pointers are reset to
     * their initial values (prior to this method call).
     *
     * @param currentBufferIdx1 - The current index for the first dimension
     * (provBuffers[X])
     * @param currentLineIdx1 - The current index for the second dimension
     * (provBuffers[][X])
     * @param currentCharIdx1 - The current index for the first dimension within
     * the second dimension (provBuffers[][].charAt(X))
     */
    void setBufferInfo(int currentBufferIdx1, int currentLineIdx1,
            int currentCharIdx1) {
        StopWatch.start(true);
        StringBuffer[] bufZero = null;
        int oldBufIdx = currentBufferIdx;
        int oldLineIdx = currentLineIdx;
        if (provBufs != null && currentBufferIdx1 >= 0
                && currentBufferIdx1 < provBufs.length) {
            this.currentBufferIdx = currentBufferIdx1;
            if (currentLineIdx1 >= 0 && provBufs.length > 0) {
                bufZero = provBufs[0];
                if (bufZero != null) {
                    if (currentLineIdx1 < bufZero.length) {
                        this.currentLineIdx = currentLineIdx1;
                        if (currentCharIdx1 >= 0
                                && bufZero.length > 0
                                && currentCharIdx1 < StoreManager.getCharsPerLine()) {
                            this.currentCharIdx = currentCharIdx1;
                        }
                    } else {
                        currentLineIdx = oldLineIdx;
                    }
                }
            } else {
                currentBufferIdx = oldBufIdx;
            }
        }
        StopWatch.stop(true);
    }

    private void setBufferInfo(int[] bufferInfo) {
        StopWatch.start(true);
        if (bufferInfo.length == 3) {
            setBufferInfo(bufferInfo[0], bufferInfo[1], bufferInfo[2]);
        }
        StopWatch.stop(true);
    }

    void setManagedBufferStartingIndex(int startingIndex) {
        StopWatch.start(true);
        bufferPositionInManager = startingIndex;
        StopWatch.stop(true);
    }

    /**
     * Toggles auto persistence.
     *
     * @return True if persistence was turned on. False if persistence was
     * turned off.
     */
    public boolean togglePersistence() {
        StopWatch.start(true);
        autoPersist = !autoPersist;
        StopWatch.stop(true);
        return autoPersist;
    }

    /**
     * Specifies identity of the object thats provenance is being stored. This
     * information is used as part of the filename when persisting the store to
     * disk.
     *
     * @param name - identifier for the object specifying the provenance in this
     * store
     */
    public void setStoreName(String name) {
        StopWatch.start(true);
        ownerClassName = name;
        StopWatch.stop(true);
    }

    /**
     * Sets the provenance collection class
     *
     * @param provenanceCollectionClass
     * @throws Exception
     */
    public void setCollector(Class provenanceCollectionClass) throws Exception {
        StopWatch.start(true);
        collector = collectorFactory.getInstance(
                provenanceCollectionClass.getCanonicalName(), (Object[]) null);
        StopWatch.stop(true);
    }

    /**
     * Sets the provenance collection class name
     *
     * @param provenanceCollectionClassName
     * @throws Exception
     */
    public void setCollector(String provenanceCollectionClassName) throws Exception {
        StopWatch.start(true);
        collector = collectorFactory.getInstance(provenanceCollectionClassName, (Object[]) null);
        StopWatch.stop(true);
    }

    /**
     * Sets the collector
     *
     * @param provenanceCollector Object responsible for calling pre/post
     * provenance collection methods corresponding to the executed method thats
     * provenance is being collected
     */
    public void setCollector(ProvenanceCollector provenanceCollector) {
        StopWatch.start(true);
        collector = provenanceCollector;
        StopWatch.stop(true);
    }

    protected void setOwner(Object newOwner) {
        StopWatch.start(true);
        ownerLocalHashCode = newOwner.hashCode();
        StopWatch.stop(true);
    }

    protected void setRegistered(boolean isRegistered) {
        registered = isRegistered;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * Provides the Length of the last, largest sequence of characters added to
     * the provenance buffer. By comparing this information with that of all
     * other managed stores the highest sequence length witnessed within all
     * stores, may be found. Using this information, it is possible to determine
     * the minimum amount of character space require in each line of all stores,
     * ensuring that no provenance will be lost.
     *
     * @return Length of the last, largest sequence of characters added to the
     * provenance buffer
     */
    public int getHighestSequenceLengthEncountered() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return highestSequenceLengthEncountered;
    }

    /**
     * Provides the identifier of this ProvenanceStore
     *
     * @return The identifier for this store (which is universally unique)
     */
    public String getUUID() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return storeUUID;
    }

    /**
     * Provides a copy of the buffer info
     *
     * @return (int[]){buffer#, line#, char#}
     */
    int[] getBufferInfoSnapshot() {
        StopWatch.start(true);
        int[] snapshot = {currentBufferIdx, currentLineIdx, currentCharIdx};
        StopWatch.stop(true);
        return snapshot;
    }

    public long getBytesSize() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return size;
    }

    public Class getCollectorClass() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return collector.getClass();
    }

    int getManagedBufferStartingIndex() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return bufferPositionInManager;
    }

    /**
     * Indicates whether or not this provenance store has been properly
     * registered with the store manager
     *
     * @return True if this store has been registered with the store manager and
     * thereafter, properly received its buffer space
     */
    public boolean isRegistered() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return registered;
    }

    public Integer getOwnerLocalHashCode() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return ownerLocalHashCode;
    }

    public String getOriginalOwnerUUID() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return originalOwnerUUID;
    }

    /**
     * Indicates whether the owner referenced by this store is the same object
     * referenced by comparisonObject.
     *
     * @param comparisonObject - Object compared to ProvenanceStore.owner for
     * reference equality
     * @return True if the owner referenced by this store is the same object
     * referenced by comparisonObject
     */
    public boolean ownerEquals(Object comparisonObject) {
        StopWatch.start(true);
        boolean same;
        if (comparisonObject == null && ownerLocalHashCode == null) {
            same = true;
        } else if (comparisonObject == null && ownerLocalHashCode != null) {
            same = false;
        } else if (ownerLocalHashCode == null) { // comparison != null at this point
            same = false;
        } else {
            same = ownerLocalHashCode.equals(comparisonObject.hashCode());
        }
        StopWatch.stop(true);
        return same;
    }

    StringBuffer[][] getBuffer() {
        StopWatch.start(true);
        StopWatch.stop(true);
        return provBufs;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Storage">
    /**
     * Identifies the SoftwareAgent using this store to record provenance. This
     * is required at least once per relational provenance model in order to
     * ensure that all triples that refer to the SoftwareAgent show up in the
     * model when queried or persisted. When a resource is not defined (i.e. the
     * resource identifier is not the subject of at least one triple), all
     * triples that rely on that definition do not show up in the persisted
     * provenance data.
     *
     * NOTE: This data is added one time per host
     *
     * @return definition for the owner entity
     */
    private String identifyOwner() {
        StopWatch.start(true);
        String ownerIdentification = null;
        if (!hostsThatOwnerWasPersistentlyIdentifiedAt.contains(ProvUtils.getHostName())) {
            ownerIdentification = ProvStaging.ProvOString(getOriginalOwnerUUID(),
                    ProvOntology.getRDFTypeFullURI(),
                    ProvOntology.getAgentStartingPointClassFullURI());
        }
        StopWatch.stop(true);
        return ownerIdentification;
    }

    /**
     * Adds a line of provenance data to the provenance buffer
     *
     * @param s provenance data to add
     * @return True if the operation succeeded, otherwise false
     */
    boolean addLineToBuffer(CharSequence s) throws Exception {
        StopWatch.start(true);
        StopWatch.stop(true);
        return addLineToBuffer(s, false);
    }

    /**
     * Adds a line of provenance data to the provenance buffer
     *
     * @param s provenance data to add
     * @return True if the operation succeeded, otherwise false
     */
    boolean addLineToBuffer(StringBuffer s) throws Exception {
        StopWatch.start(true);
        StopWatch.stop(true);
        return addLineToBuffer(s, false);
    }

    /**
     * Adds a line of data to the provenance buffer
     *
     * @param s provenance data to add
     * @param previousAttempt True if this is the second attempt to add the
     * provenance (1st attempt results in a reset and then persistence, clearly
     * the buffer)
     * @return True if the operation succeeded, otherwise false
     */
    boolean addLineToBuffer(CharSequence s, boolean previousAttempt) throws Exception {
        StopWatch.start(true);
        if (s == null) {
            s = "null";
        }
        if (debug) {
            IO.log("addLineToBuffer called with parameters s and previousAttempt");
            IO.log("s is...\n\n" + s + "\n\n");
            IO.log("previousAttempt is " + previousAttempt);
        }
        if (highestSequenceLengthEncountered < s.length()) {
            highestSequenceLengthEncountered = s.length();
            if (!loggedHugeSequenceLengthError && s.length() > StoreManager.getCharsPerLine()) {
                loggedHugeSequenceLengthError = true;
                String sCopy = "notCopied";
                try {
                    sCopy = s.toString();
                } catch (Exception e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                IO.log("Huge character sequence (length of s: " + s.length()
                        + ") addition attempted with value: \n\t");
                IO.log(sCopy);
            }
        }
        StringBuffer currentLine = null;
        StringBuffer nextLine = null;
        // take a snapshot of the current buffer info
        int[] bufferInfo = getBufferInfoSnapshot();
        if (debug) {
            IO.log("bufferInfo in addLineToBuffer is: [" + bufferInfo[0] + ", " + bufferInfo[1] + ", " + bufferInfo[2] + "]");
        }
        // success is false until operation is successful
        boolean success = false;
        if (debug) {
            IO.log("success in addRelationalProv starts off: " + success);
        }
        // add a line delimiter to the character sequence being added
        if (isRegistered()) {
            if (debug) {
                IO.log("ProvenanceStore is registered");
            }
            // first check that provenance data does not exceed the one line limit
            if (meetsCharacterLimit(s)) {
                if (debug) {
                    IO.log("s, with length " + s.length()
                            + ", in addLineToBuffer meetsCharacterLimit of "
                            + StoreManager.getCharsPerLine());
                }
                // get the current buffer line
                currentLine = provBufs[currentBufferIdx][currentLineIdx];
                if (debug) {
                    IO.log("reading currentLine from provBufs at indices: ["
                            + currentBufferIdx + "," + currentLineIdx + "]");
                    IO.log("currentLine in addLineToBuffer is: \n\n" + currentLine + "\n\n");
                }
                // sequence-length (+'\n') fits into remaining unused capacity of line buffer
                if (seqLengthFitsInCurrentLine(s, currentLine)) {
                    if (debug) {
                        IO.log("sequence length of s fits in currentLine");
                    }
                    int written = 0;
                    if (debug) {
                        IO.log("written in addLineToBuffer starts off at " + written);
                    }
                    try {
                        if (debug) {
                            IO.log("attempting to put all chars from the sequence into the current line");
                        }
                        // put all chars from the sequence into the current line
                        currentLine.append(s);
                        if (debug) {
                            IO.log("now currentLine is: " + currentLine);
                        }
                        written += s.length();
                        if (debug) {
                            IO.log("written in addLinetoBuffer has been extended to " + written);
                        }
                        currentLine.append('\n');
                        if (debug) {
                            IO.log("\\n has been added to currentLine, which is now...\n\n" + currentLine + "\n\n");
                        }
                        written += 1;
                        if (debug) {
                            IO.log("written in addLineToBuffer is now " + written);
                        }
                        success = true;
                        if (debug) {
                            IO.log("success in addLineToBuffer is " + success);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        if (debug) {
                            IO.log("an index was out of bounds in addLineToBuffer");
                        }
                        success = false;
                        if (debug) {
                            IO.log("success in addLineToBuffer is now " + success);
                        }
                        e.printStackTrace(IO.getLogWriter());
                        // erase characters written
                        currentLine.delete(currentCharIdx, currentCharIdx
                                + written + 1);
                        if (debug) {
                            IO.log("subsequence from " + currentCharIdx + " to "
                                    + currentCharIdx + written + 1
                                    + " in currentLine has been deleted");
                            IO.log("currentLine in addLineToBuffer is now: " + currentLine);
                        }
                    }
                    if (success) {
                        boolean spaceRemainingInLine = StoreManager.getCharsPerLine() - currentLine.length() > 0;
                        if (debug) {
                            IO.log("spaceRemainingInLine is assigned the value: " + spaceRemainingInLine);
                            IO.log("based on the operation: StoreManager.getCharsPerLine() - currentLine.length() > 0");
                            IO.log("where Storemanager.getCharsPerLine() is "
                                    + StoreManager.getCharsPerLine()
                                    + ", currentLine.length() is "
                                    + currentLine.length()
                                    + ", the difference of which is "
                                    + (StoreManager.getCharsPerLine()
                                    - currentLine.length())
                                    + ", and > 0??? "
                                    + ((StoreManager.getCharsPerLine()
                                    - currentLine.length()) > 0));
                        }
                        if (spaceRemainingInLine) {
                            if (debug) {
                                IO.log("there was space remaining in line");
                            }
                            // move the character position to the next available idx
                            currentCharIdx = currentLine.length();
                            if (debug) {
                                IO.log("currentCharIdx of ProvenanceStore has been set to " + currentCharIdx);
                            }
                        } else { // no room left... go to next line/buffer?
                            if (debug) {
                                IO.log("there was no space remaining in line");
                            }
                            // go to next line?
                            boolean nextLineAvailable = currentLineIdx + 1 < provBufs[currentBufferIdx].length;
                            if (debug) {
                                IO.log("currentLineIdx in addLineToBuffer is " + currentLineIdx);
                                IO.log("currentBufferIdx in addLineToBuffer is " + currentBufferIdx);
                                IO.log("provBufs[currentBufferIdx].length is " + provBufs[currentBufferIdx].length);
                                IO.log("nextLineAvailable assigned with nextLineAvailable = currentLineIdx + 1 < provBufs[currentBufferIdx].length");
                                IO.log("nextLineAvailable is " + nextLineAvailable);
                            }
                            if (nextLineAvailable) {
                                // move the buffer positions to the...
                                currentCharIdx = 0; // first character of the...
                                if (debug) {
                                    IO.log("currentCharIdx has been reset to " + currentCharIdx);
                                }
                                currentLineIdx++; // next line... in the current buffer
                                if (debug) {
                                    IO.log("currentLineIdx has been incremented to " + currentLineIdx);
                                }
                            } else {
                                // go to first line in next buffer?
                                boolean nextBufferAvailable = currentBufferIdx + 1 < provBufs.length;
                                if (debug) {
                                    IO.log("currentBufferIdx is " + currentBufferIdx);
                                    IO.log("provBufs.length is " + provBufs.length);
                                    IO.log("nextBufferAvailable assigned with currentBufferIdx + 1 < provBufs.length");
                                    IO.log("nextBufferAvailable is " + nextBufferAvailable);
                                }
                                if (nextBufferAvailable) { // move the buffer positions to...
                                    currentLineIdx = 0; // beginning...
                                    currentCharIdx = 0; // of...
                                    currentBufferIdx++; // next buffer
                                    if (debug) {
                                        IO.log("currentLineIdx has been reset to " + currentLineIdx);
                                        IO.log("currentCharIdx has been reset to " + currentCharIdx);
                                        IO.log("currentBufferIdx has been incremented to " + currentBufferIdx);
                                    }
                                } else if (!previousAttempt) { // no next buffer, so no line either
                                    persistBuffers(); // persist buffers and move pointers to 0,0,0
                                    if (debug) {
                                        IO.log("persistBuffers called");
                                        IO.log("bufferInfo in addLineToBuffer is: ["
                                                + getBufferInfoSnapshot()[0] + ", "
                                                + getBufferInfoSnapshot()[1] + ", "
                                                + getBufferInfoSnapshot()[2] + "]");
                                    }
                                }
                            }
                        }
                    }
                } else {// sequence length doesn't fit into a remainder of current line buffer
                    // make sure that accessing the line after the current line will not throw a NullPointException
                    boolean nextLineAvailable = currentLineIdx + 1 < provBufs[currentBufferIdx].length;
                    if (debug) {
                        IO.log("currentLineIdx is " + currentLineIdx);
                        IO.log("currentBufferIdx is " + currentBufferIdx);
                        IO.log("provBufs[currentBufferIdx].length is " + provBufs[currentBufferIdx].length);
                        IO.log("nextLineAvailable assigned with currentLineIdx + 1 < provBufs[currentBufferIdx].length");
                        IO.log("nextLineAvailable is " + nextLineAvailable);
                    }
                    if (nextLineAvailable) {
                        if (debug) {
                            IO.log("currentLine is...\n\n" + currentLine + "\n\n");
                            IO.log("currentCharIdx is " + currentCharIdx);
                            IO.log("s is...\n" + s + "\n");
                            IO.log("appendCharactersThatFit(currentLine, currentCharIdx, s) is being called");
                        }
                        // add beginning portion of string to remaining capacity of the current line buffer
                        int startIdxOfRemainingChars = appendCharactersThatFit(currentLine, s);
                        // increment line position and retrieve the next line
                        currentLineIdx++;
                        nextLine = provBufs[currentBufferIdx][currentLineIdx];
                        if (debug) {
                            IO.log("currentBufferIdx is " + currentBufferIdx);
                            IO.log("currentLineIdx is being incremented to " + currentLineIdx);
                            IO.log("next line assigned with provBufs[currentBufferIdx][currentLineIdx]");
                            IO.log("nextLine is...\n\n" + nextLine + "\n\n");
                            IO.log("startIdxOfRemainingChars is " + startIdxOfRemainingChars);
                            IO.log("s.length is " + s.length());
                            IO.log("checking if any characters remain in "
                                    + "sequence s with condition "
                                    + "startIdxOfRemainingChars >= 0 && "
                                    + "startIdxOfRemainingChars <= s.length()");
                        }

                        // if any characters are remaining in sequence (including newline... so <= instead of <)
                        if (startIdxOfRemainingChars >= 0 && startIdxOfRemainingChars <= s.length()) {
                            if (debug) {
                                IO.log("nextLine has not changed, see above for contents");
                                IO.log("currentCharIdx is " + currentCharIdx);
                                IO.log("s has not changed, see above for contents");
                                IO.log("startIndexOfRemainingChars has not changed, see above for value");
                                IO.log("appendRemainingCharacters(nextLine, currentCharIdx, s, startIdxOfRemainingChars) is being called");
                            }
                            // add remaining portion of sequence to beginning of incremented line
                            currentCharIdx = 0;
                            int nextIdx = appendRemainingCharacters(nextLine, s, startIdxOfRemainingChars);
                            if (debug) {
                                IO.log("nextIdx has been set with appendRemainingCharacters(nextLine, currentCharIdx, s, startIdxOfRemainingChars)");
                                IO.log("nextIdx is " + nextIdx);
                            }
                            // successful if all characters + newLine character were reported as being appended
                            success = nextIdx == s.length() + 1;
                            if (debug) {
                                IO.log("s.length() is " + s.length());
                                IO.log("success set with nextIdx == s.length() + 1");
                                IO.log("success is " + success);
                            }
                            if (success) {
                                // determine if there is any space left in the line for next append
                                int spaceRemainingInLine = StoreManager.getCharsPerLine() - nextLine.length();
                                if (debug) {
                                    IO.log("StoreManager.getCharsPerLine() is " + StoreManager.getCharsPerLine());
                                    IO.log("nextLine.length() is " + nextLine.length());
                                    IO.log("spaceRemainingInLine set with StoreManager.getCharsPerLine() - nextLine.length()");
                                    IO.log("spaceRemainingInLine is " + spaceRemainingInLine);
                                }
                                // if there is space left
                                if (spaceRemainingInLine > 0) {
                                    if (debug) {
                                        IO.log("spaceRemainingInLine > 0 is " + (spaceRemainingInLine > 0));
                                    }
                                    // move the char position to the next available index
                                    currentCharIdx = nextLine.length();
                                    if (debug) {
                                        IO.log("currentCharIdx assigned with nextLine.length()");
                                        IO.log("currentCharIdx is " + currentCharIdx);
                                    }
                                } else { // no space left in line
                                    // move to next line?
                                    boolean lineAfterNextIsAvailable = currentLineIdx + 1 < provBufs[currentBufferIdx].length;
                                    if (debug) {
                                        IO.log("currentLineIdx is " + currentLineIdx);
                                        IO.log("currentBufferIdx is " + currentBufferIdx);
                                        IO.log("provBufs[currentBufferIdx].length " + (provBufs[currentBufferIdx].length));
                                        IO.log("lineAfterNextIsAvailable assigned with currentLineIdx + 1 < provBufs[currentBufferIdx].length");
                                        IO.log("lineAfterNextIsAvailable is " + lineAfterNextIsAvailable);
                                    }
                                    if (lineAfterNextIsAvailable) {
                                        // move to next line for next append
                                        currentLineIdx++;
                                        if (debug) {
                                            IO.log("currentLineIdx incremented to " + currentLineIdx);
                                        }
                                        currentCharIdx = 0; // start at first character of line
                                        if (debug) {
                                            IO.log("currentCharIdx reset to " + currentCharIdx);
                                        }
                                    } else {
                                        // move to next buffer?
                                        boolean nextBufferAvailable = currentBufferIdx + 1 < provBufs.length;
                                        if (debug) {
                                            IO.log("currentBufferIdx is " + currentBufferIdx);
                                            IO.log("provBufs.length is " + provBufs.length);
                                            IO.log("nextBufferAvailable assigned with currentBufferIdx + 1 < provBufs.length");
                                            IO.log("nextBufferAvailable is " + nextBufferAvailable);
                                        }
                                        if (nextBufferAvailable) {
                                            // move to next buffer for next append
                                            currentBufferIdx++;
                                            currentLineIdx = 0; // start at first line of buffer
                                            currentCharIdx = 0; // start at first character of line
                                            if (debug) {
                                                IO.log("currentBufferIdx incremented to " + currentBufferIdx);
                                                IO.log("currentLineIdx reset to " + currentLineIdx);
                                                IO.log("currentCharIdx reset to " + currentCharIdx);
                                            }
                                        } else {
                                            // make space for next time
                                            persistBuffers();
                                            if (debug) {
                                                IO.log("persistBuffers called");
                                                IO.log("bufferInfo in addLineToBuffer is: ["
                                                        + getBufferInfoSnapshot()[0] + ", "
                                                        + getBufferInfoSnapshot()[1] + ", "
                                                        + getBufferInfoSnapshot()[2] + "]");
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (currentLine != null) {
                                    if (debug) {
                                        IO.log("currentLine is not null");
                                    }
                                    try {
                                        if (debug) {
                                            IO.log("currentLine is...\n\n" + currentLine + "\n\n");
                                            IO.log("deleting characters from currentLine with parameters bufferInfo[2] and currentLine.length()");
                                            IO.log("deleting characters from currentLine from idx " + bufferInfo[2] + " to " + currentLine.length());
                                        }
                                        // delete from old "next" position to end of line
                                        currentLine.delete(bufferInfo[2], currentLine.length());
                                        if (debug) {
                                            IO.log("currentLine is now...\n\n" + currentLine + "\n\n");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace(IO.getLogWriter());
                                    }
                                }
                                try {
                                    if (nextLine != null) {
                                        if (debug) {
                                            IO.log("nextLine is not null");
                                            IO.log("nextLine is...\n\n" + nextLine + "\n\n");
                                            IO.log("deleting characters from nextLine with parameters 0 and nextLine.length()");
                                            IO.log("deleting characters from currentLine from idx " + 0 + " to " + nextLine.length());
                                        }
                                        // blank the line
                                        nextLine.delete(0, nextLine.length());
                                        if (debug) {
                                            IO.log("nextLine is...\n\n" + nextLine + "\n\n");
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace(IO.getLogWriter());
                                }
                            }
                        }
                    } else { // try next buffer
                        boolean nextBufferAvailable = currentBufferIdx + 1 < provBufs.length;
                        if (debug) {
                            IO.log("currentBufferIdx is " + currentBufferIdx);
                            IO.log("provBufs.length is " + provBufs.length);
                            IO.log("nextBufferAvailable assigned with currentBufferIdx + 1 < provBufs.length");
                            IO.log("nextBufferAvailable is " + nextBufferAvailable);
                        }
                        if (nextBufferAvailable) { // next buffer is available 
                            if (debug) {
                                IO.log("appendCharactersThatFit is being called with currentLine, currentCharIdx, and s");
                                IO.log("currentCharIdx is " + currentCharIdx);
                                IO.log("s is...\n\n" + s + "\n\n");
                                IO.log("currentLine is...\n\n" + currentLine + "\n\n");
                            }
                            // add beginning portion of sequence to remaining capacity of the current line in current buffer
                            int startIdxOfRemainingChars = appendCharactersThatFit(currentLine, s);
                            if (debug) {
                                IO.log("startIdxOfRemainingChars assigned with appendCharactersThatFit(currentLine, currentCharIdx, s)");
                                IO.log("startIdxOfRemainingChars is " + startIdxOfRemainingChars);
                                IO.log("s.length is " + s.length());
                                IO.log("checking bounds with startIdxOfRemainingChars >= 0 && startIdxOfRemainingChars <= s.length()");
                                IO.log("within bounds is " + (startIdxOfRemainingChars >= 0 && startIdxOfRemainingChars <= s.length()));
                            }

                            if (startIdxOfRemainingChars >= 0 && startIdxOfRemainingChars <= s.length()) {
                                currentBufferIdx++;
                                currentLineIdx = 0;
                                // increment the buffer and retrieve its first line
                                nextLine = provBufs[currentBufferIdx][currentLineIdx];
                                if (debug) {
                                    IO.log("currentLineIdx reset to " + currentLineIdx);
                                    IO.log("currentBufferIdx incremented to " + currentBufferIdx);
                                    IO.log("nextLine assigned with provBufs[currentBufferIdx][currentLineIdx]");
                                    IO.log("nextLine is...\n\n" + nextLine + "\n\n");
                                    IO.log("currentCharIdx is " + currentCharIdx);
                                    IO.log("s has not changed, refer to the definition above for the state of the sequence s");
                                    IO.log("nextLine has not changed, refer to the definition above for the state of nextLine");
                                    IO.log("startIdxOfRemainingChars is " + startIdxOfRemainingChars);
                                    IO.log("s.length is " + s.length());
                                    IO.log("success assigned with (appendRemainingCharacters(nextLine, currentCharIdx, s, startIdxOfRemainingChars) == s.length() + 1)");
                                }
                                // add remaining portion of sequence to beginning of first line from incremented buffer
                                // successful if all characters + newLine character were reported as being appended
                                currentCharIdx = 0;
                                success = appendRemainingCharacters(nextLine, s, startIdxOfRemainingChars) == s.length() + 1;
                                if (debug) {
                                    IO.log("success is " + success);
                                }
                                if (!success) {
                                    if (currentLine != null) {
                                        if (debug) {
                                            IO.log("currentLine is not null");
                                        }
                                        try {
                                            if (debug) {
                                                IO.log("currentLine is...\n\n" + currentLine + "\n\n");
                                                IO.log("bufferInfo[2] is " + bufferInfo[2]);
                                                IO.log("currentLine.length()" + currentLine.length());
                                                IO.log("deleting characters from currentLine with currentLine.delete(bufferInfo[2], currentLine.length())");
                                                IO.log("deleting characters from " + bufferInfo[2] + " to " + currentLine.length());
                                            }
                                            // delete from old "next" position to end of line
                                            currentLine.delete(bufferInfo[2], currentLine.length());
                                            if (debug) {
                                                IO.log("currentLine is now...\n\n" + currentLine + "\n\n");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace(IO.getLogWriter());
                                        }
                                    }
                                    try {
                                        if (nextLine != null) {
                                            if (debug) {
                                                IO.log("nextLine is...\n\n" + nextLine + "\n\n");
                                                IO.log("nextLine.length()" + nextLine.length());
                                                IO.log("deleting characters from nextLine with nextLine.delete(0, nextLine.length())");
                                                IO.log("deleting characters from " + 0 + " to " + nextLine.length());
                                            }
                                            // blank the line
                                            nextLine.delete(0, nextLine.length());
                                            if (debug) {
                                                IO.log("nextLine is now... \n\n" + nextLine + "\n\n");
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace(IO.getLogWriter());
                                    }
                                }
                            }
                        } else if (!previousAttempt) { // next buffer is unavailable, attempt to persist, but only one time
                            if (debug) {
                                IO.log("previousAttempt is " + previousAttempt);
                            }
                            // persist to clear buffer (setting buffer info to init values: 0,0,0)
                            persistBuffers(); // note that nothing was added to the cleared buffer prior to...
                            if (debug) {
                                IO.log("persistBuffers called");
                                IO.log("bufferInfo in addLineToBuffer is: ["
                                        + getBufferInfoSnapshot()[0] + ", "
                                        + getBufferInfoSnapshot()[1] + ", "
                                        + getBufferInfoSnapshot()[2] + "]");
                                IO.log("RECURSION INITIATED: addLineToBuffer called with s and true");
                            }

                            // try one more (last) time
                            success = addLineToBuffer(s, true); // prior to this recursive call
                            if (debug) {
                                IO.log("success is " + success);
                            }
                            if (!success) {
                                IO.log("Failed to add line to provenance buffer, despite persisting");
                            }
                        } // else just fail (situation, 1 attempt -> 2nd attempt (persist happened) -> still no next buffer available... so just fail
                    }
                }
            }
        } else {
            if (debug) {
                IO.log("Store was never registered");
            }
            throw new Exception("Attempt to add data to unregistered store");
        }
        // if this is the original addLineToBuffer call (not a recursive call)
        if (!previousAttempt && !success) { // and the operation was not successful
            if (debug) {
                IO.log("this is the non-recursive call and it was unsuccessful");
            }
            // if a persist did not occur
            if (currentBufferIdx != 0 || currentLineIdx != 0 || currentCharIdx != 0) {
                if (debug) {
                    IO.log("At least one index pointer is not 0... presumably this means that a persist did not occur");
                }
                // reset all buffer info changes
                setBufferInfo(bufferInfo);
                if (debug) {
                    IO.log("setBufferInfo was called with the indices: "
                            + "[" + bufferInfo[0] + "," + bufferInfo[1] + ","
                            + bufferInfo[2] + "]");
                }
            } // otherwise there is no previous data, so don't reset buffer positions
        } // either everything went okay or its the recursion and just wait for calling method to adjust buffer positions
        StopWatch.stop(true);
        return success;
    }

    /**
     * Adds a line of provenance data to the next available buffer/line(s)
     *
     * NOTE: This uses up all remaining space in a line and writes the remaining
     * characters into the next line (i.e. newline character is used to
     * differentiate actual lines in persisted output)
     *
     * @param s Provenance data to add to the buffer
     * @param line buffer to add to
     * @return
     */
    boolean addLineToBuffer(String s) throws Exception {
        StopWatch.start(true);
        StopWatch.stop(true);
        return addLineToBuffer((CharSequence) s);
    }

    /**
     * Indicates whether or not the provided sequence meets the length limit
     * associated with a single line of provenance data (as configured during
     * StoreManager initialization)
     *
     * @param s sequence of characters for which bounds are being checked
     * @return True if the character sequence fits within a single line of
     * provenance data (as configured during StoreManager initialization)
     */
    boolean meetsCharacterLimit(CharSequence s) {
        StopWatch.start(true);
        boolean meetsLimit = false;
        int newLineCharacter = 1;
        int characterLimit = -1;
        if (provBufs != null && provBufs[0] != null && provBufs[0][0] != null) {
            characterLimit = StoreManager.getCharsPerLine();
            meetsLimit = s.length() + newLineCharacter <= characterLimit;
        } else {
            IO.log("Unexpected null object in ProvenanceStore.meetsCharacterLimit");
        }
        // if s.length is 3 {'0','1','2'} + newLine = 4, and buf.capacity is 4 then true
        // the above example: return 3 + 1 <= 4
        StopWatch.stop(true);
        return meetsLimit;
    }

    /**
     * Fills the provided buffer from next available character to end of the
     * buffer's capacity with characters from the provided string.
     *
     * @param line - buffer line to append to
     * @param s - string to copy from
     * @return Index of the next character after the sequence that was copied
     */
    int appendCharactersThatFit(StringBuffer line, CharSequence s) {
        StopWatch.start(true);
        int nextIdx;
        // make sure there are no errors, including sequence that does not meetsCharacterLimit()
        if (line == null || s == null || line.length() >= StoreManager.getCharsPerLine()) {
            nextIdx = -1;
        } else {
            // example line: {null,null,null} capacity: 3, currentCharIdx: 0 // first null character in sequence
            // example s   : {0   ,1   ,2   } length: 3
            int startOfCharactersThatFit = 0; // '0' in example
            int charsLeftInLine = StoreManager.getCharsPerLine() - line.length(); // 3 - 0 = 3
            // in example, needs to be: 2, charsLeftInLine: 3, startOfCharactersThatFit: 0... so 0 + 3 - 1 = 2
            // seems off but thats because we are mixing 0-based (startOf) and normal indexing (charsLeftInLine)
            int maxIdxOfLastCharInSThatWouldFitInLine = startOfCharactersThatFit + charsLeftInLine - 1;
            int end = maxIdxOfLastCharInSThatWouldFitInLine; // make a copy
            // if index exceeds the sequence bounds + \n
            if (maxIdxOfLastCharInSThatWouldFitInLine >= s.length()) {
                // adjust to last index of sequence bounds
                end = s.length() - 1;
            }
            // character after last character that fits is the next character to add after this method
            nextIdx = end + 1;
            try {
                // end is exclusive... 
                // append idxOfLastCharInSThatFits + 1 - startOfCharactersThatFit characters... 
                // 2 + 1 - 0 so, 3 characters '0', '1', and '2'
                line.append(s, startOfCharactersThatFit, end + 1);
            } catch (Exception e) {
                if (!ProvenanceStore.appendExceptionLogged) {
                    ProvenanceStore.appendExceptionLogged = true;
                    IO.log("Error appending sequence to line");
                    IO.log("\tin ProvenanceStore: " + this.storeUUID);
                    IO.log("\tof Owner: " + this.originalOwnerUUID);
                    e.printStackTrace(IO.getLogWriter());
                    IO.log("line.length() is: " + line.length());
                    IO.log("index of last character in sequence that also fits in the line: " + maxIdxOfLastCharInSThatWouldFitInLine);
                }
                nextIdx = -1;
                return nextIdx;
            }
            // add newline if the whole sequence fit perfectly
            if (maxIdxOfLastCharInSThatWouldFitInLine >= s.length()) {
                line.append('\n');
                nextIdx++; // note: if whole sequence barely fits
            }
        }
        StopWatch.stop(true);
        return nextIdx;
    }

    /**
     * Copies characters from the provided sequence starting at the provided
     * index and returns the index that comes after that of the last character
     * copied. The amount copied depends upon how many characters remain in the
     * sequence and how many free characters remain in the provided buffer. If
     * there is enough room in the buffer for the remaining characters plus the
     * newline character, then they are all copied, including the newline
     * character, and sequence length + 1 is returned (indicating that the
     * operation was completely successful and that there is not a new start
     * position for the next copy operation). Otherwise, the portion that fits
     * in the buffer is copied and the last index copied + 1 is returned.
     *
     * @param line - buffer to attempt to append provided sequence to
     * @param currentCharIdx - index of the next position to be written in the
     * provided buffer
     * @param s - sequence to attempt to append to the provided buffer
     * @param startIdxOfRemainingChars - index of the first character to be
     * appended
     * @return The next index to append from the provided sequence to the next
     * buffer for the next append operation, sequence length + 1 to indicate
     * that there are no characters left to append to the buffer, or -1 to
     * indicate that an error prevented the operation.
     */
    int appendRemainingCharacters(StringBuffer line, CharSequence s,
            int startIdxOfRemainingChars) {
        StopWatch.start(true);
        int nextIdx;
        if (line == null || s == null) {
            nextIdx = -1;
        } else {
            int start = startIdxOfRemainingChars;
            int charsLeftInS = s.length() - start;
            int spaceLeftInBuffer = StoreManager.getCharsPerLine() - line.length();
            // if the amount of characters not yet appended within the sequence is 
            // less than the amount of space left, then s.length is the last 
            // index of s that fits into the buffer, otherwise use the remaining 
            // space in the buffer, offset by the starting index in the sequence, to
            // determine the last index within the sequence that fits into the
            // remaining capacity of the buffer
            // say 4 left in buffer and 4 is the start index 4 + 4 is 8... but 
            // we want to add 4, 5, 6, and 7... since the start index is 
            // inclusive we added 4 we need to subtract 1
            int lastIdxThatFits = charsLeftInS <= spaceLeftInBuffer
                    ? s.length() - 1 : start - 1 + spaceLeftInBuffer;
            int end = lastIdxThatFits + 1;
            boolean roomForNewline = end - start < spaceLeftInBuffer;
            line.append(s, start, end);
            nextIdx = end;
            if (roomForNewline) {
                line.append('\n');
                nextIdx++;
            }
        }
        StopWatch.stop(true);
        return nextIdx;
    }

    /**
     * Manipulates buffer info members to match assumptions on a clean addition
     * of a new provenance sequence. This method is called after a successful
     * addition. Assumptions are that the next buffer, next line, and next
     * character index pointers point to what they say they point to; the next
     * available index. In the case that there was the perfect amount of space
     * available for the additional character sequence, the next pointer will be
     * left pointing beyond the bounds of the array(s).
     *
     * NOTE: If the indices point past the entire set of buffers then
     * persistence automatically occurs. In the case that we would like to
     * postpone persistence, it should not happen here, but in the persistence
     * method
     *
     * @param s The sequence that was added
     * @param line The buffer space that the sequence was added to
     */
    void incrementBufferInfo(CharSequence s, StringBuffer line) {
        StopWatch.start(true);
        int oldCharIdx = currentCharIdx;
        if (line != null && s != null) {
            int newlineChar = 1;
            currentCharIdx += s.length() + newlineChar;
            // move line position if necessary
            boolean shouldMoveToNextLine = currentCharIdx >= StoreManager.getCharsPerLine();
            if (shouldMoveToNextLine) {
                // check that moving the current line index will not cause a NullPointerException to be thrown upon next use
                int lastLineIdx = provBufs[currentBufferIdx].length - 1;
                int nextLineIdx = currentLineIdx + 1;
                boolean nextLineInBounds = nextLineIdx <= lastLineIdx;
                if (nextLineInBounds) {
                    currentLineIdx++;
                    currentCharIdx = 0; // wtf is this shit!?!? just because the line moved doesn't mean the char didnt move
                    int charsRemainingInLine = line.length() - oldCharIdx;
                    int charRemainingInSequenceAfterAddToLine = s.length() - charsRemainingInLine;
                    currentCharIdx = charRemainingInSequenceAfterAddToLine + 1;
                } else { // current line is the last line of the current buffer
                    // check that moving the current buffer will not cause a NullPointerException to be thrown upon next use
                    int lastBufferIdx = provBufs.length - 1;
                    int nextBufferIdx = currentBufferIdx + 1;
                    boolean nextBufInBounds = nextBufferIdx <= lastBufferIdx;
                    if (nextBufInBounds) {
                        currentBufferIdx++;
                        currentLineIdx = 0;
                        currentCharIdx = 0;
                    } else { // must have been persisted prior to any addition
                        currentBufferIdx = 0;
                        currentLineIdx = 0;
                        int charsRemainingInLastBuffer = line.length() - oldCharIdx;
                        // cur = 5 length = 11 lastidx = 10 5,6,7,8,9,10 = 6 written 11 - 5 = 6
                        int charsRemainingInSequence = s.length() - charsRemainingInLastBuffer;
                        // say 4 chars left in s to write to next buffer
                        // 0,1,2,3,\n next char pointer is 5
                        currentCharIdx = charsRemainingInSequence + 1;
                    }
                }
            }
            StopWatch.stop(true);
        }
    }

    boolean seqLengthFitsInCurrentLine(CharSequence s, StringBuffer buf) {
        StopWatch.start(true);
        int newlineChar = 1;
        if (s == null || buf == null) {
            StopWatch.stop(true);
            return false;
        } else {
            int lengthWithNewline = s.length() + newlineChar;
            int remainingCapacity = StoreManager.getCharsPerLine() - buf.length();
            StopWatch.stop(true);
            return lengthWithNewline <= remainingCapacity;
        }
    }

    /**
     * Resets buffer info (buf, line, char pointers to 0)
     */
    void initBufferInfo() {
        StopWatch.start(true);
        this.currentBufferIdx = 0;
        this.currentLineIdx = 0;
        this.currentCharIdx = 0;
        StopWatch.stop(true);
    }

    /**
     * Deletes all data in buffers and resets buffer info (buf, line, char
     * pointers to 0)
     */
    void clearBufs() {
        StopWatch.start(true);
        for (int i = 0, im = provBufs.length; i < im; i++) {
            for (int j = 0, jm = provBufs[i].length; j < jm; j++) {
                provBufs[i][j].delete(0, provBufs[i][j].length());
            }
        }
        initBufferInfo();
        StopWatch.stop(true);
    }

    /**
     * Adds a line of basic metadata provenance to the store. This includes a
     * metaPrefix to indicate that the line should be sorted into simulation /
     * framework-use metadata rather than relational provenance that is added to
     * a model that can be queried.
     *
     * @param line - Unprocessed basic metadata provenance
     * @return - Status corresponding to the outcome of the addition to the
     * store (see ProvenanceStore.StorageStatus java docs for further
     * information)
     */
    public StorageStatus addMetaProv(String line) {
        StopWatch.start(true);
        StorageStatus status = StorageStatus.OOM;
        if (!registered) {
            status = StorageStatus.NOT_REGISTERED;
        } else {
            try {
                line = ProvStaging.metaString(line);

                status = addProv(line);
            } catch (NullPointerException e) {
            }
        }
        StopWatch.stop(true);
        return status;
    }

    /**
     * Adds a line of relational provenance to the provenance store. This
     * includes a relationalPrefix to indicate that the line should be sorted
     * into relational provenance that is added to model for query rather than
     * simulation/framework-use metadata.
     *
     * @param subj - UID of the resource being described by the provenance
     * statement
     * @param pred - UID (RDF network address) of The relationship between the
     * subject resource and the object resource (as defined in the w3c Prov-O)
     * @param obj - UID of the resource, who's relationship to the subject
     * resource, is being stated
     * @return
     */
    public StorageStatus addRelationalProv(String subj, String pred, String obj) {
        StopWatch.start(true);
        StorageStatus status;
        synchronized (provBufs) {
            if (!registered) {
                status = StorageStatus.NOT_REGISTERED;
            } else {
                // MASS uses a lot of memory depending on the simulation. 
                // If a NullPointerException is encountered, it is likely the result 
                // of an OutOfMemory Error. While it is common to log this exception,
                // the object maintaining the provenance store should also be made aware 
                // of the implications associated with the collected provenance.
                try {
                    // gather the preprocessed provenance
                    String line = ProvStaging.ProvOString(subj, pred, obj);
                    if (debug) {
                        IO.log("line in addRelationalProv is: " + line);
                    }
                    // attempt to add it to the store
                    // Line may be null due to size restrictions. Checked in addProv.
                    //status = addProv(line);
                    status = addLineToBuffer(line) ? StorageStatus.STORED : StorageStatus.ERROR;
                    if (debug) {
                        IO.log("status in addRelationalProv is: " + status);
                    }
                    if (IO.logFlag) {
                        IO.log(ProvUtils.getHostName()
                                + " is adding the following line to its prov store: \n"
                                + line);
                    }
                } catch (Exception e) {
                    status = StorageStatus.ERROR;
                    e.printStackTrace(IO.getLogWriter());
                }
            }
        }
        StopWatch.stop(true);
        return status;
    }
    /**
     * Adds a line of relational provenance to the provenance store. This
     * includes a relationalPrefix to indicate that the line should be sorted
     * into relational provenance that is added to model for query rather than
     * simulation/framework-use metadata.
     *
     * @param subj - UID of the resource being described by the provenance
     * statement
     * @param pred - UID (RDF network address) of The relationship between the
     * subject resource and the object resource (as defined in the w3c Prov-O)
     * @param obj - UID of the resource, who's relationship to the subject
     * resource, is being stated
     * @return
     */
    public StorageStatus addRelationalProv(StringBuffer subj, StringBuffer pred,
            StringBuffer obj) {
        StopWatch.start(true);
        StorageStatus status;
        synchronized (provBufs) {
            if (!registered) {
                status = StorageStatus.NOT_REGISTERED;
            } else {
                // MASS uses a lot of memory depending on the simulation. 
                // If a NullPointerException is encountered, it is likely the result 
                // of an OutOfMemory Error. While it is common to log this exception,
                // the object maintaining the provenance store should also be made aware 
                // of the implications associated with the collected provenance.
                try {
                    // gather the preprocessed provenance
                    StringBuffer line = ProvStaging.ProvOString(subj, pred, obj);
                    if (debug) {
                        IO.log("line in addRelationalProv is: " + line);
                    }
                    // attempt to add it to the store
                    // Line may be null due to size restrictions. Checked in addProv.
                    //status = addProv(line);
                    status = addLineToBuffer(line) ? StorageStatus.STORED : StorageStatus.ERROR;
                    if (debug) {
                        IO.log("status in addRelationalProv is: " + status);
                    }
                    if (IO.logFlag) {
                        IO.log(ProvUtils.getHostName()
                                + " is adding the following line to its prov store: \n"
                                + line);
                    }
                } catch (Exception e) {
                    status = StorageStatus.ERROR;
                    e.printStackTrace(IO.getLogWriter());
                }
            }
        }
        StopWatch.stop(true);
        return status;
    }

    StorageStatus addProv(String line) throws NullPointerException {
        StopWatch.start(true);
        StorageStatus status = StorageStatus.ERROR;
        // preprocessing failed due to size of provenance?
        if ((line == null)) {
            status = StorageStatus.OVERFLOW;
        } else if (prov.size() < ProvStaging.MAX_LINES - 1) { // Space Available
            if (prov.add(line)) {
                if (IO.logFlag) {
                    IO.log(ProvUtils.getHostName() + " now contains " + prov.size() + " lines");
                }
                size += line.getBytes().length;
                status = StorageStatus.STORED;
            }
        } else if (prov.size() < ProvStaging.MAX_LINES) { // Just enough space
            if (prov.add(line)) {
                if (IO.logFlag) {
                    IO.log(ProvUtils.getHostName() + " now contains " + prov.size() + " lines");
                }
                if (autoPersist) { // persist?
                    status = persist();
                } else { // just add final item
                    status = StorageStatus.FINAL;
                }
            }
        } else { // Not enough space            
            System.err.println("Provenance cannot be stored for "
                    + this.getOwnerLocalHashCode().toString()
                    + "! Yet persistence quota was not met...");
            IO.log("Provenance cannot be stored for "
                    + this.getOwnerLocalHashCode().toString()
                    + "! Yet persistence quota was not met...");
            status = StorageStatus.FULL;
        }
        StopWatch.stop(true);
        return status;
    }

    public void transfer() {
        StopWatch.start(true);
        if (collector != null) {
            collector.transfer(this);
        }
        StopWatch.stop(true);
    }

    public void completeTransfer(Object owner) {
        StopWatch.start(true);
        if (collector != null) {
            collector.completeTransfer(this, owner);
        }
        StopWatch.stop(true);
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collection">
    /**
     * Begins collection of provenance for the method specified by className,
     * methodName, and arguments.
     *
     * @param className - Name of the class with the method that invoked this
     * method
     * @param methodName - Name of the method that invoked this method
     * @param arguments - Arguments specified for the method that invoked this
     * method
     */
    public void beginCollection(String className, String methodName,
            Object... arguments) {
        StopWatch.start(true);
        collector.beginCollection(this, className, methodName, arguments);
        StopWatch.stop(true);
    }

    /**
     * Finishes collection of provenance for the method specified by className,
     * methodName, and arguments.
     *
     * @param className - Name of the class with the method that invoked this
     * method
     * @param methodName - Name of the method that invoked this method
     * @param arguments - Arguments specified for the method that invoked this
     * method
     * @param entities - names and final values of entities used during
     * execution of method that called this method, in the form: entity name 1,
     * value 1, ..., entity name N, value N
     */
    public void endCollection(String className, String methodName,
            HashMap<String, Object> entities, Object... arguments) {
        StopWatch.start(true);
        collector.finishCollection(this, className, methodName, entities, arguments);
        StopWatch.stop(true);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Persistence">
    /**
     * Persists the current store to a new randomly named (but preprocessed
     * prefixed) file within the persistence directory
     *
     * @return Indicates the success of the operation.
     * ProvenanceStore.StorageStatus.PERSISTED if the operation succeeded,
     * otherwise ProvenanceStore.StorageStatus.PERSIST_FAILED
     */
    public StorageStatus persist() {
        StopWatch.start(true);
        StorageStatus status = StorageStatus.PERSISTED;
        if (!registered || provBufs != null) {
            status = StorageStatus.NOT_REGISTERED;
            IO.log("Persistence failed for" + " ProvenanceStore_id#"
                    + storeUUID + " was never registered with the StoreManager");
        } else {
            String filename = new StringBuilder(StoreManager.getStoreManager().
                    getPathPreprocessedProv()).append(File.separator).
                    append(ownerClassName).append("_partID=").
                    append(java.util.UUID.randomUUID()).append(".txt").toString();
            File file = new File(filename);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter writer = new BufferedWriter(fw);
                for (String statement : prov) {
                    writer.write(statement);
                    writer.newLine();
                }
                writer.newLine();
                writer.close();
                prov.clear();
                size = 0;
//                hostsThatOwnerWasPersistentlyIdentifiedAt.add(ProvUtils.getHostName());
            } catch (IOException e) {
                status = StorageStatus.PERSIST_FAILED;
            }
        }
        StopWatch.stop(true);
        return status;
    }

    public StorageStatus persistBuffers() {
        StopWatch.start(true);
        StorageStatus status = StorageStatus.PERSISTED;
//        if (!registered) {
//            status = StorageStatus.NOT_REGISTERED;
//            IO.log("Persistence failed for" + " ProvenanceStore_id#"
//                    + storeUUID
//                    + " was never registered with the StoreManager for object: "
//                    + originalOwnerUUID);
//        } else {
        // make sure the owner object has been identified as a SoftwareAgent
        // in the provenance record on this host at least once prior to 
        // persisting possibly dependent provenance, so that the dependant 
        // provenance will show in the postProcessed record
//        String ownerDefinition = identifyOwner();
        try {
            if (writer == null) {
                String filename = new StringBuilder(StoreManager.getStoreManager().
                        getPathPreprocessedProv()).append(File.separator).
                        append(ownerClassName).append("_partID=").
                        append(storeUUID).append(".txt").toString();
                File file = new File(filename);

                if (!file.getParentFile().exists()) {
                    throw new IOException("Parent directory "
                            + file.getParent() + " may have been deleted after"
                            + " initialization, but prior to persisting"
                            + " provenance file");
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                writer = new BufferedWriter(fw);
            }
            if (provBufs != null) {
                for (CharSequence[] buf : provBufs) { // all buffers
                    if (buf != null) {
                        for (CharSequence multiStatementLine : buf) { // all lines
                            writer.append(multiStatementLine); // write line
                        }
                    }
                }
//                if (ownerDefinition != null) {
//                    writer.append(ownerDefinition);
//                    writer.append('\n');
//                    hostsThatOwnerWasPersistentlyIdentifiedAt.add(ProvUtils.getHostName());
//                }
            }
            //writer.close();
            clearBufs();
            size = 0;
        } catch (IOException e) {
            status = StorageStatus.PERSIST_FAILED;
            e.printStackTrace(IO.getLogWriter()); // log main exception
        }
//        }
        StopWatch.stop(true);
        return status;
    }

    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace(IO.getLogWriter());
        }
        writer = null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Debug">
    /**
     * Prints stored provenance to standard error, as standard out is, for some
     * reason, used to stream objects to remote hosts.
     *
     * This method prints from the legacy provenance container (List prov)
     */
    public void debugStoredProv_legacy() {
        StopWatch.start(true);
        for (String line : prov) {
            System.err.println(line);
        }
        StopWatch.stop(true);
    }

    /**
     * Prints stored provenance to standard error, as standard out is, for some
     * reason, used to stream objects to remote hosts
     */
    public void debugStoredProv() {
        StopWatch.start(true);
        boolean found = false;
        if (provBufs != null) {
            for (int i = 0, im = provBufs.length; i < im; i++) {
                for (int j = 0, jm = provBufs[0].length; j < jm; j++) {
                    if (provBufs[i] != null && provBufs[i][j] != null) {
                        System.err.println(provBufs[i][j]);
                        found = true;
                    }
                }
            }
        } else {
            System.err.println("debugStoredProv: provBufs is null.");
        }
        if (!found) {
            System.err.println("debugStoredProv: no provenance stored");
        }
        StopWatch.stop(true);
    }
    // </editor-fold>
}
