package org.nakedobjects.metamodel.spec;

import org.nakedobjects.metamodel.facetdecorator.FacetDecoratorSet;


/**
 * In effect the SPI for {@link NakedObjectSpecification}.
 */
public interface IntrospectableSpecification {

    /**
     * Discovers what attributes and behaviour the type specified by this specification. 
     * 
     * <p>
     * As specifications are cyclic (specifically a class will reference its subclasses, which in turn reference their superclass)
     * they need be created first, and then later work out its internals. This allows for cyclic references to
     * the be accommodated as there should always a specification available even though it might not be
     * complete.
     */
    public void introspect(FacetDecoratorSet decorator);

    public void markAsService();

	public boolean isIntrospected();

}

// Copyright (c) Naked Objects Group Ltd.
