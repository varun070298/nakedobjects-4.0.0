package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface FloatingPointValueFacet extends Facet {

    Float floatValue(NakedObject object);

    NakedObject createValue(Float value);
}
// Copyright (c) Naked Objects Group Ltd.
