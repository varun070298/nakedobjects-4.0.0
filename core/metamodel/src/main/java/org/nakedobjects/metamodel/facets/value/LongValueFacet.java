package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface LongValueFacet extends Facet {
    Long longValue(NakedObject object);

    NakedObject createValue(Long value);
}
// Copyright (c) Naked Objects Group Ltd.
