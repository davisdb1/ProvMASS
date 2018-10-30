package edu.uw.bothell.css.dsl.MASS.prov;

import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;

/**
 * Allows all objects that have a provenance store to be reasoned about and have
 * their stores operated on using their unique identifier
 *
 * @author Delmar B. Davis
 */
public interface ProvEnabledObject {

    /**
     * Provides the universally unique identifier associated with the resource
     * that describes this provenance enabled object
     *
     * @return A Java UUID with the class name of the object
     */
    public String getOwnerUUID();

    /**
     * Provides the instance-specific provenance store
     *
     * @return A provenance store with this object as its owner and also
     * referenced by the object
     */
    public ProvenanceStore getStore();

    /**
     * Sets the instance specific provenance store
     *
     * @param provenanceStore
     */
    public void setStore(ProvenanceStore provenanceStore);

    /**
     * maps new objects for provenance capture with filters meant to select
     * specific objects for capture while filtering out others
     */
    public void mapProvenanceCapture();

    /**
     * Runs documentation procedures for construction event after mapping
     * through a filter
     */
    public void substituteConstructorDocumentation();
}
