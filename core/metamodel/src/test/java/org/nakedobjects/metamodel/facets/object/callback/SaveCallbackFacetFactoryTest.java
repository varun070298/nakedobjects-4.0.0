package org.nakedobjects.metamodel.facets.object.callback;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistedCallbackFacetViaMethod;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacetViaMethod;
import org.nakedobjects.metamodel.facets.object.callbacks.SaveCallbackFacetFactory;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class SaveCallbackFacetFactoryTest extends AbstractFacetFactoryTest {

    private SaveCallbackFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new SaveCallbackFacetFactory();
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


    public void testSavingLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void saving() {};
        }
        final Method method = findMethod(Customer.class, "saving");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PersistingCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PersistingCallbackFacetViaMethod);
        final PersistingCallbackFacetViaMethod savingCallbackFacetViaMethod = (PersistingCallbackFacetViaMethod) facet;
        assertEquals(method, savingCallbackFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }

    public void testSavedLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void saved() {};
        }
        final Method method = findMethod(Customer.class, "saved");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PersistedCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PersistedCallbackFacetViaMethod);
        final PersistedCallbackFacetViaMethod savedCallbackFacetViaMethod = (PersistedCallbackFacetViaMethod) facet;
        assertEquals(method, savedCallbackFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }


}

// Copyright (c) Naked Objects Group Ltd.
