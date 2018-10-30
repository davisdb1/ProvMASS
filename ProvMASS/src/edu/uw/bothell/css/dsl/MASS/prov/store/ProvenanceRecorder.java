package edu.uw.bothell.css.dsl.MASS.prov.store;

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledAgent;
import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledObject;
import edu.uw.bothell.css.dsl.MASS.prov.ProvOntology;
import edu.uw.bothell.css.dsl.MASS.prov.core.Agent;
import edu.uw.bothell.css.dsl.MASS.prov.core.Place;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.util.Date;

/**
 * Standardizes documentation of recurring source code concepts, such as method
 * invocation, field assignment, and return statements. Assumptions made during
 * provenance capture correspond to the level of granularity selected by the
 * application developer (e.g. ACTIVITY assumes that it can use the last stacked
 * activity ID to determine the calling procedure ID based on the assumption
 * that that ID was generated and stacked... meaning that ACTIVITY level
 * granularity is turned on and implemented in not only the method that called
 * the documentProcedure recording procedure but also the procedure that called
 * that procedure).
 *
 * @author Delmar B. Davis
 */
public class ProvenanceRecorder {
//
//    /**
//     * Documents a procedure call. If a calling method trace is available, it
//     * will be used to add the procedure to the respective call-graph. If the
//     * caller follows the ProvEnabledAgent API, it will be assumed that it was
//     * responsible for starting the activity represented by the procedure.
//     *
//     * REMEMBER TO IMPLEMENT GRANULARITY HERE, MOVE COSTUM GRANULARITY TO
//     * HIGHEST NUMBER, DURING CUSTOM GRANULARITY IGNORE NULLS, DURING OTHER
//     * GRANULARITIES STILL DO NULL CHECK BUT DONT EVEN BOTHER IF THE GRANULARITY
//     * DOESN'T MATCH THE DOCUMENTATION TASK
//     *
//     * DOCUMENT PROCEDURE INFORMEDBY FIELD <--- THIS IS HOW WE SHOW CAUSAL
//     * ORDERING OF DOCUMENTED EVENTS
//     *
//     * @param caller - An instance of the class that defines the procedure being
//     * documented. The parameter is used to determine whether or not an instance
//     * of said class can be considered a software agent. If so, it is also
//     * documented that the agent started the activity associated with the
//     * procedure. This parameter is nullable, and if null, then this parameter
//     * will be ignored, as will the associated documenting.
//     * @param store - The store where the documentation should be recorded
//     * @param procName - The name of the procedure being documented
//     * @param label
//     * @param alwaysPushActivityID
//     * @param ignoreGranularity
//     * @return The unique resource identifier associated with the documented
//     * procedure
//     */
//    public static String documentProcedure(ProvenanceStore store,
//            Object caller, String procName, String label,
//            boolean alwaysPushActivityID, boolean ignoreGranularity) {
//        StopWatch.start(true);
//        alwaysPushActivityID = false;
//        String procRID = null;
//
//        if (MASSProv.provOn && store != null && procName != null
//                && (ignoreGranularity || granularityLevel() >= Granularity.PROCEDURE.getValue())) {
//            try {
//                long msTime = System.currentTimeMillis();
//                long nsTime = System.nanoTime();
//                // get an id for the documented procedure
//                procRID = ProvUtils.getUniversalResourceID(procName);
//                // get the location where the procedure was invoked
//                String hostName = ProvUtils.getHostName();
//                /* proc a prov:entity */
//                // define the procedure as an activity
//                store.addRelationalProv(procRID, ProvOntology.getRDFTypeFullURI(),
//                        ProvOntology.getActivityStartingPointClassFullURI());
//                /* proc startedat time */
//                store.addRelationalProv(procRID,
//                        ProvOntology.getStartedAtTimeStartingPointPropertyFullURI(),
//                        new StringBuffer("\"").append(String.valueOf(nsTime)).append("\"").toString());
//                store.addRelationalProv(procRID,
//                        ProvOntology.getAtTimeQualifiedPropertyFullURI(),
//                        new StringBuffer("\"").append(String.valueOf(msTime)).append("\"").toString());
//                /* proc prov:locatedAt hostname */
//                // document where the procedure was invoked
//                store.addRelationalProv(procRID,
//                        ProvOntology.getAtLocationExpandedPropertyFullURI(),
//                        hostName);
//                ResourceMatcher matcher = ResourceMatcher.getMatcher();
//                String callerRID = matcher.peekActivityID();
//                if (callerRID != null) {
//                    /* the calling procedure's behavior or state was influenced 
//                by the invocation of the documented procedure call */
//                    store.addRelationalProv(callerRID,
//                            ProvOntology.getWasInfluencedByQualifiedPropertyFullURI(),
//                            procRID);
//                    /* the documented procedure influenced the
//                behavior or state of the calling procedure */
//                    store.addRelationalProv(procRID,
//                            ProvOntology.getInfluencedExpandedPropertyFullURI(),
//                            callerRID);
//                    /* push documented ID of documented activity */
//                    // assumes this push will be expected since an activity was available
//                    // but should only happen once
//                    if (!alwaysPushActivityID) {
//                        matcher.pushActivityID(procRID); // gets 
//                    } // if alwaysPushActivityID is true then let it be recorded elsewhere
//                }
//                if (alwaysPushActivityID) {
//                    matcher.pushActivityID(procRID); // gets 
//                }
//                if (caller instanceof ProvEnabledAgent) {
//                    store.addRelationalProv(procRID,
//                            ProvOntology.getWasStartedByExpandedPropertyFullURI(),
//                            ((ProvEnabledAgent) caller).getOwnerUUID());
//                }
//                if (label != null) {
//                    store.addRelationalProv(procRID,
//                            ProvOntology.getRDFSLabelFullURI(),
//                            new StringBuilder("\"").append(label).append("\"").toString());
//                }
//            } catch (Exception e) {
//                e.printStackTrace(IO.getLogWriter());
//            }
//        }
//        StopWatch.stop(true);
//        return procRID;
//    }
//
//    public static String documentProcedure(boolean provOn, ProvenanceStore store,
//            Object caller, String procName, String label,
//            boolean alwaysPushActivityID, boolean ignoreGranularity) {
//        StopWatch.start(true);
//        if (provOn) {
//            return documentProcedure(store, caller, procName, label, alwaysPushActivityID, ignoreGranularity);
//        }
//        StopWatch.stop(true);
//        return null;
//    }
//
//    /**
//     * @param caller - An instance of the class that defines the procedure being
//     * documented. The parameter is used to determine whether or not an instance
//     * of said class can be considered a software agent. If so, it is also
//     * documented that the agent started the activity associated with the
//     * procedure. This parameter is nullable, and if null, then this parameter
//     * will be ignored, as will the associated documenting.
//     * @param store - The store where the documentation should be recorded
//     * @param procName - The name of the procedure being documented
//     * @param label
//     * @param alwaysPushActivityID
//     * @return The unique resource identifier associated with the documented
//     * procedure
//     */
//    public static String documentProcedure(ProvenanceStore store,
//            Object caller, String procName, String label,
//            boolean alwaysPushActivityID) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        return documentProcedure(store, caller, procName, label, alwaysPushActivityID, false);
//    }
//
//    public static String documentProcedure(boolean provOn, ProvenanceStore store,
//            Object caller, String procName, String label,
//            boolean alwaysPushActivityID) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            return documentProcedure(store, caller, procName, label, alwaysPushActivityID, false);
//        }
//        return null;
//    }
//
//    /**
//     *
//     * @param store
//     * @param caller
//     * @param procName
//     * @param label
//     * @param alwaysPushActivityID
//     * @param paramNames
//     * @param params
//     * @param ignoreGranularity
//     * @return
//     */
//    public static String documentProcedure(ProvenanceStore store,
//            Object caller, String procName, String label,
//            boolean alwaysPushActivityID, String[] paramNames, Object[] params,
//            boolean ignoreGranularity) {
//        StopWatch.start(true);
//        alwaysPushActivityID = false;
//        String procRID = null;
//        if (MASSProv.provOn) {
//            try {
//                procRID = ProvenanceRecorder.documentProcedure(store, caller,
//                        procName, label, alwaysPushActivityID, ignoreGranularity);
//                if (procRID != null && paramNames != null && params != null
//                        && (ignoreGranularity || granularityLevel() >= Granularity.PARAMS.getValue())) {
//                    ResourceMatcher matcher = ResourceMatcher.getMatcher();
//                    String paramRID, stackedArgID;
//                    String paramValue = null;
//                    for (int i = 0, im = ProvUtils.getLowest(
//                            paramNames.length, params.length); i < im; i++) {
//                        paramValue = "\"null\"";
//                        paramRID = ProvUtils.getUniversalResourceID(paramNames[i]);
//                        stackedArgID = null;
//                        /* param is an entity */
//                        store.addRelationalProv(paramRID, ProvOntology.getRDFTypeFullURI(),
//                                ProvOntology.getEntityStartingPointClassFullURI());
//                        /* param has value (remember id is unique as references are
//                        passed by value... this will not overwrite the value outside 
//                        of the procedure's scope) */
//                        if (params[i] != null) { // value available
//                            String value = isImmutable(params[i]) || params[i] instanceof Date
//                                    ? params[i].toString() : String.valueOf(params[i].hashCode());
//                            paramValue = new StringBuffer("\"").append(value).
//                                    append("\"").toString();
//                        } else { // sensitive data
//                            paramValue = "\"sensitiveValue\"";
//                        }
//                        store.addRelationalProv(paramRID,
//                                ProvOntology.getValueExpandedPropertyFullURI(),
//                                paramValue);
//                        /* proc used param */
//                        store.addRelationalProv(procRID,
//                                ProvOntology.getUsedStartingPointPropertyFullURI(),
//                                paramRID);
//                        if (!isImmutable(params[i])) {
//                            stackedArgID = matcher.popEntityID();
//                            if (stackedArgID != null) { // was stacked
//                                /* param is alternate version of the corresponding argument */
//                                store.addRelationalProv(paramRID,
//                                        ProvOntology.getAlternateOfExpandedPropertyFullURI(),
//                                        stackedArgID);
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace(IO.getLogWriter());
//            }
//        }
//        StopWatch.stop(true);
//        return procRID;
//    }
//
//    public static String documentProcedure(boolean provOn, ProvenanceStore store,
//            Object caller, String procName, String label,
//            boolean alwaysPushActivityID, String[] paramNames, Object[] params,
//            boolean ignoreGranularity) {
//        StopWatch.start(true);
//        alwaysPushActivityID = false;
//        StopWatch.stop(true);
//        if (provOn) {
//            return documentProcedure(store, caller, procName, label, alwaysPushActivityID, paramNames, params, ignoreGranularity);
//        }
//        return null;
//    }
//
//    /**
//     *
//     * @param store
//     * @param caller
//     * @param procName
//     * @param label
//     * @param alwaysPushActivityID
//     * @param paramNames
//     * @param params
//     * @return
//     */
//    public static String documentProcedure(ProvenanceStore store,
//            Object caller, String procName, String label,
//            boolean alwaysPushActivityID, String[] paramNames, Object[] params) {
//        StopWatch.start(true);
//        alwaysPushActivityID = false;
//        StopWatch.stop(true);
//        return documentProcedure(store, caller, procName, label, alwaysPushActivityID, paramNames, params, false);
//    }
//
//    public static String documentProcedure(boolean provOn, ProvenanceStore store,
//            Object caller, String procName, String label,
//            boolean alwaysPushActivityID, String[] paramNames, Object[] params) {
//        StopWatch.start(true);
//        alwaysPushActivityID = false;
//        StopWatch.stop(true);
//        if (provOn) {
//            return documentProcedure(store, caller, procName, label, alwaysPushActivityID, paramNames, params, false);
//        }
//        return null;
//    }
//
//    /**
//     * At the end of a procedure it may be necessary to remove the id of the
//     * procedure from the activity stack. At the end of the documented
//     * procedure, it may also be necessary to stack the returned entity for
//     * documenting the calling procedure. Last and most importantly, we would
//     * like to know what time the procedure ended at, regardless of the
//     * configured provenance granularity level.
//     *
//     * @param store
//     * @param caller
//     * @param procRID
//     * @param procName
//     * @param returnObjectName
//     * @param returnObject
//     * @param returnObjectRID
//     * @param label
//     * @param procGeneratedReturn
//     * @param alwaysStackReturnID
//     * @param dontPopActivityID
//     * @param ignoreGranularity
//     */
//    public static void endProcedureDocumentation(ProvenanceStore store,
//            Object caller, String procName, String procRID, String returnObjectName,
//            Object returnObject, String returnObjectRID, String label,
//            boolean procGeneratedReturn, boolean alwaysStackReturnID,
//            boolean dontPopActivityID, boolean ignoreGranularity) {
//        StopWatch.start(true);
//        if (MASSProv.provOn && store != null && (ignoreGranularity
//                || granularityLevel() >= Granularity.PROCEDURE.getValue())) {
//            try {
//                // is the procedure undefined
//                if (procRID == null) {
//                    // document the procedure first
//                    procRID = ProvenanceRecorder.documentProcedure(store, caller,
//                            procName, label, false, ignoreGranularity);
//                }
//                ResourceMatcher matcher = ResourceMatcher.getMatcher();
//                String toPop = matcher.peekActivityID();
//                // do the stackedID and the ID that is expected to be popped match?
//                if (!dontPopActivityID && procRID.equals(toPop)) {
//                    // pop it
//                    matcher.popActivityID();
//                }
//                /* handle documenting proc return */
//                documentProcReturn(procName, procRID, returnObject, returnObjectRID,
//                        returnObjectName, store, procGeneratedReturn,
//                        alwaysStackReturnID, ignoreGranularity);
//                /* Record end time */
//                long endtimeNS = System.nanoTime();
//                store.addRelationalProv(procRID, ProvOntology.getRDFTypeFullURI(),
//                        ProvOntology.getActivityStartingPointClassFullURI());
//                store.addRelationalProv(procRID,
//                        ProvOntology.getEndedAtTimeStartingPointPropertyFullURI(),
//                        new StringBuilder("\"").append(String.valueOf(endtimeNS)).append("\"").toString());
//            } catch (Exception e) {
//                e.printStackTrace(IO.getLogWriter());
//            }
//        }
//        StopWatch.stop(true);
//    }
//
//    public static void endProcedureDocumentation(boolean provOn, ProvenanceStore store,
//            Object caller, String procName, String procRID, String returnObjectName,
//            Object returnObject, String returnObjectRID, String label,
//            boolean procGeneratedReturn, boolean alwaysStackReturnID,
//            boolean dontPopActivityID, boolean ignoreGranularity) {
//        StopWatch.start(true);
//        if (provOn) {
//            endProcedureDocumentation(store, caller, procName, procRID, returnObjectName,
//                    returnObject, returnObjectRID, label, procGeneratedReturn, alwaysStackReturnID,
//                    dontPopActivityID, ignoreGranularity);
//        }
//        StopWatch.stop(false);
//    }
//
//    /**
//     * @param store
//     * @param caller
//     * @param procName
//     * @param procRID
//     * @param returnObjectName
//     * @param returnObject
//     * @param returnObjectRID
//     * @param label
//     * @param procGeneratedReturn
//     * @param alwaysStackReturnID
//     * @param dontPopActivityID
//     */
//    public static void endProcedureDocumentation(ProvenanceStore store,
//            Object caller, String procName, String procRID, String returnObjectName,
//            Object returnObject, String returnObjectRID, String label,
//            boolean procGeneratedReturn, boolean alwaysStackReturnID,
//            boolean dontPopActivityID) {
//        StopWatch.start(true);
//        endProcedureDocumentation(store, caller, procName, procRID, returnObjectName,
//                returnObject, returnObjectRID, label, procGeneratedReturn, alwaysStackReturnID,
//                dontPopActivityID, false);
//        StopWatch.stop(false);
//    }
//
//    public static void endProcedureDocumentation(boolean provOn, ProvenanceStore store,
//            Object caller, String procName, String procRID, String returnObjectName,
//            Object returnObject, String returnObjectRID, String label,
//            boolean procGeneratedReturn, boolean alwaysStackReturnID,
//            boolean dontPopActivityID) {
//        StopWatch.start(true);
//        if (provOn) {
//            endProcedureDocumentation(store, caller, procName, procRID, returnObjectName,
//                    returnObject, returnObjectRID, label, procGeneratedReturn, alwaysStackReturnID,
//                    dontPopActivityID, false);
//        }
//        StopWatch.stop(false);
//    }
//
//    public static void documentProcReturn(String procName, String procRID,
//            Object returnObject, String returnObjectRID, String returnObjectName,
//            ProvenanceStore store, boolean documentGenerationByProc,
//            boolean alwaysStackReturnID, boolean ignoreGranularity) {
//        StopWatch.start(true);
//        if (MASSProv.provOn && returnObject != null && (ignoreGranularity
//                || granularityLevel() >= Granularity.RETURN.getValue())) {
//            if (procName == null) {
//                procName = "anonymousProcedure";
//            }
//            ResourceMatcher matcher = ResourceMatcher.getMatcher();
//            /* get the ID of the returned entity ready */
//            if (returnObjectRID == null) { // not yet ready
//                if (returnObjectName == null) { // has no name (e.g. created at return)
//                    // use the proc name in the id (for human-readability)
//                    returnObjectName = new StringBuilder(procName)
//                            .append("_Return").toString();
//                } // now there is a name
//                returnObjectRID = ProvUtils.getUniversalResourceID(
//                        new StringBuilder().append(returnObjectName).
//                                append("_Return").toString());
//                // the RID didn't exist so assume it hasn't been documented
//                store.addRelationalProv(returnObjectRID,
//                        ProvOntology.getRDFTypeFullURI(),
//                        ProvOntology.getEntityStartingPointClassFullURI());
//            }
//            // document the return object's value just prior to return
//            store.addRelationalProv(returnObjectRID,
//                    ProvOntology.getValueExpandedPropertyFullURI(),
//                    new StringBuilder("\"").append(returnObject.toString()).append("\"").toString());
//            if (documentGenerationByProc) {
//                boolean procRIDisNull = procRID == null;
//                if (procRID == null) {
//                    procRID = ProvUtils.getUniversalResourceID(procName);
//                }
//                store.addRelationalProv(procRID,
//                        ProvOntology.getGeneratedExpandedPropertyFullURI(),
//                        returnObjectRID);
//                long atTimeNS = System.nanoTime();
//                // only record generation time if this documentation marks the entities generation
//                if (procRIDisNull) {
//                    store.addRelationalProv(returnObjectRID,
//                            ProvOntology.getGeneratedAtTimeExpandedPropertyFullURI(),
//                            new StringBuilder("\"").append(String.valueOf(atTimeNS)).append("\"").toString());
//                }
//            }
//            if (returnObject instanceof ProvEnabledObject) {
//                store.addRelationalProv(returnObjectRID,
//                        ProvOntology.getAlternateOfExpandedPropertyFullURI(),
//                        ((ProvEnabledObject) returnObject).getOwnerUUID());
//            }
//            if (alwaysStackReturnID || ignoreGranularity || granularityLevel()
//                    >= Granularity.LINE.getValue()) {
//                matcher.pushEntityID(returnObjectRID);
//            }
//        }
//        StopWatch.stop(true);
//    }
//
//    public static void documentProcReturn(boolean provOn, String procName, String procRID,
//            Object returnObject, String returnObjectRID, String returnObjectName,
//            ProvenanceStore store, boolean documentGenerationByProc,
//            boolean alwaysStackReturnID, boolean ignoreGranularity) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            documentProcReturn(procName, procRID, returnObject, returnObjectRID,
//                    returnObjectName, store, documentGenerationByProc,
//                    alwaysStackReturnID, ignoreGranularity);
//        }
//    }
//
//    public static void documentProcReturn(String procName, String procRID,
//            Object returnObject, String returnObjectRID, String returnObjectName,
//            ProvenanceStore store, boolean documentGenerationByProc,
//            boolean alwaysStackReturnID) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        documentProcReturn(procName, procRID, returnObject, returnObjectRID, returnObjectName,
//                store, documentGenerationByProc, alwaysStackReturnID, false);
//    }
//
//    public static void documentProcReturn(boolean provOn, String procName, String procRID,
//            Object returnObject, String returnObjectRID, String returnObjectName,
//            ProvenanceStore store, boolean documentGenerationByProc,
//            boolean alwaysStackReturnID) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            documentProcReturn(procName, procRID, returnObject, returnObjectRID, returnObjectName,
//                    store, documentGenerationByProc, alwaysStackReturnID, false);
//        }
//    }
//
//    /**
//     *
//     * @param store
//     * @param caller
//     * @param callerRID
//     * @param procName
//     * @param procRID
//     * @param entityName
//     * @param entity
//     * @param entityRID
//     * @param alternativeOfRID
//     * @param stackRID
//     * @return
//     */
//    public static String documentEntity(ProvenanceStore store, Object caller,
//            String callerRID, String procName, String procRID, String entityName,
//            Object entity, String entityRID, String alternativeOfRID,
//            boolean stackRID) {
//        StopWatch.start(true);
//        if (MASSProv.provOn && store != null) {
//            try {
//                if (entityName == null) {
//                    if (entity != null) {
//                        entityName = entity.getClass().getSimpleName();
//                    } else {
//                        StringBuilder anonText
//                                = new StringBuilder("anonymousVariable");
//                        if (procName != null) {
//                            anonText.append("Of");
//                            if (caller != null) {
//                                anonText.append(caller.getClass()
//                                        .getSimpleName()).append('.');
//                            }
//                            anonText.append(procName);
//                        }
//                        entityName = anonText.toString();
//                    }
//                }
//                if (entityRID == null) {
//                    entityRID = ProvUtils.getUniversalResourceID(entityName);
//                }
//                if (callerRID != null && caller != null) {
//                    if (caller instanceof ProvEnabledObject) {
//                        // get the ID from the caller
//                        callerRID = ((ProvEnabledObject) caller).getOwnerUUID();
//                        // agent?
//                        if (caller instanceof ProvEnabledAgent) {
//                            // agents are treated as softwareAgents
//                            // attribute the entity to the agent
//                            store.addRelationalProv(entityRID,
//                                    ProvOntology.getWasAttributedToStartingPointPropertyFullURI(),
//                                    callerRID);
//                        } else {
//                            // places and messages are treated as collections
//                            // document membership of this variable as part of
//                            // collection regardless of its scope (could just as
//                            // easily been an field value that was overwritten
//                            // so it doesn't matter if this variable is local to
//                            // the procedure
//                            store.addRelationalProv(callerRID,
//                                    ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                                    entityRID);
//                        }
//                    }
//                }
//                if (entity != null) {
//                    String value = isImmutable(entity) || entity instanceof Date
//                            ? entity.toString() : String.valueOf(entity.hashCode());
//                    documentEntity(store, entityRID, value);
//                } else {
//                    documentEntity(store, entityRID);
//                }
//                if (procRID != null) {
//                    documentGeneration(store, entityRID, procRID);
//                }
//                if (alternativeOfRID != null) {
//                    documentAlternativity(store, entityRID, alternativeOfRID);
//                }
//                if (stackRID) {
//                    ResourceMatcher.getMatcher().pushEntityID(entityRID);
//                }
//            } catch (Exception e) {
//                e.printStackTrace(IO.getLogWriter());
//            }
//        }
//        StopWatch.stop(true);
//        return entityRID;
//    }
//
//    public static String documentEntity(boolean provOn, ProvenanceStore store, Object caller,
//            String callerRID, String procName, String procRID, String entityName,
//            Object entity, String entityRID, String alternativeOfRID,
//            boolean stackRID) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            return documentEntity(store, caller, callerRID, procName, procRID,
//                    entityName, entity, entityRID, alternativeOfRID, stackRID);
//        }
//        return null;
//    }
//
//    public static String documentAgent(ProvenanceStore store, Object agent,
//            String agentRID, String agentName, String label) {
//        StopWatch.start(true);
//        agentRID = MASSProv.provOn ? documentAgent(store, agent, agentRID, agentName) : null;
//        if (store != null && agentRID != null && label != null && !label.isEmpty()) {
//            store.addRelationalProv(agentRID, ProvOntology.getRDFSLabelFullURI(), new StringBuilder("\"").append(label).append("\"").toString());
//        }
//        StopWatch.stop(true);
//        return agentRID;
//    }
//
//    public static String documentAgent(boolean provOn, ProvenanceStore store, Object agent,
//            String agentRID, String agentName, String label) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            return documentAgent(store, agent, agentRID, agentName, label);
//        }
//        return null;
//    }
//
//    public static String documentProvEnabledObject(ProvenanceStore store,
//            Object provEnabledObject, String provEnabledObjectRID,
//            String provEnabledObjectName, boolean objIsProvEnabled,
//            String label) {
//        StopWatch.start(true);
//        if (MASSProv.provOn && store != null) {
//            if (provEnabledObjectRID == null) {
//                if (provEnabledObject != null) {
//                    objIsProvEnabled = provEnabledObject instanceof ProvEnabledObject;
//                    provEnabledObjectRID = ProvUtils.getUniversalResourceID(
//                            provEnabledObject.getClass().getSimpleName());
//                } else if (provEnabledObjectName != null) {
//                    provEnabledObjectRID = ProvUtils.getUniversalResourceID(
//                            provEnabledObjectName);
//                } else {
//                    provEnabledObjectRID = ProvUtils.getUniversalResourceID(
//                            new StringBuilder("anonymous").
//                                    append(provEnabledObjectName).toString());
//                }
//            } // provEnabledObjectRID != null now
//            if (objIsProvEnabled) {
//                store.addRelationalProv(provEnabledObjectRID,
//                        ProvOntology.getRDFTypeFullURI(),
//                        ProvOntology.getCollectionExpandedClassFullURI());
//            } else {
//                store.addRelationalProv(provEnabledObjectRID,
//                        ProvOntology.getRDFTypeFullURI(),
//                        ProvOntology.getEntityStartingPointClassFullURI());
//            }
//            if (label != null) {
//                store.addRelationalProv(provEnabledObjectRID,
//                        ProvOntology.getRDFSLabelFullURI(),
//                        new StringBuilder("\"").append(label).append("\"").toString());
//            }
//        }
//        StopWatch.stop(true);
//        return provEnabledObjectRID;
//    }
//
//    public static String documentProvEnabledObject(boolean provOn, ProvenanceStore store,
//            Object provEnabledObject, String provEnabledObjectRID,
//            String provEnabledObjectName, boolean objIsProvEnabled,
//            String label) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            return documentProvEnabledObject(store,
//                    provEnabledObject, provEnabledObjectRID,
//                    provEnabledObjectName, objIsProvEnabled,
//                    label);
//        }
//        return null;
//    }
//
//    /**
//     *
//     * @param store
//     * @param agent
//     * @param agentName
//     * @param agentRID
//     * @return
//     */
//    public static String documentAgent(ProvenanceStore store, Object agent,
//            String agentRID, String agentName) {
//        StopWatch.start(true);
//        if (MASSProv.provOn) {
//            if (agentRID == null) {
//                if (agentName == null) {
//                    agentName = "anonymousAgent";
//                }
//                if (agent != null) {
//                    agentRID = ProvUtils.getUniversalResourceID(agent.getClass().getSimpleName());
//                } else {
//                    agentRID = ProvUtils.getUniversalResourceID(agentName);
//                }
//            }
//            store.addRelationalProv(agentRID, ProvOntology.getRDFTypeFullURI(),
//                    ProvOntology.getSoftwareAgentExpandedClassFullURI());
//        }
//        StopWatch.stop(false);
//        return agentRID;
//    }
//
//    public static String documentAgent(boolean provOn, ProvenanceStore store, Object agent,
//            String agentRID, String agentName) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            return documentAgent(store, agent, agentRID, agentName);
//        }
//        return null;
//    }
//
//    private static String documentEntity(ProvenanceStore store,
//            String entityRID) {
//        StopWatch.start(true);
//        if (MASSProv.provOn && store != null) {
//            store.addRelationalProv(entityRID,
//                    ProvOntology.getRDFTypeFullURI(),
//                    ProvOntology.getEntityStartingPointClassFullURI());
//        }
//        StopWatch.stop(true);
//        return entityRID;
//    }
//
//    private static String documentEntity(boolean provOn, ProvenanceStore store,
//            String entityRID) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            return documentEntity(store, entityRID);
//        }
//        return null;
//    }
//
//    private static void documentGeneration(ProvenanceStore store,
//            String entityRID, String activityRID) {
//        StopWatch.start(true);
//        if (MASSProv.provOn && store != null) {
//            store.addRelationalProv(activityRID,
//                    ProvOntology.getGeneratedExpandedPropertyFullURI(),
//                    entityRID);
//            store.addRelationalProv(entityRID,
//                    ProvOntology.getGeneratedAtTimeExpandedPropertyFullURI(),
//                    new StringBuilder("\"").append(String.valueOf(System.nanoTime())).append("\"").toString());
//        }
//        StopWatch.stop(true);
//    }
//
//    private static void documentGeneration(boolean provOn, ProvenanceStore store,
//            String entityRID, String activityRID) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            documentGeneration(store, entityRID, activityRID);
//        }
//    }
//
//    private static void documentAlternativity(ProvenanceStore store,
//            String entityRID, String alternativeOfRID) {
//        StopWatch.start(true);
//        if (MASSProv.provOn && store != null) {
//            store.addRelationalProv(entityRID,
//                    ProvOntology.getAlternateOfExpandedPropertyFullURI(),
//                    alternativeOfRID);
//        }
//        StopWatch.stop(true);
//    }
//
//    private static void documentAlternativity(boolean provOn, ProvenanceStore store,
//            String entityRID, String alternativeOfRID) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            documentAlternativity(store, entityRID, alternativeOfRID);
//        }
//    }
//
//    private static void documentEntity(ProvenanceStore store, String entityRID, String value) {
//        StopWatch.start(true);
//        if (MASSProv.provOn && store != null) {
//            documentEntity(store, entityRID);
//            if (value != null) {
//                store.addRelationalProv(entityRID,
//                        ProvOntology.getValueExpandedPropertyFullURI(),
//                        new StringBuilder("\"").append(value).append("\"").toString());
//            }
//        }
//        StopWatch.stop(true);
//    }
//
//    private static void documentEntity(boolean provOn, ProvenanceStore store, String entityRID, String value) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            documentEntity(store, entityRID, value);
//        }
//    }
//

