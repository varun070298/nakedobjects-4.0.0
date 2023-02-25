package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface ByteValueFacet extends Facet {
    Byte byteValue(NakedObject object);

    NakedObject createValue(Byte value);
}
// Copyright (c) Naked Objects Group Ltd.
