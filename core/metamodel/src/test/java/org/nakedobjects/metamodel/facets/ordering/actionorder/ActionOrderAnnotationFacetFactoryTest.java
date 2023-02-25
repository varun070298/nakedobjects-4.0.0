package org.nakedobjects.metamodel.facets.ordering.actionorder;

import org.nakedobjects.applib.annotation.ActionOrder;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class ActionOrderAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private ActionOrderAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new ActionOrderAnnotationFacetFactory();
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

    public void testActionOrderAnnotationPickedUpOnClass() {
        @ActionOrder("foo,bar")
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ActionOrderFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ActionOrderFacetAnnotation);
        final ActionOrderFacetAnnotation actionOrderFacetAnnotation = (ActionOrderFacetAnnotation) facet;
        assertEquals("foo,bar", actionOrderFacetAnnotation.value());

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
