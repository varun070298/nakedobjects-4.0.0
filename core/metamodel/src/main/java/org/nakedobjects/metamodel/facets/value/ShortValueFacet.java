package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface ShortValueFacet extends Facet {
    Short shortValue(NakedObject object);

    NakedObject createValue(Short value);
}
// Copyright (c) Naked Objects Group Ltd.
