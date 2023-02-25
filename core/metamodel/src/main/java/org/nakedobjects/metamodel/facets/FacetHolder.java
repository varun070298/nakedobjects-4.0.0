package org.nakedobjects.metamodel.facets;

import org.nakedobjects.metamodel.commons.filters.Filter;


/**
 * Anything in the metamodel (which also includes peers in the reflector) that can be extended.
 */
public interface FacetHolder {

    /**
     * Get the list of all facet <i>types</i> that are supported by objects of this specification.
     */
    Class<? extends Facet>[] getFacetTypes();

    /**
     * Whether there is a facet registered of the specified type.
     * 
     * <p>
     * Convenience; saves having to {@link #getFacet(Class)} and then check if <tt>null</tt>.
     */
    boolean containsFacet(Class<? extends Facet> facetType);

    /**
     * Get the facet of the specified type (as per the type it reports from {@link Facet#facetType()}).
     */
    <T extends Facet> T getFacet(Class<T> cls);

    /**
     * Returns all {@link Facet}s matching the specified {@link FacetFilter}.
     * 
     * @param filter
     * @return
     */
    Facet[] getFacets(Filter<Facet> filter);

    /**
     * Adds the facet, extracting its {@link Facet#facetType() type} as the key.
     * 
     * <p>
     * If there are any facet of the same type, they will be overwritten <i>provided</i> that either the
     * {@link Facet} specifies to {@link Facet#alwaysReplace() always replace} or if the existing
     * {@link Facet} is a {@link Facet#isNoop() no-op}.
     */
    void addFacet(Facet facet);

    /**
     * Adds the {@link MultiTypedFacet multi-typed facet}, extracting each of its
     * {@link MultiTypedFacet#facetTypes() types} as keys.
     * 
     * <p>
     * If there are any facet of the same type, they will be overwritten <i>provided</i> that either the
     * {@link Facet} specifies to {@link Facet#alwaysReplace() always replace} or if the existing
     * {@link Facet} is a {@link Facet#isNoop() no-op}.
     */
    void addFacet(MultiTypedFacet facet);

    /**
     * Remove the facet whose type is that reported by {@link Facet#facetType()}.
     */
    void removeFacet(Facet facet);

    /**
     * Remove the facet of the specified type.
     */
    void removeFacet(Class<? extends Facet> facetType);

}
