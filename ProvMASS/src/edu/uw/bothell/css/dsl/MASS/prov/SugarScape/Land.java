/*

 	MASS Java Software License
	© 2012-2015 University of Washington

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	The following acknowledgment shall be used where appropriate in publications, presentations, etc.:      

	© 2012-2015 University of Washington. MASS was developed by Computing and Software Systems at University of 
	Washington Bothell.

	THE SOFTWARE IS PROVIDED "AS IS"), WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.

 */
package edu.uw.bothell.css.dsl.MASS.prov.SugarScape;

import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledObject;
import edu.uw.bothell.css.dsl.MASS.prov.core.Place;             // Library for Multi-Agent Spatial Simulation       
import edu.uw.bothell.css.dsl.MASS.prov.filter.PlaceFilter;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.util.*;

// Land Array
public class Land extends Place implements ProvEnabledObject {

    // Function identifiers
    public static final int EXCHANGE = 0;
    public static final StringBuffer EXCHANGE_RID = ProvUtils.getUniversalResourceID(new StringBuffer("EXCHANGE"));
    public static final int UPDATE = 1;
    public static final StringBuffer UPDATE_RID = ProvUtils.getUniversalResourceID(new StringBuffer("UPDATE"));
    public static final int INIT = 2;
    public static final StringBuffer INIT_RID = ProvUtils.getUniversalResourceID(new StringBuffer("INIT"));
    public static final int COLLECT_LOCAL_DATA = 3;
    public static final StringBuffer COLLECT_LOCAL_DATA_RID = ProvUtils.getUniversalResourceID(new StringBuffer("COLLECT_LOCAL_DATA"));

    // Setup the array size and index location (x,y), and sugar inventory
    private int sizeX, sizeY;
    private final StringBuffer sizeX_RID = ProvUtils.getUniversalResourceID(new StringBuffer("sizeX"));
    private final StringBuffer sizeY_RID = ProvUtils.getUniversalResourceID(new StringBuffer("sizeY"));
    private int myX, myY;
    private final StringBuffer myX_RID = ProvUtils.getUniversalResourceID(new StringBuffer("myX"));
    private final StringBuffer myY_RID = ProvUtils.getUniversalResourceID(new StringBuffer("myY"));
    public int sugar;
    public final StringBuffer sugar_RID = ProvUtils.getUniversalResourceID(new StringBuffer("sugar"));
    int[] nbrNumAgents = null;
    final StringBuffer nbrNumAgents_RID = ProvUtils.getUniversalResourceID(new StringBuffer("nbrNumAgents"));
    int[] nbrNumSugar = null;
    final StringBuffer nbrNumSugar_RID = ProvUtils.getUniversalResourceID(new StringBuffer("nbrNumSugar"));
    int vDist;
    final StringBuffer vDist_RID = ProvUtils.getUniversalResourceID(new StringBuffer("vDist"));
    private final StringBuffer UUID = ProvUtils.getUniversalResourceID(new StringBuffer(this.getClass().getSimpleName()));

    public Land(Object object) {
        StopWatch.start(false);
        ProvenanceRecorder.documentProvEnabledObject(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer(this.getClass().getSimpleName()), true, new StringBuffer("PLACE"));
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Land"), new StringBuffer("constructor"), false, new String[]{"object"}, new Object[]{object}, true);
        int Dist = 1;

        // Define the neighbors of each cell   
        Vector<int[]> neighbors = new Vector<int[]>();
        for (int x = 0 - Dist; x <= Dist; x++) {
            for (int y = 0 - Dist; y <= Dist; y++) {
                if (!(x == 0 && y == 0)) {
                    neighbors.add(new int[]{x, y});
                }
            }
        }
        setNeighbors(neighbors);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Land"), procRID, null, null, null, null, false, false, false, true);
        StopWatch.stop(false);
    }