    public static String documentUser(ProvenanceStore store) {
        StopWatch.start(true);
        String userRID = null;
        String userName = System.getProperty("user.name");
        if (MASSProv.provOn && store != null
                && userName != null && !userName.isEmpty()) {
            userRID = userName;
            store.addRelationalProv(new StringBuffer(userRID), ProvOntology.getRDFTypeFullURIBuffer(),
                    ProvOntology.getAgentStartingPointClassFullURIBuffer());
        }
        StopWatch.stop(true);
        return userRID;
    }
//
//    public static String documentUser(boolean provOn, ProvenanceStore store) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            return documentUser(store);
//        }
//        return null;
//    }
//
//    public static String documentSimDriver(ProvenanceStore store,
//            String simulatorRID, String driverRID, String driverMethodName,
//            String label) {
//        StopWatch.start(true);
//        if (MASSProv.provOn && store != null) {
//            // handle unspecified simulator
//            if (simulatorRID == null) {
//                String simulatorName = "UnknownMASSApp_";
//                // make sure simulatorRID is ready
//                if (MASSProv.isInitialized()) {
//                    simulatorName = StoreManager.getAppName();
//                }
//                // make the ID
//                simulatorRID = ProvUtils.getUniversalResourceID(simulatorName);
//                // assume simulator isn't documented yet... document it
//                ProvenanceRecorder.documentAgent(store, null, simulatorRID,
//                        simulatorName, "Simulator");
//            }
//            // handle unspecified driver method
//            if (driverRID == null) {
//                // handle unspecified driver method name
//                if (driverMethodName == null) {
//                    driverMethodName = "unknownDriverMethod";
//                }
//                // get a resource identifier for the driver method
//                driverRID = ProvUtils.getUniversalResourceID(driverMethodName);
//                // assume that the driver has not been documented if RID is null
//                store.addRelationalProv(driverRID,
//                        ProvOntology.getRDFTypeFullURI(),
//                        ProvOntology.getActivityQualifiedPropertyFullURI());
//            }
//            // assume that the driver procedure has not yet been associated with 
//            // the simulator software
//            // simulator started the driver activity
//            store.addRelationalProv(driverRID,
//                    ProvOntology.getWasStartedByExpandedPropertyFullURI(),
//                    simulatorRID);
//            // record start time
//            store.addRelationalProv(driverRID,
//                    ProvOntology.getAtTimeQualifiedPropertyFullURI(),
//                    new StringBuilder("\"").
//                            append(String.valueOf(System.currentTimeMillis())).
//                            append("\"").toString());
//            // and startedAt time
//            store.addRelationalProv(driverRID,
//                    ProvOntology.getStartedAtTimeStartingPointPropertyFullURI(),
//                    new StringBuilder("\"").
//                            append(String.valueOf(System.nanoTime())).
//                            append("\"").toString());
//            // record location
//            store.addRelationalProv(driverRID,
//                    ProvOntology.getAtLocationExpandedPropertyFullURI(),
//                    ProvUtils.getHostName());
//            if (label != null) {
//                store.addRelationalProv(driverRID,
//                        ProvOntology.getRDFSLabelFullURI(), new StringBuilder("\"").append(label).append("\"").toString());
//            }
//        }
//        StopWatch.stop(true);
//        return driverRID;
//    }
//
//    public static String documentSimDriver(boolean provOn, ProvenanceStore store,
//            String simulatorRID, String driverRID, String driverMethodName,
//            String label) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            return documentSimDriver(store, simulatorRID, driverRID,
//                    driverMethodName, label);
//        }
//        return null;
//    }
//
//    public static String[] documentCommandLineArguments(ProvenanceStore store,
//            String simulatorRID, String[] args, String argsCollectionRID) {
//        StopWatch.start(true);
//        String[] argRIDs = null;
//        if (MASSProv.provOn && store != null) {
//            if (simulatorRID == null) {
//                String simulatorName = "UnknownMASSApp_";
//                // make sure simulatorRID is ready
//                if (MASSProv.isInitialized()) {
//                    simulatorName = StoreManager.getAppName();
//                }
//                // make the ID
//                simulatorRID = ProvUtils.getUniversalResourceID(simulatorName);
//                // assume simulator isn't documented yet... document it
//                ProvenanceRecorder.documentAgent(store, null, simulatorRID,
//                        simulatorName, "Simulator");
//            }
//            // make sure the RID for the args collection is ready
//            if (argsCollectionRID == null || argsCollectionRID.isEmpty()) {
//                argsCollectionRID = ProvUtils.getUniversalResourceID("args");
//            }
//            // document args... specified argsCollectionRID does not necessarily
//            // mean that args collection has been documented... assume RID was
//            // generated outside of method for scoping purposes
//            store.addRelationalProv(argsCollectionRID,
//                    ProvOntology.getRDFTypeFullURI(),
//                    ProvOntology.getCollectionExpandedClassFullURI());
//            // document each argument
//            // ingore a null args array
//            if (args != null) {
//                argRIDs = new String[args.length];
//                String argRID;
//                // for each arg
//                for (int i = 0, im = args.length; i < im; i++) {
//                    // generate the arg RID
//                    argRID = ProvUtils.getUniversalResourceID("arg" + i);
//                    // set the ID in the array of return IDs
//                    argRIDs[i] = argRID;
//                    // document the arg and its value
//                    ProvenanceRecorder.documentEntity(store, argRID, args[i]);
//                    // add it to the collection
//                    store.addRelationalProv(argsCollectionRID,
//                            ProvOntology.getHadMemberExpandedPropertyFullURI(),
//                            argRID);
//                    // do not assume that the simulator used the arg though
//                }
//                // assume that the simulator used the collection if it exists
//                store.addRelationalProv(simulatorRID,
//                        ProvOntology.getUsedStartingPointPropertyFullURI(),
//                        argsCollectionRID);
//            }
//        }
//        StopWatch.stop(true);
//        return argRIDs;
//    }
//
//    public static String[] documentCommandLineArguments(boolean provOn, ProvenanceStore store,
//            String simulatorRID, String[] args, String argsCollectionRID) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            return documentCommandLineArguments(store, simulatorRID, args,
//                    argsCollectionRID);
//        }
//        return null;
//    }
//
//    public static void documentFieldAccess(ProvenanceStore store,
//            String fieldName, String fieldRID, String procRID, Object accessed) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        documentFieldAccess(store, fieldName, fieldRID, procRID, accessed, false);
//    }
//
//    public static void documentFieldAccess(boolean provOn, ProvenanceStore store,
//            String fieldName, String fieldRID, String procRID, Object accessed) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            documentFieldAccess(store, fieldName, fieldRID, procRID, accessed,
//                    false);
//        }
//    }
//
//    public static void documentFieldAccess(ProvenanceStore store,
//            String fieldName, String fieldRID, String procRID, Object accessed,
//            boolean ignoreGranularity) {
//        StopWatch.start(true);
//        if (MASSProv.provOn && store != null) {
//            if (fieldName == null) { // ensure field name
//                fieldName = "anonymousField";
//            }
//            if (fieldRID == null) { // ensure field id
//                fieldRID = ProvUtils.getUniversalResourceID(fieldName);
//            }
//            if (procRID == null) { // ensure proc id
//                procRID = ProvUtils.getUniversalResourceID("anonymousProcedure");
//            }
//            // rid for this read op
//            String fieldAccessRID = ProvUtils.getUniversalResourceID(
//                    new StringBuilder().append(fieldName).append("_read").toString());
//            // add activity
//            store.addRelationalProv(fieldAccessRID, ProvOntology.getRDFTypeFullURI(),
//                    ProvOntology.getActivityStartingPointClassFullURI());
//            // read op started at
//            store.addRelationalProv(fieldAccessRID,
//                    ProvOntology.getStartedAtTimeStartingPointPropertyFullURI(),
//                    new StringBuilder().append("\"").append(System.nanoTime()).
//                            append("\"").toString());
//            // activity used the field
//            store.addRelationalProv(fieldAccessRID,
//                    ProvOntology.getUsedStartingPointPropertyFullURI(), fieldRID);
//
//            //document value of entity read    
//            StringBuilder value = new StringBuilder("\"");
//            if (accessed == null) {
//                value.append("null");
//            } else {
//                if (isImmutable(accessed) || accessed instanceof Date) {
//                    value.append(accessed.toString());
//                } else {
//                    value.append(String.valueOf(accessed.hashCode()));
//                }
//            }
//            value.append("\"");
//            store.addRelationalProv(fieldRID,
//                    ProvOntology.getValueExpandedPropertyFullURI(),
//                    value.toString());
//
//            // connect with procedure where the access occured
//            if (procRID != null) {
//                // the read op influenced the proc
//                store.addRelationalProv(fieldAccessRID,
//                        ProvOntology.getInfluencedExpandedPropertyFullURI(),
//                        procRID);
//                // proc was influenced by the read op
//                store.addRelationalProv(procRID,
//                        ProvOntology.getWasInfluencedByQualifiedPropertyFullURI(),
//                        fieldAccessRID);
//            }
//            // read op ended at
//            store.addRelationalProv(fieldAccessRID,
//                    ProvOntology.getEndedAtTimeStartingPointPropertyFullURI(),
//                    new StringBuilder().append("\"").append(System.nanoTime()).
//                            append("\"").toString());
//        }
//        StopWatch.stop(true);
//    }
//
//    public static void documentFieldAccess(boolean provOn, ProvenanceStore store,
//            String fieldName, String fieldRID, String procRID, Object accessed,
//            boolean ignoreGranularity) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            documentFieldAccess(store, fieldName, fieldRID, procRID, accessed,
//                    ignoreGranularity);
//        }
//    }
//
//    public static void documentFieldAssignment(ProvenanceStore store,
//            String fieldName, String fieldRID, Object field, String procRID) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        documentFieldAssignment(store, fieldName, fieldRID, field, procRID, false);
//    }
//
//    public static void documentFieldAssignment(boolean provOn, ProvenanceStore store,
//            String fieldName, String fieldRID, Object field, String procRID) {
//        StopWatch.start(true);
//        StopWatch.stop(true);
//        if (provOn) {
//            documentFieldAssignment(store, fieldName, fieldRID, field, procRID,
//                    false);
//        }
//    }
//
//    public static void documentFieldAssignment(ProvenanceStore store,
//            String fieldName, String fieldRID, Object field, String procRID,
//            boolean ignoreGranularity) {
//        StopWatch.start(true);
//        if (MASSProv.provOn && store != null) {
//            if (fieldName == null) { // ensure field name
//                fieldName = "anonymousField";
//            }
//            if (fieldRID == null) { // ensure field id
//                fieldRID = ProvUtils.getUniversalResourceID(fieldName);
//            }
//            if (procRID == null) { // ensure proc id
//                procRID = ProvUtils.getUniversalResourceID("anonymousProcedure");
//            }
//            // id for this write op
//            String fieldAssignmentRID = ProvUtils.getUniversalResourceID(
//                    new StringBuilder().append(fieldName).append("_write").toString());
//            // add activity
//            store.addRelationalProv(fieldAssignmentRID, ProvOntology.getRDFTypeFullURI(),
//                    ProvOntology.getActivityStartingPointClassFullURI());
//            // read op started at
//            store.addRelationalProv(fieldAssignmentRID,
//                    ProvOntology.getStartedAtTimeStartingPointPropertyFullURI(),
//                    new StringBuilder().append("\"").append(System.nanoTime()).
//                            append("\"").toString());
//            // id for the new data
//            String dataRID = ProvUtils.getUniversalResourceID(fieldName);
//            // add data
//            store.addRelationalProv(dataRID,
//                    ProvOntology.getRDFTypeFullURI(),
//                    ProvOntology.getEntityStartingPointClassFullURI());
//            // activity generated the field
//            store.addRelationalProv(fieldAssignmentRID,
//                    ProvOntology.getGeneratedExpandedPropertyFullURI(), dataRID);
//
//            //document value of entity       
//            store.addRelationalProv(dataRID,
//                    ProvOntology.getValueExpandedPropertyFullURI(),
//                    new StringBuilder("\"").append(field.toString()).append("\"").toString());
//
//            // connect with procedure where the assignment occured
//            if (procRID != null) {
//                // the write op influenced the proc
//                store.addRelationalProv(fieldAssignmentRID,
//                        ProvOntology.getInfluencedExpandedPropertyFullURI(),
//                        procRID);
//                // proc was influenced by the write op
//                store.addRelationalProv(procRID,
//                        ProvOntology.getWasInfluencedByQualifiedPropertyFullURI(),
//                        fieldAssignmentRID);
//            }
//            // associate the new data with the field
//            store.addRelationalProv(dataRID, ProvOntology.getAlternateOfExpandedPropertyFullURI(), fieldRID);
//            // and vice-versa... not vice-versa since the relationship is repeatedly overwritten
//            // store.addRelationalProv(fieldRID, ProvOntology.getAlternateOfExpandedPropertyFullURI(), dataRID);
//            // write op ended at
//            store.addRelationalProv(fieldAssignmentRID,
//                    ProvOntology.getEndedAtTimeStartingPointPropertyFullURI(),
//                    new StringBuilder().append("\"").append(System.nanoTime()).
//                            append("\"").toString());
//        }
//        StopWatch.stop(true);
//    }
//
//    public static void documentFieldAssignment(boolean provOn, ProvenanceStore store,
//            String fieldName, String fieldRID, Object field, String procRID,
//            boolean ignoreGranularity) {
//        StopWatch.start(true);
//        if (provOn) {
//            documentFieldAssignment(store, fieldName, fieldRID, field, procRID,
//                    ignoreGranularity);
//        }
//        StopWatch.stop(true);
//    }
//////////////////// STRING BUFFER PROCS START HERE ////////////////////////////

