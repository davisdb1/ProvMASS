package edu.uw.bothell.css.dsl.MASS.prov.NetworkMotif;

import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * This class represents a single collection of graph nodes, referred to by node
 * index values.
 *
 * @author Matt Kipps (on 12/12/14), Drew Andersen (5/16/16), Delmar B. Davis
 * (provenance-enabled on 4/28/17)
 */
public class Subgraph implements Serializable {

    private int[] nodes;
    private AdjacencyMatrix matrix;
    private AdjacencyList adjacencyList;
    private int currentSize;

    public Subgraph(int order) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "add", "label", true, new String[]{"order"}, new Object[]{order}, true);
        // 'order' refers to the number of nodes the subgraph will contain
        this.currentSize = 0;
        this.nodes = new int[order];
        this.matrix = new AdjacencyMatrix(order);
        this.adjacencyList = new AdjacencyList();
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "Subgraph", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public Subgraph copy() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "copy", "label", true, null, null, true);
        Subgraph copy = new Subgraph(order());
        copy.currentSize = currentSize;
        for (int i = 0; i < size(); i++) {
            copy.nodes[i] = nodes[i];
        }
        copy.matrix = this.matrix.copy();
        copy.adjacencyList = this.adjacencyList.copy();
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "copy", procRID, "copy", copy, null, null, true, false, false, true);
        StopWatch.stop(false);
        return copy;
    }

    public int size() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "size", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "size", procRID, "currentSize", currentSize, null, null, true, false, false, true);
        StopWatch.stop(false);
        return currentSize;
    }

    public int order() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "order", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "order", procRID, "nodes.length", nodes.length, null, null, true, false, false, true);
        StopWatch.stop(false);
        return nodes.length;
    }

    public boolean isComplete() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "isComplete", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "isComplete", procRID, "size()_==_order()", size() == order(), null, null, true, false, false, true);
        StopWatch.stop(false);
        return size() == order();
    }

    public int root() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "root", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "root", procRID, "nodes[0]", nodes[0], null, null, true, false, false, true);
        StopWatch.stop(false);
        return nodes[0];
    }

    public void add(int index, AdjacencyList adjacentNodes) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "add", "label", true, new String[]{"index", "adjacentNodes"}, new Object[]{index, adjacentNodes}, true);
        nodes[currentSize] = index;

        // update the matrix
        for (int i = 0; i < currentSize; i++) {
            if (adjacentNodes.contains(get(i))) {
                matrix.addEdge(i, currentSize);
            }
        }

        // update the set of adjacent nodes (including the subgraph itself)
        adjacencyList.add(index);
        adjacencyList.addAll(adjacentNodes);

        currentSize++;
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "add", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public boolean excludes(int index) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "excludes", "label", true, new String[]{"index"}, new Object[]{index}, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "excludes", procRID, "!adjacencyList.contains(index)", !adjacencyList.contains(index), null, null, true, false, false, true);
        StopWatch.stop(false);
        return !adjacencyList.contains(index);
    }

    public int get(int index) {
        StopWatch.start(false);
       // String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "get", "label", true, new String[]{"index"}, new Object[]{index}, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "get", procRID, "nodes[index]", nodes[index], null, null, true, false, false, true);
        StopWatch.stop(false);
        return nodes[index];
    }

    @Override
    public String toString() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "toString", "label", true, null, null, true);
        String s = "[";
        for (int i = 0; i < size(); i++) {
            s = s + get(i) + ", ";
        }
        s = s + "]";
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "toString", procRID, "s", s, null, null, true, false, false, true);
        StopWatch.stop(false);
        return s;
    }

    private void readObject(ObjectInputStream ois)
            throws IOException, ClassNotFoundException {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "readObject", "label", true, new String[]{"ois"}, new Object[]{ois}, true);
        currentSize = ois.readInt();
        nodes = new int[ois.readInt()];
        for (int i = 0; i < currentSize; i++) {
            nodes[i] = ois.readInt();
        }
        matrix = (AdjacencyMatrix) ois.readObject();
        adjacencyList = (AdjacencyList) ois.readObject();
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "readObject", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "writeObject", "label", true, new String[]{"oos"}, new Object[]{oos}, true);
        oos.writeInt(currentSize);
        oos.writeInt(nodes.length);
        for (int i = 0; i < currentSize; i++) {
            oos.writeInt(nodes[i]);
        }
        oos.writeObject(matrix);
        oos.writeObject(adjacencyList);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "writeObject", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public String getByteString() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "getByteString", "label", true, null, null, true);
        try {
            //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "getByteString", procRID, "new String(matrix.toBytes(), \\\"UTF-8\\\")", new String(matrix.toBytes(), "UTF-8"), null, null, true, false, false, true);
            return new String(matrix.toBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unable to convert to graph6 format...");
            System.exit(-1);
           //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "getByteString", procRID, "null", null, null, null, true, false, false, true);
            StopWatch.stop(false);
            return null;
        }
    }
}
