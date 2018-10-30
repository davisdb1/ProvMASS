package edu.uw.bothell.css.dsl.MASS.prov.utils;

import edu.uw.bothell.css.dsl.MASS.prov.IO.IO;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * Tracks elapsed time of overhead introduced by provenance capture
 *
 * @author Delmar B. Davis
 */
public class StopWatch {

    private static final Map<Thread, Timer> overheadOpsTimers = new HashMap<>();
    private static final Map<Thread, Timer> normalOpsTimers = new HashMap<>();
    private static final Map<String, Long[]> durationsFromRemoteHosts = new HashMap<>();
    public static final int NORMAL_OPS_IDX = 0;
    public static final int OVERHEAD_OPS_IDX = 1;
    public static final int REMOTE_HOST_DURATIONS_SIZE = 2;

    private static final long MAX_THREAD_KEYS_PER_TIMER_MAP = 1000;
    private static long initialTimeMs;
    private static long initialTimeNs;
    private static long initialTimeMsAsNanos;
    private static long offsetNanos;
    private static boolean initialized = false;
    private static final boolean TRACK = false;

    public final static void init() {
        if (TRACK) {
            if (!initialized) {
                initialTimeNs = System.nanoTime();
                initialTimeMs = System.currentTimeMillis();
                initialTimeMsAsNanos = TimeUnit.MILLISECONDS.convert(initialTimeMs,
                        TimeUnit.NANOSECONDS);
                offsetNanos = initialTimeNs - initialTimeMsAsNanos;
                initialized = true;
                StopWatch.start(false);
            }
        }
    }

    ; // ensure that initial time values are recorded before anything else
    /**
     * Puts a the execution durations supplied by a remote host during
     * finalization into the durationsFromRemoteHosts map for later use
     *
     * @param hostName - Name of the host where the durations originated
     * @param durations - An array of long values consisting of (1) the total
     * duration of all normal operations and (2) the total duration of all
     * overhead operations
     */
    public static void mapDurationsReceivedFromRemoteHost(String hostName, Long[] durations) {
        if (TRACK) {
            init();
            synchronized (durationsFromRemoteHosts) {
                durationsFromRemoteHosts.put(hostName, durations);
            }
        }
    }

    private static void logDurationsFromRemoteHosts(boolean printToErrorStream) {
        if (TRACK) {
            Iterator iter = durationsFromRemoteHosts.entrySet().iterator();
            String hostname;
            Long ops[];
            Long normalOps, overheadOps;
            while (iter.hasNext()) {
                normalOps = -1L;
                overheadOps = -1L;
                Entry<String, Long[]> entry = (Entry<String, Long[]>) iter.next();
                hostname = entry.getKey();
                ops = entry.getValue();
                if (ops.length >= 2) {
                    normalOps = ops[0];
                    overheadOps = ops[1];
                }
                String msg = "Host: " + hostname
                        + " reports duration of normal operations as "
                        + normalOps + "ns and duration of overhead operations as "
                        + overheadOps + "ns";
                IO.log(msg);
                if (printToErrorStream) {
                    System.err.println(msg);
                }
            }
        }
    }

    /**
     * Outputs the total duration of execution in milliseconds (simple duration
     * from StopWatch initialization until now), the fraction of time spent on
     * overhead adding operations, and the total amount of time spent on
     * overhead adding operations. The reported values take into account
     * execution durations of all hosts.
     */
    public static void reportPerformanceOverheadEvaluation() {
        if (TRACK) {
            if (TRACK) {
                /* finish recording the duration of the most recent operations */
                init();
                StopWatch.start(true);
                StopWatch.stop(true);
                StopWatch.start(false);
                logDurationsFromRemoteHosts(true);
                /* total all execution durations across all hosts */
                long totalDurationOfNormalOps = getTotalDurationOfNormalOpsOnAllHosts();
                System.err.println("\ntotalDurationOfNormalOps: " + totalDurationOfNormalOps + "ns");
                long totalDurationOfoverheadOps = getTotalDurationOfOverheadOpsOnAllHosts();
                System.err.println("totalDurationOfoverheadOps(all hosts): " + totalDurationOfoverheadOps + "ns");
                long sequentialDurationOfAllOps = totalDurationOfNormalOps + totalDurationOfoverheadOps;
                System.err.println("sequentialDurationOfAllOps (on all threads on all hosts): " + sequentialDurationOfAllOps + "ns");

                double percentExecOnProv = (double) ((float) totalDurationOfoverheadOps / sequentialDurationOfAllOps); //percentOverheadNumerator.divide(percentOverheadDenominator);
                System.err.format("\nPercent of total execution spent collecting provenance: %.2f%%", (double) (percentExecOnProv * 100.0));
                double percentExecOnNormalOps = (double) ((float) totalDurationOfNormalOps / sequentialDurationOfAllOps); //percentOverheadNumerator.divide(percentOverheadDenominator);
                System.err.format("\nPercent of total execution spent on normal operations: %.2f%%", (double) (percentExecOnNormalOps * 100.0));
                double percentOverhead = (double) ((float) totalDurationOfoverheadOps / totalDurationOfNormalOps); //percentOverheadNumerator.divide(percentOverheadDenominator);
                System.err.format("\nOVERHEAD: %.2f%%%n", (double) (percentOverhead * 100.0));

                /* get the simple program duration end - start */
                long totalMillis = System.currentTimeMillis() - StopWatch.getInitialTimeMs();
                System.err.println("Total time in milliseconds (Parallel): " + totalMillis + "\n");
            }
        }
    }

