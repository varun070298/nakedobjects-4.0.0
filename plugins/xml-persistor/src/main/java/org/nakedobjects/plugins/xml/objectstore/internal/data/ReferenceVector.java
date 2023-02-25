package org.nakedobjects.plugins.xml.objectstore.internal.data;

import java.util.Vector;

import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;


public class ReferenceVector {
    private final Vector elements = new Vector();

    public void add(final SerialOid oid) {
        elements.addElement(oid);
    }

    public void remove(final SerialOid oid) {
        elements.removeElement(oid);
    }

    public int size() {
        return elements.size();
    }

    public SerialOid elementAt(final int index) {
        return (SerialOid) elements.elementAt(index);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof ReferenceVector) {
            return ((ReferenceVector) obj).elements.equals(elements);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int h = 17;
        h = 37 * h + elements.hashCode();
        return h;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("refs", elements);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
