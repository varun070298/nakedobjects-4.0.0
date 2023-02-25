package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;

import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class StringValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private StringValueSemanticsProvider value;

    private String string;

    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        string = "text entry";
        holder = new FacetHolderImpl();
        setValue(value = new StringValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testTitleOf() {
        assertEquals("text entry", value.displayTitleOf(string));
    }

    @Test
    public void testParse() throws Exception {
        final Object parsed = value.parseTextEntry(null, "tRUe");
        assertEquals("tRUe", parsed.toString());
    }

    @Test
    public void testEncodeNormalString() throws Exception {
    	allowMockAdapterToReturn("/slash");
        assertEquals("//slash", getEncodeableFacet().toEncodedString(mockAdapter));
    }

    @Test
    public void testEncodeNULLString() throws Exception {
    	allowMockAdapterToReturn("NULL");
        assertEquals("/NULL", getEncodeableFacet().toEncodedString(mockAdapter));
    }

    @Test
    public void testRestore() throws Exception {
        final Object parsed = value.fromEncodedString("//slash");
        assertEquals("/slash", parsed.toString());
    }

    @Test
    public void testRestoreNULLString() throws Exception {
        final Object parsed = value.fromEncodedString("/NULL");
        assertEquals("NULL", parsed.toString());
    }
}
// Copyright (c) Naked Objects Group Ltd.
