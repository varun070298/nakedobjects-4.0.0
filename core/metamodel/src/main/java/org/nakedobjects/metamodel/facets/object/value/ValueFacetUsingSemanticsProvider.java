package org.nakedobjects.metamodel.facets.object.value;

import org.nakedobjects.applib.adapters.ValueSemanticsProvider;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class ValueFacetUsingSemanticsProvider extends ValueFacetAbstract {

    public ValueFacetUsingSemanticsProvider(final ValueSemanticsProvider<?> adapter, final Facet underlyingValueTypeFacet, RuntimeContext runtimeContext) {
        super(adapter, true, underlyingValueTypeFacet.getFacetHolder(), runtimeContext);

        // add the adapter in as its own facet (eg StringFacet).
        // This facet is almost certainly superfluous; there is nothing in the
        // viewers that needs to get hold of such a facet, for example.
        FacetUtil.addFacet(underlyingValueTypeFacet);
    }

}
