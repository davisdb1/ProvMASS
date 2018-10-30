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

import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledObject;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.Serializable;
import java.util.Vector;

@SuppressWarnings("serial")
public class Message implements Serializable, ProvEnabledObject {

    /**
     * ACTION_TYPE A list of actions assigned to numbers.
     */
    public enum ACTION_TYPE {

        EMPTY, // 0             
        FINISH("FINISH"), // 1             
        ACK("ACK"), // 2             

        PLACES_INITIALIZE, // 3             
        PLACES_CALL_ALL_VOID_OBJECT, // 4             
        PLACES_CALL_ALL_RETURN_OBJECT, // 5             
        PLACES_CALL_SOME_VOID_OBJECT,
        PLACES_EXCHANGE_ALL, // 7             
        PLACES_EXCHANGE_ALL_REMOTE_REQUEST, // 8             
        PLACES_EXCHANGE_ALL_REMOTE_RETURN_OBJECT, // 9             
        PLACES_EXCHANGE_BOUNDARY, // 10            
        PLACES_EXCHANGE_BOUNDARY_REMOTE_REQUEST, // 11            

        AGENTS_INITIALIZE, // 12            
        AGENTS_CALL_ALL_VOID_OBJECT, // 13            
        AGENTS_CALL_ALL_RETURN_OBJECT, // 14            
        AGENTS_MANAGE_ALL, // 15            
        AGENTS_MIGRATION_REMOTE_REQUEST, // 16  

        /**
         * Async section *
         */
        AGENTS_CALL_ALL_ASYNC_RETURN_OBJECT("AGENTS_CALL_ALL_ASYNC_RETURN_OBJECT"), // 17
        NODE_MASTER_ASYNC_COMPLETE_REQUEST("NODE_MASTER_ASYNC_COMPLETE_REQUEST"), //18 check if all slaves are completed
        AGENTS_ASYNC_MIGRATION_REMOTE_REQUEST("AGENTS_ASYNC_MIGRATION_REMOTE_REQUEST"), // 19
        AGENT_ASYNC_RESULT("AGENT_ASYNC_RESULT"), // 20
        // NODE_SLAVE_ASYNC_COMPLETE_NOTIFY("NODE_SLAVE_ASYNC_COMPLETE_NOTIFY"),     
        NODE_COMPLETE_NOTIFY_SOURCE("NODE_COMPLETE_NOTIFY_SOURCE"), // 21 tell master that I'm done

        MASSPROV_INITIALIZE, // 22
        TOGGLE_PROVENANCE_CAPTURE, // 23
        MEASURE_HOST_DELAY; //24

        private final String value;

