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

import edu.uw.bothell.css.dsl.MASS.prov.core.logging.Log4J2Logger;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

public class AgentList {

    private final int CAPACITY_X = 1000; // max agent population = 1 million
    private final int CAPACITY_Y = 1000;

    private int capacityY = 0;
    private Agent[][] array = null;
    private boolean reduceDone = true;

    private int currentX = -1;
    private int nextY = 0;
    private int iterator = 0;
    private int estimateSize = 0;

    // logging
    private Log4J2Logger logger = Log4J2Logger.getInstance();

    public AgentList() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("AgentList"), new StringBuffer("label"), true, null, null);
        init(CAPACITY_Y);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("AgentList"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public AgentList(int init_capacity) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("AgentList"), new StringBuffer("label"), true, new String[]{"init_capacity"}, new Object[]{init_capacity});
        init(init_capacity);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("AgentList"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public synchronized void add(Agent item) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("add"), new StringBuffer("label"), true, new String[]{"item"}, new Object[]{item});
        if (nextY == capacityY) {
            increaseX();
            nextY = 0;
        }
        array[currentX][nextY++] = item;
        ++estimateSize;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("add"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     *
     * @param item
     * @param index
     */
    public void add(Agent item, int index) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("add"), new StringBuffer("label"), true, new String[]{"item", "index"}, new Object[]{item, index});
        int xindex = index / CAPACITY_X;
        int yindex = index % CAPACITY_X;
        if (array[xindex] == null) {
            array[xindex] = new Agent[CAPACITY_Y];
        }
        array[xindex][yindex] = item;
        ++estimateSize;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("add"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public int estimateSize() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("estimateSize"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("estimateSize"), procRID, new StringBuffer("estimateSize"), estimateSize, null, null, true, false, false);
        StopWatch.stop(false);
        return estimateSize;
    }

    public void checkInternal() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("checkInternal"), new StringBuffer("label"), true, null, null);
        for (int x = 0; x < currentX * capacityY + nextY; x++) {
            logger.debug("AgentList[{}]", get(x));
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("checkInternal"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public void clear() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("clear"), new StringBuffer("label"), true, null, null);

        for (int i = 0; i < size_unreduced(); i++) {
            remove(i);
        }
        reduceHelper();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("clear"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public synchronized Agent get(int linear_index) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("get"), new StringBuffer("label"), true, new String[]{"linear_index"}, new Object[]{linear_index});
        if (linear_index <= size_unreduced()) {
            int x = linear_index / capacityY;
            int y = linear_index % capacityY;
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("get"), procRID, new StringBuffer("array[x][y]"), array[x][y], null, null, true, false, false);
            return array[x][y];
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("get"), procRID, new StringBuffer("null"), null, null, null, true, false, false);
        StopWatch.stop(false);
        return null;
    }

    public synchronized boolean hasNext() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("hasNext"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("hasNext"), procRID, new StringBuffer("(iterator_<_size_unreduced())"), (iterator < size_unreduced()), null, null, true, false, false);
        StopWatch.stop(false);
        return (iterator < size_unreduced());
    }

    private void increaseX() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("increaseX"), new StringBuffer("label"), true, null, null);

        currentX++;
        array[currentX] = new Agent[capacityY];

        for (int i = 0; i < capacityY; i++) {
            array[currentX][i] = null;
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("increaseX"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public synchronized int indexOf(Agent item) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("indexOf"), new StringBuffer("label"), true, new String[]{"item"}, new Object[]{item});

        for (int i = 0; i < array.length && array[i] != null; i++) {

            int max_j = (array[i + 1] == null) ? nextY : capacityY;

            for (int j = 0; j < max_j; j++) {

                if (array[i][j] == item) {
                    ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("indexOf"), procRID, new StringBuffer("i*capacityY+j"), (i * capacityY + j), null, null, true, false, false);
                    return i * capacityY + j;
                }

            }

        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("indexOf"), procRID, new StringBuffer("-1"), -1, null, null, true, false, false);
        StopWatch.stop(false);
        return -1;

    }

    private synchronized void init(int init_capacity) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("init"), new StringBuffer("label"), true, new String[]{"init_capacity"}, new Object[]{init_capacity});
        // create array[capacity_x][]
        array = new Agent[CAPACITY_X][];

        for (int i = 1; i < array.length; i++) {
            array[i] = null;
        }

        // create only array[0][capacity_y]
        capacityY = (init_capacity > CAPACITY_Y)
                ? init_capacity : CAPACITY_Y;

        increaseX();
        estimateSize = 0;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("init"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public synchronized Agent next() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("next"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("next"), procRID, new StringBuffer("get(iterator++)"), get(iterator++), null, null, true, false, false);
        StopWatch.stop(false);
        return get(iterator++);
    }

    public synchronized void reduce() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("reduce"), new StringBuffer("label"), true, null, null);
        reduceHelper();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("reduce"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    private void reduceHelper() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("reduceHelper"), new StringBuffer("label"), true, null, null);

        if (reduceDone) {
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("reduceHelper"), procRID, null, null, null, null, true, false, false);
            return;
        }

        int max = size_unreduced();
        int currentNull = 0;
        int cur_full = max - 1;
        int xNull, yNull, x_full, y_full;

        while (true) {

            for (; currentNull < max && get(currentNull) != null; currentNull++);
            for (; cur_full >= 0 && get(cur_full) == null; cur_full--);
            if (currentNull >= cur_full) {
                break;
            }

            // swapping
            xNull = currentNull / capacityY;
            yNull = currentNull % capacityY;
            x_full = cur_full / capacityY;
            y_full = cur_full % capacityY;
            array[xNull][yNull] = array[x_full][y_full];
            array[x_full][y_full] = null;

            /*
	    	System.out.println( "swaped[" + x_null + "][" + y_null + 
				"] and [" + x_full + "][" + y_full + "] = " +
				array[x_null][y_null] );
             */
            //MASS.log("AgentList reduced done");
        }

        // reduce
        xNull = currentNull / capacityY;
        yNull = currentNull % capacityY;

        for (int i = xNull + 1; i < array.length && array[i] != null; i++) {
            array[i] = null;
        }

        currentX = xNull;
        nextY = yNull;
        reduceDone = true;
        estimateSize = this.size_unreduced();
        logger.debug("Reduce done to {}", size_unreduced());
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("reduceHelper"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public synchronized void remove(Agent item) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("remove"), new StringBuffer("label"), true, new String[]{"item"}, new Object[]{item});

        for (int i = 0; i < array.length && array[i] != null; i++) {

            int max_j = (array[i + 1] == null) ? nextY : capacityY;

            for (int j = 0; j < max_j; j++) {

                if (array[i][j] == item) {

                    array[i][j] = null;
                    reduceDone = false;
                    --estimateSize;
                    ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("remove"), procRID, null, null, null, null, true, false, false);
                    return;

                }

            }

        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("remove"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
        return;

    }

    public synchronized void remove(int linear_index) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("remove"), new StringBuffer("label"), true, new String[]{"linear_index"}, new Object[]{linear_index});

        if (linear_index <= size_unreduced()) {

            int x = linear_index / capacityY;
            int y = linear_index % capacityY;
            array[x][y] = null;
            reduceDone = false;
            --estimateSize;
            /*
	    	System.out.println( "AgentList.remove: " +
				"linear_index = " + linear_index +
				" array[" + x + "][" + y + "] = " +
				array[x][y] );
             */
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("remove"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public synchronized void setIterator() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setIterator"), new StringBuffer("label"), true, null, null);
        reduceHelper();
        iterator = 0;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setIterator"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public synchronized int size() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("size"), new StringBuffer("label"), true, null, null);
        reduceHelper();
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("size"), procRID, new StringBuffer("currentX_*_capacityY_+_nextY"), currentX * capacityY + nextY, null, null, true, false, false);
        StopWatch.stop(false);
        return currentX * capacityY + nextY;
    }

    public int size_unreduced() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("size_unreduced"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("size_unreduced"), procRID, new StringBuffer("currentX_*_capacityY_+_nextY"), currentX * capacityY + nextY, null, null, true, false, false);
        StopWatch.stop(false);
        return currentX * capacityY + nextY;
    }

    /*public synchronized LinkedList<Agent> getAll() {
	  reduce_helper();
	  LinkedList<Agent> result = new LinkedList<Agent>();
	  int x = 0, y = 0;
	  for(int i = 0; i < size_unreduced(); i++)
	  {
	    result.add(array[x][y]);
	    ++y;
	    if(y == capacity_y)
	    {
	      y = 0;
	      ++x;
	    }
	  }
	  return result;
	} */
}
