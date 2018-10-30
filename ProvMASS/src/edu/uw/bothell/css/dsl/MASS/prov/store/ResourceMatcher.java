package edu.uw.bothell.css.dsl.MASS.prov.store;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

/**
 * Provides a space to stack resource IDs between caller/callee methods. IDs
 * include the activity ID for the called method, as well as the return object
 * of the method.
 *
 * @author Delmar B. Davis
 */
public class ResourceMatcher {

    // <editor-fold defaultstate="collapsed" desc="Singleton">
    private static ResourceMatcher instance = null;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Members">
    private final HashMap<Thread, Stack<StringBuffer>> activitiesMap;
    private final HashMap<Thread, Stack<StringBuffer>> entitiesMap;
    private final HashMap<Thread, Stack<StringBuffer>> agentsMap;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">
    public static ResourceMatcher getMatcher() {
        if (instance == null) {
            instance = new ResourceMatcher();
        }
        return instance;
    }

    private ResourceMatcher() {
        activitiesMap = new HashMap<>();
        entitiesMap = new HashMap<>();
        agentsMap = new HashMap<>();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="EmptyOps">
    /**
     * Empties all of the stacks associated with the provided thread. This
     * operation is thread-safe for each ID stack (activities, entities,
     * agents), as it is manipulated.
     *
     * @param thread Any thread that has stacked resource IDs in the
     * ResourceMatcher
     */
    public void emptyAll(Thread thread) {
        Stack<StringBuffer> activityStack = activitiesMap.get(thread);
        Stack<StringBuffer> entitiesStack = entitiesMap.get(thread);
        Stack<StringBuffer> agentsStack = agentsMap.get(thread);
        if (activityStack != null) {
            synchronized (activityStack) {
                activityStack.empty();
            }
        }
        if (entitiesStack != null) {
            synchronized (entitiesStack) {
                entitiesStack.empty();
            }
        }
        if(agentsStack != null)
        synchronized (agentsStack) {
            agentsStack.empty();
        }
    }

    /**
     * Empties all of the stacks of resource IDs (activities, entities, and
     * agents) associated with all of the threads that have pushed IDs
     */
    public void clear() {
        emptyAll(activitiesMap);
        emptyAll(entitiesMap);
        emptyAll(agentsMap);
    }

    private void emptyAll(HashMap<Thread, Stack<StringBuffer>> map) {
        synchronized (map) {
            Thread thread;
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                thread = (Thread) iter.next();
                emptyAll(thread);
            }
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PushOps">
    public void pushActivityID(StringBuffer activityID) {
        pushID(getStack(activitiesMap), activityID);
    }

    public void pushEntityID(StringBuffer entityID) {
        pushID(getStack(entitiesMap), entityID);
    }

    public void pushAgentID(StringBuffer agentID) {
        pushID(getStack(agentsMap), agentID);
    }

    public void pushActivityID(String activityID) {
        pushID(getStack(activitiesMap), new StringBuffer(activityID));
    }

    public void pushEntityID(String entityID) {
        pushID(getStack(entitiesMap), new StringBuffer(entityID));
    }

    public void pushAgentID(String agentID) {
        pushID(getStack(agentsMap), new StringBuffer(agentID));
    }

    public void pushActivityID(Thread thread, StringBuffer activityID) {
        pushID(getStack(thread, activitiesMap), activityID);
    }

    public void pushEntityID(Thread thread, StringBuffer entityID) {
        pushID(getStack(thread, entitiesMap), entityID);
    }

    public void pushAgentID(Thread thread, StringBuffer agentID) {
        pushID(getStack(thread, agentsMap), agentID);
    }

    public void pushActivityID(Thread thread, String activityID) {
        pushID(getStack(thread, activitiesMap), new StringBuffer(activityID));
    }

    public void pushEntityID(Thread thread, String entityID) {
        pushID(getStack(thread, entitiesMap), new StringBuffer(entityID));
    }

    public void pushAgentID(Thread thread, String agentID) {
        pushID(getStack(thread, agentsMap), new StringBuffer(agentID));
    }

    private Stack<StringBuffer> getStack(HashMap<Thread, Stack<StringBuffer>> map) {
        Stack<StringBuffer> idStack = map.get(Thread.currentThread());
        if (idStack == null) {
            idStack = new Stack<>();
            synchronized (map) {
                map.put(Thread.currentThread(), idStack);
            }
        }
        return idStack;
    }

    private Stack<StringBuffer> getStack(Thread thread, HashMap<Thread, Stack<StringBuffer>> map) {
        Stack<StringBuffer> idStack = map.get(thread);
        if (idStack == null) {
            idStack = new Stack<>();
            synchronized (map) {
                map.put(Thread.currentThread(), idStack);
            }
        }
        return idStack;
    }

    private void pushID(Stack<StringBuffer> idStack, StringBuffer id) {
        if (idStack != null) {
            idStack.push(id);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PopOps">
    public synchronized StringBuffer popActivityIDBuffer() {
        return popID(activitiesMap);
    }

    public synchronized StringBuffer popEntityIDBuffer() {
        return popID(entitiesMap);
    }

    public synchronized StringBuffer popAgentIDBuffer() {
        return popID(agentsMap);
    }

    public synchronized String popActivityID() {
        return popID(activitiesMap).toString();
    }

    public synchronized String popEntityID() {
        return popID(entitiesMap).toString();
    }

    public synchronized String popAgentID() {
        return popID(agentsMap).toString();
    }

    public synchronized StringBuffer popActivityIDBuffer(Thread thread) {
        return popID(thread, activitiesMap);
    }

    public synchronized StringBuffer popEntityIDBuffer(Thread thread) {
        return popID(thread, entitiesMap);
    }

    public synchronized StringBuffer popAgentIDBuffer(Thread thread) {
        return popID(thread, agentsMap);
    }

    public synchronized String popActivityID(Thread thread) {
        return popID(thread, activitiesMap).toString();
    }

    public synchronized String popEntityID(Thread thread) {
        return popID(thread, entitiesMap).toString();
    }

    public synchronized String popAgentID(Thread thread) {
        return popID(thread, agentsMap).toString();
    }

    private synchronized StringBuffer popID(HashMap<Thread, Stack<StringBuffer>> map) {
        Stack<StringBuffer> idStack = map.get(Thread.currentThread());
        return idStack != null && !idStack.isEmpty() ? idStack.pop() : null;
    }

    private synchronized StringBuffer popID(Thread thread, HashMap<Thread, Stack<StringBuffer>> map) {
        Stack<StringBuffer> idStack = map.get(Thread.currentThread());
        return idStack != null && !idStack.isEmpty() ? idStack.pop() : null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PeekOps">
    public synchronized StringBuffer peekActivityIDBuffer() {
        return peekID(activitiesMap);
    }

    public synchronized StringBuffer peekEntityIDBuffer() {
        return peekID(entitiesMap);
    }

    public synchronized StringBuffer peekAgentIDBuffer() {
        return peekID(agentsMap);
    }

    public synchronized String peekActivityID() {
        return peekID(activitiesMap).toString();
    }

    public synchronized String peekEntityID() {
        return peekID(entitiesMap).toString();
    }

    public synchronized String peekAgentID() {
        return peekID(agentsMap).toString();
    }

    public synchronized StringBuffer peekActivityIDBuffer(Thread thread) {
        return peekID(thread, activitiesMap);
    }

    public synchronized StringBuffer peekEntityIDBuffer(Thread thread) {
        return peekID(thread, entitiesMap);
    }

    public synchronized StringBuffer peekAgentIDBuffer(Thread thread) {
        return peekID(thread, agentsMap);
    }

    public synchronized String peekActivityID(Thread thread) {
        return peekID(thread, activitiesMap).toString();
    }

    public synchronized String peekEntityID(Thread thread) {
        return peekID(thread, entitiesMap).toString();
    }

    public synchronized String peekAgentID(Thread thread) {
        return peekID(thread, agentsMap).toString();
    }

    private synchronized StringBuffer peekID(HashMap<Thread, Stack<StringBuffer>> map) {
        Stack<StringBuffer> idStack = map.get(Thread.currentThread());
        return idStack != null && !idStack.isEmpty() ? idStack.peek() : null;
    }

    private synchronized StringBuffer peekID(Thread thread, HashMap<Thread, Stack<StringBuffer>> map) {
        Stack<StringBuffer> idStack = map.get(thread);
        return idStack != null && !idStack.isEmpty() ? idStack.peek() : null;
    }
    // </editor-fold>
}
