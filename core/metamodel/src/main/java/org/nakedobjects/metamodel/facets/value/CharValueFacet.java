package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface CharValueFacet extends Facet {
    Character charValue(NakedObject object);

    NakedObject createValue(Character value);
}
// Copyright (c) Naked Objects Group Ltd.
