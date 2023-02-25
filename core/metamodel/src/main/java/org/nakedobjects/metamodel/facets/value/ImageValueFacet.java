package org.nakedobjects.metamodel.facets.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


public interface ImageValueFacet extends Facet {

    NakedObject createValue(java.awt.Image image);

    java.awt.Image getImage(NakedObject object);

    int getHeight(NakedObject object);

    int getWidth(NakedObject object);
}
// Copyright (c) Naked Objects Group Ltd.
