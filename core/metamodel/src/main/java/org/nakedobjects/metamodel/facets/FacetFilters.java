package org.nakedobjects.metamodel.facets;

import org.nakedobjects.metamodel.commons.filters.AbstractFilter;
import org.nakedobjects.metamodel.commons.filters.Filter;


public final class FacetFilters {

    private FacetFilters() {}

    /**
     * {@link Filter<Facet>#accept(Facet) Accepts} everything.
     */
    public static final Filter<Facet> ANY = new AbstractFilter<Facet>() {
        @Override
        public boolean accept(final Facet facet) {
            return true;
        }
    };
    /**
     * {@link Filter<Facet>#accept(Facet) Accepts} nothing.
     */
    public static final Filter<Facet> NONE = new AbstractFilter<Facet>() {
        @Override
        public boolean accept(final Facet facet) {
            return false;
        }
    };

    public static Filter<Facet> isA(final Class<?> superClass) {
        return new AbstractFilter<Facet>() {
            @Override
            public boolean accept(final Facet facet) {
                if (facet instanceof DecoratingFacet) {
                    final DecoratingFacet<?> decoratingFacet = (DecoratingFacet<?>) facet;
                    return accept(decoratingFacet.getDecoratedFacet());
                }
                return superClass.isAssignableFrom(facet.getClass());
            }
        };
    }

}
