package edu.uw.bothell.css.dsl.MASS.prov.QuickStart.provcollection;
// PERFORMANCE DOCUMENTED

import edu.uw.bothell.css.dsl.MASS.prov.ProvOntology;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.util.HashMap;

/**
 * Used to collect data provenance associated with the TestDriver class
 *
 * REMINDER: Implement Thread Stack for activity IDs!!!!!
 *
 * @author Delmar B. Davis
 */
public class TestDriver {

    public void main_retrospective(ProvenanceStore prov,
            HashMap<String, Object> entities, Object[] args) {
        StopWatch.start(true);
//
//        /* User */
//        prov.addRelationalProv(user_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getAgentStartingPointClassFullURI());
//        prov.addRelationalProv(user_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getPersonExpandedClassFullURI());
//        /* end User */
//
// /* TestDriver */
//        prov.addRelationalProv(testDriver_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getSoftwareAgentExpandedClassFullURI());
//        prov.addRelationalProv(testDriver_ID, ProvOntology.getRDFSLabelFullURI(),
//                "MASS_APPLICATION");
//        prov.addRelationalProv(testDriver_ID,
//                ProvOntology.getActedOnBehalfOfStartingPointPropertyFullURI(),
//                user_ID);
//        prov.addRelationalProv(testDriver_default_constructor_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(testDriver_default_constructor_ID,
//                ProvOntology.getWasStartedByExpandedPropertyFullURI(),
//                testDriver_ID);
//        /* end TestDriver */
//
// /* TestDriver.main */
//        prov.addRelationalProv(testDriverMain_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(testDriverMain_ID, ProvOntology.getRDFSLabelFullURI(),
//                "MAIN_METHOD");
//        prov.addRelationalProv(testDriverMain_ID,
//                ProvOntology.getWasStartedByExpandedPropertyFullURI(),
//                testDriver_ID);
//        /* end TestDriver.main */
//
// /* TestDriver.main_args */
//        if (args.length == 0) {
//            prov.addRelationalProv(testDriverMain_args_ID,
//                    ProvOntology.getRDFTypeFullURI(),
//                    ProvOntology.getEmptyCollectionExpandedClassFullURI());
//            prov.addRelationalProv(testDriverMain_args_ID,
//                    ProvOntology.getRDFSLabelFullURI(),
//                    "COMMAND_LINE_ARGUMENTS");
//            prov.addRelationalProv(testDriver_ID,
//                    ProvOntology.getUsedStartingPointPropertyFullURI(),
//                    testDriverMain_args_ID);
//        } else {
//            prov.addRelationalProv(testDriverMain_args_ID,
//                    ProvOntology.getRDFTypeFullURI(),
//                    ProvOntology.getCollectionExpandedClassFullURI());
//            prov.addRelationalProv(testDriverMain_args_ID,
//                    ProvOntology.getRDFSLabelFullURI(),
//                    "COMMAND_LINE_ARGUMENTS");
//            prov.addRelationalProv(testDriver_ID,
//                    ProvOntology.getUsedStartingPointPropertyFullURI(),
//                    testDriverMain_args_ID);
//            String argID;
//            for (int i = 0, im = args.length; i < im; i++) {
//                argID = new StringBuilder("testDriverMain_arg_").append(i).
//                        append("#id=").append(java.util.UUID.randomUUID().
//                        toString()).toString();
//                prov.addRelationalProv(argID, ProvOntology.getRDFTypeFullURI(),
//                        ProvOntology.getEntityStartingPointClassFullURI());
//                prov.addRelationalProv(argID,
//                        ProvOntology.getRDFSLabelFullURI(),
//                        new StringBuilder("TYPE=").
//                        append(argID.getClass().toString()).toString());
//                prov.addRelationalProv(argID,
//                        ProvOntology.getRDFSLabelFullURI(),
//                        new StringBuilder("INDEX_").
//                        append(String.valueOf(i)).toString());
//                prov.addRelationalProv(argID,
//                        ProvOntology.getValueExpandedPropertyFullURI(),
//                        (String) args[i]);
//                prov.addRelationalProv(testDriverMain_args_ID,
//                        ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                        argID);
//            }
//        }
//        /* end TestDriver.main_args */
//
// /* NODE_FILE */
//        prov.addRelationalProv(NODE_FILE_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(NODE_FILE_ID, ProvOntology.getRDFSLabelFullURI(),
//                "FILE_PATH");
//        prov.addRelationalProv(NODE_FILE_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                (String) entities.get("NODE_FILE"));
//        prov.addRelationalProv(testDriver_default_constructor_ID,
//                ProvOntology.getGeneratedExpandedPropertyFullURI(),
//                NODE_FILE_ID);
//        prov.addRelationalProv(NODE_FILE_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").
//                append(entities.get("NODE_FILE").getClass().toString())
//                .toString());
//        /* END NODE_FILE */
//
// /* startTime */
//        prov.addRelationalProv(startTime_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        long startTime = (long) entities.get("startTime");
//        prov.addRelationalProv(startTime_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf(startTime));
//        prov.addRelationalProv(startTime_ID,
//                ProvOntology.getWasGeneratedByStartingPointPropertyFullURI(),
//                testDriverMain_ID);
//        prov.addRelationalProv(testDriverMain_ID,
//                ProvOntology.getGeneratedExpandedPropertyFullURI(),
//                startTime_ID);
//        prov.addRelationalProv(startTime_ID,
//                ProvOntology.getGeneratedAtTimeExpandedPropertyFullURI(),
//                String.valueOf(startTime));
//        /* end startTime */
//
// /* MASS */
//        prov.addRelationalProv(MASS_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(MASS_ID, ProvOntology.getRDFSLabelFullURI(),
//                "SOFTWARE_DEPENDENCY");
//        /* end MASS */
//
// /* MASS.setNodeFilePath */
//        prov.addRelationalProv(MASSsetNodeFilePath_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(MASSsetNodeFilePath_ID,
//                ProvOntology.getRDFSLabelFullURI(), "SETTER");
//        prov.addRelationalProv(MASSsetNodeFilePath_ID,
//                ProvOntology.getWasStartedByExpandedPropertyFullURI(),
//                testDriver_ID);
//        prov.addRelationalProv(MASSsetNodeFilePath_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(),
//                NODE_FILE_ID);
//        /* end MASSsetNodeFilePath */
//
// /* collector */
//        prov.addRelationalProv(collector_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(collector_ID, ProvOntology.getRDFSLabelFullURI(),
//                "COLLECTOR_TYPE");
//        prov.addRelationalProv(collector_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").
//                append(entities.get("collector").getClass().toString()).
//                toString());
//        prov.addRelationalProv(collector_ID,
//                ProvOntology.getWasGeneratedByStartingPointPropertyFullURI(),
//                testDriverMain_ID);
//        prov.addRelationalProv(testDriverMain_ID,
//                ProvOntology.getGeneratedExpandedPropertyFullURI(),
//                collector_ID);
//        /* end collector */
//
// /* main */
//        prov.addRelationalProv(main_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(main_ID, ProvOntology.getRDFSLabelFullURI(),
//                "METHOD_NAME");
//        prov.addRelationalProv(main_ID,
//                ProvOntology.getWasGeneratedByStartingPointPropertyFullURI(),
//                testDriverMain_ID);
//        prov.addRelationalProv(testDriverMain_ID,
//                ProvOntology.getGeneratedExpandedPropertyFullURI(),
//                main_ID);
//        prov.addRelationalProv(main_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                entities.get("main").toString());
//        /* end main */
//
// /* MASSProv.init arguments */
//        prov.addRelationalProv(MASSProvinit_args_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getCollectionExpandedClassFullURI());
//        /* collector argument */
//        prov.addRelationalProv(MASSProvinit_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                collector_ID);
//        prov.addRelationalProv(collector_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").append(MASSProvinit_args_ID).
//                append("=0").toString());
//        /* end collector argument */
// /* main argument */
//        prov.addRelationalProv(MASSProvinit_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                main_ID);
//        prov.addRelationalProv(main_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").append(MASSProvinit_args_ID).
//                append("=1").toString());
//        /* end main argument */
// /* TestDriver.main_args argument */
//        prov.addRelationalProv(MASSProvinit_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                main_ID);
//        prov.addRelationalProv(main_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").append(MASSProvinit_args_ID).
//                append("=2").toString());
//        /* end TestDriver.main_args argument */
// /* end MASSProv.init arguments */
//
// /* MASSProv */
//        prov.addRelationalProv(MASSProv_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(MASSProv_ID, ProvOntology.getRDFSLabelFullURI(),
//                "SOFTWARE_DEPENDENCY");
//        /* end MASSProv */
//
// /* MASSProv.init */
//        // testDriverMain_args_ID // MASSProv_ID // main_ID // collector_ID
//        prov.addRelationalProv(MASSProvinit_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(MASSProvinit_ID,
//                ProvOntology.getWasStartedByExpandedPropertyFullURI(),
//                testDriverMain_ID);
//        prov.addRelationalProv(MASSProvinit_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(),
//                collector_ID);
//        prov.addRelationalProv(MASSProvinit_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), main_ID);
//        prov.addRelationalProv(MASSProvinit_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(),
//                testDriverMain_args_ID);
//        /* end MASSProv.init */
//
// /* MASS.init */
//        prov.addRelationalProv(MASSinit_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(MASSinit_ID,
//                ProvOntology.getWasStartedByExpandedPropertyFullURI(),
//                testDriverMain_ID);
//        prov.addRelationalProv(MASSinit_ID, ProvOntology.getRDFSLabelFullURI(),
//                "INITIALIZES_MASS");
//        /* end MASS.init */
//
// /* x */
//        prov.addRelationalProv(x_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(x_ID, ProvOntology.getRDFSLabelFullURI(),
//                "PLACES_FIRST_DIMENSION");
//        prov.addRelationalProv(x_ID,
//                ProvOntology.getWasGeneratedByStartingPointPropertyFullURI(),
//                testDriverMain_ID);
//        prov.addRelationalProv(testDriverMain_ID,
//                ProvOntology.getGeneratedExpandedPropertyFullURI(),
//                x_ID);
//        /* end x */
//
// /* y */
//        prov.addRelationalProv(y_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(y_ID, ProvOntology.getRDFSLabelFullURI(),
//                "PLACES_SECOND_DIMENSION");
//        prov.addRelationalProv(y_ID,
//                ProvOntology.getWasGeneratedByStartingPointPropertyFullURI(),
//                testDriverMain_ID);
//        prov.addRelationalProv(testDriverMain_ID,
//                ProvOntology.getGeneratedExpandedPropertyFullURI(),
//                y_ID);
//        /* end y */
//
// /* z */
//        prov.addRelationalProv(z_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(z_ID, ProvOntology.getRDFSLabelFullURI(),
//                "PLACES_THIRD_DIMENSION");
//        prov.addRelationalProv(z_ID,
//                ProvOntology.getWasGeneratedByStartingPointPropertyFullURI(),
//                testDriverMain_ID);
//        prov.addRelationalProv(testDriverMain_ID,
//                ProvOntology.getGeneratedExpandedPropertyFullURI(),
//                z_ID);
//        /* end z */
//
// /* places */
//        prov.addRelationalProv(places_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(places_ID, ProvOntology.getRDFSLabelFullURI(),
//                "MATRIX_PLACES");
//        /* end places */
//
// /* places constructor args */
//        prov.addRelationalProv(places_constructor_args_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getCollectionExpandedClassFullURI());
//        /* handle argument */
//        prov.addRelationalProv(places_constructor_args_handle_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(places_constructor_args_handle_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").
//                append(((Object[]) entities.get("places_constructor_args"))[0].
//                        getClass().toString()).toString());
//        prov.addRelationalProv(places_constructor_args_handle_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").
//                append(places_constructor_args_ID).append("=0").toString());
//        prov.addRelationalProv(places_constructor_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                places_constructor_args_handle_ID);
//        prov.addRelationalProv(places_constructor_args_handle_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf((((Object[]) entities.
//                        get("places_constructor_args"))[0].
//                        getClass().cast(
//                                ((Object[]) entities.get(
//                                        "places_constructor_args"))[0]))));
//        /* end handle argument */
// /* className argument */
//        prov.addRelationalProv(places_constructor_args_className_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(places_constructor_args_className_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").
//                append(((Object[]) entities.get("places_constructor_args"))[1].
//                        getClass().toString()).toString());
//        prov.addRelationalProv(places_constructor_args_className_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").
//                append(places_constructor_args_ID).append("=1").toString());
//        prov.addRelationalProv(places_constructor_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                places_constructor_args_handle_ID);
//        prov.addRelationalProv(places_constructor_args_className_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf((((Object[]) entities.
//                        get("places_constructor_args"))[1].
//                        getClass().cast(
//                                ((Object[]) entities.get(
//                                        "places_constructor_args"))[1]))));
//        /* end className argument */
// /* argument argument */
//        prov.addRelationalProv(places_constructor_args_argument_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(places_constructor_args_argument_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").
//                append(((Object[]) entities.get("places_constructor_args"))[2].
//                        getClass().toString()).toString());
//        prov.addRelationalProv(places_constructor_args_argument_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").
//                append(places_constructor_args_ID).append("=2").toString());
//        prov.addRelationalProv(places_constructor_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                places_constructor_args_handle_ID);
//        prov.addRelationalProv(places_constructor_args_argument_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf((((Object[]) entities.
//                        get("places_constructor_args"))[2].
//                        getClass().cast(
//                                ((Object[]) entities.get(
//                                        "places_constructor_args"))[2]))));
//        /* end argument argument */
// /* size argument */
//        prov.addRelationalProv(places_constructor_args_size_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(places_constructor_args_size_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").
//                append(((Object[]) entities.get("places_constructor_args"))[3].
//                        getClass().toString()).toString());
//        prov.addRelationalProv(places_constructor_args_size_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").
//                append(places_constructor_args_ID).append("=3").toString());
//        prov.addRelationalProv(places_constructor_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                places_constructor_args_size_ID);
//        prov.addRelationalProv(places_constructor_args_size_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf((((Object[]) entities.
//                        get("places_constructor_args"))[3].
//                        getClass().cast(
//                                ((Object[]) entities.get(
//                                        "places_constructor_args"))[3]))));
//        /* end size */
// /* end places constructor args */
//
// /* placeCallAllObjs */
//        prov.addRelationalProv(placeCallAllObjs_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getCollectionExpandedClassFullURI());
//        prov.addRelationalProv(placeCallAllObjs_ID,
//                ProvOntology.getRDFSLabelFullURI(), "RESULT_SET");
//        prov.addRelationalProv(placeCallAllObjs_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").
//                append(entities.get("placeCallAllObjs").getClass().toString())
//                .toString());
//        /* end placeCallAllObjs */
//
// /* placeCallAllObjs length calculation */
//        prov.addRelationalProv(placeCallObjs_constructor_args_length_calculation_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(placeCallObjs_constructor_args_length_calculation_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), x_ID);
//        prov.addRelationalProv(placeCallObjs_constructor_args_length_calculation_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), y_ID);
//        prov.addRelationalProv(placeCallObjs_constructor_args_length_calculation_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), z_ID);
//        /* end placeCallAllObjs length calculation */
//
// /* placeCallAllObjs constructor */
//        prov.addRelationalProv(placeCallAllObjs_constructor_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(placeCallAllObjs_constructor_ID,
//                ProvOntology.getRDFSLabelFullURI(), "CONSTRUCTION");
//        /* end placeCallAllObjs constructor */
//
// /* placeCallAllObjs constructor arguments */
//        prov.addRelationalProv(placeCallAllObjs_constructor_args_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(placeCallAllObjs_constructor_args_ID,
//                ProvOntology.getRDFSLabelFullURI(), "CONSTRUCTOR_ARGUMENTS");
//        /* length */
//        prov.addRelationalProv(placeCallAllObjs_constructor_args_length_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(placeCallAllObjs_constructor_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(),
//                placeCallAllObjs_constructor_args_length_ID);
//        /* length calculation */
//        prov.addRelationalProv(
//                placeCallObjs_constructor_args_length_calculation_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(
//                placeCallObjs_constructor_args_length_calculation_ID,
//                ProvOntology.getGeneratedExpandedPropertyFullURI(),
//                placeCallAllObjs_constructor_args_length_ID);
//        prov.addRelationalProv(
//                placeCallObjs_constructor_args_length_calculation_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), x_ID);
//        prov.addRelationalProv(
//                placeCallObjs_constructor_args_length_calculation_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), y_ID);
//        prov.addRelationalProv(
//                placeCallObjs_constructor_args_length_calculation_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), z_ID);
//        /* end length calculation */
//        prov.addRelationalProv(placeCallAllObjs_constructor_args_length_ID,
//                ProvOntology.getRDFSLabelFullURI(), new StringBuilder("TYPE=").
//                append(((Object) 1).getClass().toString()).toString());
//        prov.addRelationalProv(placeCallAllObjs_constructor_args_length_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").
//                append(placeCallAllObjs_constructor_args_ID).
//                append("=0").toString());
//        prov.addRelationalProv(placeCallAllObjs_constructor_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                placeCallAllObjs_constructor_args_length_ID);
//        prov.addRelationalProv(placeCallAllObjs_constructor_args_length_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf((int) entities.get("x")
//                        * (int) entities.get("y")
//                        * (int) entities.get("z")));
//        /* end length */
// /* end placeCallAllObjs constructor arguments */
//
// /* calledPlacesResults */
//        prov.addRelationalProv(calledPlacesResults_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getCollectionExpandedClassFullURI());
//        prov.addRelationalProv(placesCallAll_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(places_callAll_args_matrix_GET_HOSTNAME_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(placesCallAll_args_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getCollectionExpandedClassFullURI());
//        prov.addRelationalProv(placesCallAll_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                places_callAll_args_matrix_GET_HOSTNAME_ID);
//        prov.addRelationalProv(placesCallAll_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                placeCallAllObjs_ID);
//        prov.addRelationalProv(calledPlacesResults_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("RESULT_SET").toString());
//        prov.addRelationalProv(placesCallAll_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("CALL_ALL_PLACES_GET_HOSTNAME").toString());
//        prov.addRelationalProv(places_callAll_args_matrix_GET_HOSTNAME_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("SPECIFIES_FIND_HOSTNAME_FUNCTION").
//                toString());
//        prov.addRelationalProv(placesCallAll_args_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("ARGUMENTS").toString());
//        prov.addRelationalProv(places_callAll_args_matrix_GET_HOSTNAME_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").append(((Object[]) entities.get(
//                        "calledAgentsResults_args"))[0].getClass().
//                        toString()).toString());
//        prov.addRelationalProv(calledPlacesResults_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").append((entities.get(
//                        "calledPlacesResults")).getClass().toString()).
//                toString());
//        prov.addRelationalProv(places_callAll_args_matrix_GET_HOSTNAME_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").append(placesCallAll_args_ID).
//                append("=0").toString());
//        prov.addRelationalProv(placeCallAllObjs_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").append(placesCallAll_args_ID).
//                append("=1").toString());
//        /* end calledPlacesResults */
//
// /* agents */
//        prov.addRelationalProv(agents_ID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(agents_ID, ProvOntology.getRDFSLabelFullURI(),
//                "NOMAD_AGENTS");
//        /* end agents */
//
// /* agents constructor */
//        prov.addRelationalProv(agents_constructor_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(agents_constructor_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                "CONSTRUCTION");
//        /* end agents constructor */
//
// /* agents constructor args */
//        prov.addRelationalProv(agents_constructor_args_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getCollectionExpandedClassFullURI());
//        prov.addRelationalProv(agents_constructor_args_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                "CONSTRUCTOR_ARGUMENTS");
//        /* handle */
//        prov.addRelationalProv(agents_constructor_args_handle_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(agents_constructor_args_handle_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").
//                append(agents_constructor_args_ID).append("=0").
//                toString());
//        prov.addRelationalProv(agents_constructor_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                agents_constructor_args_handle_ID);
//        prov.addRelationalProv(agents_constructor_args_handle_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf(((Object[]) entities.get(
//                        "agents_constructor_args"))[0]));
//        prov.addRelationalProv(agents_constructor_args_handle_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").append(
//                        ((Object[]) entities.get("agents_constructor_args"))[0].
//                        getClass().toString()).toString());
//        /* end handle */
// /* className */
//        prov.addRelationalProv(agents_constructor_args_className_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(agents_constructor_args_className_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").
//                append(agents_constructor_args_ID).append("=1").
//                toString());
//        prov.addRelationalProv(agents_constructor_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                agents_constructor_args_className_ID);
//        prov.addRelationalProv(agents_constructor_args_className_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf(((Object[]) entities.get(
//                        "agents_constructor_args"))[1]));
//        prov.addRelationalProv(agents_constructor_args_className_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").append(
//                        ((Object[]) entities.get("agents_constructor_args"))[1].
//                        getClass().toString()).toString());
//        /* end className */
// /* argument */
//        prov.addRelationalProv(agents_constructor_args_argument_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(agents_constructor_args_argument_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").
//                append(agents_constructor_args_ID).append("=2").
//                toString());
//        prov.addRelationalProv(agents_constructor_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                agents_constructor_args_argument_ID);
//        prov.addRelationalProv(agents_constructor_args_argument_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf(((Object[]) entities.get(
//                        "agents_constructor_args"))[2]));
//        // !!!TODO: DEAL WITH NULL OBJECTS, HOPEFULLY SOMETHING BETTER THAN THIS
//        if (null != ((Object[]) entities.get("agents_constructor_args"))[2]) {
//            prov.addRelationalProv(agents_constructor_args_argument_ID,
//                    ProvOntology.getRDFSLabelFullURI(),
//                    new StringBuilder("TYPE=").append(
//                            ((Object[]) entities.get("agents_constructor_args"))[2].
//                            getClass().toString()).toString());
//        } else {
//            prov.addRelationalProv(agents_constructor_args_argument_ID,
//                    ProvOntology.getRDFSLabelFullURI(),
//                    new StringBuilder("TYPE=").append(Object.class.toString()).
//                    toString());
//        }
//        /* end argument */
// /* places */
//        prov.addRelationalProv(places_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").
//                append(agents_constructor_args_ID).append("=3").
//                toString());
//        prov.addRelationalProv(agents_constructor_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                places_ID);
//        /* end places */
// /* initPopulation */
//        prov.addRelationalProv(agents_constructor_args_argument_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        /* initPopulation calculation */
//        prov.addRelationalProv(
//                agents_constructor_args_initPopulationCalculation_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(
//                agents_constructor_args_initPopulationCalculation_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), x_ID);
//        prov.addRelationalProv(
//                agents_constructor_args_initPopulationCalculation_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), y_ID);
//        prov.addRelationalProv(
//                agents_constructor_args_initPopulationCalculation_ID,
//                ProvOntology.getGeneratedExpandedPropertyFullURI(),
//                agents_constructor_args_initPopulation_ID);
//        /* end initPopulation calculation */
//        prov.addRelationalProv(agents_constructor_args_initPopulation_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").
//                append(agents_constructor_args_ID).append("=4").
//                toString());
//        prov.addRelationalProv(agents_constructor_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                agents_constructor_args_initPopulation_ID);
//        prov.addRelationalProv(agents_constructor_args_initPopulation_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf(((Object[]) entities.get(
//                        "agents_constructor_args"))[4]));
//        prov.addRelationalProv(agents_constructor_args_initPopulation_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").append(
//                        ((Object[]) entities.get("agents_constructor_args"))[4].
//                        getClass().toString()).toString());
//        /* end initPopulation */
// /* end agents constructor args */
//
// /* agentsCallAllObjs */
//        prov.addRelationalProv(agentsCallAllObjs_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getCollectionExpandedClassFullURI());
//        prov.addRelationalProv(agentsCallAllObjs_ID,
//                ProvOntology.getRDFSLabelFullURI(), "CALL_ALL_RETURN_SPACE");
//        prov.addRelationalProv(agentsCallAllObjs_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("TYPE=").
//                append(entities.get("agentsCallAllObjs").getClass().toString())
//                .toString());
//        /* end agentsCallAllObjs */
//
// /* agentsCallAllObjs length calculation */
//        prov.addRelationalProv(agentsCallAllObjs_constructor_args_lengthCalculation_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(agentsCallAllObjs_constructor_args_lengthCalculation_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), x_ID);
//        prov.addRelationalProv(agentsCallAllObjs_constructor_args_lengthCalculation_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), y_ID);
//        /* end agentsCallAllObjs length calculation */
//
// /* agentsCallAllObjs constructor */
//        prov.addRelationalProv(agentsCallAllObjs_constructor_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(agentsCallAllObjs_constructor_ID,
//                ProvOntology.getRDFSLabelFullURI(), "CONSTRUCTION");
//        /* end agentsCallAllObjs constructor */
//
// /* agentsCallAllObjs constructor arguments */
//        prov.addRelationalProv(agentsCallAllObjs_constructor_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(agentsCallAllObjs_constructor_ID,
//                ProvOntology.getRDFSLabelFullURI(), "CONSTRUCTOR_ARGUMENTS");
//        /* length */
//        prov.addRelationalProv(agentsCallAllObjs_constructor_args_length_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(agentsCallAllObjs_constructor_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(),
//                agentsCallAllObjs_constructor_args_length_ID);
//        /* length calculation */
//        prov.addRelationalProv(agentsCallAllObjs_constructor_args_lengthCalculation_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(agentsCallAllObjs_constructor_args_lengthCalculation_ID,
//                ProvOntology.getGeneratedExpandedPropertyFullURI(),
//                agentsCallAllObjs_constructor_args_length_ID);
//        prov.addRelationalProv(agentsCallAllObjs_constructor_args_lengthCalculation_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), x_ID);
//        prov.addRelationalProv(agentsCallAllObjs_constructor_args_lengthCalculation_ID,
//                ProvOntology.getUsedStartingPointPropertyFullURI(), y_ID);
//        /* end length calculation */
//        prov.addRelationalProv(agentsCallAllObjs_constructor_args_length_ID,
//                ProvOntology.getRDFSLabelFullURI(), new StringBuilder("TYPE=").
//                append(((Object) 1).getClass().toString()).toString());
//        prov.addRelationalProv(agentsCallAllObjs_constructor_args_length_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").
//                append(agentsCallAllObjs_constructor_args_ID).
//                append("=0").toString());
//        prov.addRelationalProv(agentsCallAllObjs_constructor_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                agentsCallAllObjs_constructor_args_length_ID);
//        prov.addRelationalProv(agentsCallAllObjs_constructor_args_length_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf((int) entities.get("x")
//                        * (int) entities.get("y")));
//        /* end length */
// /* end placeCallAllObjs constructor arguments */
//
// /* calledAgentsResults */
//        prov.addRelationalProv(calledAgentsResults_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(calledAgentsResults_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                "RESULT_SET");
//        /* end calledAgentsResults */
// /* agents callAll */
//        prov.addRelationalProv(agents_callAll_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getActivityStartingPointClassFullURI());
//        prov.addRelationalProv(agents_callAll_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                "CALL_ALL_GET_HOSTNAME");
//        /* end agents callAll */
// /* agents callAll args */
//        prov.addRelationalProv(agents_callAll_args_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getCollectionExpandedClassFullURI());
//        prov.addRelationalProv(agents_callAll_args_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                "ARGUMENTS");
//        /* agents callAll functionId */
//        prov.addRelationalProv(agents_callAll_args_functionId_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(agents_callAll_args_functionId_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").append(agents_callAll_args_ID).
//                append("=0").toString());
//        if (((Object[]) entities.get("calledAgentsResults_args"))[0] != null) {
//            prov.addRelationalProv(agents_callAll_args_functionId_ID,
//                    ProvOntology.getRDFSLabelFullURI(),
//                    new StringBuilder("TYPE=").append(((Object[]) entities.get(
//                            "calledAgentsResults_args"))[0].getClass().
//                            toString()).toString());
//        } else {
//            prov.addRelationalProv(agents_callAll_args_functionId_ID,
//                    ProvOntology.getRDFSLabelFullURI(),
//                    new StringBuilder("TYPE=").append(Object.class.toString()).
//                    toString());
//        }
//        prov.addRelationalProv(agents_callAll_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                agents_callAll_args_functionId_ID);
//        prov.addRelationalProv(agents_callAll_args_functionId_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf(((Object[]) entities.get(
//                        "calledAgentsResults_args"))[0]));
//        /* end agents callAll functionId */
// /* agents callAll argument */
//        prov.addRelationalProv(agents_callAll_args_argument_ID,
//                ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(agents_callAll_args_argument_ID,
//                ProvOntology.getRDFSLabelFullURI(),
//                new StringBuilder("INDEX_IN_").append(agents_callAll_args_ID).
//                append("=0").toString());
//        if (((Object[]) entities.get("calledAgentsResults_args"))[1] != null) {
//            prov.addRelationalProv(agents_callAll_args_argument_ID,
//                    ProvOntology.getRDFSLabelFullURI(),
//                    new StringBuilder("TYPE=").append(((Object[]) entities.get(
//                            "calledAgentsResults_args"))[1].getClass().
//                            toString()).toString());
//        } else {
//            prov.addRelationalProv(agents_callAll_args_argument_ID,
//                    ProvOntology.getRDFSLabelFullURI(),
//                    new StringBuilder("TYPE=").append(Object.class.toString()).
//                    toString());
//        }
//        prov.addRelationalProv(agents_callAll_args_ID,
//                ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                agents_callAll_args_argument_ID);
//        prov.addRelationalProv(agents_callAll_args_argument_ID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                String.valueOf(((Object[]) entities.get(
//                        "calledAgentsResults_args"))[1]));
//        /* end agents callAll argument */
// /* end agents callAll args */
//
// /*for (int i = 0; i < z; i++) {
//
//         // tell Agents to move
//         agents.callAll(Nomad.MIGRATE);
//
//         // sync all Agent status
//         agents.manageAll();
//
//         // find out where they live now
//         calledAgentsResults = (Object[]) agents.callAll(Nomad.GET_HOSTNAME,
//         agentsCallAllObjs);
//
//         }*/
//        for (int i = 0, im = (int) entities.get("z"); i < im; i++) {
//            /*
//             agents.callAll(Nomad.MIGRATE);
//             */
// /* agents callAll i + 1 */
//            String agents_callAll_iPlus1 = ProvUtils.getUniversalResourceID(
//                    new StringBuilder("agents_callAll_").append(
//                            String.valueOf(i + 1)).toString());
//            /* end agents callAll i + 1 */
// /* agents callAll args i + 1 */
//            String agents_callAll_args_iPlus1
//                    = ProvUtils.getUniversalResourceID(
//                            new StringBuilder("agents_callAll_args_").append(
//                                    String.valueOf(i + 1)).toString());
//            /* end agents callAll args i + 1 */
// /* agents callAll args i + 1 functionID */
//            String agents_callAll_args_functionID_iPlus1
//                    = ProvUtils.getUniversalResourceID(
//                            new StringBuilder("agents_callAll_args_functionID").
//                            append(String.valueOf(i + 1)).toString());
//            /* end agents callAll args i + 1 functionID */
// /*
//             agents.callAll(Nomad.MIGRATE);
//             */
//
// /*
//             agents.manageAll();
//             */
// /* agents manageAll i */
//            String agents_manageAll_i
//                    = ProvUtils.getUniversalResourceID(
//                            new StringBuilder("agents_manageAll_").
//                            append(String.valueOf(i)).toString());
//            /* end agents manageAll i */
// /*
//             agents.manageAll();
//             */
// /*            
//             calledAgentsResults = (Object[]) agents.callAll(Nomad.GET_HOSTNAME,
//             agentsCallAllObjs);
//             */
// /* calledAgentsResults i + 1 */
//            String calledAgentsResults_iPlus1
//                    = ProvUtils.getUniversalResourceID(
//                            new StringBuilder("calledAgentsResults_").
//                            append(String.valueOf(i + 1)).toString());
//            /* end calledAgentsResults i + 1 */
// /* agents callAll i + 2 */
//            String agents_callAll_iPlus2
//                    = ProvUtils.getUniversalResourceID(
//                            new StringBuilder("agents_callAll_").
//                            append(String.valueOf(i + 2)).toString());
////            prov.addRelationalProv(agents_callAll_ID,
////                    ProvOntology.getRDFTypeFullURI(),
////                    ProvOntology.getActivityStartingPointClassFullURI());
////            prov.addRelationalProv(agents_callAll_ID,
////                    ProvOntology.getRDFSLabelFullURI(),
////                    "CALL_ALL_GET_HOSTNAME");
//            /* end agents callAll i + 2 */
// /* agents callAll args i + 2 */
//            String agents_callAll_args_iPlus2
//                    = ProvUtils.getUniversalResourceID(
//                            new StringBuilder("agents_callAll_args_").
//                            append(String.valueOf(i + 2)).toString());
////            prov.addRelationalProv(agents_callAll_args_ID,
////                    ProvOntology.getRDFTypeFullURI(),
////                    ProvOntology.getCollectionExpandedClassFullURI());
////            prov.addRelationalProv(agents_callAll_args_ID,
////                    ProvOntology.getRDFSLabelFullURI(),
////                    "ARGUMENTS");
//            /* agents callAll functionId i + 2 */
//            String agents_callAll_args_functionID_iPlus2
//                    = ProvUtils.getUniversalResourceID(new StringBuilder(
//                            "agents_callAll_args_functionID_").
//                            append(String.valueOf(i + 2)).toString());
////            prov.addRelationalProv(agents_callAll_functionId_ID,
////                    ProvOntology.getRDFTypeFullURI(),
////                    ProvOntology.getEntityStartingPointClassFullURI());
////            prov.addRelationalProv(agents_callAll_functionId_ID,
////                    ProvOntology.getRDFSLabelFullURI(),
////                    new StringBuilder("INDEX_IN_").append(agents_callAll_args_ID).
////                    append("=0").toString());
////            if (((Object[]) entities.get("calledAgentsResults_args"))[0] != null) {
////                prov.addRelationalProv(agents_callAll_functionId_ID,
////                        ProvOntology.getRDFSLabelFullURI(),
////                        new StringBuilder("TYPE=").append(((Object[]) entities.get(
////                                        "calledAgentsResults_args"))[0].getClass().
////                                toString()).toString());
////            } else {
////                prov.addRelationalProv(agents_callAll_functionId_ID,
////                        ProvOntology.getRDFSLabelFullURI(),
////                        new StringBuilder("TYPE=").append(Object.class.toString()).
////                        toString());
////            }
////            prov.addRelationalProv(agents_callAll_args_ID,
////                    ProvOntology.getHadMemberExpandedPropertyFullURI(),
////                    agents_callAll_functionId_ID);
////            prov.addRelationalProv(agents_callAll_functionId_ID,
////                    ProvOntology.getValueExpandedPropertyFullURI(),
////                    String.valueOf(((Object[]) entities.get(
////                                    "calledAgentsResults_args"))[0]));
//            /* end agents callAll functionId i + 2 */
// /* agents callAll argument i + 2 */
//            String agents_callAll_args_argument_iPlus2
//                    = ProvUtils.getUniversalResourceID(new StringBuilder(
//                            "agents_callAll_args_argument_").
//                            append(String.valueOf(i + 2)).toString());
////            prov.addRelationalProv(agents_callAll_argument_ID,
////                    ProvOntology.getRDFTypeFullURI(),
////                    ProvOntology.getEntityStartingPointClassFullURI());
////            prov.addRelationalProv(agents_callAll_argument_ID,
////                    ProvOntology.getRDFSLabelFullURI(),
////                    new StringBuilder("INDEX_IN_").append(agents_callAll_args_ID).
////                    append("=0").toString());
////            if (((Object[]) entities.get("calledAgentsResults_args"))[1] != null) {
////                prov.addRelationalProv(agents_callAll_argument_ID,
////                        ProvOntology.getRDFSLabelFullURI(),
////                        new StringBuilder("TYPE=").append(((Object[]) entities.get(
////                                        "calledAgentsResults_args"))[1].getClass().
////                                toString()).toString());
////            } else {
////                prov.addRelationalProv(agents_callAll_argument_ID,
////                        ProvOntology.getRDFSLabelFullURI(),
////                        new StringBuilder("TYPE=").append(Object.class.toString()).
////                        toString());
////            }
////            prov.addRelationalProv(agents_callAll_args_ID,
////                    ProvOntology.getHadMemberExpandedPropertyFullURI(),
////                    agents_callAll_argument_ID);
////            prov.addRelationalProv(agents_callAll_argument_ID,
////                    ProvOntology.getValueExpandedPropertyFullURI(),
////                    String.valueOf(((Object[]) entities.get(
////                                    "calledAgentsResults_args"))[1]));
//            /* end agents callAll argument i + 2 */
// /* end agents callAll args i + 2 */
//        }
//        /*for (int i = 0; i < z; i++) {
//
//         // tell Agents to move
//         agents.callAll(Nomad.MIGRATE);
//
//         // sync all Agent status
//         agents.manageAll();
//
//         // find out where they live now
//         calledAgentsResults = (Object[]) agents.callAll(Nomad.GET_HOSTNAME,
//         agentsCallAllObjs);
//
//         }*/
// /*
//         "calledAgentsResults", calledAgentsResults, 
//         "calledAgentsResults_args", ProvUtils.getObjectArray(Nomad.GET_HOSTNAME,
//         agentsCallAllObjs), "execTime", execTime);
//         */
//        // test distribution
//        Object[] calledAgentsResults = (Object[]) entities.get("calledAgentsResults");
//        Object lastAgentResult = calledAgentsResults[calledAgentsResults.length - 1];
//        String lastAgentResultID = ProvUtils.getGlobalResourceID("lastAgentResult"
//                + calledAgentsResults[calledAgentsResults.length - 1]);
//        prov.addRelationalProv(lastAgentResultID, ProvOntology.getRDFTypeFullURI(),
//                ProvOntology.getEntityStartingPointClassFullURI());
//        prov.addRelationalProv(lastAgentResultID,
//                ProvOntology.getValueExpandedPropertyFullURI(),
//                (String) lastAgentResult);
        StopWatch.stop(true);
    }

