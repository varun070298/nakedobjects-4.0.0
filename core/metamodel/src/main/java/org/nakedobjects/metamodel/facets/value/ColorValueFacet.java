package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface ColorValueFacet extends Facet {
    int colorValue(NakedObject object);

    NakedObject createValue(NakedObject object, int color);
}
// Copyright (c) Naked Objects Group Ltd.
