package org.nakedobjects.metamodel.facets.object.callback;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdateCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdatedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdatedCallbackFacetViaMethod;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdatingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdatingCallbackFacetViaMethod;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class UpdateCallbackFacetFactoryTest extends AbstractFacetFactoryTest {

    private UpdateCallbackFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new UpdateCallbackFacetFactory();
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


    public void testUpdatingLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void updating() {};
        }
        final Method method = findMethod(Customer.class, "updating");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(UpdatingCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof UpdatingCallbackFacetViaMethod);
        final UpdatingCallbackFacetViaMethod updatingCallbackFacetViaMethod = (UpdatingCallbackFacetViaMethod) facet;
        assertEquals(method, updatingCallbackFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }

    public void testUpdatedLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void updated() {};
        }
        final Method method = findMethod(Customer.class, "updated");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(UpdatedCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof UpdatedCallbackFacetViaMethod);
        final UpdatedCallbackFacetViaMethod updatedCallbackFacetViaMethod = (UpdatedCallbackFacetViaMethod) facet;
        assertEquals(method, updatedCallbackFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }


}

// Copyright (c) Naked Objects Group Ltd.