    private static long getTotalDurationOfNormalOpsOnAllHosts() {
        if (!TRACK) {
            return -1;
        }
        init();
        return getTotalDurationOfOpsOnAllHosts(NORMAL_OPS_IDX);
    }

    private static long getTotalDurationOfOverheadOpsOnAllHosts() {
        if (!TRACK) {
            return -1;
        }
        init();
        return getTotalDurationOfOpsOnAllHosts(OVERHEAD_OPS_IDX);
    }

    private static long getTotalDurationOfOpsOnAllHosts(int opsTypeIdx) {
        if (!TRACK) {
            return -1;
        }
        // start with local normal ops total
        init();
        Long total = 0L;
        try {
            if (opsTypeIdx == NORMAL_OPS_IDX) {
                total += StopWatch.getTotalDurationOfLocalNormalOps();
            } else if (opsTypeIdx == OVERHEAD_OPS_IDX) {
                total += StopWatch.getTotalDurationOfLocalOverheadOps();
            }
            // for all remote hosts
            for (Long[] durations : durationsFromRemoteHosts.values()) {
                // check expected length (external modification trap)
                if (durations.length == REMOTE_HOST_DURATIONS_SIZE && opsTypeIdx < durations.length) {
                    total += durations[opsTypeIdx];
                } else {
                    throw new IndexOutOfBoundsException("unexpected length of "
                            + "execution durations from remote hosts found!");
                }
            }
        } catch (Exception e) {
            IO.log(e.getMessage());
            e.printStackTrace(IO.getLogWriter());
        }
        return total;
    }

    public static Long[] packageDurationTotals() {
        if (TRACK) {
            return null;
        }
        init();
        Long[] totals = new Long[REMOTE_HOST_DURATIONS_SIZE];
        if (totals.length > NORMAL_OPS_IDX) {
            totals[NORMAL_OPS_IDX]
                    = StopWatch.getTotalDurationOfLocalNormalOps();
        }
        if (totals.length > OVERHEAD_OPS_IDX) {
            totals[OVERHEAD_OPS_IDX] = StopWatch.getTotalDurationOfLocalOverheadOps();
        }
        return totals;
    }

    /**
     * Attempts to start the Timer associated with the currently executing
     * Thread object. If the last action on the timer was a start operation then
     * this action is ignored. If this operation occurs after a stop operation
     * the time between the last stop operation and its corresponding start
     * operation is accumulated into the total elapsed time for the associated
     * timer.
     *
     * @param overheadOps - Indicates which operation is being timed
     */
    public static void start(boolean overheadOps) {
        if (TRACK) {
            init();
            Timer timer = getTimer(overheadOps);
            Timer otherTimer = getTimer(!overheadOps);
            if (timer != null) {
                if (otherTimer != null) {
                    timer.start();
                    if (otherTimer.started) {
                        otherTimer.stop();
                        otherTimer.started = false;
                        otherTimer.forceAccumulate();
                    }
                }
            }
        }
    }

    /**
     * Attempts to stop the Timer associated with the currently executing Thread
     * object. If the last action on the timer was a stop operation, this action
     * overwrites the previous stop operation.
     *
     * @param overheadOps - Indicates which operation is being timed
     */
    public static void stop(boolean overheadOps) {
        if (TRACK) {
            init();
            Timer timer = getTimer(overheadOps);
            Timer otherTimer = getTimer(!overheadOps);
            if (timer != null && timer.started) {
                timer.stop = System.nanoTime();
            }
            if (otherTimer.started) {
                otherTimer.forceAccumulate();
                otherTimer.started = false;
                timer.start = otherTimer.stop;
                timer.started = true;
            }
        }
    }

