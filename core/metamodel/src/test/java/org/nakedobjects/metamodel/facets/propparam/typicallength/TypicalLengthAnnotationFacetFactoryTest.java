package org.nakedobjects.metamodel.facets.propparam.typicallength;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.TypicalLength;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class TypicalLengthAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private TypicalLengthAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new TypicalLengthAnnotationFacetFactory();
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
        assertTrue(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testTypicalLengthAnnotationPickedUpOnProperty() {
        class Customer {
            @TypicalLength(30)
            public String getFirstName() {
                return null;
            }
        }
        final Method method = findMethod(Customer.class, "getFirstName");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypicalLengthFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypicalLengthFacetAnnotation);
        final TypicalLengthFacetAnnotation typicalLengthFacetAnnotation = (TypicalLengthFacetAnnotation) facet;
        assertEquals(30, typicalLengthFacetAnnotation.value());
    }

    public void testTypicalLengthAnnotationPickedUpOnActionParameter() {
        class Customer {
            public void someAction(@TypicalLength(20) final int foo) {}
        }
        final Method method = findMethod(Customer.class, "someAction", new Class[] { int.class });

        facetFactory.processParams(method, 0, facetHolder);

        final Facet facet = facetHolder.getFacet(TypicalLengthFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypicalLengthFacetAnnotation);
        final TypicalLengthFacetAnnotation typicalLengthFacetAnnotation = (TypicalLengthFacetAnnotation) facet;
        assertEquals(20, typicalLengthFacetAnnotation.value());
    }

}

// Copyright (c) Naked Objects Group Ltd.
