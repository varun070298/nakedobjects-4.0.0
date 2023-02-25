package org.nakedobjects.metamodel.facets.object.callback;

import java.lang.reflect.Method;
import java.util.List;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.callbacks.DeleteCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.RemoveCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovedCallbackFacetViaMethod;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.RemovingCallbackFacetViaMethod;


public class RemoveAndDeleteCallbackFacetFactoryTest extends AbstractFacetFactoryTest {

	private RemoveCallbackFacetFactory removeFacetFactory;
    private DeleteCallbackFacetFactory deleteFacetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        removeFacetFactory = new RemoveCallbackFacetFactory();
        deleteFacetFactory = new DeleteCallbackFacetFactory();
    }

	@Override
	public void testFeatureTypes() {
		// inherited - ignore.
	}


    @Override
    protected void tearDown() throws Exception {
        removeFacetFactory = null;
        deleteFacetFactory = null;
        super.tearDown();
    }

    public void testSavingAndPersistingLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void deleting() {};
            @SuppressWarnings("unused")
			public void removing() {};
        }
        final Method deleteMethod = findMethod(Customer.class, "deleting");
        final Method removeMethod = findMethod(Customer.class, "removing");

        removeFacetFactory.process(Customer.class, methodRemover, facetHolder);
        deleteFacetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(RemovingCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof RemovingCallbackFacetViaMethod);
        final RemovingCallbackFacetViaMethod removingCallbackFacetViaMethod = (RemovingCallbackFacetViaMethod) facet;
        List<Method> methods = removingCallbackFacetViaMethod.getMethods();
		assertTrue(methods.contains(deleteMethod));
		assertTrue(methods.contains(removeMethod));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(deleteMethod));
    }

    public void testSavedAndPersistedLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void deleted() {};
            @SuppressWarnings("unused")
			public void removed() {};
        }
        final Method removeMethod = findMethod(Customer.class, "removed");
        final Method deleteMethod = findMethod(Customer.class, "deleted");

        removeFacetFactory.process(Customer.class, methodRemover, facetHolder);
        deleteFacetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(RemovedCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof RemovedCallbackFacetViaMethod);
        final RemovedCallbackFacetViaMethod removedCallbackFacetViaMethod = (RemovedCallbackFacetViaMethod) facet;
        List<Method> methods = removedCallbackFacetViaMethod.getMethods();
		assertTrue(methods.contains(removeMethod));
		assertTrue(methods.contains(deleteMethod));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(removeMethod));
    }

}

// Copyright (c) Naked Objects Group Ltd.
