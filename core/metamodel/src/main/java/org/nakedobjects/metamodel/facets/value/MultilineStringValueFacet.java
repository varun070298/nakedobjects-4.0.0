package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface MultilineStringValueFacet extends Facet {
    String stringValue(NakedObject object);

    NakedObject createValue(String value);

}
// Copyright (c) Naked Objects Group Ltd.
