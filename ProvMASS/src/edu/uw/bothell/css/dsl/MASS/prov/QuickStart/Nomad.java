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
package edu.uw.bothell.css.dsl.MASS.prov.QuickStart;
// PERFORMANCE DOCUMENTED 
// CAPTURE MOVED TO CURRENT THREAD AS OWNER

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledAgent;
import edu.uw.bothell.css.dsl.MASS.prov.ProvOntology;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import java.net.InetAddress;

import edu.uw.bothell.css.dsl.MASS.prov.core.Agent;
import edu.uw.bothell.css.dsl.MASS.prov.filter.AgentFilter;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvStaging;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.store.ResourceMatcher;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Random;

public class Nomad extends Agent implements ProvEnabledAgent {

    private Object obj;
    private final StringBuffer obj_RID = ProvUtils.getUniversalResourceID(new StringBuffer("obj"));
    private HashSet<String> identifiedAt = new HashSet<>();
    String originHost = "UNKNOWN_HOST";
    final StringBuffer originHost_RID = ProvUtils.getUniversalResourceID(new StringBuffer("originHost"));
    public static final int GET_HOSTNAME = 0;
    public static final StringBuffer GET_HOSTNAME_RID = ProvUtils.getUniversalResourceID(new StringBuffer("GET_HOSTNAME"));
    public static final int MIGRATE = 1;
    public static final StringBuffer MIGRATE_RID = ProvUtils.getUniversalResourceID(new StringBuffer("MIGRATE"));
    public static final int RANDOM_MIGRATE = 2;
    public static final StringBuffer RANDOM_MIGRATE_RID = ProvUtils.getUniversalResourceID(new StringBuffer("RANDOM_MIGRATE"));
    public static boolean hasntBeenDebugged = false;
    public static boolean debug = false;
    private StringBuffer UUID = ProvUtils.getUniversalResourceID(new StringBuffer(this.getClass().getSimpleName()));
    private Object transitiveProvenanceStateData;

    /**
     * This constructor will be called upon instantiation by MASS The Object
     * supplied MAY be the same object supplied when Places was created
     *
     * @param obj
     */
    public Nomad(Object obj) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Nomad"), new StringBuffer("constructor"), false, null, null, true);
        this.obj = obj;
        ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("obj"), obj_RID, procRID, this.obj, true);
        try {
            originHost = java.net.InetAddress.getLocalHost().getHostName();
            ProvenanceRecorder.documentFieldAssignment(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("originHost"), originHost_RID, originHost, procRID, true);
        } catch (UnknownHostException ex) {
            ex.printStackTrace(IO.getLogWriter());
        }
        ProvenanceRecorder.documentAgent(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer(this.getClass().getSimpleName()));
        if (this.provOn) {
            identifiedAt.add(ProvUtils.getHostName());
        }
        ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Nomad"), procRID, null, null, null, null, false, false, false, true);
        StopWatch.stop(false);
    }

    @Override
    public void substituteConstructorDocumentation() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Nomad"), new StringBuffer("constructor"), false, null, null, true);
        ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("obj"), obj_RID, procRID, this.obj, true);
        ProvenanceRecorder.documentFieldAssignment(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("originHost"), originHost_RID, originHost, procRID, true);
        ProvenanceRecorder.documentAgent(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer(this.getClass().getSimpleName()));
        identifiedAt.add(ProvUtils.getHostName());
        ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Nomad"), procRID, null, null, null, null, false, false, false, true);
        StopWatch.stop(false);
    }

    /**
     * This method is called when "callAll" is invoked from the master node
     */
    @Override
    public Object callMethod(int method, Object o) {
        StopWatch.start(false);
        Object toReturn = null;
        if (MASSProv.provOn && !identifiedAt.contains(ProvUtils.getHostName())) {
            identifiedAt.add(ProvUtils.getHostName());
            ProvenanceRecorder.documentAgent(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer(this.getClass().getSimpleName()));
        }
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), null, true, new String[]{"method", "o"}, new Object[]{method, o}, true);
//        StringBuffer procRID = ProvenanceRecorder.documentProcedure(store, this,
//                "stubProcName"), new StringBuffer("generic proc call, change proc name and parameters"), false, new String[]{"param1"), new StringBuffer("param2"},
//                new Object[]{"value1"), new StringBuffer("value2"});
        switch (method) {
            case GET_HOSTNAME:
                ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("GET_HOSTNAME"), GET_HOSTNAME_RID, procRID, GET_HOSTNAME, true);
                toReturn = findHostName(o);
                break;
            case MIGRATE:
                ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("MIGRATE"), MIGRATE_RID, procRID, MIGRATE, true);
                toReturn = move(o);
                break;
            case RANDOM_MIGRATE:
                ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("RANDOM_MIGRATE"), RANDOM_MIGRATE_RID, procRID, RANDOM_MIGRATE, true);
                toReturn = moveRandom(o);
                break;
            default:
                toReturn = new String("Unknown Method Number: " + method);
                break;
        }
