package org.nakedobjects.metamodel.facets.object.notpersistable;

import org.nakedobjects.applib.marker.NonPersistable;
import org.nakedobjects.applib.marker.ProgramPersistable;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class NotPersistableMarkerInterfaceFacetFactoryTest extends AbstractFacetFactoryTest {

    private NotPersistableMarkerInterfacesFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new NotPersistableMarkerInterfacesFacetFactory();
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

    public void testProgramPersistableMeansNotPersistableByUser() {
        class Customer implements ProgramPersistable {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(NotPersistableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NotPersistableFacetMarkerInterface);
        final NotPersistableFacetMarkerInterface notPersistableFacetMarkerInterface = (NotPersistableFacetMarkerInterface) facet;
        final org.nakedobjects.metamodel.facets.object.notpersistable.InitiatedBy value = notPersistableFacetMarkerInterface.value();
        assertEquals(org.nakedobjects.metamodel.facets.object.notpersistable.InitiatedBy.USER, value);

        assertNoMethodsRemoved();
    }

    public void testNotPersistable() {
        class Customer implements NonPersistable {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(NotPersistableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NotPersistableFacetMarkerInterface);
        final NotPersistableFacetMarkerInterface notPersistableFacetMarkerInterface = (NotPersistableFacetMarkerInterface) facet;
        final org.nakedobjects.metamodel.facets.object.notpersistable.InitiatedBy value = notPersistableFacetMarkerInterface.value();
        assertEquals(org.nakedobjects.metamodel.facets.object.notpersistable.InitiatedBy.USER_OR_PROGRAM, value);

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
