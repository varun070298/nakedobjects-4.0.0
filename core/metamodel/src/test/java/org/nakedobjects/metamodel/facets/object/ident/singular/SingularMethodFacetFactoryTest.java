package org.nakedobjects.metamodel.facets.object.ident.singular;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class SingularMethodFacetFactoryTest extends AbstractFacetFactoryTest {

    private SingularMethodFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new SingularMethodFacetFactory();
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

    public static class Customer {
        public static String singularName() {
            return "Some name";
        }
    }

    public void testSingularNameMethodPickedUpOnClassAndMethodRemoved() {
        final Method singularNameMethod = findMethod(Customer.class, "singularName");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacetViaMethod);
        final NamedFacetViaMethod namedFacetViaMethod = (NamedFacetViaMethod) facet;
        assertEquals("Some name", namedFacetViaMethod.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(singularNameMethod));
    }

}

// Copyright (c) Naked Objects Group Ltd.
