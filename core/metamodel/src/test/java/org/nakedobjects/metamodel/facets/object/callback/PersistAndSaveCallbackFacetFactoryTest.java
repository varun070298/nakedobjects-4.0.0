package org.nakedobjects.metamodel.facets.object.callback;

import java.lang.reflect.Method;
import java.util.List;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistedCallbackFacetViaMethod;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacetViaMethod;
import org.nakedobjects.metamodel.facets.object.callbacks.SaveCallbackFacetFactory;


public class PersistAndSaveCallbackFacetFactoryTest extends AbstractFacetFactoryTest {

    private SaveCallbackFacetFactory saveFacetFactory;
    private PersistCallbackFacetFactory persistFacetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        saveFacetFactory = new SaveCallbackFacetFactory();
        persistFacetFactory = new PersistCallbackFacetFactory();
    }

	@Override
	public void testFeatureTypes() {
		// inherited - ignore.
	}


    @Override
    protected void tearDown() throws Exception {
        saveFacetFactory = null;
        persistFacetFactory = null;
        super.tearDown();
    }

    public void testSavingAndPersistingLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void saving() {};
            @SuppressWarnings("unused")
			public void persisting() {};
        }
        final Method saveMethod = findMethod(Customer.class, "saving");
        final Method persistMethod = findMethod(Customer.class, "persisting");

        saveFacetFactory.process(Customer.class, methodRemover, facetHolder);
        persistFacetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PersistingCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PersistingCallbackFacetViaMethod);
        final PersistingCallbackFacetViaMethod persistingCallbackFacetViaMethod = (PersistingCallbackFacetViaMethod) facet;
        List<Method> methods = persistingCallbackFacetViaMethod.getMethods();
		assertTrue(methods.contains(saveMethod));
		assertTrue(methods.contains(persistMethod));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(saveMethod));
    }

    public void testSavedAndPersistedLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void saved() {};
            @SuppressWarnings("unused")
			public void persisted() {};
        }
        final Method saveMethod = findMethod(Customer.class, "saved");
        final Method persistMethod = findMethod(Customer.class, "persisted");

        saveFacetFactory.process(Customer.class, methodRemover, facetHolder);
        persistFacetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PersistedCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PersistedCallbackFacetViaMethod);
        final PersistedCallbackFacetViaMethod persistedCallbackFacetViaMethod = (PersistedCallbackFacetViaMethod) facet;
        List<Method> methods = persistedCallbackFacetViaMethod.getMethods();
		assertTrue(methods.contains(saveMethod));
		assertTrue(methods.contains(persistMethod));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(saveMethod));
    }

}

// Copyright (c) Naked Objects Group Ltd.
