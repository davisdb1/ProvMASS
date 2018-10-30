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

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.ProvOntology;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import edu.uw.bothell.css.dsl.MASS.prov.core.logging.Log4J2Logger;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.store.StoreManager;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MProcess exists to facilitate message-passing between remote and master
 * nodes.
 */
public class MProcess {

    private int myPid; // my pid or rank
    private ObjectInputStream MAIN_IOS; // input from the master process
    private ObjectOutputStream MAIN_OOS; // output to the master process
    private ProvenanceStore store; // stores provenance
    // logging
    private Log4J2Logger logger = Log4J2Logger.getInstance();

    /**
     * Main MASS function that launches MProcess
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("main"), new StringBuffer("label"), true, new String[]{"args"}, new Object[]{args});
        try {
            String hostName = args[0];
            int myPid = Integer.parseInt(args[1]);
            int nProc = Integer.parseInt(args[2]);
            int nThreads = Integer.parseInt(args[3]);
            int serverPort = Integer.parseInt(args[4]);
            // work without a masshome
            String curDir = null;
            if (args.length >= 6) {
                curDir = args[5];
            }
            MProcess mprocess = new MProcess(hostName, myPid, nProc, nThreads,
                    serverPort, curDir);
            mprocess.start();
        } catch (Exception e) {
            edu.uw.bothell.css.dsl.MASS.prov.IO.IO.log("Error occurred while launching MProcess on " + ProvUtils.getHostName());
            e.printStackTrace(edu.uw.bothell.css.dsl.MASS.prov.IO.IO.getLogWriter());
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("main"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * MProcesses are the MASS threads executing on various machines. They are
     * responsible for maintaining some number of the total Places being used by
     * the entire MASS program, as well as the associated Agents. Each MProcess
     * is referred to by its rank.
     *
     * @param hostName
     * @param myPid
     * @param nProc
     * @param nThr
     * @param port
     * @param curDir
     */
    public MProcess(String hostName, int myPid, int nProc, int nThr, int port,
            String curDir) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("MProcess"), new StringBuffer("label"), true, new String[]{"hostName", "myPid", "nProc", "nThr", "port", "curDir"}, new Object[]{hostName, myPid, nProc, nThr, port, curDir});
        // this.hostName = hostName;
        this.myPid = myPid;
        // this.nProc = nProc;
        MASS.setNumThreads(nThr);
        MASSBase.setWorkingDirectory(curDir); // mprocess manually changes it.
        MASSBase.initMASSBase(hostName, myPid, nProc, port);

        logger.debug("Launching MProcess... (" + "hostname = " + hostName
                + "), myPid = " + myPid + "), nProc = " + nProc + "), nThr = " + nThr
                + "), port = " + port + "), curDir = " + curDir + ")");

        MASSBase.initializeThreads(MASS.getNumThreads());
        // set up a connection with the master process
        try {
            MAIN_IOS = new ObjectInputStream(System.in);
            MAIN_OOS = new ObjectOutputStream(System.out);
        } catch (Exception e) {
            logger.error("MProcess.Mprocess: detected ", e);
            System.exit(-1);
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("MProcess"), procRID, null, null, null, null, true, false, false);
            StopWatch.stop(false);
        }
    }

    Message receiveMessage() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("receiveMessage"), new StringBuffer("label"), true, null, null);
        try {
            Message m = (Message) MAIN_IOS.readObject();
            ProvenanceRecorder.documentEntity(ProvUtils.getStoreOfCurrentThread(), null, null, new StringBuffer("receiveMessage"), procRID, new StringBuffer("m"), m, new StringBuffer(m.getOwnerUUID()), null, true);
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("receiveMessage"), procRID, new StringBuffer("m"), m, null, null, true, false, false);
            return m;
        } catch (Exception e) {
            logger.error("MProcess.receiveMessage: detected ", e);
            System.exit(-1);
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("receiveMessage"), procRID, new StringBuffer("null"), null, null, null, true, false, false);
        StopWatch.stop(false);
        return null;

    }

    private void sendAck() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendAck"), new StringBuffer("label"), true, null, null);
        Message msg = new Message(Message.ACTION_TYPE.ACK);
        ProvenanceRecorder.documentEntity(ProvUtils.getStoreOfCurrentThread(), null, null, new StringBuffer("sendAck"), procRID, new StringBuffer("m"), msg, new StringBuffer(msg.getOwnerUUID()), null, true);
        sendMessage(msg);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendAck"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private void sendAckWithArgs() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendAck"), new StringBuffer("label"), true, null, null);
        Message msg = new Message(Message.ACTION_TYPE.ACK);
        msg.setArgument(System.nanoTime());
        ProvenanceRecorder.documentEntity(ProvUtils.getStoreOfCurrentThread(), null, null, new StringBuffer("sendAck"), procRID, new StringBuffer("m"), msg, new StringBuffer(msg.getOwnerUUID()), null, true);
        sendMessage(msg);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendAck"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private void sendAck(int localPopulation) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendAck"), new StringBuffer("label"), true, new String[]{"localPopulation"}, new Object[]{localPopulation});

        Message msg = new Message(Message.ACTION_TYPE.ACK, localPopulation);
        // if( printOutput ) {
        // MASS_base.log( "msg.getAgentPopulation = " +
        // msg.getAgentPopulation( ) );
        // }
        sendMessage(msg);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendAck"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private void sendMessage(Message msg) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendMessage"), new StringBuffer("label"), true, new String[]{"msg"}, new Object[]{msg});

        try {

            MAIN_OOS.writeObject(msg);
            MAIN_OOS.flush();

        } catch (Exception e) {

            logger.error("MProcess.sendMessage: " + e);
            System.exit(-1);

        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendMessage"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private void sendReturnValues(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendReturnValues"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument});
        Message msg = new Message(Message.ACTION_TYPE.ACK, argument);
        sendMessage(msg);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendReturnValues"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private void sendReturnValues(Object argument, int localPopulation) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendReturnValues"), new StringBuffer("label"), true, new String[]{"argument", "localPopulation"}, new Object[]{argument, localPopulation});
        Message msg = new Message(Message.ACTION_TYPE.ACK, argument,
                localPopulation);
        sendMessage(msg);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendReturnValues"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    @SuppressWarnings("incomplete-switch")
    public void start() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("start"), new StringBuffer("label"), true, null, null);

        logger.debug("MProcess started");

        // Synchronize with the master node first.
        sendAck();

        boolean alive = true;
        while (alive) {

            // receive a new message from the master
            Message m = receiveMessage();

            // if ( printOutput )
            // MASS_base.log( "A new message received: action = " +
            // m.getAction( ) );
            // get prepared for the following arguments for PLACES_INITIALIZE
            int[] size; // size[]
            Vector<String> hosts = new Vector<String>();
            Object argument = null;
            PlacesBase places = null; // new Places
            AgentsBase agents = null; // new Agents

            // retrieve an argument
            argument = m.getArgument();

            switch (m.getAction()) {
                case ACK:
                    handleMsgAck();
                    break;
                case EMPTY:
                    handleMsgEmpty();
                    break;
                case FINISH:
                    alive = handleMsgFinish();
                    break;
                case PLACES_INITIALIZE:
                    handleMsgPlacesInitialize(m, argument, hosts);
                    break;
                case PLACES_CALL_ALL_VOID_OBJECT:
                    handleMsgPlacesCallAllVoidObject(m, argument);
                    break;
                case PLACES_CALL_ALL_RETURN_OBJECT:
                    handleMsgPlacesCallAllReturnObject(m, argument);
                    break;
                case PLACES_EXCHANGE_ALL:
                    handleMsgPlacesExchangeAll(m);
                    break;
                case PLACES_EXCHANGE_BOUNDARY:
                    handleMsgPlacesExchangeBoundary(m);
                    break;
                case PLACES_EXCHANGE_ALL_REMOTE_REQUEST:
                case PLACES_EXCHANGE_ALL_REMOTE_RETURN_OBJECT:
                case PLACES_EXCHANGE_BOUNDARY_REMOTE_REQUEST:
                    break;
                case AGENTS_INITIALIZE:
                    handleMsgAgentsInitialize(m, argument);
                    break;
                case AGENTS_CALL_ALL_VOID_OBJECT:
                    handleMsgAgentsCallAllVoidObject(m, argument);
                    break;
                case AGENTS_CALL_ALL_RETURN_OBJECT:
                    handleMsgAgentsCallAllReturnObject(m, argument);
                    break;
                case AGENTS_MANAGE_ALL:
                    handleMsgAgentsManageAll(m);
                    break;
                case AGENTS_CALL_ALL_ASYNC_RETURN_OBJECT:
                    handleMessageCallAllAsyncReturnObject(m, argument);
                    break;
                case MASSPROV_INITIALIZE:
                    handleMsgMASSProvInitialize(m, argument);
                    break;
                case TOGGLE_PROVENANCE_CAPTURE:
                    handleMsgToggleProvenanceCapture(m, argument);
                    break;
                case MEASURE_HOST_DELAY:
                    handleMsgMeasureHostDelay();
                    break;
            }

        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("start"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private void handleMessageCallAllAsyncReturnObject(Message m, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMessageCallAllAsyncReturnObject"), new StringBuffer("label"), true, new String[]{"m", "argument"}, new Object[]{m, argument});
        logger.debug("AGENTS_CALL_ALL_ASYNC_RETURN_OBJECT received");
        MASSBase.prepareAsyncExecution(MASSBase.getAgentsMap().get(
                new Integer(m.getHandle())), m.getFunctionIds());
        Object[] arguments = (Object[]) argument;
        int[] autoMigrationStartIndices = m.getAutoMigrationStartingIndex();
        for (int i = 0; i < MASSBase.getCurrentAgentsBase().asyncAgentIdListSize(); i++) {
            if (arguments != null) {
                MASSBase.getCurrentAgentsBase().getAgents()
                        .get(MASSBase.getCurrentAgentsBase().asyncAgentIdListGet(i)).setAsyncArgument(arguments[i]);
            }
            if (autoMigrationStartIndices != null) {
                MASSBase.getCurrentAgentsBase().getAgents()
                        .get(MASSBase.getCurrentAgentsBase().asyncAgentIdListGet(i)).setAutoMigrationStartingIndex(i);
            }
        }
        if (!MASSBase.getCurrentAgentsBase().asyncAgentIdListIsEmpty()) {
            MASSBase.setSourceAgentPid(0); // Need to notify Master
            MASSBase.getInAsyncAgents()[0] += MASSBase.getCurrentAgentsBase().asyncAgentIdListSize();
        } else {
            MASSBase.setSourceAgentPid(-1);
        }
        // MASS_base.setCurrentReturns(new
        // Object[MASS_base.getCurrentAgents().getLocalPopulation()]); //
        // prepare an entire return space
        // resume threads
        logger.debug("MASS_base.currentgAgents = {}", MASSBase.getCurrentAgentsBase());
        logger.debug("MASS_base.getCurrentgAgents = {}", MASSBase.getCurrentAgentsBase());
        do {

            // Mark myself as busy processing my async queue
            MASSBase.getCurrentAgentsBase().setIsAsyncLoopIdle(false);
            logger.debug("begin callAllAsync loop");

            // resume threads to work on call all
            MThread.resumeThreads(MThread.STATUS_TYPE.STATUS_AGENTSCALLALL_ASYNC);
            try {
                MASSBase.getCurrentAgentsBase().callAllAsync(0);
            } catch (Exception e) {
                logger.error("Unknown exception sending async callAll", e);
            }

            // confirm all threads are done with agents.callAllAsync
            // tell master that I'm done
            synchronized (MASSBase.getCurrentAgentsBase().getAsyncAgentIdList()) {
                logger.debug("output idle = "
                        + MASSBase.getAsyncOutputThread().isIdle()
                        + "), input idle = "
                        + MASSBase.getAsyncInputThread().isIdle(false)
                        + "), output migrate set is empty = "
                        + MASSBase.getChildAgentPids().isEmpty());
                while ((MASSBase.getCurrentAgentsBase().asyncAgentIdListIsEmpty() && MASSBase
                        .getCurrentAgentsBase().hasNoInprocessAgents())
                        && (!MASSBase.getAsyncOutputThread().isIdle()
                        || !MASSBase.getAsyncInputThread().isIdle(false)
                        || !MASSBase.getChildAgentPids().isEmpty())) {
                    try {
                        MASSBase.getCurrentAgentsBase().getAsyncAgentIdList().wait();
                    } catch (InterruptedException e) {
                    }
                }

                // I'm done with my async queue and agents migration
                MASSBase.getCurrentAgentsBase().setIsAsyncLoopIdle(true);

                // Notify master that I am done
                /*
                * When an agent is started to run it is taken out from the async queue
                * However, an agent might still be running even if it is removed from the queue,
                therefore, we need to make sure if there is any agent is running.
                * When an agent stops running,  inProcessAgentCount variable is decreased by one
                in AgentsBase.java
                 */
                if (MASSBase.getCurrentAgentsBase().asyncAgentIdListIsEmpty()
                        && MASSBase.getCurrentAgentsBase().hasNoInprocessAgents()
                        && MASSBase.getChildAgentPids().isEmpty()
                        && MASSBase.getSourceAgentPid() > -1) {
                    MASSBase.getAsyncOutputThread()
                            .notifySourceOfCompleteness(
                                    MASSBase.getInAsyncAgents()[MASSBase
                                    .getSourceAgentPid()]);
                }

                while ((!MASSBase.getCurrentAgentsBase().getResultRequestFromMaster()
                        && MASSBase.getCurrentAgentsBase().asyncAgentIdListIsEmpty() && MASSBase
                        .getCurrentAgentsBase().hasNoInprocessAgents())) {
                    logger.debug("After notifying Master: "
                            + !MASSBase.getCurrentAgentsBase()
                                    .getResultRequestFromMaster() + " && "
                            + MASSBase.getCurrentAgentsBase().asyncAgentIdListIsEmpty());
                }
                try {
                    MASSBase.getCurrentAgentsBase().getAsyncAgentIdList().wait();
                } catch (InterruptedException e) {
                }
            }
            logger.debug("end of callAllAsync loop: "
                    + !MASSBase.getCurrentAgentsBase().getResultRequestFromMaster());

            // Mthread.barrierThreads(0);
        } while (!MASSBase.getCurrentAgentsBase().getResultRequestFromMaster());
        logger.debug("barrier done callAll_ASync");
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMessageCallAllAsyncReturnObject"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return;
    }

    private void handleMsgAgentsManageAll(Message m) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgAgentsManageAll"), new StringBuffer("label"), true, new String[]{"m"}, new Object[]{m});
        logger.debug("AGENTS_MANAGE_ALL received");
        MASSBase.setCurrentAgentsBase(MASSBase.getAgentsMap().get(
                new Integer(m.getHandle())));
        MThread.setAgentBagSize(MASSBase.getCurrentAgentsBase().getAgents()
                .size_unreduced());
        MThread.resumeThreads(MThread.STATUS_TYPE.STATUS_MANAGEALL);
        MASSBase.getCurrentAgentsBase().manageAll(0); // 0 = the main tid
        // confirm all threads are done with agents.manageAll.
        MThread.barrierThreads(0);
        logger.debug("sendAck will send localPopulation = {}", MASSBase.getCurrentAgentsBase().getLocalPopulation());
        sendAck(MASSBase.getCurrentAgentsBase().getLocalPopulation());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgAgentsManageAll"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return;
    }

    private void handleMsgAgentsCallAllReturnObject(Message m, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgAgentsCallAllReturnObject"), new StringBuffer("label"), true, new String[]{"m", "argument"}, new Object[]{m, argument});
        logger.debug("AGENTS_CALL_ALL_RETURN_OBJECT received");
        MASSBase.setCurrentAgentsBase(MASSBase.getAgentsMap().get(
                new Integer(m.getHandle())));
        MASSBase.setCurrentFunctionId(m.getFunctionId());
        MASSBase.setCurrentArgument(argument);
        MASSBase.setCurrentMsgType(m.getAction());
        MASSBase.setCurrentReturns(new Object[MASSBase.getCurrentAgentsBase()
                .getLocalPopulation()]);
        MThread.setAgentBagSize(MASSBase.getCurrentAgentsBase().getAgents()
                .size_unreduced());
        // resume threads to work on call all with return objects
        MThread.resumeThreads(MThread.STATUS_TYPE.STATUS_AGENTSCALLALL);
        MASSBase.getCurrentAgentsBase().callAll(MASSBase.getCurrentFunctionId(),
                (Object[]) (MASSBase.getCurrentArgument()), 0);
        // confirm all threads are done with agnets.callAll with
        // return objects
        MThread.barrierThreads(0);
        logger.debug("barrier done");
        sendReturnValues(MASSBase.getCurrentReturns(), MASSBase
                .getCurrentAgentsBase().getLocalPopulation());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgAgentsCallAllReturnObject"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return;
    }

    private void handleMsgAgentsCallAllVoidObject(Message m, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgAgentsCallAllVoidObject"), new StringBuffer("label"), true, new String[]{"m", "argument"}, new Object[]{m, argument});
        logger.debug("AGENTS_CALL_ALL_VOID_OBJECT received");
        MASSBase.setCurrentAgentsBase(MASSBase.getAgentsMap().get(
                new Integer(m.getHandle())));
        MASSBase.setCurrentFunctionId(m.getFunctionId());
        MASSBase.setCurrentArgument(argument);
        MASSBase.setCurrentMsgType(m.getAction());
        MThread.setAgentBagSize(MASSBase.getCurrentAgentsBase().getAgents()
                .size_unreduced());
        // resume threads to work on call all
        MThread.resumeThreads(MThread.STATUS_TYPE.STATUS_AGENTSCALLALL);
        MASSBase.getCurrentAgentsBase().callAll(m.getFunctionId(), argument, 0);
        // confirm all threads are done with agents.callAll
        MThread.barrierThreads(0);
        logger.debug("barrier done");
        sendAck(MASSBase.getCurrentAgentsBase().getLocalPopulation());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgAgentsCallAllVoidObject"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return;
    }

    private void handleMsgAgentsInitialize(Message m, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgAgentsInitialize"), new StringBuffer("label"), true, new String[]{"m", "argument"}, new Object[]{m, argument});
        AgentsBase agents;
        logger.debug("AGENTS_INITIALIZE received");
        agents = new AgentsBase(m.getHandle(), m.getClassname(), argument,
                m.getDestHandle(), m.getAgentPopulation());
        MASSBase.getAgentsMap().put(new Integer(m.getHandle()), agents);
        sendAck(agents.getLocalPopulation());
        logger.debug("AGENTS_INITIALIZE completed and ACK sent");
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgAgentsInitialize"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return;
    }

    private void handleMsgPlacesExchangeBoundary(Message m) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgPlacesExchangeBoundary"), new StringBuffer("label"), true, new String[]{"m"}, new Object[]{m});
        logger.debug("PLACES_EXCHANGE_BOUNDARY received handle = {}", m.getHandle());
        // retrieve the corresponding places
        MASSBase.setCurrentPlacesBase(MASSBase.getPlacesMap().get(new Integer(m.getHandle())));
        // for debug
        MASSBase.showHosts();
        // exchange boundary implementation
        MASSBase.getCurrentPlacesBase().exchangeBoundary();
        sendAck();
        logger.debug("PLACES_EXCHANGE_BOUNDARY completed and ACK sent");
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgPlacesExchangeBoundary"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return;
    }

    private void handleMsgPlacesExchangeAll(Message m) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgPlacesExchangeAll"), new StringBuffer("label"), true, new String[]{"m"}, new Object[]{m});
        logger.debug("PLACES_EXCHANGE_ALL recweived handle = "
                + m.getHandle() + " dest_handle = " + m.getDestHandle());
        // retrieve the corresponding places
        MASSBase.setCurrentPlacesBase(MASSBase.getPlacesMap().get(
                new Integer(m.getHandle())));
        MASSBase.setDestinationPlaces(MASSBase.getPlacesMap().get(
                new Integer(m.getDestHandle())));
        MASSBase.setCurrentFunctionId(m.getFunctionId());
        // MASS_base.currentDestinations = m.getDestinations( );
        // reset requestCounter by the main thread
        MASSBase.resetRequestCounter();
        // for debug
        MASSBase.showHosts();
        // resume threads to work on call all.
        MThread.resumeThreads(MThread.STATUS_TYPE.STATUS_EXCHANGEALL);
        // exchangeall implementation
        MASSBase.getCurrentPlacesBase().exchangeAll(
                MASSBase.getDestinationPlaces(), MASSBase.getCurrentFunctionId(),
                0);
        // confirm all threads are done with places.exchangeall.
        MThread.barrierThreads(0);
        logger.debug("barrier done");
        sendAck();
        logger.debug("PLACES_EXCHANGE_ALL sent ACK");
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgPlacesExchangeAll"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return;
    }

    private void handleMsgPlacesCallAllReturnObject(Message m, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgPlacesCallAllReturnObject"), new StringBuffer("label"), true, new String[]{"m", "argument"}, new Object[]{m, argument});
        logger.debug("PLACES_CALL_ALL_RETURN_OBJECT received");
        // retrieve the corresponding places
        MASSBase.setCurrentPlacesBase(MASSBase.getPlacesMap().get(
                new Integer(m.getHandle())));
        MASSBase.setCurrentFunctionId(m.getFunctionId());
        MASSBase.setCurrentArgument(argument);
        MASSBase.setCurrentMsgType(m.getAction());
        MASSBase.setCurrentReturns(new Object[MASSBase.getCurrentPlacesBase()
                .getPlacesSize()]);
        // resume threads to work on call all.
        MThread.resumeThreads(MThread.STATUS_TYPE.STATUS_CALLALL);
        // 3rd arg: 0 = the main thread id
        MASSBase.getCurrentPlacesBase().callAll(MASSBase.getCurrentFunctionId(),
                (Object[]) (MASSBase.getCurrentArgument()),
                ((Object[]) (MASSBase.getCurrentArgument())).length, 0);
        // confirm all threads are done with places.callAll w/ return
        MThread.barrierThreads(0);
        sendReturnValues(MASSBase.getCurrentReturns());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgPlacesCallAllReturnObject"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return;
    }

    private void handleMsgPlacesCallAllVoidObject(Message m, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgPlacesCallAllVoidObject"), new StringBuffer("label"), true, new String[]{"m", "argument"}, new Object[]{m, argument});
        logger.debug("PLACES_CALL_ALL_VOID_OBJECT received");
        // retrieve the corresponding places
        MASSBase.setCurrentPlacesBase(MASSBase.getPlacesMap().get(
                new Integer(m.getHandle())));
        MASSBase.setCurrentFunctionId(m.getFunctionId());
        MASSBase.setCurrentArgument(argument);
        MASSBase.setCurrentMsgType(m.getAction());
        // resume threads to work on call all.
        MThread.resumeThreads(MThread.STATUS_TYPE.STATUS_CALLALL);
        // 3rd arg: 0 = the main thread id
        MASSBase.getCurrentPlacesBase().callAll(m.getFunctionId(), argument, 0);
        // confirm all threads are done with places.callAll
        MThread.barrierThreads(0);
        sendAck();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgPlacesCallAllVoidObject"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return;
    }

    private void handleMsgPlacesInitialize(Message m, Object argument, Vector<String> hosts) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgPlacesInitialize"), new StringBuffer("label"), true, new String[]{"m", "argument", "hosts"}, new Object[]{m, argument, hosts});
        int[] size;
        PlacesBase places;
        logger.debug("PLACES_INITIALIZE received");
        // create a new Places
        size = m.getSize();
        places = new PlacesBase(m.getHandle(), m.getClassname(),
                m.getBoundaryWidth(), argument, size);
        for (int i = 0; i < m.getHosts().size(); i++) {
            hosts.add(m.getHosts().get(i));
        }
        // establish all inter-node connections within setHosts( )
        MASSBase.setHosts(hosts);
        MASSBase.getPlacesMap().put(new Integer(m.getHandle()), places);
        sendAck();
        logger.debug("PLACES_INITIALIZE completed and ACK sent");
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgPlacesInitialize"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return;
    }

    private boolean handleMsgFinish() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgFinish"), new StringBuffer("label"), true, null, null);
        boolean alive;
        if (MASSProv.isInitialized()) {
            // persist remaining provenance
            StoreManager sm = StoreManager.getStoreManager();
            sm.persistAllStores();
            if (MASSProv.shouldPostProcess()) {
                sm.postProcessPersistedStores();
            }
//            sm.amalgamateAndCopyPreprocessedProvenance("/tmp/MASSProvData/"
//                    + ProvUtils.getHostName() + "_allPreProcProv.txt"),
//                    ProvUtils.getHostName() + "_allPreProcProv.txt");
            // sm.deletePreProcessedProvenanceDirectorysMASSProvParent(); // moved
        }
        MThread.resumeThreads(MThread.STATUS_TYPE.STATUS_TERMINATE);
        // confirm all threads are done with finish
        MThread.barrierThreads(0);
        MASSBase.getExchange().terminateConnection(this.myPid);
        MASSBase.getAsyncOutputThread().finish();
        MASSBase.getAsyncInputThread().finish();
        // sendAck();
        sendFinishAck();
        alive = false;
        // if( printOutput )
        // MASS_base.log( "FINISH received and ACK sent" );
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgFinish"), procRID, new StringBuffer("alive"), alive, null, null, true, false, false);
        if (MASSProv.isInitialized()) {
            StoreManager sm = StoreManager.getStoreManager();
            String massprovparent = sm.getPreProcessedProvenanceDirectorysMASSProvParent();
            IO.log("Deleting " + massprovparent);
            try {
                Runtime.getRuntime().exec("nohup rm -rf " + massprovparent);
            } catch (IOException ex) {
                Logger.getLogger(MProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        StopWatch.stop(false);
        return alive;

    }

    private void handleMsgEmpty() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgEmpty"), new StringBuffer("label"), true, null, null);
        if (MASS.isConsoleLoggingEnabled()) {
            logger.debug("EMPTY received!!!!");
        }
        sendAck();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgEmpty"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private void handleMsgAck() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgAck"), new StringBuffer("label"), true, null, null);
        sendAck();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgAck"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private void handleMsgMeasureHostDelay() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgMeasureHostDelay"), new StringBuffer("label"), true, null, null);
        sendAckWithArgs();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgMeasureHostDelay"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private void handleMsgMASSProvInitialize(Message m, Object argument) {
        StopWatch.start(false);
        //StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgMASSProvInitialize"), new StringBuffer("label"), true, new String[]{"m"), new StringBuffer("argument"}, new Object[]{m, argument});
        MASSProv.provOn = true;
        MASSProv.remoteInitialized();
        // set collector class and buffer sizes
        StoreManager.consumeMessageStateData((Object[]) argument);
        StoreManager sm = StoreManager.getStoreManager();
        ProvenanceStore store = ProvUtils.getStoreOfCurrentThread();
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(store, this, new StringBuffer("handleMsgMASSProvInitialize"), new StringBuffer("label"), true, new String[]{"m", "argument"}, new Object[]{m, argument});
        StringBuffer classRID = ProvUtils.getGlobalResourceID(new StringBuffer("MProcess"));
        store.addRelationalProv(classRID,
                ProvOntology.getRDFTypeFullURIBuffer(),
                ProvOntology.getSoftwareAgentExpandedClassFullURIBuffer());
        StringBuffer methodRID = ProvUtils.getGlobalResourceID(new StringBuffer("handleMsgMASSProvInitialize"));
        store.addRelationalProv(methodRID,
                ProvOntology.getRDFTypeFullURIBuffer(),
                ProvOntology.getActivityStartingPointClassFullURIBuffer());
        store.addRelationalProv(methodRID,
                ProvOntology.getWasStartedByExpandedPropertyFullURIBuffer(),
                classRID);
        sendAck();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("handleMsgMASSProvInitialize"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Sends performance data along with normal acknowledgment (see sendAck())
     */
    private void sendFinishAck() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendFinishAck"), new StringBuffer("label"), true, null, null);
        Message msg = new Message(Message.ACTION_TYPE.ACK);
        if (MASSProv.provOn) {
            msg.setArgument(StopWatch.packageDurationTotals());
        }
        sendMessage(msg);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendFinishAck"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private void handleMsgToggleProvenanceCapture(Message m, Object argument) {
        StopWatch.start(false);
        if (MASSProv.isInitialized()) {
            if (argument instanceof Boolean) {
                MASSProv.provOn = (boolean) argument;
            } else {
                IO.log("message argument in handleMsgToggleProvenanceCapture was not an instanceof Boolean");
            }
        } else {
            IO.log("WARNING: Attempt to toggle provenance prior to "
                    + "initialization of provenance capture on remote hosts");
        }
        sendAck();
        StopWatch.stop(false);
    }
}
