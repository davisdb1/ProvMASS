package edu.uw.bothell.css.dsl.MASS.prov.SugarScape;

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.Main;
import edu.uw.bothell.css.dsl.MASS.prov.ProvOntology;
import java.util.*;

// Library for Multi-Agent Spatial Simulation
import edu.uw.bothell.css.dsl.MASS.prov.core.Agents;
import edu.uw.bothell.css.dsl.MASS.prov.core.MASS;
import edu.uw.bothell.css.dsl.MASS.prov.core.Places;
import edu.uw.bothell.css.dsl.MASS.prov.filter.AgentFilter;
import edu.uw.bothell.css.dsl.MASS.prov.filter.PlaceFilter;
import edu.uw.bothell.css.dsl.MASS.prov.store.Granularity;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.store.ResourceMatcher;
import edu.uw.bothell.css.dsl.MASS.prov.store.StoreManager;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;

public class SugarScape {

    public static void main(String[] args) {
        ProvUtils.stageEvaluation(args);

        int vDist = 2;
        int size = 25;
        int nAgents = size * size;
        int maxTime = 10;

        // try for size argument
        String sizeArgValue = ProvUtils.getArgRemainder("size=", args);
        if (sizeArgValue != null) {
            try {
                size = Integer.valueOf(sizeArgValue);
            } catch (NumberFormatException e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }

        // try for size argument
        String populationArgValue = ProvUtils.getArgRemainder("population=", args);
        if (populationArgValue != null) {
            try {
                nAgents = Integer.valueOf(populationArgValue);
            } catch (NumberFormatException e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }

        // try for agents
        String timeArgValue = ProvUtils.getArgRemainder("time=", args);
        if (timeArgValue != null) {
            try {
                maxTime = Integer.valueOf(timeArgValue);
            } catch (NumberFormatException e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }

        // try for agent filter
        String agentFilterValue = ProvUtils.getArgRemainder("agentfilter=", args);
        if (agentFilterValue != null) {
            AgentFilter agentFilter = AgentFilter.fromString(agentFilterValue);
            if (agentFilter != null) {
                MASSProv.agentFilter = agentFilter;
            }
        }

        // try for place filter
        String placeFilterValue = ProvUtils.getArgRemainder("placefilter=", args);
        if (placeFilterValue != null) {
            PlaceFilter placeFilter = PlaceFilter.fromString(placeFilterValue);
            if (placeFilter != null) {
                MASSProv.placeFilter = placeFilter;
            }
        }

        // try for filter criteria
        String agentFilterCriteriaValue = ProvUtils.getArgRemainder("agentfiltercriteria=", args);
        if (agentFilterCriteriaValue != null) {
            Object agentFilterCriteria = AgentFilter.getCriteria(MASSProv.agentFilter, agentFilterCriteriaValue);
            if (agentFilterCriteria != null) {
                MASSProv.agentFilterCriteria = agentFilterCriteria;
            }
        }

        // try for filter criteria
        String placeFilterCriteriaValue = ProvUtils.getArgRemainder("placefiltercriteria=", args);
        if (placeFilterCriteriaValue != null) {
            Object placeFilterCriteria = PlaceFilter.getCriteria(MASSProv.placeFilter, placeFilterCriteriaValue);
            if (placeFilterCriteria != null) {
                MASSProv.placeFilterCriteria = placeFilterCriteria;
            }
        }

//        printConfig(size, nAgents, maxTime);
//        MASS.setLoggingLevel(LogLevel.DEBUG);
        int nThr = Runtime.getRuntime().availableProcessors();
//        System.err.println("Setting thread-count to: " + nThr);
        MASS.setNumThreads(nThr);
        MASS.setNodeFilePath(Main.NODE_FILE);
        MASS.init();
        ProvenanceStore store = ProvUtils.getStoreOfCurrentThread();
        ProvUtils.endSlice();
// du -sh --apparent-size /tmp/davisdb1/MASSProvData
        StringBuffer places_RID1 = new StringBuffer();
        ResourceMatcher matcher = ResourceMatcher.getMatcher();
        if (MASSProv.provOn && store != null
                && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            StringBuffer size_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("size")),
                    size_RID2 = ProvUtils.getUniversalResourceID(new StringBuffer("size")),
                    size_RID3 = ProvUtils.getUniversalResourceID(new StringBuffer("size")),
                    handle_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("handle")),
                    className_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("className")),
                    argument_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("argument")),
                    sizes_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("sizes"));
            store.addRelationalProv(sizes_RID1, ProvOntology.getRDFTypeFullURIBuffer(),
                    ProvOntology.getCollectionExpandedClassFullURIBuffer());
            store.addRelationalProv(size_RID3, ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(), size_RID1);
            store.addRelationalProv(size_RID2, ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(), size_RID1);
            store.addRelationalProv(sizes_RID1, ProvOntology.getHadMemberExpandedPropertyFullURIBuffer(), size_RID2);
            store.addRelationalProv(sizes_RID1, ProvOntology.getHadMemberExpandedPropertyFullURIBuffer(), size_RID3);
            matcher.pushEntityID(handle_RID1);
            matcher.pushEntityID(className_RID1);
            matcher.pushEntityID(sizes_RID1);

