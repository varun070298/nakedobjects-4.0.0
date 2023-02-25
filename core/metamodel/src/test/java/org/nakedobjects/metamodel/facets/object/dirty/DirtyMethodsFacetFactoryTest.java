package org.nakedobjects.metamodel.facets.object.dirty;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class DirtyMethodsFacetFactoryTest extends AbstractFacetFactoryTest {

    private DirtyMethodsFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new DirtyMethodsFacetFactory();
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

    public void testMarkDirtyMethodPickedUpOn() {
        class Customer {
            public void markDirty() {};
        }
        final Method method = findMethod(Customer.class, "markDirty");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(MarkDirtyObjectFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MarkDirtyObjectFacetViaMethod);
        final MarkDirtyObjectFacetViaMethod markDirtyFacet = (MarkDirtyObjectFacetViaMethod) facet;
        assertEquals(method, markDirtyFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }

    public void testIsDirtyMethodPickedUpOn() {
        class Customer {
            public boolean isDirty() {
                return false;
            };
        }
        final Method method = findMethod(Customer.class, "isDirty");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(IsDirtyObjectFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof IsDirtyObjectFacetViaMethod);
        final IsDirtyObjectFacetViaMethod isDirtyFacet = (IsDirtyObjectFacetViaMethod) facet;
        assertEquals(method, isDirtyFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }

    public void testClearDirtyMethodPickedUpOn() {
        class Customer {
            public void clearDirty() {};
        }
        final Method method = findMethod(Customer.class, "clearDirty");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ClearDirtyObjectFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ClearDirtyObjectFacetViaMethod);
        final ClearDirtyObjectFacetViaMethod clearDirtyFacet = (ClearDirtyObjectFacetViaMethod) facet;
        assertEquals(method, clearDirtyFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(method));
    }

}

// Copyright (c) Naked Objects Group Ltd.
