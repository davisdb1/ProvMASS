package edu.uw.bothell.css.dsl.MASS.prov.store;

import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Preprocesses provenance. Generally, this includes sorting (basic simulation
 * and framework use metadata vs. structured relational statements), formatting
 * (escaping unrecognized characters for model input and terminating
 * line-endings), and logistical bookkeeping (providing maximum amount of
 * strings per file and characters per string).
 *
 * @author Delmar B. Davis
 */
public class ProvStaging {

    /**
     * Provides a line of basic metadata provenance. This includes a metaPrefix
     * to indicate that the line should be sorted into simulation/framework-use
     * metadata rather than relational provenance that is added to model for
     * query.
     *
     * @param line - unprocessed provenance data
     * @return preprocessed metadata provenance
     */
    protected static StringBuffer metaString(StringBuffer line) {
        StringBuffer formattedString = new StringBuffer();
        if (line.length() + META_PREFIX.length() > MAX_PROV_STRING_LENGTH) {
            formattedString = null;
        } else {
            formattedString.append(META_PREFIX).append("<").append(
                    new GregorianCalendar(TimeZone.getDefault()).
                            toZonedDateTime()).
                    append(">:").append(line);
        }
        return formattedString;
    }

    /**
     * Provides a line of basic metadata provenance. This includes a metaPrefix
     * to indicate that the line should be sorted into simulation/framework-use
     * metadata rather than relational provenance that is added to model for
     * query.
     *
     * @param line - unprocessed provenance data
     * @return preprocessed metadata provenance
     */
    protected static String metaString(String line) {
        String formattedString;
        if (line.length() + META_PREFIX.length() > MAX_PROV_STRING_LENGTH) {
            formattedString = null;
        } else {
            formattedString = new StringBuilder()
                    .append(META_PREFIX).append("<").append(
                    new GregorianCalendar(TimeZone.getDefault()).
                            toZonedDateTime()).
                    append(">:").append(line).toString();
        }
        return formattedString;
    }

    /**
     * Provides a line of relational provenance. This includes a
     * relationalPrefix to indicate that the line should be sorted into
     * relational provenance that is added to model for query rather than
     * simulation/framework-use metadata.
     *
     * @param line - unprocessed provenance data
     * @return preprocessed relational provenance
     */
    protected static StringBuffer relationalString(StringBuffer line) {
        StringBuffer formattedString = new StringBuffer();
        if (line.length() + RELATIONAL_PREFIX.length() > MAX_PROV_STRING_LENGTH) {
            formattedString = null;
        } else {
            formattedString.append(RELATIONAL_PREFIX).append("<").append(
                    new GregorianCalendar(TimeZone.getDefault()).
                            toZonedDateTime()).
                    append(">:").append(line).toString();
        }
        return formattedString;
    }

    /**
     * Provides a line of relational provenance. This includes a
     * relationalPrefix to indicate that the line should be sorted into
     * relational provenance that is added to model for query rather than
     * simulation/framework-use metadata.
     *
     * @param line - unprocessed provenance data
     * @return preprocessed relational provenance
     */
    protected static String relationalString(String line) {
        String formattedString;
        if (line.length() + RELATIONAL_PREFIX.length() > MAX_PROV_STRING_LENGTH) {
            formattedString = null;
        } else {
            formattedString = new StringBuffer()
                    .append(RELATIONAL_PREFIX).append("<").append(
                    new GregorianCalendar(TimeZone.getDefault()).
                            toZonedDateTime()).
                    append(">:").append(line).toString();
        }
        return formattedString;
    }

    /**
     * Provides a line of provenance that can be preprocessed for addition to an
     * RDF model.
     *
     * @param subj - UID of the resource being described by the provenance
     * statement
     * @param pred - UID (RDF network address) of the relationship between the
     * subject resource and the object resource (as defined in the w3c Prov-O)
     * @param obj - UID of the resource, who's relationship to the subject
     * resource, is being stated
     * @return ProvO based line of provenance data
     */
    public static String ProvOString(String subj, String pred, String obj) {
        /*return relationalString(encapsulateResourceID(subj) + RESOURCE_DELIMITER
                + pred + RESOURCE_DELIMITER
                + encapsulateResourceID(obj));*/

        return relationalString(new StringBuilder(subj).append(RESOURCE_DELIMITER).
                append(pred).append(RESOURCE_DELIMITER).append(obj).toString());
    }

    /**
     * Provides a line of provenance that can be preprocessed for addition to an
     * RDF model.
     *
     * @param subj - UID of the resource being described by the provenance
     * statement
     * @param pred - UID (RDF network address) of the relationship between the
     * subject resource and the object resource (as defined in the w3c Prov-O)
     * @param obj - UID of the resource, who's relationship to the subject
     * resource, is being stated
     * @return ProvO based line of provenance data
     */
    public static StringBuffer ProvOString(StringBuffer subj, StringBuffer pred,
            StringBuffer obj) {
        StringBuffer tag = new StringBuffer();
        tag.append(subj);
        tag.append(RESOURCE_DELIMITER);
        tag.append(pred);
        tag.append(RESOURCE_DELIMITER);
        tag.append(obj);
        return relationalString(tag);
    }

    private static final String META_PREFIX = "Annotation";
    private static final String RELATIONAL_PREFIX = "Relational";
    public static final String RESOURCE_DELIMITER = "`,~";
    private static final int MAX_PROV_STRING_LENGTH = 1024 * 1024;
    /**
     * The maximum number of provenance strings to be stored in an individual
     * preprocessed file
     */
    public static int MAX_LINES = 1024;
    public static int MAX_CHAR_PER_LINE = 1024;
}
