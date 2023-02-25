package org.nakedobjects.metamodel.commons.debug;

import java.util.ArrayList;
import java.util.List;


public class DebugList {
    private final List<DebugInfo> l = new ArrayList<DebugInfo>();
    private final DebugString summary = new DebugString();

    public DebugList(final String name) {
        l.add(new DebugInfo() {
            public void debugData(DebugString debug) {
                debug.append(summary.toString());
            }

            public String debugTitle() {
                return name;
            }
        });
    }

    public void add(String name, Object object) {
        boolean b = object instanceof DebugInfo;
        if (b) {
            l.add((DebugInfo) object);
        }
        if (object != null) {
            summary.appendln(name + (b ? "*" : ""), object.toString());
        }
    }

    public DebugInfo[] debug() {
        return l.toArray(new DebugInfo[l.size()]);
    }

}

// Copyright (c) Naked Objects Group Ltd.
