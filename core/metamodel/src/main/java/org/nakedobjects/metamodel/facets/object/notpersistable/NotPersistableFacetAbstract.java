package org.nakedobjects.metamodel.facets.object.notpersistable;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleValueFacetAbstract;


public abstract class NotPersistableFacetAbstract extends SingleValueFacetAbstract implements NotPersistableFacet {

    public static Class<? extends Facet> type() {
        return NotPersistableFacet.class;
    }

    private final InitiatedBy value;

    public NotPersistableFacetAbstract(final InitiatedBy value, final FacetHolder holder) {
        super(type(), holder);
        this.value = value;
    }

    public InitiatedBy value() {
        return value;
    }

}
