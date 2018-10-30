package edu.uw.bothell.css.dsl.MASS.prov.store.collect;

import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import java.util.HashMap;

/**
 * Collectors implementing this interface gather provenance at the beginning and
 * end of each function called from the MASS library. More importantly each
 * collector deals with owner reassignment prior to transfer (e.g. agent
 * transferred to a new MThread), and provenance store retention at each owner.
 *
 * @author Delmar B. Davis
 */
public interface ProvenanceCollector {

    static Class[] beginCollectionParamTypes = {ProvenanceStore.class,
        Object[].class};
    static Class[] finishCollectionParamTypes = {ProvenanceStore.class,
        Object[].class, HashMap.class};
    static String prospectiveCollectionMethodSuffix = "_prospective";
    static String retrospectiveCollectionMethodSuffix = "_retrospective";

    /**
     * Collects prospective provenance for a specific class and method.
     *
     * @param provenanceStore - storage facility for the provenance data
     * @param className - name of the class that this method call came from.
     * Determines specific provenance collection class to use.
     * @param methodName - name of the method that made this method call.
     * Determines the method within the specific provenance collection class to
     * call.
     * @param arguments - arguments from the method that called this method
     */
    public void beginCollection(ProvenanceStore provenanceStore,
            String className, String methodName, Object[] arguments);

    /**
     * Collects prospective provenance for a specific class and method.
     *
     * @param provenanceStore - storage facility for the provenance data
     * @param className - name of the class that this method call came from.
     * Determines specific provenance collection class to use.
     * @param methodName - name of the method that made this method call.
     * Determines the method within the specific provenance collection class to
     * call.
     * @param arguments - arguments from the method that called this method
     * @param entities
     */
    public void finishCollection(ProvenanceStore provenanceStore,
            String className, String methodName,
            HashMap<String, Object> entities, Object[] arguments);

    /**
     * Handles the transfer of provenance store ownership between objects across
     * a network boundary. This affects the owner referenced by the store, the
     * directory key/value pairs within the provenance store manager, and the
     * store referenced by one, both, or neither of the owners (depending on the
     * collection strategy implemented)
     *
     * Note: This method can be called when transfer of ownership should not
     * occur, by setting one or both of the owners to null. <em>Do not</em> pass
     * in one object as both owners. Correct behavior will be maintained, but
     * performance will be reduced.
     *
     * @param store
     */
    public void transfer(ProvenanceStore store);

    /**
     * Handles the transfer of provenance store ownership between objects across
     * a network boundary. The incoming provenance store is dealt with as if it
     * had been constructed, registered, manipulated, unregistered, and finally,
     * registered with the store manager once again in a used state. Buffer slot
     * references from within the store replace those in the slots assigned
     * within the master buffer of the StoreManager.
     *
     * @param store - The provenance store being adopted within the local
     * StoreManager
     * @param owner - The object which has possession of the store (multiple
     * objects may reference a single store, but only one is registered as the
     * its owner)
     */
    public void completeTransfer(ProvenanceStore store, Object owner);
}
