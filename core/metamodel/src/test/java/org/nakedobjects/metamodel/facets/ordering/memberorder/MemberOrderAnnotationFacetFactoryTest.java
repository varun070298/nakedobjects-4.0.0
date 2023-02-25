package org.nakedobjects.metamodel.facets.ordering.memberorder;

import java.lang.reflect.Method;
import java.util.Collection;

import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class MemberOrderAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private MemberOrderAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new MemberOrderAnnotationFacetFactory();
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
        assertTrue(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testMemberOrderAnnotationPickedUpOnProperty() {
        class Customer {
            @MemberOrder(sequence = "1")
            public String getFirstName() {
                return null;
            }
        }
        final Method method = findMethod(Customer.class, "getFirstName");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(MemberOrderFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MemberOrderFacetAnnotation);
        final MemberOrderFacetAnnotation memberOrderFacetAnnotation = (MemberOrderFacetAnnotation) facet;
        assertEquals("1", memberOrderFacetAnnotation.sequence());

        assertNoMethodsRemoved();
    }

    public void testMemberOrderAnnotationPickedUpOnCollection() {
        class Order {}
        class Customer {
            @MemberOrder(sequence = "2")
            public Collection getOrders() {
                return null;
            }

            public void addToOrders(final Order o) {}
        }
        final Method method = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(MemberOrderFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MemberOrderFacetAnnotation);
        final MemberOrderFacetAnnotation memberOrderFacetAnnotation = (MemberOrderFacetAnnotation) facet;
        assertEquals("2", memberOrderFacetAnnotation.sequence());

        assertNoMethodsRemoved();
    }

    public void testMemberOrderAnnotationPickedUpOnAction() {
        class Customer {
            @MemberOrder(sequence = "3")
            public void someAction() {}
        }
        final Method method = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(MemberOrderFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MemberOrderFacetAnnotation);
        final MemberOrderFacetAnnotation memberOrderFacetAnnotation = (MemberOrderFacetAnnotation) facet;
        assertEquals("3", memberOrderFacetAnnotation.sequence());

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
