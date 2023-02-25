package org.nakedobjects.metamodel.facets.object.callback;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.callbacks.CreatedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.CreatedCallbackFacetFactory;
import org.nakedobjects.metamodel.facets.object.callbacks.CreatedCallbackFacetViaMethod;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class CreatedCallbackFacetFactoryTest extends AbstractFacetFactoryTest {

    private CreatedCallbackFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new CreatedCallbackFacetFactory();
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

    public void testCreatedLifecycleMethodPickedUpOn() {
        class Customer {
            @SuppressWarnings("unused")
			public void created() {};
        }
        final Method method = findMethod(Customer.class, "created");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(CreatedCallbackFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof CreatedCallbackFacetViaMethod);
        final CreatedCallbackFacetViaMethod createdCallbackFacetViaMethod = (CreatedCallbackFacetViaMethod) facet;
        assertEquals(method, createdCallbackFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }


}

// Copyright (c) Naked Objects Group Ltd.
