/*

 	MASS Java Software License
	© 2012-2015 University of Washington

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	The following acknowledgment shall be used where appropriate in publications, presentations, etc.:      

	© 2012-2015 University of Washington. MASS was developed by Computing and Software Systems at University of 
	Washington Bothell.

	THE SOFTWARE IS PROVIDED "AS IS"), WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.

 */
package edu.uw.bothell.css.dsl.MASS.prov.core;
// PERFORMANCE DOCUMENTED

import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import edu.uw.bothell.css.dsl.MASS.prov.core.factory.ObjectFactory;
import edu.uw.bothell.css.dsl.MASS.prov.core.factory.SimpleObjectFactory;
import edu.uw.bothell.css.dsl.MASS.prov.core.logging.Log4J2Logger;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 * MASS_base maintains references to all Places, Agents, and mNode instances
 * within the cluster. Methods are provided to allow access to remote objects.
 */
public class MASSBase {

    private static MThread[] threads;          // including main and children
    private static boolean initialized;  	// check if Mthreads are initialized
    private static Vector<String> hosts = new Vector<String>();    // all host names
    private static Hashtable<Integer, PlacesBase> placesMap = new Hashtable<Integer, PlacesBase>();
    private static Hashtable<Integer, AgentsBase> agentsMap = new Hashtable<Integer, AgentsBase>();
    private static Vector<Vector<RemoteExchangeRequest>> remoteRequests = new Vector<Vector<RemoteExchangeRequest>>();
    private static Vector<Vector<AgentMigrationRequest>> migrationRequests = new Vector<Vector<AgentMigrationRequest>>();
    private static PlacesBase currentPlacesBase = null;
    private static AgentsBase currentAgentsBase = null;
    private static ExchangeHelper exchange = new ExchangeHelper();
    private static PlacesBase destinationPlaces;
    private static int currentFunctionId;
    private static Object currentArgument;
    private static Object[] currentReturns;
    private static Message.ACTION_TYPE currentMsgType;
    private static MNode thisNode;			// this node configuration

    // TODO - this is dumb. Calculate from number of hosts identified.
    private static int systemSize;          // # of processes (nodes) in the cluster (temporary!)

    // the collection of all nodes
    private static Vector<MNode> allNodes = new Vector<MNode>();

    // for performance, collection of all remote nodes
    private static Vector<MNode> remoteNodes = new Vector<MNode>();

    // for performance, the master node
    private static MNode masterNode = null;

    // remember the last PID used
    private static int lastPid = 0;

    // object factories are singletons, continue configuration within this class
    private static ObjectFactory objectFactory = SimpleObjectFactory.getInstance();

    // logging
    private static Log4J2Logger logger = Log4J2Logger.getInstance();

    // helper classes
    private static Utilities utilities = new Utilities();

    /**
     * BEGIN ASync vars section
     */
    private static AsyncInputThread inputThread = null;
    private static AsyncOutputThread outputThread = null;

    /**
     * Estimated number of completed slave node in order to reduce number of
     * complete check in case master finish too early, issue check IFF this >= #
     * of slaves
     */
    //private static AtomicInteger estimateSlaveNodeComplete = new AtomicInteger(0);
    /**
     * Agents async migrate out and into this node
     */
    private static volatile int[] outAgents, inAgents;
    private static volatile int sourceAgentPid = -1;
    private static volatile Set<Integer> childAgentPids = new HashSet<Integer>();

