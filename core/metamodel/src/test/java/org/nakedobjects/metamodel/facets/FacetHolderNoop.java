package org.nakedobjects.metamodel.facets;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.commons.filters.Filter;


/**
 * Has no functionality but makes it easier to write tests that require an instance of an {@link Identifier}.
 */
public class FacetHolderNoop implements FacetHolder {

    public void addFacet(final Facet facet) {}

    public void addFacet(final MultiTypedFacet facet) {}

    public boolean containsFacet(final Class<? extends Facet> facetType) {
        return false;
    }

    public <T extends Facet> T getFacet(final Class<T> cls) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Facet>[] getFacetTypes() {
        return new Class[0];
    }

    public Facet[] getFacets(final Filter<Facet> filter) {
        return new Facet[0];
    }

    public Identifier getIdentifier() {
        return null;
    }

    public void removeFacet(final Facet facet) {}

    public void removeFacet(final Class<? extends Facet> facetType) {}

}

// Copyright (c) Naked Objects Group Ltd.
