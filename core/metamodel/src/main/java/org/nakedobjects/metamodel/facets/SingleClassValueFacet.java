package org.nakedobjects.metamodel.facets;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public interface SingleClassValueFacet extends Facet {

    public Class<?> value();

    /**
     * Convenience to return the {@link NakedObjectSpecification} corresponding to this facet's
     * {@link #value() class}.
     */
    public NakedObjectSpecification valueSpec();

}
