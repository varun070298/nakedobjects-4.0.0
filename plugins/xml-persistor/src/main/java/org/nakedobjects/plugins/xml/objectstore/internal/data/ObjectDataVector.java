package org.nakedobjects.plugins.xml.objectstore.internal.data;

import java.util.Vector;


public class ObjectDataVector {
    Vector elements = new Vector();

    public void addElement(final ObjectData instanceData) {
        elements.addElement(instanceData);
    }

    public int size() {
        return elements.size();
    }

    public ObjectData element(final int i) {
        return (ObjectData) elements.elementAt(i);
    }

    public boolean contains(final ObjectData data) {
        return elements.contains(data);
    }
}
// Copyright (c) Naked Objects Group Ltd.
