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

import edu.uw.bothell.css.dsl.MASS.prov.core.logging.Log4J2Logger;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

@SuppressWarnings("serial")
public class Agent implements Serializable {

    /**
     * Is this agent’s identifier. It is calculated as: the sequence number *
     * the size of this agent’s belonging matrix + the index of the current
     * place when all places are flattened to a single dimensional array.
     */
    private int agentId;
    public static final StringBuffer agentId_RID = ProvUtils.getUniversalResourceID(new StringBuffer("agentId"));

    /**
     * The current place where this Agent resides
     */
    private Place place = null;

    /**
     * Is an array that maintains the coordinates of where this agent resides.
     * Intuitively, index[0], index[1], and index[2] correspond to coordinates
     * of x, y, and z, or those of i, j, and k.
     */
    private int[] index = null;

    /**
     * Is true while this agent is active. Once it is set false, this agent is
     * killed upon a next call to Agents.manageAll( ).
     */
    private boolean alive = true;

    /**
     * Is the number of new children created by this agent upon a next call to
     * Agents.manageAll( ).
     */
    private int newChildren = 0;

    /**
     * Is an array of arguments, each passed to a different new child.
     */
    private Object[] arguments = null;

    // logging
    private transient Log4J2Logger logger = Log4J2Logger.getInstance();

    // Async
    private volatile int asyncFuncListIndex = 0; // next func in the async func list to execute
    private Object[] asyncResults;
    private volatile int asyncResultsIndex = 0; // next index to be inserted
    private Object asyncArgument;
    private volatile AgentsBase myAgentsBase; // myAGentsBase is necessary to spawn a new instance from current agent's AgentsBase

    // true to signal a thread to stop processing this Agent's asyncFuncList
    // this happens in kill & migrate case
    private volatile boolean hasAlreadyGone = false;
    private volatile boolean needsToGoBackToAsyncQueue = false;

    /**
     * backward compatibility with agentbag, together with myAsyncPid keep track
     * of the original position of the agent, set at the beginning of
     * callAllAsync and not changed throughout execution
     */
    private int myOriginalAsyncIndex;

    /**
     * The current index of this agent in agent list change when remote migrate,
     * used for killing, async queue access
     */
    private volatile int myCurrentIndex;

    /**
     * Original Pid before execution
     */
    private int myAsyncOriginalPid;

    private int autoMigrationStartingIndex;

    public boolean provOn = false;

