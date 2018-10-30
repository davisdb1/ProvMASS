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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nicolas on 8/10/2015.
 */
@SuppressWarnings("serial")
public class UpdatePackage extends MASSPacket implements Serializable {

    private PlaceData[] placeData;

    public PlaceData[] getPlaceData() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("UpdatePackage"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("UpdatePackage"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return placeData;
    }

    public void setPlaceData(PlaceData[] placeData) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPlaceData"), new StringBuffer("label"), true, new String[]{"placeData"}, new Object[]{placeData});
        this.placeData = placeData;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPlaceData"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public UpdatePackage() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("UpdatePackage"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("UpdatePackage"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public UpdatePackage(InitialData data) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("UpdatePackage"), new StringBuffer("label"), true, new String[]{"data"}, new Object[]{data});
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("UpdatePackage"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public UpdatePackage(String jsonString) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("UpdatePackage"), new StringBuffer("label"), true, new String[]{"jsonString"}, new Object[]{jsonString});
        JsonObject jsonObject = new JsonParser().parse(jsonString.trim()).getAsJsonObject();
        JsonObject packet = jsonObject.getAsJsonObject("packet");
        JsonArray jsonArr = packet.get("PlaceArray").getAsJsonArray();
        Gson googleJson = new Gson();

        ArrayList<LinkedTreeMap> list = googleJson.fromJson(jsonArr, ArrayList.class);
        PlaceData[] placeDataArr = new PlaceData[list.size()];

        for (int i = 0; i < list.size(); i++) {
            LinkedTreeMap<String, Object> map = list.get(i);
            PlaceData newPlace = new PlaceData();

            newPlace.setThisPlaceData((Double) map.get("Number"));
            newPlace.setIndex(((Double) map.get("index")).intValue());
            newPlace.setHasAgents(((Double) map.get("hasAgents")).intValue() == 1);

            placeDataArr[i] = newPlace;

            ArrayList<LinkedTreeMap> arr = (ArrayList) map.get("AgentArray");
            AgentData[] agents = new AgentData[arr.size()];

            for (int j = 0; j < arr.size(); i++) {
                LinkedTreeMap<String, Object> agentMap = arr.get(j);
                AgentData agent = new AgentData();

                agent.setIsAlive((Double) agentMap.get("isAlive") == 1.);
                agent.setId(((Double) agentMap.get("id")).intValue());
                agent.setDebugData((Double) agentMap.get("Number"));
                agent.setIndex(((Double) agentMap.get("index")).intValue());
                agent.setChildren(((Double) agentMap.get("children")).intValue());

                agents[j] = agent;
            }

            placeDataArr[i].setAgentDataOnThisPlace(agents);
        }
        placeData = placeDataArr;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("UpdatePackage"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    @Override
    public String toJSONString() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("toJSONString"), new StringBuffer("label"), true, null, null);
        String jsonString = "{\"packet\":{\"PlaceArray\":[";
        jsonString += "]}, \"request\":1}";
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("toJSONString"), procRID, new StringBuffer("jsonString"), jsonString, null, null, true, false, false);
        StopWatch.stop(false);
        return jsonString;
    }
}
