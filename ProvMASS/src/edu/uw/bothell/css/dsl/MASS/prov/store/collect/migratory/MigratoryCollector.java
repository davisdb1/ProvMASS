package edu.uw.bothell.css.dsl.MASS.prov.store.collect.migratory;
// PERFORMANCE DOCUMENTED

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.store.StoreManager;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.ProvenanceCollector;
import static edu.uw.bothell.css.dsl.MASS.prov.store.collect.ProvenanceCollector.prospectiveCollectionMethodSuffix;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Implements a provenance collection strategy, in which, the provenance store
 * is maintained with the mobile code that owns it, when that mobile code is
 * transferred to another machine.
 *
 * @author Delmar B. Davis
 */
public class MigratoryCollector implements ProvenanceCollector, Serializable {

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
//        // release store since it is going to move with its owner
//        StoreManager sm = StoreManager.getStoreManager();
//        sm.releaseStore(store);
        StopWatch.stop(true);
    }

    @Override
    public void completeTransfer(ProvenanceStore store, Object owner) {
        StopWatch.start(true);
//        StoreManager sm = StoreManager.getStoreManager();
//        sm.adoptForeignStore(store, owner);
        StopWatch.stop(true);
    }
}
