package org.nakedobjects.metamodel.facets.object.parseable;

import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.applib.annotation.Parseable;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class ParseableFacetFactoryTest extends AbstractFacetFactoryTest {

    private ParseableFacetFactory facetFactory;
    private PropertiesConfiguration propertiesConfiguration;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new ParseableFacetFactory();
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
        assertFalse(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testFacetPickedUp() {

        facetFactory.process(MyParseableUsingParserName.class, methodRemover, facetHolder);

        final ParseableFacet facet = facetHolder.getFacet(ParseableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ParseableFacetAbstract);
    }

    public void testFacetFacetHolderStored() {

        facetFactory.process(MyParseableUsingParserName.class, methodRemover, facetHolder);

        final ParseableFacetAbstract parseableFacet = (ParseableFacetAbstract) facetHolder.getFacet(ParseableFacet.class);
        assertEquals(facetHolder, parseableFacet.getFacetHolder());
    }

    public void testNoMethodsRemoved() {

        facetFactory.process(MyParseableUsingParserName.class, methodRemover, facetHolder);

        assertNoMethodsRemoved();
    }

    @Parseable(parserName = "org.nakedobjects.metamodel.facets.object.parseable.ParseableFacetFactoryTest$MyParseableUsingParserName")
    public static class MyParseableUsingParserName extends ParserNoop<MyParseableUsingParserName> {

        /**
         * Required since is a Parser.
         */
        public MyParseableUsingParserName() {}
    }

    public void testParseableUsingParserName() {

        facetFactory.process(MyParseableUsingParserName.class, methodRemover, facetHolder);

        final ParseableFacetAbstract parseableFacet = (ParseableFacetAbstract) facetHolder.getFacet(ParseableFacet.class);
        assertEquals(MyParseableUsingParserName.class, parseableFacet.getParserClass());
    }

    public static class ParserNoop<T> implements Parser<T> {
        public T parseTextEntry(final T context, final String entry) {
            return null;
        }

        public int typicalLength() {
            return 0;
        }

        public String displayTitleOf(final T object) {
            return null;
        }
        
        public String displayTitleOf(T object, String usingMask) {
            return null;
        }

        public String parseableTitleOf(final T existing) {
            return null;
        }
    }

    @Parseable(parserClass = MyParseableUsingParserClass.class)
    public static class MyParseableUsingParserClass extends ParserNoop<MyParseableUsingParserClass> {
        /**
         * Required since is a Parser.
         */
        public MyParseableUsingParserClass() {}
    }

    public void testParseableUsingParserClass() {

        facetFactory.process(MyParseableUsingParserClass.class, methodRemover, facetHolder);

        final ParseableFacetAbstract parseableFacet = (ParseableFacetAbstract) facetHolder.getFacet(ParseableFacet.class);
        assertEquals(MyParseableUsingParserClass.class, parseableFacet.getParserClass());
    }

    public void testParseableMustBeAParser() {
    // no test, because compiler prevents us from nominating a class that doesn't
    // implement Parser
    }

    @Parseable(parserClass = MyParseableWithoutNoArgConstructor.class)
    public static class MyParseableWithoutNoArgConstructor extends ParserNoop<MyParseableWithoutNoArgConstructor> {

        // no no-arg constructor

        public MyParseableWithoutNoArgConstructor(final int value) {}
    }

    public void testParseableHaveANoArgConstructor() {
        facetFactory.process(MyParseableWithoutNoArgConstructor.class, methodRemover, facetHolder);

        final ParseableFacetAbstract parseableFacet = (ParseableFacetAbstract) facetHolder.getFacet(ParseableFacet.class);
        assertNull(parseableFacet);
    }

    @Parseable(parserClass = MyParseableWithoutPublicNoArgConstructor.class)
    public static class MyParseableWithoutPublicNoArgConstructor extends ParserNoop<MyParseableWithoutPublicNoArgConstructor> {

        // no public no-arg constructor
        MyParseableWithoutPublicNoArgConstructor() {
            this(0);
        }

        public MyParseableWithoutPublicNoArgConstructor(final int value) {}
    }

    public void testParseableHaveAPublicNoArgConstructor() {
        facetFactory.process(MyParseableWithoutPublicNoArgConstructor.class, methodRemover, facetHolder);

        final ParseableFacetAbstract parseableFacet = (ParseableFacetAbstract) facetHolder.getFacet(ParseableFacet.class);
        assertNull(parseableFacet);
    }

    @Parseable()
    public static class MyParseableWithParserSpecifiedUsingConfiguration extends
            ParserNoop<MyParseableWithParserSpecifiedUsingConfiguration> {

        /**
         * Required since is a Parser.
         */
        public MyParseableWithParserSpecifiedUsingConfiguration() {}
    }

    public void testParserNameCanBePickedUpFromConfiguration() {
        final String className = "org.nakedobjects.metamodel.facets.object.parseable.ParseableFacetFactoryTest$MyParseableWithParserSpecifiedUsingConfiguration";
        propertiesConfiguration.add(ParserUtil.PARSER_NAME_KEY_PREFIX + canonical(className) + ParserUtil.PARSER_NAME_KEY_SUFFIX,
                className);
        facetFactory.process(MyParseableWithParserSpecifiedUsingConfiguration.class, methodRemover, facetHolder);
        final ParseableFacetAbstract facet = (ParseableFacetAbstract) facetHolder.getFacet(ParseableFacet.class);
        assertNotNull(facet);
        assertEquals(MyParseableWithParserSpecifiedUsingConfiguration.class, facet.getParserClass());
    }

    public static class NonAnnotatedParseableParserSpecifiedUsingConfiguration extends
            ParserNoop<NonAnnotatedParseableParserSpecifiedUsingConfiguration> {

        /**
         * Required since is a Parser.
         */
        public NonAnnotatedParseableParserSpecifiedUsingConfiguration() {}
    }

    public void testNonAnnotatedParseableCanPickUpParserFromConfiguration() {
        final String className = "org.nakedobjects.metamodel.facets.object.parseable.ParseableFacetFactoryTest$NonAnnotatedParseableParserSpecifiedUsingConfiguration";
        propertiesConfiguration.add(ParserUtil.PARSER_NAME_KEY_PREFIX + canonical(className) + ParserUtil.PARSER_NAME_KEY_SUFFIX,
                className);
        facetFactory.process(NonAnnotatedParseableParserSpecifiedUsingConfiguration.class, methodRemover, facetHolder);
        final ParseableFacetAbstract facet = (ParseableFacetAbstract) facetHolder.getFacet(ParseableFacet.class);
        assertNotNull(facet);
        assertEquals(NonAnnotatedParseableParserSpecifiedUsingConfiguration.class, facet.getParserClass());
    }

    private String canonical(final String className) {
        return className.replace('$', '.');
    }

}

// Copyright (c) Naked Objects Group Ltd.
