package edu.uw.bothell.css.dsl.MASS.prov.NetworkMotif;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.NoSuchElementException;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 *
 * @author Matt Kipps (on 12/12/14), Drew Andersen (5/16/16), Delmar B. Davis
 * (provenance-enabled on 4/28/17)
 */
public class CompactHashSet implements Serializable {

    private static final int DEFAULT_CAPACITY = 33;
    private static final int STARTING_BUCKET_SIZE = 4;
    private static final int NULL_ELEMENT = -1;

    private int[][] table;
    private int size;

    public CompactHashSet() {
        this(DEFAULT_CAPACITY);
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "CompactHashSet", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "CompactHashSet", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public CompactHashSet(int tableSize) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "CompactHashSet", "label", true, new String[]{"tableSize"}, new Object[]{tableSize}, true);
        if (tableSize < 0) {
            throw new IllegalArgumentException(
                    "Argument out of range (must be non-negative).");
        }

        size = 0;
        table = new int[(tableSize == 0 ? 1 : tableSize)][];
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "CompactHashSet", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public CompactHashSet copy() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "copy", "label", true, null, null, true);
        CompactHashSet copy = new CompactHashSet(table.length);
        Iter iter = iterator();
        while (iter.hasNext()) {
            copy.add(iter.next());
        }
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "copy", procRID, "copy", copy, null, null, true, false, false, true);
        StopWatch.stop(false);
        return copy;
    }

    public int size() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "size", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "size", procRID, "size", size, null, null, true, false, false, true);
        StopWatch.stop(false);
        return size;
    }

    public boolean isEmpty() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "isEmpty", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "isEmpty", procRID, "size()_==_0", size() == 0, null, null, true, false, false, true);
        StopWatch.stop(false);
        return size() == 0;
    }

    public void add(int element) {
        StopWatch.start(false);
       // String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "add", "label", true, new String[]{"element"}, new Object[]{element}, true);
        if (element < 0) {
            throw new IllegalArgumentException(
                    "Argument out of range (must be non-negative).");
        }

        int bucket = hash(element) % table.length;
        if (table[bucket] == null) {

            // create a bucket if it does not exist
            table[bucket] = new int[STARTING_BUCKET_SIZE];
            for (int i = 1; i < table[bucket].length; i++) {
                table[bucket][i] = NULL_ELEMENT;
            }

            // add element to bucket
            table[bucket][0] = element;
            size++;
            return;
        }

        // check bucket if element already exists
        for (int i = 0; i < table[bucket].length; i++) {
            if (table[bucket][i] == element) {
                return;
            }
        }

        // try to add element if there is space
        for (int i = 0; i < table[bucket].length; i++) {
            if (table[bucket][i] == NULL_ELEMENT) {
                table[bucket][i] = element;
                size++;
                return;
            }
        }

        // otherwise grow the bucket and add to first new position
        int previousLength = table[bucket].length;
        grow(bucket);
        table[bucket][previousLength] = element;
        size++;
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "add", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public boolean contains(int element) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "contains", "label", true, new String[]{"element"}, new Object[]{element}, true);
        if (element < 0) {
            return false;
        }

        if (size() == 0) {
            return false;
        }

        int bucket = hash(element) % table.length;
        if (table[bucket] == null) {
            return false;
        } else {
            for (int index = 0; index < table[bucket].length; index++) {
                if (table[bucket][index] == element) {
                    return true;
                }
            }
           // ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "contains", procRID, "false", false, null, null, true, false, false, true);
            StopWatch.stop(false);
            return false;
        }
    }

    public boolean remove(int element) {
        StopWatch.start(false);
       // String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "remove", "label", true, new String[]{"element"}, new Object[]{element}, true);
        if (element < 0) {
            return false;
        }

        int bucket = hash(element) % table.length;
        if (table[bucket] == null) {
            return false;
        } else {
            for (int index = 0; index < table[bucket].length; index++) {
                if (table[bucket][index] == element) {
                    table[bucket][index] = NULL_ELEMENT;
                    size--;
                    return true;
                }
            }
           // ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "remove", procRID, "false", false, null, null, true, false, false, true);
            StopWatch.stop(false);
            return false;
        }
    }

    // increase the size of the bucket at the given index
    private void grow(int bucketIndex) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "grow", "label", true, new String[]{"bucketIndex"}, new Object[]{bucketIndex}, true);
        // double the bucket size
        int[] newBucket = new int[table[bucketIndex].length * 2];

        int index = 0;
        for (; index < table[bucketIndex].length; index++) {
            newBucket[index] = table[bucketIndex][index];
        }
        for (; index < newBucket.length; index++) {
            newBucket[index] = NULL_ELEMENT;
        }
        table[bucketIndex] = newBucket;
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "grow", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    private int hash(int element) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "hash", "label", true, new String[]{"element"}, new Object[]{element}, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "hash", procRID, "element", element, null, null, true, false, false, true);
        StopWatch.stop(false);
        return element;
    }

    public Iter iterator() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "iterator", "label", true, null, null, true);
        Iter toReturn = new Iter(this);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "iterator", procRID, "new_Iter(this)", toReturn, null, null, true, false, false, true);
        StopWatch.stop(false);
        return toReturn;
    }

    @Override
    public String toString() {
        StopWatch.start(false);
//        String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "toString", "label", true, null, null, true);
        String s = "[";
        Iter iter = iterator();
        while (iter.hasNext()) {
            s = s + iter.next() + ", ";
        }
        s = s + "]";
//        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "toString", procRID, "s", s, null, null, true, false, false, true);
        StopWatch.stop(false);
        return s;
    }

    private void readObject(ObjectInputStream ois)
            throws IOException, ClassNotFoundException {
        StopWatch.start(false);
       // String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "readObject", "label", true, new String[]{"ois"}, new Object[]{ois}, true);
        table = new int[ois.readInt()][];
        int elements = ois.readInt();
        for (; elements > 0; elements--) {
            add(ois.readInt());
        }
       // ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "readObject", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "writeObject", "label", true, new String[]{"oos"}, new Object[]{oos}, true);
        oos.writeInt(table.length);
        oos.writeInt(size());
        Iter iter = iterator();
        while (iter.hasNext()) {
            oos.writeInt(iter.next());
        }
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "writeObject", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    public static class Iter {

        private CompactHashSet set;
        private int row;
        private int col;
        private int prevCol;
        private int prevRow;

        public Iter(CompactHashSet set) {
            StopWatch.start(false);
            //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "Iter", "label", true, new String[]{"set"}, new Object[]{String.valueOf(set.hashCode())}, true);
            this.set = set;
            row = 0;
            col = -1;
            prevRow = row;
            prevCol = col;

            moveToNext();
            //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "Iter", procRID, null, null, null, null, true, false, false, true);
            StopWatch.stop(false);
        }

        private void moveToNext() {
            StopWatch.start(false);
            //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "moveToNext", "label", true, null, null, true);
            col++;
            while (row < set.table.length) {
                if (set.table[row] != null && col < set.table[row].length) {
                    for (; col < set.table[row].length; col++) {
                        if (set.table[row][col] != NULL_ELEMENT) {
                            return;
                        }
                    }
                }
                col = 0;
                row++;
            }
            //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "moveToNext", procRID, null, null, null, null, true, false, false, true);
            StopWatch.stop(false);
        }

        public boolean hasNext() {
            StopWatch.start(false);
            //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "hasNext", "label", true, null, null, true);
            //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "hasNext", procRID, "row_<_set.table.length", row < set.table.length, null, null, true, false, false, true);
            StopWatch.stop(false);
            return row < set.table.length;
        }

        public int next() throws NoSuchElementException {
            StopWatch.start(false);
            //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "next", "label", true, null, null, true);
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            prevRow = row;
            prevCol = col;
            moveToNext();
            //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "next", procRID, "set.table[prevRow][prevCol]", set.table[prevRow][prevCol], null, null, true, false, false, true);
            StopWatch.stop(false);
            return set.table[prevRow][prevCol];
        }

        public void remove() throws IllegalStateException {
            StopWatch.start(false);
            //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "remove", "label", true, null, null, true);
            if (prevCol == -1 || set.table[prevRow][prevCol] == NULL_ELEMENT) {
                throw new IllegalStateException();
            }
            set.table[prevRow][prevCol] = NULL_ELEMENT;
            set.size--;
            //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "remove", procRID, null, null, null, null, true, false, false, true);
            StopWatch.stop(false);
        }
    }
}
