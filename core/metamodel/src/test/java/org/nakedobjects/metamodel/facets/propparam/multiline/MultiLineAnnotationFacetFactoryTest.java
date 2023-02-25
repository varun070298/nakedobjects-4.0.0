package org.nakedobjects.metamodel.facets.propparam.multiline;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.MultiLine;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class MultiLineAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private MultiLineAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new MultiLineAnnotationFacetFactory();
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

    public void testMultiLineAnnotationPickedUpOnClass() {
        @MultiLine(numberOfLines = 3, preventWrapping = false)
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(MultiLineFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MultiLineFacetAnnotation);
        final MultiLineFacetAnnotation multiLineFacetAnnotation = (MultiLineFacetAnnotation) facet;
        assertEquals(3, multiLineFacetAnnotation.numberOfLines());
        assertEquals(false, multiLineFacetAnnotation.preventWrapping());
    }

    public void testMultiLineAnnotationPickedUpOnProperty() {
        class Customer {
            @MultiLine(numberOfLines = 12, preventWrapping = true)
            public String getFirstName() {
                return null;
            }
        }
        final Method method = findMethod(Customer.class, "getFirstName");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(MultiLineFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MultiLineFacetAnnotation);
        final MultiLineFacetAnnotation multiLineFacetAnnotation = (MultiLineFacetAnnotation) facet;
        assertEquals(12, multiLineFacetAnnotation.numberOfLines());
        assertEquals(true, multiLineFacetAnnotation.preventWrapping());
    }

    public void testMultiLineAnnotationPickedUpOnActionParameter() {
        class Customer {
            public void someAction(@MultiLine(numberOfLines = 8, preventWrapping = false) final String foo) {}
        }
        final Method method = findMethod(Customer.class, "someAction", new Class[] { String.class });

        facetFactory.processParams(method, 0, facetHolder);

        final Facet facet = facetHolder.getFacet(MultiLineFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MultiLineFacetAnnotation);
        final MultiLineFacetAnnotation multiLineFacetAnnotation = (MultiLineFacetAnnotation) facet;
        assertEquals(8, multiLineFacetAnnotation.numberOfLines());
        assertEquals(false, multiLineFacetAnnotation.preventWrapping());
    }

    public void testMultiLineAnnotationDefaults() {
        @MultiLine
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(MultiLineFacet.class);
        final MultiLineFacetAnnotation multiLineFacetAnnotation = (MultiLineFacetAnnotation) facet;
        assertEquals(6, multiLineFacetAnnotation.numberOfLines());
        assertEquals(true, multiLineFacetAnnotation.preventWrapping());
    }

    public void testMultiLineAnnotationIgnoredForNonStringProperties() {
        class Customer {
            @MultiLine(numberOfLines = 8, preventWrapping = false)
            public int getNumberOfOrders() {
                return 0;
            }
        }
        final Method method = findMethod(Customer.class, "getNumberOfOrders");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(MultiLineFacet.class);
        assertNull(facet);
    }

    public void testMultiLineAnnotationIgnoredForNonStringActionParameters() {
        class Customer {
            public void someAction(@MultiLine(numberOfLines = 8, preventWrapping = false) final int foo) {}
        }
        final Method method = findMethod(Customer.class, "someAction", new Class[] { int.class });

        facetFactory.processParams(method, 0, facetHolder);

        assertNull(facetHolder.getFacet(MultiLineFacet.class));
    }

}

// Copyright (c) Naked Objects Group Ltd.
