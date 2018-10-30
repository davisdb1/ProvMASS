package edu.uw.bothell.css.dsl.MASS.prov.NetworkMotif;

import java.io.Serializable;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 *
 * @author Matt Kipps (on 12/12/14), Drew Andersen (5/16/16), Delmar B. Davis
 * (provenance-enabled on 4/28/17)
 */
public class AdjacencyList implements Serializable {

    private CompactHashSet nodes;

    public AdjacencyList() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "add", "label", true, null, null, true);
        this.nodes = new CompactHashSet();
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "add", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    private AdjacencyList(AdjacencyList adjacencyList) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "AdjacencyList", "label", true, new String[]{"adjacencyList"}, new Object[]{adjacencyList}, true);
        this.nodes = adjacencyList.nodes.copy();
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "add", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public void add(int node) {
        StopWatch.start(false);
       // String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "add", "label", true, new String[]{"node"}, new Object[]{node}, true);
        nodes.add(node);
       //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "add", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public void addAll(AdjacencyList adjacencyList) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "addAll", "label", true, new String[]{"adjacencyList"}, new Object[]{adjacencyList}, true);
        CompactHashSet.Iter iter = adjacencyList.iterator();
        while (iter.hasNext()) {
            nodes.add(iter.next());
        }
       // ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "addAll", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public CompactHashSet.Iter iterator() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "iterator", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "iterator", procRID, "nodes.iterator()", nodes.iterator(), null, null, true, false, false, true);
        StopWatch.stop(false);
        return nodes.iterator();
    }

    public boolean contains(int node) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "contains", "label", true, new String[]{"node"}, new Object[]{node}, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "contains", procRID, "nodes.contains(node)", nodes.contains(node), null, null, true, false, false, true);
        StopWatch.stop(false);
        return nodes.contains(node);
    }

    public int size() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "size", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "size", procRID, "nodes.size()", nodes.size(), null, null, true, false, false, true);
        StopWatch.stop(false);
        return nodes.size();
    }

    public boolean isEmpty() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "isEmpty", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "isEmpty", procRID, "nodes.isEmpty()", nodes.isEmpty(), null, null, true, false, false, true);
        StopWatch.stop(false);
        return nodes.isEmpty();
    }

    @Override
    public String toString() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "toString", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "toString", procRID, "nodes.toString()", nodes.toString(), null, null, true, false, false, true);
        StopWatch.stop(false);
        return nodes.toString();
    }

    public boolean remove(int node) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "remove", "label", true, new String[]{"node"}, new Object[]{node}, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "remove", procRID, "nodes.remove(node)", nodes.remove(node), null, null, true, false, false, true);
        StopWatch.stop(false);
        return nodes.remove(node);
    }

    public AdjacencyList copy() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "copy", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "copy", procRID, "new_AdjacencyList(this)", new AdjacencyList(this), null, null, true, false, false, true);
        StopWatch.stop(false);
        return new AdjacencyList(this);
    }
}
