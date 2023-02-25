package org.nakedobjects.metamodel.facets.propparam.validate.mask;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Mask;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;


public class MaskAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private MaskAnnotationFacetFactory facetFactory;
    private final NakedObjectSpecification customerNoSpec = new TestProxySpecification(String.class);
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        
        reflector.setLoadSpecificationStringReturn(customerNoSpec);
        facetFactory = new MaskAnnotationFacetFactory();
        facetFactory.setSpecificationLoader(reflector);
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

    public void testMaskAnnotationPickedUpOnClass() {
        @Mask("###")
        class Customer {}
        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(MaskFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MaskFacetAnnotation);
        final MaskFacetAnnotation maskFacet = (MaskFacetAnnotation) facet;
        assertEquals("###", maskFacet.value());
    }

    public void testMaskAnnotationPickedUpOnProperty() {
        class Customer {
            @Mask("###")
            public String getFirstName() {
                return null;
            }
        }
        final Method method = findMethod(Customer.class, "getFirstName");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(MaskFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MaskFacetAnnotation);
        final MaskFacetAnnotation maskFacet = (MaskFacetAnnotation) facet;
        assertEquals("###", maskFacet.value());
    }

    public void testMaskAnnotationPickedUpOnActionParameter() {
        class Customer {
            public void someAction(@Mask("###") final String foo) {}
        }
        final Method method = findMethod(Customer.class, "someAction", new Class[] { String.class });

        facetFactory.processParams(method, 0, facetHolder);

        final Facet facet = facetHolder.getFacet(MaskFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MaskFacetAnnotation);
        final MaskFacetAnnotation maskFacet = (MaskFacetAnnotation) facet;
        assertEquals("###", maskFacet.value());
    }

    public void testMaskAnnotationNotIgnoredForNonStringsProperty() {
        class Customer {
            @Mask("###")
            public int getNumberOfOrders() {
                return 0;
            }
        }
        final Method method = findMethod(Customer.class, "getNumberOfOrders");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        assertNotNull(facetHolder.getFacet(MaskFacet.class));
    }

    public void testMaskAnnotationNotIgnoredForPrimitiveOnActionParameter() {
        class Customer {
            public void someAction(@Mask("###") final int foo) {}
        }
        final Method method = findMethod(Customer.class, "someAction", new Class[] { int.class });

        facetFactory.processParams(method, 0, facetHolder);

        assertNotNull(facetHolder.getFacet(MaskFacet.class));
    }

}

// Copyright (c) Naked Objects Group Ltd.
