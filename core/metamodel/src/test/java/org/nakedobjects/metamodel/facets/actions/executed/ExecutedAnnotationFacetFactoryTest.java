package org.nakedobjects.metamodel.facets.actions.executed;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Executed;
import org.nakedobjects.applib.annotation.Executed.Where;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class ExecutedAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private ExecutedAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new ExecutedAnnotationFacetFactory();
    }

    @Override
    protected void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }

    @Override
    public void testFeatureTypes() {
        final NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertFalse(contains(featureTypes, NakedObjectFeatureType.OBJECT));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testExecutedLocallyAnnotationPickedUp() {
        class Customer {
            @Executed(Where.LOCALLY)
            public void someAction() {}
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ExecutedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ExecutedFacetAbstract);
        final ExecutedFacetAbstract executedFacetAbstract = (ExecutedFacetAbstract) facet;
        assertEquals(NakedObjectActionConstants.LOCAL, executedFacetAbstract.getTarget());

        assertNoMethodsRemoved();
    }

    public void testExecutedRemotelyAnnotationPickedUp() {
        class Customer {
            @Executed(Where.REMOTELY)
            public void someAction() {}
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ExecutedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ExecutedFacetAbstract);
        final ExecutedFacetAbstract executedFacetAbstract = (ExecutedFacetAbstract) facet;
        assertEquals(NakedObjectActionConstants.REMOTE, executedFacetAbstract.getTarget());

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
