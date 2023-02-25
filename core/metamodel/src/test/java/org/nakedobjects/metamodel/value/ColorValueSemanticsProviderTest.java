package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.value.Color;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class ColorValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private ColorValueSemanticsProvider value;
    private Color color;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.value.format.decimal");
    		will(returnValue(null));
    	}});
    	
        color = new Color(0x3366ff);
        allowMockAdapterToReturn(color);
        holder = new FacetHolderImpl();

        setValue(value = new ColorValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testParseValidString() throws Exception {
        final Object newValue = value.parseTextEntry(null, "#3366fF");
        assertEquals(color, newValue);
    }

    @Test
    public void testParseInvalidString() throws Exception {
        try {
            value.parseTextEntry(null, "red");
            fail();
        } catch (final TextEntryParseException expected) {}
    }

    @Test
    public void testTitleOf() {
        assertEquals("#3366FF", value.displayTitleOf(color));
    }

    @Test
    public void testTitleOfBlack() {
        assertEquals("Black", value.displayTitleOf(new Color(0)));
    }

    @Test
    public void testTitleOfWhite() {
        assertEquals("White", value.displayTitleOf(new Color(0xffffff)));
    }
    
    @Test
    public void testEncode() {
        assertEquals("3366ff", value.toEncodedString(color));
    }

    @Test
    public void testDecode() throws Exception {
        final Object newValue = value.fromEncodedString("3366ff");
        assertEquals(color, newValue);
    }


}

// Copyright (c) Naked Objects Group Ltd.
