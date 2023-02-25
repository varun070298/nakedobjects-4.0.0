package org.nakedobjects.metamodel.facets.object.callbacks;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.java5.ImperativeFacetMulti;


/**
 * A {@link Facet} that represents some type of lifecycle callback on the object (eg about to be persisted).
 */
public interface CallbackFacet extends Facet, ImperativeFacetMulti {

    public void invoke(NakedObject object);

}

// Copyright (c) Naked Objects Group Ltd.
