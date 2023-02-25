package org.nakedobjects.plugins.dnd.viewer.debug;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.util.Dump;


public class DebugObjectSpecification implements DebugInfo {
    private final NakedObjectSpecification specification;

    public DebugObjectSpecification(final NakedObject object) {
        this.specification = object.getSpecification();
    }

    public DebugObjectSpecification(final NakedObjectSpecification object) {
        this.specification = object;
    }

    public void debugData(final DebugString debug) {
        if (specification == null) {
            debug.appendln("no specfication");
        } else {
            Dump.specification(specification, debug);
        }
    }

    public String debugTitle() {
        return "Object Specification";
    }

}
// Copyright (c) Naked Objects Group Ltd.
