package org.nakedobjects.metamodel.facets.object.notpersistable;

import org.nakedobjects.applib.annotation.NotPersistable;
import org.nakedobjects.applib.annotation.NotPersistable.By;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class NotPersistableAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private NotPersistableAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new NotPersistableAnnotationFacetFactory();
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

    public void testNotPersistableAnnotationPickedUpOnClassAndDefaultsToUserOrProgram() {
        @NotPersistable
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(NotPersistableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NotPersistableFacetAnnotation);
        final NotPersistableFacetAnnotation notPersistableFacetAnnotation = (NotPersistableFacetAnnotation) facet;
        final org.nakedobjects.metamodel.facets.object.notpersistable.InitiatedBy value = notPersistableFacetAnnotation.value();
        assertEquals(org.nakedobjects.metamodel.facets.object.notpersistable.InitiatedBy.USER_OR_PROGRAM, value);

        assertNoMethodsRemoved();
    }

    public void testNotPersistableAnnotationUserOrProgramPickedUpOn() {
        @NotPersistable(By.USER_OR_PROGRAM)
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(NotPersistableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NotPersistableFacetAnnotation);
        final NotPersistableFacetAnnotation notPersistableFacetAnnotation = (NotPersistableFacetAnnotation) facet;
        final org.nakedobjects.metamodel.facets.object.notpersistable.InitiatedBy value = notPersistableFacetAnnotation.value();
        assertEquals(org.nakedobjects.metamodel.facets.object.notpersistable.InitiatedBy.USER_OR_PROGRAM, value);

        assertNoMethodsRemoved();
    }

    public void testNotPersistableAnnotationUserPickedUpOn() {
        @NotPersistable(By.USER)
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(NotPersistableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NotPersistableFacetAnnotation);
        final NotPersistableFacetAnnotation notPersistableFacetAnnotation = (NotPersistableFacetAnnotation) facet;
        final org.nakedobjects.metamodel.facets.object.notpersistable.InitiatedBy value = notPersistableFacetAnnotation.value();
        assertEquals(org.nakedobjects.metamodel.facets.object.notpersistable.InitiatedBy.USER, value);

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
