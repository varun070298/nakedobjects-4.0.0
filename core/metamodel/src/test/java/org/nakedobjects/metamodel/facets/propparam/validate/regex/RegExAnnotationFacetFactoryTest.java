package org.nakedobjects.metamodel.facets.propparam.validate.regex;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.RegEx;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class RegExAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private RegExAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new RegExAnnotationFacetFactory();
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

    public void testRegExAnnotationPickedUpOnClass() {
        @RegEx(validation = "^A.*", caseSensitive = false)
        class Customer {}
        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(RegExFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof RegExFacetAnnotation);
        final RegExFacetAnnotation regExFacet = (RegExFacetAnnotation) facet;
        assertEquals("^A.*", regExFacet.validation());
        assertEquals(false, regExFacet.caseSensitive());
    }

    public void testRegExAnnotationPickedUpOnProperty() {
        class Customer {
            @RegEx(validation = "^A.*", caseSensitive = false)
            public String getFirstName() {
                return null;
            }
        }
        final Method method = findMethod(Customer.class, "getFirstName");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(RegExFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof RegExFacetAnnotation);
        final RegExFacetAnnotation regExFacet = (RegExFacetAnnotation) facet;
        assertEquals("^A.*", regExFacet.validation());
        assertEquals(false, regExFacet.caseSensitive());
    }

    public void testRegExAnnotationPickedUpOnActionParameter() {
        class Customer {
            public void someAction(@RegEx(validation = "^A.*", caseSensitive = false) final String foo) {}
        }
        final Method method = findMethod(Customer.class, "someAction", new Class[] { String.class });

        facetFactory.processParams(method, 0, facetHolder);

        final Facet facet = facetHolder.getFacet(RegExFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof RegExFacetAnnotation);
        final RegExFacetAnnotation regExFacet = (RegExFacetAnnotation) facet;
        assertEquals("^A.*", regExFacet.validation());
        assertEquals(false, regExFacet.caseSensitive());
    }

    public void testRegExAnnotationIgnoredForNonStringsProperty() {
        class Customer {
            @RegEx(validation = "^A.*", caseSensitive = false)
            public int getNumberOfOrders() {
                return 0;
            }
        }
        final Method method = findMethod(Customer.class, "getNumberOfOrders");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        assertNull(facetHolder.getFacet(RegExFacet.class));
    }

    public void testRegExAnnotationIgnoredForPrimitiveOnActionParameter() {
        class Customer {
            public void someAction(@RegEx(validation = "^A.*", caseSensitive = false) final int foo) {}
        }
        final Method method = findMethod(Customer.class, "someAction", new Class[] { int.class });

        facetFactory.processParams(method, 0, facetHolder);

        assertNull(facetHolder.getFacet(RegExFacet.class));
    }

}

// Copyright (c) Naked Objects Group Ltd.