//        ProvenanceRecorder.endProcedureDocumentation(store,
//            this, new StringBuffer("stubProcName"), procRID, new StringBuffer("returnReference"),
//            "returnValue"), null, new StringBuffer("this is a stub, replace return and method name"),
//            true, false,
//            false);
        ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), procRID, new StringBuffer("toReturn"), toReturn, null, null, true, false, false, true);
        StopWatch.stop(false);
        return toReturn;
    }

    /**
     * Return a String identifying where this Agent is actually located
     *
     * @param o
     * @return
     */
    public Object findHostName(Object o) {
        StopWatch.start(false);
        String toReturn = null;
        ProvenanceStore store = ProvUtils.getStoreOfCurrentThread(provOn);
        Object caller = this;
        StringBuffer procName = new StringBuffer("findHostName");
        boolean alwaysPushActivityID = false;
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn, store, caller, procName, null, alwaysPushActivityID, true);
        try {
            // new n-dimensional index message
            int[] index = getIndex();
            String logicalLocation = index.length > 0 ? "" : "none";
            for (int i = 0, im = index.length; i < im; i++) {
                logicalLocation += index[i];
                if (i + 1 < im) {
                    logicalLocation += "),";
                }
            }
            toReturn = new StringBuilder("Agent originating from ")
                    .append(originHost)
                    .append(" is currently on host: ")
                    .append(InetAddress.getLocalHost().getCanonicalHostName())
                    .append(" and logically located at Place index: ")
                    .append(logicalLocation).toString();
            ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("originHost"), originHost_RID, procRID, originHost, true);
        } catch (Exception e) {
            toReturn = "Error : " + e.getLocalizedMessage() + e.getStackTrace();
        }
