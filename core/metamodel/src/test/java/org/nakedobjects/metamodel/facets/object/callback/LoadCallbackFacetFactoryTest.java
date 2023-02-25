package org.nakedobjects.metamodel.facets.object.callback;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.callbacks.LoadCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.LoadedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.LoadedCallbackFacetViaMethod;
import org.nakedobjects.metamodel.facets.object.callbacks.LoadingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.LoadingCallbackFacetViaMethod;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class LoadCallbackFacetFactoryTest extends AbstractFacetFactoryTest {

    private LoadCallbackFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new LoadCallbackFacetFactory();
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


    public void testLoadingLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void loading() {};
        }
        final Method method = findMethod(Customer.class, "loading");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(LoadingCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof LoadingCallbackFacetViaMethod);
        final LoadingCallbackFacetViaMethod loadingCallbackFacetViaMethod = (LoadingCallbackFacetViaMethod) facet;
        assertEquals(method, loadingCallbackFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }

    public void testLoadedLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void loaded() {};
        }
        final Method method = findMethod(Customer.class, "loaded");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(LoadedCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof LoadedCallbackFacetViaMethod);
        final LoadedCallbackFacetViaMethod loadedCallbackFacetViaMethod = (LoadedCallbackFacetViaMethod) facet;
        assertEquals(method, loadedCallbackFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }


}

// Copyright (c) Naked Objects Group Ltd.
