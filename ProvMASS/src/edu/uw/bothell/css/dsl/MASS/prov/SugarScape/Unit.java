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

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledAgent;
import edu.uw.bothell.css.dsl.MASS.prov.core.Agent; // parent class
import edu.uw.bothell.css.dsl.MASS.prov.filter.AgentFilter;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.util.HashSet;
import java.util.Random;    // for Migration
import java.util.Vector;   // for Neighbor Operations

public class Unit extends Agent implements ProvEnabledAgent {

    // function identifiers
    public static final int decideNewPosition_ = 0;
    public static final StringBuffer decideNewPosition__RID = ProvUtils.getUniversalResourceID(new StringBuffer("decideNewPosition_"));
    // Define the four neighbors of each cell
    public final int MAX_SUGAR = 10;
    public final StringBuffer MAX_SUGAR_RID = ProvUtils.getUniversalResourceID(new StringBuffer("MAX_SUGAR"));
    public final int MIN_SUGAR = 0;
    public final StringBuffer MIN_SUGAR_RID = ProvUtils.getUniversalResourceID(new StringBuffer("MIN_SUGAR"));

    private final StringBuffer UUID = ProvUtils.getUniversalResourceID(new StringBuffer(this.getClass().getSimpleName()));
    private final HashSet<String> identifiedAt = new HashSet<>();

    private final Vector<int[]> neighbors = new Vector<int[]>();
    // private final String neighbors_RID = ProvUtils.getUniversalResourceID("neighbors"); // unused, save agent space

    private int sugar;
    private final StringBuffer sugar_RID = ProvUtils.getUniversalResourceID(new StringBuffer("sugar"));

    private int vDist;
    private final StringBuffer vDist_RID = ProvUtils.getUniversalResourceID(new StringBuffer("vDist"));

    private static boolean decideNewPositionCalledOnHostLogged = false;

    // Constructors
    // ------------
    public Unit() {
        super();
        StopWatch.start(false);
        if (MASSProv.provOn && provOn && !identifiedAt.contains(ProvUtils.getHostName())) {
            identifiedAt.add(ProvUtils.getHostName());
            ProvenanceRecorder.documentAgent(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer(this.getClass().getSimpleName()));
        }
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn,
                ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Unit"), new StringBuffer("constructor"), false, null, null, true);
        ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Unit"), procRID, null, null, null, null, false, false, false, true);
        StopWatch.stop(false);
    }

    public Unit(Object object) {
        super();
        StopWatch.start(false);
        if (MASSProv.provOn && provOn && !identifiedAt.contains(ProvUtils.getHostName())) {
            identifiedAt.add(ProvUtils.getHostName());
            ProvenanceRecorder.documentAgent(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer(this.getClass().getSimpleName()));
        }
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn,
                ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Unit"), new StringBuffer("constructor"), false, new String[]{"object"}, new Object[]{object}, true);

        Random gen = new Random();

        int maxVDist = 3;

        vDist = gen.nextInt(maxVDist) + 1;
        ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("vDist"), vDist_RID, procRID, vDist, true);

        // Set initial sugar level
        sugar = 5;
        ProvenanceRecorder.documentFieldAssignment(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, sugar, procRID, true);

