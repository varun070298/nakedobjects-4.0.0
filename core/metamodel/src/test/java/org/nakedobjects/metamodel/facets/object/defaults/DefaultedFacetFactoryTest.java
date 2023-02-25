package org.nakedobjects.metamodel.facets.object.defaults;

import org.nakedobjects.applib.adapters.DefaultsProvider;
import org.nakedobjects.applib.annotation.Defaulted;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class DefaultedFacetFactoryTest extends AbstractFacetFactoryTest {

    private DefaultedAnnotationFacetFactory facetFactory;
    private PropertiesConfiguration propertiesConfiguration;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new DefaultedAnnotationFacetFactory();
        propertiesConfiguration = new PropertiesConfiguration();
        facetFactory.setNakedObjectConfiguration(propertiesConfiguration);
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
        assertFalse(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testFacetPickedUp() {
        facetFactory.process(MyDefaultedUsingDefaultsProvider.class, methodRemover, facetHolder);

        final DefaultedFacet facet = facetHolder.getFacet(DefaultedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DefaultedFacetAbstract);
    }

    public void testFacetFacetHolderStored() {

        facetFactory.process(MyDefaultedUsingDefaultsProvider.class, methodRemover, facetHolder);

        final DefaultedFacetAbstract valueFacet = (DefaultedFacetAbstract) facetHolder.getFacet(DefaultedFacet.class);
        assertEquals(facetHolder, valueFacet.getFacetHolder());
    }

    public void testNoMethodsRemoved() {

        facetFactory.process(MyDefaultedUsingDefaultsProvider.class, methodRemover, facetHolder);

        assertNoMethodsRemoved();
    }

    abstract static class DefaultsProviderNoop<T> implements DefaultsProvider<T> {

        public abstract T getDefaultValue();

    }

    @Defaulted(defaultsProviderName = "org.nakedobjects.metamodel.facets.object.defaults.DefaultedFacetFactoryTest$MyDefaultedUsingDefaultsProvider")
    public static class MyDefaultedUsingDefaultsProvider extends DefaultsProviderNoop<MyDefaultedUsingDefaultsProvider> {

        /**
         * Required since is a DefaultsProvider.
         */
        public MyDefaultedUsingDefaultsProvider() {}

        @Override
        public MyDefaultedUsingDefaultsProvider getDefaultValue() {
            return new MyDefaultedUsingDefaultsProvider();
        }
    }

    public void testDefaultedUsingDefaultsProviderName() {
        facetFactory.process(MyDefaultedUsingDefaultsProvider.class, methodRemover, facetHolder);
        final DefaultedFacetAbstract facet = (DefaultedFacetAbstract) facetHolder.getFacet(DefaultedFacet.class);
        assertEquals(MyDefaultedUsingDefaultsProvider.class, facet.getDefaultsProviderClass());
    }

    @Defaulted(defaultsProviderClass = MyDefaultedUsingDefaultsProviderClass.class)
    public static class MyDefaultedUsingDefaultsProviderClass extends DefaultsProviderNoop<MyDefaultedUsingDefaultsProviderClass> {

        /**
         * Required since is a DefaultsProvider.
         */
        public MyDefaultedUsingDefaultsProviderClass() {}

        @Override
        public MyDefaultedUsingDefaultsProviderClass getDefaultValue() {
            return new MyDefaultedUsingDefaultsProviderClass();
        }
    }

    public void testDefaultedUsingDefaultsProviderClass() {
        facetFactory.process(MyDefaultedUsingDefaultsProviderClass.class, methodRemover, facetHolder);
        final DefaultedFacetAbstract facet = (DefaultedFacetAbstract) facetHolder.getFacet(DefaultedFacet.class);
        assertEquals(MyDefaultedUsingDefaultsProviderClass.class, facet.getDefaultsProviderClass());
    }

    public void testDefaultedMustBeADefaultsProvider() {
    // no test, because compiler prevents us from nominating a class that doesn't
    // implement DefaultsProvider
    }

    @Defaulted(defaultsProviderClass = MyDefaultedWithoutNoArgConstructor.class)
    public static class MyDefaultedWithoutNoArgConstructor extends DefaultsProviderNoop<MyDefaultedWithoutNoArgConstructor> {

        // no no-arg constructor

        public MyDefaultedWithoutNoArgConstructor(final int value) {}

        @Override
        public MyDefaultedWithoutNoArgConstructor getDefaultValue() {
            return new MyDefaultedWithoutNoArgConstructor(0);
        }

    }

    public void testDefaultedMustHaveANoArgConstructor() {
        facetFactory.process(MyDefaultedWithoutNoArgConstructor.class, methodRemover, facetHolder);
        final DefaultedFacetAbstract facet = (DefaultedFacetAbstract) facetHolder.getFacet(DefaultedFacet.class);
        assertNull(facet);
    }

    @Defaulted(defaultsProviderClass = MyDefaultedWithoutPublicNoArgConstructor.class)
    public static class MyDefaultedWithoutPublicNoArgConstructor extends
            DefaultsProviderNoop<MyDefaultedWithoutPublicNoArgConstructor> {

        // no public no-arg constructor
        MyDefaultedWithoutPublicNoArgConstructor() {}

        public MyDefaultedWithoutPublicNoArgConstructor(final int value) {}

        @Override
        public MyDefaultedWithoutPublicNoArgConstructor getDefaultValue() {
            return new MyDefaultedWithoutPublicNoArgConstructor();
        }

    }

    public void testDefaultedHaveAPublicNoArgConstructor() {
        facetFactory.process(MyDefaultedWithoutPublicNoArgConstructor.class, methodRemover, facetHolder);
        final DefaultedFacetAbstract facet = (DefaultedFacetAbstract) facetHolder.getFacet(DefaultedFacet.class);
        assertNull(facet);
    }

    @Defaulted()
    public static class MyDefaultedWithDefaultsProviderSpecifiedUsingConfiguration extends
            DefaultsProviderNoop<MyDefaultedWithDefaultsProviderSpecifiedUsingConfiguration> {

        /**
         * Required since is a DefaultsProvider.
         */
        public MyDefaultedWithDefaultsProviderSpecifiedUsingConfiguration() {}

        @Override
        public MyDefaultedWithDefaultsProviderSpecifiedUsingConfiguration getDefaultValue() {
            return new MyDefaultedWithDefaultsProviderSpecifiedUsingConfiguration();
        }
    }

    public void testDefaultedProviderNameCanBePickedUpFromConfiguration() {
        final String className = "org.nakedobjects.metamodel.facets.object.defaults.DefaultedFacetFactoryTest$MyDefaultedWithDefaultsProviderSpecifiedUsingConfiguration";
        propertiesConfiguration.add(DefaultsProviderUtil.DEFAULTS_PROVIDER_NAME_KEY_PREFIX + canonical(className)
                + DefaultsProviderUtil.DEFAULTS_PROVIDER_NAME_KEY_SUFFIX, className);
        facetFactory.process(MyDefaultedWithDefaultsProviderSpecifiedUsingConfiguration.class, methodRemover, facetHolder);
        final DefaultedFacetAbstract facet = (DefaultedFacetAbstract) facetHolder.getFacet(DefaultedFacet.class);
        assertNotNull(facet);
        assertEquals(MyDefaultedWithDefaultsProviderSpecifiedUsingConfiguration.class, facet.getDefaultsProviderClass());
    }

    public static class NonAnnotatedDefaultedDefaultsProviderSpecifiedUsingConfiguration extends
            DefaultsProviderNoop<NonAnnotatedDefaultedDefaultsProviderSpecifiedUsingConfiguration> {

        /**
         * Required since is a DefaultsProvider.
         */
        public NonAnnotatedDefaultedDefaultsProviderSpecifiedUsingConfiguration() {}

        @Override
        public NonAnnotatedDefaultedDefaultsProviderSpecifiedUsingConfiguration getDefaultValue() {
            return new NonAnnotatedDefaultedDefaultsProviderSpecifiedUsingConfiguration();
        }
    }

    public void testNonAnnotatedDefaultedCanBePickedUpFromConfiguration() {
        final String className = "org.nakedobjects.metamodel.facets.object.defaults.DefaultedFacetFactoryTest$NonAnnotatedDefaultedDefaultsProviderSpecifiedUsingConfiguration";
        propertiesConfiguration.add(DefaultsProviderUtil.DEFAULTS_PROVIDER_NAME_KEY_PREFIX + canonical(className)
                + DefaultsProviderUtil.DEFAULTS_PROVIDER_NAME_KEY_SUFFIX, className);
        facetFactory.process(NonAnnotatedDefaultedDefaultsProviderSpecifiedUsingConfiguration.class, methodRemover, facetHolder);
        final DefaultedFacetAbstract facet = (DefaultedFacetAbstract) facetHolder.getFacet(DefaultedFacet.class);
        assertNotNull(facet);
        assertEquals(NonAnnotatedDefaultedDefaultsProviderSpecifiedUsingConfiguration.class, facet.getDefaultsProviderClass());
    }

    private String canonical(final String className) {
        return className.replace('$', '.');
    }

}

// Copyright (c) Naked Objects Group Ltd.