    public Agent() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Agent"), new StringBuffer("label"), true, null, null);
        agentId = Agents.getAgentInitAgentId();
        ProvenanceRecorder.documentFieldAssignment(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("agentId"), agentId_RID, agentId, procRID, true);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Agent"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Is called from Agents.callAll. It invokes the function specified with
     * functionId as passing arguments to this function. A user-derived Agent
     * class must implement this method.
     *
     * @param functionId
     * @param argument
     * @return
     */
    public Object callMethod(int functionId, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), new StringBuffer("label"), true, new String[]{"functionId", "argument"}, new Object[]{functionId, argument});
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), procRID, new StringBuffer("null"), null, null, new StringBuffer(""), true, false, false);
        StopWatch.stop(false);
        return null;
    }

    public int getAgentId() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getAgentId"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getAgentId"), procRID, new StringBuffer("agentId"), agentId, null, null, true, false, false);
        StopWatch.stop(false);
        return agentId;
    }

    protected Object[] getArguments() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getArguments"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getArguments"), procRID, new StringBuffer("arguments"), arguments, null, null, true, false, false);
        StopWatch.stop(false);
        return arguments;
    }

    /**
     * Get debug data from the agent
     *
     * @return Debug data
     */
    public Number getDebugData() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getDebugData"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getDebugData"), procRID, new StringBuffer("null"), null, null, new StringBuffer("Debug data"), true, false, false);
        StopWatch.stop(false);
        return null;
    }

    public void setDebugData(Number data) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setDebugData"), new StringBuffer("label"), true, new String[]{"data"}, new Object[]{data});
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setDebugData"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public int[] getIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getIndex"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getIndex"), procRID, new StringBuffer("index"), index, null, null, true, false, false);
        StopWatch.stop(false);
        return index;
    }

    protected int getNewChildren() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getNewChildren"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getNewChildren"), procRID, new StringBuffer("newChildren"), newChildren, null, null, true, false, false);
        StopWatch.stop(false);
        return newChildren;
    }

    public Place getPlace() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getPlace"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getPlace"), procRID, new StringBuffer("place"), place, null, null, true, false, false);
        StopWatch.stop(false);
        return place;
    }

    protected boolean isAlive() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("isAlive"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("isAlive"), procRID, new StringBuffer("alive"), alive, null, null, true, false, false);
        StopWatch.stop(false);
        return alive;
    }

    /**
     * Terminates the calling agent upon a next call to Agents.manageAll( ).
     * More specifically, kill( ) sets the "alive" variable false.
     */
    public void kill() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("kill"), new StringBuffer("label"), true, null, null);
        alive = false;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("kill"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public void killAsync() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("killAsync"), new StringBuffer("label"), true, null, null);

        kill();
        hasAlreadyGone = true;

        synchronized (MThread.class) {
            MThread.setAgentBagSize(MThread.getAgentBagSize() - 1);
        }

        // remove the agent from this place
        getPlace().getAgents().remove(this);

        // remove from AgentList, too!
        // unlike sync myAsyncIndex start from 0
        /**
         * TO DO IN callAllAsyncLoop only myAgentsBase.getAgents().remove(
         * myCurrentIndex );
         */
        // So Agents_base put the result into completeQueue
        asyncFuncListIndex = -1;
        myAgentsBase = null;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("killAsync"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Returns the number of agents to initially instantiate on a place indexed
     * with coordinates[]. The maxAgents parameter indicates the number of
     * agents to create over the entire application. The argument size[] defines
     * the size of the "Place" matrix to which a given "Agent" class belongs.
     * The system-provided (thus default) map( ) method distributes agents over
     * places uniformly as in: maxAgents / size.length The map( ) method may be
     * overloaded by an application-specific method. A user-provided map( )
     * method may ignore maxAgents when creating agents.
     *
     * @param initPopulation
     * @param size
     * @param index
     * @return
     */
    public int map(int initPopulation, int[] size, int[] index) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("map"), new StringBuffer("label"), true, new String[]{"initPopulation", "size", "index"}, new Object[]{initPopulation, size, index});

        // compute the total # places
        int placeTotal = 1;
        for (int x = 0; x < size.length; x++) {
            placeTotal *= size[x];
        }

        // compute the global linear index
        int linearIndex = 0;
        for (int i = 0; i < index.length; i++) {
            if (index[i] >= 0 && size[i] > 0 && index[i] < size[i]) {
                linearIndex = linearIndex * size[i];
                linearIndex += index[i];
            }
        }

        // compute #agents per place a.k.a. colonists
        int colonists = initPopulation / placeTotal;
        int remainders = initPopulation % placeTotal;
        if (linearIndex < remainders) {
            colonists++; // add a remainder
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("map"), procRID, new StringBuffer("colonists"), colonists, null, new StringBuffer(""), true, false, false);
        StopWatch.stop(false);
        return colonists;

    }

    /**
     * Initiates an agent migration upon a next call to Agents.manageAll( ).
     * More specifically, migrate( ) updates the calling agent’s index[].
     *
     * @param index
     * @return
     */
    protected boolean migrate(int... index) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("migrate"), new StringBuffer("label"), true, new String[]{"index"}, new Object[]{index});
