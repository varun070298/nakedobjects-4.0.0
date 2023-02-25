package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface IntegerValueFacet extends Facet {
    Integer integerValue(NakedObject object);

    NakedObject createValue(Integer value);
}
// Copyright (c) Naked Objects Group Ltd.
