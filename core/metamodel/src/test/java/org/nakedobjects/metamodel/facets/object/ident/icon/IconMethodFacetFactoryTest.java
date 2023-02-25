package org.nakedobjects.metamodel.facets.object.ident.icon;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class IconMethodFacetFactoryTest extends AbstractFacetFactoryTest {

    private IconMethodFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new IconMethodFacetFactory();
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

    public void testIconNameMethodPickedUpOnClassAndMethodRemoved() {
        class Customer {
            public String iconName() {
                return null;
            }
        }
        final Method iconNameMethod = findMethod(Customer.class, "iconName");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(IconFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof IconFacetViaMethod);

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(iconNameMethod));
    }

}

// Copyright (c) Naked Objects Group Ltd.
