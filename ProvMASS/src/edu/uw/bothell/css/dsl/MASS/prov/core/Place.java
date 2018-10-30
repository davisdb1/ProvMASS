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
import java.util.Collections; // for synchronized set
import java.util.HashSet;     // implementation for Agent bag
import java.util.Set;         // local Agent bag
import java.util.Vector;

/**
 * Place represents a single element from a collection of places distributed
 * among all cluster nodes. A Place may contain a collection of Agents that
 * perform operations on objects contained within the Place.
 *
 */
public class Place {

    /**
     * Defines the size of the matrix that consists of application-specific
     * places. Intuitively, size[0], size[1], and size[2] correspond to the size
     * of x, y, and z, or that of i, j, and k.
     */
    private int[] size;

    /**
     * Is an array that maintains each place’s coordinates. Intuitively,
     * index[0], index[1], and index[2] correspond to coordinates of x, y, and
     * z, or those of i, j, and k.
     */
    private int[] index;

    /**
     * Stores a set arguments to be passed to a set of remote-cell functions
     * that will be invoked by exchangeAll( ) or exchangeSome( ) in the nearest
     * future. The argument size must be specified with outMessage_size.
     */
    private Object outMessage = null;

    /**
     * Receives a return value in inMessages[i] from a function call made to the
     * i-th remote cell through exchangeAll( ) and exchangeSome( ). Each element
     * size must be specified with inMessage_size.
     */
    private Object[] inMessages = null;

    /**
     * Includes all the agents residing locally on this place.
     */
    private Set<Agent> agents = Collections.synchronizedSet(new HashSet<Agent>());

    private Vector< int[]> neighbors = null;

    protected boolean provOn = false;

