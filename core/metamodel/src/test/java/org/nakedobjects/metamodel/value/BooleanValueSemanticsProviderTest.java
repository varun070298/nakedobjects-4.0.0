package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class BooleanValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private BooleanValueSemanticsProviderAbstract value;

    private Object booleanObj;
    private FacetHolder facetHolder;

    @Before
    public void setUpObjects() throws Exception {
        booleanObj = new Boolean(true);
        facetHolder = new FacetHolderImpl();
        setValue(value = new BooleanWrapperValueSemanticsProvider(facetHolder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testParseFalseString() throws Exception {
        final Object parsed = value.parseTextEntry(null, "faLSe");
        assertEquals(new Boolean(false), parsed);
    }

    @Test
    public void testParseTrueString() throws Exception {
        final Object parsed = value.parseTextEntry(null, "TRue");
        assertEquals(new Boolean(true), parsed);
    }

    @Test
    public void testParseInvalidString() throws Exception {
        try {
            value.parseTextEntry(null, "yes");
            fail();
        } catch (final TextEntryParseException expected) {}
    }

    @Test
    public void testTitle() throws Exception {
        assertEquals("True", value.displayTitleOf(booleanObj));
    }

    @Test
    public void testTitleWhenNotSet() throws Exception {
        assertEquals("", value.titleString(null));
    }

    @Test
    public void testEncode() throws Exception {
        assertEquals("T", value.toEncodedString(booleanObj));
    }

    @Test
    public void testDecode() throws Exception {
        final Object parsed = value.fromEncodedString("T");
        assertEquals(new Boolean(true), parsed);
    }

    @Test
    public void testIsSet() {
        allowMockAdapterToReturn(new Boolean(true));
        assertEquals(true, value.isSet(mockAdapter));
    }

    @Test
    public void testIsNotSet() {
        allowMockAdapterToReturn(new Boolean(false));
        assertEquals(false, value.isSet(mockAdapter));
    }
}

// Copyright (c) Naked Objects Group Ltd.
