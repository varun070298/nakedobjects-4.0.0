package org.nakedobjects.metamodel.facets.actions;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class IteratorFilteringFacetFactoryTest extends AbstractFacetFactoryTest {

    private IteratorFilteringFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new IteratorFilteringFacetFactory();
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

    public void testRequestsRemoverToRemoveIteratorMethods() {
        class Customer {
            public void someAction() {}
        }
        facetFactory.process(Customer.class, methodRemover, facetHolder);

        assertEquals(1, methodRemover.getRemoveMethodArgsCalls().size());
    }

    public void testNoIteratorMethodFiltered() {
        class Customer {
            public void someAction() {}
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        assertFalse(facetFactory.recognizes(actionMethod));
    }

    /**
     * Not tested; this facet factory is needed, I think, but only filters out stuff when generics are in use.
     */
    public void xxxtestIterableIteratorMethodFiltered() {
        class Customer implements Iterable {
            public void someAction() {}

            public Iterator iterator() {
                return null;
            }
        }
        final Method iteratorMethod = findMethod(Customer.class, "iterator");

        assertTrue(facetFactory.recognizes(iteratorMethod));
    }

}

// Copyright (c) Naked Objects Group Ltd.
