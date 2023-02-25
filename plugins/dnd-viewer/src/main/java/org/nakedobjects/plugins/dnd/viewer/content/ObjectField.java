package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


final class ObjectField {
    private final NakedObjectAssociation field;
    private final NakedObject parent;

    ObjectField(final NakedObject parent, final NakedObjectAssociation field) {
        this.parent = parent;
        this.field = field;
    }

    public void debugDetails(final DebugString debug) {
        debug.appendln("field", getNakedObjectAssociation());
        debug.appendln("name", getName());
        debug.appendln("specification", getSpecification());
        debug.appendln("parent", parent);
    }

    public String getDescription() {
        return field.getDescription();
    }

    public String getHelp() {
        return field.getHelp();
    }

    public NakedObjectAssociation getNakedObjectAssociation() {
        return field;
    }

    public final String getName() {
        return field.getName();
    }

    public NakedObject getParent() {
        return parent;
    }

    public NakedObjectSpecification getSpecification() {
        return field.getSpecification();
    }
}
// Copyright (c) Naked Objects Group Ltd.