    /**
     * Documents a procedure call. If a calling method trace is available, it
     * will be used to add the procedure to the respective call-graph. If the
     * caller follows the ProvEnabledAgent API, it will be assumed that it was
     * responsible for starting the activity represented by the procedure.
     *
     * REMEMBER TO IMPLEMENT GRANULARITY HERE, MOVE COSTUM GRANULARITY TO
     * HIGHEST NUMBER, DURING CUSTOM GRANULARITY IGNORE NULLS, DURING OTHER
     * GRANULARITIES STILL DO NULL CHECK BUT DONT EVEN BOTHER IF THE GRANULARITY
     * DOESN'T MATCH THE DOCUMENTATION TASK
     *
     * DOCUMENT PROCEDURE INFORMEDBY FIELD <--- THIS IS HOW WE SHOW CAUSAL
     * ORDERING OF DOCUMENTED EVENTS
     *
     * @param caller - An instance of the class that defines the procedure being
     * documented. The parameter is used to determine whether or not an instance
     * of said class can be considered a software agent. If so, it is also
     * documented that the agent started the activity associated with the
     * procedure. This parameter is nullable, and if null, then this parameter
     * will be ignored, as will the associated documenting.
     * @param store - The store where the documentation should be recorded
     * @param procName - The name of the procedure being documented
     * @param label
     * @param alwaysPushActivityID
     * @param ignoreGranularity
     * @return The unique resource identifier associated with the documented
     * procedure
     */
    public static StringBuffer documentProcedure(ProvenanceStore store,
            Object caller, StringBuffer procName, StringBuffer label,
            boolean alwaysPushActivityID, boolean ignoreGranularity) {
        StopWatch.start(true);
        alwaysPushActivityID = false;
        StringBuffer procRID = null;

        if (MASSProv.provOn && store != null && procName != null
                && (ignoreGranularity || granularityLevel() >= Granularity.PROCEDURE.getValue())) {
            try {
                long msTime = System.currentTimeMillis();
                long nsTime = System.nanoTime();
                // get an id for the documented procedure
                procRID = ProvUtils.getUniversalResourceID(procName);
                // get the location where the procedure was invoked
                StringBuffer hostName = ProvUtils.getHostNameBuffer();
                /* proc a prov:entity */
                // define the procedure as an activity
                store.addRelationalProv(procRID, ProvOntology.getRDFTypeFullURIBuffer(),
                        ProvOntology.getActivityStartingPointClassFullURIBuffer());
                /* proc startedat time */
                store.addRelationalProv(procRID,
                        ProvOntology.getStartedAtTimeStartingPointPropertyFullURIBuffer(),
                        new StringBuffer("\"").append(String.valueOf(nsTime)).append("\""));
                store.addRelationalProv(procRID,
                        ProvOntology.getAtTimeQualifiedPropertyFullURIBuffer(),
                        new StringBuffer("\"").append(String.valueOf(msTime)).append("\""));
                /* proc prov:locatedAt hostname */
                // document where the procedure was invoked
                store.addRelationalProv(procRID,
                        ProvOntology.getAtLocationExpandedPropertyFullURIBuffer(),
                        hostName);
                ResourceMatcher matcher = ResourceMatcher.getMatcher();
                StringBuffer callerRID = matcher.peekActivityIDBuffer();
                if (callerRID != null) {
                    /* the calling procedure's behavior or state was influenced 
                by the invocation of the documented procedure call */
                    store.addRelationalProv(callerRID,
                            ProvOntology.getWasInfluencedByQualifiedPropertyFullURIBuffer(),
                            procRID);
                    /* the documented procedure influenced the
                behavior or state of the calling procedure */
                    store.addRelationalProv(procRID,
                            ProvOntology.getInfluencedExpandedPropertyFullURIBuffer(),
                            callerRID);
                    /* push documented ID of documented activity */
                    // assumes this push will be expected since an activity was available
                    // but should only happen once
                    if (!alwaysPushActivityID) {
                        // if callerRID isn't null assume we should also push this procRID
                        matcher.pushActivityID(procRID); // gets 
                    } // if alwaysPushActivityID is true then let it be recorded elsewhere
                }
                if (alwaysPushActivityID) {
                    matcher.pushActivityID(procRID); // gets 
                }
                if (caller instanceof ProvEnabledAgent) {
                    store.addRelationalProv(procRID,
                            ProvOntology.getWasStartedByExpandedPropertyFullURIBuffer(),
                            new StringBuffer(((ProvEnabledAgent) caller).getOwnerUUID()));
                }
                if (label != null) {
                    store.addRelationalProv(procRID,
                            ProvOntology.getRDFSLabelFullURIBuffer(),
                            new StringBuffer("\"").append(label).append("\""));
                }
            } catch (Exception e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }
        StopWatch.stop(true);
        return procRID;
    }

    public static StringBuffer documentProcedure(boolean provOn, ProvenanceStore store,
            Object caller, StringBuffer procName, StringBuffer label,
            boolean alwaysPushActivityID, boolean ignoreGranularity) {
        StopWatch.start(true);
        if (provOn) {
            return documentProcedure(store, caller, procName, label, alwaysPushActivityID, ignoreGranularity);
        }
        StopWatch.stop(true);
        return null;
    }

    /**
     * @param caller - An instance of the class that defines the procedure being
     * documented. The parameter is used to determine whether or not an instance
     * of said class can be considered a software agent. If so, it is also
     * documented that the agent started the activity associated with the
     * procedure. This parameter is nullable, and if null, then this parameter
     * will be ignored, as will the associated documenting.
     * @param store - The store where the documentation should be recorded
     * @param procName - The name of the procedure being documented
     * @param label
     * @param alwaysPushActivityID
     * @return The unique resource identifier associated with the documented
     * procedure
     */
    public static StringBuffer documentProcedure(ProvenanceStore store,
            Object caller, StringBuffer procName, StringBuffer label,
            boolean alwaysPushActivityID) {
        StopWatch.start(true);
        StopWatch.stop(true);
        return documentProcedure(store, caller, procName, label, alwaysPushActivityID, false);
    }

    public static StringBuffer documentProcedure(boolean provOn, ProvenanceStore store,
            Object caller, StringBuffer procName, StringBuffer label,
            boolean alwaysPushActivityID) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            return documentProcedure(store, caller, procName, label, alwaysPushActivityID, false);
        }
        return null;
    }

    /**
     *
     * @param store
     * @param caller
     * @param procName
     * @param label
     * @param alwaysPushActivityID
     * @param paramNames
     * @param params
     * @param ignoreGranularity
     * @return
     */
    public static StringBuffer documentProcedure(ProvenanceStore store,
            Object caller, StringBuffer procName, StringBuffer label,
            boolean alwaysPushActivityID, String[] paramNames, Object[] params,
            boolean ignoreGranularity) {
        StopWatch.start(true);
        alwaysPushActivityID = false;
        StringBuffer procRID = null;
        if (MASSProv.provOn) {
            try {
                procRID = ProvenanceRecorder.documentProcedure(store, caller,
                        procName, label, alwaysPushActivityID, ignoreGranularity);
                if (procRID != null && paramNames != null && params != null
                        && (ignoreGranularity || granularityLevel() >= Granularity.PARAMS.getValue())) {
                    ResourceMatcher matcher = ResourceMatcher.getMatcher();
                    StringBuffer paramRID, stackedArgID;
                    StringBuffer paramValue = null;
                    for (int i = 0, im = ProvUtils.getLowest(
                            paramNames.length, params.length); i < im; i++) {
                        paramRID = ProvUtils.getUniversalResourceID(new StringBuffer(paramNames[i]));
                        stackedArgID = null;
                        /* param is an entity */
                        store.addRelationalProv(paramRID, ProvOntology.getRDFTypeFullURIBuffer(),
                                ProvOntology.getEntityStartingPointClassFullURIBuffer());
                        /* param has value (remember id is unique as references are
                        passed by value... this will not overwrite the value outside 
                        of the procedure's scope) */
                        if (params[i] != null) { // value available
                            StringBuffer value = isImmutable(params[i]) || params[i] instanceof Date
                                    ? new StringBuffer(params[i].toString()) : new StringBuffer(String.valueOf(params[i].hashCode()));
                            paramValue = new StringBuffer("\"").append(value).
                                    append("\"");
                        } else { // sensitive data
                            paramValue = new StringBuffer("\"sensitiveValue\"");
                        }
                        store.addRelationalProv(paramRID,
                                ProvOntology.getValueExpandedPropertyFullURIBuffer(),
                                paramValue);
                        /* proc used param */
                        store.addRelationalProv(procRID,
                                ProvOntology.getUsedStartingPointPropertyFullURIBuffer(),
                                paramRID);
                        if (!isImmutable(params[i])) {
                            stackedArgID = matcher.popEntityIDBuffer();
                            if (stackedArgID != null) { // was stacked
                                /* param is alternate version of the corresponding argument */
                                store.addRelationalProv(paramRID,
                                        ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(),
                                        stackedArgID);
                            }
                        }
                        // alt param with ProvEnabledObject UUID if available
                        if(params[i] instanceof ProvEnabledObject){                            
                            store.addRelationalProv(paramRID, 
                                    ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(), 
                                    new StringBuffer(((ProvEnabledObject)params[i]).getOwnerUUID()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }
        StopWatch.stop(true);
        return procRID;
    }

    public static StringBuffer documentProcedure(boolean provOn, ProvenanceStore store,
            Object caller, StringBuffer procName, StringBuffer label,
            boolean alwaysPushActivityID, String[] paramNames, Object[] params,
            boolean ignoreGranularity) {
        StopWatch.start(true);
        alwaysPushActivityID = false;
        StopWatch.stop(true);
        if (provOn) {
            return documentProcedure(store, caller, procName, label, alwaysPushActivityID, paramNames, params, ignoreGranularity);
        }
        return null;
    }

    /**
     *
     * @param store
     * @param caller
     * @param procName
     * @param label
     * @param alwaysPushActivityID
     * @param paramNames
     * @param params
     * @return
     */ 
    public static StringBuffer documentProcedure(ProvenanceStore store,
            Object caller, StringBuffer procName, StringBuffer label,
            boolean alwaysPushActivityID, String[] paramNames, Object[] params) {
        StopWatch.start(true);
        alwaysPushActivityID = false;
        StopWatch.stop(true);
        return documentProcedure(store, caller, procName, label, alwaysPushActivityID, paramNames, params, false);
    }

    public static StringBuffer documentProcedure(boolean provOn, ProvenanceStore store,
            Object caller, StringBuffer procName, StringBuffer label,
            boolean alwaysPushActivityID, String[] paramNames, Object[] params) {
        StopWatch.start(true);
        alwaysPushActivityID = false;
        StopWatch.stop(true);
        if (provOn) {
            return documentProcedure(store, caller, procName, label, alwaysPushActivityID, paramNames, params, false);
        }
        return null;
    }

    /**
     * At the end of a procedure it may be necessary to remove the id of the
     * procedure from the activity stack. At the end of the documented
     * procedure, it may also be necessary to stack the returned entity for
     * documenting the calling procedure. Last and most importantly, we would
     * like to know what time the procedure ended at, regardless of the
     * configured provenance granularity level.
     *
     * @param store
     * @param caller
     * @param procRID
     * @param procName
     * @param returnObjectName
     * @param returnObject
     * @param returnObjectRID
     * @param label
     * @param procGeneratedReturn
     * @param alwaysStackReturnID
     * @param dontPopActivityID
     * @param ignoreGranularity
     */
    public static void endProcedureDocumentation(ProvenanceStore store,
            Object caller, StringBuffer procName, StringBuffer procRID, StringBuffer returnObjectName,
            Object returnObject, StringBuffer returnObjectRID, StringBuffer label,
            boolean procGeneratedReturn, boolean alwaysStackReturnID,
            boolean dontPopActivityID, boolean ignoreGranularity) {
        StopWatch.start(true);
        if (MASSProv.provOn && store != null && (ignoreGranularity
                || granularityLevel() >= Granularity.PROCEDURE.getValue())) {
            try {
                // is the procedure undefined
                if (procRID == null) {
                    // document the procedure first
                    procRID = ProvenanceRecorder.documentProcedure(store, caller,
                            procName, label, false, ignoreGranularity);
                }
                ResourceMatcher matcher = ResourceMatcher.getMatcher();
                StringBuffer toPop = matcher.peekActivityIDBuffer();
                // do the stackedID and the ID that is expected to be popped match?
                if (!dontPopActivityID && procRID.equals(toPop)) {
                    // pop it
                    matcher.popActivityIDBuffer();
                }
                /* handle documenting proc return */
                documentProcReturn(procName, procRID, returnObject, returnObjectRID,
                        returnObjectName, store, procGeneratedReturn,
                        alwaysStackReturnID, ignoreGranularity);
                /* Record end time */
                long endtimeNS = System.nanoTime();
                store.addRelationalProv(procRID, ProvOntology.getRDFTypeFullURIBuffer(),
                        ProvOntology.getActivityStartingPointClassFullURIBuffer());
                store.addRelationalProv(procRID,
                        ProvOntology.getEndedAtTimeStartingPointPropertyFullURIBuffer(),
                        new StringBuffer("\"").append(String.valueOf(endtimeNS)).append("\""));
            } catch (Exception e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }
        StopWatch.stop(true);
    }

    public static void endProcedureDocumentation(boolean provOn, ProvenanceStore store,
            Object caller, StringBuffer procName, StringBuffer procRID, StringBuffer returnObjectName,
            Object returnObject, StringBuffer returnObjectRID, StringBuffer label,
            boolean procGeneratedReturn, boolean alwaysStackReturnID,
            boolean dontPopActivityID, boolean ignoreGranularity) {
        StopWatch.start(true);
        if (provOn) {
            endProcedureDocumentation(store, caller, procName, procRID, returnObjectName,
                    returnObject, returnObjectRID, label, procGeneratedReturn, alwaysStackReturnID,
                    dontPopActivityID, ignoreGranularity);
        }
        StopWatch.stop(false);
    }

    /**
     * @param store
     * @param caller
     * @param procName
     * @param procRID
     * @param returnObjectName
     * @param returnObject
     * @param returnObjectRID
     * @param label
     * @param procGeneratedReturn
     * @param alwaysStackReturnID
     * @param dontPopActivityID
     */
    public static void endProcedureDocumentation(ProvenanceStore store,
            Object caller, StringBuffer procName, StringBuffer procRID, StringBuffer returnObjectName,
            Object returnObject, StringBuffer returnObjectRID, StringBuffer label,
            boolean procGeneratedReturn, boolean alwaysStackReturnID,
            boolean dontPopActivityID) {
        StopWatch.start(true);
        endProcedureDocumentation(store, caller, procName, procRID, returnObjectName,
                returnObject, returnObjectRID, label, procGeneratedReturn, alwaysStackReturnID,
                dontPopActivityID, false);
        StopWatch.stop(false);
    }

    public static void endProcedureDocumentation(boolean provOn, ProvenanceStore store,
            Object caller, StringBuffer procName, StringBuffer procRID, StringBuffer returnObjectName,
            Object returnObject, StringBuffer returnObjectRID, StringBuffer label,
            boolean procGeneratedReturn, boolean alwaysStackReturnID,
            boolean dontPopActivityID) {
        StopWatch.start(true);
        if (provOn) {
            endProcedureDocumentation(store, caller, procName, procRID, returnObjectName,
                    returnObject, returnObjectRID, label, procGeneratedReturn, alwaysStackReturnID,
                    dontPopActivityID, false);
        }
        StopWatch.stop(false);
    }

    public static void documentProcReturn(StringBuffer procName, StringBuffer procRID,
            Object returnObject, StringBuffer returnObjectRID, StringBuffer returnObjectName,
            ProvenanceStore store, boolean documentGenerationByProc,
            boolean alwaysStackReturnID, boolean ignoreGranularity) {
        StopWatch.start(true);
        // set entity stacking to false as a quickfix for false alternativeOf's resulting from no corresponding pop
        alwaysStackReturnID = false; // Remove this after instrumentation project has been fixed
        if (MASSProv.provOn && returnObject != null && (ignoreGranularity
                || granularityLevel() >= Granularity.RETURN.getValue())) {
            if (procName == null) {
                procName = new StringBuffer("anonymousProcedure");
            }
            ResourceMatcher matcher = ResourceMatcher.getMatcher();
            /* get the ID of the returned entity ready */
            if (returnObjectRID == null) { // not yet ready
                if (returnObjectName == null) { // has no name (e.g. created at return)
                    // use the proc name in the id (for human-readability)
                    returnObjectName = new StringBuffer(procName)
                            .append("_Return");
                } // now there is a name
                returnObjectRID = ProvUtils.getUniversalResourceID(
                        new StringBuffer().append(returnObjectName).
                                append("_Return"));
                // the RID didn't exist so assume it hasn't been documented
                store.addRelationalProv(returnObjectRID,
                        ProvOntology.getRDFTypeFullURIBuffer(),
                        ProvOntology.getEntityStartingPointClassFullURIBuffer());
            }

            // document the return object's value just prior to return
            store.addRelationalProv(returnObjectRID,
                    ProvOntology.getValueExpandedPropertyFullURIBuffer(),
                    new StringBuffer("\"").append(
                            isImmutable(returnObject) || returnObject instanceof Date
                            ? returnObject.toString()
                            : returnObject.hashCode())
                            .append("\""));
            if (documentGenerationByProc) {
                boolean procRIDisNull = procRID == null;
                if (procRID == null) {
                    procRID = ProvUtils.getUniversalResourceID(procName);
                }
                store.addRelationalProv(procRID,
                        ProvOntology.getGeneratedExpandedPropertyFullURIBuffer(),
                        returnObjectRID);
                long atTimeNS = System.nanoTime();
                // only record generation time if this documentation marks the entities generation
                if (procRIDisNull) {
                    store.addRelationalProv(returnObjectRID,
                            ProvOntology.getGeneratedAtTimeExpandedPropertyFullURIBuffer(),
                            new StringBuffer("\"").append(String.valueOf(atTimeNS)).append("\""));
                }
            }
            if (returnObject instanceof ProvEnabledObject) {
                store.addRelationalProv(returnObjectRID,
                        ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(),
                        new StringBuffer(((ProvEnabledObject) returnObject).getOwnerUUID()));
            }
            if (alwaysStackReturnID || ignoreGranularity || granularityLevel()
                    >= Granularity.LINE.getValue()) {
                matcher.pushEntityID(returnObjectRID);
            }
        }
        StopWatch.stop(true);
    }

    public static void documentProcReturn(boolean provOn, StringBuffer procName, StringBuffer procRID,
            Object returnObject, StringBuffer returnObjectRID, StringBuffer returnObjectName,
            ProvenanceStore store, boolean documentGenerationByProc,
            boolean alwaysStackReturnID, boolean ignoreGranularity) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            documentProcReturn(procName, procRID, returnObject, returnObjectRID,
                    returnObjectName, store, documentGenerationByProc,
                    alwaysStackReturnID, ignoreGranularity);
        }
    }

    public static void documentProcReturn(StringBuffer procName, StringBuffer procRID,
            Object returnObject, StringBuffer returnObjectRID, StringBuffer returnObjectName,
            ProvenanceStore store, boolean documentGenerationByProc,
            boolean alwaysStackReturnID) {
        StopWatch.start(true);
        StopWatch.stop(true);
        documentProcReturn(procName, procRID, returnObject, returnObjectRID, returnObjectName,
                store, documentGenerationByProc, alwaysStackReturnID, false);
    }

    public static void documentProcReturn(boolean provOn, StringBuffer procName, StringBuffer procRID,
            Object returnObject, StringBuffer returnObjectRID, StringBuffer returnObjectName,
            ProvenanceStore store, boolean documentGenerationByProc,
            boolean alwaysStackReturnID) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            documentProcReturn(procName, procRID, returnObject, returnObjectRID, returnObjectName,
                    store, documentGenerationByProc, alwaysStackReturnID, false);
        }
    }

    /**
     *
     * @param store
     * @param caller
     * @param callerRID
     * @param procName
     * @param procRID
     * @param entityName
     * @param entity
     * @param entityRID
     * @param alternativeOfRID
     * @param stackRID
     * @return
     */
    public static StringBuffer documentEntity(ProvenanceStore store, Object caller,
            StringBuffer callerRID, StringBuffer procName, StringBuffer procRID, StringBuffer entityName,
            Object entity, StringBuffer entityRID, StringBuffer alternativeOfRID,
            boolean stackRID) {
        StopWatch.start(true);
        if (MASSProv.provOn && store != null) {
            try {
                if (entityName == null) {
                    if (entity != null) {
                        entityName = new StringBuffer(entity.getClass().getSimpleName());
                    } else {
                        StringBuffer anonText
                                = new StringBuffer("anonymousVariable");
                        if (procName != null) {
                            anonText.append("Of");
                            if (caller != null) {
                                anonText.append(caller.getClass()
                                        .getSimpleName()).append('.');
                            }
                            anonText.append(procName);
                        }
                        entityName = anonText;
                    }
                }
                if (entityRID == null) {
                    entityRID = ProvUtils.getUniversalResourceID(entityName);
                }
                if (callerRID != null && caller != null) {
                    if (caller instanceof ProvEnabledObject) {
                        // get the ID from the caller
                        callerRID = new StringBuffer(((ProvEnabledObject) caller).getOwnerUUID());
                        // agent?
                        if (caller instanceof ProvEnabledAgent) {
                            // agents are treated as softwareAgents
                            // attribute the entity to the agent
                            store.addRelationalProv(entityRID,
                                    ProvOntology.getWasAttributedToStartingPointPropertyFullURIBuffer(),
                                    callerRID);
                        } else {
                            // places and messages are treated as collections
                            // document membership of this variable as part of
                            // collection regardless of its scope (could just as
                            // easily been an field value that was overwritten
                            // so it doesn't matter if this variable is local to
                            // the procedure
                            store.addRelationalProv(callerRID,
                                    ProvOntology.getHadMemberExpandedPropertyFullURIBuffer(),
                                    entityRID);
                        }
                    }
                }
                if (entity != null) {
                    StringBuffer value = isImmutable(entity) || entity instanceof Date
                            ? new StringBuffer(entity.toString()) : new StringBuffer(String.valueOf(entity.hashCode()));
                    documentEntity(store, entityRID, value);
                } else {
                    documentEntity(store, entityRID);
                }
                if (procRID != null) {
                    documentGeneration(store, entityRID, procRID);
                }
                if (alternativeOfRID != null) {
                    documentAlternativity(store, entityRID, alternativeOfRID);
                }
                if (stackRID) {
                    ResourceMatcher.getMatcher().pushEntityID(entityRID);
                }
            } catch (Exception e) {
                e.printStackTrace(IO.getLogWriter());
            }
        }
        StopWatch.stop(true);
        return entityRID;
    }

    public static StringBuffer documentEntity(boolean provOn, ProvenanceStore store, Object caller,
            StringBuffer callerRID, StringBuffer procName, StringBuffer procRID, StringBuffer entityName,
            Object entity, StringBuffer entityRID, StringBuffer alternativeOfRID,
            boolean stackRID) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            return documentEntity(store, caller, callerRID, procName, procRID,
                    entityName, entity, entityRID, alternativeOfRID, stackRID);
        }
        return null;
    }

    public static StringBuffer documentAgent(ProvenanceStore store, Object agent,
            StringBuffer agentRID, StringBuffer agentName, StringBuffer label) {
        StopWatch.start(true);
        agentRID = MASSProv.provOn ? documentAgent(store, agent, agentRID, agentName) : null;
        if (store != null && agentRID != null && label != null && label.length() > 0) {
            store.addRelationalProv(agentRID, ProvOntology.getRDFSLabelFullURIBuffer(), new StringBuffer("\"").append(label).append("\""));
        }
        StopWatch.stop(true);
        return agentRID;
    }

    public static StringBuffer documentAgent(boolean provOn, ProvenanceStore store, Object agent,
            StringBuffer agentRID, StringBuffer agentName, StringBuffer label) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            return documentAgent(store, agent, agentRID, agentName, label);
        }
        return null;
    }

    public static StringBuffer documentProvEnabledObject(ProvenanceStore store,
            Object provEnabledObject, StringBuffer provEnabledObjectRID,
            StringBuffer provEnabledObjectName, boolean objIsProvEnabled,
            StringBuffer label) {
        StopWatch.start(true);
        if (MASSProv.provOn && store != null) {
            if (provEnabledObjectRID == null) {
                if (provEnabledObject != null) {
                    objIsProvEnabled = provEnabledObject instanceof ProvEnabledObject;
                    provEnabledObjectRID = ProvUtils.getUniversalResourceID(
                            new StringBuffer(provEnabledObject.getClass().getSimpleName()));
                } else if (provEnabledObjectName != null) {
                    provEnabledObjectRID = ProvUtils.getUniversalResourceID(
                            provEnabledObjectName);
                } else {
                    provEnabledObjectRID = ProvUtils.getUniversalResourceID(
                            new StringBuffer("anonymous").append(provEnabledObjectName));
                }
            } // provEnabledObjectRID != null now
            if (objIsProvEnabled) {
                store.addRelationalProv(provEnabledObjectRID,
                        ProvOntology.getRDFTypeFullURIBuffer(),
                        ProvOntology.getCollectionExpandedClassFullURIBuffer());
            } else {
                store.addRelationalProv(provEnabledObjectRID,
                        ProvOntology.getRDFTypeFullURIBuffer(),
                        ProvOntology.getEntityStartingPointClassFullURIBuffer());
            }
            if (label != null) {
                store.addRelationalProv(provEnabledObjectRID,
                        ProvOntology.getRDFSLabelFullURIBuffer(),
                        new StringBuffer("\"").append(label).append("\""));
            }
        }
        StopWatch.stop(true);
        return provEnabledObjectRID;
    }

    public static StringBuffer documentProvEnabledObject(boolean provOn, ProvenanceStore store,
            Object provEnabledObject, StringBuffer provEnabledObjectRID,
            StringBuffer provEnabledObjectName, boolean objIsProvEnabled,
            StringBuffer label) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            return documentProvEnabledObject(store,
                    provEnabledObject, provEnabledObjectRID,
                    provEnabledObjectName, objIsProvEnabled,
                    label);
        }
        return null;
    }

    /**
     *
     * @param store
     * @param agent
     * @param agentName
     * @param agentRID
     * @return
     */
    public static StringBuffer documentAgent(ProvenanceStore store, Object agent,
            StringBuffer agentRID, StringBuffer agentName) {
        StopWatch.start(true);
        if (MASSProv.provOn) {
            if (agentRID == null) {
                if (agentName == null) {
                    agentName = new StringBuffer("anonymousAgent");
                }
                if (agent != null) {
                    agentRID = ProvUtils.getUniversalResourceID(
                            new StringBuffer(agent.getClass().getSimpleName()));
                } else {
                    agentRID = ProvUtils.getUniversalResourceID(agentName);
                }
            }
            store.addRelationalProv(agentRID, ProvOntology.getRDFTypeFullURIBuffer(),
                    ProvOntology.getSoftwareAgentExpandedClassFullURIBuffer());
        }
        StopWatch.stop(false);
        return agentRID;
    }

    public static StringBuffer documentAgent(boolean provOn, ProvenanceStore store, Object agent,
            StringBuffer agentRID, StringBuffer agentName) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            return documentAgent(store, agent, agentRID, agentName);
        }
        return null;
    }

    private static StringBuffer documentEntity(ProvenanceStore store,
            StringBuffer entityRID) {
        StopWatch.start(true);
        if (MASSProv.provOn && store != null) {
            store.addRelationalProv(entityRID,
                    ProvOntology.getRDFTypeFullURIBuffer(),
                    ProvOntology.getEntityStartingPointClassFullURIBuffer());
        }
        StopWatch.stop(true);
        return entityRID;
    }

    private static StringBuffer documentEntity(boolean provOn, ProvenanceStore store,
            StringBuffer entityRID) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            return documentEntity(store, entityRID);
        }
        return null;
    }

    private static void documentGeneration(ProvenanceStore store,
            StringBuffer entityRID, StringBuffer activityRID) {
        StopWatch.start(true);
        if (MASSProv.provOn && store != null) {
            store.addRelationalProv(activityRID,
                    ProvOntology.getGeneratedExpandedPropertyFullURIBuffer(),
                    entityRID);
            store.addRelationalProv(entityRID,
                    ProvOntology.getGeneratedAtTimeExpandedPropertyFullURIBuffer(),
                    new StringBuffer("\"").append(String.valueOf(System.nanoTime())).
                            append("\""));
        }
        StopWatch.stop(true);
    }

    private static void documentGeneration(boolean provOn, ProvenanceStore store,
            StringBuffer entityRID, StringBuffer activityRID) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            documentGeneration(store, entityRID, activityRID);
        }
    }

    private static void documentAlternativity(ProvenanceStore store,
            StringBuffer entityRID, StringBuffer alternativeOfRID) {
        StopWatch.start(true);
        if (MASSProv.provOn && store != null) {
            store.addRelationalProv(entityRID,
                    ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(),
                    alternativeOfRID);
        }
        StopWatch.stop(true);
    }

    private static void documentAlternativity(boolean provOn, ProvenanceStore store,
            StringBuffer entityRID, StringBuffer alternativeOfRID) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            documentAlternativity(store, entityRID, alternativeOfRID);
        }
    }

    private static void documentEntity(ProvenanceStore store, StringBuffer entityRID, StringBuffer value) {
        StopWatch.start(true);
        if (MASSProv.provOn && store != null) {
            documentEntity(store, entityRID);
            if (value != null) {
                store.addRelationalProv(entityRID,
                        ProvOntology.getValueExpandedPropertyFullURIBuffer(),
                        new StringBuffer("\"").append(value).append("\""));
            }
        }
        StopWatch.stop(true);
    }

    private static void documentEntity(boolean provOn, ProvenanceStore store, StringBuffer entityRID, StringBuffer value) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            documentEntity(store, entityRID, value);
        }
    }

    public static StringBuffer documentSimDriver(ProvenanceStore store,
            StringBuffer simulatorRID, StringBuffer driverRID, StringBuffer driverMethodName,
            StringBuffer label) {
        StopWatch.start(true);
        if (MASSProv.provOn && store != null) {
            // handle unspecified simulator
            if (simulatorRID == null) {
                StringBuffer simulatorName = new StringBuffer("UnknownMASSApp_");
                // make sure simulatorRID is ready
                if (MASSProv.isInitialized()) {
                    simulatorName = StoreManager.getAppNameBuffer();
                }
                // make the ID
                simulatorRID = ProvUtils.getUniversalResourceID(simulatorName);
                // assume simulator isn't documented yet... document it
                ProvenanceRecorder.documentAgent(store, null, simulatorRID,
                        simulatorName, new StringBuffer("Simulator"));
            }
            // handle unspecified driver method
            if (driverRID == null) {
                // handle unspecified driver method name
                if (driverMethodName == null) {
                    driverMethodName = new StringBuffer("unknownDriverMethod");
                }
                // get a resource identifier for the driver method
                driverRID = ProvUtils.getUniversalResourceID(driverMethodName);
                // assume that the driver has not been documented if RID is null
                store.addRelationalProv(driverRID,
                        ProvOntology.getRDFTypeFullURIBuffer(),
                        ProvOntology.getActivityQualifiedPropertyFullURIBuffer());
            }
            // assume that the driver procedure has not yet been associated with 
            // the simulator software
            // simulator started the driver activity
            store.addRelationalProv(driverRID,
                    ProvOntology.getWasStartedByExpandedPropertyFullURIBuffer(),
                    simulatorRID);
            // record start time
            store.addRelationalProv(driverRID,
                    ProvOntology.getAtTimeQualifiedPropertyFullURIBuffer(),
                    new StringBuffer("\"").
                            append(String.valueOf(System.currentTimeMillis())).
                            append("\""));
            // and startedAt time
            store.addRelationalProv(driverRID,
                    ProvOntology.getStartedAtTimeStartingPointPropertyFullURIBuffer(),
                    new StringBuffer("\"").
                            append(String.valueOf(System.nanoTime())).
                            append("\""));
            // record location
            store.addRelationalProv(driverRID,
                    ProvOntology.getAtLocationExpandedPropertyFullURIBuffer(),
                    ProvUtils.getHostNameBuffer());
            if (label != null) {
                store.addRelationalProv(driverRID,
                        ProvOntology.getRDFSLabelFullURIBuffer(), new StringBuffer("\"").append(label).append("\""));
            }
        }
        StopWatch.stop(true);
        return driverRID;
    }

    public static StringBuffer documentSimDriver(boolean provOn, ProvenanceStore store,
            StringBuffer simulatorRID, StringBuffer driverRID, StringBuffer driverMethodName,
            StringBuffer label) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            return documentSimDriver(store, simulatorRID, driverRID,
                    driverMethodName, label);
        }
        return null;
    }

    public static String[] documentCommandLineArguments(ProvenanceStore store,
            StringBuffer simulatorRID, String[] args, StringBuffer argsCollectionRID) {
        StopWatch.start(true);
        String[] argRIDs = null;
        if (MASSProv.provOn && store != null) {
            if (simulatorRID == null) {
                StringBuffer simulatorName = new StringBuffer("UnknownMASSApp_");
                // make sure simulatorRID is ready
                if (MASSProv.isInitialized()) {
                    simulatorName = StoreManager.getAppNameBuffer();
                }
                // make the ID
                simulatorRID = ProvUtils.getUniversalResourceID(simulatorName);
                // assume simulator isn't documented yet... document it
                ProvenanceRecorder.documentAgent(store, null, simulatorRID,
                        simulatorName, new StringBuffer("Simulator"));
            }
            // make sure the RID for the args collection is ready
            if (argsCollectionRID == null || argsCollectionRID.length() > 0) {
                argsCollectionRID = ProvUtils.getUniversalResourceID(new StringBuffer("args"));
            }
            // document args... specified argsCollectionRID does not necessarily
            // mean that args collection has been documented... assume RID was
            // generated outside of method for scoping purposes
            store.addRelationalProv(argsCollectionRID,
                    ProvOntology.getRDFTypeFullURIBuffer(),
                    ProvOntology.getCollectionExpandedClassFullURIBuffer());
            // document each argument
            // ingore a null args array
            if (args != null) {
                argRIDs = new String[args.length];
                StringBuffer argRID;
                // for each arg
                for (int i = 0, im = args.length; i < im; i++) {
                    // generate the arg RID
                    argRID = ProvUtils.getUniversalResourceID(new StringBuffer("arg").append(i));
                    // set the ID in the array of return IDs
                    argRIDs[i] = argRID.toString();
                    // document the arg and its value
                    ProvenanceRecorder.documentEntity(store, argRID, new StringBuffer(args[i]));
                    // add it to the collection
                    store.addRelationalProv(argsCollectionRID,
                            ProvOntology.getHadMemberExpandedPropertyFullURIBuffer(),
                            argRID);
                    // do not assume that the simulator used the arg though
                }
                // assume that the simulator used the collection if it exists
                store.addRelationalProv(simulatorRID,
                        ProvOntology.getUsedStartingPointPropertyFullURIBuffer(),
                        argsCollectionRID);
            }
        }
        StopWatch.stop(true);
        return argRIDs;
    }

    public static String[] documentCommandLineArguments(boolean provOn, ProvenanceStore store,
            StringBuffer simulatorRID, String[] args, StringBuffer argsCollectionRID) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            return documentCommandLineArguments(store, simulatorRID, args,
                    argsCollectionRID);
        }
        return null;
    }

    public static void documentFieldAccess(ProvenanceStore store,
            StringBuffer fieldName, StringBuffer fieldRID, StringBuffer procRID, Object accessed) {
        StopWatch.start(true);
        StopWatch.stop(true);
        documentFieldAccess(store, fieldName, fieldRID, procRID, accessed, false);
    }

    public static void documentFieldAccess(boolean provOn, ProvenanceStore store,
            StringBuffer fieldName, StringBuffer fieldRID, StringBuffer procRID, Object accessed) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            documentFieldAccess(store, fieldName, fieldRID, procRID, accessed,
                    false);
        }
    }

    public static void documentFieldAccess(ProvenanceStore store,
            StringBuffer fieldName, StringBuffer fieldRID, StringBuffer procRID, Object accessed,
            boolean ignoreGranularity) {
        StopWatch.start(true);
        if (MASSProv.provOn && store != null) {
            if (fieldName == null) { // ensure field name
                fieldName = new StringBuffer("anonymousField");
            }
            if (fieldRID == null) { // ensure field id
                fieldRID = ProvUtils.getUniversalResourceID(fieldName);
            }
            if (procRID == null) { // ensure proc id
                procRID = ProvUtils.getUniversalResourceID(new StringBuffer("anonymousProcedure"));
            }
            // rid for this read op
            StringBuffer fieldAccessRID = ProvUtils.getUniversalResourceID(
                    new StringBuffer().append(fieldName).append("_read"));
            // add activity
            store.addRelationalProv(fieldAccessRID, ProvOntology.getRDFTypeFullURIBuffer(),
                    ProvOntology.getActivityStartingPointClassFullURIBuffer());
            // read op started at
            store.addRelationalProv(fieldAccessRID,
                    ProvOntology.getStartedAtTimeStartingPointPropertyFullURIBuffer(),
                    new StringBuffer().append("\"").append(System.nanoTime()).
                            append("\""));
            // activity used the field
            store.addRelationalProv(fieldAccessRID,
                    ProvOntology.getUsedStartingPointPropertyFullURIBuffer(), fieldRID);

            //document value of entity read    
            StringBuffer value = new StringBuffer("\"");
            if (accessed == null) {
                value.append("null");
            } else {
                if (isImmutable(accessed) || accessed instanceof Date) {
                    value.append(accessed.toString());
                } else {
                    value.append(String.valueOf(accessed.hashCode()));
                }
            }
            value.append("\"");
            store.addRelationalProv(fieldRID,
                    ProvOntology.getValueExpandedPropertyFullURIBuffer(),
                    value);

            // connect with procedure where the access occured
            if (procRID != null) {
                // the read op influenced the proc
                store.addRelationalProv(fieldAccessRID,
                        ProvOntology.getInfluencedExpandedPropertyFullURIBuffer(),
                        procRID);
                // proc was influenced by the read op
                store.addRelationalProv(procRID,
                        ProvOntology.getWasInfluencedByQualifiedPropertyFullURIBuffer(),
                        fieldAccessRID);
            }
            // read op ended at
            store.addRelationalProv(fieldAccessRID,
                    ProvOntology.getEndedAtTimeStartingPointPropertyFullURIBuffer(),
                    new StringBuffer().append("\"").append(System.nanoTime()).
                            append("\""));
        }
        StopWatch.stop(true);
    }

    public static void documentFieldAccess(boolean provOn, ProvenanceStore store,
            StringBuffer fieldName, StringBuffer fieldRID, StringBuffer procRID, Object accessed,
            boolean ignoreGranularity) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            documentFieldAccess(store, fieldName, fieldRID, procRID, accessed,
                    ignoreGranularity);
        }
    }

    public static void documentFieldAssignment(ProvenanceStore store,
            StringBuffer fieldName, StringBuffer fieldRID, Object field, StringBuffer procRID) {
        StopWatch.start(true);
        StopWatch.stop(true);
        documentFieldAssignment(store, fieldName, fieldRID, field, procRID, false);
    }

    public static void documentFieldAssignment(boolean provOn, ProvenanceStore store,
            StringBuffer fieldName, StringBuffer fieldRID, Object field, StringBuffer procRID) {
        StopWatch.start(true);
        StopWatch.stop(true);
        if (provOn) {
            documentFieldAssignment(store, fieldName, fieldRID, field, procRID,
                    false);
        }
    }

    public static void documentFieldAssignment(ProvenanceStore store,
            StringBuffer fieldName, StringBuffer fieldRID, Object field, StringBuffer procRID,
            boolean ignoreGranularity) {
        StopWatch.start(true);
        if (MASSProv.provOn && store != null) {
            if (fieldName == null) { // ensure field name
                fieldName = new StringBuffer("anonymousField");
            }
            if (fieldRID == null) { // ensure field id
                fieldRID = ProvUtils.getUniversalResourceID(fieldName);
            }
            if (procRID == null) { // ensure proc id
                procRID = ProvUtils.getUniversalResourceID(new StringBuffer("anonymousProcedure"));
            }
            // id for this write op
            StringBuffer fieldAssignmentRID = ProvUtils.getUniversalResourceID(
                    new StringBuffer().append(fieldName).append("_write"));
            // add activity
            store.addRelationalProv(fieldAssignmentRID, ProvOntology.getRDFTypeFullURIBuffer(),
                    ProvOntology.getActivityStartingPointClassFullURIBuffer());
            // read op started at
            store.addRelationalProv(fieldAssignmentRID,
                    ProvOntology.getStartedAtTimeStartingPointPropertyFullURIBuffer(),
                    new StringBuffer().append("\"").append(System.nanoTime()).
                            append("\""));
            // id for the new data
            StringBuffer dataRID = ProvUtils.getUniversalResourceID(fieldName);
            // add data
            store.addRelationalProv(dataRID,
                    ProvOntology.getRDFTypeFullURIBuffer(),
                    ProvOntology.getEntityStartingPointClassFullURIBuffer());
            // activity generated the field
            store.addRelationalProv(fieldAssignmentRID,
                    ProvOntology.getGeneratedExpandedPropertyFullURIBuffer(), dataRID);

            //document value of entity       
            store.addRelationalProv(dataRID,
                    ProvOntology.getValueExpandedPropertyFullURIBuffer(),
                    new StringBuffer("\"").append(field.toString()).append("\""));

            // connect with procedure where the assignment occured
            if (procRID != null) {
                // the write op influenced the proc
                store.addRelationalProv(fieldAssignmentRID,
                        ProvOntology.getInfluencedExpandedPropertyFullURIBuffer(),
                        procRID);
                // proc was influenced by the write op
                store.addRelationalProv(procRID,
                        ProvOntology.getWasInfluencedByQualifiedPropertyFullURIBuffer(),
                        fieldAssignmentRID);
            }
            // associate the new data with the field
            store.addRelationalProv(dataRID, ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(), fieldRID);
            // and vice-versa... not vice-versa since the relationship is repeatedly overwritten
            // store.addRelationalProv(fieldRID, ProvOntology.getAlternateOfExpandedPropertyFullURIBuffer(), dataRID);
            // write op ended at
            store.addRelationalProv(fieldAssignmentRID,
                    ProvOntology.getEndedAtTimeStartingPointPropertyFullURIBuffer(),
                    new StringBuffer().append("\"").append(System.nanoTime()).
                            append("\""));
        }
        StopWatch.stop(true);
    }

    public static void documentFieldAssignment(boolean provOn, ProvenanceStore store,
            StringBuffer fieldName, StringBuffer fieldRID, Object field, StringBuffer procRID,
            boolean ignoreGranularity) {
        StopWatch.start(true);
        if (provOn) {
            documentFieldAssignment(store, fieldName, fieldRID, field, procRID,
                    ignoreGranularity);
        }
        StopWatch.stop(true);
    }

    public static int granularityLevel() {
        StopWatch.start(true);
        int level = 0;
        try {
            // safe: initializer on consts, but no singleton instantiation side-effects
            level = StoreManager.getGranularityLevel().getValue();
        } catch (Exception e) {
            e.printStackTrace(IO.getLogWriter());
        }
        StopWatch.stop(true);
        return level;
    }

    private static boolean isImmutable(Object param) {
        StopWatch.start(true);
        boolean isImmutable = false;
        try {
            isImmutable = param == null || param instanceof String
                    || param instanceof Byte || param instanceof Short
                    || param instanceof Integer || param instanceof Long
                    || param instanceof Float || param instanceof Double
                    || param instanceof Character || param instanceof Boolean;
        } catch (Exception e) {
            e.printStackTrace(IO.getLogWriter());
        }
        StopWatch.stop(true);
        return isImmutable;
    }

    public static void documentAgentVisitToPlace(ProvenanceStore store, Agent agent, Place place, String callingMethodRID) {
        if (store != null && agent instanceof ProvEnabledObject && agent.isProvOn()
                && place instanceof ProvEnabledObject) {
            String agentID = ((ProvEnabledObject) agent).getOwnerUUID();
            String placeID = ((ProvEnabledObject) place).getOwnerUUID();
            // document visit as activity
            String activityRID = ProvenanceRecorder.documentActivity(store, "visit", null);
            store.addRelationalProv(activityRID, ProvOntology.getRDFSLabelFullURI(), "AGENT_VISIT");
            // document visit used agent
            store.addRelationalProv(activityRID, ProvOntology.getUsedStartingPointPropertyFullURI(), agentID);
            // document visit used place
            store.addRelationalProv(activityRID, ProvOntology.getUsedStartingPointPropertyFullURI(), placeID);
            // document method invocation where/when the visit happened
            if (callingMethodRID != null) {
                store.addRelationalProv(activityRID, ProvOntology.getInfluenceQualifiedClassFullURI(), callingMethodRID);
            }
        }
    }

    private static String documentActivity(ProvenanceStore store, String activityName, String activityRID) {
        if (activityRID == null) {
            if (activityName != null) {
                activityRID = ProvUtils.getUniversalResourceID(activityName);
            } else {
                activityRID = ProvUtils.getUniversalResourceID("anonymousActivity");
            }
        }
        // add activity
        store.addRelationalProv(activityRID, ProvOntology.getRDFTypeFullURI(),
                ProvOntology.getActivityStartingPointClassFullURI());
        // add atLocation
        store.addRelationalProv(activityRID, ProvOntology.getAtLocationExpandedPropertyFullURI(),
                ProvUtils.getHostName());
        String time = String.valueOf(System.nanoTime());
        // add startedAtTime
        store.addRelationalProv(activityRID,
                ProvOntology.getStartedAtTimeStartingPointPropertyFullURI(),
                time);
        // add endedAtTime
        store.addRelationalProv(activityRID,
                ProvOntology.getEndedAtTimeStartingPointPropertyFullURI(),
                time);
        return activityRID;
    }
}
