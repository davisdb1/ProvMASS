package edu.uw.bothell.css.dsl.MASS.prov;

/**
 * Provides provenance definition URIs for building provenance RDF models.
 *
 * NOTE: This class was designed to provide static functions and fields.
 *
 * @author Delmar B. Davis
 */
public class ProvOntology {

    /* Namespaces */
    // see: RDF-CONCEPTS @ http://www.w3.org/TR/prov-o/#bib-RDF-CONCEPTS
    private static final String RDF_NS
            = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    // see: RDF-CONCEPTS @ http://www.w3.org/TR/prov-o/#bib-RDF-CONCEPTS
    private static final String RDFS_NS
            = "http://www.w3.org/2000/01/rdf-schema#";
    // see: XMLSCHEMA11-2 @ http://www.w3.org/TR/prov-o/#bib-XMLSCHEMA11-2
    private static final String XSD_NS = "http://www.w3.org/2000/10/XMLSchema#";
    // see: OWL2-OVERVIEW @ http://www.w3.org/TR/prov-o/#bib-OWL2-OVERVIEW
    private static final String OWL_NS = "http://www.w3.org/2002/07/owl#";
    // PROV-DM @ http://www.w3.org/TR/prov-o/#bib-PROV-DM
    private static final String PROV_NS = "http://www.w3.org/ns/prov#";
    // see: FOAF Vocabulary Specification @ http://xmlns.com/foaf/spec/#term_account
    private static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";

    /* Prefixes (used for abbreviating above namespaces in output)*/
    private static final String RDF_PREFIX = "rdf:";
    private static final String RDFS_PREFIX = "rdfs:";
    private static final String XSD_PREFIX = "xsd:";
    private static final String OWL_PREFIX = "owl:";
    private static final String PROV_PREFIX = "prov:";
    private static final String FOAF_PREFIX = "foaf:";

    /* RDF Property (limited to those commonly used with prov) */
    // see: http://www.w3.org/1999/02/22-rdf-syntax-ns#type
    // see also (about equivelence to 'a'): 
    //      https://www.w3.org/TR/turtle/#iri-a
    private static final String RDF_TYPE_PROPERTY = "type";
    // see: http://www.w3.org/2000/01/rdf-schema#label
    private static final String RDFS_LABEL_PROPERTY = "label";


    /* Starting Point Classes */
    // see: http://www.w3.org/TR/prov-o/#Entity
    private static final String ENTITY = "Entity";
    // see: http://www.w3.org/TR/prov-o/#Activity
    private static final String ACTIVITY = "Activity";
    // see: http://www.w3.org/TR/prov-o/#Agent
    private static final String AGENT = "Agent";

    /* Starting Point Properties */
    // see: http://www.w3.org/TR/prov-o/#wasGeneratedBy
    private static final String WAS_GENERATED_BY = "wasGeneratedBy";
    // see: http://www.w3.org/TR/prov-o/#wasDerivedFrom
    private static final String WAS_DERIVED_FROM = "wasDerivedFrom";
    // see: http://www.w3.org/TR/prov-o/#wasAttributedTo
    private static final String WAS_ATTRIBUTED_TO = "wasAttributedTo";
    // see: http://www.w3.org/TR/prov-o/#startedAtTime
    private static final String STARTED_AT_TIME = "startedAtTime";
    // see: http://www.w3.org/TR/prov-o/#used
    private static final String USED = "used";
    // see: http://www.w3.org/TR/prov-o/#wasInformedBy
    private static final String WAS_INFORMED_BY = "wasInformedBy";
    // see: http://www.w3.org/TR/prov-o/#endedAtTime
    private static final String ENDED_AT_TIME = "endedAtTime";
    // see: http://www.w3.org/TR/prov-o/#wasAssociatedWith
    private static final String WAS_ASSOCIATED_WITH = "wasAssociatedWith";
    // see: http://www.w3.org/TR/prov-o/#actedOnBehalfOf
    private static final String ACTED_ON_BEHALF_OF = "actedOnBehalfOf";

    /* Expaded Classes */
    // see: http://www.w3.org/TR/prov-o/#Collection
    private static final String COLLECTION = "Collection";
    // see: http://www.w3.org/TR/prov-o/#EmptyCollection
    private static final String EMPTY_COLLECTION = "EmptyCollection";
    // see: http://www.w3.org/TR/prov-o/#Bundle
    private static final String BUNDLE = "Bundle";
    // see: http://www.w3.org/TR/prov-o/#Person
    private static final String PERSON = "Person";
    // see: http://www.w3.org/TR/prov-o/#SoftwareAgent
    private static final String SOFTWARE_AGENT = "SoftwareAgent";
    // see: http://www.w3.org/TR/prov-o/#Organization
    private static final String ORGANIZATION = "Organization";
    // see: http://www.w3.org/TR/prov-o/#Location
    private static final String LOCATION = "Location";

    /* Expanded Properties */
    // see: http://www.w3.org/TR/prov-o/#alternateOf
    private static final String ALTERNATE_OF = "alternateOf";
    // see: http://www.w3.org/TR/prov-o/#specializationOf
    private static final String SPECIALIZATION_OF = "specializationOf";
    // see: http://www.w3.org/TR/prov-o/#generatedAtTime
    private static final String GENERATED_AT_TIME = "generatedAtTime";
    // see: http://www.w3.org/TR/prov-o/#hadPrimarySource
    private static final String HAD_PRIMARY_SOURCE = "hadPrimarySource";
    // see: http://www.w3.org/TR/prov-o/#value
    private static final String VALUE = "value";
    // see: http://www.w3.org/TR/prov-o/#wasQuotedFrom
    private static final String WAS_QUOTED_FROM = "wasQuotedFrom";
    // see: http://www.w3.org/TR/prov-o/#wasRevisionOf
    private static final String WAS_REVISION_OF = "wasRevisionOf";
    // see: http://www.w3.org/TR/prov-o/#invalidatedAtTime
    private static final String INVALIDATED_AT_TIME = "invalidatedAtTime";
    // see: http://www.w3.org/TR/prov-o/#wasInvalidatedBy
    private static final String WAS_INVALIDATED_BY = "wasInvalidatedBy";
    // see: http://www.w3.org/TR/prov-o/#hadMember
    private static final String HAD_MEMBER = "hadMember";
    // see: http://www.w3.org/TR/prov-o/#wasStartedBy
    private static final String WAS_STARTED_BY = "wasStartedBy";
    // see: http://www.w3.org/TR/prov-o/#wasEndedBy
    private static final String WAS_ENDED_BY = "wasEndedBy";
    // see: http://www.w3.org/TR/prov-o/#invalidated
    private static final String INVALIDATED = "invalidated";
    // see: http://www.w3.org/TR/prov-o/#influenced
    private static final String INFLUENCED = "influenced";
    // see: http://www.w3.org/TR/prov-o/#atLocation
    private static final String AT_LOCATION = "atLocation";
    // see: http://www.w3.org/TR/prov-o/#generated
    private static final String GENERATED = "generated";

    /* Qualified Classes */
    // see: http://www.w3.org/TR/prov-o/#Influence 
    private static final String INFLUENCE = "Influence";
    // see: http://www.w3.org/TR/prov-o/#EntityInfluence 
    private static final String ENTITY_INFLUENCE = "EntityInfluence";
    // see: http://www.w3.org/TR/prov-o/#Usage  
    private static final String USAGE = "Usage";
    // see: http://www.w3.org/TR/prov-o/#Start   
    private static final String START = "Start";
    // see: http://www.w3.org/TR/prov-o/#End   
    private static final String END = "End";
    // see: http://www.w3.org/TR/prov-o/#Derivation   
    private static final String DERIVATION = "Derivation";
    // see: http://www.w3.org/TR/prov-o/#PrimarySource  
    private static final String PRIMARY_SOURCE = "PrimarySource";
    // see: http://www.w3.org/TR/prov-o/#Quotation   
    private static final String QUOTATION = "Quotation";
    // see: http://www.w3.org/TR/prov-o/#Revision   
    private static final String REVISION = "Revision";
    // see: http://www.w3.org/TR/prov-o/#ActivityInfluence   
    private static final String ACTIVITY_INFLUENCE = "ActivityInfluence";
    // see: http://www.w3.org/TR/prov-o/#Generation  
    private static final String GENERATION = "Generation";
    // see: http://www.w3.org/TR/prov-o/#Communication   
    private static final String COMMUNICATION = "Communication";
    // see: http://www.w3.org/TR/prov-o/#Invalidation   
    private static final String INVALIDATION = "Invalidation";
    // see: http://www.w3.org/TR/prov-o/#AgentInfluence   
    private static final String AGENT_INFLUENCE = "AgentInfluence";
    // see: http://www.w3.org/TR/prov-o/#Attribution   
    private static final String ATTRIBUTION = "Attribution";
    // see: http://www.w3.org/TR/prov-o/#Association   
    private static final String ASSOCIATION = "Association";
    // see: http://www.w3.org/TR/prov-o/#Plan   
    private static final String PLAN = "Plan";
    // see: http://www.w3.org/TR/prov-o/#Delegation   
    private static final String DELEGATION = "Delegation";
    // see: http://www.w3.org/TR/prov-o/#InstantaneousEvent   
    private static final String INSTANTATANEOUS_EVENT = "InstantaneousEvent";
    // see: http://www.w3.org/TR/prov-o/#Role 
    private static final String ROLE = "Role";

