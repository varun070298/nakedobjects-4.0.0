package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface BooleanValueFacet extends Facet {

    boolean isSet(NakedObject object);

    void set(NakedObject object);

    void reset(NakedObject object);

    void toggle(NakedObject object);
}
// Copyright (c) Naked Objects Group Ltd.
