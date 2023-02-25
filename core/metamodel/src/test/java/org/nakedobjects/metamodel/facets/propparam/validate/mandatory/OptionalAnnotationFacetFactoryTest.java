package org.nakedobjects.metamodel.facets.propparam.validate.mandatory;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Optional;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class OptionalAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private OptionalAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new OptionalAnnotationFacetFactory();
    }

    @Override
    protected void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }

    @Override
    public void testFeatureTypes() {
        final NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertFalse(contains(featureTypes, NakedObjectFeatureType.OBJECT));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testOptionalAnnotationPickedUpOnProperty() {
        class Customer {
            @Optional
            public String getFirstName() {
                return null;
            }
        }
        final Method method = findMethod(Customer.class, "getFirstName");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof OptionalFacet);
    }

    public void testOptionalAnnotationPickedUpOnActionParameter() {
        class Customer {
            public void someAction(@Optional final String foo) {}
        }
        final Method method = findMethod(Customer.class, "someAction", new Class[] { String.class });

        facetFactory.processParams(method, 0, facetHolder);

        final Facet facet = facetHolder.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof OptionalFacet);
    }

    public void testOptionalAnnotationIgnoredForPrimitiveOnProperty() {
        class Customer {
            @Optional
            public int getNumberOfOrders() {
                return 0;
            }
        }
        final Method method = findMethod(Customer.class, "getNumberOfOrders");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        assertNull(facetHolder.getFacet(MandatoryFacet.class));
    }

    public void testOptionalAnnotationIgnoredForPrimitiveOnActionParameter() {
        class Customer {
            public void someAction(@Optional final int foo) {}
        }
        final Method method = findMethod(Customer.class, "someAction", new Class[] { int.class });

        facetFactory.processParams(method, 0, facetHolder);

        assertNull(facetHolder.getFacet(MandatoryFacet.class));
    }

}

// Copyright (c) Naked Objects Group Ltd.
