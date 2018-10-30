package edu.uw.bothell.css.dsl.MASS.prov.FireDrill;

import edu.uw.bothell.css.dsl.MASS.prov.core.Place;
import edu.uw.bothell.css.dsl.MASS.prov.core.logging.Log4J2Logger;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.util.Random;
import java.util.Vector;

/**
 * Represents a building sector in a FireDrill
 *
 * @author Delmar B. Davis (original C++ version by Khadouj Fikry)
 */
public class Building extends Place {

    final boolean printOutput = false;
    final boolean printOutputK = false;
    final boolean debugK = false;
    final Log4J2Logger logger = Log4J2Logger.getInstance();
    static final int NUM_NEIGHBORS = 4;//A Building has 4 neighbors

    // define functionId's that will 'point' to the functions they represent.
    public static final int init_ = 0;
    public static final int callalltest_ = 1;
    public static final int exchangetest_ = 2;
    public static final int checkInMessage_ = 3;
    public static final int printOutMessage_ = 4;
    public static final int printShadow_ = 5;
    public static final int popCount_ = 6;
    public static final int recordAsNeighborPop_ = 7;
    public static final int resetForNextTurn_ = 8;
    public static final int displayPopAsOut_ = 9;
    public static final int recordNeighborPopByOut_ = 10;
    public static final int setMyAgent_ = 11;
    public static final int getMigrationReservation_ = 12;
    public static final int makeReservation_ = 13;
    public static final int showReservation_ = 14;
    public static final int checkReservation_ = 15;
    public static final int reservationExchange_ = 16;
    public static final int showReservation2_ = 17;
    public static final int delegateCalculation_ = 18;
    Vector<int[]> cardinals = new Vector<>();                          //Vector form of cardinals
    public static final int[][] neighbor = new int[4][2];    //Array form of cardinals
    //Migration Data//
    int myAgent;                        //Stores the id of my agent temporarily
    int[] nextMove = new int[2]; //Stores the next move for my agent to migrate
    boolean reserved;  //Status of whether next move is valid reservation or not
    int[] neighborPop = new int[4];//An array to store int population of neighbors
    private String arg;

    public Building(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Building"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        arg = (String) argument;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("Building"), procRID, null, null, null, null, true, false, false, true);
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
            case init_:
                returnVal = init(argument);
                break;
            case callalltest_:
                returnVal = callalltest(argument);
                break;
            case exchangetest_:
                returnVal = exchangetest(argument);
                break;
            case checkInMessage_:
                returnVal = checkInMessage(argument);
                break;
            case printOutMessage_:
                returnVal = printOutMessage(argument);
                break;
            case printShadow_:
                returnVal = printShadow(argument);
                break;
            case popCount_:
                returnVal = popCount(argument);
                break;
            case recordAsNeighborPop_:
                returnVal = recordAsNeighborPop();
                break;
            case resetForNextTurn_:
                returnVal = resetForNextTurn();
                break;
            case displayPopAsOut_:
                returnVal = displayPopAsOut();
                break;
            case recordNeighborPopByOut_:
                returnVal = recordNeighborPopByOut();
                break;
            case setMyAgent_:
                returnVal = setMyAgent(argument);
                break;
            case getMigrationReservation_:
                returnVal = getMigrationReservation();
                break;
            case makeReservation_:
                returnVal = makeReservation();
                break;
            case showReservation_:
                returnVal = showReservation();
                break;
            case checkReservation_:
                returnVal = checkReservation();
                break;
            case reservationExchange_:
                returnVal = reservationExchange();
                break;
            case showReservation2_:
                returnVal = showReservation2();
                break;
            case delegateCalculation_:
                returnVal = delegateCalculation();
                break;
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callMethod"), procRID, new StringBuffer("returnVal"), returnVal, null, null, true, false, false, true);
        StopWatch.stop(false);
        return returnVal;
    }

