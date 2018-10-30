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

public class MThread extends Thread {

    /**
     * Status Type A list of possible statuses
     */
    public enum STATUS_TYPE {
        STATUS_READY, // 0
        STATUS_TERMINATE, // 1
        STATUS_CALLALL, // 2
        STATUS_EXCHANGEALL, // 3
        STATUS_AGENTSCALLALL, // 4
        STATUS_MANAGEALL, // 5
        STATUS_AGENTSCALLALL_ASYNC // 6
    }

    private static Object lock;
    private static int barrierCount;
    private static STATUS_TYPE status;
    private static int threadCreated;
    private static int agentBagSize;
    private static int barrierPhases;
    private int tid;                  // this mthread's id

    // logging
    private static Log4J2Logger logger = Log4J2Logger.getInstance();

    public MThread(int id) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("MThread"), new StringBuffer("label"), true, new String[]{"id"}, new Object[]{id});
        this.tid = id;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("MThread"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void barrierThreads(int tid) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("barrierThreads"), new StringBuffer("label"), true, new String[]{"tid"}, new Object[]{tid});

        synchronized (lock) {

            if (++barrierCount < MASSBase.getThreads().length) {

                logger.debug("tid[" + tid + "] waiting: barrier = " + barrierPhases);

                try {
                    lock.wait();
                } catch (Exception e) {
                    logger.error("Unknown exception thrown in barrierThreads", e);
                }

            } else {

                barrierCount = 0;
                status = STATUS_TYPE.STATUS_READY;
                logger.debug("tid[" + tid + "] woke up all: barrier = " + barrierPhases);
                barrierPhases++;
                lock.notifyAll();

            }

        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("barrierThreads"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void init() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("init"), new StringBuffer("label"), true, null, null);
        lock = new Object();
        status = STATUS_TYPE.STATUS_READY;
        barrierCount = 0;
        barrierPhases = 0;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("init"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static void resumeThreads(STATUS_TYPE new_status) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("resumeThreads"), new StringBuffer("label"), true, new String[]{"new_status"}, new Object[]{new_status});
        synchronized (lock) {
            status = new_status;
            lock.notifyAll();
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("resumeThreads"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public void run() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("run"), new StringBuffer("label"), true, null, null);
        try {
            // Initialization portion
            synchronized (lock) {
                threadCreated = tid;  // to inform MASS_base of my invocation
            }

            // breath message
            logger.debug("Mthread[{}] invoked", tid);

            // the following variables are used to call callAll( )
            PlacesBase places = null;
            PlacesBase destinationPlaces = null;
            AgentsBase agents = null;

            int functionId = 0;
            Object argument = null;
            Message.ACTION_TYPE msgType = Message.ACTION_TYPE.EMPTY;
            //Vector<int[]> destinations = null;

            // END Initialization
            boolean running = true;
            while (running) {

                // wait for a new command
                synchronized (lock) {

                    if (status == STATUS_TYPE.STATUS_READY) {
                        lock.wait();
                    }

                    // wake-up message
                    logger.debug("Mthread[" + tid + "] woken up " + status);

                }
                if (status == MThread.STATUS_TYPE.STATUS_AGENTSCALLALL_ASYNC) {
                    agents = MASSBase.getCurrentAgentsBase();
                    agents.callAllAsync(tid);
                } else {
                    // perform each task
                    switch (status) {

                        case STATUS_READY:

                            logger.error("Mthread reached STATUS_READY in switch");
                            System.exit(-1);
                            break;

                        case STATUS_TERMINATE:

                            running = false;
                            break;

                        case STATUS_CALLALL:

                            places = MASSBase.getCurrentPlacesBase();
                            functionId = MASSBase.getCurrentFunctionId();
                            argument = MASSBase.getCurrentArgument();
                            msgType = MASSBase.getCurrentMsgType();

                            logger.debug("Mthread[" + tid + "] works on CALLALL:"
                                    + " placese = " + places
                                    + " functionId = " + functionId
                                    + " argument = " + argument
                                    + " msgType = " + msgType);

                            if (msgType == Message.ACTION_TYPE.PLACES_CALL_ALL_VOID_OBJECT) {
                                places.callAll(functionId, argument, tid);
                            } else {
                                places.callAll(functionId, (Object[]) argument,
                                        ((Object[]) argument).length, tid);
                            }
                            break;

                        case STATUS_EXCHANGEALL:

                            logger.debug("Mthread[{}] works on EXCHANGEALL", tid);

                            places = MASSBase.getCurrentPlacesBase();
                            functionId = MASSBase.getCurrentFunctionId();
                            destinationPlaces = MASSBase.getDestinationPlaces();
                            //destinations = MASS_base.getCurrentDestinations( );

                            //		places.exchangeAll( destinationPlaces, functionId, 
                            //			    destinations, tid );
                            places.exchangeAll(destinationPlaces, functionId, tid);
                            break;

                        case STATUS_AGENTSCALLALL:

                            agents = MASSBase.getCurrentAgentsBase();
                            functionId = MASSBase.getCurrentFunctionId();
                            argument = MASSBase.getCurrentArgument();
                            msgType = MASSBase.getCurrentMsgType();

                            logger.debug("Mthread[" + tid
                                    + "] works on AGENST_CALLALL:"
                                    + " agents = " + agents
                                    + " functionId = " + functionId
                                    + " argument = " + argument
                                    + " msgType = " + msgType);

                            if (msgType == Message.ACTION_TYPE.AGENTS_CALL_ALL_VOID_OBJECT) {

                                //System.err.println( "Mthread[" + tid + 
                                //			"] call all agent void object" );
                                agents.callAll(functionId, argument, tid);

                            } else {

                                //System.err.println( "Mthread[" + tid + 
                                //			"] call all agents return object" );
                                agents.callAll(functionId, (Object[]) argument, tid);

                            }
                            break;

                        case STATUS_MANAGEALL:

                            //Get agents to be called with Manageall
                            agents = MASSBase.getCurrentAgentsBase();

                            //Send logging message
                            logger.debug("Mthread[" + tid + "] works on MANAGEALL: agents = " + agents);

                            //Sent message for manageall
                            agents.manageAll(tid);

                            break;
                    }
                    // barrier
                    barrierThreads(tid);
                }
            }
        } catch (Exception e) {
            logger.error("Thread {} fails", tid, e);
        }

        // last message
        logger.debug("Mthread[{}] terminated", tid);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("run"), procRID, null, null, null, null, true, false, false);
        ProvUtils.releaseThreadStore();
        StopWatch.stop(false);
    }

    public static int getAgentBagSize() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getAgentBagSize"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getAgentBagSize"), procRID, new StringBuffer("agentBagSize"), agentBagSize, null, null, true, false, false);
        StopWatch.stop(false);
        return agentBagSize;
    }

    public static void setAgentBagSize(int agentBagSize) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setAgentBagSize"), new StringBuffer("label"), true, new String[]{"agentBagSize"}, new Object[]{agentBagSize});
        MThread.agentBagSize = agentBagSize;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setAgentBagSize"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static Object getLock() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getLock"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getLock"), procRID, new StringBuffer("lock"), lock, null, null, true, false, false);
        StopWatch.stop(false);
        return lock;
    }

    public static void setLock(Object lock) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setLock"), new StringBuffer("label"), true, new String[]{"lock"}, new Object[]{lock});
        MThread.lock = lock;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setLock"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public static int getThreadCreated() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getThreadCreated"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("getThreadCreated"), procRID, new StringBuffer("threadCreated"), threadCreated, null, null, true, false, false);
        StopWatch.stop(false);
        return threadCreated;
    }

    public static void setThreadCreated(int threadCreated) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setThreadCreated"), new StringBuffer("label"), true, new String[]{"threadCreated"}, new Object[]{threadCreated});
        MThread.threadCreated = threadCreated;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), null, new StringBuffer("setThreadCreated"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

}
