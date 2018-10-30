package edu.uw.bothell.css.dsl.MASS.prov.filter;

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import static edu.uw.bothell.css.dsl.MASS.prov.MASSProv.provOn;
import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledObject;
import edu.uw.bothell.css.dsl.MASS.prov.core.Agent;
import edu.uw.bothell.css.dsl.MASS.prov.core.Place;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import java.util.Set;

/**
 * An AgentFilter specifies how provenance capture will be mapped to specific
 * agent instances
 *
 * @author Delmar B. Davis
 */
public enum AgentFilter {
    SINGLE_BY_LINEAR_INDEX, SINGLE_BY_COORDINATES, SINGLE_BY_ID, BY_ID_RANGE,
    FRACTION, ALL_BY_COORDINATES, ALL_BY_LINEAR_INDEX, ALL, NONE;
    
    public static AgentFilter fromString(String filterEnum) {
        AgentFilter filter = null;
        if (filterEnum.toLowerCase().equals(AgentFilter.ALL.toString().toLowerCase())) {
            filter = AgentFilter.ALL;
        } else if (filterEnum.toLowerCase().equals(AgentFilter.NONE.toString().toLowerCase())) {
            filter = AgentFilter.NONE;
        } else if (filterEnum.toLowerCase().equals(AgentFilter.ALL_BY_COORDINATES.toString().toLowerCase())) {
            filter = AgentFilter.ALL_BY_COORDINATES;
        } else if (filterEnum.toLowerCase().equals(AgentFilter.ALL_BY_LINEAR_INDEX.toString().toLowerCase())) {
            filter = AgentFilter.ALL_BY_LINEAR_INDEX;
        } else if (filterEnum.toLowerCase().equals(AgentFilter.FRACTION.toString().toLowerCase())) {
            filter = AgentFilter.FRACTION;
        } else if (filterEnum.toLowerCase().equals(AgentFilter.SINGLE_BY_COORDINATES.toString().toLowerCase())) {
            filter = AgentFilter.SINGLE_BY_COORDINATES;
        } else if (filterEnum.toLowerCase().equals(AgentFilter.SINGLE_BY_ID.toString().toLowerCase())) {
            filter = AgentFilter.SINGLE_BY_ID;
        } else if (filterEnum.toLowerCase().equals(AgentFilter.BY_ID_RANGE.toString().toLowerCase())) {
            filter = AgentFilter.BY_ID_RANGE;
        } else if (filterEnum.toLowerCase().equals(AgentFilter.SINGLE_BY_LINEAR_INDEX.toString().toLowerCase())) {
            filter = AgentFilter.SINGLE_BY_LINEAR_INDEX;
        }
        return filter;
    }
    
