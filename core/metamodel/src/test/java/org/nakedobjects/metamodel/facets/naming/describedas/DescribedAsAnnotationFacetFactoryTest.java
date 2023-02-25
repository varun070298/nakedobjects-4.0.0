package org.nakedobjects.metamodel.facets.naming.describedas;

import java.lang.reflect.Method;
import java.util.Collection;

import org.nakedobjects.applib.annotation.DescribedAs;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class DescribedAsAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private DescribedAsAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new DescribedAsAnnotationFacetFactory();
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
        assertTrue(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testDescribedAsAnnotationPickedUpOnClass() {
        @DescribedAs("some description")
        class Customer {}
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DescribedAsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DescribedAsFacetAbstract);
        final DescribedAsFacetAbstract describedAsFacetAbstract = (DescribedAsFacetAbstract) facet;
        assertEquals("some description", describedAsFacetAbstract.value());

        assertNoMethodsRemoved();
    }

    public void testDescribedAsAnnotationPickedUpOnProperty() {
        class Customer {
            @DescribedAs("some description")
            public int getNumberOfOrders() {
                return 0;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "getNumberOfOrders");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DescribedAsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DescribedAsFacetAbstract);
        final DescribedAsFacetAbstract describedAsFacetAbstract = (DescribedAsFacetAbstract) facet;
        assertEquals("some description", describedAsFacetAbstract.value());

        assertNoMethodsRemoved();
    }

    public void testDescribedAsAnnotationPickedUpOnCollection() {
        class Customer {
            @DescribedAs("some description")
            public Collection getOrders() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DescribedAsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DescribedAsFacetAbstract);
        final DescribedAsFacetAbstract describedAsFacetAbstract = (DescribedAsFacetAbstract) facet;
        assertEquals("some description", describedAsFacetAbstract.value());

        assertNoMethodsRemoved();
    }

    public void testDescribedAsAnnotationPickedUpOnAction() {
        class Customer {
            @DescribedAs("some description")
            public void someAction() {}
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DescribedAsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DescribedAsFacetAbstract);
        final DescribedAsFacetAbstract describedAsFacetAbstract = (DescribedAsFacetAbstract) facet;
        assertEquals("some description", describedAsFacetAbstract.value());

        assertNoMethodsRemoved();
    }

    public void testDescribedAsAnnotationPickedUpOnActionParameter() {
        class Customer {
            public void someAction(@DescribedAs("some description") final int x) {}
        }
        final Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class });

        facetFactory.processParams(actionMethod, 0, facetHolder);

        final Facet facet = facetHolder.getFacet(DescribedAsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DescribedAsFacetAbstract);
        final DescribedAsFacetAbstract describedAsFacetAbstract = (DescribedAsFacetAbstract) facet;
        assertEquals("some description", describedAsFacetAbstract.value());
    }

}

// Copyright (c) Naked Objects Group Ltd.