    /* Qualified Properties */
    // see: http://www.w3.org/TR/prov-o/#wasInfluencedBy
    private static final String WAS_INFLUENCED_BY = "wasInfluencedBy";
    // see: http://www.w3.org/TR/prov-o/#qualifiedInfluence
    private static final String QUALIFIED_INFLUENCE = "qualifiedInfluence";
    // see: http://www.w3.org/TR/prov-o/#qualifiedGeneration 
    private static final String QUALIFIED_GENERATION = "qualifiedGeneration";
    // see: http://www.w3.org/TR/prov-o/#qualifiedDerivation 
    private static final String QUALIFIED_DERIVATION = "qualifiedDerivation";
    // see: http://www.w3.org/TR/prov-o/#qualifiedPrimarySource 
    private static final String QUALIFIED_PRIMARY_SOURCE
            = "qualifiedPrimarySource";
    // see: http://www.w3.org/TR/prov-o/#qualifiedQuotation 
    private static final String QUALIFIED_QUOTATION = "qualifiedQuotation";
    // see: http://www.w3.org/TR/prov-o/#qualifiedRevision
    private static final String QUALIFIED_REVISION = "qualifiedRevision";
    // see: http://www.w3.org/TR/prov-o/#qualifiedAttribution 
    private static final String QAULIFIED_ATTRIBUTION = "qualifiedAttribution";
    // see: http://www.w3.org/TR/prov-o/#qualifiedInvalidation 
    private static final String QAULIFIED_INVALIDATION = "qualifiedInvalidation";
    // see: http://www.w3.org/TR/prov-o/#qualifiedStart 
    private static final String QUALIFIED_START = "qualifiedStart";
    // see: http://www.w3.org/TR/prov-o/#qualifiedUsage 
    private static final String QUALIFIED_USAGE = "qualifiedUsage";
    // see: http://www.w3.org/TR/prov-o/#qualifiedCommunication 
    private static final String QUALIFIED_COMMUNICATION
            = "qualifiedCommunication";
    // see: http://www.w3.org/TR/prov-o/#qualifiedAssociation 
    private static final String QUALIFIED_ASSOCIATION = "qualifiedAssociation";
    // see: http://www.w3.org/TR/prov-o/#qualifiedEnd
    private static final String QUALIFIED_END = "qualifiedEnd";
    // see: http://www.w3.org/TR/prov-o/#qualifiedDelegation 
    private static final String QUALIFIED_DELEGATION = "qualifiedDelegation";
    // see: http://www.w3.org/TR/prov-o/#influencer 
    private static final String INFLUENCER = "influencer";
    // see: http://www.w3.org/TR/prov-o/#entity 
    private static final String ENTITY_QUALIFIED_ = "entity";
    // see: http://www.w3.org/TR/prov-o/#hadUsage 
    private static final String HAD_USAGE = "hadUsage";
    // see: http://www.w3.org/TR/prov-o/#hadGeneration 
    private static final String HAD_GENERATION = "hadGeneration";
    // see: http://www.w3.org/TR/prov-o/#activity 
    private static final String ACTIVITY_QUALIFIED = "activity";
    // see: http://www.w3.org/TR/prov-o/#agent 
    private static final String AGENT_QUALIFIED = "agent";
    // see: http://www.w3.org/TR/prov-o/#hadPlan 
    private static final String HAD_PLAN = "hadPlan";
    // see: http://www.w3.org/TR/prov-o/#hadActivity 
    private static final String HAD_ACTIVITY = "hadActivity";
    // see: http://www.w3.org/TR/prov-o/#atTime 
    private static final String AT_TIME = "atTime";
    // see: http://www.w3.org/TR/prov-o/#hadRole
    private static final String HAD_ROLE = "hadRole";

    /* FOAF Qualified Properties */
    private static final String NAME = "name";

    // translations from prov predicate to english sentence-part
    private static final String RDF_TYPE_TRANSLATION_BEFORE_CONSONATE = "is a";
    private static final String RDF_TYPE_TRANSLATION_BEFORE_VOWEL = "is an";
    private static final String AT_TIME_TRANSLATION = "happened on";
    private static final String ASSOCIATION_TRANSLATION = "was responsible for";
    private static final String WAS_ASSOCIATED_WITH_TRANSLATION = "was associated with";
    private static final String RDFS_LABEL_TRANSLATION = "is labeled as";
    private static final String WAS_DERIVED_FROM_TRANSLATION = "was derived from";
    private static final String ENDED_AT_TIME_TRANSLATION = "ended at";
    private static final String STARTED_AT_TIME_TRANSLATION = "started at";
    private static final String USED_TRANSLATION = "used";
    private static final String GENERATED_TRANSLATION = "generated";
    private static final String WAS_GENERATED_BY_TRANSLATION = "was generated by";

    /* Namespace Getters */
    // see: RDF-CONCEPTS @ http://www.w3.org/TR/prov-o/#bib-RDF-CONCEPTS
    public static String getRDFNameSpaceURI() {
        return RDF_NS;
    }

    // see: RDF-CONCEPTS @ http://www.w3.org/TR/prov-o/#bib-RDF-CONCEPTS
    public static String getRDFSNameSpaceURI() {
        return RDFS_NS;
    }

    // see: XMLSCHEMA11-2 @ http://www.w3.org/TR/prov-o/#bib-XMLSCHEMA11-2
    public static String getXSDNameSpaceURI() {
        return XSD_NS;
    }

    // see: OWL2-OVERVIEW @ http://www.w3.org/TR/prov-o/#bib-OWL2-OVERVIEW
    public static String getOWLNameSpaceURI() {
        return OWL_NS;
    }

    // PROV-DM @ http://www.w3.org/TR/prov-o/#bib-PROV-DM
    public static String getPROVNameSpaceURI() {
        return PROV_NS;
    }

    /**
     * Provides prefix for abbreviating RDF namespace in model output
     *
     * @return prefix for abbreviating RDF namespace in model output
     */
    public static String getRDFPrefix() {
        return RDF_PREFIX;
    }

    /**
     * Provides prefix for abbreviating XSD namespace in model output
     *
     * @return prefix for abbreviating XSD namespace in model output
     */
    public static String getXSDPrefix() {
        return XSD_PREFIX;
    }

    /**
     * Provides prefix for abbreviating OWL namespace in model output
     *
     * @return prefix for abbreviating OWL namespace in model output
     */
    public static String getOWLPrefix() {
        return OWL_PREFIX;
    }

    /**
     * Provides prefix for abbreviating PROV namespace in model output
     *
     * @return prefix for abbreviating PROV namespace in model output
     */
    public static String getProvPrefix() {
        return PROV_PREFIX;
    }

    /* RDF Property Getter (limited to those commonly used with prov) */
    public static String getRDFTypePrefixedURI() {
        return RDFS_PREFIX + RDF_TYPE_PROPERTY;
    }

    /* RDF Property Getter (limited to those commonly used with prov) */
    public static String getRDFTypeFullURI() {
        return RDFS_NS + RDF_TYPE_PROPERTY;
    }

    /* RDFS Property Getter (limited to those commonly used with prov) */
    public static String getRDFSLabelPrefixedURI() {
        return RDFS_PREFIX + RDFS_LABEL_PROPERTY;
    }

    /* RDFS Property Getter (limited to those commonly used with prov) */
    public static String getRDFSLabelFullURI() {
        return RDFS_NS + RDFS_LABEL_PROPERTY;
    }

    /* Starting Point Classes */
    // see: http://www.w3.org/TR/prov-o/#Entity
    public static String getEntityStartingPointClassPrefixedURI() {
        return PROV_PREFIX + ENTITY;
    }

    // see: http://www.w3.org/TR/prov-o/#Entity
    public static String getEntityStartingPointClassFullURI() {
        return PROV_NS + ENTITY;
    }

    // see: http://www.w3.org/TR/prov-o/#Activity
    public static String getActivityStartingPointClassPrefixedURI() {
        return PROV_PREFIX + ACTIVITY;
    }

    // see: http://www.w3.org/TR/prov-o/#Activity
    public static String getActivityStartingPointClassFullURI() {
        return PROV_NS + ACTIVITY;
    }

    // see: http://www.w3.org/TR/prov-o/#Agent
    public static String getAgentStartingPointClassPrefixedURI() {
        return PROV_PREFIX + AGENT;
    }

    // see: http://www.w3.org/TR/prov-o/#Agent
    public static String getAgentStartingPointClassFullURI() {
        return PROV_NS + AGENT;
    }

    /* Starting Point Properties */
    // see: http://www.w3.org/TR/prov-o/#wasGeneratedBy
    public static String getWasGeneratedByStartingPointPropertyPrefixedURI() {
        return PROV_PREFIX + WAS_GENERATED_BY;
    }

    // see: http://www.w3.org/TR/prov-o/#wasGeneratedBy
    public static String getWasGeneratedByStartingPointPropertyFullURI() {
        return PROV_NS + WAS_GENERATED_BY;
    }

    // see: http://www.w3.org/TR/prov-o/#wasDerivedFrom
    public static String getWasDerivedFromStartingPointPropertyPrefixedURI() {
        return PROV_PREFIX + WAS_DERIVED_FROM;
    }

    // see: http://www.w3.org/TR/prov-o/#wasDerivedFrom
    public static String getWasDerivedFromStartingPointPropertyFullURI() {
        return PROV_NS + WAS_DERIVED_FROM;
    }

    // see: http://www.w3.org/TR/prov-o/#wasAttributedTo
    public static String getWasAttributedToStartingPointPropertyPrefixedURI() {
        return PROV_PREFIX + WAS_ATTRIBUTED_TO;
    }

    // see: http://www.w3.org/TR/prov-o/#wasAttributedTo
    public static String getWasAttributedToStartingPointPropertyFullURI() {
        return PROV_NS + WAS_ATTRIBUTED_TO;
    }

    // see: http://www.w3.org/TR/prov-o/#startedAtTime
    public static String getStartedAtTimeStartingPointPropertyPrefixedURI() {
        return PROV_PREFIX + STARTED_AT_TIME;
    }

    // see: http://www.w3.org/TR/prov-o/#startedAtTime
    public static String getStartedAtTimeStartingPointPropertyFullURI() {
        return PROV_NS + STARTED_AT_TIME;
    }

    // see: http://www.w3.org/TR/prov-o/#used
    public static String getUsedStartingPointPropertyPrefixedURI() {
        return PROV_PREFIX + USED;
    }

    // see: http://www.w3.org/TR/prov-o/#used
    public static String getUsedStartingPointPropertyFullURI() {
        return PROV_NS + USED;
    }

    // see: http://www.w3.org/TR/prov-o/#wasInformedBy
    public static String getWasInformedByStartingPointPropertyPrefixedURI() {
        return PROV_PREFIX + WAS_INFORMED_BY;
    }

    // see: http://www.w3.org/TR/prov-o/#wasInformedBy
    public static String getWasInformedByStartingPointPropertyFullURI() {
        return PROV_NS + WAS_INFORMED_BY;
    }

    // see: http://www.w3.org/TR/prov-o/#endedAtTime
    public static String getEndedAtTimeStartingPointPropertyPrefixedURI() {
        return PROV_PREFIX + ENDED_AT_TIME;
    }

    // see: http://www.w3.org/TR/prov-o/#endedAtTime
    public static String getEndedAtTimeStartingPointPropertyFullURI() {
        return PROV_NS + ENDED_AT_TIME;
    }

    // see: http://www.w3.org/TR/prov-o/#wasAssociatedWith
    public static String getWasAssociatedWithStartingPointPropertyPrefixedURI() {
        return PROV_PREFIX + WAS_ASSOCIATED_WITH;
    }

    // see: http://www.w3.org/TR/prov-o/#wasAssociatedWith
    public static String getWasAssociatedWithStartingPointPropertyFullURI() {
        return PROV_NS + WAS_ASSOCIATED_WITH;
    }

    // see: http://www.w3.org/TR/prov-o/#actedOnBehalfOf
    public static String getActedOnBehalfOfStartingPointPropertyPrefixedURI() {
        return PROV_PREFIX + ACTED_ON_BEHALF_OF;
    }

    // see: http://www.w3.org/TR/prov-o/#actedOnBehalfOf
    public static String getActedOnBehalfOfStartingPointPropertyFullURI() {
        return PROV_NS + ACTED_ON_BEHALF_OF;
    }

    /* Expaded Classes */
    // see: http://www.w3.org/TR/prov-o/#Collection
    public static String getCollectionExpandedClassPrefixedURI() {
        return PROV_PREFIX + COLLECTION;
    }

