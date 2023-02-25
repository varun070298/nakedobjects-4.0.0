package org.nakedobjects.metamodel.facets.object.ident.title;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.runtimecontext.spec.IntrospectableSpecificationAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class TitleMethodFacetFactoryTest extends AbstractFacetFactoryTest {

    private TitleMethodFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new TitleMethodFacetFactory();
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

    public void testTitleMethodPickedUpOnClassAndMethodRemoved() {
        class Customer {
            public String title() {
                return "Some title";
            }
        }
        final Method titleMethod = findMethod(Customer.class, "title");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TitleFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TitleFacetViaTitleMethod);
        final TitleFacetViaTitleMethod titleFacetViaTitleMethod = (TitleFacetViaTitleMethod) facet;
        assertEquals(titleMethod, titleFacetViaTitleMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(titleMethod));
    }

    public void testToStringMethodPickedUpOnClassAndMethodRemoved() {
        class Customer {
            @Override
            public String toString() {
                return "Some title via toString";
            }
        }
        final Method toStringMethod = findMethod(Customer.class, "toString");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TitleFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TitleFacetViaToStringMethod);
        final TitleFacetViaToStringMethod titleFacetViaTitleMethod = (TitleFacetViaToStringMethod) facet;
        assertEquals(toStringMethod, titleFacetViaTitleMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(toStringMethod));
    }

    /**
     * This change means that it will be ignored by {@link IntrospectableSpecificationAbstract#getFacet(Class)}
     * is the superclass has a none no-op implementation.
     */
    public void testTitleFacetMethodUsingToStringIsClassifiedAsANoop() {
        assertTrue(new TitleFacetViaToStringMethod(null, facetHolder).isNoop());
    }

    public void testNoExplicitTitleOrToStringMethod() {
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        assertNull(facetHolder.getFacet(TitleFacet.class));

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
