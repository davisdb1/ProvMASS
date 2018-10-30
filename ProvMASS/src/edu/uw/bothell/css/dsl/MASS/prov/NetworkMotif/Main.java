package edu.uw.bothell.css.dsl.MASS.prov.NetworkMotif;

import edu.uw.bothell.css.dsl.MASS.prov.core.Agents;
import edu.uw.bothell.css.dsl.MASS.prov.core.Places;
import java.io.IOException;
import java.util.*;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 * Simulation application for the agents-based implementation of ESU Network
 * Motif algorithm.
 *
 * @author Matt Kipps (on 12/12/14), Drew Anderson (5/16/16), Delmar B. Davis
 * (provenance-enabled on 4/28/17)
 */
public class Main {

    // app execution code goes here
    public void run() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("run"), new StringBuffer("label"), true, null, null, true);
        System.out.println("Parsing input data file '" + filename + "'...");
        Graph graph;
        long start = System.currentTimeMillis();
        try {
            graph = new Graph(filename);
        } catch (IOException e) {
            System.out.println("Unable to parse data file");
            return;
        }

        System.out.println(
                (System.currentTimeMillis() - start) + " milliseconds to "
                + "generate a network of size " + graph.size());

        int networkSize = graph.size();

        // create network nodes
        System.out.println("Setting up MASS places and agents...");
        start = System.currentTimeMillis();
        Places placesGraph = new Places(
                1,
                GraphNode.class.getCanonicalName(),
                (Object) (new GraphNode.Constructor(networkSize)),
                networkSize);

        // initialize node edges
        Object[] params = new Object[networkSize];
        for (int node = 0; node < params.length; node++) {
            params[node] = (Object) (graph.getAdjacencyList(node));
        }
        placesGraph.callAll(GraphNode.initializeEdges_, params);

        // initialize agents, spawning 1 agent per node in the network
        // this approach assumes agents will be evenly spread across the nodes
        // so that each node will have one agent to start
        Agents crawlers = new Agents(
                2,
                GraphCrawler.class.getCanonicalName(),
                (Object) (new GraphCrawler.Constructor(motifSize)),
                placesGraph,
                networkSize);

        System.out.println(
                (System.currentTimeMillis() - start) + " milliseconds to "
                + "set up MASS places and agents.");

        // run until agents terminate themselves
        System.out.println("Executing ESU for motif size " + motifSize + "...");
        start = System.currentTimeMillis();
        int remainingSubgraphs = crawlers.nAgents();
        while (remainingSubgraphs > 0) {
            System.out.println("enumerating ESU: " + remainingSubgraphs + " subgraphs in progress");
            crawlers.callAll(GraphCrawler.update_);
            crawlers.manageAll();
            remainingSubgraphs = crawlers.nAgents();
        }

        System.out.println(
                (System.currentTimeMillis() - start) + " milliseconds to "
                + "execute ESU.");

        // collect the subgaph data left at the places
        // (MASS requires parameter array in order to get return values)
        start = System.currentTimeMillis();
        Object[] dummyParams = new Object[networkSize];
        for (int i = 0; i < dummyParams.length; i++) {
            dummyParams[i] = (Object) (0); // some serializable object
        }

        System.out.println("Collecting subgraph results...");
        Object[] results = (Object[]) placesGraph.callAll(
                GraphNode.collectSubgraphs_,
                dummyParams);

        System.out.println(
                (System.currentTimeMillis() - start) + " milliseconds to "
                + "collect subgraph results at master");

        // get the canonical label counts
        // this portion is executed sequentially on the master node.
        System.out.println("Determining subgraph (label) frequency...");
        start = System.currentTimeMillis();

        // collect the labels into one master subgraph collection
        Map<String, Integer> subgraphs = new HashMap<String, Integer>();
        for (int i = 0; i < results.length; i++) {
            // convert generic Object types
            @SuppressWarnings("unchecked")
            Map<String, Integer> result = (Map<String, Integer>) results[i];

            // merge the results into the master subgraph collection
            for (Map.Entry<String, Integer> entry : result.entrySet()) {
                int count = entry.getValue();
                if (subgraphs.containsKey(entry.getKey())) {
                    count += subgraphs.get(entry.getKey());
                }
                subgraphs.put(entry.getKey(), count);
            }
        }

        // get canonical label counts from subgraphs
        Labeler labeler = new Labeler();
        Map<String, Integer> labels = labeler.getCanonicalLabels(subgraphs);

        System.out.println(
                (System.currentTimeMillis() - start) + " milliseconds to "
                + "determine subgraph (label) frequency");

        if (showResults) {
            System.out.println("LabeltFrequency");
            for (Map.Entry<String, Integer> entry : labels.entrySet()) {
                System.out.println(entry.getKey() + "t" + entry.getValue());
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("run"), procRID, null, null, null, null, true, false, false, true);
        ProvUtils.releaseThreadStore();
        StopWatch.stop(false);
    }

    private int motifSize;
    private String filename;
    private boolean showResults;

    public Main(String filename, int motifSize, boolean showResults) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Main"), new StringBuffer("label"), true, null, null, true);
        this.filename = filename;
        this.motifSize = motifSize;
        this.showResults = showResults;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Main"), procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }
}