    public void main_prospective(ProvenanceStore prov, Object[] args) {
        StopWatch.start(true);
        StopWatch.stop(true);
    }

    private final String user_ID = ProvUtils.getGlobalUserName();
    private final String testDriver_ID
            = ProvUtils.getUniversalResourceID("TestDriver");
    private final String testDriver_default_constructor_ID
            = ProvUtils.getUniversalResourceID(
                    "TestDriver_default_constructor");
    private final String testDriverMain_ID
            = ProvUtils.getUniversalResourceID("TestDriver.main");
    private final String testDriverMain_args_ID
            = ProvUtils.getUniversalResourceID("TestDriver.main_args");
    private final String NODE_FILE_ID
            = ProvUtils.getUniversalResourceID("NODE_FILE");
    private final String startTime_ID
            = ProvUtils.getUniversalResourceID("startTime");
    private final String MASS_ID = ProvUtils.getGlobalResourceID("MASS");
    private final String MASSsetNodeFilePath_ID
            = ProvUtils.getUniversalResourceID("MASS.setNodeFilePath");
    private final String MASSinit_ID
            = ProvUtils.getUniversalResourceID("MASS.init");
    private final String MASSProvinit_args_ID
            = ProvUtils.getUniversalResourceID("MASS.init_args");
    private final String main_ID = ProvUtils.getUniversalResourceID("main");
    private final String collector_ID
            = ProvUtils.getUniversalResourceID("collector");
    private final String MASSProv_ID
            = ProvUtils.getUniversalResourceID("MASSProv");
    private final String MASSProvinit_ID
            = ProvUtils.getUniversalResourceID("MASSProv.init");
    private final String x_ID = ProvUtils.getUniversalResourceID("x");
    private final String y_ID = ProvUtils.getUniversalResourceID("y");
    private final String z_ID = ProvUtils.getUniversalResourceID("z");
    private final String places_ID = ProvUtils.getUniversalResourceID("places");
    private final String places_constructor_args_ID
            = ProvUtils.getUniversalResourceID("places_constructor_args");
    private final String places_constructor_args_handle_ID
            = ProvUtils.getUniversalResourceID(
                    "places_constructor_args_handle");
    private final String places_constructor_args_className_ID
            = ProvUtils.getUniversalResourceID(
                    "places_constructor_args_className");
    private final String places_constructor_args_argument_ID
            = ProvUtils.getUniversalResourceID(
                    "places_constructor_args_argument");
    private final String places_constructor_args_size_ID
            = ProvUtils.getUniversalResourceID("places_constructor_args_size");
    private final String placeCallAllObjs_ID
            = ProvUtils.getUniversalResourceID("placeCallAllObjs");
    private final String placeCallObjs_constructor_args_length_calculation_ID
            = ProvUtils.getUniversalResourceID(
                    "placeCallObjs_constructor_args_length_calculation");
    private final String placeCallAllObjs_constructor_args_length_ID
            = ProvUtils.getUniversalResourceID(
                    "placeCallAllObjs_constructor_args_length");
    private final String placeCallAllObjs_constructor_ID
            = ProvUtils.getUniversalResourceID("placeCallAllObjs_constructor");
    private final String placeCallAllObjs_constructor_args_ID = ProvUtils.
            getUniversalResourceID("placeCallAllObjs_constructor_args");
    private final String calledPlacesResults_ID
            = ProvUtils.getUniversalResourceID("calledPlacesResults");
    private final String placesCallAll_ID
            = ProvUtils.getUniversalResourceID("placesCallAll");
    private final String places_callAll_args_matrix_GET_HOSTNAME_ID
            = ProvUtils.getUniversalResourceID(
                    "places_callAll_args_matrix_GET_HOSTNAME");
    private final String placesCallAll_args_ID
            = ProvUtils.getUniversalResourceID("placesCallAll_args");
    private final String agents_ID
            = ProvUtils.getUniversalResourceID("agents");
    private final String agents_constructor_ID
            = ProvUtils.getUniversalResourceID("agents_constructor");
    private final String agents_constructor_args_ID
            = ProvUtils.getUniversalResourceID("agents_constructor_args");
    private final String agents_constructor_args_handle_ID
            = ProvUtils.getUniversalResourceID(
                    "agents_constructor_args_handle");
    private final String agents_constructor_args_className_ID
            = ProvUtils.getUniversalResourceID(
                    "agents_constructor_args_className");
    private final String agents_constructor_args_argument_ID
            = ProvUtils.getUniversalResourceID(
                    "agents_constructor_args_argument");
    private final String agents_constructor_args_initPopulation_ID
            = ProvUtils.getUniversalResourceID(
                    "agents_constructor_args_initPopulation");
    private final String agents_constructor_args_initPopulationCalculation_ID
            = ProvUtils.getUniversalResourceID(
                    "agents_constructor_args_initPopulationCalculation");
    private final String agentsCallAllObjs_ID
            = ProvUtils.getUniversalResourceID("agentsCallAllObjs");
    private final String agentsCallAllObjs_constructor_args_lengthCalculation_ID
            = ProvUtils.getUniversalResourceID(
                    "agentsCallAllObjs_constructor_args_lengthCalculation");
    private final String agentsCallAllObjs_constructor_args_length_ID
            = ProvUtils.getUniversalResourceID(
                    "agentsCallAllObjs_constructor_args_length");
    private final String agentsCallAllObjs_constructor_ID
            = ProvUtils.getUniversalResourceID("agentsCallAllObjs_constructor");
    private final String agentsCallAllObjs_constructor_args_ID
            = ProvUtils.getUniversalResourceID(
                    "agentsCallAllObjs_constructor_args");
    private final String calledAgentsResults_ID
            = ProvUtils.getUniversalResourceID("calledAgentsResults");
    private final String agents_callAll_ID
            = ProvUtils.getUniversalResourceID("agents_callAll");
    private final String agents_callAll_args_ID
            = ProvUtils.getUniversalResourceID("agents_callAll_args");
    private final String agents_callAll_args_functionId_ID
            = ProvUtils.getUniversalResourceID(
                    "agents_callAll_args_functionId");
    private final String agents_callAll_args_argument_ID
            = ProvUtils.getUniversalResourceID("agents_callAll_args_argument");
    private final String execTime_ID
            = ProvUtils.getUniversalResourceID("execTime");
}
