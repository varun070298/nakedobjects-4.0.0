package org.nakedobjects.metamodel.facets.hide;

import java.lang.reflect.Method;
import java.util.Collection;

import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.When;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class HiddenAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private HiddenAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new HiddenAnnotationFacetFactory();
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
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testHiddenAnnotationPickedUpOnProperty() {
        class Customer {
            @Hidden
            public int getNumberOfOrders() {
                return 0;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "getNumberOfOrders");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HiddenFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HiddenFacetAbstract);

        assertNoMethodsRemoved();
    }

    public void testHiddenAnnotationPickedUpOnCollection() {
        class Customer {
            @Hidden
            public Collection getOrders() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HiddenFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HiddenFacetAbstract);

        assertNoMethodsRemoved();
    }

    public void testHiddenAnnotationPickedUpOnAction() {
        class Customer {
            @Hidden
            public void someAction() {}
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HiddenFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HiddenFacetAbstract);

        assertNoMethodsRemoved();
    }

    public void testHiddenWhenAlwaysAnnotationPickedUpOn() {
        class Customer {
            @Hidden(When.ALWAYS)
            public void someAction() {}
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HiddenFacet.class);
        final HiddenFacetAbstract hiddenFacetAbstract = (HiddenFacetAbstract) facet;

        assertEquals(org.nakedobjects.metamodel.facets.When.ALWAYS, hiddenFacetAbstract.value());
    }

    public void testHiddenWhenNeverAnnotationPickedUpOn() {
        class Customer {
            @Hidden(When.NEVER)
            public void someAction() {}
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HiddenFacet.class);
        final HiddenFacetAbstract hiddenFacetAbstract = (HiddenFacetAbstract) facet;

        assertEquals(org.nakedobjects.metamodel.facets.When.NEVER, hiddenFacetAbstract.value());
    }

    public void testHiddenWhenOncePersistedAnnotationPickedUpOn() {
        class Customer {
            @Hidden(When.ONCE_PERSISTED)
            public void someAction() {}
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HiddenFacet.class);
        final HiddenFacetAbstract hiddenFacetAbstract = (HiddenFacetAbstract) facet;

        assertEquals(org.nakedobjects.metamodel.facets.When.ONCE_PERSISTED, hiddenFacetAbstract.value());
    }

    public void testDisabledWhenUntilPersistedAnnotationPickedUpOn() {
        class Customer {
            @Hidden(When.UNTIL_PERSISTED)
            public void someAction() {}
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HiddenFacet.class);
        final HiddenFacetAbstract hiddenFacetAbstract = (HiddenFacetAbstract) facet;

        assertEquals(org.nakedobjects.metamodel.facets.When.UNTIL_PERSISTED, hiddenFacetAbstract.value());
    }

}

// Copyright (c) Naked Objects Group Ltd.
