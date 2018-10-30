package edu.uw.bothell.css.dsl.MASS.prov.FireDrill;

import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.Main;
import edu.uw.bothell.css.dsl.MASS.prov.core.Agents;
import edu.uw.bothell.css.dsl.MASS.prov.core.MASS;
import edu.uw.bothell.css.dsl.MASS.prov.core.Places;
import edu.uw.bothell.css.dsl.MASS.prov.core.logging.Log4J2Logger;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import java.util.Vector;

/**
 * This program is build on Professor Fukuda's example of Nomad and Land sample
 * files. Program Summary: This program simulates Fire drill exit strategy on a
 * 1 story building. Once the program runs, and based on which executions you
 * select in run.sh, the program runs, display the number of people inside the
 * building, and it shows the elapsed time it takes for everybody to exit.
 *
 * @author Delmar B. Davis (original C++ version by Khadouj Fikry)
 */
public class FireDrill {

    private static final int ITER_PER_TURN = 3;
    private static final boolean PRINT_OUTPUT = true;
    private static final Log4J2Logger LOGGER = Log4J2Logger.getInstance();

    public static void main(String[] args) {
        ProvUtils.stageEvaluation(args);
        final int numTurns = 4;
        final int sizeX = 35;
        final int sizeY = 35;
        final double rmSpawn = 0.2;
        final int myPopulation = (int) ((double) sizeX * (double) sizeY * rmSpawn * rmSpawn); //Population{RMsize, RMSpawn}

        // start MASS (machine informations)
        MASS.setNodeFilePath(Main.NODE_FILE);
        int nThr = Runtime.getRuntime().availableProcessors(); // number of threads
        System.err.println("Setting thread-count to: " + nThr);
        MASS.setNumThreads(nThr);
        MASS.init();

        // prepare a message for the places (this is argument below)
        String msg = "hello"; // should not be char msg[]

        /*  THIS SECTION OF CODE DEALS ONLY WITH PLACES  */
        // Create the places.
        // Arguments are, in order: handle, className, boundary_width, argument, argument_size, dim, ...
        Places building = new Places(1, Building.class.getCanonicalName(), 1, msg, 2, sizeX, sizeY);
        // define the destinations, which represent the Places 
        // adjacent to a particular place (represented by [0, 0].
        //        [0, 1]                 [ north]
        // [-1, 0][0, 0][1, 0]  == [west][origin][east]
        //        [0,-1]                 [ south]
        // Each X is represent by an array containing its coordinates.
        // Note that you can have an arbritrary number of destinations.  For example,
        // northwest would be [-1,1].
        Vector<int[]> destinations = new Vector<>();
        int[] north = {0, 1};
        destinations.add(north);
        int[] east = {1, 0};
        destinations.add(east);
        int[] south = {0, -1};
        destinations.add(south);
        int[] west = {-1, 0};
        destinations.add(west);

        /*  THIS SECTION OF CODE DEALS ONLY WITH AGENTS  */
        // Create the agents.
        // Arguments are, in order:handle, className, *argument, argument_size, *places,  initPopulation
        Agents persons = new Agents(2, Person.class.getCanonicalName(), msg, building, myPopulation);
        building.callAll(Building.init_);   //initialize Buildings

        int agentCount = 0;
        int InitialAgentCount = 0;
        int actualTurn = 0;
        System.err.println("------------------------------------------------------");
        //Print initial Agent count
        StringBuilder convert1 = new StringBuilder();
        InitialAgentCount = persons.nAgents();
        convert1.append("Firedrill started with ").append(InitialAgentCount).
                append(" participants spread through a building of sizeX = ").
                append(sizeX).append(" and sizeY= ").append(sizeY).append("\n");
        LOGGER.debug(convert1.toString());

        for (int turn = 0; turn < numTurns; turn++) {
            persons.callAll(Person.newTurn_);	//Reset movecount and iteration to act

            if (agentCount != persons.nAgents()) {
                agentCount = persons.nAgents();
                actualTurn = turn;
            }
            //Breaks from the iteration if no person is left
            if (persons.nAgents() <= 0) {
                break;
            }

            //Iterations
            for (int i = 0, iter = 0; i < ITER_PER_TURN; i++, iter++) {
                // reset the information on each place for the next location ordered move.
                building.callAll(Building.resetForNextTurn_);
                //Delegate
                persons.callAll(Person.delegateMove_, iter);
                //Display the current population
                building.callAll(Building.displayPopAsOut_);
                //Exchange shadow space information
                building.exchangeBoundary();
                building.callAll(Building.delegateCalculation_);
                //Read delegate and move
                persons.callAll(Person.moveDelegated_, iter);
                //Actual move the agent. Here moves agents calculated each iteration
                persons.manageAll();
            }

            if (PRINT_OUTPUT && (numTurns >= 10) && (turn % 100 == 0)) {
                int count = persons.nAgents();
                if (count > 1) {
                    System.err.println("On turn " + turn + ", " + count + " participants are inside the building\n");
                } else {
                    System.err.println("On turn " + turn + ", " + count + " participant is inside the building\n");
                }
            }
        }

        int maxTurns = numTurns;
        persons.callAll(Person.printCurrentPositionWithID_, maxTurns);
        MASS.finish();
    }
}
