package org.nakedobjects.metamodel.facets.ordering.fieldorder;

import org.nakedobjects.applib.annotation.FieldOrder;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class FieldOrderAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private FieldOrderAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new FieldOrderAnnotationFacetFactory();
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

    public void testFieldOrderAnnotationPickedUpOnClass() {
        @FieldOrder("foo,bar")
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(FieldOrderFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof FieldOrderFacetAnnotation);
        final FieldOrderFacetAnnotation fieldOrderFacetAnnotation = (FieldOrderFacetAnnotation) facet;
        assertEquals("foo,bar", fieldOrderFacetAnnotation.value());

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
