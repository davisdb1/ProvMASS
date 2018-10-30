package edu.uw.bothell.css.dsl.MASS.prov;

import edu.uw.bothell.css.dsl.MASS.prov.FireDrill.FireDrill;
import edu.uw.bothell.css.dsl.MASS.prov.NetworkMotif.NetworkMotif;
import edu.uw.bothell.css.dsl.MASS.prov.QuickStart.TestDriver;
import edu.uw.bothell.css.dsl.MASS.prov.SugarScape.SugarScape;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 * This class provides a single entry point for simulation execution. The
 * inserts a first argument into their normal execution, in which the driver
 * class is specified. The Main driver uses this information to pass invoke the
 * main method of the specified simulation driver. The first argument is removed
 * from the arguments that are passed into the main method of the simulation
 * driver. Outside of the driver execution performance results are output file
 *
 * @author Delmar B. Davis
 */
public class Main {

    public static final String NODE_FILE = "nodes.xml";

    public static void main(String[] args) throws Exception {
        StopWatch.init();
        MASSProv.setProcessStartTime();
        // just change granularity right here if you don't want to do it from the command line
        // do not set print to true if you are gathering performance data
        //MASSProv.setGranularity(Granularity.PROCEDURE, false);
        if (args.length > 0) { // at least 1 arg
            // make a copy of args minus the first element
            String[] newArgs = new String[args.length - 1];
            for (int i = 1, im = args.length; i < im; i++) {
                newArgs[i - 1] = args[i];
            }
            // based on the first argument
            switch (args[0].trim().toLowerCase()) {
                // determine the driver to run
                case "testdriver":
                    TestDriver.main(newArgs);
                    break;
                case "sugarscape":
                    SugarScape.main(newArgs);
                    break;
                case "firedrill":
                    FireDrill.main(newArgs);
                    break;
                case "networkmotif":
                    NetworkMotif.main(newArgs);
                    break;
                default:
                    TestDriver.main(newArgs);
                    break;
            }
        } else { // no args, assume the test driver
            TestDriver.main(args);
        }
        long endNano = System.nanoTime();
        if (MASSProv.endnanos > 0L) {
            endNano = MASSProv.getEndTime();
        }
//        System.out.println("\n----------------------------");
//        System.out.println("Total execution time: " + (endNano - startNano) + " nanoseconds");
//        System.out.println("----------------------------");
//        System.out.println("----------------------------");
//        System.out.println("Total execution time: " + ((endNano - startNano) / 1000000.0) + " milliseconds");
//        System.out.println("----------------------------\n");
        System.err.println(endNano - MASSProv.getStartTime());
    }
}
