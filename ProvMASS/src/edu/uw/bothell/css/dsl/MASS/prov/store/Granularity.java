package edu.uw.bothell.css.dsl.MASS.prov.store;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a level of provenance granularity with respect to the source code
 * whose execution it describes.
 *
 * @author Delmar B. Davis
 */
public enum Granularity {
    NONE(0), PROCESS(1), SIMULATION(2), PROCEDURE(3), RETURN(4), PARAMS(5),
    FIELD(6), LINE(7), EXPRESSION(8), INSTRUCTION(9), CUSTOM(10);

    private int value = 0;

    private static Map<Integer, Granularity> map = new HashMap<>();

    private Granularity(int level) {
        this.value = level;
    }

    static {
        for (Granularity pageType : Granularity.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static Granularity valueOf(int level) {
        return map.get(level);
    }

    public int getValue() {
        return value;
    }
}
