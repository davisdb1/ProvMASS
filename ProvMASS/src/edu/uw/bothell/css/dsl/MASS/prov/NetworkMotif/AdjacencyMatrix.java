package edu.uw.bothell.css.dsl.MASS.prov.NetworkMotif;

import java.io.Serializable;
import java.util.BitSet;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 *
 * @author Matt Kipps (on 12/12/14), Drew Andersen (5/16/16), Delmar B. Davis
 * (provenance-enabled on 4/28/17)
 */
public class AdjacencyMatrix implements Serializable {

    private int order;
    private BitSet matrix;

    public AdjacencyMatrix(int order) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "AdjacencyMatrix", "label", true, new String[]{"order"}, new Object[]{order}, true);
        this.order = order;
        this.matrix = new BitSet((order * (order - 1)) / 2);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "AdjacencyMatrix", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    private AdjacencyMatrix(AdjacencyMatrix source) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "AdjacencyMatrix", "label", true, new String[]{"source"}, new Object[]{source}, true);
        this.order = source.order;
        this.matrix = (BitSet) source.matrix.clone();
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "AdjacencyMatrix", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public void addEdge(int x, int y) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "addEdge", "label", true, new String[]{"x", "y"}, new Object[]{x, y}, true);
        if (x == y) {
            //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "addEdge", procRID, null, null, null, null, true, false, false, true);
            StopWatch.stop(false);
            return;
        }
        matrix.set(indexFor(x, y));
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "addEdge", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public boolean hasEdge(int x, int y) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "hasEdge", "label", true, new String[]{"x", "y"}, new Object[]{x, y}, true);
        if (x == y) {
            //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "hasEdge", procRID, null, null, null, null, true, false, false, true);
            StopWatch.stop(false);
            return true;
        }
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "hasEdge", procRID, "matrix.get(indexFor(x,_y))", matrix.get(indexFor(x, y)), null, null, true, false, false, true);
        StopWatch.stop(false);
        return matrix.get(indexFor(x, y));
    }

    public AdjacencyMatrix copy() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "copy", "label", true, null, null, true);
        AdjacencyMatrix toReturn = new AdjacencyMatrix(this);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "copy", procRID, "new_AdjacencyMatrix(this)", toReturn, null, null, true, false, false, true);
        StopWatch.stop(false);
        return toReturn;
    }

    private int indexFor(int x, int y) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "indexFor", "label", true, new String[]{"x", "y"}, new Object[]{x, y}, true);
        int n = Math.max(x, y);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "indexFor", procRID, "((n_*_(n_-_1))_/_2)_+_Math.min(x,_y)", ((n * (n - 1)) / 2) + Math.min(x, y), null, null, true, false, false, true);
        StopWatch.stop(false);
        return ((n * (n - 1)) / 2) + Math.min(x, y);
    }

    public byte[] toBytes() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "toBytes", "label", true, null, null, true);
        // Byte representation is per the graph6 format specfication
        // (http://cs.anu.edu.au/~bdm/nauty/nug25.pdf page 74)

        // code adapted from Vartika Verma's Nemo Finder project (UWB 2014)
        byte[] orderBytes = convertOrderToBytes(order);

        int bitVectorLength = (order * (order - 1)) / 2;
        int outputLength = orderBytes.length + (bitVectorLength / 6)
                + ((bitVectorLength % 6) > 0 ? 1 : 0);

        byte[] output = new byte[outputLength];
        System.arraycopy(orderBytes, 0, output, 0, orderBytes.length);

        int currentBit = 0;
        int currentIndex = orderBytes.length;
        byte currentByte = 0;
        for (int col = 1; col < order; col++) {
            for (int row = 0; row < col; row++) {
                if (hasEdge(row, col)) {
                    currentByte = (byte) (currentByte | (1 << (5 - currentBit)));
                }

                // increment the bit
                currentBit = (currentBit + 1) % 6;

                if (currentBit == 0) {
                    // add byte to output (increment by 63
                    // according to the graph6 algorithm)
                    output[currentIndex] = (byte) (currentByte + 63);
                    currentIndex++;
                    currentByte = 0;
                }
            }
        }

        // complete last byte
        if (currentIndex < outputLength) {
            output[currentIndex] = (byte) (currentByte + 63);
        }

        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "toBytes", procRID, "output", output, null, null, true, false, false, true);
        StopWatch.stop(false);
        return output;
    }

    private static byte[] convertOrderToBytes(int order) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, "convertOrderToBytes", "label", true, new String[]{"order"}, new Object[]{order}, true);
        // Per the graph6 format specfication
        // (http://cs.anu.edu.au/~bdm/nauty/nug25.pdf page 74)

        // code adapted from Vartika Verma's Nemo Finder project (UWB 2014)
        byte[] bytes;

        if (order <= 62) {
            bytes = new byte[1];
            bytes[0] = (byte) (order + 63);
        } else if (order <= 258047) {
            bytes = new byte[4];
            bytes[0] = 126;
            bytes[1] = (byte) ((order >>> 12) & 63);
            bytes[2] = (byte) ((order >>> 6) & 63);
            bytes[3] = (byte) (order & 63);
        } else {
            bytes = new byte[8];
            bytes[0] = 126;
            bytes[1] = 126;
            bytes[2] = (byte) ((order >>> 30) & 63);
            bytes[3] = (byte) ((order >>> 24) & 63);
            bytes[4] = (byte) ((order >>> 18) & 63);
            bytes[5] = (byte) ((order >>> 12) & 63);
            bytes[6] = (byte) ((order >>> 6) & 63);
            bytes[7] = (byte) (order & 63);
        }

        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, "convertOrderToBytes", procRID, "bytes", bytes, null, null, true, false, false, true);
        StopWatch.stop(false);
        return bytes;
    }
}