    @Override
    public void substituteConstructorDocumentation() {
        StopWatch.start(false);
        ProvenanceRecorder.documentProvEnabledObject(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer(this.getClass().getSimpleName()), true, new StringBuffer("PLACE"));
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Land"), new StringBuffer("constructor"), false, new String[]{"object"}, new Object[]{new Object()}, true);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Land"), procRID, null, null, null, null, false, false, false, true);
        StopWatch.stop(false);
    }

    /**
     * @param funcId the function Id to call
     * @param args argumenets passed to this funcId.
     */
    // --------------------------------------------------------------------------
    public Object callMethod(int funcId, Object args) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), new StringBuffer("label"), false, new String[]{"funcId", "args"}, new Object[]{funcId, args}, true);
        Object returnReference;
        switch (funcId) {
            case INIT:
                ProvenanceRecorder.documentFieldAccess(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("INIT"), INIT_RID, procRID, INIT, true);
                returnReference = init(args);
                ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), procRID, new StringBuffer("returnReference"), returnReference, null, null, false, false, false);
                StopWatch.stop(false);
                return returnReference;
            case EXCHANGE:
                ProvenanceRecorder.documentFieldAccess(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("EXCHANGE"), EXCHANGE_RID, procRID, EXCHANGE, true);
                returnReference = exchange(args);
                ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), procRID, new StringBuffer("returnReference"), returnReference, null, null, false, false, false);
                StopWatch.stop(false);
                return returnReference;
            case UPDATE:
                ProvenanceRecorder.documentFieldAccess(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("UPDATE"), UPDATE_RID, procRID, UPDATE, true);
                returnReference = update(args);
                ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), procRID, new StringBuffer("returnReference"), returnReference, null, null, false, false, false);
                StopWatch.stop(false);
                return returnReference;
            case COLLECT_LOCAL_DATA:
                ProvenanceRecorder.documentFieldAccess(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("COLLECT_LOCAL_DATA"), COLLECT_LOCAL_DATA_RID, procRID, COLLECT_LOCAL_DATA, true);
                returnReference = collectLocationData(args);
                ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), procRID, new StringBuffer("returnReference"), returnReference, null, null, false, false, false);
                StopWatch.stop(false);
                return returnReference;
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), procRID, new StringBuffer("null"), null, null, null, false, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    // Initialize the place -----------------------------------------------------
    public Object init(Object args) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("init"), new StringBuffer("label"), false, new String[]{"args"}, new Object[]{args}, true);
        sizeX = getSize()[0];
        ProvenanceRecorder.documentFieldAssignment(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sizeX"), sizeX_RID, sizeX, procRID, true);
        sizeY = getSize()[1]; // size  is the base data members
        ProvenanceRecorder.documentFieldAssignment(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sizeY"), sizeY_RID, sizeY, procRID, true);
        myX = getIndex()[0];
        ProvenanceRecorder.documentFieldAssignment(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("myX"), myX_RID, myX, procRID, true);
        myY = getIndex()[1];  // index is the base data members
        ProvenanceRecorder.documentFieldAssignment(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("myY"), myY_RID, myY, procRID, true);

        // Setup Visual Distance and the arrays for 
        // storing my neighbors agent and sugar count
        vDist = 8;
        ProvenanceRecorder.documentFieldAssignment(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("vDist"), vDist_RID, vDist, procRID, true);
        int arrySize = 4 * ((vDist * vDist) + vDist);
        nbrNumAgents = new int[arrySize];
        ProvenanceRecorder.documentFieldAssignment(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("nbrNumAgents"), nbrNumAgents_RID, nbrNumAgents, procRID, true);
        nbrNumSugar = new int[arrySize];
        ProvenanceRecorder.documentFieldAssignment(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("nbrNumSugar"), nbrNumSugar_RID, nbrNumSugar, procRID, true);
        for (int i = 0; i < arrySize; i++) {
            nbrNumAgents[i] = 0;
            nbrNumSugar[i] = 0;
        }

        // Place Sugar Randomly all over the grid
        int numSugarValues = 5;
        Random gen = new Random();
        int tmpValue = gen.nextInt(numSugarValues * 2);
        if (tmpValue >= numSugarValues) {
            sugar = 0;
            ProvenanceRecorder.documentFieldAssignment(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, sugar, procRID, true);
        } else {
            sugar = tmpValue;
            ProvenanceRecorder.documentFieldAssignment(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, sugar, procRID, true);
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("init"), procRID, new StringBuffer("null"), null, null, null, false, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * Is called from exchangeAll( ) to exchange #agents with my neighbors
     *
     * @param args formally requested but actuall not used.
     */
    public Object exchange(Object args) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("exchange"), new StringBuffer("label"), false, new String[]{"args"}, new Object[]{args}, true);
        int[] unitData = {getNumAgents(), sugar};
        ProvenanceRecorder.documentFieldAccess(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, procRID, sugar, true);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("exchange"), procRID, new StringBuffer("(Object)_unitData"), (Object) unitData, null, null, false, false, false, true);
        StopWatch.stop(false);
        return (Object) unitData;
    }

    /**
     * Is called from callAll( ) to update my neighbors' #agents and #sugar
     *
     * @param args formally requested but actuall not used.
     */
    public Object update(Object args) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("update"), new StringBuffer("label"), false, new String[]{"args"}, new Object[]{args}, true);
        int arrySize = getInMessages().length;
        for (int i = 0; i < arrySize; i++) {

            if (getInMessages()[i] == null) {
                nbrNumAgents[i] = 0;
                nbrNumSugar[i] = 0;
            } else {
                nbrNumAgents[i] = ((int[]) getInMessages()[i])[0];
                nbrNumSugar[i] = ((int[]) getInMessages()[i])[1];
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("update"), procRID, new StringBuffer("null"), null, null, null, false, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * Used by Unit (agent) to get the index of array locations that contain the
     * neighbors for the unit with a different (smaller) visual distance than
     * what is stored by the land
     *
     * @param unitVDist Unit's visual distance
     */
    public int[] getNbrLocations(int unitVDist) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getNbrLocations"), new StringBuffer("label"), false, new String[]{"unitVDist"}, new Object[]{unitVDist}, true);
        if (unitVDist > vDist) {
            ProvenanceRecorder.documentFieldAccess(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("vDist"), vDist_RID, procRID, vDist, true);
            ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getNbrLocations"), procRID, new StringBuffer("null"), null, null, new StringBuffer("Unit's visual distance"), false, false, false, true);
            return null;
        }

        int arrySize = 4 * ((unitVDist * unitVDist) + unitVDist);
        int[] nbrList = new int[arrySize];

        int maxIndex = 0;
        int tmpIndex = 0;

        for (int x = 0 - vDist; x <= vDist; x++) {
            ProvenanceRecorder.documentFieldAccess(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("vDist"), vDist_RID, procRID, vDist, true);
            for (int y = 0 - vDist; y <= vDist; y++) {
                ProvenanceRecorder.documentFieldAccess(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("vDist"), vDist_RID, procRID, vDist, true);
                if (!(x == 0 && y == 0)
                        && (x >= 0 - unitVDist) && (x <= unitVDist)
                        && (y >= 0 - unitVDist) && (y <= unitVDist)) {

                    nbrList[tmpIndex] = maxIndex;
                    tmpIndex++;
                }
                if (!(x == 0 && y == 0)) {
                    maxIndex++;
                }
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getNbrLocations"), procRID, new StringBuffer("nbrList"), nbrList, null, new StringBuffer("Unit's visual distance"), false, false, false, true);
        StopWatch.stop(false);
        return nbrList;
    }

    /**
     *
     */
    public boolean consumeSugar() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("consumeSugar"), new StringBuffer("label"), false, null, null, true);
        if (sugar > 0) {
            ProvenanceRecorder.documentFieldAccess(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, procRID, sugar, true);
            //System.err.print( "Sugar consumed.... Level: " + sugar );
            sugar--;
            ProvenanceRecorder.documentFieldAssignment(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, sugar, procRID, true);
            //System.err.println( " -> " + sugar );
            ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("consumeSugar"), procRID, new StringBuffer("true"), true, null, null, false, false, false, true);
            return true;
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("consumeSugar"), procRID, new StringBuffer("false"), false, null, null, false, false, false, true);
        StopWatch.stop(false);
        return false;
    }

    /**
     * Used for collecting the local data to diplay
     *
     * @param args formally declared but actually not used.
     */
    public Object collectLocationData(Object args) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("collectLocationData"), new StringBuffer("label"), false, new String[]{"args"}, new Object[]{args}, true);
        int[] unitData = {getNumAgents(), sugar};
        ProvenanceRecorder.documentFieldAccess(provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, procRID, sugar, true);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("collectLocationData"), procRID, new StringBuffer("(Object)_unitData"), (Object) unitData, null, null, false, false, false, true);
        StopWatch.stop(false);
        return (Object) unitData;
    }

    @Override
    public String getOwnerUUID() {
        if (UUID == null) {
            return null;
        } else {
            return UUID.toString();
        }
    }

    @Override
    public ProvenanceStore getStore() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStore(ProvenanceStore provenanceStore) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mapProvenanceCapture() {
        PlaceFilter.filter(MASSProv.placeFilter, this, MASSProv.placeFilterCriteria);
    }
}
