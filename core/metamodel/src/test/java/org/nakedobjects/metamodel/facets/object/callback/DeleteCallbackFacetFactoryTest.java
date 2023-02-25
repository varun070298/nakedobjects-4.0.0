package org.nakedobjects.metamodel.facets.object.callback;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.callbacks.DeleteCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovedCallbackFacetViaMethod;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovingCallbackFacetViaMethod;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class DeleteCallbackFacetFactoryTest extends AbstractFacetFactoryTest {

    private DeleteCallbackFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new DeleteCallbackFacetFactory();
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


    public void testDeletingLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void deleting() {};
        }
        final Method method = findMethod(Customer.class, "deleting");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(RemovingCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof RemovingCallbackFacetViaMethod);
        final RemovingCallbackFacetViaMethod deletingCallbackFacetViaMethod = (RemovingCallbackFacetViaMethod) facet;
        assertEquals(method, deletingCallbackFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }

    public void testDeletedLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void deleted() {};
        }
        final Method method = findMethod(Customer.class, "deleted");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(RemovedCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof RemovedCallbackFacetViaMethod);
        final RemovedCallbackFacetViaMethod deletedCallbackFacetViaMethod = (RemovedCallbackFacetViaMethod) facet;
        assertEquals(method, deletedCallbackFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }

}

// Copyright (c) Naked Objects Group Ltd.
