package org.nakedobjects.metamodel.facets.actions.invoke;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


/**
 * Represents the mechanism by which the action should be invoked.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the actual action method itself (a
 * <tt>public</tt> method that does not represent a property, a collection or any of the supporting methods).
 */
public interface ActionInvocationFacet extends Facet {

    public NakedObject invoke(NakedObject target, NakedObject[] parameters);

    public NakedObjectSpecification getReturnType();

    public NakedObjectSpecification getOnType();

}

// Copyright (c) Naked Objects Group Ltd.
