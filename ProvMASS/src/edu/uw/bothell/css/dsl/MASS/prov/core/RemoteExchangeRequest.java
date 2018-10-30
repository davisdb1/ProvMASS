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
public class RemoteExchangeRequest implements Serializable {

    private int destinationGlobalLinearIndex;
    private int originatingGlobalLinearIndex;
    private int inMessageIndex;
    private Object outMessage;

    public RemoteExchangeRequest(int destinationIndex, int originatingIndex, int inMessageIndex, Object outMessage) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("RemoteExchangeRequest"), new StringBuffer("label"), true, new String[]{"destinationIndex", "originatingIndex", "inMessageIndex", "outMessage"}, new Object[]{destinationIndex, originatingIndex, inMessageIndex, outMessage});

        this.destinationGlobalLinearIndex = destinationIndex;
        this.originatingGlobalLinearIndex = originatingIndex;
        this.inMessageIndex = inMessageIndex;
        this.outMessage = outMessage;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("RemoteExchangeRequest"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public int getDestGlobalLinearIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getDestGlobalLinearIndex"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getDestGlobalLinearIndex"), procRID, new StringBuffer("destinationGlobalLinearIndex"), destinationGlobalLinearIndex, null, null, true, false, false);
        StopWatch.stop(false);
        return destinationGlobalLinearIndex;
    }

    public int getInMessageIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getInMessageIndex"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getInMessageIndex"), procRID, new StringBuffer("inMessageIndex"), inMessageIndex, null, null, true, false, false);
        StopWatch.stop(false);
        return inMessageIndex;
    }

    public int getOrgGlobalLinearIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getOrgGlobalLinearIndex"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getOrgGlobalLinearIndex"), procRID, new StringBuffer("originatingGlobalLinearIndex"), originatingGlobalLinearIndex, null, null, true, false, false);
        StopWatch.stop(false);
        return originatingGlobalLinearIndex;
    }

    public Object getOutMessage() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getOutMessage"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getOutMessage"), procRID, new StringBuffer("outMessage"), outMessage, null, null, true, false, false);
        StopWatch.stop(false);
        return outMessage;
    }

}
