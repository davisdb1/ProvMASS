package edu.uw.bothell.css.dsl.MASS.prov.store.collect;

import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Turns provenance collection off. The collect and transfer methods will still
 * be called, but nothing will happen.
 *
 * @author Delmar B. Davis
 */
public class NoProvenanceCollectionCollector implements ProvenanceCollector, Serializable {

    @Override
    public void beginCollection(ProvenanceStore provenanceStore,
            String className, String methodName, Object[] arguments) {
        // do nothing
    }

    @Override
    public void finishCollection(ProvenanceStore provenanceStore,
            String className, String methodName,
            HashMap<String, Object> entities, Object[] arguments) {
        // do nothing
    }

    @Override
    public void transfer(ProvenanceStore store) {
        // do nothing
    }

    @Override
    public void completeTransfer(ProvenanceStore store, Object owner) {
        // do nothing
    }
}
