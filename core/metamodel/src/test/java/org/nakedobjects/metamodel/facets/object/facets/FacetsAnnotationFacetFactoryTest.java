package org.nakedobjects.metamodel.facets.object.facets;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Facets;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class FacetsAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private FacetsAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new FacetsAnnotationFacetFactory();
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

    public static class CustomerFacetFactory implements FacetFactory {
        public NakedObjectFeatureType[] getFeatureTypes() {
            return null;
        }

        public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
            return false;
        }

        public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
            return false;
        }

        public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
            return false;
        }
    }

    public static class CustomerFacetFactory2 implements FacetFactory {
        public NakedObjectFeatureType[] getFeatureTypes() {
            return null;
        }

        public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
            return false;
        }

        public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
            return false;
        }

        public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
            return false;
        }
    }

    public void testFacetsFactoryNames() {
        @Facets(facetFactoryNames = {
                "org.nakedobjects.metamodel.facets.object.facets.FacetsAnnotationFacetFactoryTest$CustomerFacetFactory",
                "org.nakedobjects.metamodel.facets.object.facets.FacetsAnnotationFacetFactoryTest$CustomerNotAFacetFactory" })
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(FacetsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof FacetsFacetAnnotation);
        final FacetsFacetAnnotation facetsFacet = (FacetsFacetAnnotation) facet;
        final Class<? extends FacetFactory>[] facetFactories = facetsFacet.facetFactories();
        assertEquals(1, facetFactories.length);
        assertEquals(CustomerFacetFactory.class, facetFactories[0]);

        assertNoMethodsRemoved();
    }

    public void testFacetsFactoryClass() {
        @Facets(facetFactoryClasses = {
                org.nakedobjects.metamodel.facets.object.facets.FacetsAnnotationFacetFactoryTest.CustomerFacetFactory.class,
                org.nakedobjects.metamodel.facets.object.facets.FacetsAnnotationFacetFactoryTest.CustomerNotAFacetFactory.class })
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(FacetsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof FacetsFacetAnnotation);
        final FacetsFacetAnnotation facetsFacet = (FacetsFacetAnnotation) facet;
        final Class<? extends FacetFactory>[] facetFactories = facetsFacet.facetFactories();
        assertEquals(1, facetFactories.length);
        assertEquals(CustomerFacetFactory.class, facetFactories[0]);
    }

    public static class CustomerNotAFacetFactory {}

    public void testFacetsFactoryNameAndClass() {
        @Facets(facetFactoryNames = { "org.nakedobjects.metamodel.facets.object.facets.FacetsAnnotationFacetFactoryTest$CustomerFacetFactory" }, facetFactoryClasses = { org.nakedobjects.metamodel.facets.object.facets.FacetsAnnotationFacetFactoryTest.CustomerFacetFactory2.class })
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(FacetsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof FacetsFacetAnnotation);
        final FacetsFacetAnnotation facetsFacet = (FacetsFacetAnnotation) facet;
        final Class<? extends FacetFactory>[] facetFactories = facetsFacet.facetFactories();
        assertEquals(2, facetFactories.length);
        assertEquals(CustomerFacetFactory.class, facetFactories[0]);
        assertEquals(CustomerFacetFactory2.class, facetFactories[1]);
    }

    public void testFacetsFactoryNoop() {
        @Facets
        class Customer {}

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(FacetsFacet.class);
        assertNull(facet);
    }

}

// Copyright (c) Naked Objects Group Ltd.
