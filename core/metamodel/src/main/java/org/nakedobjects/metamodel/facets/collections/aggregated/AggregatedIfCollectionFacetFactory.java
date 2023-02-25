package org.nakedobjects.metamodel.facets.collections.aggregated;

import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.collections.CollectionFacetFactory;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.object.aggregated.AggregatedFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacets;


/**
 * All {@link CollectionTypeRegistry collection types} are intrinsically {@link AggregatedFacet aggregeted}.
 * 
 * 
 * Must be registered in the  {@link ProgrammingModelFacets} after {@link CollectionFacetFactory}.
 */
public class AggregatedIfCollectionFacetFactory extends FacetFactoryAbstract {

    public AggregatedIfCollectionFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        if (!holder.containsFacet(CollectionFacet.class)) {
            return false;
        }
        return FacetUtil.addFacet(new AggregatedSinceCollection(holder));
    }

}
