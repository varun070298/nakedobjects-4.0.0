package org.nakedobjects.plugins.xml.objectstore.internal.data;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.xml.objectstore.internal.version.FileVersion;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;


/**
 * A logical collection of elements of a specified type
 */
public class CollectionData extends Data {
    private final ReferenceVector elements;

    public CollectionData(final NakedObjectSpecification noSpec, final SerialOid oid, final FileVersion version) {
        super(noSpec, oid, version);
        elements = new ReferenceVector();
    }

    public void addElement(final SerialOid elementOid) {
        elements.add(elementOid);
    }

    public void removeElement(final SerialOid elementOid) {
        elements.remove(elementOid);
    }

    public ReferenceVector references() {
        return elements;
    }

    @Override
    public String toString() {
        return "CollectionData[type=" + getTypeName() + ",elements=" + elements + "]";
    }
}
// Copyright (c) Naked Objects Group Ltd.
