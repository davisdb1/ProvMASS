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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import edu.uw.bothell.css.dsl.MASS.prov.core.logging.Log4J2Logger;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 * An Agent is an execution instance that resides in a Place, perform operations
 * on objects contained by the Place, and possibly migrate to another Place.
 */
@SuppressWarnings("serial")
public class Agents extends AgentsBase implements Serializable {

    private int[] localAgents; // localAgents[i] = # agents in rank[i]
    private int total;

    // logging
    private transient Log4J2Logger logger = Log4J2Logger.getInstance();

    /**
     * Instantiates a set of agents from the "className" class, passes the
     * "argument" object to their constructor, associates them with a given
     * "Places" matrix, and distributes them over these places, based the map( )
     * method that is defined within the Agent class. If a user does not
     * overload it by him/herself, map( ) uniformly distributes an
     * "initPopulation" number of agents. If a user-provided map( ) method is
     * used, it must return the number of agents spawned at each place
     * regardless of the initPopulation parameter. Each set of agents is
     * associated with a user-given handle that must be unique over machines.
     *
     * @param handle
     * @param className The name of the user-defined class to instantiate
     * @param argument The argument to pass to each Agent as it is being
     * instantiated
     * @param places The Places instance that will contain the Agents
     * @param initPopulation The number of Agents to create
     */
    public Agents(int handle, String className, Object argument, Places places,
            int initPopulation) {
        super(handle, className, argument, places.getHandle(), initPopulation);
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Agents"), new StringBuffer("label"), true, new String[]{"handle", "className", "argument", "places", "initPopulation"}, new Object[]{handle, className, argument, places, initPopulation});
        localAgents = new int[MASSBase.getSystemSize()];
        initMaster(argument);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Agents"), procRID, null, null, null, new StringBuffer("Construction"), true, false, false);
        StopWatch.stop(false);
    }

    Object callAllSetup(int functionId, Object argument, Message.ACTION_TYPE type) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callAllSetup"), new StringBuffer("label"), true, new String[]{"functionId", "argument", "type"}, new Object[]{functionId, argument, type});

        // calculate the total number of agents
        total = 0;
        for (int i = 0; i < MASSBase.getSystemSize(); i++) {
            total += localAgents[i];
        }

        // send a AGENTS_CALL_ALL message to each slave
        // i is the indicator of MNode at ith position of the MNode vector
        Message m = null;
        for (int i = 0; i < MASS.getRemoteNodes().size(); i++) {

            // create a message
            if (type == Message.ACTION_TYPE.AGENTS_CALL_ALL_VOID_OBJECT) {
                m = new Message(type, this.getHandle(), functionId, argument);
            } else {

                // calculate argument position
                int argumentPosition = 0;
                for (int dest = 0; dest <= i; dest++) {
                    argumentPosition += localAgents[dest];

                    if (MASS.isConsoleLoggingEnabled()) {
                        System.err
                                .println("Agents.callAll: calc arg_pos = " + argumentPosition
                                        + " localAgents[" + (dest + 1) + "] = "
                                        + localAgents[dest + 1]);
                    }

                }

                Object[] partitionedArgument = new Object[localAgents[i + 1]];

                System.arraycopy((Object[]) argument, argumentPosition, partitionedArgument, 0,
                        localAgents[i + 1]);

                m = new Message(type, this.getHandle(), functionId,
                        partitionedArgument);

                if (MASS.isConsoleLoggingEnabled()) {
                    System.err.println("Agents.callAll: to rank[" + (i + 1)
                            + "] arg_pos = " + argumentPosition);
                }

            }

            // send it
            MASS.getRemoteNodes().get(i).sendMessage(m);

            if (MASS.isConsoleLoggingEnabled()) {

                System.err
                        .println("AGENTS_CALL_ALL " + m.getAction() + " sent to " + i);

                System.err.println("Bag Size is: "
                        + MASSBase.getAgentsMap().get(new Integer(getHandle()))
                                .getAgents().size_unreduced());

            }

        }

        MThread.setAgentBagSize(MASSBase.getAgentsMap()
                .get(new Integer(getHandle())).getAgents().size_unreduced());

        // Check for correct behavior post-Agents_base implementation
        // retrieve the corresponding agents
        // shared between agents
        MASSBase.setCurrentAgentsBase(this);
        MASSBase.setCurrentFunctionId(functionId);
        MASSBase.setCurrentArgument(argument);
        MASSBase.setCurrentMsgType(type);