            places_RID1 = ProvenanceRecorder.documentProcedure(store, null,
                    new StringBuffer("Places"), new StringBuffer("label"), true, new String[]{"handle", "className",
                        "argument", "size"}, new Object[]{1, Land.INIT_RID, null,
                        new int[]{size, size}}, true);
        }

        // Create a Land array.
        Places land = new Places(1, Land.class.getName(), null, size, size); // exchange all

        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            // do end doc here (remember ignore granularity boolean)
            if (places_RID1 != null && places_RID1.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null, new StringBuffer("Places"), places_RID1, null, null, null, null, true, false, false, true);
            }
        }

        land.callAll(Land.INIT);   // exchange boundary
        ProvenanceRecorder.documentFieldAccess(ProvUtils.getStoreOfCurrentThread(), new StringBuffer("INIT"), Land.INIT_RID, MASSProv.getDriverRID(), Land.INIT, true);

        StringBuffer agents_RID1 = new StringBuffer();
        if (MASSProv.provOn && store != null
                && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            StringBuffer handle_RID2 = ProvUtils.getUniversalResourceID(new StringBuffer("handle")),
                    className_RID2 = ProvUtils.getUniversalResourceID(new StringBuffer("className")),
                    argument_RID2 = ProvUtils.getUniversalResourceID(new StringBuffer("argument")),
                    places_RID2 = ProvUtils.getUniversalResourceID(new StringBuffer("places")),
                    initPopulation_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("initPopulation"));
            store.addRelationalProv(places_RID2, ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(), places_RID1);

            matcher.pushEntityID(handle_RID2);
            matcher.pushEntityID(className_RID2);
            matcher.pushEntityID(places_RID2);
            matcher.pushEntityID(initPopulation_RID1);

            agents_RID1 = ProvenanceRecorder.documentProcedure(store, null,
                    new StringBuffer("Agents"), new StringBuffer("label"), true, new String[]{"handle", "className",
                        "argument", "places", "initPopulation"}, new Object[]{2,
                        Unit.decideNewPosition_, null, land, nAgents}, true);
        }
        // Populate Agents (unit) on the Land array
        Agents unit = new Agents(2, Unit.class.getName(), null, land, nAgents);

        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            // do end doc here (remember ignore granularity boolean)
            if (agents_RID1 != null && agents_RID1.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null,
                        new StringBuffer("Agents"), agents_RID1, null, null, null, null, true,
                        false, false, true);
            }
        }

        // Define the neighbors of each cell   
        Vector<int[]> neighbors = new Vector<int[]>();
        StringBuffer forLoop_RID1 = new StringBuffer();
        StringBuffer forLoop_RID2 = new StringBuffer();
        StringBuffer neighbors_RID1 = new StringBuffer();
        if (MASSProv.provOn && store != null
                && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            neighbors_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("neighbors"));
            ProvenanceRecorder.documentEntity(store, null, null, null, null,
                    new StringBuffer("neighbors"), neighbors, neighbors_RID1, null, true);

            String vDist_RID1 = ProvUtils.getUniversalResourceID("vDist");
            String x_RID1 = ProvUtils.getUniversalResourceID("x");

            store.addRelationalProv(neighbors_RID1, ProvOntology.getRDFTypeFullURIBuffer(),
                    ProvOntology.getCollectionExpandedClassFullURIBuffer());

            matcher.pushEntityID(vDist_RID1);
            matcher.pushEntityID(x_RID1);

            forLoop_RID1 = ProvenanceRecorder.documentProcedure(store, null,
                    new StringBuffer("ForLoop"), new StringBuffer("label"), true, new String[]{"x", "vDist"},
                    new Object[]{0 - vDist, vDist}, true);

        }
        for (int x = 0 - vDist; x <= vDist; x++) {
            StringBuffer x_RID2 = new StringBuffer();
            if (MASSProv.provOn && store != null
                    && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                    && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
                x_RID2 = ProvUtils.getUniversalResourceID(new StringBuffer("x"));
                ProvenanceRecorder.documentEntity(store, null, null, null,
                        null, new StringBuffer("x"), x, x_RID2, null, true);

                String y_RID1 = ProvUtils.getUniversalResourceID("y");
                matcher.pushEntityID(y_RID1);
                matcher.pushEntityID(x_RID2);

                forLoop_RID2 = ProvenanceRecorder.documentProcedure(store, null,
                        new StringBuffer("ForLoop"), new StringBuffer("label"), true, new String[]{"x", "y", "vDist"},
                        new Object[]{0 - vDist, 0 - vDist, vDist}, true);
                store.addRelationalProv(forLoop_RID1,
                        ProvOntology.getUsedStartingPointPropertyFullURIBuffer(),
                        forLoop_RID2);
            }
            StringBuffer y_RID1 = new StringBuffer();
            for (int y = 0 - vDist; y <= vDist; y++) {
                if (!(x == 0 && y == 0)) {
                    neighbors.add(new int[]{x, y});

                    if (MASSProv.provOn && store != null
                            && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                            && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
                        StringBuffer y_RID2;
                        if (y_RID1 == null || y_RID1.length() == 0) {
                            y_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("y"));
                        }

                        y_RID2 = ProvUtils.getUniversalResourceID(new StringBuffer("y"));
                        store.addRelationalProv(forLoop_RID2,
                                ProvOntology.getUsedStartingPointPropertyFullURIBuffer(),
                                neighbors_RID1);
                        ProvenanceRecorder.documentEntity(store, null, null,
                                null, null, new StringBuffer("y"), y, y_RID2, y_RID1, true);

                        StringBuffer neighborsCell_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("neighborsCell"));
                        store.addRelationalProv(neighborsCell_RID1, ProvOntology.getRDFTypeFullURIBuffer(),
                                ProvOntology.getCollectionExpandedClassFullURIBuffer());
                        store.addRelationalProv(neighborsCell_RID1, ProvOntology.getHadMemberExpandedPropertyFullURIBuffer(), x_RID2);
                        store.addRelationalProv(neighborsCell_RID1, ProvOntology.getHadMemberExpandedPropertyFullURIBuffer(), y_RID2);

                        store.addRelationalProv(neighbors_RID1,
                                ProvOntology.getHadMemberExpandedPropertyFullURIBuffer(),
                                neighborsCell_RID1);
                    }
                }
            }

            if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                    && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
                if (forLoop_RID2 != null && forLoop_RID2.length() > 0) {
                    ProvenanceRecorder.endProcedureDocumentation(store, null,
                            new StringBuffer("ForLoop"), forLoop_RID2, null, null, null, null, true,
                            false, false, true);
                }
            }

        }
        land.setAllPlacesNeighbors(neighbors);

        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            if (forLoop_RID1 != null && forLoop_RID1.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null,
                        new StringBuffer("ForLoop"), forLoop_RID1, null, null, null, null, true,
                        false, false, true);
            }
        }

        StringBuffer agentsCallAllObjects_RID1 = new StringBuffer();
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            String size_RID4 = ProvUtils.getUniversalResourceID("size");
            matcher.pushEntityID(size_RID4);
            agentsCallAllObjects_RID1 = ProvenanceRecorder.documentProcedure(
                    store, null, new StringBuffer("Object[]"),
                    new StringBuffer("label"), true, new String[]{"size"},
                    new Object[]{size * size}, true);
        }

        Object[] agentsCallAllObjects = new Object[size * size];

        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            if (agentsCallAllObjects_RID1 != null && agentsCallAllObjects_RID1.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null, new StringBuffer("Object[]"), agentsCallAllObjects_RID1, null, null, null, null, true, false, false, true);
            }
        }

        StringBuffer forLoop_RID3 = new StringBuffer();
        String time_RID1 = "";
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            time_RID1 = ProvUtils.getUniversalResourceID("time");
            matcher.pushEntityID(time_RID1);
            forLoop_RID3 = ProvenanceRecorder.documentProcedure(
                    store, null, new StringBuffer("ForLoop"),
                    new StringBuffer("label"), true, new String[]{"time", "maxTime"},
                    new Object[]{0, maxTime}, true);
        }

        // Start simulation time
        for (int time = 0; time < maxTime; time++) {
            // on last time
//            if (time == maxTime - 1) {
//                //////////////// GET NETWORK LOAD BALANCE INFO /////////////////
//                //MASS.measureHostDelay();
//                ////////////////////////////////////////////////////////////////
//            }
            // Exchange #agents with neighbors
            land.exchangeAll(1, Land.EXCHANGE);			// << exchangeAll()
            ProvenanceRecorder.documentFieldAccess(ProvUtils.getStoreOfCurrentThread(), new StringBuffer("EXCHANGE"), Land.EXCHANGE_RID, MASSProv.getDriverRID(), Land.EXCHANGE, true);

            if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                    && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
                store.addRelationalProv(forLoop_RID3, ProvOntology.getUsedStartingPointPropertyFullURIBuffer(), Land.EXCHANGE_RID);
            }

            land.callAll(Land.UPDATE);
            ProvenanceRecorder.documentFieldAccess(ProvUtils.getStoreOfCurrentThread(), new StringBuffer("UPDATE"), Land.UPDATE_RID, MASSProv.getDriverRID(), Land.UPDATE, true);

            if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                    && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
                store.addRelationalProv(forLoop_RID3, ProvOntology.getUsedStartingPointPropertyFullURIBuffer(), Land.UPDATE_RID);
            }

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // !!!!!!!!!!!!!!!!DOCUMENT LAST TWO OPERATIONS ONLY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if (time >= 8 && time <= 9) {
                ProvUtils.startSlice();
            }
            // Move agents to a neighbor with the least population
            Object[] callAllResults = (Object[]) unit.callAll(Unit.decideNewPosition_, agentsCallAllObjects); // N - 1
            ProvenanceRecorder.documentFieldAccess(ProvUtils.getStoreOfCurrentThread(), new StringBuffer("decideNewPostion_"), Unit.decideNewPosition__RID, MASSProv.getDriverRID(), Unit.decideNewPosition_, true);
            if (time >= 8 && time <= 9) {
                ProvUtils.endSlice();
            }
            unit.manageAll(); // N

            if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                    && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
                store.addRelationalProv(forLoop_RID3, ProvOntology.getUsedStartingPointPropertyFullURIBuffer(), Unit.decideNewPosition__RID);
            }
        }

        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            if (forLoop_RID3 != null && forLoop_RID3.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null, new StringBuffer("ForLoop"), forLoop_RID3, null, null, null, null, true, false, false, true);
            }
        }

        // finish MASS
        MASS.finish();
    }

    private static void printConfig(int size, int nAgents, int maxTime) {
        System.err.println("provOn: " + MASSProv.provOn);
        System.err.println("granularity: " + StoreManager.getGranularityLevel());
        System.err.println("maxstores: " + StoreManager.getMaxProvStoreSize());
        System.err.println("buffersperstore: " + StoreManager.getBuffersPerStore());
        System.err.println("linesperbuffer: " + StoreManager.getLinesPerBuffer());
        System.err.println("charsperline: " + StoreManager.getCharsPerLine());
        System.err.println("size: " + size);
        System.err.println("population: " + nAgents);
        System.err.println("time: " + maxTime);
        System.err.println("agentfilter: " + MASSProv.agentFilter);
        System.err.println("agentfiltercriteria: " + MASSProv.agentFilterCriteria);
        switch (MASSProv.agentFilter) {
            case ALL_BY_COORDINATES:
                int[] criteria = {-1};
                try {
                    criteria = (int[]) MASSProv.agentFilterCriteria;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
//                System.err.println("\t" + criteria);
                break;
            case ALL_BY_LINEAR_INDEX:
                int criterion = -1;
                try {
                    criterion = (int) MASSProv.agentFilterCriteria;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                System.err.println("\t" + criterion);
                break;
            case FRACTION:
                int denominator = -1;
                try {
                    denominator = (int) MASSProv.agentFilterCriteria;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                System.err.println("\t" + denominator);
                break;
            case SINGLE_BY_COORDINATES:
                int[] coordinates = {-1};
                try {
                    coordinates = (int[]) MASSProv.agentFilterCriteria;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                for (int i = 0, im = coordinates.length; i < im; i++) {
                    System.err.println("\t" + coordinates[i]);
                }
                break;
            case SINGLE_BY_ID:
                int id = -1;
                try {
                    id = (int) MASSProv.agentFilterCriteria;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                System.err.println("\t" + id);
                break;
            case BY_ID_RANGE:
                int[] pairs = null;
                if (MASSProv.agentFilterCriteria != null) {
                    try {
                        pairs = (int[]) MASSProv.agentFilterCriteria;
                    } catch (ClassCastException e) {
                        e.printStackTrace(IO.getLogWriter());
                    }
                }
                for (int i = 0, im = pairs.length; i < im; i += 2) {
                    System.err.println("\t" + pairs[i] + " " + pairs[i + 1]);
                }
                break;
            case SINGLE_BY_LINEAR_INDEX:
                int linearIndex = -1;
                try {
                    linearIndex = (int) MASSProv.agentFilterCriteria;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                System.err.println("\t" + linearIndex);
                break;
            default:
                break;
        }
        System.err.println("placefilter: " + MASSProv.placeFilter);
        System.err.println("placefiltercriteria: " + MASSProv.placeFilterCriteria);
    }
}
