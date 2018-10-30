package edu.uw.bothell.css.dsl.MASS.prov.QuickStart;

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.core.Places;
import edu.uw.bothell.css.dsl.MASS.prov.core.Agents;
import edu.uw.bothell.css.dsl.MASS.prov.core.MASS;
import edu.uw.bothell.css.dsl.MASS.prov.Main;
import edu.uw.bothell.css.dsl.MASS.prov.ProvOntology;
import edu.uw.bothell.css.dsl.MASS.prov.filter.AgentFilter;
import edu.uw.bothell.css.dsl.MASS.prov.filter.PlaceFilter;
import edu.uw.bothell.css.dsl.MASS.prov.store.Granularity;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.store.ResourceMatcher;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;

/**
 * QuickStart class modified to collect simulation execution provenance
 *
 * @author Mathew Sell and Delmar B. Davis
 */
public class TestDriver {

    public static void main(String args[]) {
        ProvUtils.stageEvaluation(args);

        // the total number of Place objects that will be created is x*y*z
        int x = 25;	// 462
        int y = 25;	// 222
        int z = 1;	// 150
        int nAgents = x * y;
        int maxTime = 10;

        // try for size argument
        String sizeArgValue = ProvUtils.getArgRemainder("size=", args);
        if (sizeArgValue != null) {
            try {
                x = y = Integer.valueOf(sizeArgValue);
                nAgents = x * y;
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
                nAgents = x * y;
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

        // start MASS core
        MASS.setNodeFilePath(Main.NODE_FILE);

        int nThr = Runtime.getRuntime().availableProcessors();
        MASS.setNumThreads(nThr);
        MASS.init();
        ProvUtils.endSlice();

        StringBuffer places_RID1 = new StringBuffer();
        ProvenanceStore store = ProvUtils.getStoreOfCurrentThread();
        ResourceMatcher matcher = ResourceMatcher.getMatcher();
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            StringBuffer size_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("size"));
            StringBuffer size_RID2 = ProvUtils.getUniversalResourceID(new StringBuffer("size"));
            StringBuffer size_RID3 = ProvUtils.getUniversalResourceID(new StringBuffer("size"));
            StringBuffer handle_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("handle"));
            StringBuffer className_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("className"));
            StringBuffer argument_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("argument"));
            StringBuffer sizes_RID1 = ProvUtils.getUniversalResourceID(new StringBuffer("sizes"));

            store.addRelationalProv(sizes_RID1, ProvOntology.getRDFTypeFullURIBuffer(),
                    ProvOntology.getCollectionExpandedClassFullURIBuffer());
            store.addRelationalProv(size_RID3, ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(), size_RID1);
            store.addRelationalProv(size_RID2, ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(), size_RID1);
            store.addRelationalProv(sizes_RID1, ProvOntology.getHadMemberExpandedPropertyFullURIBuffer(), size_RID2);
            store.addRelationalProv(sizes_RID1, ProvOntology.getHadMemberExpandedPropertyFullURIBuffer(), size_RID3);
            matcher.pushEntityID(handle_RID1);
            matcher.pushEntityID(className_RID1);
            matcher.pushEntityID(argument_RID1);
            matcher.pushEntityID(sizes_RID1);

            places_RID1 = ProvenanceRecorder.documentProcedure(store, null, new StringBuffer("Places"), new StringBuffer("label"), true, new String[]{"handle", "className", "argument", "size"}, new Object[]{1, "edu.uw.bothell.css.dsl.MASS.prov.QuickStart.Matrix", (Object) 0, new int[]{x, y}}, true);
        }
        // create all Places (having dimensions of x, y, and z)
        Places places = new Places(1,
                "edu.uw.bothell.css.dsl.MASS.prov.QuickStart.Matrix",
                (Object) 0, x, y);
        if (ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            // do end doc here (remember ignore granularity boolean)
            if (places_RID1 != null && places_RID1.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null, new StringBuffer("Places"), places_RID1, null, null, null, null, true, false, false, true);
            }
        }
        StringBuffer agents_RID1 = new StringBuffer();
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            String handle_RID2 = ProvUtils.getUniversalResourceID("handle"),
                    className_RID2 = ProvUtils.getUniversalResourceID("className"),
                    argument_RID2 = ProvUtils.getUniversalResourceID("argument"),
                    places_RID2 = ProvUtils.getUniversalResourceID("places"),
                    initPopulation_RID1 = ProvUtils.getUniversalResourceID("initPopulation");
            matcher.pushEntityID(handle_RID2);
            matcher.pushEntityID(className_RID2);
            //matcher.pushEntityID(argument_RID2);
            matcher.pushEntityID(places_RID2);
            matcher.pushEntityID(initPopulation_RID1);

            agents_RID1 = ProvenanceRecorder.documentProcedure(store, null, new StringBuffer("Agents"), new StringBuffer("label"), true, new String[]{"handle", "className", "argument", "places", "initPopulation"}, new Object[]{1, "edu.uw.bothell.css.dsl.MASS.prov.QuickStart.Nomad", null, places, nAgents}, true);
        }
        // create Agents (number of Agents = x*y in this case), in Places
        Agents agents = new Agents(1,
                "edu.uw.bothell.css.dsl.MASS.prov.QuickStart.Nomad",
                null, places, nAgents);

        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            // do end doc here (remember ignore granularity boolean)
            if (agents_RID1 != null && agents_RID1.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null, new StringBuffer("Agents"), agents_RID1, null, null, null, null, true, false, false, true);
            }
        }

        StringBuffer placeCallAllObjs_RID1 = new StringBuffer();
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            String size_RID1 = ProvUtils.getUniversalResourceID("size");
            matcher.pushEntityID(size_RID1);

            placeCallAllObjs_RID1 = ProvenanceRecorder.documentProcedure(store, null, new StringBuffer("Object[]"), new StringBuffer("label"), true, new String[]{"size"}, new Object[]{x * y * z}, true);
        }
        // as a test, instruct all places to return the hostnames of the
        // machines where they are located
        Object[] placeCallAllObjs = new Object[x * y * z];

        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            // do end doc here (remember ignore granularity boolean)
            if (placeCallAllObjs_RID1 != null && placeCallAllObjs_RID1.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null, new StringBuffer("Object[]"), placeCallAllObjs_RID1, null, null, null, null, true, false, false, true);
            }
        }

        StringBuffer calledPlacesResults_RID1 = new StringBuffer();
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            String placesArray_RID1 = ProvUtils.getUniversalResourceID("placesObjectArray");
            matcher.pushEntityID(placesArray_RID1);
            calledPlacesResults_RID1 = ProvenanceRecorder.documentProcedure(store, null, new StringBuffer("Object[]"), new StringBuffer("label"), true, new String[]{"placesObjectArray"}, new Object[]{Matrix.GET_HOSTNAME}, true);
        }

        Object[] calledPlacesResults = (Object[]) places.callAll(
                Matrix.GET_HOSTNAME, placeCallAllObjs);
        ProvenanceRecorder.documentFieldAccess(ProvUtils.getStoreOfCurrentThread(), new StringBuffer("GET_HOSTNAME"), Matrix.GET_HOSTNAME_RID, MASSProv.getDriverRID(), Matrix.GET_HOSTNAME, true);

        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            // do end doc here (remember ignore granularity boolean)
            if (calledPlacesResults_RID1 != null && calledPlacesResults_RID1.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null, new StringBuffer("Object[]"), calledPlacesResults_RID1, null, null, null, null, true, false, false, true);
            }
        }

        StringBuffer calledPlacesResults_RID2 = new StringBuffer();
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            String placesArray_RID2 = ProvUtils.getUniversalResourceID("placesObjectArray");
            matcher.pushEntityID(placesArray_RID2);
            calledPlacesResults_RID2 = ProvenanceRecorder.documentProcedure(store, null, new StringBuffer("Object[]"), new StringBuffer("label"), true, new String[]{"placesObjectArray"}, new Object[]{Matrix.GET_HOSTNAME}, true);
        }
        // see if we can duplicate the provenance recording from the last step
        calledPlacesResults = (Object[]) places.callAll(
                Matrix.GET_HOSTNAME, placeCallAllObjs);
        ProvenanceRecorder.documentFieldAccess(ProvUtils.getStoreOfCurrentThread(), new StringBuffer("GET_HOSTNAME"), Matrix.GET_HOSTNAME_RID, MASSProv.getDriverRID(), Matrix.GET_HOSTNAME, true);

        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            // do end doc here (remember ignore granularity boolean)
            if (calledPlacesResults_RID2 != null && calledPlacesResults_RID2.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null, new StringBuffer("Object[]"), calledPlacesResults_RID2, null, null, null, null, true, false, false, true);
            }
        }

        StringBuffer agentsCallAllObjs_RID1 = new StringBuffer();
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            String size_RID2 = ProvUtils.getUniversalResourceID("size");
            matcher.pushEntityID(size_RID2);

            agentsCallAllObjs_RID1 = ProvenanceRecorder.documentProcedure(store, null, new StringBuffer("Object[]"), new StringBuffer("label"), true, new String[]{"size"}, new Object[]{x * y}, true);
        }

        Object[] agentsCallAllObjs = new Object[x * y];

        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            // do end doc here (remember ignore granularity boolean)
            if (agentsCallAllObjs_RID1 != null && agentsCallAllObjs_RID1.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null, new StringBuffer("Object[]"), agentsCallAllObjs_RID1, null, null, null, null, true, false, false, true);
            }
        }

        StringBuffer calledAgentsResults_RID1 = new StringBuffer();
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            String agentsArray_RID1 = ProvUtils.getUniversalResourceID("agentsObjectArray");
            matcher.pushEntityID(agentsArray_RID1);
            calledAgentsResults_RID1 = ProvenanceRecorder.documentProcedure(store, null, new StringBuffer("Object[]"), new StringBuffer("label"), true, new String[]{"agentsObjectArray"}, new Object[]{Nomad.GET_HOSTNAME}, true);
        }
        Object[] calledAgentsResults = (Object[]) agents.callAll(
                Nomad.GET_HOSTNAME, agentsCallAllObjs);
        ProvenanceRecorder.documentFieldAccess(ProvUtils.getStoreOfCurrentThread(), new StringBuffer("GET_HOSTNAME"), Nomad.GET_HOSTNAME_RID, MASSProv.getDriverRID(), Nomad.GET_HOSTNAME, true);
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            // do end doc here (remember ignore granularity boolean)
            if (calledAgentsResults_RID1 != null && calledAgentsResults_RID1.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null, new StringBuffer("Object[]"), calledAgentsResults_RID1, null, null, null, null, true, false, false, true);
            }
        }

        StringBuffer forLoop_RID1 = new StringBuffer();
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            String i_RID1 = ProvUtils.getUniversalResourceID("i");
            String maxTime_RID1 = ProvUtils.getUniversalResourceID("maxTime");
            matcher.pushEntityID(i_RID1);
            matcher.pushEntityID(maxTime_RID1);
            forLoop_RID1 = ProvenanceRecorder.documentProcedure(store, null, new StringBuffer("ForLoop"), new StringBuffer("label"), true, new String[]{"i", "maxTime"}, new Object[]{0, maxTime}, true);
        }
        // move all Agents four times to cover all dimensions in Places
        for (int i = 0; i < maxTime; i++) {
            // tell Agents to move
            agents.callAll(Nomad.RANDOM_MIGRATE);
            ProvenanceRecorder.documentFieldAccess(ProvUtils.getStoreOfCurrentThread(), new StringBuffer("RANDOM_MIGRATE"), Nomad.RANDOM_MIGRATE_RID, MASSProv.getDriverRID(), Nomad.RANDOM_MIGRATE, true);

            if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                    && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
                store.addRelationalProv(forLoop_RID1, ProvOntology.getUsedStartingPointPropertyFullURIBuffer(), Nomad.RANDOM_MIGRATE_RID);
            }

            // sync all Agent status
            agents.manageAll();
            if (i >= 8 && i <= 9) {
                ProvUtils.startSlice();
            }
            // find out where they live now
            calledAgentsResults = (Object[]) agents.callAll(Nomad.GET_HOSTNAME,
                    agentsCallAllObjs);
            ProvenanceRecorder.documentFieldAccess(ProvUtils.getStoreOfCurrentThread(), new StringBuffer("GET_HOSTNAME"), Nomad.GET_HOSTNAME_RID, MASSProv.getDriverRID(), Nomad.GET_HOSTNAME, true);
            if (i >= 8 && i <= 9) {
                ProvUtils.endSlice();
            }
            if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                    && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
                store.addRelationalProv(forLoop_RID1, ProvOntology.getUsedStartingPointPropertyFullURIBuffer(), Nomad.GET_HOSTNAME_RID);
            }
        }

        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            // do end doc here (remember ignore granularity boolean)
            if (forLoop_RID1 != null && forLoop_RID1.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null, new StringBuffer("ForLoop"), forLoop_RID1, null, null, null, null, true, false, false, true);
            }
        }
        //////////////// GET NETWORK LOAD BALANCE INFO /////////////////////////
        //MASS.measureHostDelay();
        ////////////////////////////////////////////////////////////////////////
        StringBuffer calledAgentsResults_RID2 = new StringBuffer();
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            String agentsArray_RID2 = ProvUtils.getUniversalResourceID("agentsObjectArray");
            matcher.pushEntityID(agentsArray_RID2);
            calledAgentsResults_RID2 = ProvenanceRecorder.documentProcedure(store, null,
                    new StringBuffer("Object[]"), new StringBuffer("label"), true, new String[]{"agentsObjectArray"}, new Object[]{Nomad.GET_HOSTNAME}, true);
        }
        // find out where all of the Agents wound up when all movements complete
        calledAgentsResults = (Object[]) agents.callAll(Nomad.GET_HOSTNAME,
                agentsCallAllObjs);
        ProvenanceRecorder.documentFieldAccess(ProvUtils.getStoreOfCurrentThread(), new StringBuffer("GET_HOSTNAME"), Nomad.GET_HOSTNAME_RID, MASSProv.getDriverRID(), Nomad.GET_HOSTNAME, true);
        if (MASSProv.provOn && ProvenanceRecorder.granularityLevel() >= Granularity.SIMULATION.getValue()
                && ProvenanceRecorder.granularityLevel() < Granularity.PROCEDURE.getValue()) {
            // do end doc here (remember ignore granularity boolean)
            if (calledAgentsResults_RID2 != null && calledAgentsResults_RID2.length() > 0) {
                ProvenanceRecorder.endProcedureDocumentation(store, null, new StringBuffer("Object[]"), calledAgentsResults_RID2, null, null, null, null, true, false, false, true);
            }
        }

        // orderly shutdown
        MASS.finish();

    }
}
