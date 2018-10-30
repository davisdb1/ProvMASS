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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.jcraft.jsch.Channel;
import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.ProvOntology;

import edu.uw.bothell.css.dsl.MASS.prov.core.MassData.AgentData;
import edu.uw.bothell.css.dsl.MASS.prov.core.MassData.InitialData;
import edu.uw.bothell.css.dsl.MASS.prov.core.MassData.MASSRequest;
import edu.uw.bothell.css.dsl.MASS.prov.core.MassData.PlaceData;
import edu.uw.bothell.css.dsl.MASS.prov.core.MassData.UpdatePackage;
import edu.uw.bothell.css.dsl.MASS.prov.core.factory.ObjectFactory;
import edu.uw.bothell.css.dsl.MASS.prov.core.factory.SimpleObjectFactory;
import edu.uw.bothell.css.dsl.MASS.prov.core.logging.Log4J2Logger;
import edu.uw.bothell.css.dsl.MASS.prov.core.logging.LogLevel;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.StoreManager;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 * MASS is responsible for the construction and deconstruction of the cluster.
 */
public class MASS extends MASSBase {

    private static boolean printOutput = false;
    private static boolean initialized = false;
    private static Utilities util = new Utilities();  // used for channel creation

    // the number of threads to spawn on each node (default to 1)
    private static int numThreads = 1;

    // default user credentials (can be overridden via XML)
    private static String defaultUsername;
    private static String defaultPassword;

    // name of file containing cluster node definitions
    private static String nodeFilePath = "nodes.xml";

    // object factories are singletons, so we'll use this opportunity to initialize it
    // yes - unused at this point right now...
    @SuppressWarnings("unused")
    private static ObjectFactory objectFactory = SimpleObjectFactory.getInstance();

    // Async
    // number of agents at rank i that returns async results
    private static int[] LocalAgents;

    // Logging
    private static Log4J2Logger logger = Log4J2Logger.getInstance();
    private static boolean waitingOnFinishAck = false;

