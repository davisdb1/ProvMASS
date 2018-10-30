package edu.uw.bothell.css.dsl.MASS.prov.QuickStart.provcollection;
// PERFORMANCE DOCUMENTED

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.store.collect.ProvenanceCollector;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Collects provenance for the TestDriver MASS Application
 *
 * @author Delmar B. Davis
 */
public class TestDriverProvenanceCollector implements ProvenanceCollector, Serializable {

    public TestDriverProvenanceCollector() {
    }

    @Override
    public void beginCollection(ProvenanceStore provenanceStore,
            String className, String methodName, Object[] arguments) {
        StopWatch.start(true);
        try {
            ProvUtils.collect(prospectiveCollectionMethodSuffix, className,
                    methodName, provenanceStore, (Object[]) arguments);
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
            ProvUtils.collect(retrospectiveCollectionMethodSuffix, className,
                    methodName, provenanceStore, entities, arguments);
        } catch (Exception ex) {
            ex.printStackTrace(IO.getLogWriter());
        }
        StopWatch.stop(true);
    }

    @Override
    public void transfer(ProvenanceStore store) {
        StopWatch.start(true);
        StopWatch.stop(true);
        throw new UnsupportedOperationException("Not supported, as this class is stationary");

    }

    @Override
    public void completeTransfer(ProvenanceStore store, Object owner) {
        StopWatch.start(true);
        StopWatch.stop(true);
        throw new UnsupportedOperationException("Not supported, as this class is stationary");
    }
}
