package edu.uw.bothell.css.dsl.MASS.prov.NetworkMotif;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 *
 * @author Matt Kipps (on 12/12/14), Drew Andersen (5/16/16), Delmar B. Davis
 * (provenance-enabled on 4/28/17)
 */
public class Graph {

    private List<AdjacencyList> adjacencyLists;
    private Map<String, Integer> nameToIndex;
    private Map<Integer, String> indexToName;

    public Graph(String filename) throws IOException {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "Graph", "label", true, new String[]{"filename"}, new String[]{filename}, true);
        adjacencyLists = new ArrayList<AdjacencyList>();
        nameToIndex = new HashMap<String, Integer>();
        indexToName = new HashMap<Integer, String>();
        parse(filename);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "Graph", procRID, "adjacencyLists.size()", adjacencyLists.size(), null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    // get the number of nodes in the graph
    public int size() {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "size", "label", true, null, null, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "size", procRID, "adjacencyLists.size()", adjacencyLists.size(), null, null, true, false, false, true);
        StopWatch.stop(false);
        return adjacencyLists.size();
    }

    public AdjacencyList getAdjacencyList(Integer index) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "getAdjacencyList", "label", true, new String[]{"index"}, new Object[]{index}, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "getAdjacencyList", procRID, "adjacencyLists.get(index)", adjacencyLists.get(index), null, null, true, false, false, true);
        StopWatch.stop(false);
        return adjacencyLists.get(index);
    }

    public Integer nameToIndex(String name) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "nameToIndex", "label", true, new String[]{"name"}, new Object[]{name}, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "nameToIndex", procRID, "nameToIndex.get(name)", nameToIndex.get(name), null, null, true, false, false, true);
        StopWatch.stop(false);
        return nameToIndex.get(name);
    }

    public String indexToName(Integer index) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "indexToName", "label", true, new String[]{"index"}, new Object[]{index}, true);
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "indexToName", procRID, "indexToName.get(index)", indexToName.get(index), null, null, true, false, false, true);
        StopWatch.stop(false);
        return indexToName.get(index);
    }

    // parses a data file into an adjacency list representing the graph
    private void parse(String filename) throws IOException {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "parse", "label", true, new String[]{"filename"}, new Object[]{filename}, true);
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<String> lines = new ArrayList<String>();
        String currentLine = reader.readLine();
        while (currentLine != null) {
            lines.add(currentLine);
            currentLine = reader.readLine();
        }
        reader.close();

        // avoid data collection bias by randomly parsing lines of data
        Collections.shuffle(lines);

        String delimiters = "\\s+"; // one or more whitespace characters
        for (String line : lines) {
            String[] edge = line.split(delimiters);
            int fromIndex = getOrCreateIndex(edge[0]);
            int toIndex = getOrCreateIndex(edge[1]);

            // don't add self edges
            if (fromIndex != toIndex) {
                adjacencyLists.get(fromIndex).add(toIndex);
                adjacencyLists.get(toIndex).add(fromIndex);
            }
        }
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "parse", procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    // get index of a node given the node's name
    // create an entry if it does not exist
    private Integer getOrCreateIndex(String nodeName) {
        StopWatch.start(false);
        //String procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, "getOrCreateIndex", "label", true, new String[]{"nodeName"}, new Object[]{nodeName}, true);
        if (!nameToIndex.containsKey(nodeName)) {
            nameToIndex.put(nodeName, adjacencyLists.size());
            indexToName.put(adjacencyLists.size(), nodeName);
            adjacencyLists.add(new AdjacencyList());
        }
        //ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, "getOrCreateIndex", procRID, "nameToIndex.get(nodeName)", nameToIndex.get(nodeName), null, null, true, false, false, true);
        StopWatch.stop(false);
        return nameToIndex.get(nodeName);
    }
}