    static void barrierAllSlaves() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("barrierAllSlaves"), new StringBuffer("label"), true, null, null);
        barrierAllSlaves(null, 0, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("barrierAllSlaves"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    static void barrierAllSlaves(int localAgents[]) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("barrierAllSlaves"), new StringBuffer("label"), true, new String[]{"localAgents"}, new Object[]{localAgents});
        barrierAllSlaves(null, 0, localAgents);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("barrierAllSlaves"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    static void barrierAllSlaves(Object[] returnValues, int stripe) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("barrierAllSlaves"), new StringBuffer("label"), true, new String[]{"returnValues", "stripe"}, new Object[]{returnValues, stripe});
        barrierAllSlaves(returnValues, stripe, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("barrierAllSlaves"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    static void barrierAllSlaves(Object[] returnValues, int stripe, int localAgents[]) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("barrierAllSlaves"), new StringBuffer("label"), true, new String[]{"returnValues", "stripe", "localAgents"}, new Object[]{returnValues, stripe, localAgents});

        // counts the agent population from each Mprocess
        int nAgentsSoFar = (localAgents != null) ? localAgents[0] : 0;

        // Synchronize with all slave processes
        for (int i = 0; i < getRemoteNodes().size(); i++) {
            if (printOutput == true) {
                System.err.println("barrier waits for ack from "
                        + getRemoteNodes().get(i).getHostName());
            }

            Message m = getRemoteNodes().get(i).receiveMessage();
            ProvenanceRecorder.documentEntity(ProvUtils.getStoreOfCurrentThread(), null, null, new StringBuffer("barrierAllSlaves"), procRID, new StringBuffer("m"), m, new StringBuffer(m.getOwnerUUID()), null, true);

            if (printOutput == true) {
                System.err.println("barrier received a message from "
                        + getRemoteNodes().get(i).getHostName()
                        + "...message = " + m);
            }

            /* Gather Performance Documentation On FINISH */
            if (MASSProv.provOn && waitingOnFinishAck) {
                if (m.getAction() == Message.ACTION_TYPE.ACK) {
                    Long[] durations = null;
                    try {
                        durations = (Long[]) m.getArgument();
                    } catch (ClassCastException e) {
                        e.printStackTrace(IO.getLogWriter());
                    }
                    if (durations != null) {
                        StopWatch.mapDurationsReceivedFromRemoteHost(
                                getRemoteNodes().get(i).getHostName(), durations);
                    } else {
                        IO.log("No durations were returned in FINISH " + "acknowledgement from remote hosts");
                    }
                }
            }

            // check this is an Ack
            if (m.getAction() != Message.ACTION_TYPE.ACK) {
                System.err.println("barrier didn't receive ack from rank "
                        + (i + 1) + " at "
                        + getRemoteNodes().get(i).getHostName()
                        + " message action type = " + m.getAction());
                System.exit(-1);
            }

            // retrieve arguments back from each Mprocess
            // places.callAll( ) with return values
            if (returnValues != null) {
                if (stripe > 0 && localAgents == null) {

                    // check if the message is from the last mNode as
                    // the last mNode might have a remainder (stripe + rem)
                    // for simplicity, we just use the length of the returned
                    // array
                    int copyLength;
                    if (i == getRemoteNodes().size() - 1) {
                        copyLength = ((Object[]) m.getArgument()).length;
                    } else {
                        copyLength = stripe;
                    }

                    // copy the partial array into the return_values array
                    System.arraycopy(m.getArgument(), 0,
                            returnValues, stripe * (i + 1),
                            copyLength);
                }
                if (stripe == 0 && localAgents != null) {
                    // agents.callAll( ) with return values
                    System.arraycopy(m.getArgument(), 0,
                            returnValues, nAgentsSoFar,
                            localAgents[i + 1]);
                }
            }

            // retrieve agent population from each Mprocess
            if (printOutput == true) {
                System.err.println("localAgents[" + (i + 1)
                        + "] = m.getAgentPopulation: "
                        + m.getAgentPopulation());
            }

            if (localAgents != null) {
                localAgents[i + 1] = m.getAgentPopulation();
                nAgentsSoFar += localAgents[i + 1];
            }

            if (printOutput == true) {
                System.err.println("message deleted");
            }

        }
        if (waitingOnFinishAck) {
            waitingOnFinishAck = false;
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("barrierAllSlaves"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Finish computation, terminate remote processes, and perform cleanup and
     * disconnection operations.
     *
     * This method should be called when all computational work has been
     * completed.
     */
    public static void finish() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("finish"), new StringBuffer("label"), true, null, null);

        MThread.resumeThreads(MThread.STATUS_TYPE.STATUS_TERMINATE);
        MThread.barrierThreads(0);

        if (MASS.isConsoleLoggingEnabled()) {
            System.err.println("MASS::finish: all MASS threads terminated");
        }

        // Close connection and finish each mprocess
        for (MNode node : getRemoteNodes()) {
            // Send a finish messages
            Message m = new Message(Message.ACTION_TYPE.FINISH);
            node.sendMessage(m);
            waitingOnFinishAck = true;
        }

        // Synchronize with all slaves
        barrierAllSlaves();

        for (MNode node : getRemoteNodes()) {
            node.closeMainConnection();
        }

        MASSBase.getAsyncOutputThread().finish();
        MASSBase.getAsyncInputThread().finish();

//        System.err.println("MASS::finish: done");
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("finish"), procRID, null, null, null, null, true, false, false);
        if (MASSProv.isInitialized()) {
            MASSProv.finish(); // finish here, as well
        }
        StopWatch.stop(false);
    }

    /**
     * Get the default password for connecting to remote nodes
     *
     * @return The default login password
     */
    @Deprecated
    protected static String getDefaultPassword() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getDefaultPassword"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getDefaultPassword"), procRID, new StringBuffer("defaultPassword"), defaultPassword, null, new StringBuffer("The default login password"), true, false, false);
        StopWatch.stop(false);
        return defaultPassword;
    }

    /**
     * Get the default username for connecting to remote nodes
     *
     * @return The default login username
     */
    protected static String getDefaultUsername() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getDefaultUsername"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getDefaultUsername"), procRID, new StringBuffer("defaultUsername"), defaultUsername, null, new StringBuffer("The default login username"), true, false, false);
        StopWatch.stop(false);
        return defaultUsername;
    }

    /**
     * Get the filename for the cluster node definition file
     *
     * @return The cluster node definition filename
     */
    public static String getNodeFilePath() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getNodeFilePath"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getNodeFilePath"), procRID, new StringBuffer("nodeFilePath"), nodeFilePath, null, new StringBuffer("The cluster node definition filename"), true, false, false);
        StopWatch.stop(false);
        return nodeFilePath;
    }

    /**
     * Get the number of threads that will be spawned on each node
     *
     * @return The number of threads spawned
     */
    public static int getNumThreads() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getNumThreads"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getNumThreads"), procRID, new StringBuffer("numThreads"), numThreads, null, new StringBuffer("The number of threads spawned"), true, false, false);
        StopWatch.stop(false);
        return numThreads;
    }

    // TODO - replace with a logger library hopefully
    public static boolean isConsoleLoggingEnabled() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("isConsoleLoggingEnabled"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("isConsoleLoggingEnabled"), procRID, new StringBuffer("printOutput"), printOutput, null, null, true, false, false);
        StopWatch.stop(false);
        return printOutput;
    }

    /**
     * Initialize the MASS library (using settings made previously via setters).
     * Calling this method effectively begins computation.
     */
    public static void init() {
        StopWatch.start(false);
        // start MASSProv on node where this main method was invoked
        if (MASSProv.provOn && !MASSProv.isInitialized()) {
            MASSProv.init();
        }
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("init"), new StringBuffer("label"), true, null, null);

        // attempt to load node definitions from specified file
        if (getNodeFilePath() != null && getNodeFilePath().length() > 0) {

            // attempt to open the specified file
            File machineFile = new File(getNodeFilePath());

            // does the file actually exist?
            if (!machineFile.canRead()) {
                System.err.println("node file path: " + getNodeFilePath()
                        + " does not exist or is not readable.");
                System.err.println("absolute machine file path: " + machineFile.getAbsolutePath()
                        + " does not exist or is not readable.");

                System.exit(-1);
            }

            // is the machine file an XML document? 
            if (getNodeFilePath().toLowerCase().contains("xml")) {

                // yes - filename specified is an XML document - get MNodes directly from the doc
                try {
                    JAXBContext jaxbContext = JAXBContext.newInstance(Nodelist.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    Nodelist nodeList = (Nodelist) jaxbUnmarshaller.unmarshal(machineFile);

                    // iterate through the nodes, adding each
                    for (MNode node : nodeList.getNodes()) {
                        addNode(node);
                    }
                } catch (JAXBException e) {

                    System.err.println("Error initializing JAXB parser..."
                            + e.getStackTrace());
                    e.printStackTrace();
                    System.exit(-1);
                }
            } else {
                // no - this machine file is the classic one-line-per-node format
                BufferedReader fileReader = null;

                try {
                    fileReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(
                            machineFile))));

                    while (fileReader.ready()) {
                        // create a new MNode for each line in the file (these will all be remote nodes)
                        MNode node = new MNode();
                        node.setHostName(fileReader.readLine());
                        addNode(node);
                    }
                    fileReader.close();
                } catch (Exception e) {
                    System.err.println("machine file: " + getNodeFilePath()
                            + " could not open.");

                    System.exit(-1);
                }
            }
        } else {
            System.err.println(" No Node File Path Given");
            System.exit(-1);
        }

        // For debugging
        if (printOutput == true) {
            for (MNode node : getRemoteNodes()) {
                System.err.println("rank " + node.getPid() + ": "
                        + node.getHostName());
            }
        }

        // if not already defined, create master node representation
        if (getMasterNode() == null) {

            MNode masterNode = new MNode();
            masterNode.setMaster(true);
            addNode(masterNode);
        }

        // Initialize MASS_base.constants and identify the CWD.
        if (getMasterNode() != null) {

            // init using Master node config
            initMASSBase(getMasterNode());
        } else {
            // init using "old" method
            initMASSBase("localhost", 0, getAllNodes().size(), getCommunicationPort());
        }
        MASSProv.setInitNanos();
        // Launch remote processes
        for (MNode node : getRemoteNodes()) {

            // set login credentials if not defined in the node config already
            if (node.getUserName() == null) {
                node.setUserName(getDefaultUsername());
            }
            //if (node.getPassWord() == null) node.setPassWord(getDefaultPassword());

            // retrieve each canonical remote machine name
            try {

                InetAddress addr = InetAddress.getByName(node.getHostName());
                node.setHostName(addr.getCanonicalHostName());

            } catch (Exception e) {

                logger.error("Wrong host name: {}", node.getHostName(), e);
                System.exit(-1);

            }

            // For debugging
            if (printOutput == true) {
                System.err.println("curHostName = " + node.getHostName());
            }

            // Start a remote process
            // java attributes and its jar files
            StringBuilder commandBuilder = new StringBuilder();

            // add location of JVM if specified
            if (node.getJavaHome() != null) {
                commandBuilder.append(node.getJavaHome() + "/");
            }

            // gotta specify the JVM
            commandBuilder.append("java ");

            // TODO - add configurable heap memory sizes per node
            commandBuilder.append("-d64 ");
            commandBuilder.append("-Xmx9g ");

            // add MASS home directory itself as part of the classpath
            // set location of MASS.jar
            commandBuilder.append("-cp ");
            // Commented out to use local HDD for Prov data without having to copy jar to all masshome directories
//            if (node.getMassHome() != null) {
//                commandBuilder.append(node.getMassHome() + "/");
//            }
            commandBuilder.append("MASSProv-StandardJavaProject.jar ");

//            if (node.getMassHome() != null) {
//                commandBuilder.append("-cp " + node.getMassHome() + "/*.jar ");
//            } else {
//                commandBuilder.append("MASSProv-StandardJavaProject.jar ");
//            }
            // MProcess and its arguments
            commandBuilder.append(MProcess.class.getCanonicalName()).append(" ");	// the program
            commandBuilder.append(node.getHostName()).append(" ");	// 1st arg: hostName
            commandBuilder.append(node.getPid()).append(" ");			// 2nd arg: pid
            commandBuilder.append(getAllNodes().size()).append(" ");	// 3rd arg: #processes
            commandBuilder.append(getNumThreads()).append(" ");   	// 4th arg: #threads
            commandBuilder.append(getCommunicationPort()).append(" ");// 5th arg: MASS_PORT
            if (node.getMassHome() != null) {
                commandBuilder.append(node.getMassHome());			// 6th arg: cur working dir
            }
            // debug
//            System.err.println("MProcess on " + node.getHostName()
//                    + " run with command: " + commandBuilder);

            try {
//    			Channel ssh2connection = util.LaunchRemoteProcess( node.getHostName(),
//    					JschPort,
//    					commandBuilder.toString(),
//    					node.getUserName(),
//    					node.getPassWord() );
                /**
                 * ** DELETE THIS ****
                 */
                // debug MProcess launch... 
//                System.err.println("Issuing command on " + node.getHostName() + ": " + commandBuilder.toString());
                /**
                 * ** DELETE THIS ****
                 */
                Channel ssh2connection = util.LaunchRemoteProcess(commandBuilder.toString(), node);

                if (ssh2connection == null) {
                    throw new Exception("JSCH channel not created");
                }

                // A new remote process launched. 
                // The corresponding Mnode created
                node.setChannel(ssh2connection);
                node.initialize();

            } catch (Exception e) {
                // connection failure
                System.err.println("MASS: error in connection to "
                        + node.getHostName() + " " + e);
                System.exit(-1);
            }
        }

        initializeThreads(getNumThreads());
        setInitialized(true);	// this node is now running

        // Synchronize with all slave processes
        for (MNode node : getRemoteNodes()) {

            if (printOutput == true) {
                System.err.println("init: wait for ack from "
                        + node.getHostName());
            }

            Message m = node.receiveMessage();
            ProvenanceRecorder.documentEntity(ProvUtils.getStoreOfCurrentThread(), null, null, new StringBuffer("init"), procRID, new StringBuffer("m"), m, new StringBuffer(m.getOwnerUUID()), null, true);

            if (m.getAction() != Message.ACTION_TYPE.ACK) {

                System.err.println("init didn't receive ack from rank "
                        + (node.getPid()) + " at "
                        + node.getHostName());
                System.exit(-1);
            }
        }