    ;
    /**
     * Initializes a Building object.
     */
    Object init(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("init"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        if (printOutputK) {
            logger.debug("inside Building::init");
        }
        //Define cardinals
        int[] north = {0, 1};
        cardinals.add(north);
        int[] east = {1, 0};
        cardinals.add(east);
        int[] south = {0, -1};
        cardinals.add(south);
        int[] west = {-1, 0};
        cardinals.add(west);
        //Initialize by clearing the migration status
        resetForNextTurn();
        if (printOutput == true) {
            StringBuilder convert = new StringBuilder();
            convert.append("Init:[").append(getIndex()[0]).append("][").
                    append(getIndex()[1]).append("]").append(getSize()[0]).
                    append(getSize()[1]).append(
                    (Object[]) getOutMessage() != null ? 0
                            : ((Object[]) getOutMessage()).length);
            System.err.println(convert);
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("init"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * callalltest causes all Building's to report their location and contents.
     */
    Object callalltest(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callalltest"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        if (printOutputK) {
            logger.debug("inside Building::callalltest");
        }

        double ret_val = argument == null ? 0 : (int) argument * 10.0;

        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("callalltest"), procRID, new StringBuffer("ret_val"), ret_val, null, null, true, false, false, true);
        StopWatch.stop(false);
        return ret_val;
    }

    /**
     * exchangetest causes each place to exchange information with it's
     * neighbors.
     */
    Object exchangetest(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("exchangetest"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        if (printOutputK) {
            logger.debug("inside Building::exchangetest");
        }

        int retVal = (int) argument * 10000 + getIndex()[0] * 100 + getIndex()[1];

        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("exchangetest"), procRID, new StringBuffer("retVal"), retVal, null, null, true, false, false, true);
        StopWatch.stop(false);
        return retVal;
    }

    /**
     * Logs any inMessages associated with the Place.
     */
    Object checkInMessage(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("checkInMessage"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        if (printOutputK) {
            logger.debug("inside Building::checkInMessage");
        }
        if (printOutput == true) {
            StringBuilder convert = new StringBuilder();
            convert.append("checkInMessage[").
                    append(getIndex()[0]).append("][").append(getIndex()[1]).
                    append("] out of [").append(getSize()[0]).append("][").
                    append(getSize()[1]).append("] inMessages.size = ").
                    append(getInMessages().length).append(" received ");
            for (int i = 0, im = getInMessages().length; i < im; i++) {
                convert.append(" [").append(i).append("] = ").
                        append(getInMessages()[i]);
            }
            logger.debug(convert.toString());
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("checkInMessage"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     */
    Object printOutMessage(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("printOutMessage"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        StringBuilder convert = new StringBuilder();
        convert.append("printOutMessage Building[").append(getIndex()[0]).
                append("][").append(getIndex()[1]).append("]'s outMessage = ").
                append(getOutMessage());
        logger.debug(convert.toString());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("printOutMessage"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * Prints out the neighbors defined below if they exist.
     */
    Object printShadow(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("printShadow"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        if (printOutputK) {
            logger.debug("inside Building::printShadow");
        }
        int[] shadow = new int[4];
        int[] north = {0, 1};
        int[] east = {1, 0};
        int[] south = {0, -1};
        int[] west = {-1, 0};
        int ptr = getOutMessage(1, north) == null ? -1 : (int) getOutMessage(1, north);
        shadow[0] = ptr;
        ptr = getOutMessage(1, east) == null ? -1 : (int) getOutMessage(1, east);
        shadow[1] = ptr;
        ptr = getOutMessage(1, south) == null ? -1 : (int) getOutMessage(1, south);
        shadow[2] = ptr;
        ptr = getOutMessage(1, west) == null ? -1 : (int) getOutMessage(1, west);
        shadow[3] = ptr;

        StringBuilder convert = new StringBuilder();
        convert.append("printShadow:  Building[").append(getIndex()[0]).
                append("][").append(getIndex()[1]).append("]'s north = ").
                append(shadow[0]).append("), east = ").append(shadow[1]).
                append("), south = ").append(shadow[2]).append("), west = ").
                append(shadow[3]);
        logger.debug(convert.toString());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("printShadow"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * (int) Returns the population of this building
     *
     * @return:	AN INT, containing population of local agents of this building
     */
    Object popCount(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("popCount"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        if (printOutputK) {
            logger.debug("inside Building::popCount");
        }
        int retVal = getAgents().size();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("popCount"), procRID, new StringBuffer("retVal"), retVal, null, null, true, false, false, true);
        StopWatch.stop(false);
        return retVal;
    }

    /**
     * (void) Record inmessage data into neighborPop
     *
     * @pre:	Inmessage must contain integers
     */
    Object recordAsNeighborPop() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("recordAsNeighborPop"), new StringBuffer("label"), true, null, null, true);
        if (printOutputK) {
            logger.debug("inside Building::recordAsNeighborPop");
        }
        int max = getInMessages().length > NUM_NEIGHBORS
                ? NUM_NEIGHBORS : getInMessages().length;
        for (int i = 0; i < max; i++) {
            neighborPop[i] = (int) getInMessages()[i];
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("recordAsNeighborPop"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * (void) Resets migration data for the next turn
     */
    Object resetForNextTurn() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("resetForNextTurn"), new StringBuffer("label"), true, null, null, true);
        if (printOutputK) {
            logger.debug("inside Building::resetForNextTurn");
        }
        for (int i = 0; i < NUM_NEIGHBORS; i++) {
            neighborPop[i] = -1;
        }
        myAgent = -1;
        nextMove[0] = 0;
        nextMove[1] = 0;
        reserved = false;
        setOutMessage(null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("resetForNextTurn"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * (void) Set the outmessage as population of this building
     */
    Object displayPopAsOut() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("displayPopAsOut"), new StringBuffer("label"), true, null, null, true);
        if (printOutputK) {
            logger.debug("inside Building::displayPopAsOut");
        }
        setOutMessage(getAgents().size());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("displayPopAsOut"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * (void)	#Visual logging confirmed 160710 Record the outmessage of
     * neighbors as population pre: neighborPop is initialized and contains 4
     * -1s
     */
    Object recordNeighborPopByOut() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("recordNeighborPopByOut"), new StringBuffer("label"), true, null, null, true);
        if (printOutputK) {
            logger.debug("inside Building::recordNeighborPopByOut");
        }
        //Only need to record neighborpopulation if the building has an agent who has delegated the Building to do so
        if (getAgents().isEmpty()) {
            return null;
        }
        int[] North = {0, 1};
        int[] East = {1, 0};
        int[] South = {0, -1};
        int[] West = {-1, 0};
        /**/
        int ptr = getOutMessage(1, North) == null ? 0 : (int) getOutMessage(1, North);
        neighborPop[0] = ptr;
        ptr = getOutMessage(1, East) == null ? 0 : (int) getOutMessage(1, East);
        neighborPop[1] = ptr;
        ptr = getOutMessage(1, South) == null ? 0 : (int) getOutMessage(1, South);
        neighborPop[2] = ptr;
        ptr = getOutMessage(1, West) == null ? 0 : (int) getOutMessage(1, West);
        neighborPop[3] = ptr;
        //Debug logging: Expects neighbor populations
        StringBuilder convert = new StringBuilder();
        convert.append("recordNeighborPopByOut:  Building[").
                append(getIndex()[0]).append("][").append(getIndex()[1]).
                append("]'s north = ").append(neighborPop[0]).
                append("), east = ").append(neighborPop[1]).
                append("), south = ").append(neighborPop[2]).
                append("), west = ").append(neighborPop[3]);
        logger.debug(convert.toString());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("recordNeighborPopByOut"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * (void)	#Visual logging confirmed 160710 Record my active agent by ID
     *
     * @param:	argument is an int, the id of my agent
     */
    Object setMyAgent(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setMyAgent"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument}, true);
        if (printOutputK) {
            logger.debug("inside Building::setMyAgent");
        }
        //Can only set agentID if agent resides on the Building
        if (getAgents().isEmpty()) {
            return null;
        }
        myAgent = (int) argument;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setMyAgent"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * (int *)	#UNTESTED Return the relative coordinate of successfully reserved
     * place if any. Returns null if no reservations, and {0, 0} if reservation
     * failed. This is so the Nomad can retrieve its next move from the Building
     * it is currently on
     */
    Object getMigrationReservation() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getMigrationReservation"), new StringBuffer("label"), true, null, null, true);
        if (printOutputK) {
            logger.debug("inside Building::getMigrationReservation");
        }
        int[] retVal;
        if (nextMove[0] == 0 && nextMove[1] == 0) {
            retVal = null;
        } else {
            retVal = new int[2];
            if (!reserved) {
                retVal[0] = 0;
                retVal[1] = 0;
            }
            retVal[0] = nextMove[0];
            retVal[1] = nextMove[1];
        }
        //TO DEBUG LOG
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getMigrationReservation"), procRID, new StringBuffer("retVal"), retVal, null, null, true, false, false, true);
        StopWatch.stop(false);
        return retVal;
    }

    /**
     * (void)	#DOES NOT WORK: putInMessage() fails accross ranks Makes a
     * reservation on behalf of its agent by putInMessage to neighbor the ID of
     * its agent. First, it calculates and stores a neighbor to reserve. Then it
     * attempts to put its agent's ID into the neighbor's inMessage.
     *
     * @pre:	Neighbors have been polled for population status
     * @post:	Creates reservation on a selected neighbor
     */
    Object makeReservation() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("makeReservation"), new StringBuffer("label"), true, null, null, true);
        if (printOutputK) {
            logger.debug("inside Building::makeReservation");
        }
        //Can only make reservation if delegated to do so
        if (getAgents().isEmpty()) {
            return null;
        }
        Vector<Integer> moveset = new Vector<>();
        for (int i = 0; i < (int) cardinals.size(); i++) {
            if (neighborPop[i] == 0) {
                moveset.add(i);
            }
        }
        if (!moveset.isEmpty()) {
            int r = new Random().nextInt() % moveset.size();
            nextMove[0] = neighbor[moveset.get(r)][0];
            nextMove[1] = neighbor[moveset.get(r)][1];
        }
        //If any neighbor selected, reserve
        if (!(nextMove[0] == 0 && nextMove[1] == 0)) {
            int arg = myAgent;
        }
        //Debug logging: Expects to reserve a space on an unoccupied building
        StringBuilder convert = new StringBuilder();
        if (!(nextMove[0] == 0 && nextMove[1] == 0)) {
            convert.append("makeReservation: Building[").append(getIndex()[0]).
                    append("][").append(getIndex()[1]).
                    append("] reserving on [").
                    append((getIndex()[0] + nextMove[0])).append("][").
                    append((getIndex()[1] + nextMove[1])).append("]");
        } else {
            convert.append("makeReservation: Building[").
                    append(getIndex()[0]).append("][").
                    append(getIndex()[1]).
                    append("] could not make reservation");
        }
        logger.debug(convert.toString());
        //TO DEBUG LOG
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("makeReservation"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * (void)	#DOES NOT WORK: putInMessage() fails accross ranks Shows the
     * reservation status as 0 or agentID of the agent that can move to this
     * place for the next turn First, it reads from inMessages to see if any
     * reservations have been made.
     *
     * @post:	The reservation made will be set as outMessage, or 0 if it hasn't
     * been made. Also clears the inMessages[0] used for reservation channel
     */
    Object showReservation() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("showReservation"), new StringBuffer("label"), true, null, null, true);
        if (printOutputK) {
            logger.debug("inside Building::showReservation");
        }/*
	//inMessage_size = sizeof( int );
	int *reservation = null;
	if(!inMessages.empty())
	{
		
		vector<int> reservations;
		//int max = (inMessages.size() > NUM_NEIGHBORS) ? NUM_NEIGHBORS : inMessages.size();
		for(int i = 0; i < (int)inMessages.size(); i++)
		{
			if(inMessages[i] != null)
			{
				reservations.push_back(*(int*)inMessages[i]);
			}
		}
		if(!reservations.empty())
		{
			int r = rand() % reservations.size();
			*reservation = reservations[r];
		}
		
		//reservation = (int *)(inMessages[5]);		//Read reservation from in message if any	 //ERROR: Does not work as documented: cannot allocate cross ranks
		
		//DEBUG
		ostringstream convert;convert << "Read a " << *reservation;MASS_base::log( convert.str( ) );
	}
	
	//Set corresponding reservation status in outMessage;
	if(reservation == null)
	{
		outMessage_size = sizeof( int );
		outMessage = new int( );
		*(int *)outMessage = 0;
	}
	else
	{
		outMessage_size = sizeof( int );
		outMessage = new int( );
		*(int *)outMessage = *reservation;
		//inMessages[0] = null;
		
		
		//clear #doesn't help
		//int *ptr = new int;
		// *ptr = 0;
		//inMessages[0] = (void*)ptr;
	}*/
        StringBuilder DEBUG = new StringBuilder();												//DEBUG OUTPUT
        Vector<Integer> reservations = new Vector<>();
        int cur;
        for (int i = 0, im = getInMessages() != null ? getInMessages().length : 0;
                i < im; i++) {
            cur = getInMessages()[i] == null ? -1 : (int) getInMessages()[i];
            //Add whatever is trying to reserve on me
            if (cur != -1) {
                if (cur != 0) {
                    reservations.add(cur);
                }
                DEBUG.append(cur).append("), new StringBuffer(");
            } else {
                DEBUG.append("null, new StringBuffer(");
            }
        }
        //If empty, outMessage is 0
        if (reservations.isEmpty()) {
            setOutMessage(0);
        } //Else, outMessage is a randomly selected reservation
        else {
            int r = new Random().nextInt() % reservations.size();
            setOutMessage(reservations.get(r));
        }
        //Debug logging: Expects to have read a reservation of whichever agentID wishing to migrate to this Building or 0 if none
        StringBuilder convert = new StringBuilder();
        convert.append("showReservation: Building[").append(getIndex()[0]).
                append("][").append(getIndex()[1]).append("] is reserved (").
                append(getOutMessage()).append("). InMessage was { ").
                append(DEBUG.toString()).append("}");
        logger.debug(convert.toString());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("showReservation"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * (void)	#Probably works 160711, not sure about successful reservation
     * Checks the reservation status of the neighbor for the its reservation
     * status, by reading from the neighbor's outMessage
     *
     * @post:	Sets the reserve status to true if its reservation is successful
     */
    Object checkReservation() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("checkReservation"), new StringBuffer("label"), true, null, null, true);
        if (printOutputK) {
            logger.debug("inside Building::reservationExchange");
        }
        //Can only check reservation if delegated to do so
        if (getAgents().isEmpty()) {
            return null;
        }
        //If any neighbor selected, check reservation status
        if (!(nextMove[0] == 0 && nextMove[1] == 0)) {
            int ptr = getOutMessage(1, nextMove) == null ? -1 : (int) getOutMessage(1, nextMove);
            if (ptr != -1 && ptr == myAgent) {
                reserved = true;
            }
        }
        //Debug logging: Correctly shows whether or not the random move was successfully reserved
        StringBuilder convert = new StringBuilder();
        if (reserved) {
            convert.append("checkReservation: Building[").append(getIndex()[0]).
                    append("][").append(getIndex()[1]).
                    append("] successfully reserved on [").
                    append((getIndex()[0] + nextMove[0])).
                    append("][").append((getIndex()[1] + nextMove[1])).
                    append("]");
        } else if (!(nextMove[0] == 0 && nextMove[1] == 0)) {
            convert.append("checkReservation: Building[").append(getIndex()[0]).
                    append("][").append(getIndex()[1]).
                    append("] could not reserve on [").
                    append((getIndex()[0] + nextMove[0])).append("][").
                    append((getIndex()[1] + nextMove[1])).append("]");
        } else {
            convert.append("checkReservation: Building[").append(getIndex()[0]).
                    append("][").append(getIndex()[1]).
                    append("] could not decide a reservation.");
        }
        logger.debug(convert.toString());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("checkReservation"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * (int*)	#DOESN'T WORK: ExchangeAll() return values will not cross
     * boundaries Exchange all version of making reservation, where next move
     * was previously calculated.
     *
     * @return:Returns int[3] {nextMove[0], nextMove[1], AgentID} if I have
     * reservation to make, else null
     * @pre:	makeReservation must have been previously been called, or else no
     * reservations will be made
     * @post:	Sets the reserve status to true if its reservation is successful
     */
    Object reservationExchange() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("reservationExchange"), new StringBuffer("label"), true, null, null, true);
        if (printOutputK) {
            logger.debug("inside Building::reservationExchange");
        }
        int[] retVal = null;
        //Return only if a next move is to be made
        if (!(nextMove[0] == 0 && nextMove[1] == 0)) {
            retVal = new int[3];
            retVal[0] = getIndex()[0] + nextMove[0];
            retVal[1] = getIndex()[1] + nextMove[1];
            retVal[2] = myAgent;
        }
        //Debug logging: Should be almost identical to setMyAgent and makeReservation() combined
        StringBuilder convert = new StringBuilder();
        if (retVal != null) {
            convert.append("reservationExchange: Building[").
                    append(getIndex()[0]).append("][").append(getIndex()[1]).
                    append("] reservation message {").append(retVal[0]).
                    append("), new StringBuffer(").append(retVal[1]).append("), new StringBuffer(").
                    append(retVal[2]).append("}");
        } else {
            convert.append("reservationExchange: Building[").
                    append(getIndex()[0]).append("][").append(getIndex()[1]).
                    append("] reservation message: No Reservation");
        }
        logger.debug(convert.toString());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("reservationExchange"), procRID, new StringBuffer("retVal"), retVal, null, null, true, false, false, true);
        StopWatch.stop(false);
        return retVal;
    }

    /**
     * (void)	#DOESN'T WORK: ExchangeAll() return values will not cross
     * boundaries Exchange all version of showing reservation Shows the
     * reservation status as 0 or agentID of the agent that can move to this
     * place for the next turn First, it reads from inMessages to see if any
     * reservations have been made, and randomly selects one of them for
     * display, or 0 if none exist.
     *
     * @post:	The reservation made will be set as outMessage, or 0 if it hasn't
     * been made.
     */
    Object showReservation2() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("showReservation2"), new StringBuffer("label"), true, null, null, true);
        if (printOutputK) {
            logger.debug("inside Building::showReservation2");
        }
        //Read what's for reservation
        Vector<Integer> reservations = new Vector<>();
        int[] cur;
        for (int i = 0, im = getInMessages() == null
                ? 0 : (int) getInMessages().length; i < im; i++) {
            cur = (int[]) getInMessages()[i];
            //Add whatever is trying to reserve on me
            if (cur != null && cur[0] == getIndex()[0] && cur[1] == getIndex()[1]) {
                reservations.add(cur[2]);
            }
        }
        //If empty, outMessage is 0
        if (reservations.isEmpty()) {
            setOutMessage(0);
        } //Else, outMessage is a randomly selected reservation
        else {
            int r = new Random().nextInt() % reservations.size();
            setOutMessage(reservations.get(r));
        }
        //Debug logging: Expects to have read a reservation of whichever agentID wishing to migrate to this Building or 0 if none
        StringBuilder convert = new StringBuilder();
        convert.append("showReservation2: Building[").append(getIndex()[0]).
                append("][").append(getIndex()[1]).append("] is reserved (").
                append(getOutMessage()).append(")");
        logger.debug(convert.toString());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("showReservation2"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * (void)	#LOG visually confirmed 160728 Record the outmessage of neighbors
     * as population pre: neighborPop is initialized and contains 4 -1s,
     * outmessage of Buildings are population
     */
    Object delegateCalculation() //#####################
    {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("delegateCalculation"), new StringBuffer("label"), true, null, null, true);
        if (printOutputK) {
            logger.debug("inside Building::delegateCalculation");
        }
        //Only calculate if delegated
        if (getAgents().isEmpty() || myAgent == -1) {
            return null;
        }
        //Record neighbor population as before
        int[] North = {0, 1};
        int[] East = {1, 0};
        int[] South = {0, -1};
        int[] West = {-1, 0};
        Object outMsg = null;
        try {
            outMsg = getOutMessage(1, North);
        } catch (Exception e) {
        }

        int ptr = outMsg == null ? 0 : (int) outMsg;
        neighborPop[0] = ptr;
        outMsg = null;
        try {
            outMsg = getOutMessage(1, East);
        } catch (Exception e) {
        }
        ptr = outMsg == null ? 0 : (int) outMsg;
        neighborPop[1] = ptr;
        outMsg = null;
        try {
            outMsg = getOutMessage(1, South);
        } catch (Exception e) {
        }
        ptr = outMsg == null ? 0 : (int) outMsg;
        neighborPop[2] = ptr;
        try {
            outMsg = getOutMessage(1, West);
        } catch (Exception e) {
        }
        ptr = outMsg == null ? 0 : (int) outMsg;
        neighborPop[3] = ptr;

        //get the building dimensions x and y
        int sizeX = getSize()[0];
        int sizeY = getSize()[1];

        int hallway_x = sizeX / 2; // hallway location
        int wall_y = sizeY / 2;

        int door_1 = sizeY * 1 / 4; // first door y location
        int door_2 = sizeY * 3 / 4; // second door y location

        boolean atDoor = false;	  // true if agent is at any of the doors
        boolean atExit = false;	  // true if the agent made it all the way to the exit

        int x = getIndex()[0];		  //grab the x coordinates of the agent 
        int y = getIndex()[1];		  //grab the y coordinates of the agent

        //Calculate nextmove as before
        Vector<Integer> moveset;

        // check if the agent is by a door
        if ((y == door_1) || (y == door_2)) {
            atDoor = true;
        }

        //check if the agent made it to the exit
        if ((x == hallway_x) && (y == sizeY - 1)) {
            atExit = true;
        }

        if (debugK) {
            logger.debug("inside Building::delegateCalculation, before for loop");
            for (int i = 0; i < (int) cardinals.size(); i++) {
                /* Case 1: Agent at left side of the hallway
		  
		        Hallway_x
		 _ _ _ _ _ _ _ _ _ _ _     ----> X
		|         | |         |    |
		|                     |    |
		|        A| |         |    Y
		|_ _ _ _ _| |_ _ _ _ _| wall_y
		|         | |         |
		|        A| |         |
		|                     |
		|_ _ _ _ _| |_ _ _ _ _|
					
				  Exit
                 */

                if ((x == hallway_x - 1) && !atDoor && i == 1) // i == 1 to not move east 
                {
                    continue;
                }

                /* Case 2: Agent at the upper rooms, and cannot cross the wall
		  
		         Hallway
		 _ _ _ _ _ _ _ _ _ _ _     ----> X
		|         | |         |    |
		|                     |    |
		|         | |         |    Y
		|_ _A_ _ _| |_ _A_ _ _| wall_y
		|         | |         |
		|         | |         |
		|                     |
		|_ _ _ _ _| |_ _ _ _ _|
		 
		         Exit
                 */
                if ((y == wall_y - 1) && x != hallway_x && i == 2) // i = 2 restrict south movement
                {
                    continue;
                }

                /* Case 3:Agent at the lower rooms and cannot cross the wall
		  
		        Hallway_x
		 _ _ _ _ _ _ _ _ _ _ _     ----> X
		|         | |         |    |
		|                     |    |
		|         | |         |    Y
		|_ _ _ _ _| |_ _ _ _ _| wall_y
		|   A     | |     A   |
		|         | |         |
		|                     |
		|_ _ _ _ _| |_ _ _ _ _|
		
				 Exit
                 */
                if ((y == wall_y + 1) && x != hallway_x && i == 0) // i = 0 not to move north 
                {
                    continue;
                }

                /* Case 4: agent at the right side of the hallway and shouldn't move left
		  
		       Hallway_x
		 _ _ _ _ _ _ _ _ _ _ _     ----> X
		|         | |         |    |
		|                     |    |
		|         | |A        |    Y
		|_ _ _ _ _| |_ _ _ _ _| wall_y
		|         | |         |
		|         | |A        |
		|                     |
		|_ _ _ _ _| |_ _ _ _ _|
		
				 Exit
                 */
                if (x == (hallway_x + 1) && i == 3 && !atDoor) // i == 3 to not move West
                {
                    continue;
                }

                /* Case 5: Agent at the exit. it is done. 
		  
		        Hallway_x
		 _ _ _ _ _ _ _ _ _ _ _     ----> X
		|         | |         |    |
		|                     |    |
		|         | |         |    Y
		|_ _ _ _ _| |_ _ _ _ _|  wall_y
		|         | |         |
		|         | |         |
		|                     |
		|_ _ _ _ _|A|_ _ _ _ _|
		
				 Exit
                 */
                if (atExit) {
                    continue;
                }

                /* Case 6: Agent at the hallway
		  
		        Hallway_x
		 _ _ _ _ _ _ _ _ _ _ _     ----> X
		|         | |         |    |
		|                     |    |
		|         | |         |    Y
		|_ _ _ _ _| |_ _ _ _ _|  wall_y
		|         |A|         |
		|         | |         |
		|          A          |
		|_ _ _ _ _|A|_ _ _ _ _|
				
				  Exit
                 */
 /*if((x == hallway_x) && (neighborPop[0] == 0))
		{
			moveset.push_back(0);
		}
                 */
                //Saves valid route for agent in moveset vector
                if (neighborPop[i] == 0) {
                    moveset.add(i);
                }
            }

            if (!moveset.isEmpty()) {
                //	MASS_base::log("Move Set not empty");

                if (getIndex()[0] == hallway_x) // if in the hallway, limit direction to South Only toward the exit 
                {
                    nextMove[0] = 0;
                    nextMove[1] = 1;
                } else {
                    int r = new Random().nextInt() % moveset.size();	// agents moves in random
                    nextMove[0] = neighbor[moveset.get(r)][0];
                    nextMove[1] = neighbor[moveset.get(r)][1];
                }

            }
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("delegateCalculation"), procRID, new StringBuffer("null"), null, null, null, true, false, false, true);
        StopWatch.stop(false);
        return null;
    }
}