    /**
     * Forces the respective timer to add the time between stop and start and
     * add that time into total. The start and stop flags are reset to false.
     * Use this method to deal with events which should not be timed, to
     * alternate between overheadOps recording and nonOverheadOps recording, or
     * to finalize accumulation for reporting.
     *
     * Timers accept multiple starts and stops, never accumulating until the
     * start operation is encountered while in a stop state. This way there is
     * no need to track timing operations between called and calling procedures.
     * However, this entails that accumulation of the recorded execution time is
     * always successive. Meanwhile, overheadOps are measured together and when
     * they are finished the timer should be reset for the next set of overhead
     * operations. The operations in between the current overhead operations and
     * the next overhead operation are non-overhead operations and should not be
     * recorded when the next start operation comes in. Therefore, it is
     * important to force accumulation when switching between overhead-ops and
     * non-overhead-ops and vice-versa. Also it may be necessary to call this
     * method in order to ignore certain operations completely.
     *
     * @param overheadOps Whether or not operations that add overhead to
     * standard execution are currently being executed
     */
    private static void forceAccumulate(boolean overheadOps) {
        if (TRACK) {
            init();
            Timer timer = getTimer(overheadOps);
            if (timer != null) {
                timer.forceAccumulate();
            }
        }
    }

    private static Timer getTimer(boolean overheadOps) {
        if (!TRACK) {
            return null;
        }
        /* work with the right set of timers */
        Map<Thread, Timer> timers = overheadOps ? overheadOpsTimers : normalOpsTimers;
        /* get the timer for this thread */
        // timer associated with the currently executing Thread object
        Timer timer = null;
        Thread thread = Thread.currentThread();
        if (thread != null) {
            //if(timers.containsKey(timer))
            // get the timer associated with the currently executing Thread object
            timer = timers.get(thread);
            // if the timer for the currently executing Thread object has not been mapped
            if (timer == null) {
                if (overheadOps) {
                    synchronized (overheadOpsTimers) {
                        if (timers.size()
                                < MAX_THREAD_KEYS_PER_TIMER_MAP) {
                            timer = new Timer();
                            timers.put(thread, timer);
                        }
                    }
                } else {
                    synchronized (normalOpsTimers) {
                        if (timers.size()
                                < MAX_THREAD_KEYS_PER_TIMER_MAP) {
                            timer = new Timer();
                            timers.put(thread, timer);
                        }
                    }
                }
            }
        }
        return timer;
    }

    public static long getInitialTimeMs() {
        if (!TRACK) {
            return -1;
        }
        init();
        return initialTimeMs;
    }

    public static long getInitialTimeNs() {
        if (!TRACK) {
            return -1;
        }
        init();
        return initialTimeNs;
    }

    public static long getInitialTimeMsAsNanos() {
        if (!TRACK) {
            return -1;
        }
        init();
        return initialTimeMsAsNanos;
    }

    public static long getOffsetNanos() {
        if (!TRACK) {
            return -1;
        }
        init();
        return offsetNanos;
    }

    public static long getMillisFromSystemNanosSnapshot(long nanosSnapshot) {
        if (!TRACK) {
            return -1;
        }
        init();
        return TimeUnit.MILLISECONDS.convert(nanosSnapshot - offsetNanos,
                TimeUnit.NANOSECONDS);
    }

    public static Date getDateFromNanosSnapshot(long nanosSnapshot) {
        if (!TRACK) {
            return null;
        }
        init();
        return new Date(getMillisFromSystemNanosSnapshot(nanosSnapshot));
    }

    public static long getTotalDurationOfAllOps(boolean overheadOps) {
        if (!TRACK) {
            return -1;
        }
        init();
        return getTotalDurationOfLocalOverheadOps() + getTotalDurationOfLocalNormalOps();
    }

    public static long getTotalDurationOfLocalOverheadOps() {
        if (!TRACK) {
            return -1;
        }
        init();
        long total = 0L;
        synchronized (overheadOpsTimers) {
            for (Timer timer : overheadOpsTimers.values()) {
                total += timer.getTotal();
            }
        }
        return total;
    }

    public static long getTotalDurationOfLocalNormalOps() {
        if (!TRACK) {
            return -1;
        }
        init();
        long total = 0L;
        synchronized (normalOpsTimers) {
            for (Timer timer : normalOpsTimers.values()) {
                total += timer.getTotal();
            }
        }
        return total;
    }

    private static class Timer {

        private final long initialTimeMs;
        long initialTimeNs;
        long offsetNanos;
        long start;
        boolean started;
        long stop;
        boolean stopped;
        private long total;

        public Timer() {
            initialTimeMs = System.nanoTime();
            initialTimeNs = System.currentTimeMillis();
            Long initialTimeMsAsNanos
                    = TimeUnit.MILLISECONDS.convert(initialTimeMs,
                            TimeUnit.NANOSECONDS);
            offsetNanos = initialTimeNs - initialTimeMsAsNanos;
            total = 0L;
        }

        private void start() {
            if (!started) {
                start = System.nanoTime();
                started = true;
            }
        }

        private void stop() {
            if (started) {
                stop = System.nanoTime();
            }
        }

        private void accumulate() {
            if (stopped) {
                total += stop - start;
                started = false;
            }
        }

        private void forceAccumulate() {
            total += stop - start;
        }

        public long getInitialTimeMs() {
            return initialTimeMs;
        }

        public long getTotal() {
            return total;
        }
    }
}
