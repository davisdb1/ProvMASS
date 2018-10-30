package edu.uw.bothell.css.dsl.MASS.prov.NetworkMotif;

import edu.uw.bothell.css.dsl.MASS.prov.core.MASS;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 * Driver class for the Agents-based implementation of ESU Network Motif
 * algorithm.
 *
 * @author Matt Kipps (on 12/12/14), Drew Andersen (5/16/16), Delmar B. Davis
 * (provenance-enabled on 4/28/17)
 */
public class NetworkMotif {

    public static void main(String args[]) {
        ProvUtils.stageEvaluation(args);

        boolean showResults = ProvUtils.findArg("--show-results", args);

        // start MASS
        MASS.setNodeFilePath(edu.uw.bothell.css.dsl.MASS.prov.Main.NODE_FILE);
        int nThr = Runtime.getRuntime().availableProcessors();
        System.err.println("Setting thread-count to: " + nThr);
        MASS.setNumThreads(nThr);
        MASS.init();

        // run the program
        String graphFile;
        graphFile = ProvUtils.getArgRemainder("graphfile=", args);
        if (graphFile == null) {
            graphFile = "data/test01";
        }
        String motifSizeText;
        int motifSize;
        motifSizeText = ProvUtils.getArgRemainder("motifsize=", args);
        if (motifSizeText != null) {
            try {
                motifSize = Integer.valueOf(motifSizeText);
            } catch (NumberFormatException e) {
                motifSize = 100;
            }
        } else {
            motifSize = 100;
        }
        Main app = new Main(graphFile, motifSize, showResults);
        app.run();

        // finish MASS
        MASS.finish();
    }
}