//        System.err.println("MASS.init: done");
        MASSProv.setStartTime();
        initialized = true;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("init"), procRID, null, null, null, null, true, false, false);
        if (MASSProv.provOn) {
            MASSProv.initRemoteHosts();
        }
        StopWatch.stop(false);
    }

    /**
     * Initialize the MASS library using arguments. Calling this method
     * effectively begins computation.
     *
     * @param args An array of command-line style arguments
     * @param nProc Unused - maintained only for compatibility with previous
     * versions. Now calculated from number of defined nodes.
     * @param nThr The number of threads to spawn on each node
     */
    public static void init(String[] args, int nProc, int nThr) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("init"), new StringBuffer("label"), true, new String[]{"args", "nProc", "nThr"}, new Object[]{args, nProc, nThr});

        // variable assignment
        setDefaultUsername(args[0]);
        setDefaultPassword(args[1]);
        setNodeFilePath(args[2]);
        setCommunicationPort(Integer.parseInt(args[3]));
        setNumThreads(nThr);
        //MASS.nProc = nProc;

        // after parameters have been set, perform initialization
        init();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("init"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the default password for connecting to remote nodes
     *
     * @param defaultPassword The default password
     */
    @Deprecated
    protected static void setDefaultPassword(String defaultPassword) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setDefaultPassword"), new StringBuffer("label"), true, new String[]{"defaultPassword"}, new Object[]{defaultPassword});
        MASS.defaultPassword = defaultPassword;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setDefaultPassword"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the default username for connecting to remote nodes
     *
     * @param defaultUsername The default login username
     */
    protected static void setDefaultUsername(String defaultUsername) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setDefaultUsername"), new StringBuffer("label"), true, new String[]{"defaultUsername"}, new Object[]{defaultUsername});
        MASS.defaultUsername = defaultUsername;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setDefaultUsername"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the filename for the cluster node definition file
     *
     * @param nodeFilePath The cluster node definition filename
     */
    public static void setNodeFilePath(String nodeFilePath) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setNodeFilePath"), new StringBuffer("label"), true, new String[]{"nodeFilePath"}, new Object[]{nodeFilePath});
        MASS.nodeFilePath = nodeFilePath;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setNodeFilePath"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the number of threads to spawn on each node
     *
     * @param numThreads The number of threads to spawn
     */
    public static void setNumThreads(int numThreads) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setNumThreads"), new StringBuffer("label"), true, new String[]{"numThreads"}, new Object[]{numThreads});

        if (numThreads >= 1) {
            MASS.numThreads = numThreads;
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setNumThreads"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected static int[] getLocalAgents() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getLocalAgents"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getLocalAgents"), procRID, new StringBuffer("LocalAgents"), LocalAgents, null, null, true, false, false);
        StopWatch.stop(false);
        return LocalAgents;
    }

    protected static void setLocalAgents(int[] values) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setLocalAgents"), new StringBuffer("label"), true, new String[]{"values"}, new Object[]{values});
        LocalAgents = values;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setLocalAgents"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void getRemoteAsyncResults() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getRemoteAsyncResults"), new StringBuffer("label"), true, null, null);

        if (!getRemoteNodes().isEmpty()) {
            LocalAgents = new int[getRemoteNodes().size()];
            getAsyncOutputThread().requestAsyncResults();
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getRemoteAsyncResults"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Change logger level
     *
     * @param level The logging level
     */
    public static void setLoggingLevel(LogLevel level) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setLoggingLevel"), new StringBuffer("label"), true, new String[]{"level"}, new Object[]{level});
        logger.setLogLevel(level);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setLoggingLevel"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * START MASS DEBUGGER METHODS
     */
    //MASS debugger variables
    public static final int DEBUGGER_HANDLE = 99;
    private static ObjectInputStream inputStream;
    private static ObjectOutputStream outputStream;
    private static ServerSocket socket;
    private static Socket client;
    private static int placesHandle = 0;
    static int agentsHandle = 0;

    public static void debugInit(int pHandle, int aHandle, int port) throws IOException {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("debugInit"), new StringBuffer("label"), true, new String[]{"pHandle", "aHandle", "port"}, new Object[]{pHandle, aHandle, port});
        //TODO - get rid of all params
        agentsHandle = aHandle;
        placesHandle = pHandle;

        //connect to GUI
        socket = new ServerSocket(port);
        client = socket.accept();
        outputStream = new ObjectOutputStream(client.getOutputStream());
        inputStream = new ObjectInputStream(client.getInputStream());

        //completely unnecessary, don't remove though!
        @SuppressWarnings("unused")
        MASSRequest request;

        try {
            //request = ( MASSRequest ) (( ObjectInputStream )inputStream ).readObject();
            request = (MASSRequest) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //end completely unnecessary stuff

        String placesName = null;
        String agentsName = null;
        Class<? extends Number> placeDataType = null;
        Class<? extends Number> agentDataType = null;
        boolean overloadsPlaceData = false;
        boolean overloadsAgentData = false;
        int x = 0;
        int y = 0;
        int numberOfAgents = 0;

        if (getPlaces(placesHandle)
                != null) {
            x = MASS.getPlaces(placesHandle).getSize()[0];
            y = MASS.getPlaces(placesHandle).getSize()[1];
            placesName = MASS.getPlaces(placesHandle).getPlaces()[0].getClass().getSimpleName();
            overloadsPlaceData = (MASS.getPlaces(placesHandle).getPlaces()[0].getDebugData() != null);
            if (overloadsPlaceData) {
                placeDataType = MASS.getPlaces(placesHandle).getPlaces()[0].getDebugData().getClass();
            }
        }

        if (getAgents(aHandle)
                != null) {
            numberOfAgents = MASS.getAgents(aHandle).getInitPopulation();
            agentsName = MASS.getAgents(aHandle).getAgents().get(0).getClass().getSimpleName();
            overloadsAgentData = (MASS.getAgents(aHandle).getAgents().get(0).getDebugData() != null);
            if (overloadsAgentData) {
                agentDataType = MASS.getAgents(aHandle).getAgents().get(0).getDebugData().getClass();
            }
        }

        InitialData iniData = new InitialData();

        iniData.setAgentsName(agentsName);

        iniData.setPlacesName(placesName);

        iniData.setPlacesX(x);

        iniData.setPlacesY(y);

        iniData.setNumberOfAgents(numberOfAgents);

        iniData.setNumberOfPlaces(x
                * y);
        iniData.setPlaceDataType(placeDataType);

        iniData.setAgentDataType(agentDataType);

        iniData.placeOverloadsGetDebugData(overloadsPlaceData);

        iniData.agentOverloadsGetDebugData(overloadsAgentData);

        //( (ObjectOutputStream )outputStream ).writeObject( iniData );
        outputStream.writeObject(iniData);

        outputStream.flush();

        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("debugInit"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(
                false);
    }

    public static void debugUpdate() throws IOException {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("debugUpdate"), new StringBuffer("label"), true, null, null);

        MASSRequest request = null;

        try {
            request = (MASSRequest) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        switch (request.getRequest()) {
            case INITIAL_DATA:
                //TODO - remove debugInit, handle from here
                break;
            case UPDATE_PACKAGE:
                sendUpdate();
                break;
            case INJECT_PLACE:
                injectPlace(request);
                break;
            case INJECT_AGENT:
                injectAgent(request);
                break;
            case TERMINATE:
                closeDebugConnection();
                break;
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("debugUpdate"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private static void injectPlace(MASSRequest request) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("injectPlace"), new StringBuffer("label"), true, new String[]{"request"}, new Object[]{request});
        PlaceData updates = (PlaceData) request.getPacket();
        Place place = MASS.getCurrentPlacesBase().getPlaces()[updates.getIndex()];

        place.setDebugData(updates.getThisPlaceData());

        try {
            outputStream.writeObject(new UpdatePackage());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("injectPlace"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private static void injectAgent(MASSRequest request) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("injectAgent"), new StringBuffer("label"), true, new String[]{"request"}, new Object[]{request});

        AgentData updates = (AgentData) request.getPacket();

        //fantastic complexity...
        for (int i = 0; i < MASS.getCurrentPlacesBase().getPlaces().length; i++) {
            for (int j = 0; j < MASS.getCurrentPlacesBase().getPlaces()[i].getAgents().size(); j++) {
                Set<Agent> agents = MASS.getCurrentPlacesBase().getPlaces()[i].getAgents();
                for (Agent agent : agents) {
                    if (updates.getId() == agent.getAgentId()) {
                        agent.setDebugData(updates.getDebugData());
                    }
                }
            }
        }
        try {
            // ( ObjectOutputStream )outputStream ).writeObject( new UpdatePackage() );
            outputStream.writeObject(new UpdatePackage());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("injectAgent"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private static void closeDebugConnection() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("closeDebugConnection"), new StringBuffer("label"), true, null, null);
        try {
            //todo - send null MASSPackage back first to prevent blocking
            outputStream.close();
            inputStream.close();
            client.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //( ( ObjectOutputStream )outputStream ).writeObject( new UpdatePackage() );
            outputStream.writeObject(new UpdatePackage());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("closeDebugConnection"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private static void sendUpdate() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("sendUpdate"), new StringBuffer("label"), true, null, null);
        Place[] places = MASS.getCurrentPlacesBase().getPlaces();
        //Place[] places = MASSBase.getPlaces(placesHandle).getPlaces();
        //System.out.println(places.length);
        PlaceData[] updatedPlaces = new PlaceData[places.length];

        AgentData[] agentDataArr;

        for (int i = 0; i < places.length; i++) {
            Number placeData = places[i].getDebugData();

            //if (placeData == null) System.out.println("placeData == null");
            Set<Agent> agents = places[i].getAgents();
            int j = 0;
            agentDataArr = new AgentData[agents.size()];

            for (Agent agent : agents) {
                agentDataArr[j] = new AgentData();
                agentDataArr[j].setDebugData(agent.getDebugData());
                agentDataArr[j].setChildren(agent.getNewChildren());
                agentDataArr[j].setId(agent.getAgentId());
                agentDataArr[j].setIsAlive(agent.isAlive());
                agentDataArr[j].setIndex(i);
                j++;
            }

            updatedPlaces[i] = new PlaceData();
            updatedPlaces[i].setAgentDataOnThisPlace(agentDataArr);
            updatedPlaces[i].setThisPlaceData(placeData);
            updatedPlaces[i].setHasAgents(agents.size() != 0);
            //updatedPlaces[i] = new PlaceData( placeData, i, agents.size() != 0, agentDataArr );
        }

        UpdatePackage newPackage = new UpdatePackage();
        newPackage.setPlaceData(updatedPlaces);

        //write package
        try {
            //( ( ObjectOutputStream )outputStream ).writeObject( newPackage );
            outputStream.writeObject(newPackage);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("sendUpdate"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void initMASSProv() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initMASSProv"), new StringBuffer("label"), true, null, null);
        // Only attempt init work if remote hosts are not initialized
        if (initialized && StoreManager.isInitialized()) {
            // empty message
            Message m = new Message();
            // indicate this is a MASSPROV_INITIALIZATION message
            m.setAction(Message.ACTION_TYPE.MASSPROV_INITIALIZE);
            // package state of StoreManage fields
            Object[] storeMgrMembers = StoreManager.provideStateDataForMessage();
            // set field data as message argument
            m.setArgument(storeMgrMembers);
            // send message to nodes
            for (MNode node : getRemoteNodes()) {
                // send message to node
                node.sendMessage(m);
            }
            // Synchronize with all slaves
            barrierAllSlaves();
        } else { // log the problem
            String msg = "";
            if (!initialized) {
                msg += "Warning: Attempt to initialize MASSProv on remote "
                        + "hosts prior to remote host initialization";
            }
            if (!StoreManager.isInitialized()) {
                msg += "Warning: Attempt to access initialize remote "
                        + "StoreManagers before initializing local StoreManager";
            }
            IO.log(msg);
            System.err.println(msg);
            try { // record the stack trace in case method wasn't called from app main
                throw new Exception("InitializationOrderException");
            } catch (Exception e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initMASSProv"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void toggleProvenanceCapture(boolean provOn) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initMASSProv"), new StringBuffer("label"), true, null, null);
        // Only attempt init work if remote hosts are not initialized
        if (initialized && StoreManager.isInitialized()) {
            // empty message
            Message m = new Message();
            // indicate this is a MASSPROV_INITIALIZATION message
            m.setAction(Message.ACTION_TYPE.TOGGLE_PROVENANCE_CAPTURE);
            // toggle value
            Object arg = (Boolean) provOn;
            // set provOn flag value as message argument
            m.setArgument(arg);
            // send message to nodes
            for (MNode node : getRemoteNodes()) {
                // send message to node
                node.sendMessage(m);
            }
            // Synchronize with all slaves
            barrierAllSlaves();
            // change prov capture on this (master) node
            MASSProv.provOn = provOn;
        } else { // log the problem
            String msg = "";
            if (!initialized) {
                msg += "Warning: Attempt to set provenance capture on remote "
                        + "hosts prior to remote host initialization";
            }
            if (!StoreManager.isInitialized()) {
                msg += "Warning: Attempt to set provenance capture on remote hosts "
                        + " before initializing local StoreManager";
            }
            IO.log(msg);
            System.err.println(msg);
            try { // record the stack trace in case method wasn't called from app main
                throw new Exception("ProvenanceToggleOrderException");
            } catch (Exception e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("initMASSProv"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void measureHostDelay() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("measureHostDelay"), new StringBuffer("label"), true, null, null);

        int counter = 0;
        // send message to nodes
        for (MNode node : getRemoteNodes()) {
            // empty message
            Message m = new Message();

            // indicate this is a ACK message
            m.setAction(Message.ACTION_TYPE.MEASURE_HOST_DELAY);

            // set field data as ns of system
            m.setArgument(System.nanoTime());
            ProvenanceRecorder.documentEntity(ProvUtils.getStoreOfCurrentThread(), null, null, new StringBuffer("measureHostDelay"), procRID, new StringBuffer("mSent"), m, new StringBuffer(m.getOwnerUUID()), null, true);

            Long timeBeforeSendMsg = System.nanoTime();

            // send message to node
            node.sendMessage(m);
            String m_RID = ProvUtils.getUniversalResourceID("mS" + counter);
            ProvenanceRecorder.documentEntity(ProvUtils.getStoreOfCurrentThread(), null, null, new StringBuffer("measureHostDelay"), procRID, new StringBuffer("mS"), m, new StringBuffer(m_RID), null, true);

            Message mRecieved = node.receiveMessage();
            String m2_RID = ProvUtils.getUniversalResourceID("mR" + counter);
            ProvenanceRecorder.documentEntity(ProvUtils.getStoreOfCurrentThread(), null, null, new StringBuffer("measureHostDelay"), procRID, new StringBuffer("mR"), mRecieved, new StringBuffer(m2_RID), null, true);

            if (ProvUtils.getStoreOfCurrentThread() != null) {
                ProvUtils.getStoreOfCurrentThread().addRelationalProv(new StringBuffer(m2_RID), ProvOntology.getAssociationQualifiedClassFullURIBuffer(), new StringBuffer(m_RID));
                ProvUtils.getStoreOfCurrentThread().addRelationalProv(new StringBuffer(m_RID), ProvOntology.getAssociationQualifiedClassFullURIBuffer(), new StringBuffer(m2_RID));
                ProvUtils.getStoreOfCurrentThread().addRelationalProv(new StringBuffer(m_RID), ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(), new StringBuffer(m.getOwnerUUID()));
                ProvUtils.getStoreOfCurrentThread().addRelationalProv(new StringBuffer(m2_RID), ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(), new StringBuffer(mRecieved.getOwnerUUID()));

                String node_RID = ProvUtils.getUniversalResourceID("node" + counter);
                ProvUtils.getStoreOfCurrentThread().addRelationalProv(new StringBuffer(mRecieved.getOwnerUUID()), ProvOntology.getAssociationQualifiedClassFullURIBuffer(), new StringBuffer(node_RID));
                ProvUtils.getStoreOfCurrentThread().addRelationalProv(new StringBuffer(m.getOwnerUUID()), ProvOntology.getAssociationQualifiedClassFullURIBuffer(), new StringBuffer(node_RID));
                ProvUtils.getStoreOfCurrentThread().addRelationalProv(new StringBuffer(m_RID), ProvOntology.getAssociationQualifiedClassFullURIBuffer(), new StringBuffer(node_RID));
                ProvUtils.getStoreOfCurrentThread().addRelationalProv(new StringBuffer(m2_RID), ProvOntology.getAssociationQualifiedClassFullURIBuffer(), new StringBuffer(node_RID));
            }
            counter++;

            //check if message recieved was an ack, log if not
            if (mRecieved.getAction() != Message.ACTION_TYPE.ACK) {
                System.err.println("sendAckWaitForAck didn't receive ack at "
                        + node.getHostName()
                        + " message action type = " + mRecieved.getAction());
                System.exit(-1);
            } else {
                IO.log(System.nanoTime() - timeBeforeSendMsg + " ns round trip time from " + ProvUtils.getHostName() + " to " + node.getHostName() + " and back ");
            }

        }

        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("measureHostDelay"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * END MASS DEBUGGER METHODS
     */
}