//        
//        // <editor-fold defaultstate="collapsed" desc="prov">
//        // the value of toReturn is...
//        if (store != null) {
//            store.addRelationalProv(toReturnID,
//                    ProvOntology.getValueExpandedPropertyFullURI(), toReturn);
//            matcher.pushEntityID(toReturnID);
//            matcher.pushActivityID(methodResourceID);
//        }
//        // </editor-fold>
//        
        StringBuffer returnObjectName = new StringBuffer("toReturn");
        Object returnObject = toReturn;
        StringBuffer returnObjectRID = null;
        boolean procGeneratedReturn = true;
        boolean alwaysStackReturnID = true;
        boolean dontPopActivityID = false;
        ProvenanceRecorder.endProcedureDocumentation(this.provOn, store, caller, procName, procRID, returnObjectName, returnObject, returnObjectRID, null, procGeneratedReturn, false, dontPopActivityID, true);
        StopWatch.stop(false);
        return toReturn;
    }

    private void startProvAdditionDebugging() {
        StopWatch.start(false);
        // start debugging?
        if (Nomad.hasntBeenDebugged && Nomad.debug) {
            IO.log("\n------------------\nSTARTING NOMAD DEBUG\n------------------\n");
            Nomad.debug = true;
            ProvUtils.getStoreOfCurrentThread(provOn).debug = true;
            Nomad.hasntBeenDebugged = false;
        }
        StopWatch.stop(false);
    }

    private void endProvAdditionDebugging(ProvenanceStore.StorageStatus status,
            String methodResourceID, boolean leaveNomadProvDebuggingOn) {
        StopWatch.start(false);
        // end debugging?
        if (Nomad.debug) {
            if (status == ProvenanceStore.StorageStatus.STORED) {
                IO.log("Added: " + ProvStaging.ProvOString(methodResourceID,
                        ProvOntology.getRDFTypeFullURI(),
                        ProvOntology.getActivityStartingPointClassFullURI()));
            } else {
                IO.log("Storage of " + methodResourceID + " as a RDF subject failed");
                IO.log("The line of provenance added would have been: "
                        + ProvStaging.ProvOString(methodResourceID,
                                ProvOntology.getRDFTypeFullURI(),
                                ProvOntology.getActivityStartingPointClassFullURI()));
                IO.log("The storage status is: " + status.toString());
            }
            Nomad.debug = leaveNomadProvDebuggingOn; // debug only 1 time
            ProvUtils.getStoreOfCurrentThread(provOn).debug = false; // turn debugging off in the store
            IO.log("\n------------------\nENDING NOMAD DEBUG\n------------------\n");
        }
        StopWatch.stop(false);
    }

    /**
     * Move this Agent to the next position in the X-coordinate
     *
     * @param o
     * @return
     */
    public Object move(Object o) {
        StopWatch.start(false);
        // <editor-fold defaultstate="collapsed" desc="prov">
        StringBuffer callingMethodResourceID = null;
        StringBuffer methodResourceID = null;
        ResourceMatcher matcher = ResourceMatcher.getMatcher();
        ProvenanceStore store = null;
        if (MASSProv.provOn) {
            store = ProvUtils.getStoreOfCurrentThread(provOn);
        }
        if (store != null) {
            // get the id of the calling method
            callingMethodResourceID = matcher.popActivityIDBuffer();
            // get an id for this method
            methodResourceID = ProvUtils.getLocationAwareURID(new StringBuffer("move"));
            // this method is an activity
            store.addRelationalProv(methodResourceID, ProvOntology.getRDFTypeFullURIBuffer(), ProvOntology.getActivityStartingPointClassFullURIBuffer());
            // this method was started by the calling method
            store.addRelationalProv(methodResourceID, ProvOntology.getWasStartedByExpandedPropertyFullURIBuffer(), callingMethodResourceID);
        }
        StringBuffer toReturnID = null;
        // get an id for toReturn;
        if (store != null) {
            toReturnID = ProvUtils.getLocationAwareURID(new StringBuffer("o"));
            // toReturn is an entity
            store.addRelationalProv(toReturnID, ProvOntology.getRDFTypeFullURIBuffer(), ProvOntology.getEntityStartingPointClassFullURIBuffer());
            // toReturn was generated by this method
            store.addRelationalProv(toReturnID, ProvOntology.getWasGeneratedByStartingPointPropertyFullURIBuffer(), methodResourceID);
        }
        // </editor-fold>
        int xModifier = this.getPlace().getIndex()[0];
        int yModifier = this.getPlace().getIndex()[1];
        int zModifier = this.getPlace().getIndex()[2];
        xModifier++;

        migrate(xModifier, yModifier, zModifier);
        // <editor-fold defaultstate="collapsed" desc="prov">
        // the value of toReturn is...
        if (store != null) {
            StringBuffer value = new StringBuffer("null");
            if (o != null) {
                value = new StringBuffer(o.toString());
            }
            store.addRelationalProv(toReturnID, ProvOntology.getValueExpandedPropertyFullURIBuffer(), value);
            matcher.pushEntityID(toReturnID);
            matcher.pushActivityID(methodResourceID);
        }
        // </editor-fold>
        StopWatch.stop(false);
        return o;
    }

    /**
     * Moves this agent to a Place with a random x position within the
     * containing Places.
     *
     * @param o - null
     * @return void
     */
    public Object moveRandom(Object o) {
        StopWatch.start(false);
        // <editor-fold defaultstate="collapsed" desc="prov">
        int[] agentIdx = getIndex();
        // </editor-fold>
        int[] randomPositions = generateRandomMigratePosition();
        boolean migrated = migrate(randomPositions);
        ProvenanceStore store = null;
        if (MASSProv.provOn) {
            store = ProvUtils.getStoreOfCurrentThread(provOn);
        }
        // <editor-fold defaultstate="collapsed" desc="prov">
        if (store != null) {
            StringBuffer label = null;
            if (migrated && agentIdx != null) {
                label = new StringBuffer("migrated ");
                String delimiter = "),";
                if (agentIdx.length > 0) {
                    label.append("from [");
                    for (int i = 0, im = agentIdx.length; i < im; i++) {
                        label.append(agentIdx[i]);
                        if (i + 1 < im) {
                            label.append(delimiter);
                        } else {
                            label.append("] ");
                        }
                    }
                }
                agentIdx = getIndex(); // new index set in procedure: migrate
                if (agentIdx.length > 0) {
                    label.append(" to [");
                    for (int i = 0, im = agentIdx.length; i < im; i++) {
                        label.append(agentIdx[i]);
                        if (i + 1 < im) {
                            label.append(delimiter);
                        } else {
                            label.append("]");
                        }
                    }
                }
            } else {
                label = new StringBuffer("Migration Failed");
            }
            ProvenanceRecorder.endProcedureDocumentation(this.provOn, store, this, new StringBuffer("moveRandom"), null, new StringBuffer("o"), o, null, label, false, true, true, true);
        }
        // </editor-fold>
        StopWatch.stop(false);
        return o;
    }

    private int[] generateRandomMigratePosition() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("generateRandomMigratePosition"), null, true, null,
                null, true);
        int[] dimensionSizes = new int[getPlace().getSize().length];
        int[] randomPositions = new int[dimensionSizes.length];
        for (int i = 0, im = dimensionSizes.length; i < im; i++) {
            dimensionSizes[i] = getPlace().getSize()[i];
            randomPositions[i] = 0;
            if (dimensionSizes[i] > 1) {
                randomPositions[i] = new Random().nextInt(dimensionSizes[i]);
            }
        }
        StopWatch.stop(false);
        ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("generateRandomMigratePosition"), procRID, new StringBuffer("randomPositions"), (Object) randomPositions, null, null, true, false, false, true);
        return randomPositions;
    }

    // <editor-fold defaultstate="collapsed" desc="ProvEnabledAgent Procedures">
    @Override
    public ProvenanceStore getStore() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStore(ProvenanceStore provenanceStore) {
        throw new UnsupportedOperationException();
    }

