package org.nakedobjects.runtime.system.internal.monitor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;


public class Monitor {
    private static final Logger LOG = Logger.getLogger(Monitor.class);
    private static List<MonitorListener> listeners = new ArrayList<MonitorListener>();

    public static void addListener(final MonitorListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(final MonitorListener listener) {
        listeners.remove(listener);
    }

    public static void addEvent(final String category, final String message) {
        addEvent(category, message, null);
    }

    public static void addEvent(final String category, final String message, DebugInfo[] debug) {
        final MonitorEvent event = new MonitorEvent(category, message, debug);
        LOG.info(event);
        dispatchEvent(event);
    }

    private static void dispatchEvent(final MonitorEvent event) {
        for(MonitorListener listener: listeners) {
            listener.postEvent(event);
        }
    }
}

// Copyright (c) Naked Objects Group Ltd.
