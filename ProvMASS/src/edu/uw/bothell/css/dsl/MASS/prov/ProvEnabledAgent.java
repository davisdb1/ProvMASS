package edu.uw.bothell.css.dsl.MASS.prov;

/**
 * Allows agents to be treated as provenance carrying agents
 *
 * @author Delmar B. Davis
 */
public interface ProvEnabledAgent extends ProvEnabledObject {

    public void storeTransitiveProvenanceState(Object stateData);

    public Object retrieveTransitiveProvenanceState();
}