        private ACTION_TYPE(String v) {
            StopWatch.start(false);
            StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("ACTION_TYPE"), new StringBuffer("label"), true, new String[]{"v"}, new Object[]{v});
            value = v;
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("ACTION_TYPE"), procRID, null, null, null, null, true, false, false);
            StopWatch.stop(false);
        }

        private ACTION_TYPE() {
            StopWatch.start(false);
            StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("ACTION_TYPE"), new StringBuffer("label"), true, null, null);
            value = "UNDEFINED";
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("ACTION_TYPE"), procRID, null, null, null, null, true, false, false);
            StopWatch.stop(false);
        }

        public String getValue() {
            StopWatch.start(false);
            StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getValue"), new StringBuffer("label"), true, null, null);
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getValue"), procRID, new StringBuffer("value"), value, null, null, true, false, false);
            StopWatch.stop(false);
            return value;
        }
    }

    private static final int VOID_HANDLE = -1;
    private ACTION_TYPE action;
    private int[] size = null;
    private int handle = VOID_HANDLE;
    private int destinationHandle = VOID_HANDLE;
    private int functionId = 0;
    private String classname = null;      // classname.class must be located in CWD.
    private Object argument = null;
    private Vector<String> hosts = null; // all hosts participated in computation
    private Vector<int[]> destinations = null; // all destinations of exchangeAll
    private int agentPopulation = -1;
    private int boundaryWidth = 0;
    private Vector<RemoteExchangeRequest> exchangeReqList = null;
    private Vector<AgentMigrationRequest> migrationReqList = null;
    // Pid of the source when sending back result in
    // callAllAsync
    private int sourcePid = -1;

    // Async vars'[
    private int[] functionIds = null;
    private int[] autoMigrateStartingIndex = null;

    // EMPTY
    public Message() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * FINISH ACK
     *
     * @param action
     */
    public Message(ACTION_TYPE action) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action"}, new Object[]{action});
        this.action = action;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * ACK used for AGENTS_INITIALIZE and AGENTS_CALL_ALL_VOID_OBJECT
     *
     * @param action
     * @param localPopulation
     */
    public Message(ACTION_TYPE action, int localPopulation) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action", "localPopulation"}, new Object[]{action, localPopulation});
        this.action = action;
        this.agentPopulation = localPopulation;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * AGENTS_MANAGE_ALL and PLACES_EXCHANGE_BOUNDARY
     *
     * @param action
     * @param handle
     * @param dummy
     */
    public Message(ACTION_TYPE action, int handle, int dummy) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action", "handle", "dummy"}, new Object[]{action, handle, dummy});
        this.action = action;
        this.handle = handle;
        this.destinationHandle = handle;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public Message(ACTION_TYPE action, int handle, int destHandle, int funcID) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action", "handle", "destHandle", "funcID"}, new Object[]{action, handle, destHandle, funcID});
        this.action = action;
        this.handle = handle;
        this.destinationHandle = destHandle;
        this.functionId = funcID;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * AGENTS_INITIALIZE
     *
     * @param action
     * @param initPopulation
     * @param handle
     * @param placeHandle
     * @param className
     * @param argument
     */
    public Message(ACTION_TYPE action, int initPopulation, int handle, int placeHandle, String className, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action", "initPopulation", "handle", "placeHandle", "className", "argument"}, new Object[]{action, initPopulation, handle, placeHandle, className, argument});
        this.action = action;
        this.handle = handle;
        this.destinationHandle = placeHandle;
        this.classname = className;
        this.argument = argument;
        this.agentPopulation = initPopulation;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * PLACES_EXCHANGE_ALL
     *
     * @param action
     * @param handle
     * @param dest_handle
     * @param functionId
     * @param destinations
     */
    public Message(ACTION_TYPE action, int handle, int dest_handle, int functionId, Vector<int[]> destinations) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action", "handle", "dest_handle", "functionId", "destinations"}, new Object[]{action, handle, dest_handle, functionId, destinations});
        this.action = action;
        this.handle = handle;
        this.destinationHandle = dest_handle;
        this.functionId = functionId;
        this.destinations = destinations;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * PLACES_EXCHANGE_ALL_REMOTE_REQUEST
     *
     * @param action
     * @param handle
     * @param destinationHandle
     * @param functionId
     * @param exchangeReqList
     * @param dummy
     */
    public Message(ACTION_TYPE action, int handle, int destinationHandle, int functionId, Vector<RemoteExchangeRequest> exchangeReqList, int dummy) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action", "handle", "destinationHandle", "functionId", "exchangeReqList", "dummy"}, new Object[]{action, handle, destinationHandle, functionId, exchangeReqList, dummy});
        this.action = action;
        this.handle = handle;
        this.destinationHandle = destinationHandle;
        this.functionId = functionId;
        this.exchangeReqList = exchangeReqList;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * PLACES_CALL_ALL_VOID_OBJECT, PLACES_CALL_ALL_RETURN_OBJECT,
     * AGENTS_CALL_ALL_VOID_OBJECT, AGENTS_CALL_ALL_RETURN_OBJECT
     *
     * @param action
     * @param handle
     * @param functionId
     * @param argument
     */
    public Message(ACTION_TYPE action, int handle, int functionId, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action", "handle", "functionId", "argument"}, new Object[]{action, handle, functionId, argument});
        this.action = action;
        this.handle = handle;
        this.functionId = functionId;
        this.argument = argument;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * AGENTS_MIGRATION_REMOTE_REQUEST
     *
     * @param action
     * @param agentHandle
     * @param placeHandle
     * @param migrationReqList
     */
    public Message(ACTION_TYPE action, int agentHandle, int placeHandle, Vector<AgentMigrationRequest> migrationReqList) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action", "agentHandle", "placeHandle", "migrationReqList"}, new Object[]{action, agentHandle, placeHandle, migrationReqList});
        this.action = action;
        this.handle = agentHandle;
        this.destinationHandle = placeHandle;
        this.migrationReqList = migrationReqList;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * PLACES_INITIALIZE
     *
     * @param action
     * @param size
     * @param handle
     * @param classname
     * @param argument
     * @param boundaryWidth
     * @param hosts
     */
    public Message(ACTION_TYPE action, int[] size, int handle, String classname, Object argument, int boundaryWidth, Vector<String> hosts) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action", "size", "handle", "classname", "argument", "boundaryWidth", "hosts"}, new Object[]{action, size, handle, classname, argument, boundaryWidth, hosts});
        this.action = action;
        this.size = size;
        this.handle = handle;
        this.classname = classname;
        this.argument = argument;
        this.hosts = hosts;
        this.boundaryWidth = boundaryWidth;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * PLACES_EXCHANGE_ALL_REMOTE_RETURN_OBJECT and
     * PLACES_EXCHANGE_BOUNDARY_REMOTE_REQUEST ACK used for
     * PLACES_CALL_ALL_RETURN_OBJECT
     *
     * @param action
     * @param retVals
     */
    public Message(ACTION_TYPE action, Object retVals) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action", "retVals"}, new Object[]{action, retVals});
        this.action = action;
        this.argument = retVals;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * ACK used for AGENTS_CALL_ALL_RETURN_OBJECT and AGENT_ASYNC_RESULT
     *
     * @param action
     * @param argument
     * @param localPopulation
     */
    public Message(ACTION_TYPE action, Object argument, int localPopulation) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action", "argument", "localPopulation"}, new Object[]{action, argument, localPopulation});
        this.action = action;
        this.argument = argument;
        this.agentPopulation = localPopulation;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    // AGENT_ASYNC_RESULT
    public void setSourcePid(int pid) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setSourcePid"), new StringBuffer("label"), true, new String[]{"pid"}, new Object[]{pid});
        sourcePid = pid;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setSourcePid"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Get the action
     *
     * @return action
     */
    public ACTION_TYPE getAction() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAction"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAction"), procRID, new StringBuffer("action"), action, null, new StringBuffer("action"), true, false, false);
        StopWatch.stop(false);
        return action;
    }

    /**
     * Get the Agent Populations
     *
     * @return agent_population
     */
    public int getAgentPopulation() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAgentPopulation"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAgentPopulation"), procRID, new StringBuffer("agentPopulation"), agentPopulation, null, new StringBuffer("agent_population"), true, false, false);
        StopWatch.stop(false);
        return agentPopulation;
    }

    /**
     * Get the argument
     *
     * @return argument
     */
    public Object getArgument() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getArgument"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getArgument"), procRID, new StringBuffer("argument"), argument, null, new StringBuffer("argument"), true, false, false);
        StopWatch.stop(false);
        return argument;
    }

    /**
     * Get the Boundary Width
     *
     * @return boundary_width
     */
    public int getBoundaryWidth() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getBoundaryWidth"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getBoundaryWidth"), procRID, new StringBuffer("boundaryWidth"), boundaryWidth, null, new StringBuffer("boundary_width"), true, false, false);
        StopWatch.stop(false);
        return boundaryWidth;
    }

    /**
     * Get the class name
     *
     * @return classname
     */
    public String getClassname() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getClassname"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getClassname"), procRID, new StringBuffer("classname"), classname, null, new StringBuffer("classname"), true, false, false);
        StopWatch.stop(false);
        return classname;
    }

    /**
     * Get the destination handle
     *
     * @return dest_handle
     */
    public int getDestHandle() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getDestHandle"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getDestHandle"), procRID, new StringBuffer("destinationHandle"), destinationHandle, null, new StringBuffer("dest_handle"), true, false, false);
        StopWatch.stop(false);
        return destinationHandle;
    }

    /**
     * Get the destinations
     *
     * @return destinations
     */
    public Vector<int[]> getDestinations() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getDestinations"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getDestinations"), procRID, new StringBuffer("destinations"), destinations, null, new StringBuffer("destinations"), true, false, false);
        StopWatch.stop(false);
        return destinations;
    }

    public Vector<RemoteExchangeRequest> getExchangeReqList() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getExchangeReqList"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getExchangeReqList"), procRID, new StringBuffer("exchangeReqList"), exchangeReqList, null, null, true, false, false);
        StopWatch.stop(false);
        return exchangeReqList;
    }

    /**
     * Get the functionId
     *
     * @return functionId
     */
    public int getFunctionId() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getFunctionId"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getFunctionId"), procRID, new StringBuffer("functionId"), functionId, null, new StringBuffer("functionId"), true, false, false);
        StopWatch.stop(false);
        return functionId;
    }

    /**
     * Get the handle
     *
     * @return handle
     */
    public int getHandle() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getHandle"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getHandle"), procRID, new StringBuffer("handle"), handle, null, new StringBuffer("handle"), true, false, false);
        StopWatch.stop(false);
        return handle;
    }

    /**
     * Get the hosts
     *
     * @return *hosts
     */
    public Vector<String> getHosts() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getHosts"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getHosts"), procRID, new StringBuffer("hosts"), hosts, null, new StringBuffer("*hosts"), true, false, false);
        StopWatch.stop(false);
        return hosts;
    }

    /**
     * Get the Agent Migration Request List
     *
     * @return migrationReqList
     */
    public Vector<AgentMigrationRequest> getMigrationReqList() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getMigrationReqList"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getMigrationReqList"), procRID, new StringBuffer("migrationReqList"), migrationReqList, null, new StringBuffer("migrationReqList"), true, false, false);
        StopWatch.stop(false);
        return migrationReqList;
    }

    /**
     * Get the size
     *
     * @return size
     */
    public int[] getSize() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getSize"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getSize"), procRID, new StringBuffer("size"), size, null, new StringBuffer("size"), true, false, false);
        StopWatch.stop(false);
        return size;
    }

    /**
     * Check if argument is valid
     *
     * @return (argument != NULL)
     */
    public boolean isArgumentValid() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isArgumentValid"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isArgumentValid"), procRID, new StringBuffer("(argument_!=_null)"), (argument != null), null, new StringBuffer("(argument != NULL)"), true, false, false);
        StopWatch.stop(false);
        return (argument != null);
    }

    // Async methods
    // AGENTS_CALL_ALL_ASYNC_RETURN_OBJECT
    public Message(ACTION_TYPE action, int handle, int[] functionIds, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), new StringBuffer("label"), true, new String[]{"action", "handle", "functionIds", "argument"}, new Object[]{action, handle, functionIds, argument});

        this.action = action;
        this.handle = handle;
        this.functionIds = functionIds;
        this.argument = argument;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Message"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public int[] getFunctionIds() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getFunctionIds"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getFunctionIds"), procRID, new StringBuffer("functionIds"), functionIds, null, null, true, false, false);
        StopWatch.stop(false);
        return functionIds;
    }

    public int getSourcePid() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getSourcePid"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getSourcePid"), procRID, new StringBuffer("sourcePid"), sourcePid, null, null, true, false, false);
        StopWatch.stop(false);
        return sourcePid;
    }

    public String getActionString() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getActionString"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getActionString"), procRID, new StringBuffer("action.getValue()"), action.getValue(), null, null, true, false, false);
        StopWatch.stop(false);
        return action.getValue();
    }

    public void setAutoMigrationStartingIndex(int[] startingPlaceGlobalIndex) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setAutoMigrationStartingIndex"), new StringBuffer("label"), true, new String[]{"startingPlaceGlobalIndex"}, new Object[]{startingPlaceGlobalIndex});
        this.autoMigrateStartingIndex = startingPlaceGlobalIndex;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setAutoMigrationStartingIndex"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public int[] getAutoMigrationStartingIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAutoMigrationStartingIndex"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAutoMigrationStartingIndex"), procRID, new StringBuffer("this.autoMigrateStartingIndex"), this.autoMigrateStartingIndex, null, null, true, false, false);
        StopWatch.stop(false);
        return this.autoMigrateStartingIndex;
    }

    void setAction(ACTION_TYPE action_type) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setAction"), new StringBuffer("label"), true, new String[]{"action_type"}, new Object[]{action_type});
        this.action = action_type;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setAction"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Assigns the provided reference to the argument object
     *
     * @param argument - payload of message
     */
    public void setArgument(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setArgument"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument});
        this.argument = argument;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setArgument"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private final String UUID = ProvUtils.getUniversalResourceID(this.getClass().getSimpleName());

    @Override
    public String getOwnerUUID() {
        return UUID;
    }

    @Override
    public ProvenanceStore getStore() {
        return ProvUtils.getStoreOfCurrentThread();
    }

    @Override
    public void setStore(ProvenanceStore provenanceStore) {
        throw new UnsupportedOperationException("Migratory collection is not yet supported.");
    }

    @Override
    public void mapProvenanceCapture() {
        // ignore
    }

    @Override
    public void substituteConstructorDocumentation() {
        // ignore because mapProvenanceCapture is ignored
    }
}