    // see: http://www.w3.org/TR/prov-o/#Collection
    public static String getCollectionExpandedClassFullURI() {
        return PROV_NS + COLLECTION;
    }

    // see: http://www.w3.org/TR/prov-o/#EmptyCollection
    public static String getEmptyCollectionExpandedClassPrefixedURI() {
        return PROV_PREFIX + EMPTY_COLLECTION;
    }

    // see: http://www.w3.org/TR/prov-o/#EmptyCollection
    public static String getEmptyCollectionExpandedClassFullURI() {
        return PROV_NS + EMPTY_COLLECTION;
    }

    // see: http://www.w3.org/TR/prov-o/#Bundle
    public static String getBundleExpandedClassPrefixedURI() {
        return PROV_PREFIX + BUNDLE;
    }

    // see: http://www.w3.org/TR/prov-o/#Bundle
    public static String getBundleExpandedClassFullURI() {
        return PROV_NS + BUNDLE;
    }

    // see: http://www.w3.org/TR/prov-o/#Person
    public static String getPersonExpandedClassPrefixedURI() {
        return PROV_PREFIX + PERSON;
    }

    // see: http://www.w3.org/TR/prov-o/#Person
    public static String getPersonExpandedClassFullURI() {
        return PROV_NS + PERSON;
    }

    public static String getNamePrefixedURI() {
        return FOAF_PREFIX + NAME;
    }

    public static String getNameFullURI() {
        return FOAF_NS + NAME;
    }

    // see: http://www.w3.org/TR/prov-o/#SoftwareAgent
    public static String getSoftwareAgentExpandedClassPrefixedURI() {
        return PROV_PREFIX + SOFTWARE_AGENT;
    }

    // see: http://www.w3.org/TR/prov-o/#SoftwareAgent
    public static String getSoftwareAgentExpandedClassFullURI() {
        return PROV_NS + SOFTWARE_AGENT;
    }

    // see: http://www.w3.org/TR/prov-o/#Organization
    public static String getOrganizationExpandedClassPrefixedURI() {
        return PROV_PREFIX + ORGANIZATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Organization
    public static String getOrganizationExpandedClassFullURI() {
        return PROV_NS + ORGANIZATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Location
    public static String getLocationExpandedClassPrefixedURI() {
        return PROV_PREFIX + LOCATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Location
    public static String getLocationExpandedClassFullURI() {
        return PROV_NS + LOCATION;
    }

    /* Expanded Properties */
    // see: http://www.w3.org/TR/prov-o/#alternateOf
    public static String getAlternateOfExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + ALTERNATE_OF;
    }

    // see: http://www.w3.org/TR/prov-o/#alternateOf
    public static String getAlternateOfExpandedPropertyFullURI() {
        return PROV_NS + ALTERNATE_OF;
    }

    // see: http://www.w3.org/TR/prov-o/#specializationOf
    public static String getSpecializationOfExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + SPECIALIZATION_OF;
    }

    // see: http://www.w3.org/TR/prov-o/#specializationOf
    public static String getSpecializationOfExpandedPropertyFullURI() {
        return PROV_NS + SPECIALIZATION_OF;
    }

    // see: http://www.w3.org/TR/prov-o/#generatedAtTime
    public static String getGeneratedAtTimeExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + GENERATED_AT_TIME;
    }

    // see: http://www.w3.org/TR/prov-o/#generatedAtTime
    public static String getGeneratedAtTimeExpandedPropertyFullURI() {
        return PROV_NS + GENERATED_AT_TIME;
    }

