package org.nakedobjects.runtime.system.internal.monitor;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;


public class MonitorEvent {
    private static int nextSerialId = 1;
    private final int serialId = nextSerialId++;
    private final String message;
    private final String category;
    private DebugString debug;

    public MonitorEvent(final String category, final String message, DebugInfo[] debugDetails) {
        this.message = message;
        this.category = category;
        debug = new DebugString();
        try {
            if (debugDetails != null) {
                for (DebugInfo info : debugDetails) {
                    debug.appendTitle(info.debugTitle());
                    debug.indent();
                    info.debugData(debug);
                    debug.unindent();
                }
            }
        } catch (RuntimeException e) {
            debug.appendException(e);
        }
    }

    public String getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }

    public int getSerialId() {
        return serialId;
    }

    public String getDebug() {
        return debug.toString();
    }

    @Override
    public String toString() {
        return category + ": " + message;
    }
}

// Copyright (c) Naked Objects Group Ltd.
