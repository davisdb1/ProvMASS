package edu.uw.bothell.css.dsl.MASS.prov.FireDrill;

import edu.uw.bothell.css.dsl.MASS.prov.core.Agent;
import edu.uw.bothell.css.dsl.MASS.prov.core.Place;
import edu.uw.bothell.css.dsl.MASS.prov.core.logging.Log4J2Logger;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.util.Vector;

/**
 * Represents a person participating in a FireDrill
 *
 * @author Delmar B. Davis (original C++ version by Khadouj Fikry)
 */
public class Person extends Agent {

    private static final boolean printOut = false;
    // define functionId's that will 'point' to the functions they represent.
    public static final int agentInit_ = 0;
    public static final int somethingFun_ = 1;
    public static final int createChild_ = 2;
    public static final int killMe_ = 3;
    public static final int callalltest_ = 4;
    public static final int addData_ = 5;
    public static final int calculateMoveset_ = 6;
    public static final int delegateMove_ = 7;
    public static final int moveDelegated_ = 8;
    public static final int newTurn_ = 9;
    public static final int ravelledxy_ = 10;
    public static final int printCurrentPositionWithID_ = 11;

    // define the cardinals, which represent the Places 
    // adjacent to a particular getPlace() (represented by [0, 0].
    //        [0, 1]                 [ north]
    // [-1, 0][0, 0][1, 0]  == [west][origin][east]
    //        [0,-1]                 [ south]
    // Each X is represent by an array containing its coordinates.
    // Note that you can have an arbritrary number of destinations.  For example,
    // northwest would be [-1,1].
    private static final int[][] cardinals = {{0, 0}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    private final Log4J2Logger logger = Log4J2Logger.getInstance();
    private Vector<Integer> moveset;    //Moveset of directions to randomly walk
    private int movesLeft;              //Moves remaining in the turn
    private int iterationToAct;         //Which iteration in the turn to act    #partion-space
    //Toggles output for user program
    private final boolean printOutput = false;
    private final boolean printOutputK = false;
    private final boolean printToDisplay = false;

    /**
     * Initialize a Person object
     */
    public Person(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Person"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        if (printOut == true) {
            logger.debug("BORN!!");
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Person"), procRID, null, null, null, null, true, false, false, true);
        StopWatch.stop(false);
    }

    /**
     * the callMethod uses the function ID to determine which method to execute.
     * It is assumed the arguments passed in contain everything those methods
     * need to run.
     */
    public Object callMethod(int functionId, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callMethod"), new StringBuffer("label"), true, new String[]{"functionId", "argument"}, new Object[]{functionId, argument}, true);
        Object returnVal = null;
        switch (functionId) {
            case agentInit_:
                returnVal = agentInit(argument);
                break;
            case somethingFun_:
                returnVal = somethingFun(argument);
                break;
            case printCurrentPositionWithID_:
                returnVal = printCurrentWithID(argument);
                break;
            case createChild_:
                returnVal = createChild(argument);
                break;
            case killMe_:
                returnVal = killMe(argument);
                break;
            case callalltest_:
                returnVal = callalltest(argument);
                break;
            case addData_:
                returnVal = addData(argument);
                break;
            case calculateMoveset_:
                returnVal = calculateMoveset(argument);
                break;
            case delegateMove_:
                returnVal = delegateMove(argument);
                break;
            case moveDelegated_:
                returnVal = moveDelegated(argument);
                break;
            case newTurn_:
                returnVal = newTurn();
                break;
            case ravelledxy_:
                returnVal = ravelledxy(argument);
                break;
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callMethod"), procRID, new StringBuffer("returnVal"), returnVal, null, null, true, false, false, true);
        StopWatch.stop(false);
        return returnVal;
    }

    // Initialize a Person with the given argument.
    Object agentInit(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("agentInit"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        StringBuilder convert = new StringBuilder();
        if (printOutput == true) {
            convert.append("agentInit[").append(getAgentId()).append("] called, argument = ").append(argument);
            logger.debug(convert.toString());
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("agentInit"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

//Prints out a message.
    Object somethingFun(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("somethingFun"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        StringBuilder convert = new StringBuilder();
        if (printOutput == true) {
            convert.append("somethingFun[").append(getAgentId()).append("] called, argument = ").append(argument);
            logger.debug(convert.toString());
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("somethingFun"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

//Given Agent ID, it prints agent coordinates
    Object printCurrentWithID(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("printCurrentWithID"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        int turnPtr = (int) argument;
        StringBuilder convert = new StringBuilder();
        convert.append("Turn ").append(turnPtr).append(" agent ").
                append(getAgentId()).append(" current Location = [").
                append(getPlace().getIndex()[0]).append("][").
                append(getPlace().getIndex()[1]).append("]").append("n");
        logger.debug(convert.toString());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("printCurrentWithID"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    //Creates a child agent from a parent agent.
    Object createChild(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("createChild"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        int nChildren = 0;
        Vector<Object> arguments = new Vector<>();
        arguments.add(argument);
        arguments.add(argument);
        if (getAgentId() % 2 == 0) { // only spawn a child if my agent id is even
            nChildren = 2;
            spawn(nChildren, arguments.toArray());
        }
        StringBuilder convert = new StringBuilder();
        if (printOutput == true) {
            convert.append("createChild[").append(getAgentId()).
                    append("] called to spawn ").append(nChildren);
            logger.debug(convert.toString());
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("createChild"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

//Remove the agents who exited the building.
    Object killMe(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("killMe"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        kill();
        if (printOutputK) {
            logger.debug("inside Person::killMe");
        }

        StringBuilder convert = new StringBuilder();
        if (printOutput == true) {
            convert.append("killMe[").append(getAgentId()).append("] called");
            logger.debug(convert.toString());
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("killMe"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

//Call all agents and have them report their arguments.
    Object callalltest(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callalltest"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        StringBuilder convert = new StringBuilder();
        if (printOutput == true) {
            convert.append("callalltest: agent(").append(getAgentId());
            logger.debug(convert.toString());
        }
        double ret_val = (int) argument * 10.0;

        if (printOutput == true) {
            convert.append("callalltest: agent(").append(getAgentId()).
                    append("): argument = ").append((int) argument).
                    append(" *(double *)ret_val = ").append(ret_val);
            logger.debug(convert.toString());
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callalltest"), procRID, new StringBuffer("ret_val"), ret_val, null, null, true, false, false, true);
        StopWatch.stop(false);
        return ret_val;
    }

//Add data to an agent to carry.
    Object addData(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("addData"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        if (printOutputK) {
            logger.debug("inside Person::addData");
        }
        StringBuilder convert = new StringBuilder();
        if (printOutput == true) {
            convert.append("my agent id = ").append(getAgentId());
        }
        String migratableData = convert.toString();

        if (printOutput == true) {
            convert.append(" dataSize = ").append(24);
            logger.debug(convert.toString());
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("addData"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

//Sets parameters for the Map
    int map(int initPopulation, Vector<Integer> size, Vector<Integer> index, Place curPlace) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("map"), new StringBuffer("label"), true, new String[]{"initPopulation", "size", "index", "curPlace"}, new Object[]{initPopulation, size, index, curPlace}, true);
        StringBuilder convert = new StringBuilder();

        if (size.size() != index.size()) {
            return 0;
        }
        for (int i = 0; i < size.size(); i++) {
            if (getIndex()[0] == size.get(0) / 2) {
                return 0;
            }
        }
        if (printOutput == true) {
            convert.append("Person::Map - creating agent at ").append(getIndex()[0]);
            for (int i = 1; i < size.size(); i++) {
                convert.append("),").append(getIndex()[i]);
            }
            convert.append("n");
            logger.debug(convert.toString());
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("map"), procRID, new StringBuffer("1"), 1, null, null, true, false, false, true);
        StopWatch.stop(false);
        return 1;
    }

//Provided the agent's current location and which space to move in.
    Object calculateMoveset(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("calculateMoveset"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        moveset.clear();
        moveset.add(1);
        moveset.add(2);
        moveset.add(3);
        moveset.add(4);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("calculateMoveset"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

//Reset migration data for the new turn, including calculation for which iteration to act
    Object newTurn() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("newTurn"), new StringBuffer("label"), true, null, null, true);
        movesLeft = 1;
        int x = getIndex()[0];
        int y = getIndex()[1];
        //Select based on location-- partition by x % 3 + y % 3 * 3 = {0 to 8}
        iterationToAct = x % 3 + 3 * (y % 3);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("newTurn"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

//Provide my Place myID so that it can reserve my next move
    Object delegateMove(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("delegateMove"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        StringBuilder convert = new StringBuilder();
        if (printOutputK) {
            logger.debug("inside Person::delegateMove");
        }
        convert.append("moveDelegated: Agent-").append(getAgentId()).
                append(" at [").append(getPlace().getIndex()[0]).append("), new StringBuffer(").
                append(getPlace().getIndex()[1]).append("] could not move!");
        //Check if iteration to act
        int partitionCounter = (int) argument;
        if (iterationToAct != partitionCounter) {
            return null;
        }
        int id = getAgentId();
        getPlace().callMethod(Building.setMyAgent_, id);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("delegateMove"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

//Move to the randomly reserved neighboring square
    Object moveDelegated(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("moveDelegated"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        StringBuilder convert = new StringBuilder();
        if (printOutputK) {
            logger.debug("inside Person::moveDelegated");
        }
        //Check if iteration to act
        int partitionCounter = (int) argument;
        if (iterationToAct != partitionCounter) {
            return null;
        }

        if (printOutput) {
            logger.debug("inside moveDelegated");
        }
        Vector<Integer> dest = new Vector<>();
        int x = 0, y = 0;
        int sizeX = 0, sizeY = 0; //recapture the size
        int[] nextMove = (int[]) (getPlace().callMethod(Building.getMigrationReservation_, null));
        if (nextMove != null) {
            sizeX = getPlace().getSize()[0];	//intialialize sizeX
            sizeY = getPlace().getSize()[1];	//intialialize sizeY 

            x = getPlace().getIndex()[0] + nextMove[0];
            y = getPlace().getIndex()[1] + nextMove[1];
            dest.add(x);
            dest.add(y);
            migrate(new int[]{x, y});
        }
        if (printOutput) {
            logger.debug(convert.toString());
        }
        //If at the door, exit, and remove the agent
        if (x == sizeX / 2 && y == sizeY - 1) {
            if (printToDisplay) {
                convert.append("Agent-").append(getAgentId()).append(" at [").
                        append(x).append("), new StringBuffer(").append(y).
                        append("] Made it to the EXIT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                logger.debug(convert.toString());
            }
            killMe(null);
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("moveDelegated"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    //Return Agent location
    Object ravelledxy(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("ravelledxy"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        int ret_val = getPlace().getIndex()[0] + 100 * getPlace().getIndex()[1];
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("ravelledxy"), procRID, new StringBuffer("ret_val"), ret_val, null, null, true, false, false, true);
        StopWatch.stop(false);
        return ret_val;
    }
}