    public static void filter(AgentFilter FILTER, Agent agent, Object FILTER_CRITERIA) {
	StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), agent, new StringBuffer("setPlace"), new StringBuffer("label"), true, new String[]{"place"}, new Object[]{agent.getPlace()});
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), agent, new StringBuffer("setPlace"), procRID, null, null, null, null, true, false, false);
        switch (FILTER) {
            case ALL_BY_COORDINATES:
                int[] criteria = {-1};
                try {
                    criteria = (int[]) FILTER_CRITERIA;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                mapAllAgentsAtSinglePlace(agent, criteria);
                break;
            case ALL_BY_LINEAR_INDEX:
                int criterion = -1;
                try {
                    criterion = (int) FILTER_CRITERIA;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                mapAllAgentsAtSinglePlace(agent, criterion);
                break;
            case FRACTION:
                int denominator = -1;
                try {
                    denominator = (int) FILTER_CRITERIA;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                mapFraction(agent, denominator);
                break;
            case SINGLE_BY_COORDINATES:
                int[] coordinates = {-1};
                try {
                    coordinates = (int[]) FILTER_CRITERIA;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                mapSingleAgent(agent, coordinates);
                break;
            case SINGLE_BY_ID:
                int id = -1;
                try {
                    id = (int) FILTER_CRITERIA;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                mapSingleAgent(agent, id);
                break;
            case BY_ID_RANGE:
                int[] pairs = null;
                if (FILTER_CRITERIA != null) {
                    try {
                        pairs = (int[]) FILTER_CRITERIA;
                    } catch (ClassCastException e) {
                        e.printStackTrace(IO.getLogWriter());
                    }
                }
                mapByIDRange(agent, pairs);
                break;
            case SINGLE_BY_LINEAR_INDEX:
                int linearIndex = -1;
                try {
                    linearIndex = (int) FILTER_CRITERIA;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                mapSingleAgent(linearIndex, agent);
                break;
            case ALL:
                mapAll(agent);
                break;
            case NONE:
                mapNone(agent);
                break;
            default:
                agent.setProvOn(false);
                break;
        }
        if (agent.isProvOn()) {
            if (agent instanceof ProvEnabledObject) {
                ((ProvEnabledObject) agent).substituteConstructorDocumentation();
            }
        }
    }
    
    public static Object getCriteria(AgentFilter FILTER, String criteriaValue) {
        Object criteria = null;
        switch (FILTER) {
            case ALL_BY_COORDINATES:
                try {
                    int[] coordinates = {-1};
                    String[] coords = criteriaValue.split(",");
                    for (int i = 0, im = coords.length; i < im; i++) {
                        try {
                            coordinates[i] = Integer.valueOf(coords[i]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace(IO.getLogWriter());
                            break;
                        }
                        if (i == im - 1) {
                            criteria = coordinates;
                        }
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                break;
            case ALL_BY_LINEAR_INDEX:
                try {
                    int index = Integer.valueOf(criteriaValue);
                    criteria = index;
                } catch (NumberFormatException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                break;
            case FRACTION:
                try {
                    int denominator = Integer.valueOf(criteriaValue);
                    criteria = denominator;
                } catch (NumberFormatException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                break;
            case SINGLE_BY_COORDINATES:
                try {
                    int[] coordinates = {-1};
                    String[] coords = criteriaValue.split(",");
                    for (int i = 0, im = coords.length; i < im; i++) {
                        try {
                            coordinates[i] = Integer.valueOf(coords[i]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace(IO.getLogWriter());
                            break;
                        }
                        if (i == im - 1) {
                            criteria = coordinates;
                        }
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                break;
            case SINGLE_BY_ID:
                try {
                    int id = Integer.valueOf(criteriaValue);
                    criteria = id;
                } catch (NumberFormatException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                break;
            case BY_ID_RANGE:
                int[] pairs = null;
                if (criteriaValue != null) {
                    try {
                        String[] startAndEndPairsText = criteriaValue.split(",");
                        int remainder = startAndEndPairsText.length % 2;
                        pairs = new int[startAndEndPairsText.length - remainder];
                        for (int i = 0, im = pairs.length; i < im; i++) {
                            pairs[i] = Integer.parseInt(startAndEndPairsText[i]);
                        }
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        e.printStackTrace(IO.getLogWriter());
                    }
                }
                criteria = pairs;
                break;
            case SINGLE_BY_LINEAR_INDEX:
                try {
                    int index = Integer.valueOf(criteriaValue);
                    criteria = index;
                } catch (NumberFormatException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                break;
            default:
                break;
        }
        return criteria;
    }
    
    private static void mapAll(Agent agent) {
        agent.setProvOn(true);
    }
    
    private static void mapNone(Agent agent) {
        agent.setProvOn(false);
    }

    /**
     * Maps provenance capture for a specific fraction of the agents in the in
     * the places where the specified agent resides. For example, if 1/6 of the
     * agents within the places where this agent resides should have provenance
     * capture turned on, then every 6th agent that is evaluated by this
     * procedure will have its provenance turned on. In the example, the
     * specified denominator is 6.
     *
     * @param agent - the agent whose provenance capture functionality is being
     * mapped
     * @param denominator - the denominator (with a numerator of one) of the
     * fraction of agents to capture provenance for
     */
    private static void mapFraction(Agent agent, int denominator) {
        agent.setProvOn(!agent.isProvOn() && denominator > 0
                && agent.getLinearIndex() % denominator == 0);
    }

    /**
     * Maps provenance capture for a single agent based on the coordinates of
     * the place where the agent resides. Note: if more than one agent is mapped
     * to the place, only one of the agents will be mapped for provenance
     * capture
     *
     * @param agent - the agent whose provenance capture functionality is being
     * mapped
     * @param coordinates - coordinates within the places collection where the
     * specified agent resides, that should match the coordinates of the
     * specified agent in order to map the agent for provenance capture
     */
    private static void mapSingleAgent(Agent agent, int[] coordinates) {
        agent.setProvOn(!agent.provOn
                && coordinatesMatch(agent.getIndex(), coordinates));
    }

    /**
     * Maps provenance capture for a single agent based on the index of the
     * place where the agent resides. Note: if more than one agent is mapped to
     * the place, only one of the agents will be mapped for provenance capture
     *
     * @param index - the linear index of the place of an agent that should have
     * provenance capture turned on. The linear index is the offset into the
     * multi-dimensional array (e.g. 26 is the index of 2,2,2 in a 3x3x3
     * 3-dimensional collection of Place objects)
     * @param agent - the agent who whose provenance capture functionality is
     * being mapped
     */
    private static void mapSingleAgent(int index, Agent agent) {
        // set prov on to true if the coordinate index matches and no 
        // siblings have prov on and the place matches
        agent.setProvOn(!agent.isProvOn()
                && agent.getLinearIndex() == index
                && !doesAnyAgentAtPlaceHaveProvOn(agent.getPlace()));
    }

    /**
     * Maps a single agent for provenance capture, based on the agent's id
     *
     * @param agent - the agent who whose provenance capture functionality is
     * being mapped
     * @param id - The ID of the agent is an incrementally provided number,
     * which may be recycled when an agent dies and reused by newly spawned
     * agents, thereafter.
     */
    private static void mapSingleAgent(Agent agent, int id) {
        agent.setProvOn(!agent.provOn && agent.getAgentId() == id);
    }

    /**
     * Maps all agents at a specified place for provenance capture.
     *
     * @param agent - the agent who whose provenance capture functionality is
     * being mapped
     * @param index - these are the coordinates of the place where the agent
     * should reside if its provenance capture is to be turned on (note:
     * agent.getIndex() and agent.getPlace().getIndex() should be identical in
     * MASS)
     */
    private static void mapAllAgentsAtSinglePlace(Agent agent, int[] index) {
        agent.setProvOn(!agent.isProvOn() && coordinatesMatch(agent.getIndex(), index));
    }

    /**
     * Maps all agents at a specified place for provenance capture.
     *
     * @param index - the linear index of the place of the specified agent that
     * should have provenance capture turned on. The linear index is the offset
     * into the multi-dimensional array (e.g. 26 is the index of 2,2,2 in a
     * 3x3x3 3-dimensional collection of Place objects)
     * @param agent - the agent who whose provenance capture functionality is
     * being mapped
     */
    private static void mapAllAgentsAtSinglePlace(Agent agent, int index) {
        agent.setProvOn(!agent.isProvOn() && agent.getLinearIndex() == index);
    }

    /**
     * Indicates whether any agents residing at the specified place (yes there
     * can be multiple) have provenance capture turned on, already.
     *
     * @param place - The place in which to check residing agent's prov capture
     * setting
     * @return True if any agents residing at the specified place have
     * provenance capture turned on
     */
    private static boolean doesAnyAgentAtPlaceHaveProvOn(Place place) {
        /* make sure no agents at this place have prov turned on */
        boolean found = false; // we will see if one is found
        Set<Agent> agentsAtPlace = place.getAgents();
        for (Agent sibling : agentsAtPlace) {
            if (sibling.isProvOn()) { // one was found
                found = true; // flag it found
                break; // short-circuit the rest of this operation
            }
        }
        return found;
    }

    /**
     * Indicates whether or not the coordinates in two sets of coordinates match
     *
     * @param sourceCoordinates - coordinates to compare against
     * @param targetCoordinates - coordinates being compared
     * @return True if each value within sourceCoordinates matches each value
     * within targetCoordinates
     */
    private static boolean coordinatesMatch(int[] sourceCoordinates, int[] targetCoordinates) {
        boolean same = true;
        if (sourceCoordinates.length != targetCoordinates.length) {
            same = false;
        } else {
            //if agent index matches with provide index turn prov on
            for (int i = 0; i < sourceCoordinates.length; i++) {
                if (sourceCoordinates[i] != targetCoordinates[i]) {
                    same = false;
                    break;
                }
            }
        }
        return same;
    }
    
    private static void mapByIDRange(Agent agent, int[] pairs) {
        if (pairs != null && !agent.provOn) {
            boolean inRange = false;
            int agentID = agent.getAgentId();
            for (int i = 0, im = pairs.length; i < im; i += 2) {
                if (inRange = agentID >= pairs[i] && agentID <= pairs[i + 1]) {
                    break;
                }
            }
            agent.setProvOn(inRange);
        }
    }
}
