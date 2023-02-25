package org.nakedobjects.metamodel.facets.object.immutable;

import org.nakedobjects.applib.marker.AlwaysImmutable;
import org.nakedobjects.applib.marker.ImmutableOncePersisted;
import org.nakedobjects.applib.marker.ImmutableUntilPersisted;
import org.nakedobjects.applib.marker.NeverImmutable;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class ImmutableMarkerInterfaceFacetFactoryTest extends AbstractFacetFactoryTest {

    private ImmutableMarkerInterfacesFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new ImmutableMarkerInterfacesFacetFactory();
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

    public void testAlwaysImmutable() {
        class Customer implements AlwaysImmutable {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetMarkerInterface);
        final ImmutableFacetMarkerInterface immutableFacetMarkerInterface = (ImmutableFacetMarkerInterface) facet;
        assertEquals(org.nakedobjects.metamodel.facets.When.ALWAYS, immutableFacetMarkerInterface.value());

        assertNoMethodsRemoved();
    }

    public void testImmutableOncePersisted() {
        class Customer implements ImmutableOncePersisted {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetMarkerInterface);
        final ImmutableFacetMarkerInterface immutableFacetMarkerInterface = (ImmutableFacetMarkerInterface) facet;
        assertEquals(org.nakedobjects.metamodel.facets.When.ONCE_PERSISTED, immutableFacetMarkerInterface.value());

        assertNoMethodsRemoved();
    }

    public void testImmutableUntilPersisted() {
        class Customer implements ImmutableUntilPersisted {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetMarkerInterface);
        final ImmutableFacetMarkerInterface immutableFacetMarkerInterface = (ImmutableFacetMarkerInterface) facet;
        assertEquals(org.nakedobjects.metamodel.facets.When.UNTIL_PERSISTED, immutableFacetMarkerInterface.value());

        assertNoMethodsRemoved();
    }

    public void testNeverImmutable() {
        class Customer implements NeverImmutable {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetMarkerInterface);
        final ImmutableFacetMarkerInterface immutableFacetMarkerInterface = (ImmutableFacetMarkerInterface) facet;
        assertEquals(org.nakedobjects.metamodel.facets.When.NEVER, immutableFacetMarkerInterface.value());

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
