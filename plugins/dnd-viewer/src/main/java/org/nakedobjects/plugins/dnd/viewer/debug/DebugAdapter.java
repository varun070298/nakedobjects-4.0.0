package org.nakedobjects.plugins.dnd.viewer.debug;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.runtime.util.Dump;


public class DebugAdapter implements DebugInfo {
    private final NakedObject object;

    public DebugAdapter(final NakedObject object) {
        this.object = object;
    }

    public void debugData(final DebugString debug) {
        dumpObject(object, debug);
    }

    public String debugTitle() {
        return "Adapter";
    }

    private void dumpObject(final NakedObject object, final DebugString info) {
        if (object != null) {
            Dump.adapter(object, info);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