        if (type == Message.ACTION_TYPE.AGENTS_CALL_ALL_VOID_OBJECT) {
            MASSBase.setCurrentReturns(null);
        } else {
            MASSBase.setCurrentReturns(new Object[total]); // prepare an entire
            // return space
        }

        // resume threads
        logger.debug("MASS_base.currentAgents = {}", MASSBase.getCurrentAgentsBase());

        MThread.resumeThreads(MThread.STATUS_TYPE.STATUS_AGENTSCALLALL);

        // callall implementation
        // Agents.java is visible to user but AgentsBase(super) should not be visible
        if (type == Message.ACTION_TYPE.AGENTS_CALL_ALL_VOID_OBJECT) {
            super.callAll(functionId, argument, 0); // 0 = main tid
        } else {
            super.callAll(functionId, (Object[]) argument, 0);
        }

        // confirm all threads are done with agents.callAll
        MThread.barrierThreads(0);
        localAgents[0] = getLocalPopulation();

        // Synchronized with all slave processes by main thread.
        MASS.barrierAllSlaves(MASSBase.getCurrentReturns(), 0, localAgents);

        total = 0;
        for (int i = 0; i < MASSBase.getSystemSize(); i++) {

            total += localAgents[i];

            // for debugging
            if (MASS.isConsoleLoggingEnabled()) {
                System.err.println("rank[" + i + "]'s local agent population = "
                        + localAgents[i]);
            }

        }
        Object[] currentReturns = MASSBase.getCurrentReturns();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callAllSetup"), procRID, new StringBuffer("currentReturns"), currentReturns, null, null, true, false, false);
        StopWatch.stop(false);
        return currentReturns;
    }

    // The master program needs to inform processes that callAllAsync is about to start
    List<Agent> callAllSetupAsync(int[] functionIds, Object[] arguments, boolean autoMigration) throws Exception {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callAllSetupAsync"), new StringBuffer("label"), true, new String[]{"functionIds", "arguments", "autoMigration"}, new Object[]{functionIds, arguments, autoMigration});
        // FOR auto migration
        Places places = MASSBase.getPlaces(this.getPlacesHandle());
        int lastDimensionLength = places.getSize()[places.getSize().length - 1];

        // The functionality is not checked yet
        if (autoMigration) {
            // if user supplies funcs a b c then the func list
            // become -2 a b c -1 a b c -1 a b c .. -1 a b c
            int[] tempFuncIds = functionIds;
            functionIds = new int[lastDimensionLength * (1 + tempFuncIds.length)];
            functionIds[0] = -2;
            for (int i = 0; i < tempFuncIds.length; i++) {
                functionIds[i + 1] = tempFuncIds[i];
            }
            for (int i = 1; i < lastDimensionLength; i++) {
                functionIds[i * (tempFuncIds.length + 1)] = -1;
                for (int j = 0; j < tempFuncIds.length; j++) {
                    functionIds[i * (tempFuncIds.length + 1) + j + 1] = tempFuncIds[j];
                }
            }
        }

        // Preparing this node for callAllAsync
        MASSBase.prepareAsyncExecution(this, functionIds);

        // calculate the total number of agents
        // i is the indicator of MNode at ith position of the MNode vector
        total = 0;
        for (int i = 0; i < MASSBase.getSystemSize(); i++) {
            total += localAgents[i];
            if (i != 0 && localAgents[i] != 0) {
                // Node started with zero agent won't send completeness notification
                MASS.getChildAgentPids().add(i);
                MASS.getOutAsyncAgents()[i] += localAgents[i]; // a way for master to keep track
                logger.debug("Node {} has master as originator", i);
            }
        }

        if (autoMigration) {
            int expectedAgentSize = 1;
            // if dimension is a1 * a2 * ... an then there need to be
            // a1*a2*..*a(n-1) agents
            for (int i = 0; i < places.getSize().length - 1; i++) {
                expectedAgentSize *= places.getSize()[i];
            }
            if (total != expectedAgentSize) {
                logger.debug("Need " + expectedAgentSize + " for automigration. There are " + total + " agents total");
                ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callAllSetupAsync"), procRID, new StringBuffer("null"), null, null, null, true, false, false);
                return null;
            }
        }

        // send a AGENTS_CALL_ALL_ASYNC message to each slave
        // i is the indicator of MNode at ith position of the MNode vector
        Message m = null;
        for (int i = 0; i < MASS.getRemoteNodes().size(); i++) {
            // calculate argument position
            int argumentPosition = 0;
            for (int dest = 0; dest <= i; dest++) {
                argumentPosition += localAgents[dest];
                if (MASS.isConsoleLoggingEnabled()) {
                    System.err.println("Agents.callAll: calc arg_pos = " + argumentPosition
                            + " localAgents[" + (dest + 1) + "] = " + localAgents[dest + 1]);
                }
            }

            // localAgents[0] indicates the agents in the master node, slaves start from the index 1
            // in order to avoid getting an exception partitionedArgument is used. If arguments is null
            //  we are going to have any empty object.
            Object[] partitionedArgument = new Object[localAgents[i + 1]];
            if (arguments != null) {
                System.arraycopy((Object[]) arguments, argumentPosition, partitionedArgument, 0,
                        localAgents[i + 1]);
            }
            // create message that is going to be sent to remote MNodes
            m = new Message(Message.ACTION_TYPE.AGENTS_CALL_ALL_ASYNC_RETURN_OBJECT,
                    this.getHandle(), functionIds, partitionedArgument);
            if (autoMigration) {
                int[] startingPlaceGlobalIndex = new int[localAgents[i + 1]];
                for (int j = 0; j < startingPlaceGlobalIndex.length; j++) {
                    startingPlaceGlobalIndex[j] = (argumentPosition + j) * lastDimensionLength;
                }
                m.setAutoMigrationStartingIndex(startingPlaceGlobalIndex);
            }
            if (MASS.isConsoleLoggingEnabled()) {
                System.err.println("Agents.callAll: to rank[" + (i + 1)
                        + "] arg_pos = " + argumentPosition);
            }

            // send callAllAsync to remote MNodes
            MASS.getRemoteNodes().get(i).sendMessage(m);
            if (MASS.isConsoleLoggingEnabled()) {
                System.err.println("AGENTS_CALL_ALL_ASYNC " + m.getAction()
                        + " sent to " + i);
                System.err.println("Bag Size is: "
                        + MASSBase.getAgentsMap().get(new Integer(getHandle()))
                                .getAgents().size_unreduced());
            }
        }

        for (int i = 0; i < asyncAgentIdListSize(); i++) {
            if (arguments != null) {
                getAgents().get(asyncAgentIdListGet(i)).setAsyncArgument(arguments[i]);
            }
            getAgents().get(asyncAgentIdListGet(i)).setAutoMigrationStartingIndex(i * lastDimensionLength);
        }
        // shared between agents
        // TODO What is share here?

        // We need this so AsyncInputThread can quickly pass the migration request
        /*
     * MASS_base.setCurrentFunctionId(functionId);
     * MASS_base.setCurrentArgument(argument);
     * MASS_base.setCurrentMsgType(type);
         */
        // resume threads
        logger.debug("MASS_base.currentAgents = {}", MASSBase.getCurrentAgentsBase());

        boolean asyncQueueComplete = false;
        do {

            logger.debug("Begin callAllAsync loop");

            // Mark myself as busy executing my async queue
            setIsAsyncLoopIdle(false);
            // callAllAsync to all slave threads
            MThread.resumeThreads(MThread.STATUS_TYPE.STATUS_AGENTSCALLALL_ASYNC);

            // callAllAsync in my own thread
            super.callAllAsync(0);

            // Done with processing my async queue
            setIsAsyncLoopIdle(true);
            synchronized (getAsyncAgentIdList()) {

                asyncQueueComplete = asyncAgentIdListIsEmpty() && hasNoInprocessAgents();

                logger.debug("getAsyncQueue().isEmpty() && hasNoInprocessAgents() = " + asyncAgentIdListIsEmpty() + " && "
                        + hasNoInprocessAgents() + "; MASS.getChildAgentPids().isEmpty() = "
                        + MASS.getChildAgentPids().isEmpty());

            }

            while ((!MASS.getChildAgentPids().isEmpty() || !MASS.getAsyncOutputThread().isIdle()
                    || !MASS.getAsyncInputThread().isIdle(false)) && asyncQueueComplete) {

                logger.debug(MASS.getChildAgentPids().isEmpty() + " && " + MASS.getAsyncOutputThread().isIdle() + " && "
                        + MASS.getAsyncInputThread().isIdle(false) + " getAsyncQueue().size() = " + asyncAgentIdListSize());

                try {
                    getAsyncAgentIdList().wait();
                } catch (InterruptedException e) {
                    logger.error("Caught exception while processing async queue", e);
                }

                asyncQueueComplete = asyncAgentIdListIsEmpty() && hasNoInprocessAgents();

            }

            // confirm all threads are done with agents.callAllAsync
            // backward compatibility barrier twice,
            // once in callAllAsync in each thread, but then slave thread
            // enter another barrier at the end of Mthread.run() while() loop
            // so master thread has to barrier here again to get every one back onto
            // the top
            //Mthread.barrierThreads(0);
        } while (!asyncQueueComplete);

        collectAsyncResult();
        List<Agent> getAsyncCompletedAgentList = getAsyncCompletedAgentList();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAsyncCompletedAgentList"), procRID, new StringBuffer("getAsyncCompletedAgentList"), getAsyncCompletedAgentList, null, null, true, false, false);
        return getAsyncCompletedAgentList;
    }

    /**
     * Calls the method specified with functionId of all agents. Done in
     * parallel among multi-processes/threads
     *
     * @param functionId
     */
    public void callAll(int functionId) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callAll"), new StringBuffer("label"), true, new String[]{"functionId"}, new Object[]{functionId});
        callAllSetup(functionId, null, Message.ACTION_TYPE.AGENTS_CALL_ALL_VOID_OBJECT);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callAll"), procRID, null, null, null, null, true, false, false);
    }

    /**
     * Calls the method specified with functionId of all agents as passing a
     * (void) argument to the method. Done in parallel among
     * multi-processes/threads.
     *
     * @param functionId
     * @param argument
     */
    public void callAll(int functionId, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callAll"), new StringBuffer("label"), true, new String[]{"functionId", "argument"}, new Object[]{functionId, argument});
        callAllSetup(functionId, argument,
                Message.ACTION_TYPE.AGENTS_CALL_ALL_VOID_OBJECT);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callAll"), procRID, null, null, null, null, true, false, false);
    }

    /**
     * Calls the method specified with functionId of all agents as passing
     * arguments[i] to agent[i]’s method, and receives a return value from it
     * into (void *)[i] whose element’s size is return_value. Done in parallel
     * among multi-processes/threads. The order of agents depends on the index
     * of a place where they resides, starts from the place[0][0]…[0], and gets
     * increased with the right-most index first and the left-most index last.
     *
     * @param functionId
     * @param argument
     * @return
     */
    public Object callAll(int functionId, Object[] argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callAll"), new StringBuffer("label"), true, new String[]{"functionId", "argument"}, new Object[]{functionId, argument});
        Object callAllReturn = callAllSetup(functionId, argument,
                Message.ACTION_TYPE.AGENTS_CALL_ALL_RETURN_OBJECT);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("hasNext"), procRID, new StringBuffer("callAllReturn"), callAllReturn, null, null, true, false, false);
        return callAllReturn;
    }

    // user program calls this method
    // Arguments are delivered to all agents but should be given to only first function
    public List<Agent> callAllAsync(int[] functionIds,
            Object[] arguments) throws Exception {
        return callAllSetupAsync(functionIds, arguments, false);
    }

    // user program calls this method
    // Arguments are delivered to all agents but should be given to only first function
    public List<Agent> callAllAsync(int[] functionIds,
            Object[] arguments, boolean autoMigration) throws Exception {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callAllAsync"), new StringBuffer("label"), true, new String[]{"functionIds", "arguments", "autoMigration"}, new Object[]{functionIds, arguments, autoMigration});
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("initMaster"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return callAllSetupAsync(functionIds, arguments, autoMigration);
    }

    private void initMaster(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("initMaster"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument});

        // check if MASS_base.hosts is empty (i.e., Places not yet created)
        if (MASSBase.getHosts().isEmpty()) {
            System.err.println("Agents(" + getClassName()
                    + ") can't be created without Places!!");
            System.exit(-1);
        }

        // create a new list for message
        Message m = new Message(Message.ACTION_TYPE.AGENTS_INITIALIZE,
                getInitPopulation(), getHandle(), getPlacesHandle(), getClassName(),
                argument);

        // send a AGENT_INITIALIZE message to each slave
        for (MNode node : MASS.getRemoteNodes()) {

            node.sendMessage(m);
            logger.debug("AGENT_INITIALIZE sent to {}", node.getPid());

        }

        // Synchronized with all slave processes
        MASS.barrierAllSlaves(localAgents);
        localAgents[0] = getLocalPopulation();

        total = 0;
        for (int i = 0; i < MASSBase.getSystemSize(); i++) {

            total += localAgents[i];
            logger.debug("rank[" + i + "]'s local agent population = " + localAgents[i]);

        }

        // register this agents in the places hash map
        MASSBase.getAgentsMap().put(new Integer(getHandle()), this);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("initMaster"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private void manageAllSetup() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("manageAllSetup"), new StringBuffer("label"), true, null, null);

        // send an AGENTS_MANAGE_ALL message to each slave
        Message m = null;
        for (MNode node : MASS.getRemoteNodes()) {

            // create a message
            m = new Message(Message.ACTION_TYPE.AGENTS_MANAGE_ALL, this.getHandle(),
                    0);

            // send it
            node.sendMessage(m);

            // MThread Update
            MThread.setAgentBagSize(MASSBase.getAgentsMap()
                    .get(new Integer(getHandle())).getAgents().size_unreduced());

        }

        // retrieve the corresponding agents
        MASSBase.setCurrentAgentsBase(this);
        MASSBase.setCurrentMsgType(Message.ACTION_TYPE.AGENTS_MANAGE_ALL);

        // resume threads
        MThread.resumeThreads(MThread.STATUS_TYPE.STATUS_MANAGEALL);

        // callall implementatioin
        super.manageAll(0); // 0 = the main thread id

        // confirm all threads are done with agents.callAll
        MThread.barrierThreads(0);

        // Synchronized with all slave processes
        MASS.barrierAllSlaves(localAgents);
        localAgents[0] = getLocalPopulation();

        total = 0;
        for (int i = 0; i < MASSBase.getSystemSize(); i++) {

            total += localAgents[i];

            // for debugging
            if (MASS.isConsoleLoggingEnabled() == true) {
                System.err.println("rank[" + i + "]'s local agent population = "
                        + localAgents[i]);
            }

        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("manageAllSetup"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Updates each agent’s status, based on each of its latest migrate( ),
     * spawn( ), and kill( ) calls. These methods are defined in the Agent base
     * class and may be invoked from other functions through callAll and
     * exchangeAll. Done in parallel among multi-processes/threads
     */
    public void manageAll() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("manageAll"), new StringBuffer("label"), true, null, null);
        manageAllSetup();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("nAgents"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Returns the current number of agents.
     *
     * @return nAgents
     */
    public int nAgents() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("nAgents"), new StringBuffer("label"), true, null, null);

        int nAgents = 0;
        for (int i = 0; i < MASSBase.getSystemSize(); i++) {
            nAgents += localAgents[i];
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("nAgents"), procRID, new StringBuffer("nAgents"), nAgents, null, null, true, false, false);
        StopWatch.stop(false);
        return nAgents;
    }

    private void collectAsyncResult() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("collectAsyncResult"), new StringBuffer("label"), true, null, null);
        // TODO Auto Migration somewhere?

        // in case of killing agent, backward compatibility
        getAgents().reduce();
        setLocalPopulation(getAgents().size_unreduced());
        localAgents[0] = getLocalPopulation();

        MASS.getRemoteAsyncResults();
        Collections.sort(getAsyncCompletedAgentList(), new AgentAsyncComparator());
        MASSBase.setCurrentReturns(getAsyncCompletedAgentList().toArray());
        for (int i = 1; i < MASSBase.getSystemSize(); i++) {
            localAgents[i] = MASS.getLocalAgents()[i - 1];
        }
        total = 0;
        for (int i = 0; i < MASSBase.getSystemSize(); i++) {
            total += localAgents[i];
            // for debugging
            if (MASS.isConsoleLoggingEnabled()) {
                System.err.println("rank[" + i + "]'s local agent population = "
                        + localAgents[i]);
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("collectAsyncResult"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }
}
