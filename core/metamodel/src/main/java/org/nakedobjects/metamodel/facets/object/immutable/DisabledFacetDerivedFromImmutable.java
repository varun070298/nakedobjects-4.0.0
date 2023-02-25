package org.nakedobjects.metamodel.facets.object.immutable;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.disable.DisabledFacetAbstract;


public class DisabledFacetDerivedFromImmutable extends DisabledFacetAbstract {

    public DisabledFacetDerivedFromImmutable(final ImmutableFacet immutableFacet, final FacetHolder holder) {
        super(immutableFacet.value(), holder);
    }

    public String disabledReason(final NakedObject target) {
        return when().isNowFor(target) ? "Immutable" : null;
    }

}
