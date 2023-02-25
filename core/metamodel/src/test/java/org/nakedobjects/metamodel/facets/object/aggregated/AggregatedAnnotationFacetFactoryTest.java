package org.nakedobjects.metamodel.facets.object.aggregated;

import org.nakedobjects.applib.annotation.Aggregated;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class AggregatedAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private AggregatedAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new AggregatedAnnotationFacetFactory();
    }

    @Override
    protected void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }

    @Override
    public void testFeatureTypes() {
        final NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertTrue(contains(featureTypes, NakedObjectFeatureType.OBJECT));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testImmutableAnnotationPickedUpOnClassAndDefaultsToAlways() {
        @Aggregated
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(AggregatedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof AggregatedFacetAnnotation);

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
