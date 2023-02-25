package org.nakedobjects.metamodel.facets.object.immutable;

import org.nakedobjects.applib.annotation.Immutable;
import org.nakedobjects.applib.annotation.When;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class ImmutableAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private ImmutableAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new ImmutableAnnotationFacetFactory();
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
        assertTrue(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testImmutableAnnotationPickedUpOnClassAndDefaultsToAlways() {
        @Immutable
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetAnnotation);
        final ImmutableFacetAnnotation immutableFacetAnnotation = (ImmutableFacetAnnotation) facet;
        assertEquals(org.nakedobjects.metamodel.facets.When.ALWAYS, immutableFacetAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testImmutableAnnotationAlwaysPickedUpOnClass() {
        @Immutable(When.ALWAYS)
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetAnnotation);
        final ImmutableFacetAnnotation immutableFacetAnnotation = (ImmutableFacetAnnotation) facet;
        assertEquals(org.nakedobjects.metamodel.facets.When.ALWAYS, immutableFacetAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testImmutableAnnotationNeverPickedUpOnClass() {
        @Immutable(When.NEVER)
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetAnnotation);
        final ImmutableFacetAnnotation immutableFacetAnnotation = (ImmutableFacetAnnotation) facet;
        assertEquals(org.nakedobjects.metamodel.facets.When.NEVER, immutableFacetAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testImmutableAnnotationOncePersistedPickedUpOnClass() {
        @Immutable(When.ONCE_PERSISTED)
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetAnnotation);
        final ImmutableFacetAnnotation immutableFacetAnnotation = (ImmutableFacetAnnotation) facet;
        assertEquals(org.nakedobjects.metamodel.facets.When.ONCE_PERSISTED, immutableFacetAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testImmutableAnnotationUntilPersistedPickedUpOnClass() {
        @Immutable(When.UNTIL_PERSISTED)
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetAnnotation);
        final ImmutableFacetAnnotation immutableFacetAnnotation = (ImmutableFacetAnnotation) facet;
        assertEquals(org.nakedobjects.metamodel.facets.When.UNTIL_PERSISTED, immutableFacetAnnotation.value());

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
