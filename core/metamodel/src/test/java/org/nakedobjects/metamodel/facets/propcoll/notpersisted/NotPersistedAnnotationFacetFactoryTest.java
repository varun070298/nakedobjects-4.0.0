package org.nakedobjects.metamodel.facets.propcoll.notpersisted;

import java.lang.reflect.Method;
import java.util.Collection;

import org.nakedobjects.applib.annotation.NotPersisted;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class NotPersistedAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private NotPersistedAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new NotPersistedAnnotationFacetFactory();
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
        assertTrue(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testNotPersistedAnnotationPickedUpOnProperty() {
        class Customer {
            @NotPersisted()
            public String getFirstName() {
                return null;
            }
        }
        final Method method = findMethod(Customer.class, "getFirstName");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(NotPersistedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NotPersistedFacetAnnotation);

        assertNoMethodsRemoved();
    }

    public void testNotPersistedAnnotationPickedUpOnCollection() {
        class Customer {
            @NotPersisted()
            public Collection getOrders() {
                return null;
            }
        }
        final Method method = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(NotPersistedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NotPersistedFacetAnnotation);

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
