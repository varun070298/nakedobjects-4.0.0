package org.nakedobjects.metamodel.facets.object.callback;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistedCallbackFacetViaMethod;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacetViaMethod;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class PersistCallbackFacetFactoryTest extends AbstractFacetFactoryTest {

    private PersistCallbackFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new PersistCallbackFacetFactory();
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


    public void testPersistingLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void persisting() {};
        }
        final Method method = findMethod(Customer.class, "persisting");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PersistingCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PersistingCallbackFacetViaMethod);
        final PersistingCallbackFacetViaMethod persistingCallbackFacetViaMethod = (PersistingCallbackFacetViaMethod) facet;
        assertEquals(method, persistingCallbackFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }

    public void testPersistedLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void persisted() {};
        }
        final Method method = findMethod(Customer.class, "persisted");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PersistedCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PersistedCallbackFacetViaMethod);
        final PersistedCallbackFacetViaMethod persistedCallbackFacetViaMethod = (PersistedCallbackFacetViaMethod) facet;
        assertEquals(method, persistedCallbackFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }


}

// Copyright (c) Naked Objects Group Ltd.