        // Define the neighbors 
        for (int x = 0 - vDist; x <= vDist; x++) {
            ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("vDist"), vDist_RID, procRID, vDist, true);
            for (int y = 0 - vDist; y <= vDist; y++) {
                ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("vDist"), vDist_RID, procRID, vDist, true);
                if (!(x == 0 && y == 0)) {
                    neighbors.add(new int[]{x, y});
                }
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Unit"), procRID, null, null, null, null, false, false, false, true);
        StopWatch.stop(false);
    }

    @Override
    public void substituteConstructorDocumentation() {
        StopWatch.start(false);
        if (MASSProv.provOn && !identifiedAt.contains(ProvUtils.getHostName())) {
            identifiedAt.add(ProvUtils.getHostName());
            ProvenanceRecorder.documentAgent(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer(this.getClass().getSimpleName()));
        }
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Unit"), new StringBuffer("constructor"), false, new String[]{"object"}, new Object[]{new Object()}, true);
        ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("vDist"), vDist_RID, procRID, vDist, true);
        ProvenanceRecorder.documentFieldAssignment(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, sugar, procRID, true);
        ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Unit"), procRID, null, null, null, null, false, false, false, true);
        StopWatch.stop(false);
    }

    /**
     *
     */
    public int getSugarLevel() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getSugarLevel"), null, false, null, null, true);
        ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, procRID, sugar, true);
        ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getSugarLevel"), procRID, new StringBuffer("sugar"), sugar, null, null, false, false, false, true);
        StopWatch.stop(false);
        return sugar;
    }

    /**
     * Instantiate agents randomly around the grid
     */
    public int map(int maxAgents, int[] size, int[] coordinates) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("map"), null, false, new String[]{"maxAgents", "size", "coordinates"}, new Object[]{maxAgents, size, coordinates}, true);
        Random gen = new Random();
        int num = gen.nextInt(size[0]);

        if (num < 5) {
            ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("map"), procRID, new StringBuffer("1"), 1, null, null, false, false, false, true);
            StopWatch.stop(false);
            return 1;
        } else {
            ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("map"), procRID, new StringBuffer("0"), 0, null, null, false, false, false, true);
            StopWatch.stop(false);
            return 0;
        }
    }

    /**
     * Is called from callAll( ) and forwards this call to decideNewePosition( )
     *
     * @param funcId the function Id to call
     * @param args argumenets passed to this funcId.
     */
    public Object callMethod(int funcId, Object args) {
        StopWatch.start(false);
        if (MASSProv.provOn && !identifiedAt.contains(ProvUtils.getHostName())) {
            identifiedAt.add(ProvUtils.getHostName());
            ProvenanceRecorder.documentAgent(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer(this.getClass().getSimpleName()));
        }
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), null, false, new String[]{"funcId", "args"}, new Object[]{funcId, args}, true);
        switch (funcId) {
            case decideNewPosition_:
                ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("decideNewPostion_"), decideNewPosition__RID, procRID, decideNewPosition_, true);
                Object returnVal = decideNewPosition(args);
                ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("decideNewPostion_"), decideNewPosition__RID, procRID, decideNewPosition_, true);
                ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), procRID, new StringBuffer("decideNewPosition(args)"), returnVal, null, null, false, false, false, true);
                StopWatch.stop(false);
                return decideNewPosition(args);
        }
        ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), procRID, new StringBuffer("null"), null, null, null, false, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    /**
     * Computes the index of a next cell to migrate to.
     *
     * @param args formally requested but actually not used
     */ //-----------------------------------------------------------
    public Object decideNewPosition(Object args) {
        StopWatch.start(false);
//        if (!decideNewPositionCalledOnHostLogged) {
//            decideNewPositionCalledOnHostLogged = true;
//            IO.log("decideNewPosition called by agentID:"
//                    + this.getAgentId() + " with UUID:" + UUID);
//        }
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("decideNewPosition"), null, false, new String[]{"args"}, new Object[]{args}, true);
        if (sugar <= MIN_SUGAR) {
            ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, procRID, sugar, true);
            ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("MIN_SUGAR"), MIN_SUGAR_RID, procRID, MIN_SUGAR, true);
            //System.err.println("Unit died... :( ");
            kill();
            ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("decideNewPosition"), procRID, new StringBuffer("null"), null, null, null, false, false, false, true);
            StopWatch.stop(false);
            return null;
        }

        // Get Neighbor List
        int[] nbrLandIdx = ((Land) getPlace()).getNbrLocations(vDist);
        ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("vDist"), vDist_RID, procRID, vDist_RID, true);

        // ERROR checking
        if (nbrLandIdx == null) {
            //System.out.println( "*** ERROR: Neighbor Land Index (nbrLandIdx) is NULL !!! ***");
            System.exit(-1);
        }
        if (nbrLandIdx.length != neighbors.size()) {
            //System.out.println( "*** ERROR: nbrLandIdx length (" + nbrLandIdx.length  
            //			+ ")  !=  neighbor length (" + neighbors.size() + ") ***");
            System.exit(-1);
        }

        int sizeX = getPlace().getSize()[0], sizeY = getPlace().getSize()[1];  	// land size
        int currX = getPlace().getIndex()[0], currY = getPlace().getIndex()[1]; 	// curr index
        int newX = currX, newY = currY; 				// new destination X and Y

        ProvenanceRecorder.documentEntity(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer("decideNewPosition"), procRID, new StringBuffer("currX"), currX, ProvUtils.getUniversalResourceID(new StringBuffer("currX")), null, true);
        ProvenanceRecorder.documentEntity(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer("decideNewPosition"), procRID, new StringBuffer("currY"), currY, ProvUtils.getUniversalResourceID(new StringBuffer("currY")), null, true);

        int localSugar = ((Land) getPlace()).sugar;				// local land sugar level
        int localAgents = ((Land) getPlace()).getNumAgents();// local land agent level
        int localMigrateVal = localSugar - localAgents + 1;		// local land migrate value 
        int largestMigrateVal = localSugar - localAgents;			// largest migrate value (migrate->highest)
        boolean migrate = false;

        // Loop through each of the neighbor locations looking for a 
        // location with the largest migrate value (#sugar - #agents)
        for (int i = 0; i < neighbors.size(); i++) {
            int idx = nbrLandIdx[i];
            int[] neighbor = neighbors.get(i);
            int nbrSugar = ((Land) getPlace()).nbrNumSugar[idx];
            int nbrAgents = ((Land) getPlace()).nbrNumAgents[idx];
            int migrateVal = nbrSugar - nbrAgents;
            ProvenanceRecorder.documentEntity(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer("decideNewPosition"), procRID, new StringBuffer("migrateVal"), migrateVal, ProvUtils.getUniversalResourceID(new StringBuffer("migrateVal")), null, true);

            // Check if neighbor has better migrate value, if so update larget values
            if (migrateVal > largestMigrateVal) {

                largestMigrateVal = migrateVal;
                newX = currX + neighbor[0];
                newY = currY + neighbor[1];
            }
        }

        ProvenanceRecorder.documentEntity(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer("decideNewPosition"), procRID, new StringBuffer("largestMigrateVal"), largestMigrateVal, ProvUtils.getUniversalResourceID(new StringBuffer("largestMigrateVal")), null, true);
        ProvenanceRecorder.documentEntity(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer("decideNewPosition"), procRID, new StringBuffer("localMigrateVal"), localMigrateVal, ProvUtils.getUniversalResourceID(new StringBuffer("localMigrateVal")), null, true);

        // After checking all neighbors, decide wheather to move or not 
        if (largestMigrateVal > localMigrateVal) {
            migrate = true;
        } else {
            boolean consumed = false;
            ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, procRID, sugar, true);
            ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("MAX_SUGAR"), MAX_SUGAR_RID, procRID, MAX_SUGAR, true);
            // Try to consume sugar, set consumed flag to result
            if (sugar <= MAX_SUGAR) {
                consumed = ((Land) getPlace()).consumeSugar();
            }
            ProvenanceRecorder.documentEntity(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer("decideNewPosition"), procRID, new StringBuffer("consumed"), consumed, ProvUtils.getUniversalResourceID(new StringBuffer("consumed")), null, true);
            // If sugar consumed, add sugar to units inventory
            // If unable (sugar value == 0  or  agents sugar value == max) 
            // move to a random neighbor location
            if (consumed) {
                sugar++;
                ProvenanceRecorder.documentFieldAssignment(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, sugar, procRID, true);
            } else {
                Random gen = new Random();
                int tmpVal = gen.nextInt(neighbors.size());
                int[] neighbor = neighbors.get(tmpVal);
                newX = currX + neighbor[0];
                newY = currY + neighbor[1];
                migrate = true;

                //System.err.print("RANDOM ");
            }
        }

        if (migrate) {
            boolean migrateGeneratedValue = migrate(newX, newY);								// Move (migrate) unit
            ProvenanceRecorder.documentEntity(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer("decideNewPosition"), procRID, new StringBuffer("newX"), newX, ProvUtils.getUniversalResourceID(new StringBuffer("newX")), null, true);
            ProvenanceRecorder.documentEntity(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer("decideNewPosition"), procRID, new StringBuffer("newY"), newY, ProvUtils.getUniversalResourceID(new StringBuffer("newY")), null, true);
            ProvenanceRecorder.documentEntity(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, UUID, new StringBuffer("decideNewPosition"), procRID, new StringBuffer("migrateGeneratedValue"), migrateGeneratedValue, ProvUtils.getUniversalResourceID(new StringBuffer("migrateGeneratedValue")), null, true);
            if (sugar > 0) {
                ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, procRID, sugar, true);
                sugar--;						// Move penalty: -1 sugar for each move
                ProvenanceRecorder.documentFieldAssignment(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("sugar"), sugar_RID, sugar, procRID, true);
            }        	//System.err.println("Migrating... (" +currX+ ")," +currY+ ") -> (" +newX+ ")," +newY+ ")" );
        }
        ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("decideNewPosition"), procRID, new StringBuffer("null"), null, null, null, false, false, false, true);
        StopWatch.stop(false);
        return null;
    }

    @Override
    public void storeTransitiveProvenanceState(Object stateData) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object retrieveTransitiveProvenanceState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        AgentFilter.filter(MASSProv.agentFilter, this, MASSProv.agentFilterCriteria);
    }
}
