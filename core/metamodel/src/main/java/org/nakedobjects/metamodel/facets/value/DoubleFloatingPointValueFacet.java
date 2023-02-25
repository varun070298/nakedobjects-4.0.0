package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface DoubleFloatingPointValueFacet extends Facet {
    Double doubleValue(NakedObject object);

    NakedObject createValue(Double value);
}
// Copyright (c) Naked Objects Group Ltd.