    /**
     * END Async vars section
     */
    /**
     * Add a new node to the cluster
     *
     * @param node The node to add to the cluster
     */
    public static void addNode(MNode node) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("addNode"), new StringBuffer("label"), true, new String[]{"node"}, new Object[]{node});

        logger.debug("Adding a node ({}) to the cluster...", node.getHostName());

        // add the node to the collection of all nodes
        allNodes.add(node);

        // if a remote, add to the collection of all remotes, or set the master if not
        // this is done so remotes and master node configurations can be obtained quickly without a lookup
        if (node.isMaster()) {

            node.setPid(0);		// master node ALWAYS has a PID of zero
            masterNode = node;

            logger.debug("This node is the MASTER node");

        } else {

            logger.debug("This node is a REMOTE node");

            // increment last PID and set for this remote node
            lastPid++;
            node.setPid(lastPid);

            remoteNodes.add(node);

        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("addNode"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static Agents getAgents(int handle) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getAgents"), new StringBuffer("label"), true, new String[]{"handle"}, new Object[]{handle});
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getAgents"), procRID, new StringBuffer("(Agents)_agentsMap.get(new_Integer(handle))"), (Agents) agentsMap.get(new Integer(handle)), null, null, true, false, false);
        StopWatch.stop(false);
        return (Agents) agentsMap.get(new Integer(handle));
    }

    public static Hashtable<Integer, AgentsBase> getAgentsMap() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getAgentsMap"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getAgentsMap"), procRID, new StringBuffer("agentsMap"), agentsMap, null, null, true, false, false);
        StopWatch.stop(false);
        return agentsMap;
    }

    /**
     * Get all MNode objects, master and remotes
     *
     * @return MNodes representing all nodes
     */
    public static Vector<MNode> getAllNodes() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getAllNodes"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getAllNodes"), procRID, new StringBuffer("allNodes"), allNodes, null, new StringBuffer("MNodes representing all nodes"), true, false, false);
        StopWatch.stop(false);
        return allNodes;
    }

    /**
     * Get the number of cores (Hyperthreading included!) in this system
     *
     * @return The number of CPU cores
     */
    public static int getCores() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCores"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCores"), procRID, new StringBuffer("Runtime.getRuntime().availableProcessors()"), Runtime.getRuntime().availableProcessors(), null, new StringBuffer("The number of CPU cores"), true, false, false);
        StopWatch.stop(false);
        return Runtime.getRuntime().availableProcessors();
    }

    public static AgentsBase getCurrentAgentsBase() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCurrentAgentsBase"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCurrentAgentsBase"), procRID, new StringBuffer("currentAgentsBase"), currentAgentsBase, null, null, true, false, false);
        StopWatch.stop(false);
        return currentAgentsBase;
    }

    public static Object getCurrentArgument() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCurrentArgument"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCurrentArgument"), procRID, new StringBuffer("currentArgument"), currentArgument, null, null, true, false, false);
        StopWatch.stop(false);
        return currentArgument;
    }

    public static int getCurrentFunctionId() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCurrentFunctionId"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCurrentFunctionId"), procRID, new StringBuffer("currentFunctionId"), currentFunctionId, null, null, true, false, false);
        StopWatch.stop(false);
        return currentFunctionId;
    }

    public static Message.ACTION_TYPE getCurrentMsgType() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCurrentMsgType"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCurrentMsgType"), procRID, new StringBuffer("currentMsgType"), currentMsgType, null, null, true, false, false);
        StopWatch.stop(false);
        return currentMsgType;
    }

    /**
     * Get the current Places object being worked on
     *
     * @return The current Places object
     */
    public static PlacesBase getCurrentPlacesBase() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCurrentPlacesBase"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCurrentPlacesBase"), procRID, new StringBuffer("currentPlacesBase"), currentPlacesBase, null, new StringBuffer("The current Places object"), true, false, false);
        StopWatch.stop(false);
        return currentPlacesBase;
    }

    public static Object[] getCurrentReturns() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCurrentReturns"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCurrentReturns"), procRID, new StringBuffer("currentReturns"), currentReturns, null, null, true, false, false);
        StopWatch.stop(false);
        return currentReturns;
    }

    public static PlacesBase getDestinationPlaces() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getDestinationPlaces"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getDestinationPlaces"), procRID, new StringBuffer("destinationPlaces"), destinationPlaces, null, null, true, false, false);
        StopWatch.stop(false);
        return destinationPlaces;
    }

    /**
     * Get the ExchangeHelper used by this instance of MASS_base
     *
     * @return The ExchangeHelper used by this instance
     */
    public static ExchangeHelper getExchange() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getExchange"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getExchange"), procRID, new StringBuffer("exchange"), exchange, null, new StringBuffer("The ExchangeHelper used by this instance"), true, false, false);
        StopWatch.stop(false);
        return exchange;
    }

    public static Vector<String> getHosts() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getHosts"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getHosts"), procRID, new StringBuffer("hosts"), hosts, null, null, true, false, false);
        StopWatch.stop(false);
        return hosts;
    }

    /**
     * Get the MNode representation of the master node only
     *
     * @return The MNode representation of the master node
     */
    public static MNode getMasterNode() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getMasterNode"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getMasterNode"), procRID, new StringBuffer("masterNode"), masterNode, null, new StringBuffer("The MNode representation of the master node"), true, false, false);
        StopWatch.stop(false);
        return masterNode;
    }

    public static Vector<Vector<AgentMigrationRequest>> getMigrationRequests() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getMigrationRequests"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getMigrationRequests"), procRID, new StringBuffer("migrationRequests"), migrationRequests, null, null, true, false, false);
        StopWatch.stop(false);
        return migrationRequests;
    }

    /**
     * Get the PID (or node number) of this node
     *
     * @return The PID of this node
     */
    public static int getMyPid() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getMyPid"), new StringBuffer("label"), true, null, null);
        // TODO - Need to throw an Exception if MASS hasn't been init'd yet!
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getMyPid"), procRID, new StringBuffer("thisNode.getPid()"), thisNode.getPid(), null, new StringBuffer("The PID of this node"), true, false, false);
        StopWatch.stop(false);
        return thisNode.getPid();

    }

    ;
	
	
	public static Places getPlaces(int handle) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getPlaces"), new StringBuffer("label"), true, new String[]{"handle"}, new Object[]{handle});
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getPlaces"), procRID, new StringBuffer("(Places)_placesMap.get(new_Integer(handle))"), (Places) placesMap.get(new Integer(handle)), null, null, true, false, false);
        StopWatch.stop(false);
        return (Places) placesMap.get(new Integer(handle));
    }

    /**
     * Get the collection of Places located on this node
     *
     * @return Places located on this node
     */
    public static Hashtable<Integer, PlacesBase> getPlacesMap() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getPlacesMap"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getPlacesMap"), procRID, new StringBuffer("placesMap"), placesMap, null, new StringBuffer("Places located on this node"), true, false, false);
        StopWatch.stop(false);
        return placesMap;
    }

    /**
     * Get all MNode objects representing remote nodes only
     *
     * @return MNodes representing all remote nodes
     */
    public static Vector<MNode> getRemoteNodes() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getRemoteNodes"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getRemoteNodes"), procRID, new StringBuffer("remoteNodes"), remoteNodes, null, new StringBuffer("MNodes representing all remote nodes"), true, false, false);
        StopWatch.stop(false);
        return remoteNodes;
    }

    public static Vector<Vector<RemoteExchangeRequest>> getRemoteRequests() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getRemoteRequests"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getRemoteRequests"), procRID, new StringBuffer("remoteRequests"), remoteRequests, null, null, true, false, false);
        remoteRequests.toString();
        StopWatch.stop(false);
        return remoteRequests;
    }

    /**
     * Get the total number of nodes in the cluster
     *
     * @return The number of nodes
     */
    public static int getSystemSize() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getSystemSize"), new StringBuffer("label"), true, null, null);
        int size = systemSize;
        // if nodes have been defined, use the number of nodes as the system size
        if (allNodes.size() > 0) {
            size = allNodes.size();
        } else // have the hosts been identified this way?
        if (hosts.size() > 0) {
            size = hosts.size();
        }

        // TODO - should not need to set system size using constructor
        // must be using a legacy method of init, use the old method
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getSystemSize"), procRID, new StringBuffer("size"), size, null, new StringBuffer("The number of nodes"), true, false, false);
        StopWatch.stop(false);
        return size;
    }

    public static MThread[] getThreads() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getThreads"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getThreads"), procRID, new StringBuffer("threads"), threads, null, null, true, false, false);
        StopWatch.stop(false);
        return threads;
    }

    /**
     * Get the directory ("MASS Home") that this node is working from
     *
     * @return The working directory
     */
    public static String getWorkingDirectory() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getWorkingDirectory"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getWorkingDirectory"), procRID, new StringBuffer("thisNode_!=_null_?_thisNode.getMassHome()_:_null"), thisNode != null ? thisNode.getMassHome() : null, null, new StringBuffer("The working directory"), true, false, false);
        StopWatch.stop(false);
        return thisNode != null ? thisNode.getMassHome() : null;
    }

    public static boolean initializeThreads(int nThr) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initializeThreads"), new StringBuffer("label"), true, new String[]{"nThr"}, new Object[]{nThr});
        if (initialized) {
            logger.error("Error: MASS.init is already initialized");
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initializeThreads"), procRID, new StringBuffer("true"), true, null, null, true, false, false);
            StopWatch.stop(false);
            return false;
        }

        int cores = (nThr <= 0) ? getCores() : nThr;

        // all pthread_t structures
        threads = new MThread[cores];
        threads[0] = null; // reserved for the main thread

        // initialize Mthread's static variables
        MThread.init();

        // now launch child threads
        synchronized (MThread.getLock()) {
            MThread.setThreadCreated(0);
        }

        for (int i = 1; i < cores; i++) {

            threads[i] = new MThread(i);
            threads[i].start();

            while (true) {

                synchronized (MThread.getLock()) {
                    if (MThread.getThreadCreated() == i) {
                        break;
                    }

                }

            }

        }

        logger.debug("Initialized threads - # " + cores);
        initialized = true;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initializeThreads"), procRID, new StringBuffer("true"), true, null, null, true, false, false);
        StopWatch.stop(false);
        return true;
    }

    /**
     * Initialize MASS_base, using an MNode object representing this node as the
     * source for configuration
     *
     * @param nodeConfig The MNode object representing this node
     */
    public static void initMASSBase(MNode nodeConfig) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initMASSBase"), new StringBuffer("label"), true, new String[]{"nodeConfig"}, new Object[]{nodeConfig});

        // TODO - everything assumes that a nodeConfig is supplied! Probably should throw IllegalArgumentException.
        if (nodeConfig == null) {
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initMASSBase"), procRID, null, null, null, null, true, false, false);
            return;
        }

        MASSBase.thisNode = nodeConfig;

        // Set hostname if not set previously
        if (thisNode.getHostName() == null) {
            thisNode.setHostName(utilities.getLocalHostname());
        }

        // Set the current working directory to default value if not set previously
        if (thisNode.getMassHome() == null) {
            thisNode.setMassHome(System.getProperty("user.dir"));
        }

        // with options set, now configure logging
        logger.setLogFileName(getLogFileName());

        // log options that have been set, now that there is a valid log filename
        logger.debug("Working directory set to {}", getWorkingDirectory());
        logger.debug("Hostname set to {}", thisNode.getHostName());

        // add MASS home to the list of URLs to be used by the object factory
        try {
            objectFactory.addUri(new File(MASSBase.getWorkingDirectory()).toURI().toString());
        } catch (Exception e) {
            logger.error("Exception caught while adding ObjectFactory URI", e);
        }
        // Async section
        initAsyncCommunicationThreads();
        logger.debug("MASSBase initialization complete");
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initMASSBase"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Initialize MASS_base, new StringBuffer("legacy" mode
     *
     * @param name The hostname or IP address of this node
     * @param myPid The PID assigned to this node
     * @param nProc The total number of nodes in the cluster
     * @param port The port number to use for communications with this node
     */
    //@Deprecated
    public static void initMASSBase(String name, int myPid, int nProc, int port) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initMASSBase"), new StringBuffer("label"), true, new String[]{"name", "myPid", "nProc", "port"}, new Object[]{name, myPid, nProc, port});
        // create a MNode representation of this node, only for init purposes (legacy mode)
        MNode thisNode = new MNode();
        thisNode.setHostName(name);
        thisNode.setPid(myPid);
        thisNode.setPort(port);
        // TODO - this is a hack. System size is the number of identified hosts, not some command-line argument.
        systemSize = nProc;
        // init from the MNode object
        initMASSBase(thisNode);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initMASSBase"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Get the initialized status of this node
     *
     * @return True, if this node has been initialized successfully
     */
    public static boolean isInitialized() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("isInitialized"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("isInitialized"), procRID, new StringBuffer("initialized"), initialized, null, new StringBuffer("True, if this node has been initialized successfully"), true, false, false);
        StopWatch.stop(false);
        return initialized;

    }

    /**
     * Reset the request counter
     */
    public static void resetRequestCounter() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("resetRequestCounter"), new StringBuffer("label"), true, null, null);
        //requestCounter = 0;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("resetRequestCounter"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void setAgentsMap(Hashtable<Integer, AgentsBase> agentsMap) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setAgentsMap"), new StringBuffer("label"), true, new String[]{"agentsMap"}, new Object[]{agentsMap});
        MASSBase.agentsMap = agentsMap;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setAgentsMap"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void setCurrentAgentsBase(AgentsBase currentAgents) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCurrentAgentsBase"), new StringBuffer("label"), true, new String[]{"currentAgents"}, new Object[]{currentAgents});
        MASSBase.currentAgentsBase = currentAgents;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCurrentAgentsBase"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void setCurrentArgument(Object currentArgument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCurrentArgument"), new StringBuffer("label"), true, new String[]{"currentArgument"}, new Object[]{currentArgument});
        MASSBase.currentArgument = currentArgument;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCurrentArgument"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void setCurrentFunctionId(int currentFunctionId) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCurrentFunctionId"), new StringBuffer("label"), true, new String[]{"currentFunctionId"}, new Object[]{currentFunctionId});
        MASSBase.currentFunctionId = currentFunctionId;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCurrentFunctionId"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void setCurrentMsgType(Message.ACTION_TYPE currentMsgType) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCurrentMsgType"), new StringBuffer("label"), true, new String[]{"currentMsgType"}, new Object[]{currentMsgType});
        MASSBase.currentMsgType = currentMsgType;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCurrentMsgType"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the current Places object to be worked on
     *
     * @param currentPlaces The current Places object
     */
    public static void setCurrentPlacesBase(PlacesBase currentPlaces) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCurrentPlacesBase"), new StringBuffer("label"), true, new String[]{"currentPlaces"}, new Object[]{currentPlaces});
        MASSBase.currentPlacesBase = currentPlaces;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCurrentPlacesBase"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void setCurrentReturns(Object[] currentReturns) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCurrentReturns"), new StringBuffer("label"), true, new String[]{"currentReturns"}, new Object[]{currentReturns});
        MASSBase.currentReturns = currentReturns;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCurrentReturns"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void setDestinationPlaces(PlacesBase destinationPlaces) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setDestinationPlaces"), new StringBuffer("label"), true, new String[]{"destinationPlaces"}, new Object[]{destinationPlaces});
        MASSBase.destinationPlaces = destinationPlaces;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setDestinationPlaces"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Sets the hosts that MASS is using.
     *
     * @param host_args
     */
    public static void setHosts(Vector<String> host_args) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setHosts"), new StringBuffer("label"), true, new String[]{"host_args"}, new Object[]{host_args});

        if (!hosts.isEmpty()) {
            // already initialized
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setHosts"), procRID, null, null, null, null, true, false, false);
            return;

        }

        // register all hosts including myself
        for (int i = 0; i < host_args.size(); i++) {
            logger.debug("MASS_base.setHosts: Adding host {}", host_args.get(i));
            hosts.add(host_args.get(i));
        }

        logger.debug("MASS_base.setHosts: System size = {}", getSystemSize());

        // instantiate remoteRequests: Vector< Vector<RemoteExchangeReques> >
        // as well as migrationRequests for the purpose of agent migration.
        remoteRequests = new Vector<Vector<RemoteExchangeRequest>>();
        migrationRequests = new Vector<Vector<AgentMigrationRequest>>();

        for (int i = 0; i < getSystemSize(); i++) {
            remoteRequests.add(new Vector<RemoteExchangeRequest>());
            migrationRequests.add(new Vector<AgentMigrationRequest>());
        }

        // establish inter-MASS connection
        exchange.establishConnection(getSystemSize(), thisNode.getPid(), hosts, thisNode.getPort());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setHosts"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the initialized status of this node
     *
     * @param initialized The initialization complete status for this node
     */
    public static void setInitialized(boolean initialized) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setInitialized"), new StringBuffer("label"), true, new String[]{"initialized"}, new Object[]{initialized});
        MASSBase.initialized = initialized;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setInitialized"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void setMigrationRequests(
            Vector<Vector<AgentMigrationRequest>> migrationRequests) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setMigrationRequests"), new StringBuffer("label"), true, new String[]{"migrationRequests"}, new Object[]{migrationRequests});
        MASSBase.migrationRequests = migrationRequests;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setMigrationRequests"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void setRemoteRequests(
            Vector<Vector<RemoteExchangeRequest>> remoteRequests) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setRemoteRequests"), new StringBuffer("label"), true, new String[]{"remoteRequests"}, new Object[]{remoteRequests});
        MASSBase.remoteRequests = remoteRequests;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setRemoteRequests"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set (override) the working directory ("MASS Home") for this node
     *
     * @param workingDirectory The new working directory for this node
     */
    public static void setWorkingDirectory(String workingDirectory) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setWorkingDirectory"), new StringBuffer("label"), true, new String[]{"workingDirectory"}, new Object[]{workingDirectory});

        // has MASS been initialized yet?
        if (thisNode == null) {
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setWorkingDirectory"), procRID, null, null, null, null, true, false, false);
            return;
        }

        if (workingDirectory != null) {
            thisNode.setMassHome(workingDirectory);
        } else {

        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setWorkingDirectory"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Logs the hosts MASS is using.
     */
    public static void showHosts() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("showHosts"), new StringBuffer("label"), true, null, null);
        if (logger.isDebugEnabled()) {

            String convert = "Hosts: ";

            for (int i = 0; i < hosts.size(); i++) {
                convert += "rank[" + i + "] = " + hosts.get(i) + " ";
            }

            logger.debug(convert);

        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("showHosts"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * BEGIN Async methods
     */
    public static AsyncInputThread getAsyncInputThread() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getAsyncInputThread"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getAsyncInputThread"), procRID, new StringBuffer("inputThread"), inputThread, null, null, true, false, false);
        StopWatch.stop(false);
        return inputThread;
    }

    public static AsyncOutputThread getAsyncOutputThread() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getAsyncOutputThread"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getAsyncOutputThread"), procRID, new StringBuffer("outputThread"), outputThread, null, null, true, false, false);
        StopWatch.stop(false);
        return outputThread;
    }

    public static void initAsyncCommunicationThreads() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initAsyncCommunicationThreads"), new StringBuffer("label"), true, null, null);
        logger.debug("Init Async Communication Threads");
        inputThread = new AsyncInputThread(thisNode.getPort() + 1);
        outputThread = new AsyncOutputThread(thisNode.getPort() + 1);
        inputThread.start();
        outputThread.start();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initAsyncCommunicationThreads"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /*
		1) Called from callAllSetupAsync method in Agents.java
		2) In MProcess.java upon receiving the message AGENTS_CALL_ALL_ASYNC_RETURN_OBJECT
	* */
    public static void prepareAsyncExecution(AgentsBase agents, int[] fIds) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("prepareAsyncExecution"), new StringBuffer("label"), true, new String[]{"agents", "fIds"}, new Object[]{agents, fIds});
        setCurrentAgentsBase(agents);
        MThread.setAgentBagSize(currentAgentsBase.getAgents().size()); // number of agents that are going to be executed

        currentAgentsBase.setAsyncFuncList(fIds);
        currentAgentsBase.resetChildAsyncIndex(); // queue for maintaining agents created
        currentAgentsBase.resetAsyncCompletedAgentList(); // queue for maintaining agents that completed the function

        currentAgentsBase.asyncAgentIdListClear(); // agents to be executed
        for (int i = 0; i < currentAgentsBase.getAgents().size_unreduced(); i++) {
            currentAgentsBase.asyncAgentIdListAdd(i);
            currentAgentsBase.getAgents().get(i).setMyAgentsBase(currentAgentsBase);
            currentAgentsBase.getAgents().get(i).setAsyncFuncListIndex(0);
            currentAgentsBase.getAgents().get(i).resetAsyncResults();
            currentAgentsBase.getAgents().get(i).setMyAsyncOriginalPid(getMyPid());
            currentAgentsBase.getAgents().get(i).setMyOriginalAsyncIndex(i);
            currentAgentsBase.getAgents().get(i).setCurrentIndex(i);
        }
        outputThread.setAgentHandle(agents.getHandle());
        outputThread.setPlaceHandle(agents.getPlacesHandle());
        outAgents = new int[getSystemSize()];
        inAgents = new int[getSystemSize()];
        for (int i = 0; i < outAgents.length; i++) {
            outAgents[i] = 0;
            inAgents[i] = 0;
        }
        currentAgentsBase.setResultRequestFromMaster(false);
        sourceAgentPid = -1;
        childAgentPids.clear();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("prepareAsyncExecution"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static Set<Integer> getChildAgentPids() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getChildAgentPids"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getChildAgentPids"), procRID, new StringBuffer("childAgentPids"), childAgentPids, null, null, true, false, false);
        StopWatch.stop(false);
        return childAgentPids;
    }

    public static int getSourceAgentPid() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getSourceAgentPid"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getSourceAgentPid"), procRID, new StringBuffer("sourceAgentPid"), sourceAgentPid, null, null, true, false, false);
        StopWatch.stop(false);
        return sourceAgentPid;
    }

    public static void setSourceAgentPid(int value) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setSourceAgentPid"), new StringBuffer("label"), true, new String[]{"value"}, new Object[]{value});
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setSourceAgentPid"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        sourceAgentPid = value;
    }

    public static int[] getOutAsyncAgents() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getOutAsyncAgents"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getOutAsyncAgents"), procRID, new StringBuffer("outAgents"), outAgents, null, null, true, false, false);
        StopWatch.stop(false);
        return outAgents;
    }

    public static int[] getInAsyncAgents() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getInAsyncAgents"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getInAsyncAgents"), procRID, new StringBuffer("inAgents"), inAgents, null, null, true, false, false);
        StopWatch.stop(false);
        return inAgents;
    }

    /**
     * END Async methods
     */
    /**
     * Get the port number used for inter-node communications
     *
     * @return The port number
     */
    public static int getCommunicationPort() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCommunicationPort"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getCommunicationPort"), procRID, new StringBuffer("thisNode.getPort()"), thisNode.getPort(), null, new StringBuffer("The port number"), true, false, false);
        StopWatch.stop(false);
        return thisNode.getPort();
    }

    /**
     * Set the port number used for inter-node communications
     *
     * @param communicationPort The port number
     */
    public static void setCommunicationPort(int communicationPort) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCommunicationPort"), new StringBuffer("label"), true, new String[]{"communicationPort"}, new Object[]{communicationPort});

        // can't set port to zero
        // TODO - should throw IllegalArgumentException
        if (communicationPort == 0) {
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCommunicationPort"), procRID, null, null, null, null, true, false, false);
            StopWatch.stop(false);
            return;
        }
        // not init'd yet?
        // TODO - should throw some form of Exception
        if (thisNode == null) {
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCommunicationPort"), procRID, null, null, null, null, true, false, false);
            StopWatch.stop(false);
            return;
        }
        thisNode.setPort(communicationPort);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setCommunicationPort"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Get the filename of the log file, based in part on the node number and
     * hostname
     *
     * @return The name of the file that should be used for logging
     */
    public static String getLogFileName() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getLogFileName"), new StringBuffer("label"), true, null, null);

        if (thisNode == null) {
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getLogFileName"), procRID, new StringBuffer("null"), null, null, null, true, false, false);
            return null;	// not initialized yet!
        }
        // make sure hostname is cleansed to provide a safe filename fragment
        String safeHostname = thisNode.getHostName();
        if (safeHostname != null) {

            // dots mess up paths
            safeHostname = safeHostname.replace(".", "_");

        }

        String logFilename = getWorkingDirectory() + "/logs/" + "PID" + getMyPid() + "_" + safeHostname + "_result.txt";
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getLogFileName"), procRID, new StringBuffer("logFilename"), logFilename, null, new StringBuffer("The name of the file that should be used for logging"), true, false, false);
        StopWatch.stop(false);
        return logFilename;

    }

    /**
     * Get the Logger instance, primarily for MASS applications to record
     * messages to the same logger the library is using
     *
     * @return The logger
     */
    public static Log4J2Logger getLogger() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getLogger"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getLogger"), procRID, new StringBuffer("logger"), logger, null, new StringBuffer("The logger"), true, false, false);
        StopWatch.stop(false);
        return logger;
    }

}