//        edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore store 
//                = edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils.getStoreSafely(this);
//        if(store != null){
//            String methodRID = ProvUtils.getLocationAwareURID("migrate");
//            String callingMethodRID = ResourceMatcher.getMatcher().popActivityID();
//        }
        int[] placesSize = place.getSize();
        for (int i = 0; i < placesSize.length; i++) {
            if (index[i] >= 0 && index[i] < placesSize[i]) {
                continue;
            } else {
                ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn),
                        this, new StringBuffer("migrate"), procRID, new StringBuffer("false"), false, null, new StringBuffer(""), true, false, false);
                return false;
            }
        }

        this.index = index.clone(); // assign the new index
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn),
                this, new StringBuffer("migrate"), procRID, new StringBuffer("true"), true, null, new StringBuffer(""), true, false, false);
        StopWatch.stop(false);
        return true;

    }

    protected boolean migrateAsync(int... index) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("migrateAsync"), new StringBuffer("label"), true, new String[]{"index"}, new Object[]{index});
        boolean result = migrate(index);
        //stopProcessAsyncFuncList = true;
        myAgentsBase.migrateAsync(this);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("migrateAsync"), procRID, new StringBuffer("result"), result, null, null, true, false, false);
        StopWatch.stop(false);
        return result;
    }

    protected void setIndex(int[] index) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setIndex"), new StringBuffer("label"), true, new String[]{"index"}, new Object[]{index});
        this.index = index;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setIndex"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected void setNewChildren(int newChildren) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setNewChildren"), new StringBuffer("label"), true, new String[]{"newChildren"}, new Object[]{newChildren});
        this.newChildren = newChildren;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setNewChildren"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected void setPlace(Place place) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setPlace"), new StringBuffer("label"), true, new String[]{"place"}, new Object[]{place});
        this.place = place;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setPlace"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected int getAsyncFuncListIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getAsyncFuncListIndex"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getAsyncFuncListIndex"), procRID, new StringBuffer("asyncFuncListIndex"), asyncFuncListIndex, null, null, true, false, false);
        StopWatch.stop(false);
        return asyncFuncListIndex;
    }

    public int nextAsyncFuncListIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("nextAsyncFuncListIndex"), new StringBuffer("label"), true, null, null);
        ++asyncFuncListIndex;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("nextAsyncFuncListIndex"), procRID, new StringBuffer("asyncFuncListIndex_-_1"), asyncFuncListIndex - 1, null, null, true, false, false);
        StopWatch.stop(false);
        return asyncFuncListIndex - 1;
    }

    protected void setAsyncFuncListIndex(int index) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setAsyncFuncListIndex"), new StringBuffer("label"), true, new String[]{"index"}, new Object[]{index});
        asyncFuncListIndex = index;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setAsyncFuncListIndex"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public Object[] getAsyncResults() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getAsyncResults"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getAsyncResults"), procRID, new StringBuffer("asyncResults"), asyncResults, null, null, true, false, false);
        StopWatch.stop(false);
        return asyncResults;
    }

    protected void appendAsyncResult(Object newResult) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("appendAsyncResult"), new StringBuffer("label"), true, new String[]{"newResult"}, new Object[]{newResult});
        asyncResults[asyncResultsIndex] = newResult;
        ++asyncResultsIndex;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("appendAsyncResult"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public void resetAsyncResults() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("resetAsyncResults"), new StringBuffer("label"), true, null, null);
        asyncResults = new Object[myAgentsBase.getAsyncFuncList().length];
        asyncResultsIndex = 0;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("resetAsyncResults"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected int asyncResultsSize() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("asyncResultsSize"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("asyncResultsSize"), procRID, new StringBuffer("asyncResultsIndex"), asyncResultsIndex, null, null, true, false, false);
        StopWatch.stop(false);
        return asyncResultsIndex;
    }

    protected void setAsyncArgument(Object newArg) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setAsyncArgument"), new StringBuffer("label"), true, new String[]{"newArg"}, new Object[]{newArg});
        asyncArgument = newArg;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setAsyncArgument"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected Object getAsyncArgument() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getAsyncArgument"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getAsyncArgument"), procRID, new StringBuffer("asyncArgument"), asyncArgument, null, null, true, false, false);
        StopWatch.stop(false);
        return asyncArgument;
    }

    protected void setMyOriginalAsyncIndex(int newIndex) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setMyOriginalAsyncIndex"), new StringBuffer("label"), true, new String[]{"newIndex"}, new Object[]{newIndex});
        myOriginalAsyncIndex = newIndex;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setMyOriginalAsyncIndex"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected int getMyOriginalAsyncIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getMyOriginalAsyncIndex"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getMyOriginalAsyncIndex"), procRID, new StringBuffer("myOriginalAsyncIndex"), myOriginalAsyncIndex, null, null, true, false, false);
        StopWatch.stop(false);
        return myOriginalAsyncIndex;
    }

    protected void setCurrentIndex(int newIndex) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setCurrentIndex"), new StringBuffer("label"), true, new String[]{"newIndex"}, new Object[]{newIndex});
        myCurrentIndex = newIndex;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setCurrentIndex"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected int getCurrentIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getCurrentIndex"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getCurrentIndex"), procRID, new StringBuffer("myCurrentIndex"), myCurrentIndex, null, null, true, false, false);
        StopWatch.stop(false);
        return myCurrentIndex;
    }

    protected void setMyAsyncOriginalPid(int pid) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setMyAsyncOriginalPid"), new StringBuffer("label"), true, new String[]{"pid"}, new Object[]{pid});
        myAsyncOriginalPid = pid;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setMyAsyncOriginalPid"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected int getMyAsyncOriginalPid() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getMyAsyncOriginalPid"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getMyAsyncOriginalPid"), procRID, new StringBuffer("myAsyncOriginalPid"), myAsyncOriginalPid, null, null, true, false, false);
        StopWatch.stop(false);
        return myAsyncOriginalPid;
    }

    public void setMyAgentsBase(AgentsBase parent) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setMyAgentsBase"), new StringBuffer("label"), true, new String[]{"parent"}, new Object[]{parent});
        myAgentsBase = parent;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setMyAgentsBase"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public AgentsBase getMyAgentsBase() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getMyAgentsBase"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getMyAgentsBase"), procRID, new StringBuffer("myAgentsBase"), myAgentsBase, null, null, true, false, false);
        StopWatch.stop(false);
        return myAgentsBase;
    }

    public boolean isHasAlreadyGone() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("isHasAlreadyGone"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("isHasAlreadyGone"), procRID, new StringBuffer("hasAlreadyGone"), hasAlreadyGone, null, null, true, false, false);
        StopWatch.stop(false);
        return hasAlreadyGone;
    }

    public void setHasAlreadyGone(boolean value) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setHasAlreadyGone"), new StringBuffer("label"), true, new String[]{"value"}, new Object[]{value});
        hasAlreadyGone = value;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setHasAlreadyGone"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public boolean isNeedsToGoBackToAsyncQueue() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("isNeedsToGoBackToAsyncQueue"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("isNeedsToGoBackToAsyncQueue"), procRID, new StringBuffer("needsToGoBackToAsyncQueue"), needsToGoBackToAsyncQueue, null, null, true, false, false);
        StopWatch.stop(false);
        return needsToGoBackToAsyncQueue;
    }

    public void setNeedsToGoBackToAsyncQueue(boolean value) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setNeedsToGoBackToAsyncQueue"), new StringBuffer("label"), true, new String[]{"value"}, new Object[]{value});
        needsToGoBackToAsyncQueue = value;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setNeedsToGoBackToAsyncQueue"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Spawns a “numAgents’ of new agents, as passing arguments[i] (with
     * arg_size) to the i-th new agent upon a next call to Agents.manageAll( ).
     * More specifically, spawn( ) changes the calling agent’s newChildren.
     *
     * @param numAgents
     * @param arguments
     */
    protected void spawn(int numAgents, Object[] arguments) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("spawn"), new StringBuffer("label"), true, new String[]{"numAgents", "arguments"}, new Object[]{numAgents, arguments});

        //Only want to make changes if the number to be created is above zero
        if (numAgents > 0) {
            newChildren = numAgents;
            this.arguments = arguments.clone();
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("spawn"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Spawn new children async and supply them with the arguments and
     * functionIds
     *
     * @param numAgents
     * @param initializedArguments
     * @param arguments
     */
    protected void spawnAsync(int numAgents, Object[] initializedArguments, Object[] arguments) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("spawnAsync"), new StringBuffer("label"), true, new String[]{"numAgents", "initializedArguments", "arguments"}, new Object[]{numAgents, initializedArguments, arguments});
        if (numAgents > 0) {
            myAgentsBase.spawnAsync(this, numAgents, initializedArguments, arguments);
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("spawnAsync"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Only FOR ASYNC
     */
    protected Agent cloneForAsyncResult() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("cloneForAsyncResult"), new StringBuffer("label"), true, null, null);
        Agent result = new Agent();
        result.alive = this.alive;
        result.asyncResults = new Object[asyncResultsIndex];
        for (int i = 0; i < asyncResultsIndex; i++) {
            result.asyncResults[i] = this.asyncResults[i];
        }
        result.myAsyncOriginalPid = this.myAsyncOriginalPid;
        result.myOriginalAsyncIndex = this.myOriginalAsyncIndex;
        logger.debug("cloneForAsyncResult asyncResults size = " + result.asyncResultsSize() + " original idx " + result.myOriginalAsyncIndex);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("cloneForAsyncResult"), procRID, new StringBuffer("result"), result, null, null, true, false, false);
        StopWatch.stop(false);
        return result;
    }

    void autoMigrateStart() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("autoMigrateStart"), new StringBuffer("label"), true, null, null);
        int[] size = place.getSize();
        int[] index = size.clone();
        for (int i = size.length - 1; i >= 0; i--) {
            // autoMigrationStartingIndex value is altered after this
            index[i] = this.autoMigrationStartingIndex % size[i];
            this.autoMigrationStartingIndex = autoMigrationStartingIndex / size[i];
        }
        migrateAsync(index);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("autoMigrateStart"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    void autoMigrateNext() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("autoMigrateNext"), new StringBuffer("label"), true, null, null);
        int[] index = this.getPlace().getIndex().clone();
        ++index[index.length - 1];
        migrateAsync(index);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("autoMigrateNext"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected void setAutoMigrationStartingIndex(int i) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setAutoMigrationStartingIndex"), new StringBuffer("label"), true, new String[]{"i"}, new Object[]{i});
        this.autoMigrationStartingIndex = i;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setAutoMigrationStartingIndex"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public boolean isProvOn() {
        return provOn;
    }

    public void setProvOn(boolean newPrevSetting) {
        provOn = newPrevSetting;
    }

    public int getLinearIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getLinearIndex"), new StringBuffer("label"), true, null, null);
        int linearIndex = getLinearIndexFromArrayIndex(index, getPlace().getSize());
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getGlobalLinearIndexFromGlobalArrayIndex"), procRID, new StringBuffer("linearIndex"), linearIndex, null, null, true, false, false);
        StopWatch.stop(true);
        return linearIndex;
    }

    protected int getLinearIndexFromArrayIndex(int index[], int size[]) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getGlobalLinearIndexFromGlobalArrayIndex"), new StringBuffer("label"), true, new String[]{"index", "size"}, new Object[]{index, size});

        int retVal = 0;

        for (int i = 0; i < index.length; i++) {

            if (size[i] <= 0) {
                continue;
            }

            if (index[i] >= 0 && index[i] < size[i]) {
                retVal = retVal * size[i];
                retVal += index[i];
            } else {
                ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getGlobalLinearIndexFromGlobalArrayIndex"), procRID, new StringBuffer("Integer.MIN_VALUE"), Integer.MIN_VALUE, null, null, true, false, false);
                StopWatch.stop(false);
                return Integer.MIN_VALUE; // out of space
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getGlobalLinearIndexFromGlobalArrayIndex"), procRID, new StringBuffer("retVal"), retVal, null, null, true, false, false);
        StopWatch.stop(false);
        return retVal;
    }
}
