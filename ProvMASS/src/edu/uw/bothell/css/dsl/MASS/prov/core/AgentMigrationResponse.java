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
package edu.uw.bothell.css.dsl.MASS.prov.core;
// PERFORMANCE DOCUMENTED

import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.Serializable;

@SuppressWarnings("serial")
public class AgentMigrationResponse implements Serializable {

    private int numOfAgentReceived;
    private boolean chosenAsParentPid;

    public AgentMigrationResponse(int nOA, boolean chosen) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("AgentMigrationResponse"), new StringBuffer("label"), true, new String[]{"nOA", "chosen"}, new Object[]{nOA, chosen});
        numOfAgentReceived = nOA;
        chosenAsParentPid = chosen;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("AgentMigrationResponse"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public int getNumOfAgentReceived() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getNumOfAgentReceived"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getNumOfAgentReceived"), procRID, new StringBuffer("numOfAgentReceived"), numOfAgentReceived, null, null, true, false, false);
        StopWatch.stop(false);
        return numOfAgentReceived;
    }

    public boolean isChosenAsParentPid() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isChosenAsParentPid"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isChosenAsParentPid"), procRID, new StringBuffer("this.chosenAsParentPid"), this.chosenAsParentPid, null, null, true, false, false);
        StopWatch.stop(false);
        return this.chosenAsParentPid;
    }
}
