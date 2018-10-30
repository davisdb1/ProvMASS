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

/**
 * Created by Nicolas on 8/9/2015.
 */
import java.io.Serializable;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 *
 * @author Nicolas
 */
@SuppressWarnings("serial")
public class InitialData extends MASSPacket implements Serializable {

    //The name of the users mass application
    private String agentsName = "";

    //places file name
    private String placesName = "";

    //The number of places in the x dimension
    private int placesX;

    //The number of places in the Y dimension
    private int placesY;

    //The number of places
    private int numberOfPlaces;

    //The number of agents
    private int numberOfAgents;

    //does place overload setDebugData()
    private boolean placeOverloadsSetDebugData;

    //does place overload getDebugData()
    private boolean placeOverloadsGetDebugData;

    //does agent overload setDebugData()
    private boolean agentOverloadsSetDebugData;

    //does agent overload getDebugData()
    private boolean agentOverloadsGetDebugData;

    //data type of place debug data, must extend Number
    private Class<? extends Number> placeDataType;

    //data type of agent debug data, must extend number
    private Class<? extends Number> agentDataType;

    /**
     * Default constructor
     *
     * Defaults all members to java default values. No touching!
     *
     */
    public InitialData() {
    }

    /**
     * JSON constructor
     *
     * This constructor is strictly for instantiating InitialData packet from
     * the given JSON string, and is only called from MASSCppConnection.java.
     * MASSCppConnection gathers the JSON string from a MASS C++ simulation and
     * is java-fied here so that communication between the JAVA GUI and the MASS
     * C++ library is possible.
     *
     * *WARNING* edits to this constructor must be reflected in multiple places
     * though out this project including the MASS C++ debugInit() method as well
     * as the toJsonStirng() methods. And probably other places *Danger!*
     *
     * @param jsonString
     */
    public InitialData(String jsonString) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("InitialData"), new StringBuffer("label"), true, new String[]{"jsonString"}, new Object[]{jsonString});
        //convert JSON to GSON
        JsonObject jsonObject = new JsonParser().parse(jsonString.trim()).getAsJsonObject();
        JsonObject packet = jsonObject.getAsJsonObject("packet");

        //convert GSON to this object
        this.agentsName = packet.get("agentsName").getAsString();
        this.placesName = packet.get("placesName").getAsString();
        this.placesX = packet.get("placesX").getAsInt();
        this.placesY = packet.get("placesY").getAsInt();
        this.numberOfPlaces = packet.get("numberOfPlaces").getAsInt();
        this.numberOfAgents = packet.get("numberOfAgents").getAsInt();
        this.placesX = packet.get("placesX").getAsInt();
        this.placeOverloadsSetDebugData = packet.get("placeOverloadsSetDebugData").getAsBoolean();
        this.placeOverloadsGetDebugData = packet.get("placeOverloadsGetDebugData").getAsBoolean();
        this.agentOverloadsSetDebugData = packet.get("agentOverloadsSetDebugData").getAsBoolean();
        this.agentOverloadsGetDebugData = packet.get("agentOverloadsGetDebugData").getAsBoolean();

        //get object type of overloaded debug data for agents and places, must extend Number
        String pDataType = packet.get("placeDataType").getAsString();
        String aDataType = packet.get("agentDataType").getAsString();

        if (pDataType.trim().isEmpty()) {
            this.placeDataType = null;
        } else {
            try {
                placeDataType = (Class<? extends Number>) Class.forName("java.lang." + pDataType);
            } catch (ClassNotFoundException | ClassCastException ex) {
                System.out.println("No such class: " + pDataType);
                this.placeDataType = null;
            }
        }

        if (aDataType.trim().isEmpty()) {
            this.agentDataType = null;
        } else {
            try {
                agentDataType = (Class<? extends Number>) Class.forName("java.lang." + pDataType);
            } catch (ClassNotFoundException | ClassCastException ex) {
                System.out.println("No such class: " + aDataType);
                this.agentDataType = null;
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("InitialData"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * getApplicationName
     *
     * Gets the name of the user defines MASS application.
     *
     * @return the name of the application
     */
    public String getAgentsName() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAgentsName"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAgentsName"), procRID, new StringBuffer("this.agentsName"), this.agentsName, null, new StringBuffer("the name of the application"), true, false, false);
        StopWatch.stop(false);
        return this.agentsName;
    }

    /**
     * getPlacesX
     *
     * Gets the number of places in the x dimension
     *
     * @return the number of places in the x dimension
     */
    public int getPlacesX() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPlacesX"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPlacesX"), procRID, new StringBuffer("this.placesX"), this.placesX, null, new StringBuffer("the number of places in the x dimension"), true, false, false);
        StopWatch.stop(false);
        return this.placesX;
    }

    /**
     * getPlacesY
     *
     * Gets the number of places in the y dimension
     *
     * @return the number of places in the y dimension
     */
    public int getPlacesY() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPlacesY"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPlacesY"), procRID, new StringBuffer("this.placesY"), this.placesY, null, new StringBuffer("the number of places in the y dimension"), true, false, false);
        StopWatch.stop(false);
        return this.placesY;
    }

    /**
     * getNumberOfPlaces
     *
     * Gets the number of places
     *
     * @return the number of places
     */
    public int getNumberOfPlaces() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getNumberOfPlaces"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getNumberOfPlaces"), procRID, new StringBuffer("this.numberOfPlaces"), this.numberOfPlaces, null, new StringBuffer("the number of places"), true, false, false);
        StopWatch.stop(false);
        return this.numberOfPlaces;
    }

    /**
     * getNumberOfAgents
     *
     * Gets the number of agents
     *
     * @return the number of agents
     */
    public int getNumberOfAgents() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getNumberOfAgents"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getNumberOfAgents"), procRID, new StringBuffer("this.numberOfAgents"), this.numberOfAgents, null, new StringBuffer("the number of agents"), true, false, false);
        StopWatch.stop(false);
        return this.numberOfAgents;
    }

    public void setPlacesX(int placesX) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPlacesX"), new StringBuffer("label"), true, new String[]{"placesX"}, new Object[]{placesX});
        this.placesX = placesX;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPlacesX"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public void setPlacesY(int placesY) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPlacesY"), new StringBuffer("label"), true, new String[]{"placesY"}, new Object[]{placesY});
        this.placesY = placesY;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPlacesY"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public void setNumberOfPlaces(int numberOfPlaces) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setNumberOfPlaces"), new StringBuffer("label"), true, new String[]{"numberOfPlaces"}, new Object[]{numberOfPlaces});
        this.numberOfPlaces = numberOfPlaces;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setNumberOfPlaces"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public void setNumberOfAgents(int numberOfAgents) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setNumberOfAgents"), new StringBuffer("label"), true, new String[]{"numberOfAgents"}, new Object[]{numberOfAgents});
        this.numberOfAgents = numberOfAgents;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setNumberOfAgents"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public void setAgentsName(String agentsName) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setAgentsName"), new StringBuffer("label"), true, new String[]{"agentsName"}, new Object[]{agentsName});
        this.agentsName = agentsName;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setAgentsName"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public String getPlacesName() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPlacesName"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPlacesName"), procRID, new StringBuffer("placesName"), placesName, null, null, true, false, false);
        StopWatch.stop(false);
        return placesName;
    }

    public void setPlacesName(String placesName) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPlacesName"), new StringBuffer("label"), true, new String[]{"placesName"}, new Object[]{placesName});
        this.placesName = placesName;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPlacesName"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public boolean placeOverloadsSetDebugData() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("placeOverloadsSetDebugData"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("placeOverloadsSetDebugData"), procRID, new StringBuffer("placeOverloadsSetDebugData"), placeOverloadsSetDebugData, null, null, true, false, false);
        StopWatch.stop(false);
        return placeOverloadsSetDebugData;
    }

    public void placeOverloadsSetDebugData(boolean placeOverloadsSetDebugData) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("placeOverloadsSetDebugData"), new StringBuffer("label"), true, new String[]{"placeOverloadsSetDebugData"}, new Object[]{placeOverloadsSetDebugData});
        this.placeOverloadsSetDebugData = placeOverloadsSetDebugData;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("placeOverloadsSetDebugData"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public boolean placeOverloadsGetDebugData() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("placeOverloadsGetDebugData"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("placeOverloadsGetDebugData"), procRID, new StringBuffer("placeOverloadsGetDebugData"), placeOverloadsGetDebugData, null, null, true, false, false);
        StopWatch.stop(false);
        return placeOverloadsGetDebugData;
    }

    public void placeOverloadsGetDebugData(boolean placeOverloadsGetDebugData) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("placeOverloadsGetDebugData"), new StringBuffer("label"), true, new String[]{"placeOverloadsGetDebugData"}, new Object[]{placeOverloadsGetDebugData});
        this.placeOverloadsGetDebugData = placeOverloadsGetDebugData;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("placeOverloadsGetDebugData"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public boolean agentOverloadsSetDebugData() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("agentOverloadsSetDebugData"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("agentOverloadsSetDebugData"), procRID, new StringBuffer("agentOverloadsSetDebugData"), agentOverloadsSetDebugData, null, null, true, false, false);
        StopWatch.stop(false);
        return agentOverloadsSetDebugData;
    }

    public void agentOverloadsSetDebugData(boolean agentOverloadsSetDebugData) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("agentOverloadsSetDebugData"), new StringBuffer("label"), true, new String[]{"agentOverloadsSetDebugData"}, new Object[]{agentOverloadsSetDebugData});
        this.agentOverloadsSetDebugData = agentOverloadsSetDebugData;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("agentOverloadsSetDebugData"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public boolean agentOverloadsGetDebugData() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("agentOverloadsGetDebugData"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("agentOverloadsGetDebugData"), procRID, new StringBuffer("agentOverloadsGetDebugData"), agentOverloadsGetDebugData, null, null, true, false, false);
        StopWatch.stop(false);
        return agentOverloadsGetDebugData;
    }

    public void agentOverloadsGetDebugData(boolean agentOverloadsGetDebugData) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("agentOverloadsGetDebugData"), new StringBuffer("label"), true, new String[]{"agentOverloadsGetDebugData"}, new Object[]{agentOverloadsGetDebugData});
        this.agentOverloadsGetDebugData = agentOverloadsGetDebugData;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("agentOverloadsGetDebugData"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public Class<? extends Number> getPlaceDataType() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPlaceDataType"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPlaceDataType"), procRID, new StringBuffer("placeDataType"), placeDataType, null, null, true, false, false);
        StopWatch.stop(false);
        return placeDataType;
    }

    public void setPlaceDataType(Class<? extends Number> placeDataType) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPlaceDataType"), new StringBuffer("label"), true, new String[]{"placeDataType"}, new Object[]{placeDataType});
        this.placeDataType = placeDataType;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPlaceDataType"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public Class<? extends Number> getAgentDataType() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAgentDataType"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getAgentDataType"), procRID, new StringBuffer("agentDataType"), agentDataType, null, null, true, false, false);
        StopWatch.stop(false);
        return agentDataType;
    }

    public void setAgentDataType(Class<? extends Number> agentDataType) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setAgentDataType"), new StringBuffer("label"), true, new String[]{"agentDataType"}, new Object[]{agentDataType});
        this.agentDataType = agentDataType;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setAgentDataType"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * toJSONString overridden method
     *
     * This method is to only be called by MASSCppConnection.java. This is a
     * valid JSON representation of this object with default values. This string
     * is sent to MASS c++ library and serves as a skeleton that is to be filled
     * in by MASS C++ and returned here via the InitialData(jsonString)
     * constructor to be re java-fied.
     *
     * *WARNING* all edits here must be reflected in the
     * InitialData(jsonString) constructor as well as the debugInit() method in
     * MASS.cpp.
     *
     * @return
     */
    @Override
    public String toJSONString() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("toJSONString"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("toJSONString"), procRID, new StringBuffer("{\"packet\":{\"agentsName\":\"\"),\"placesName\":\"\"),\"placesX\":0,\"placesY\":0,\"numberOfPlaces\":0,\"numberOfAgents\":0,\"placeOverloadsSetDebugData\":false,\"placeOverloadsGetDebugData\":false,\"agentOverloadsSetDebugData\":false,\"agentOverloadsGetDebugData\":false,\"placeDataType\":\"\"),\"agentDataType\":\"\"},\"request\":0}"), new StringBuffer("{\"packet\":{\"agentsName\":\"\"),\"placesName\":\"\"),\"placesX\":0,\"placesY\":0,\"numberOfPlaces\":0,\"numberOfAgents\":0,\"placeOverloadsSetDebugData\":false,\"placeOverloadsGetDebugData\":false,\"agentOverloadsSetDebugData\":false,\"agentOverloadsGetDebugData\":false,\"placeDataType\":\"\"),\"agentDataType\":\"\"},\"request\":0}"), null, new StringBuffer(""), true, false, false);
        StopWatch.stop(false);
        return "{\"packet\":{\"agentsName\":\"\"),\"placesName\":\"\"),\"placesX\":0,\"placesY\":0,\"numberOfPlaces\":0,\"numberOfAgents\":0,\"placeOverloadsSetDebugData\":false,\"placeOverloadsGetDebugData\":false,\"agentOverloadsSetDebugData\":false,\"agentOverloadsGetDebugData\":false,\"placeDataType\":\"\"),\"agentDataType\":\"\"},\"request\":0}";
    }
}
