package org.nakedobjects.metamodel.facets.object.facets;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.facets.MultipleValueFacet;


/**
 * Indicates that this class has additional arbitrary facets, to be processed.
 * 
 * <p>
 * Corresponds to the <tt>@Facets</tt> annotation in the applib.
 * 
 * <p>
 * <i>This</i> {@link Facet} allows the {@link FacetFactory}(s) that will create those {@link Facet}s to be
 * accessed. Which, admittedly, is rather confusing.
 */
public interface FacetsFacet extends MultipleValueFacet {

    /**
     * Returns the fully qualified class of the facet factory, which should be
     * {@link Class#isAssignableFrom(Class)} {@link FacetFactory}.
     * 
     * <p>
     * Includes both the named facet factories and those identified directly by class. However, all are
     * guaranteed to implement {@link FacetFactory}.
     */
    public Class<? extends FacetFactory>[] facetFactories();
}
