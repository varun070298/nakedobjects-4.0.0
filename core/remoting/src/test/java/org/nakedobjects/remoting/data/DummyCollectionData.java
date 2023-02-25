package org.nakedobjects.remoting.data;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.remoting.data.common.CollectionData;
import org.nakedobjects.remoting.data.common.ReferenceData;


public class DummyCollectionData extends DummyReferenceData implements CollectionData {
    private static final long serialVersionUID = 1L;
    private final ReferenceData[] elements;
    private final String elementType;

    public DummyCollectionData(
            final Oid oid,
            final String collectionType,
            final String elementType,
            final ReferenceData[] elements,
            final Version version) {
        super(oid, collectionType, version);
        this.elementType = elementType;
        this.elements = elements;
    }

    public ReferenceData[] getElements() {
        return elements;
    }

    public String getElementype() {
        return elementType;
    }

    public boolean hasAllElements() {
        return false;
    }
}
// Copyright (c) Naked Objects Group Ltd.
