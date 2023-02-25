package org.nakedobjects.metamodel.facets.object.ident.plural;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class PluralMethodFacetFactoryTest extends AbstractFacetFactoryTest {

    private PluralMethodFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new PluralMethodFacetFactory();
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
        public static String pluralName() {
            return "Some plural name";
        }
    }

    public void testPluralNameMethodPickedUpOnClassAndMethodRemoved() {
        final Method pluralNameMethod = findMethod(Customer.class, "pluralName");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PluralFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PluralFacetViaMethod);
        final PluralFacetViaMethod pluralFacet = (PluralFacetViaMethod) facet;
        assertEquals("Some plural name", pluralFacet.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(pluralNameMethod));
    }

}

// Copyright (c) Naked Objects Group Ltd.
