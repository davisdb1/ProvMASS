package edu.uw.bothell.css.dsl.MASS.prov.store.collect.stationary;
// PERFORMANCE DOCUMENTED

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledAgent;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.store.StoreManager;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.ProvenanceCollector;
import static edu.uw.bothell.css.dsl.MASS.prov.store.collect.ProvenanceCollector.prospectiveCollectionMethodSuffix;
import static edu.uw.bothell.css.dsl.MASS.prov.store.collect.ProvenanceCollector.retrospectiveCollectionMethodSuffix;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Implements a provenance collection strategy, in which, the provenance within
 * the provenance store is maintained with the mobile code that owns it, when
 * that mobile code is transferred to another machine.
 *
 * @author Delmar B. Davis
 */
public class StationaryCollector implements ProvenanceCollector, Serializable {

    @Override
    public void beginCollection(ProvenanceStore provenanceStore,
            String className, String methodName, Object[] arguments) {
        StopWatch.start(true);
        try {
            ProvUtils.collect(prospectiveCollectionMethodSuffix, className,
                    methodName, provenanceStore, arguments);
        } catch (Exception ex) {
            ex.printStackTrace(IO.getLogWriter());
        }
        StopWatch.stop(true);
    }

    @Override
    public void finishCollection(ProvenanceStore provenanceStore,
            String className, String methodName,
            HashMap<String, Object> entities, Object[] arguments) {
        StopWatch.start(true);
        try {
            Object[] args = {provenanceStore, arguments, entities};
            ProvUtils.collect(retrospectiveCollectionMethodSuffix, className,
                    methodName, provenanceStore, entities, args);
        } catch (Exception ex) {
            ex.printStackTrace(IO.getLogWriter());
        }
        StopWatch.stop(true);
    }

    @Override
    public void transfer(ProvenanceStore store) {
        StopWatch.start(true);
//        //orphan the current store on this node
//        StoreManager sm = StoreManager.getStoreManager();
//        Object owner = sm.getOwner(store);
//        if (owner instanceof ProvEnabledAgent) {
//            ProvEnabledAgent provEnabledAgent = (ProvEnabledAgent) owner;
//            provEnabledAgent.storeTransitiveProvenanceState(store.getHostsThatOwnerWasPersistentlyIdentifiedAt());
//        }
//        sm.orphanStore(store);
        StopWatch.stop(true);
    }

    @Override
    public void completeTransfer(ProvenanceStore store, Object owner) {
        StopWatch.start(true);
        if (owner instanceof ProvEnabledAgent) {
//            try {
//                ProvenanceStore newStore = new ProvenanceStore(owner);
//                ProvEnabledAgent provEnabledAgent = (ProvEnabledAgent) owner;
//                String uuid = provEnabledAgent.getOwnerUUID();
//                newStore.setOwnerUUID(uuid);
//                provEnabledAgent.setStore(newStore);
//                Object stateData = provEnabledAgent.retrieveTransitiveProvenanceState();
//                if (stateData instanceof HashSet) {
//                    try {
//                        HashSet<String> hostsPersistentlyIdentifiedAt = (HashSet<String>) stateData;
//                        newStore.setHostsThatOwnerWasPersistentlyIdentifiedAt(hostsPersistentlyIdentifiedAt);
//                    } catch (ClassCastException e) {
//                        e.printStackTrace(IO.getLogWriter());
//                    }
//                }
//            } catch (Exception e) {
//
//            }
        }
        StopWatch.stop(true);
    }
}
