package org.nakedobjects.remoting.transport.socket.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


// REVIEW should this class be replaced with something from concurrent.util ? 
public class WorkerPool {
    private static final Logger LOG = Logger.getLogger(WorkerPool.class);
    
    private final ThreadGroup group;
    
    public Vector available = new Vector();
    public Vector all = new Vector();

    public WorkerPool(final int noThreads) {
        addWorkers(noThreads);
        group = new ThreadGroup("worker");
    }

    public void shutdown() {
        gracefulShutdown();
    }

    private void interruptGroup() {
        final Class cls = group.getClass();
        try {
            final Method interrupt = cls.getMethod("interrupt", (Class[]) null);
            interrupt.invoke(group, (Object[]) null);
        } catch (final NoSuchMethodException ignore) {
            // expected if pre java 1.2 - fall through
        } catch (final IllegalAccessException e) {
            throw new NakedObjectException(e.getMessage());
        } catch (final InvocationTargetException e) {
            throw new NakedObjectException(e.getMessage());
        }
        group.stop();
    }

    public synchronized void gracefulShutdown() {
        LOG.info("worker pool graceful shutdown");
        for (final Iterator iter = all.iterator(); iter.hasNext();) {
            final Worker worker = (Worker) iter.next();
            worker.gracefulStop();
        }
        interruptGroup();
    }

    public synchronized void addWorkers(final int noThreads) {
        for (int i = 0; i < noThreads; i++) {
            final Worker worker = new Worker(this);
            available.add(worker);
            all.add(worker);
            final Thread t = new Thread(group, worker);
            t.start();
        }
        notify();
    }

    public synchronized Worker getWorker() {
        while (available.size() == 0) {
            try {
                wait();
            } catch (final InterruptedException e) {}
        }
        final Worker worker = (Worker) available.elementAt(0);
        available.removeElementAt(0);
        LOG.debug("worker thread provided " + worker);
        return worker;
    }

    public synchronized void returnWorker(final Worker worker) {
        if (available.contains(worker)) {
            throw new NakedObjectException("Worker thread has already been returned to queue");
        }
        LOG.debug("worker thread returned " + worker);
        available.addElement(worker);
        notify();
    }

    public void debug(final DebugString debug) {
        debug.appendln("available", available);
        debug.appendln();
        debug.appendln();

        for (int i = 0; i < available.size(); i++) {
            final Worker worker = (Worker) available.elementAt(i);
            debug.appendln(i + 1 + ". " + worker.toString());
            debug.indent();
            worker.debug(debug);
            debug.unindent();
            debug.appendln();
        }

    }

    @Override
    public String toString() {
        return "WorkerGroup[groupName=" + group.getName() + "]";
    }
}

// Copyright (c) Naked Objects Group Ltd.