    /**
     * Is called from Places.callAll( ), callSome( ), exchangeAll( ), and
     * exchangeSome( ), and invoke the function specified with functionId as
     * passing arguments to this function. A user-derived Place class must
     * implement this method.
     *
     * @param functionId
     * @param argument
     * @return
     */
    public Object callMethod(int functionId, Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), new StringBuffer("label"), true, new String[]{"functionId","argument"}, new Object[]{functionId, argument});
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("callMethod"), procRID, new StringBuffer("null"), null, null, new StringBuffer(""), true, false, false);
        StopWatch.stop(false);
        return null;
    }

    private Place findDstPlace(int handle, int offset[]) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("findDstPlace"), new StringBuffer("label"), true, new String[]{"handle","offset"}, new Object[]{handle, offset});

        // compute the global linear index from offset[]
        PlacesBase places = MASSBase.getPlacesMap().get(new Integer(handle));
        int[] neighborCoord = new int[places.getSize().length];
        places.getGlobalNeighborArrayIndex(index, offset, places.getSize(),
                neighborCoord);
        int globalLinearIndex
                = places.getGlobalLinearIndexFromGlobalArrayIndex(neighborCoord,
                        places.getSize());

        if (globalLinearIndex == Integer.MIN_VALUE) {
            ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("findDstPlace"), procRID, new StringBuffer("null"), null, null, null, true, false, false);
            return null;
        }

        // identify the destination place  
        int destinationLocalLinearIndex
                = globalLinearIndex - places.getLowerBoundary();

        Place destintationPlace = null;
        int shadowIndex;
        if (destinationLocalLinearIndex >= 0
                && destinationLocalLinearIndex < places.getPlacesSize()) {
            destintationPlace = places.getPlaces()[destinationLocalLinearIndex];
        } else if (destinationLocalLinearIndex < 0
                && (shadowIndex = destinationLocalLinearIndex
                + places.getShadowSize()) >= 0) {
            destintationPlace = places.getLeftShadow()[shadowIndex];
        } else if ((shadowIndex
                = destinationLocalLinearIndex - places.getPlacesSize()) >= 0
                && shadowIndex < places.getShadowSize()) {
            destintationPlace = places.getRightShadow()[shadowIndex];
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("findDstPlace"), procRID, new StringBuffer("destintationPlace"), destintationPlace, null, null, true, false, false);
        StopWatch.stop(false);
        return destintationPlace;

    }

    public synchronized Set<Agent> getAgents() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getAgents"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getAgents"), procRID, new StringBuffer("agents"), agents, null, null, true, false, false);
        StopWatch.stop(false);
        return agents;
    }

    public int getNumAgents() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getNumAgents"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getNumAgents"), procRID, new StringBuffer("agents.size()"), agents.size(), null, null, true, false, false);
        StopWatch.stop(false);
        return agents.size();
    }

    public Number getDebugData() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getDebugData"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getDebugData"), procRID, new StringBuffer("null"), null, null, null, true, false, false);
        StopWatch.stop(false);
        return null;
    }

    // To be overridden by developer - for debugging
    public void setDebugData(Number argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setDebugData"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument});
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setDebugData"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public int[] getIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getIndex"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getIndex"), procRID, new StringBuffer("index"), index, null, null, true, false, false);
        StopWatch.stop(false);
        return index;
    }

    public Object[] getInMessages() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getInMessages"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getInMessages"), procRID, new StringBuffer("inMessages"), inMessages, null, null, true, false, false);
        StopWatch.stop(false);
        return inMessages;
    }

    public Vector<int[]> getNeighbours() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getNeighbours"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getNeighbours"), procRID, new StringBuffer("neighbors"), neighbors, null, null, true, false, false);
        StopWatch.stop(false);
        return neighbors;
    }

    public void setNeighbors(Vector<int[]> neighbors) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setNeighbors"), new StringBuffer("label"), true, new String[]{"neighbors"}, new Object[]{neighbors});
        this.neighbors = neighbors;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setNeighbors"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected Object getOutMessage() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getOutMessage"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getOutMessage"), procRID, new StringBuffer("outMessage"), outMessage, null, null, true, false, false);
        StopWatch.stop(false);
        return outMessage;
    }

    public Object getOutMessage(int handle, int[] offsetIndex) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getOutMessage"), new StringBuffer("label"), true, new String[]{"handle", "offsetIndex"}, new Object[]{handle, offsetIndex});

        Place dstPlace = findDstPlace(handle, offsetIndex);

        // return the destination outMessage
        Object outMsg = (dstPlace != null) ? dstPlace.outMessage : null;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getOutMessage"), procRID, new StringBuffer("outMsg"), outMsg, null, null, true, false, false);
        StopWatch.stop(false);
        return outMsg;

    }

    /**
     * Returns the size of the matrix that consists of application-specific
     * places. Intuitively, size[0], size[1], and size[2] correspond to the size
     * of x, y, and z, or that of i, j, and k.
     *
     * @return
     */
    public int[] getSize() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getSize"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getSize"), procRID, new StringBuffer("size"), size, null, new StringBuffer(""), true, false, false);
        StopWatch.stop(false);
        return size;
    }

    protected void putInMessage(int handle, int[] offsetIndex, int position, Object value) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("putInMessage"), new StringBuffer("label"), true, new String[]{"handle","offsetIndex","position","value"}, new Object[]{handle, offsetIndex, position, value});

        Place dstPlace = findDstPlace(handle, offsetIndex);
        // write to the destination inMessage[position]
        if (dstPlace != null && position < dstPlace.inMessages.length) {
            dstPlace.inMessages[position] = value;
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("putInMessage"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected void setIndex(int[] index) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setIndex"), new StringBuffer("label"), true, new String[]{"index"}, new Object[]{index});
        this.index = index.clone();
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setIndex"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    // To be overridden by developer - for debugging
    public void setDebugData(Object argument) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setDebugData"), new StringBuffer("label"), true, new String[]{"argument"}, new Object[]{argument});
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setDebugData"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public void setInMessages(Object[] inMessages) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setInMessages"), new StringBuffer("label"), true, new String[]{"inMessages"}, new Object[]{inMessages});
        this.inMessages = inMessages;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setInMessages"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public void setOutMessage(Object outMessage) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setOutMessage"), new StringBuffer("label"), true, new String[]{"outMessage"}, new Object[]{outMessage});
        this.outMessage = outMessage;
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setOutMessage"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    protected void setSize(int[] size) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setSize"), new StringBuffer("label"), true, new String[]{"size"}, new Object[]{size});
        this.size = size.clone();
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("setSize"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public boolean isProvOn() {
        StopWatch.start(false);
        StopWatch.stop(true);
        return provOn;
    }

    public void setProvOn(boolean on) {
        StopWatch.start(false);
        StopWatch.stop(true);
        provOn = on;
    }

    public int getLinearIndex() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getLinearIndex"), new StringBuffer("label"), true, null, null);
        int linearIndex = getLinearIndexFromArrayIndex(index, size);
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getGlobalLinearIndexFromGlobalArrayIndex"), procRID, new StringBuffer("linearIndex"), linearIndex, null, null, true, false, false);
        StopWatch.stop(true);
        return linearIndex;
    }

    protected int getLinearIndexFromArrayIndex(int index[], int size[]) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getGlobalLinearIndexFromGlobalArrayIndex"), new StringBuffer("label"), true, new String[]{"index","size"}, new Object[]{index, size});

        int retVal = 0;

        for (int i = 0; i < index.length; i++) {

            if (size[i] <= 0) {
                continue;
            }

            if (index[i] >= 0 && index[i] < size[i]) {
                retVal = retVal * size[i];
                retVal += index[i];
            } else {
                ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getGlobalLinearIndexFromGlobalArrayIndex"), procRID, new StringBuffer("Integer.MIN_VALUE"), Integer.MIN_VALUE, null, null, true, false, false);
                StopWatch.stop(false);
                return Integer.MIN_VALUE; // out of space
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(provOn, ProvUtils.getStoreOfCurrentThread(provOn), this, new StringBuffer("getGlobalLinearIndexFromGlobalArrayIndex"), procRID, new StringBuffer("retVal"), retVal, null, null, true, false, false);
        StopWatch.stop(false);
        return retVal;
    }
}
