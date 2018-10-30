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
 * Created by Nicolas on 8/13/2015.
 */
@SuppressWarnings("serial")
public class AgentData extends MASSPacket implements Serializable {

    private boolean isAlive;
    private int id;
    private Number debugData;
    private int index;
    private int children;

    public boolean isAlive() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isAlive"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isAlive"), procRID, new StringBuffer("isAlive"), isAlive, null, null, true, false, false);
        StopWatch.stop(false);
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setIsAlive"), new StringBuffer("label"), true, new String[]{"isAlive"}, new Object[]{isAlive});
        this.isAlive = isAlive;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setIsAlive"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public int getId() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getId"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getId"), procRID, new StringBuffer("id"), id, null, null, true, false, false);
        StopWatch.stop(false);
        return id;
    }

    public void setId(int id) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setId"), new StringBuffer("label"), true, new String[]{"id"}, new Object[]{id});
        this.id = id;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setId"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public Number getDebugData() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getDebugData"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getDebugData"), procRID, new StringBuffer("debugData"), debugData, null, null, true, false, false);
        StopWatch.stop(false);
        return debugData;
    }

    public void setDebugData(Number debugData) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setDebugData"), new StringBuffer("label"), true, new String[]{"debugData"}, new Object[]{debugData});
        this.debugData = debugData;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setDebugData"), procRID, null, null, null, null, true, false, false);
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

    public int getChildren() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getChildren"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getChildren"), procRID, new StringBuffer("children"), children, null, null, true, false, false);
        StopWatch.stop(false);
        return children;
    }

    public void setChildren(int children) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setChildren"), new StringBuffer("label"), true, new String[]{"children"}, new Object[]{children});
        this.children = children;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setChildren"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    @Override
    public String toJSONString() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("toJSONString"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("toJSONString"), procRID, new StringBuffer("{\"isAlive\":false,\"id\":0,\"Number\":0\"index\":0\"children\":0}"), new StringBuffer("{\"isAlive\":false,\"id\":0,\"Number\":0\"index\":0\"children\":0}"), null, null, true, false, false);
        StopWatch.stop(false);
        return "{\"isAlive\":false,\"id\":0,\"Number\":0\"index\":0\"children\":0}";
    }
}
