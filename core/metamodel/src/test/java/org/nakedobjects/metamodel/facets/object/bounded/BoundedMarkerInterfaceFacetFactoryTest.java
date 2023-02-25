package org.nakedobjects.metamodel.facets.object.bounded;

import org.nakedobjects.applib.marker.Bounded;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class BoundedMarkerInterfaceFacetFactoryTest extends AbstractFacetFactoryTest {

    private BoundedMarkerInterfaceFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new BoundedMarkerInterfaceFacetFactory();
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

    public void testBoundedInterfaceAnnotationPickedUpOnClass() {
        class Customer implements Bounded {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(BoundedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof BoundedFacetAbstract);

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
