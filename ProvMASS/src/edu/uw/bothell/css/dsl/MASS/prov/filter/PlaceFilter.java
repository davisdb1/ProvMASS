package edu.uw.bothell.css.dsl.MASS.prov.filter;

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledObject;
import edu.uw.bothell.css.dsl.MASS.prov.core.Place;

/**
 * A PlaceFilter specifies how provenance capture will be mapped to specific
 * place instances
 *
 * @author Delmar B. Davis
 */
public enum PlaceFilter {
    SINGLE_BY_LINEAR_INDEX, SINGLE_BY_COORINATES, FRACTION, ALL, NONE;

    public static PlaceFilter fromString(String filterEnum) {
        PlaceFilter filter = null;
        if (filterEnum.toLowerCase().equals(PlaceFilter.ALL.toString().toLowerCase())) {
            filter = PlaceFilter.ALL;
        } else if (filterEnum.toLowerCase().equals(PlaceFilter.NONE.toString().toLowerCase())) {
            filter = PlaceFilter.NONE;
        } else if (filterEnum.toLowerCase().equals(PlaceFilter.FRACTION.toString().toLowerCase())) {
            filter = PlaceFilter.FRACTION;
        } else if (filterEnum.toLowerCase().equals(PlaceFilter.SINGLE_BY_LINEAR_INDEX.toString().toLowerCase())) {
            filter = PlaceFilter.SINGLE_BY_LINEAR_INDEX;
        } else if (filterEnum.toLowerCase().equals(PlaceFilter.SINGLE_BY_COORINATES.toString().toLowerCase())) {
            filter = PlaceFilter.SINGLE_BY_COORINATES;
        }
        return filter;
    }

    public static void filter(PlaceFilter FILTER, Place place, Object FILTER_CRITERIA) {
        switch (FILTER) {
            case FRACTION:
                int denominator = -1;
                try {
                    denominator = (int) FILTER_CRITERIA;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                mapFraction(place, denominator);
                break;
            case SINGLE_BY_COORINATES:
                int[] coordinates = {-1};
                try {
                    coordinates = (int[]) FILTER_CRITERIA;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                mapPlace(place, coordinates);
                break;
            case SINGLE_BY_LINEAR_INDEX:
                int index = -1;
                try {
                    index = (int) FILTER_CRITERIA;
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                mapPlace(place, index);
                break;
            case ALL:
                mapAll(place);
                break;
            case NONE:
                mapNone(place);
                break;
            default:
                place.setProvOn(false);
                break;
        }
        if (place.isProvOn()) {
            if (place instanceof ProvEnabledObject) {
                ((ProvEnabledObject) place).substituteConstructorDocumentation();
            }
        }
    }

    public static Object getCriteria(PlaceFilter FILTER, String criteriaValue) {
        Object criteria = null;
        switch (FILTER) {
            case FRACTION:
                try {
                    int denominator = Integer.valueOf(criteriaValue);
                    criteria = denominator;
                } catch (NumberFormatException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                break;
            case SINGLE_BY_COORINATES:
                try {
                    int[] coordinates = {-1};
                    String[] coords = criteriaValue.split(",");
                    for (int i = 0, im = coords.length; i < im; i++) {
                        try {
                            coordinates[i] = Integer.valueOf(coords[i]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace(IO.getLogWriter());
                            break;
                        }
                        if (i == im - 1) {
                            criteria = coordinates;
                        }
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                break;
            case SINGLE_BY_LINEAR_INDEX:
                try {
                    int index = Integer.valueOf(criteriaValue);
                    criteria = index;
                } catch (NumberFormatException e) {
                    e.printStackTrace(IO.getLogWriter());
                }
                break;
            default:
                break;
        }
        return criteria;
    }

    private static void mapAll(Place place) {
        place.setProvOn(true);
    }

    private static void mapNone(Place place) {
        place.setProvOn(false);
    }

    /**
     * Maps provenance capture for a specific fraction of the place instances
     * within the places collection where the specified place resides. For
     * example, if 1/6 of the place instances within the places collection where
     * this place resides should have provenance capture turned on, then every
     * 6th place that is evaluated by this procedure will have its provenance
     * turned on. In the example, the specified denominator is 6.
     *
     * @param place - the place thats provenance capture functionality is being
     * mapped
     * @param denominator - the denominator (with a numerator of one) of the
     * fraction of place instances to capture provenance for
     */
    private static void mapFraction(Place place, int denominator) {
        place.setProvOn(!place.isProvOn()
                && denominator > 0
                && place.getLinearIndex() % denominator == 0);
    }

    /**
     * Maps a single place based on the coordinates within the places collection
     * where the specified place instance resides.
     *
     * @param place - the place thats provenance capture functionality is being
     * mapped
     * @param coordinates - coordinates within the places collection where the
     * specified place resides, that should match the specified coordinates in
     * the case that the specified place instance should have its provenance
     * captured.
     */
    private static void mapPlace(Place place, int[] coordinates) {
        place.setProvOn(!place.isProvOn()
                && coordinatesMatch(place.getIndex(), coordinates));
    }

    /**
     * Evaluates the specified place for provenance capture mapping. The
     * specified index should match that of the specified place in the case that
     * its provenance should be captured.
     *
     * @param place - the place thats provenance capture functionality is being
     * mapped
     * @param index - the linear index of the place that should have provenance
     * capture turned on. The linear index is the offset into the
     * multi-dimensional array (e.g. 26 is the index of 2,2,2 in a 3x3x3
     * 3-dimensional collection of Place objects)
     */
    private static void mapPlace(Place place, int index) {
        // set prov on to true if the coordinate index matches
        place.setProvOn(!place.isProvOn()
                && place.getLinearIndex() == index);
    }

    /**
     * Indicates whether or not the coordinates in two sets of coordinates match
     *
     * @param sourceCoordinates - coordinates to compare against
     * @param targetCoordinates - coordinates being compared
     * @return True if each value within sourceCoordinates matches each value
     * within targetCoordinates
     */
    private static boolean coordinatesMatch(int[] sourceCoordinates, int[] targetCoordinates) {
        boolean same = true;
        if (sourceCoordinates.length != targetCoordinates.length) {
            same = false;
        } else {
            //if agent index matches with provide index turn prov on
            for (int i = 0; i < sourceCoordinates.length; i++) {
                if (sourceCoordinates[i] != targetCoordinates[i]) {
                    same = false;
                    break;
                }
            }
        }
        return same;
    }
}