@Override
    public String getOwnerUUID() {
        if(UUID == null) {
            return null;
        }else {
            return UUID.toString();
        }        
    }

    @Override
    public void storeTransitiveProvenanceState(Object stateData) {
        transitiveProvenanceStateData = stateData;
    }

    @Override
    public Object retrieveTransitiveProvenanceState() {
        return transitiveProvenanceStateData;
    }

    @Override
    public void mapProvenanceCapture() {
        AgentFilter.filter(MASSProv.agentFilter, this, MASSProv.agentFilterCriteria);
    }

    // </editor-fold>
    // OVERRIDE map function
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
    @Override
    public int map( int initPopulation, int[] size, int[] index ) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("map"), new StringBuffer("label"), true, new String[]{"initPopulation", "size", "index"}, new Object[]{initPopulation, size, index});

        // compute the total # places
        int placeTotal = 1;
        for (int x = 0; x < size.length; x++) {
            placeTotal *= size[x];
        }

        // compute the global linear index
        int linearIndex = getLinearIndexFromArrayIndex(index, size);

        // compute #agents per place a.k.a. colonists
        int colonists = initPopulation / placeTotal; // agents per place
        int remainders = initPopulation % placeTotal; // this stuff says that the positive ratio of agents to places is the minimum agents per place and then...
//        if (linearIndex < remainders) { // if the coordinates of place can accomodate more agents...
//            colonists++; // add a remainder             // then let them...
//        }

        // instead... we want to know if the linear index of the place should take an evenly spaced agent
        // in other words is placeTotal divided by remainders even? 
        // For example 10 places 105 agents,  we start with at least 10 agents 
        // per place with 5 left over to distributed across places (which is 10)
        // so 10 / 5 is 2, 
        if(remainders != 0) {
            int placeForRemainder = placeTotal / remainders; // note using this ignores any additional agents over the even divide thanks to integer division
            // if the place index can be evenly divided by 2 then an additional agent should go there
            if(linearIndex % placeForRemainder == 0){
                colonists++;
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("map"), procRID, new StringBuffer("colonists"), colonists, null, new StringBuffer(""), true, false, false);
        StopWatch.stop(false);
        return colonists;
    }
}
