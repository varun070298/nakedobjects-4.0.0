package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class ByteValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private ByteValueSemanticsProviderAbstract value;

    private Byte byteObj;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        byteObj = new Byte((byte) 102);
        allowMockAdapterToReturn(byteObj);
        holder = new FacetHolderImpl();
        
        mockery.checking(new Expectations(){{
        	allowing(mockConfiguration).getString("nakedobjects.value.format.byte");
        	will(returnValue(null));
        }});

        setValue(value = new ByteWrapperValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testParseValidString() throws Exception {
        final Object parsed = value.parseTextEntry(null, "21");
        assertEquals(new Byte((byte) 21), parsed);
    }

    @Test
    public void testParseInvalidString() throws Exception {
        try {
            value.parseTextEntry(mockAdapter, "xs21z4xxx23");
            fail();
        } catch (final TextEntryParseException expected) {}
    }

    @Test
    public void testTitleOf() throws Exception {
        assertEquals("102", value.displayTitleOf(byteObj));
    }

    @Test
    public void testEncode() throws Exception {
        assertEquals("102", value.toEncodedString(byteObj));
    }

    @Test
    public void testDecode() throws Exception {
        final Object parsed = value.fromEncodedString("-91");
        assertEquals(new Byte((byte) -91), parsed);
    }

}

// Copyright (c) Naked Objects Group Ltd.
