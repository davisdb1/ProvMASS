package edu.uw.bothell.css.dsl.MASS.prov.NetworkMotif;

import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledObject;
import edu.uw.bothell.css.dsl.MASS.prov.core.Place;
import edu.uw.bothell.css.dsl.MASS.prov.filter.PlaceFilter;
import java.util.HashMap;
import java.util.Map;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 *
 * @author Matt Kipps (on 12/12/14), Drew Andersen (5/16/16), Delmar B. Davis
 * (provenance-enabled on 4/28/17)
 */
public class GraphNode extends Place implements ProvEnabledObject {

    public static final int collectSubgraphs_ = 0;
    public static final int initializeEdges_ = 1;
    private final StringBuffer UUID = ProvUtils.getUniversalResourceID(new StringBuffer(this.getClass().getSimpleName()));

    public Object callMethod(int functionID, Object args) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callMethod"), new StringBuffer("label"), true, new String[]{"functionID", "args"}, new Object[]{functionID, args}, true);
        switch (functionID) {
            case initializeEdges_:
                AdjacencyList edges = (AdjacencyList) args;
                initializeEdges(edges);
                break;
            case collectSubgraphs_:
                return (Object) collectSubgraphs();
            default:
                break;
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callMethod"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    private int networkSize;
    private AdjacencyList adjacencyList;
    private Map<String, Integer> subgraphs;

    // quick note: this doesn't seem to be called... just the default constructor
    // maybe the custom mass jar calls this with the rawArgs Constructor return
    // in which case, the simulation might not accurately portray the network motif
    public GraphNode(Object rawArgs) {
        super();
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("GraphNode"), new StringBuffer("label"), true, new String[]{"rawArgs"}, new Object[]{rawArgs}, true);
        Constructor constructor = (Constructor) rawArgs;
        this.networkSize = constructor.getNetworkSize();
        this.subgraphs = new HashMap<String, Integer>();
        this.adjacencyList = null;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("GraphNode"), procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    @Override
    public void substituteConstructorDocumentation() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("GraphNode"), new StringBuffer("label"), true, new String[]{"rawArgs"}, new Object[]{(GraphNode.Constructor) new Object()}, true);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("GraphNode"), procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public int getNetworkSize() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getNetworkSize"), new StringBuffer("label"), true, null, null, true);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getNetworkSize"), procRID, new StringBuffer("networkSize"), networkSize, null, null, true, false, false, true);
        StopWatch.stop(false);
        return networkSize;
    }

    public Map<String, Integer> collectSubgraphs() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("collectSubgraphs"), new StringBuffer("label"), true, null, null, true);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("collectSubgraphs"), procRID, new StringBuffer("subgraphs"), subgraphs, null, null, true, false, false, true);
        StopWatch.stop(false);
        return subgraphs;
    }

    // set all the network edges for this node
    public void initializeEdges(AdjacencyList adjacencyList) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("initializeEdges"), new StringBuffer("label"), true, new String[]{"adjacencyList"}, new Object[]{adjacencyList}, true);
        this.adjacencyList = adjacencyList;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("initializeEdges"), procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public void addToSubgraphs(Subgraph subgraph) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("addToSubgraphs"), new StringBuffer("label"), true, new String[]{"subgraph"}, new Object[]{subgraph}, true);
        String repr = subgraph.getByteString();
        int count = 1;
        synchronized (subgraphs) {
            if (subgraphs.containsKey(repr)) {
                count += subgraphs.get(repr);
            }
            subgraphs.put(repr, count);
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("addToSubgraphs"), procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public AdjacencyList getAdjacencyList() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAdjacencyList"), new StringBuffer("label"), true, null, null, true);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAdjacencyList"), procRID, new StringBuffer("adjacencyList"), adjacencyList, null, null, true, false, false, true);
        StopWatch.stop(false);
        return adjacencyList;
    }
//
//    public int getIndex() {
//        return index[0];
//    }

    @Override
    public String getOwnerUUID() {
        if (UUID == null) {
            return null;
        } else {
            return UUID.toString();
        }
    }

    @Override
    public ProvenanceStore getStore() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStore(ProvenanceStore provenanceStore) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mapProvenanceCapture() {
        PlaceFilter.filter(MASSProv.placeFilter, this, MASSProv.placeFilterCriteria);
    }

    // the nested Constructor class simplifies the instantiation of GraphNode
    // objects through MASS library calls
    public static class Constructor implements java.io.Serializable {

        private int networkSize;

        public Constructor(int networkSize) {
            this.networkSize = networkSize;
        }

        public int getNetworkSize() {
            return networkSize;
        }
    }
}