    // see: http://www.w3.org/TR/prov-o/#hadPrimarySource
    public static String getHadPrimarySourceExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + HAD_PRIMARY_SOURCE;
    }

    // see: http://www.w3.org/TR/prov-o/#hadPrimarySource
    public static String getHadPrimarySourceExpandedPropertyFullURI() {
        return PROV_NS + HAD_PRIMARY_SOURCE;
    }

    // see: http://www.w3.org/TR/prov-o/#value
    public static String getValueExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + VALUE;
    }

    // see: http://www.w3.org/TR/prov-o/#value
    public static String getValueExpandedPropertyFullURI() {
        return PROV_NS + VALUE;
    }

    // see: http://www.w3.org/TR/prov-o/#wasQuotedFrom
    public static String getWasQuotedFromExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + WAS_QUOTED_FROM;
    }

    // see: http://www.w3.org/TR/prov-o/#wasQuotedFrom
    public static String getWasQuotedFromExpandedPropertyFullURI() {
        return PROV_NS + WAS_QUOTED_FROM;
    }

    // see: http://www.w3.org/TR/prov-o/#wasRevisionOf
    public static String getWasRevisionOfExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + WAS_REVISION_OF;
    }

    // see: http://www.w3.org/TR/prov-o/#wasRevisionOf
    public static String getWasRevisionOfExpandedPropertyFullURI() {
        return PROV_NS + WAS_REVISION_OF;
    }

    // see: http://www.w3.org/TR/prov-o/#invalidatedAtTime
    public static String getInvalidatedAtTimeExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + INVALIDATED_AT_TIME;
    }

    // see: http://www.w3.org/TR/prov-o/#invalidatedAtTime
    public static String getInvalidatedAtTimeExpandedPropertyFullURI() {
        return PROV_NS + INVALIDATED_AT_TIME;
    }

    // see: http://www.w3.org/TR/prov-o/#wasInvalidatedBy
    public static String getWasInvalidatedByExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + WAS_INVALIDATED_BY;
    }

    // see: http://www.w3.org/TR/prov-o/#wasInvalidatedBy
    public static String getWasInvalidatedByExpandedPropertyFullURI() {
        return PROV_NS + WAS_INVALIDATED_BY;
    }

    // see: http://www.w3.org/TR/prov-o/#hadMember
    public static String getHadMemberExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + HAD_MEMBER;
    }

    // see: http://www.w3.org/TR/prov-o/#hadMember
    public static String getHadMemberExpandedPropertyFullURI() {
        return PROV_NS + HAD_MEMBER;
    }

    // see: http://www.w3.org/TR/prov-o/#wasStartedBy
    public static String getWasStartedByExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + WAS_STARTED_BY;
    }

    // see: http://www.w3.org/TR/prov-o/#wasStartedBy
    public static String getWasStartedByExpandedPropertyFullURI() {
        return PROV_NS + WAS_STARTED_BY;
    }

    // see: http://www.w3.org/TR/prov-o/#wasEndedBy
    public static String getWasEndedByExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + WAS_ENDED_BY;
    }

    // see: http://www.w3.org/TR/prov-o/#wasEndedBy
    public static String getWasEndedByExpandedPropertyFullURI() {
        return PROV_NS + WAS_ENDED_BY;
    }

    // see: http://www.w3.org/TR/prov-o/#invalidated
    public static String getInvalidatedExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + INVALIDATED;
    }

    // see: http://www.w3.org/TR/prov-o/#invalidated
    public static String getInvalidatedExpandedPropertyFullURI() {
        return PROV_NS + INVALIDATED;
    }

    // see: http://www.w3.org/TR/prov-o/#influenced
    public static String getInfluencedExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + INFLUENCED;
    }

    // see: http://www.w3.org/TR/prov-o/#influenced
    public static String getInfluencedExpandedPropertyFullURI() {
        return PROV_NS + INFLUENCED;
    }

    // see: http://www.w3.org/TR/prov-o/#atLocation
    public static String getAtLocationExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + AT_LOCATION;
    }

    // see: http://www.w3.org/TR/prov-o/#atLocation
    public static String getAtLocationExpandedPropertyFullURI() {
        return PROV_NS + AT_LOCATION;
    }

    // see: http://www.w3.org/TR/prov-o/#generated
    public static String getGeneratedExpandedPropertyPrefixedURI() {
        return PROV_PREFIX + GENERATED;
    }

    // see: http://www.w3.org/TR/prov-o/#generated
    public static String getGeneratedExpandedPropertyFullURI() {
        return PROV_NS + GENERATED;
    }

    /* Qualified Classes */
    // see: http://www.w3.org/TR/prov-o/#Influence 
    public static String getInfluenceQualifiedClassPrefixedURI() {
        return PROV_PREFIX + INFLUENCE;
    }

    // see: http://www.w3.org/TR/prov-o/#Influence
    public static String getInfluenceQualifiedClassFullURI() {
        return PROV_NS + INFLUENCE;
    }

    // see: http://www.w3.org/TR/prov-o/#EntityInfluence 
    public static String getEntityInfluenceQualifiedClassPrefixedURI() {
        return PROV_PREFIX + ENTITY_INFLUENCE;
    }

    // see: http://www.w3.org/TR/prov-o/#EntityInfluence
    public static String getEntityInfluenceQualifiedClassFullURI() {
        return PROV_NS + ENTITY_INFLUENCE;
    }

    // see: http://www.w3.org/TR/prov-o/#Usage  
    public static String getUsageQualifiedClassPrefixedURI() {
        return PROV_PREFIX + USAGE;
    }

    // see: http://www.w3.org/TR/prov-o/#Usage
    public static String getUsageQualifiedClassFullURI() {
        return PROV_NS + USAGE;
    }

    // see: http://www.w3.org/TR/prov-o/#Start   
    public static String getStartQualifiedClassPrefixedURI() {
        return PROV_PREFIX + START;
    }

    // see: http://www.w3.org/TR/prov-o/#Start
    public static String getStartQualifiedClassFullURI() {
        return PROV_NS + START;
    }

    // see: http://www.w3.org/TR/prov-o/#End   
    public static String getEndQualifiedClassPrefixedURI() {
        return PROV_PREFIX + END;
    }

    // see: http://www.w3.org/TR/prov-o/#End
    public static String getEndQualifiedClassFullURI() {
        return PROV_NS + END;
    }

    // see: http://www.w3.org/TR/prov-o/#Derivation   
    public static String getDerivationQualifiedClassPrefixedURI() {
        return PROV_PREFIX + DERIVATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Derivation
    public static String getDerivationQualifiedClassFullURI() {
        return PROV_NS + DERIVATION;
    }

    // see: http://www.w3.org/TR/prov-o/#PrimarySource  
    public static String getPrimarySourceQualifiedClassPrefixedURI() {
        return PROV_PREFIX + PRIMARY_SOURCE;
    }

    // see: http://www.w3.org/TR/prov-o/#PrimarySource
    public static String getPrimarySourceQualifiedClassFullURI() {
        return PROV_NS + PRIMARY_SOURCE;
    }

    // see: http://www.w3.org/TR/prov-o/#Quotation   
    public static String getQuotationQualifiedClassPrefixedURI() {
        return PROV_PREFIX + QUOTATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Quotation 
    public static String getQuotationQualifiedClassFullURI() {
        return PROV_NS + QUOTATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Revision   
    public static String getRevisionQualifiedClassPrefixedURI() {
        return PROV_PREFIX + REVISION;
    }

    // see: http://www.w3.org/TR/prov-o/#Revision
    public static String getRevisionQualifiedClassFullURI() {
        return PROV_NS + REVISION;
    }

    // see: http://www.w3.org/TR/prov-o/#ActivityInfluence   
    public static String getActivityInfluenceQualifiedClassPrefixedURI() {
        return PROV_PREFIX + ACTIVITY_INFLUENCE;
    }

    // see: http://www.w3.org/TR/prov-o/#ActivityInfluence
    public static String getActivityInfluenceQualifiedClassFullURI() {
        return PROV_NS + ACTIVITY_INFLUENCE;
    }

    // see: http://www.w3.org/TR/prov-o/#Generation  
    public static String getGenerationQualifiedClassPrefixedURI() {
        return PROV_PREFIX + GENERATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Generation
    public static String getGenerationQualifiedClassFullURI() {
        return PROV_NS + GENERATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Communication   
    public static String getCommunicationQualifiedClassPrefixedURI() {
        return PROV_PREFIX + COMMUNICATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Communication
    public static String getCommunicationQualifiedClassFullURI() {
        return PROV_NS + COMMUNICATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Invalidation   
    public static String getInvalidationQualifiedClassPrefixedURI() {
        return PROV_PREFIX + INVALIDATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Invalidation
    public static String getInvalidationQualifiedClassFullURI() {
        return PROV_NS + INVALIDATION;
    }

    // see: http://www.w3.org/TR/prov-o/#AgentInfluence   
    public static String getAgentInfluenceQualifiedClassPrefixedURI() {
        return PROV_PREFIX + AGENT_INFLUENCE;
    }

    // see: http://www.w3.org/TR/prov-o/#AgentInfluence
    public static String getAgentInfluenceQualifiedClassFullURI() {
        return PROV_NS + AGENT_INFLUENCE;
    }

    // see: http://www.w3.org/TR/prov-o/#Attribution   
    public static String getAttributionQualifiedClassPrefixedURI() {
        return PROV_PREFIX + ATTRIBUTION;
    }

    // see: http://www.w3.org/TR/prov-o/#Attribution
    public static String getAttributionQualifiedClassFullURI() {
        return PROV_NS + ATTRIBUTION;
    }

    // see: http://www.w3.org/TR/prov-o/#Association   
    public static String getAssociationQualifiedClassPrefixedURI() {
        return PROV_PREFIX + ASSOCIATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Association
    public static String getAssociationQualifiedClassFullURI() {
        return PROV_NS + ASSOCIATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Plan   
    public static String getPlanQualifiedClassPrefixedURI() {
        return PROV_PREFIX + PLAN;
    }

    // see: http://www.w3.org/TR/prov-o/#Plan
    public static String getPlanQualifiedClassFullURI() {
        return PROV_NS + PLAN;
    }

    // see: http://www.w3.org/TR/prov-o/#Delegation   
    public static String getDelegationQualifiedClassPrefixedURI() {
        return PROV_PREFIX + DELEGATION;
    }

    // see: http://www.w3.org/TR/prov-o/#Delegation
    public static String getDelegationQualifiedClassFullURI() {
        return PROV_NS + DELEGATION;
    }

    // see: http://www.w3.org/TR/prov-o/#InstantaneousEvent   
    public static String getInstantaneousEventQualifiedClassPrefixedURI() {
        return PROV_PREFIX + INSTANTATANEOUS_EVENT;
    }

    // see: http://www.w3.org/TR/prov-o/#InstantaneousEvent
    public static String getInstantaneousEventQualifiedClassFullURI() {
        return PROV_NS + INSTANTATANEOUS_EVENT;
    }

    // see: http://www.w3.org/TR/prov-o/#Role 
    public static String getRoleQualifiedClassPrefixedURI() {
        return PROV_PREFIX + ROLE;
    }

    // see: http://www.w3.org/TR/prov-o/#Role
    public static String getRoleQualifiedClassFullURI() {
        return PROV_NS + ROLE;
    }

    /* Qualified Properties */
    // see: http://www.w3.org/TR/prov-o/#wasInfluencedBy
    public static String getWasInfluencedByQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + WAS_INFLUENCED_BY;
    }

    // see: http://www.w3.org/TR/prov-o/#wasInfluencedBy
    public static String getWasInfluencedByQualifiedPropertyFullURI() {
        return PROV_NS + WAS_INFLUENCED_BY;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedInfluence
    public static String getQualifiedInfluenceQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QUALIFIED_INFLUENCE;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedInfluence
    public static String getQualifiedInfluenceQualifiedPropertyFullURI() {
        return PROV_NS + QUALIFIED_INFLUENCE;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedGeneration 
    public static String getQualifiedGenerationQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QUALIFIED_GENERATION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedGeneration
    public static String getQualifiedGenerationQualifiedPropertyFullURI() {
        return PROV_NS + QUALIFIED_GENERATION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedDerivation 
    public static String getQualifiedDerivationQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QUALIFIED_DERIVATION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedDerivation
    public static String getQualifiedDerivationQualifiedPropertyFullURI() {
        return PROV_NS + QUALIFIED_DERIVATION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedPrimarySource 
    public static String getQualifiedPrimarySourceQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QUALIFIED_PRIMARY_SOURCE;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedPrimarySource
    public static String getQualifiedPrimarySourceQualifiedPropertyFullURI() {
        return PROV_NS + QUALIFIED_PRIMARY_SOURCE;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedQuotation 
    public static String getQualifiedQuotationQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QUALIFIED_QUOTATION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedQuotation
    public static String getQualifiedQuotationQualifiedPropertyFullURI() {
        return PROV_NS + QUALIFIED_QUOTATION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedRevision
    public static String getQualifiedRevisionQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QUALIFIED_REVISION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedRevision
    public static String getQualifiedRevisionQualifiedPropertyFullURI() {
        return PROV_NS + QUALIFIED_REVISION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedAttribution 
    public static String getQualifiedAttributionQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QAULIFIED_ATTRIBUTION;
    }

    public static String getQualifiedAttributionQualifiedPropertyFullURI() {
        return PROV_NS + QAULIFIED_ATTRIBUTION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedInvalidation 
    public static String getQualifiedInvalidationQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QAULIFIED_INVALIDATION;
    }

    public static String getQualifiedInvalidationQualifiedPropertyFullURI() {
        return PROV_NS + QAULIFIED_INVALIDATION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedStart 
    public static String getQualifiedStartQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QUALIFIED_START;
    }

    public static String getQualifiedStartQualifiedPropertyFullURI() {
        return PROV_NS + QUALIFIED_START;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedUsage 
    public static String getQualifiedUsageQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QUALIFIED_USAGE;
    }

    public static String getQualifiedUsageQualifiedPropertyFullURI() {
        return PROV_NS + QUALIFIED_USAGE;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedCommunication 
    public static String getQualifiedCommunicationQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QUALIFIED_COMMUNICATION;
    }

    public static String getQualifiedCommunicationQualifiedPropertyFullURI() {
        return PROV_NS + QUALIFIED_COMMUNICATION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedAssociation 
    public static String getQualifiedAssociationQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QUALIFIED_ASSOCIATION;
    }

    public static String getQualifiedAssociationQualifiedPropertyFullURI() {
        return PROV_NS + QUALIFIED_ASSOCIATION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedEnd
    public static String getQualifiedEndQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QUALIFIED_END;
    }

    public static String getQualifiedEndQualifiedPropertyFullURI() {
        return PROV_NS + QUALIFIED_END;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedDelegation 
    public static String getQualifiedDelegationQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + QUALIFIED_DELEGATION;
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedDelegation
    public static String getQualifiedDelegationQualifiedPropertyFullURI() {
        return PROV_NS + QUALIFIED_DELEGATION;
    }

    // see: http://www.w3.org/TR/prov-o/#influencer 
    public static String getInfluencerQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + INFLUENCER;
    }

    // see: http://www.w3.org/TR/prov-o/#influencer 
    public static String getInfluencerQualifiedPropertyFullURI() {
        return PROV_NS + INFLUENCER;
    }

    // see: http://www.w3.org/TR/prov-o/#entity 
    public static String getEntityQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + ENTITY_QUALIFIED_;
    }

    // see: http://www.w3.org/TR/prov-o/#entity
    public static String getEntityQualifiedPropertyFullURI() {
        return PROV_NS + ENTITY_QUALIFIED_;
    }

    // see: http://www.w3.org/TR/prov-o/#hadUsage 
    public static String getHadUsageQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + HAD_USAGE;
    }

    // see: http://www.w3.org/TR/prov-o/#hadUsage
    public static String getHadUsageQualifiedPropertyFullURI() {
        return PROV_NS + HAD_USAGE;
    }

    // see: http://www.w3.org/TR/prov-o/#hadGeneration 
    public static String getHadGenerationQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + HAD_GENERATION;
    }

    // see: http://www.w3.org/TR/prov-o/#hadGeneration
    public static String getHadGenerationQualifiedPropertyFullURI() {
        return PROV_NS + HAD_GENERATION;
    }

    // see: http://www.w3.org/TR/prov-o/#activity 
    public static String getActivityQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + ACTIVITY_QUALIFIED;
    }

    // see: http://www.w3.org/TR/prov-o/#activity
    public static String getActivityQualifiedPropertyFullURI() {
        return ACTIVITY_QUALIFIED;
    }

    // see: http://www.w3.org/TR/prov-o/#agent 
    public static String getAgentQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + AGENT_QUALIFIED;
    }

    // see: http://www.w3.org/TR/prov-o/#agent 
    public static String getAgentQualifiedPropertyFullURI() {
        return PROV_NS + AGENT_QUALIFIED;
    }

    // see: http://www.w3.org/TR/prov-o/#hadPlan 
    public static String getHadPlanQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + HAD_PLAN;
    }

    // see: http://www.w3.org/TR/prov-o/#hadPlan 
    public static String getHadPlanQualifiedPropertyFullURI() {
        return PROV_NS + HAD_PLAN;
    }

    // see: http://www.w3.org/TR/prov-o/#hadActivity 
    public static String getHadActivityQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + HAD_ACTIVITY;
    }

    // see: http://www.w3.org/TR/prov-o/#hadActivity 
    public static String getHadActivityQualifiedPropertyFullURI() {
        return PROV_NS + HAD_ACTIVITY;
    }

    // see: http://www.w3.org/TR/prov-o/#atTime 
    public static String getAtTimeQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + AT_TIME;
    }

    // see: http://www.w3.org/TR/prov-o/#atTime 
    public static String getAtTimeQualifiedPropertyFullURI() {
        return PROV_NS + AT_TIME;
    }

    // see: http://www.w3.org/TR/prov-o/#hadRole
    public static String getHadRoleQualifiedPropertyPrefixedURI() {
        return PROV_PREFIX + HAD_ROLE;
    }

    // see: http://www.w3.org/TR/prov-o/#hadRole
    public static String getHadRoleQualifiedPropertyFullURI() {
        return PROV_NS + HAD_ROLE;
    }

    /**
     * Provides a human-readable sentence-part for the predicate of a provenance
     * triple
     *
     * @param uri - The PROV-O URI for the predicate
     * @param objectStartWithVowel - Indicates whether or not the object of the
     * triple starts with a vowel
     * @return Human readable text describing the relationship between a subject
     * and object, based on the PROV-O predicate
     */
    public static String translatePredicate(String uri, boolean objectStartWithVowel) {
        String translation = uri;
        if (uri.equals(ProvOntology.getRDFTypeFullURI())) {
            if (!objectStartWithVowel) {
                translation = RDF_TYPE_TRANSLATION_BEFORE_CONSONATE;
            } else {
                translation = RDF_TYPE_TRANSLATION_BEFORE_VOWEL;
            }
        } else if (uri.equals(ProvOntology.getAtTimeQualifiedPropertyFullURI())) {
            translation = AT_TIME_TRANSLATION;
        } else if (uri.equals(ProvOntology.getAssociationQualifiedClassFullURI())) {
            translation = ASSOCIATION_TRANSLATION;
        } else if (uri.equals(ProvOntology.getWasAssociatedWithStartingPointPropertyFullURI())) {
            translation = WAS_ASSOCIATED_WITH_TRANSLATION;
        } else if (uri.equals(ProvOntology.getRDFSLabelFullURI())) {
            translation = RDFS_LABEL_TRANSLATION;
        } else if (uri.equals(ProvOntology.getWasDerivedFromStartingPointPropertyFullURI())) {
            translation = WAS_DERIVED_FROM_TRANSLATION;
        } else if (uri.equals(ProvOntology.getEndedAtTimeStartingPointPropertyFullURI())) {
            translation = ENDED_AT_TIME_TRANSLATION;
        } else if (uri.equals(ProvOntology.getStartedAtTimeStartingPointPropertyFullURI())) {
            translation = STARTED_AT_TIME_TRANSLATION;
        } else if (uri.equals(ProvOntology.getUsedStartingPointPropertyFullURI())) {
            translation = USED_TRANSLATION;
        } else if (uri.equals(ProvOntology.getGeneratedExpandedPropertyFullURI())) {
            translation = GENERATED_TRANSLATION;
        } else if (uri.equals(ProvOntology.getWasGeneratedByStartingPointPropertyFullURI())) {
            translation = WAS_GENERATED_BY_TRANSLATION;
        }
        return translation;
    }


    /* Namespaces */
    // see: RDF-CONCEPTS @ http://www.w3.org/TR/prov-o/#bib-RDF-CONCEPTS
    private static final StringBuffer RDF_NS_BUFFER
            = new StringBuffer("http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    // see: RDF-CONCEPTS @ http://www.w3.org/TR/prov-o/#bib-RDF-CONCEPTS
    private static final StringBuffer RDFS_NS_BUFFER
            = new StringBuffer("http://www.w3.org/2000/01/rdf-schema#");
    // see: XMLSCHEMA11-2 @ http://www.w3.org/TR/prov-o/#bib-XMLSCHEMA11-2
    private static final StringBuffer XSD_NS_BUFFER = new StringBuffer("http://www.w3.org/2000/10/XMLSchema#");
    // see: OWL2-OVERVIEW @ http://www.w3.org/TR/prov-o/#bib-OWL2-OVERVIEW
    private static final StringBuffer OWL_NS_BUFFER = new StringBuffer("http://www.w3.org/2002/07/owl#");
    // PROV-DM @ http://www.w3.org/TR/prov-o/#bib-PROV-DM
    private static final StringBuffer PROV_NS_BUFFER = new StringBuffer("http://www.w3.org/ns/prov#");
    // see: FOAF Vocabulary Specification @ http://xmlns.com/foaf/spec/#term_account
    private static final StringBuffer FOAF_NS_BUFFER = new StringBuffer("http://xmlns.com/foaf/0.1/");

    /* Prefixes (used for abbreviating above namespaces in output)*/
    private static final StringBuffer RDF_PREFIX_BUFFER = new StringBuffer("rdf:");
    private static final StringBuffer RDFS_PREFIX_BUFFER = new StringBuffer("rdfs:");
    private static final StringBuffer XSD_PREFIX_BUFFER = new StringBuffer("xsd:");
    private static final StringBuffer OWL_PREFIX_BUFFER = new StringBuffer("owl:");
    private static final StringBuffer PROV_PREFIX_BUFFER = new StringBuffer("prov:");
    private static final StringBuffer FOAF_PREFIX_BUFFER = new StringBuffer("foaf:");

    /* RDF Property (limited to those commonly used with prov) */
    // see: http://www.w3.org/1999/02/22-rdf-syntax-ns#type
    // see also (about equivelence to 'a'): 
    //      https://www.w3.org/TR/turtle/#iri-a
    private static final StringBuffer RDF_TYPE_PROPERTY_BUFFER = new StringBuffer("type");
    // see: http://www.w3.org/2000/01/rdf-schema#label
    private static final StringBuffer RDFS_LABEL_PROPERTY_BUFFER = new StringBuffer("label");


    /* Starting Point Classes */
    // see: http://www.w3.org/TR/prov-o/#Entity
    private static final StringBuffer ENTITY_BUFFER = new StringBuffer("Entity");
    // see: http://www.w3.org/TR/prov-o/#Activity
    private static final StringBuffer ACTIVITY_BUFFER = new StringBuffer("Activity");
    // see: http://www.w3.org/TR/prov-o/#Agent
    private static final StringBuffer AGENT_BUFFER = new StringBuffer("Agent");

    /* Starting Point Properties */
    // see: http://www.w3.org/TR/prov-o/#wasGeneratedBy
    private static final StringBuffer WAS_GENERATED_BY_BUFFER = new StringBuffer("wasGeneratedBy");
    // see: http://www.w3.org/TR/prov-o/#wasDerivedFrom
    private static final StringBuffer WAS_DERIVED_FROM_BUFFER = new StringBuffer("wasDerivedFrom");
    // see: http://www.w3.org/TR/prov-o/#wasAttributedTo
    private static final StringBuffer WAS_ATTRIBUTED_TO_BUFFER = new StringBuffer("wasAttributedTo");
    // see: http://www.w3.org/TR/prov-o/#startedAtTime
    private static final StringBuffer STARTED_AT_TIME_BUFFER = new StringBuffer("startedAtTime");
    // see: http://www.w3.org/TR/prov-o/#used
    private static final StringBuffer USED_BUFFER = new StringBuffer("used");
    // see: http://www.w3.org/TR/prov-o/#wasInformedBy
    private static final StringBuffer WAS_INFORMED_BY_BUFFER = new StringBuffer("wasInformedBy");
    // see: http://www.w3.org/TR/prov-o/#endedAtTime
    private static final StringBuffer ENDED_AT_TIME_BUFFER = new StringBuffer("endedAtTime");
    // see: http://www.w3.org/TR/prov-o/#wasAssociatedWith
    private static final StringBuffer WAS_ASSOCIATED_WITH_BUFFER = new StringBuffer("wasAssociatedWith");
    // see: http://www.w3.org/TR/prov-o/#actedOnBehalfOf
    private static final StringBuffer ACTED_ON_BEHALF_OF_BUFFER = new StringBuffer("actedOnBehalfOf");

    /* Expaded Classes */
    // see: http://www.w3.org/TR/prov-o/#Collection
    private static final StringBuffer COLLECTION_BUFFER = new StringBuffer("Collection");
    // see: http://www.w3.org/TR/prov-o/#EmptyCollection
    private static final StringBuffer EMPTY_COLLECTION_BUFFER = new StringBuffer("EmptyCollection");
    // see: http://www.w3.org/TR/prov-o/#Bundle
    private static final StringBuffer BUNDLE_BUFFER = new StringBuffer("Bundle");
    // see: http://www.w3.org/TR/prov-o/#Person
    private static final StringBuffer PERSON_BUFFER = new StringBuffer("Person");
    // see: http://www.w3.org/TR/prov-o/#SoftwareAgent
    private static final StringBuffer SOFTWARE_AGENT_BUFFER = new StringBuffer("SoftwareAgent");
    // see: http://www.w3.org/TR/prov-o/#Organization
    private static final StringBuffer ORGANIZATION_BUFFER = new StringBuffer("Organization");
    // see: http://www.w3.org/TR/prov-o/#Location
    private static final StringBuffer LOCATION_BUFFER = new StringBuffer("Location");

    /* Expanded Properties */
    // see: http://www.w3.org/TR/prov-o/#alternateOf
    private static final StringBuffer ALTERNATE_OF_BUFFER = new StringBuffer("alternateOf");
    // see: http://www.w3.org/TR/prov-o/#specializationOf
    private static final StringBuffer SPECIALIZATION_OF_BUFFER = new StringBuffer("specializationOf");
    // see: http://www.w3.org/TR/prov-o/#generatedAtTime
    private static final StringBuffer GENERATED_AT_TIME_BUFFER = new StringBuffer("generatedAtTime");
    // see: http://www.w3.org/TR/prov-o/#hadPrimarySource
    private static final StringBuffer HAD_PRIMARY_SOURCE_BUFFER = new StringBuffer("hadPrimarySource");
    // see: http://www.w3.org/TR/prov-o/#value
    private static final StringBuffer VALUE_BUFFER = new StringBuffer("value");
    // see: http://www.w3.org/TR/prov-o/#wasQuotedFrom
    private static final StringBuffer WAS_QUOTED_FROM_BUFFER = new StringBuffer("wasQuotedFrom");
    // see: http://www.w3.org/TR/prov-o/#wasRevisionOf
    private static final StringBuffer WAS_REVISION_OF_BUFFER = new StringBuffer("wasRevisionOf");
    // see: http://www.w3.org/TR/prov-o/#invalidatedAtTime
    private static final StringBuffer INVALIDATED_AT_TIME_BUFFER = new StringBuffer("invalidatedAtTime");
    // see: http://www.w3.org/TR/prov-o/#wasInvalidatedBy
    private static final StringBuffer WAS_INVALIDATED_BY_BUFFER = new StringBuffer("wasInvalidatedBy");
    // see: http://www.w3.org/TR/prov-o/#hadMember
    private static final StringBuffer HAD_MEMBER_BUFFER = new StringBuffer("hadMember");
    // see: http://www.w3.org/TR/prov-o/#wasStartedBy
    private static final StringBuffer WAS_STARTED_BY_BUFFER = new StringBuffer("wasStartedBy");
    // see: http://www.w3.org/TR/prov-o/#wasEndedBy
    private static final StringBuffer WAS_ENDED_BY_BUFFER = new StringBuffer("wasEndedBy");
    // see: http://www.w3.org/TR/prov-o/#invalidated
    private static final StringBuffer INVALIDATED_BUFFER = new StringBuffer("invalidated");
    // see: http://www.w3.org/TR/prov-o/#influenced
    private static final StringBuffer INFLUENCED_BUFFER = new StringBuffer("influenced");
    // see: http://www.w3.org/TR/prov-o/#atLocation
    private static final StringBuffer AT_LOCATION_BUFFER = new StringBuffer("atLocation");
    // see: http://www.w3.org/TR/prov-o/#generated
    private static final StringBuffer GENERATED_BUFFER = new StringBuffer("generated");

    /* Qualified Classes */
    // see: http://www.w3.org/TR/prov-o/#Influence 
    private static final StringBuffer INFLUENCE_BUFFER = new StringBuffer("Influence");
    // see: http://www.w3.org/TR/prov-o/#EntityInfluence 
    private static final StringBuffer ENTITY_INFLUENCE_BUFFER = new StringBuffer("EntityInfluence");
    // see: http://www.w3.org/TR/prov-o/#Usage  
    private static final StringBuffer USAGE_BUFFER = new StringBuffer("Usage");
    // see: http://www.w3.org/TR/prov-o/#Start   
    private static final StringBuffer START_BUFFER = new StringBuffer("Start");
    // see: http://www.w3.org/TR/prov-o/#End   
    private static final StringBuffer END_BUFFER = new StringBuffer("End");
    // see: http://www.w3.org/TR/prov-o/#Derivation   
    private static final StringBuffer DERIVATION_BUFFER = new StringBuffer("Derivation");
    // see: http://www.w3.org/TR/prov-o/#PrimarySource  
    private static final StringBuffer PRIMARY_SOURCE_BUFFER = new StringBuffer("PrimarySource");
    // see: http://www.w3.org/TR/prov-o/#Quotation   
    private static final StringBuffer QUOTATION_BUFFER = new StringBuffer("Quotation");
    // see: http://www.w3.org/TR/prov-o/#Revision   
    private static final StringBuffer REVISION_BUFFER = new StringBuffer("Revision");
    // see: http://www.w3.org/TR/prov-o/#ActivityInfluence   
    private static final StringBuffer ACTIVITY_INFLUENCE_BUFFER = new StringBuffer("ActivityInfluence");
    // see: http://www.w3.org/TR/prov-o/#Generation  
    private static final StringBuffer GENERATION_BUFFER = new StringBuffer("Generation");
    // see: http://www.w3.org/TR/prov-o/#Communication   
    private static final StringBuffer COMMUNICATION_BUFFER = new StringBuffer("Communication");
    // see: http://www.w3.org/TR/prov-o/#Invalidation   
    private static final StringBuffer INVALIDATION_BUFFER = new StringBuffer("Invalidation");
    // see: http://www.w3.org/TR/prov-o/#AgentInfluence   
    private static final StringBuffer AGENT_INFLUENCE_BUFFER = new StringBuffer("AgentInfluence");
    // see: http://www.w3.org/TR/prov-o/#Attribution   
    private static final StringBuffer ATTRIBUTION_BUFFER = new StringBuffer("Attribution");
    // see: http://www.w3.org/TR/prov-o/#Association   
    private static final StringBuffer ASSOCIATION_BUFFER = new StringBuffer("Association");
    // see: http://www.w3.org/TR/prov-o/#Plan   
    private static final StringBuffer PLAN_BUFFER = new StringBuffer("Plan");
    // see: http://www.w3.org/TR/prov-o/#Delegation   
    private static final StringBuffer DELEGATION_BUFFER = new StringBuffer("Delegation");
    // see: http://www.w3.org/TR/prov-o/#InstantaneousEvent   
    private static final StringBuffer INSTANTATANEOUS_EVENT_BUFFER = new StringBuffer("InstantaneousEvent");
    // see: http://www.w3.org/TR/prov-o/#Role 
    private static final StringBuffer ROLE_BUFFER = new StringBuffer("Role");

    /* Qualified Properties */
    // see: http://www.w3.org/TR/prov-o/#wasInfluencedBy
    private static final StringBuffer WAS_INFLUENCED_BY_BUFFER = new StringBuffer("wasInfluencedBy");
    // see: http://www.w3.org/TR/prov-o/#qualifiedInfluence
    private static final StringBuffer QUALIFIED_INFLUENCE_BUFFER = new StringBuffer("qualifiedInfluence");
    // see: http://www.w3.org/TR/prov-o/#qualifiedGeneration 
    private static final StringBuffer QUALIFIED_GENERATION_BUFFER = new StringBuffer("qualifiedGeneration");
    // see: http://www.w3.org/TR/prov-o/#qualifiedDerivation 
    private static final StringBuffer QUALIFIED_DERIVATION_BUFFER = new StringBuffer("qualifiedDerivation");
    // see: http://www.w3.org/TR/prov-o/#qualifiedPrimarySource 
    private static final StringBuffer QUALIFIED_PRIMARY_SOURCE_BUFFER = new StringBuffer("qualifiedPrimarySource");
    // see: http://www.w3.org/TR/prov-o/#qualifiedQuotation 
    private static final StringBuffer QUALIFIED_QUOTATION_BUFFER = new StringBuffer("qualifiedQuotation");
    // see: http://www.w3.org/TR/prov-o/#qualifiedRevision
    private static final StringBuffer QUALIFIED_REVISION_BUFFER = new StringBuffer("qualifiedRevision");
    // see: http://www.w3.org/TR/prov-o/#qualifiedAttribution 
    private static final StringBuffer QAULIFIED_ATTRIBUTION_BUFFER = new StringBuffer("qualifiedAttribution");
    // see: http://www.w3.org/TR/prov-o/#qualifiedInvalidation 
    private static final StringBuffer QAULIFIED_INVALIDATION_BUFFER = new StringBuffer("qualifiedInvalidation");
    // see: http://www.w3.org/TR/prov-o/#qualifiedStart 
    private static final StringBuffer QUALIFIED_START_BUFFER = new StringBuffer("qualifiedStart");
    // see: http://www.w3.org/TR/prov-o/#qualifiedUsage 
    private static final StringBuffer QUALIFIED_USAGE_BUFFER = new StringBuffer("qualifiedUsage");
    // see: http://www.w3.org/TR/prov-o/#qualifiedCommunication 
    private static final StringBuffer QUALIFIED_COMMUNICATION_BUFFER = new StringBuffer("qualifiedCommunication");
    // see: http://www.w3.org/TR/prov-o/#qualifiedAssociation 
    private static final StringBuffer QUALIFIED_ASSOCIATION_BUFFER = new StringBuffer("qualifiedAssociation");
    // see: http://www.w3.org/TR/prov-o/#qualifiedEnd
    private static final StringBuffer QUALIFIED_END_BUFFER = new StringBuffer("qualifiedEnd");
    // see: http://www.w3.org/TR/prov-o/#qualifiedDelegation 
    private static final StringBuffer QUALIFIED_DELEGATION_BUFFER = new StringBuffer("qualifiedDelegation");
    // see: http://www.w3.org/TR/prov-o/#influencer 
    private static final StringBuffer INFLUENCER_BUFFER = new StringBuffer("influencer");
    // see: http://www.w3.org/TR/prov-o/#entity 
    private static final StringBuffer ENTITY__QUALIFIED__BUFFER = new StringBuffer("entity");
    // see: http://www.w3.org/TR/prov-o/#hadUsage 
    private static final StringBuffer HAD_USAGE_BUFFER = new StringBuffer("hadUsage");
    // see: http://www.w3.org/TR/prov-o/#hadGeneration 
    private static final StringBuffer HAD_GENERATION_BUFFER = new StringBuffer("hadGeneration");
    // see: http://www.w3.org/TR/prov-o/#activity 
    private static final StringBuffer ACTIVITY_QUALIFIED_BUFFER = new StringBuffer("activity");
    // see: http://www.w3.org/TR/prov-o/#agent 
    private static final StringBuffer AGENT__QUALIFIED__BUFFER = new StringBuffer("agent");
    // see: http://www.w3.org/TR/prov-o/#hadPlan 
    private static final StringBuffer HAD_PLAN_BUFFER = new StringBuffer("hadPlan");
    // see: http://www.w3.org/TR/prov-o/#hadActivity 
    private static final StringBuffer HAD_ACTIVITY_BUFFER = new StringBuffer("hadActivity");
    // see: http://www.w3.org/TR/prov-o/#atTime 
    private static final StringBuffer AT_TIME_BUFFER = new StringBuffer("atTime");
    // see: http://www.w3.org/TR/prov-o/#hadRole
    private static final StringBuffer HAD_ROLE_BUFFER = new StringBuffer("hadRole");

    /* FOAF Qualified Properties */
    private static final StringBuffer NAME_BUFFER = new StringBuffer("name");

    // translations from prov predicate to english sentence-part
    private static final StringBuffer RDF_TYPE_TRANSLATION_BEFORE_CONSONATE_BUFFER = new StringBuffer("is a");
    private static final StringBuffer RDF_TYPE_TRANSLATION_BEFORE_VOWEL_BUFFER = new StringBuffer("is an");
    private static final StringBuffer AT_TIME_TRANSLATION_BUFFER = new StringBuffer("happened on");
    private static final StringBuffer ASSOCIATION_TRANSLATION_BUFFER = new StringBuffer("was responsible for");
    private static final StringBuffer WAS_ASSOCIATED_WITH_TRANSLATION_BUFFER = new StringBuffer("was associated with");
    private static final StringBuffer RDFS_LABEL_TRANSLATION_BUFFER = new StringBuffer("is labeled as");
    private static final StringBuffer WAS_DERIVED_FROM_TRANSLATION_BUFFER = new StringBuffer("was derived from");
    private static final StringBuffer ENDED_AT_TIME_TRANSLATION_BUFFER = new StringBuffer("ended at");
    private static final StringBuffer STARTED_AT_TIME_TRANSLATION_BUFFER = new StringBuffer("started at");
    private static final StringBuffer USED_TRANSLATION_BUFFER = new StringBuffer("used");
    private static final StringBuffer GENERATED_TRANSLATION_BUFFER = new StringBuffer("generated");
    private static final StringBuffer WAS_GENERATED_BY_TRANSLATION_BUFFER = new StringBuffer("was generated by");

    /* Namespace Getters */
    // see: RDF-CONCEPTS @ http://www.w3.org/TR/prov-o/#bib-RDF-CONCEPTS
    public static StringBuffer getRDFNameSpaceURIBuffer() {
        return new StringBuffer(RDF_NS_BUFFER);
    }

    // see: RDF-CONCEPTS @ http://www.w3.org/TR/prov-o/#bib-RDF-CONCEPTS
    public static StringBuffer getRDFSNameSpaceURIBuffer() {
        return new StringBuffer(RDFS_NS_BUFFER);
    }

    // see: XMLSCHEMA11-2 @ http://www.w3.org/TR/prov-o/#bib-XMLSCHEMA11-2
    public static StringBuffer getXSDNameSpaceURIBuffer() {
        return new StringBuffer(XSD_NS_BUFFER);
    }

    // see: OWL2-OVERVIEW @ http://www.w3.org/TR/prov-o/#bib-OWL2-OVERVIEW
    public static StringBuffer getOWLNameSpaceURIBuffer() {
        return new StringBuffer(OWL_NS_BUFFER);
    }

    // PROV-DM @ http://www.w3.org/TR/prov-o/#bib-PROV-DM
    public static StringBuffer getPROVNameSpaceURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER);
    }

    /**
     * Provides prefix for abbreviating RDF namespace in model output
     *
     * @return new StringBuffer(prefix for abbreviating RDF namespace in model output
     */
    public static StringBuffer getRDFPrefixBuffer() {
        return new StringBuffer(RDF_PREFIX_BUFFER);
    }

    /**
     * Provides prefix for abbreviating XSD namespace in model output
     *
     * @return new StringBuffer(prefix for abbreviating XSD namespace in model output
     */
    public static StringBuffer getXSDPrefixBuffer() {
        return new StringBuffer(XSD_PREFIX_BUFFER);
    }

    /**
     * Provides prefix for abbreviating OWL namespace in model output
     *
     * @return new StringBuffer(prefix for abbreviating OWL namespace in model output
     */
    public static StringBuffer getOWLPrefixBuffer() {
        return new StringBuffer(OWL_PREFIX_BUFFER);
    }

    /**
     * Provides prefix for abbreviating PROV namespace in model output
     *
     * @return new StringBuffer(prefix for abbreviating PROV namespace in model output
     */
    public static StringBuffer getProvPrefixBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER);
    }

    /* RDF Property Getter Buffer(limited to those commonly used with prov) */
    public static StringBuffer getRDFTypePrefixedURIBuffer() {
        return new StringBuffer(RDFS_PREFIX_BUFFER).append(RDF_TYPE_PROPERTY_BUFFER);
    }

    /* RDF Property Getter Buffer(limited to those commonly used with prov) */
    public static StringBuffer getRDFTypeFullURIBuffer() {
        return new StringBuffer(RDFS_NS_BUFFER).append(RDF_TYPE_PROPERTY_BUFFER);
    }

    /* RDFS Property Getter Buffer(limited to those commonly used with prov) */
    public static StringBuffer getRDFSLabelPrefixedURIBuffer() {
        return new StringBuffer(RDFS_PREFIX_BUFFER).append(RDFS_LABEL_PROPERTY_BUFFER);
    }

    /* RDFS Property Getter Buffer(limited to those commonly used with prov) */
    public static StringBuffer getRDFSLabelFullURIBuffer() {
        return new StringBuffer(RDFS_NS_BUFFER).append(RDFS_LABEL_PROPERTY_BUFFER);
    }

    /* Starting Point Classes */
    // see: http://www.w3.org/TR/prov-o/#Entity
    public static StringBuffer getEntityStartingPointClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ENTITY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Entity
    public static StringBuffer getEntityStartingPointClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(ENTITY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Activity
    public static StringBuffer getActivityStartingPointClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ACTIVITY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Activity
    public static StringBuffer getActivityStartingPointClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(ACTIVITY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Agent
    public static StringBuffer getAgentStartingPointClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(AGENT_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Agent
    public static StringBuffer getAgentStartingPointClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(AGENT_BUFFER);
    }

    /* Starting Point Properties */
    // see: http://www.w3.org/TR/prov-o/#wasGeneratedBy
    public static StringBuffer getWasGeneratedByStartingPointPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(WAS_GENERATED_BY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasGeneratedBy
    public static StringBuffer getWasGeneratedByStartingPointPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(WAS_GENERATED_BY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasDerivedFrom
    public static StringBuffer getWasDerivedFromStartingPointPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(WAS_DERIVED_FROM_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasDerivedFrom
    public static StringBuffer getWasDerivedFromStartingPointPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(WAS_DERIVED_FROM_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasAttributedTo
    public static StringBuffer getWasAttributedToStartingPointPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(WAS_ATTRIBUTED_TO_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasAttributedTo
    public static StringBuffer getWasAttributedToStartingPointPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(WAS_ATTRIBUTED_TO_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#startedAtTime
    public static StringBuffer getStartedAtTimeStartingPointPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(STARTED_AT_TIME_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#startedAtTime
    public static StringBuffer getStartedAtTimeStartingPointPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(STARTED_AT_TIME_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#used
    public static StringBuffer getUsedStartingPointPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(USED_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#used
    public static StringBuffer getUsedStartingPointPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(USED_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasInformedBy
    public static StringBuffer getWasInformedByStartingPointPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(WAS_INFORMED_BY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasInformedBy
    public static StringBuffer getWasInformedByStartingPointPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(WAS_INFORMED_BY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#endedAtTime
    public static StringBuffer getEndedAtTimeStartingPointPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ENDED_AT_TIME_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#endedAtTime
    public static StringBuffer getEndedAtTimeStartingPointPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(ENDED_AT_TIME_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasAssociatedWith
    public static StringBuffer getWasAssociatedWithStartingPointPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(WAS_ASSOCIATED_WITH_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasAssociatedWith
    public static StringBuffer getWasAssociatedWithStartingPointPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(WAS_ASSOCIATED_WITH_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#actedOnBehalfOf
    public static StringBuffer getActedOnBehalfOfStartingPointPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ACTED_ON_BEHALF_OF_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#actedOnBehalfOf
    public static StringBuffer getActedOnBehalfOfStartingPointPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(ACTED_ON_BEHALF_OF_BUFFER);
    }

    /* Expaded Classes */
    // see: http://www.w3.org/TR/prov-o/#Collection
    public static StringBuffer getCollectionExpandedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(COLLECTION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Collection
    public static StringBuffer getCollectionExpandedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(COLLECTION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#EmptyCollection
    public static StringBuffer getEmptyCollectionExpandedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(EMPTY_COLLECTION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#EmptyCollection
    public static StringBuffer getEmptyCollectionExpandedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(EMPTY_COLLECTION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Bundle
    public static StringBuffer getBundleExpandedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(BUNDLE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Bundle
    public static StringBuffer getBundleExpandedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(BUNDLE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Person
    public static StringBuffer getPersonExpandedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(PERSON_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Person
    public static StringBuffer getPersonExpandedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(PERSON_BUFFER);
    }

    public static StringBuffer getNamePrefixedURIBuffer() {
        return new StringBuffer(FOAF_PREFIX_BUFFER).append(NAME_BUFFER);
    }

    public static StringBuffer getNameFullURIBuffer() {
        return new StringBuffer(FOAF_NS_BUFFER).append(NAME_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#SoftwareAgent
    public static StringBuffer getSoftwareAgentExpandedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(SOFTWARE_AGENT_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#SoftwareAgent
    public static StringBuffer getSoftwareAgentExpandedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(SOFTWARE_AGENT_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Organization
    public static StringBuffer getOrganizationExpandedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ORGANIZATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Organization
    public static StringBuffer getOrganizationExpandedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(ORGANIZATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Location
    public static StringBuffer getLocationExpandedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(LOCATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Location
    public static StringBuffer getLocationExpandedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(LOCATION_BUFFER);
    }

    /* Expanded Properties */
    // see: http://www.w3.org/TR/prov-o/#alternateOf
    public static StringBuffer getAlternateOfExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ALTERNATE_OF_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#alternateOf
    public static StringBuffer getAlternateOfExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(ALTERNATE_OF_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#specializationOf
    public static StringBuffer getSpecializationOfExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(SPECIALIZATION_OF_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#specializationOf
    public static StringBuffer getSpecializationOfExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(SPECIALIZATION_OF_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#generatedAtTime
    public static StringBuffer getGeneratedAtTimeExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(GENERATED_AT_TIME_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#generatedAtTime
    public static StringBuffer getGeneratedAtTimeExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(GENERATED_AT_TIME_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadPrimarySource
    public static StringBuffer getHadPrimarySourceExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(HAD_PRIMARY_SOURCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadPrimarySource
    public static StringBuffer getHadPrimarySourceExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(HAD_PRIMARY_SOURCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#value
    public static StringBuffer getValueExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(VALUE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#value
    public static StringBuffer getValueExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(VALUE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasQuotedFrom
    public static StringBuffer getWasQuotedFromExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(WAS_QUOTED_FROM_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasQuotedFrom
    public static StringBuffer getWasQuotedFromExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(WAS_QUOTED_FROM_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasRevisionOf
    public static StringBuffer getWasRevisionOfExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(WAS_REVISION_OF_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasRevisionOf
    public static StringBuffer getWasRevisionOfExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(WAS_REVISION_OF_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#invalidatedAtTime
    public static StringBuffer getInvalidatedAtTimeExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(INVALIDATED_AT_TIME_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#invalidatedAtTime
    public static StringBuffer getInvalidatedAtTimeExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(INVALIDATED_AT_TIME_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasInvalidatedBy
    public static StringBuffer getWasInvalidatedByExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(WAS_INVALIDATED_BY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasInvalidatedBy
    public static StringBuffer getWasInvalidatedByExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(WAS_INVALIDATED_BY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadMember
    public static StringBuffer getHadMemberExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(HAD_MEMBER_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadMember
    public static StringBuffer getHadMemberExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(HAD_MEMBER_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasStartedBy
    public static StringBuffer getWasStartedByExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(WAS_STARTED_BY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasStartedBy
    public static StringBuffer getWasStartedByExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(WAS_STARTED_BY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasEndedBy
    public static StringBuffer getWasEndedByExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(WAS_ENDED_BY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasEndedBy
    public static StringBuffer getWasEndedByExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(WAS_ENDED_BY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#invalidated
    public static StringBuffer getInvalidatedExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(INVALIDATED_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#invalidated
    public static StringBuffer getInvalidatedExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(INVALIDATED_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#influenced
    public static StringBuffer getInfluencedExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(INFLUENCED_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#influenced
    public static StringBuffer getInfluencedExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(INFLUENCED_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#atLocation
    public static StringBuffer getAtLocationExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(AT_LOCATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#atLocation
    public static StringBuffer getAtLocationExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(AT_LOCATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#generated
    public static StringBuffer getGeneratedExpandedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(GENERATED_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#generated
    public static StringBuffer getGeneratedExpandedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(GENERATED_BUFFER);
    }

    /* Qualified Classes */
    // see: http://www.w3.org/TR/prov-o/#Influence 
    public static StringBuffer getInfluenceQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(INFLUENCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Influence
    public static StringBuffer getInfluenceQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(INFLUENCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#EntityInfluence 
    public static StringBuffer getEntityInfluenceQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ENTITY_INFLUENCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#EntityInfluence
    public static StringBuffer getEntityInfluenceQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(ENTITY_INFLUENCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Usage  
    public static StringBuffer getUsageQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(USAGE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Usage
    public static StringBuffer getUsageQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(USAGE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Start   
    public static StringBuffer getStartQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(START_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Start
    public static StringBuffer getStartQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(START_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#End   
    public static StringBuffer getEndQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(END_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#End
    public static StringBuffer getEndQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(END_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Derivation   
    public static StringBuffer getDerivationQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(DERIVATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Derivation
    public static StringBuffer getDerivationQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(DERIVATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#PrimarySource  
    public static StringBuffer getPrimarySourceQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(PRIMARY_SOURCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#PrimarySource
    public static StringBuffer getPrimarySourceQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(PRIMARY_SOURCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Quotation   
    public static StringBuffer getQuotationQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUOTATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Quotation 
    public static StringBuffer getQuotationQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUOTATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Revision   
    public static StringBuffer getRevisionQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(REVISION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Revision
    public static StringBuffer getRevisionQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(REVISION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#ActivityInfluence   
    public static StringBuffer getActivityInfluenceQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ACTIVITY_INFLUENCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#ActivityInfluence
    public static StringBuffer getActivityInfluenceQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(ACTIVITY_INFLUENCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Generation  
    public static StringBuffer getGenerationQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(GENERATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Generation
    public static StringBuffer getGenerationQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(GENERATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Communication   
    public static StringBuffer getCommunicationQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(COMMUNICATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Communication
    public static StringBuffer getCommunicationQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(COMMUNICATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Invalidation   
    public static StringBuffer getInvalidationQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(INVALIDATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Invalidation
    public static StringBuffer getInvalidationQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(INVALIDATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#AgentInfluence   
    public static StringBuffer getAgentInfluenceQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(AGENT_INFLUENCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#AgentInfluence
    public static StringBuffer getAgentInfluenceQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(AGENT_INFLUENCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Attribution   
    public static StringBuffer getAttributionQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ATTRIBUTION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Attribution
    public static StringBuffer getAttributionQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(ATTRIBUTION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Association   
    public static StringBuffer getAssociationQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ASSOCIATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Association
    public static StringBuffer getAssociationQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(ASSOCIATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Plan   
    public static StringBuffer getPlanQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(PLAN_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Plan
    public static StringBuffer getPlanQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(PLAN_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Delegation   
    public static StringBuffer getDelegationQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(DELEGATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Delegation
    public static StringBuffer getDelegationQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(DELEGATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#InstantaneousEvent   
    public static StringBuffer getInstantaneousEventQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(INSTANTATANEOUS_EVENT_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#InstantaneousEvent
    public static StringBuffer getInstantaneousEventQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(INSTANTATANEOUS_EVENT_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Role 
    public static StringBuffer getRoleQualifiedClassPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ROLE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#Role
    public static StringBuffer getRoleQualifiedClassFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(ROLE_BUFFER);
    }

    /* Qualified Properties */
    // see: http://www.w3.org/TR/prov-o/#wasInfluencedBy
    public static StringBuffer getWasInfluencedByQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(WAS_INFLUENCED_BY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#wasInfluencedBy
    public static StringBuffer getWasInfluencedByQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(WAS_INFLUENCED_BY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedInfluence
    public static StringBuffer getQualifiedInfluenceQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUALIFIED_INFLUENCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedInfluence
    public static StringBuffer getQualifiedInfluenceQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUALIFIED_INFLUENCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedGeneration 
    public static StringBuffer getQualifiedGenerationQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUALIFIED_GENERATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedGeneration
    public static StringBuffer getQualifiedGenerationQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUALIFIED_GENERATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedDerivation 
    public static StringBuffer getQualifiedDerivationQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUALIFIED_DERIVATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedDerivation
    public static StringBuffer getQualifiedDerivationQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUALIFIED_DERIVATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedPrimarySource 
    public static StringBuffer getQualifiedPrimarySourceQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUALIFIED_PRIMARY_SOURCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedPrimarySource
    public static StringBuffer getQualifiedPrimarySourceQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUALIFIED_PRIMARY_SOURCE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedQuotation 
    public static StringBuffer getQualifiedQuotationQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUALIFIED_QUOTATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedQuotation
    public static StringBuffer getQualifiedQuotationQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUALIFIED_QUOTATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedRevision
    public static StringBuffer getQualifiedRevisionQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUALIFIED_REVISION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedRevision
    public static StringBuffer getQualifiedRevisionQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUALIFIED_REVISION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedAttribution 
    public static StringBuffer getQualifiedAttributionQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QAULIFIED_ATTRIBUTION_BUFFER);
    }

    public static StringBuffer getQualifiedAttributionQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QAULIFIED_ATTRIBUTION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedInvalidation 
    public static StringBuffer getQualifiedInvalidationQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QAULIFIED_INVALIDATION_BUFFER);
    }

    public static StringBuffer getQualifiedInvalidationQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QAULIFIED_INVALIDATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedStart 
    public static StringBuffer getQualifiedStartQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUALIFIED_START_BUFFER);
    }

    public static StringBuffer getQualifiedStartQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUALIFIED_START_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedUsage 
    public static StringBuffer getQualifiedUsageQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUALIFIED_USAGE_BUFFER);
    }

    public static StringBuffer getQualifiedUsageQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUALIFIED_USAGE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedCommunication 
    public static StringBuffer getQualifiedCommunicationQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUALIFIED_COMMUNICATION_BUFFER);
    }

    public static StringBuffer getQualifiedCommunicationQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUALIFIED_COMMUNICATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedAssociation 
    public static StringBuffer getQualifiedAssociationQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUALIFIED_ASSOCIATION_BUFFER);
    }

    public static StringBuffer getQualifiedAssociationQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUALIFIED_ASSOCIATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedEnd
    public static StringBuffer getQualifiedEndQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUALIFIED_END_BUFFER);
    }

    public static StringBuffer getQualifiedEndQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUALIFIED_END_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedDelegation 
    public static StringBuffer getQualifiedDelegationQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(QUALIFIED_DELEGATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#qualifiedDelegation
    public static StringBuffer getQualifiedDelegationQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(QUALIFIED_DELEGATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#influencer 
    public static StringBuffer getInfluencerQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(INFLUENCER_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#influencer 
    public static StringBuffer getInfluencerQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(INFLUENCER_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#entity 
    public static StringBuffer getEntityQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ENTITY__QUALIFIED__BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#entity
    public static StringBuffer getEntityQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(ENTITY__QUALIFIED__BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadUsage 
    public static StringBuffer getHadUsageQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(HAD_USAGE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadUsage
    public static StringBuffer getHadUsageQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(HAD_USAGE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadGeneration 
    public static StringBuffer getHadGenerationQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(HAD_GENERATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadGeneration
    public static StringBuffer getHadGenerationQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(HAD_GENERATION_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#activity 
    public static StringBuffer getActivityQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(ACTIVITY_QUALIFIED_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#activity
    public static StringBuffer getActivityQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(ACTIVITY_QUALIFIED_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#agent 
    public static StringBuffer getAgentQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(AGENT__QUALIFIED__BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#agent 
    public static StringBuffer getAgentQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(AGENT__QUALIFIED__BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadPlan 
    public static StringBuffer getHadPlanQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(HAD_PLAN_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadPlan 
    public static StringBuffer getHadPlanQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(HAD_PLAN_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadActivity 
    public static StringBuffer getHadActivityQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(HAD_ACTIVITY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadActivity 
    public static StringBuffer getHadActivityQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(HAD_ACTIVITY_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#atTime 
    public static StringBuffer getAtTimeQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(AT_TIME_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#atTime 
    public static StringBuffer getAtTimeQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(AT_TIME_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadRole
    public static StringBuffer getHadRoleQualifiedPropertyPrefixedURIBuffer() {
        return new StringBuffer(PROV_PREFIX_BUFFER).append(HAD_ROLE_BUFFER);
    }

    // see: http://www.w3.org/TR/prov-o/#hadRole
    public static StringBuffer getHadRoleQualifiedPropertyFullURIBuffer() {
        return new StringBuffer(PROV_NS_BUFFER).append(HAD_ROLE_BUFFER);
    }

    /**
     * Provides a human-readable sentence-part for the predicate of a provenance
     * triple
     *
     * @param uri - The PROV-O URI for the predicate
     * @param objectStartWithVowel - Indicates whether or not the object of the
     * triple starts with a vowel
     * @return Human readable text describing the relationship between a subject
     * and object, based on the PROV-O predicate
     */
    public static StringBuffer translatePredicate(StringBuffer uri, boolean objectStartWithVowel) {
        StringBuffer translation = null;
        if (uri.equals(ProvOntology.getRDFTypeFullURIBuffer())) {
            if (!objectStartWithVowel) {
                translation = new StringBuffer(RDF_TYPE_TRANSLATION_BEFORE_CONSONATE_BUFFER);
            } else {
                translation = new StringBuffer(RDF_TYPE_TRANSLATION_BEFORE_VOWEL_BUFFER);
            }
        } else if (uri.equals(ProvOntology.getAtTimeQualifiedPropertyFullURIBuffer())) {
            translation = new StringBuffer(AT_TIME_TRANSLATION_BUFFER);
        } else if (uri.equals(ProvOntology.getAssociationQualifiedClassFullURIBuffer())) {
            translation = new StringBuffer(ASSOCIATION_TRANSLATION_BUFFER);
        } else if (uri.equals(ProvOntology.getWasAssociatedWithStartingPointPropertyFullURIBuffer())) {
            translation = new StringBuffer(WAS_ASSOCIATED_WITH_TRANSLATION_BUFFER);
        } else if (uri.equals(ProvOntology.getRDFSLabelFullURIBuffer())) {
            translation = new StringBuffer(RDFS_LABEL_TRANSLATION_BUFFER);
        } else if (uri.equals(ProvOntology.getWasDerivedFromStartingPointPropertyFullURIBuffer())) {
            translation = new StringBuffer(WAS_DERIVED_FROM_TRANSLATION_BUFFER);
        } else if (uri.equals(ProvOntology.getEndedAtTimeStartingPointPropertyFullURIBuffer())) {
            translation = new StringBuffer(ENDED_AT_TIME_TRANSLATION_BUFFER);
        } else if (uri.equals(ProvOntology.getStartedAtTimeStartingPointPropertyFullURIBuffer())) {
            translation = new StringBuffer(STARTED_AT_TIME_TRANSLATION_BUFFER);
        } else if (uri.equals(ProvOntology.getUsedStartingPointPropertyFullURIBuffer())) {
            translation = new StringBuffer(USED_TRANSLATION_BUFFER);
        } else if (uri.equals(ProvOntology.getGeneratedExpandedPropertyFullURIBuffer())) {
            translation = new StringBuffer(GENERATED_TRANSLATION_BUFFER);
        } else if (uri.equals(ProvOntology.getWasGeneratedByStartingPointPropertyFullURIBuffer())) {
            translation = new StringBuffer(WAS_GENERATED_BY_TRANSLATION_BUFFER);
        }
        return translation;
    }
}
