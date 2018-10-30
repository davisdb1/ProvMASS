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
package edu.uw.bothell.css.dsl.MASS.prov.QuickStart;

import edu.uw.bothell.css.dsl.MASS.prov.MASSProv;
import edu.uw.bothell.css.dsl.MASS.prov.ProvEnabledObject;
import edu.uw.bothell.css.dsl.MASS.prov.core.Place;
import edu.uw.bothell.css.dsl.MASS.prov.filter.PlaceFilter;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceStore;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.net.InetAddress;
import java.util.Arrays;

public class Matrix extends Place implements ProvEnabledObject {

    private Object obj;
    private StringBuffer obj_RID = ProvUtils.getUniversalResourceID(new StringBuffer("obj"));
    public static final int GET_HOSTNAME = 0;
    public static final StringBuffer GET_HOSTNAME_RID = ProvUtils.getUniversalResourceID(new StringBuffer("GET_HOSTNAME"));
    private final StringBuffer UUID = ProvUtils.getUniversalResourceID(new StringBuffer(this.getClass().getSimpleName()));

    /**
     * This constructor will be called upon instantiation by MASS The Object
     * supplied MAY be the same object supplied when Places was created
     *
     * @param obj
     */
    public Matrix(Object obj) {
        StopWatch.start(false);
        ProvenanceRecorder.documentProvEnabledObject(provOn,
                ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer(UUID),
                new StringBuffer(this.getClass().getSimpleName()), true, new StringBuffer("PLACE"));
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn,
                ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Matrix"), new StringBuffer("constructor"), false, null,
                null, true);
        this.obj = obj;
        ProvenanceRecorder.documentFieldAssignment(provOn,
                ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("obj"), obj_RID, obj, procRID, true);
        ProvenanceRecorder.endProcedureDocumentation(provOn,
                ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Matrix"), procRID, null, null, null, null, false, false, false, true);
        StopWatch.stop(false);
    }

    @Override
    public void substituteConstructorDocumentation() {
        StopWatch.start(false);
        ProvenanceRecorder.documentProvEnabledObject(provOn,
                ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer(UUID), new StringBuffer(this.getClass().getSimpleName()), true, new StringBuffer("PLACE"));
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn,
                ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Matrix"), new StringBuffer("constructor"), false, null, null, true);
        ProvenanceRecorder.documentFieldAssignment(provOn,
                ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("obj"), obj_RID, obj, procRID, true);
        ProvenanceRecorder.endProcedureDocumentation(provOn,
                ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("Matrix"), procRID, null, null, null, null, false, false, false, true);
        StopWatch.stop(false);
    }

    /**
     * This method is called when "callAll" is invoked from the master node
     */
    public Object callMethod(int method, Object o) {
        StopWatch.start(false);
        ProvenanceStore store = ProvUtils.getStoreOfCurrentThread(provOn);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), null, true, new String[]{"method", "o"}, new Object[]{method, o}, true);
        Object toReturn;
        switch (method) {
            case GET_HOSTNAME:
                ProvenanceRecorder.documentFieldAccess(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), new StringBuffer("GET_HOSTNAME"), GET_HOSTNAME_RID, procRID, GET_HOSTNAME, true);
                toReturn = findEntryHost(o);
                break;
            default:
                toReturn = "Unknown Method Number: " + method;
                break;
        }
        ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), procRID, new StringBuffer("toReturn"), toReturn, null, null, false, false, false, true);
        StopWatch.stop(false);
        return toReturn;
    }

    public Object findEntryHost(Object o) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("findEntryHost"), null, false, true);
        try {

            StringBuilder hostMsgBuilder = new StringBuilder("Place #");
            for (int i = 0, im = getIndex().length; i < im; i++) {
                hostMsgBuilder.append("[").append(getIndex()[i]).append("]");
            }
            String hostMsg = hostMsgBuilder.append(" located at: ")
                    .append(InetAddress.getLocalHost().getCanonicalHostName())
                    .toString();

            ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("findEntryHost"), procRID, new StringBuffer("hostMsg"), hostMsg, null, null, true, false, false, true);
            StopWatch.stop(false);
            return hostMsg;
        } catch (Exception e) {
            String hostMsg = "Error : " + e.getClass().getSimpleName() + " : "
                    + e.getMessage() + Arrays.toString(e.getStackTrace());
            ProvenanceRecorder.endProcedureDocumentation(this.provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("findEntryHost"), procRID, new StringBuffer("hostMsg"), hostMsg, null, null, true, false, false, true);

            StopWatch.stop(false);
            return hostMsg;
        }
    }

@Override
    public String getOwnerUUID() {
        if(UUID == null) {
            return null;
        }else {
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
