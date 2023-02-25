package org.nakedobjects.plugins.dnd.viewer.debug;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.util.Dump;


public class DebugObjectGraph implements DebugInfo {
    private final NakedObject object;

    public DebugObjectGraph(final NakedObject object) {
        this.object = object;
    }

    public void debugData(final DebugString debug) {
        dumpGraph(object, debug);
    }

    public String debugTitle() {
        return "Object Graph";
    }

    private void dumpGraph(final NakedObject object, final DebugString info) {
        if (object != null) {
            Dump.graph(object, info, NakedObjectsContext.getAuthenticationSession());
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
