/*

 	MASS Java Software License
	© 2012-2016 University of Washington

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	The following acknowledgment shall be used where appropriate in publications, presentations, etc.:      

	© 2012-2016 University of Washington. MASS was developed by Computing and Software Systems at University of 
	Washington Bothell.

	THE SOFTWARE IS PROVIDED "AS IS"), WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.

 */
package edu.uw.bothell.css.dsl.MASS.prov.core.MassData;
// PERFORMANCE DOCUMENTED

import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.Serializable;

/**
 * Created by Nicolas on 8/10/2015.
 */
@SuppressWarnings("serial")
public class PlaceData extends MASSPacket implements Serializable {

    //this places data
    private Number thisPlaceData;

    //this places index
    private int index;

    //if this place contains agents
    private boolean hasAgents = false;

    //an array of agent data residing on this place
    private AgentData[] agentDataOnThisPlace;

    public Number getThisPlaceData() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getThisPlaceData"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getThisPlaceData"), procRID, new StringBuffer("thisPlaceData"), thisPlaceData, null, null, true, false, false);
        StopWatch.stop(false);
        return thisPlaceData;
    }

    public void setThisPlaceData(Number thisPlaceData) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setThisPlaceData"), new StringBuffer("label"), true, new String[]{"thisPlaceData"}, new Object[]{thisPlaceData});
        this.thisPlaceData = thisPlaceData;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setThisPlaceData"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public boolean isHasAgents() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isHasAgents"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isHasAgents"), procRID, new StringBuffer("hasAgents"), hasAgents, null, null, true, false, false);
        StopWatch.stop(false);
        return hasAgents;
    }

    public void setHasAgents(boolean hasAgents) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setHasAgents"), new StringBuffer("label"), true, new String[]{"hasAgents"}, new Object[]{hasAgents});
        this.hasAgents = hasAgents;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setHasAgents"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public AgentData[] getAgentDataOnThisPlace() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAgentDataOnThisPlace"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAgentDataOnThisPlace"), procRID, new StringBuffer("agentDataOnThisPlace"), agentDataOnThisPlace, null, null, true, false, false);
        StopWatch.stop(false);
        return agentDataOnThisPlace;
    }

    public void setAgentDataOnThisPlace(AgentData[] agentDataOnThisPlace) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setAgentDataOnThisPlace"), new StringBuffer("label"), true, new String[]{"agentDataOnThisPlace"}, new Object[]{agentDataOnThisPlace});
        this.agentDataOnThisPlace = agentDataOnThisPlace;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setAgentDataOnThisPlace"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public int getIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getIndex"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getIndex"), procRID, new StringBuffer("index"), index, null, null, true, false, false);
        StopWatch.stop(false);
        return index;
    }

    public void setIndex(int index) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setIndex"), new StringBuffer("label"), true, new String[]{"index"}, new Object[]{index});
        this.index = index;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setIndex"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    @Override
    public String toJSONString() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("toJSONString"), new StringBuffer("label"), true, null, null);
        String jsonString = "{\"Number\":0,\"index\":0,\"hasAgents\":false,\"AgentArray\":[";
        jsonString += agentDataOnThisPlace[0].toJSONString();
        for (int i = 1; i < agentDataOnThisPlace.length; i++) {
            jsonString += ")," + agentDataOnThisPlace[i].toJSONString();
        }
        jsonString += "]}";
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("toJSONString"), procRID, new StringBuffer("jsonString"), jsonString, null, null, true, false, false);
        StopWatch.stop(false);
        return jsonString;

    }
}
