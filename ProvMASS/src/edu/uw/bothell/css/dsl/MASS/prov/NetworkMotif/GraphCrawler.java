package edu.uw.bothell.css.dsl.MASS.prov.NetworkMotif;

import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledAgent;
import edu.uw.bothell.css.dsl.MASS.prov.core.Agent;
import edu.uw.bothell.css.dsl.MASS.prov.filter.AgentFilter;
import java.io.Serializable;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 *
 * @author Matt Kipps (on 12/12/14), Drew Andersen (5/16/16), Delmar B. Davis
 * (provenance-enabled on 4/28/17)
 */
public class GraphCrawler extends Agent implements ProvEnabledAgent {

    public static final int update_ = 0;
    private final StringBuffer UUID = ProvUtils.getUniversalResourceID(new StringBuffer(this.getClass().getSimpleName()));
    private Object transitiveProvenanceStateData;

    public Object callMethod(int functionID, Object args) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callMethod"), new StringBuffer("label"), true, new String[]{"functionID", "args"}, new Object[]{functionID, args}, true);
        switch (functionID) {
            case update_:
                update();
                break;
            default:
                break;
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callMethod"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    private int migrateTo;
    private Subgraph subgraph;
    private AdjacencyList extension;

    public GraphCrawler(Object rawArgs) {
        super();
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("GraphCrawler"), new StringBuffer("label"), true, new String[]{"rawArgs"}, new Object[]{rawArgs}, true);
        Constructor constructor = (Constructor) rawArgs;
        this.migrateTo = constructor.getMigrateTo();
        this.subgraph = constructor.getSubgraph();
        this.extension = constructor.getExtension();
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("GraphCrawler"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    @Override
    public void substituteConstructorDocumentation() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("GraphCrawler"), new StringBuffer("label"), true, new String[]{"rawArgs"}, new Object[]{(GraphCrawler.Constructor) (new Object())}, true);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("GraphCrawler"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public void update() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("update"), new StringBuffer("label"), true, null, null, true);
        if (migrateTo != -1) {
            migrate(migrateTo);
            migrateTo = -1;
            return;
        }

        // 'v' is equal to the subgraph root, unless it is empty, then
        // 'v' is equal to 'w' ('w' is the current node).
        GraphNode node = getNode();
        int w = node.getIndex()[0];
        int v;
        if (subgraph.size() == 0) {
            v = w;
        } else {
            v = subgraph.root();
        }

        // add the current node to the subgraph
        if (subgraph.size() == subgraph.order() - 1) {
            subgraph.add(w, node.getAdjacencyList());
            node.addToSubgraphs(subgraph);
            kill();
            return;
        }

        // examine each node 'u' from the set of nodes adjacent to 'w',
        // and add it to the next extension if it is exclusive to the
        // subgraph, and greater than 'v'
        CompactHashSet.Iter uIter = node.getAdjacencyList().iterator();
        while (uIter.hasNext()) {
            int u = uIter.next();
            if (u > v) {
                if (subgraph.excludes(u)) {
                    extension.add(u);
                }
            }
        }

        if (extension.isEmpty()) {
            kill();
            return;
        }

        // add the current node to the subgraph
        subgraph.add(w, node.getAdjacencyList());

        // extend the subgraph
        CompactHashSet.Iter iter = extension.iterator();
        if (extension.size() > 1) {
            Object[] spawnParams = new Object[extension.size() - 1];
            for (int i = 0; i < spawnParams.length; i++) {
                int spawnAt = iter.next();
                iter.remove();
                spawnParams[i] = (Object) (new GraphCrawler.Constructor(
                        spawnAt,
                        subgraph.copy(),
                        (subgraph.size() < subgraph.order() - 1) ? extension.copy() : null));
            }

            spawn(spawnParams.length, spawnParams);
        }

        // pick the destination for this crawler
        int destination = iter.next();
        iter.remove();

        // migrate to the new destination
        migrate(destination);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("update"), procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    private GraphNode getNode() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getNode"), new StringBuffer("label"), true, null, null, true);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getNode"), procRID, new StringBuffer("(GraphNode)_getPlace()"), (GraphNode) getPlace(), null, null, true, false, false, true);
        StopWatch.stop(false);
        return (GraphNode) getPlace();
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
        AgentFilter.filter(MASSProv.agentFilter, this, MASSProv.agentFilterCriteria);
    }

    // the nested Constructor class simplifies the instantiation of GraphCrawler
    // objects through MASS library calls
    public static class Constructor implements Serializable {

        private int migrateTo;
        private int motifSize;
        private Subgraph subgraph;
        private AdjacencyList extension;

        public Constructor(int motifSize) {
            this(-1, motifSize, null, null);
        }

        public Constructor(
                int migrateTo,
                Subgraph subgraph,
                AdjacencyList extension) {
            this(migrateTo, subgraph.order(), subgraph, extension);
        }

        private Constructor(
                int migrateTo,
                int motifSize,
                Subgraph subgraph,
                AdjacencyList extension) {
            this.migrateTo = migrateTo;
            this.motifSize = motifSize;
            this.subgraph = subgraph;
            this.extension = extension;
        }

        public int getMigrateTo() {
            return migrateTo;
        }

        public Subgraph getSubgraph() {
            if (subgraph == null) {
                return new Subgraph(motifSize);
            } else {
                return subgraph;
            }
        }

        public AdjacencyList getExtension() {
            if (extension == null) {
                return new AdjacencyList();
            } else {
                return extension;
            }
        }
    }
}
