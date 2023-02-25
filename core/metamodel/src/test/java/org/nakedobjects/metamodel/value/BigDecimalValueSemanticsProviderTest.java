package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class BigDecimalValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private BigDecimalValueSemanticsProvider value;
    private BigDecimal bigDecimal;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.value.format.decimal");
    		will(returnValue(null));
    	}});
    	
        bigDecimal = new BigDecimal("34132.199");
        allowMockAdapterToReturn(bigDecimal);
        holder = new FacetHolderImpl();

        setValue(value = new BigDecimalValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testParseValidString() throws Exception {
        final Object newValue = value.parseTextEntry(null, "2142342334");
        assertEquals(new BigDecimal(2142342334L), newValue);
    }

    @Test
    public void testParseInvalidString() throws Exception {
        try {
            value.parseTextEntry(null, "214xxx2342334");
            fail();
        } catch (final TextEntryParseException expected) {}
    }

    @Test
    public void testTitleOf() {
        assertEquals("34,132.199", value.displayTitleOf(bigDecimal));
    }

    @Test
    public void testEncode() {
        assertEquals("34132.199", value.toEncodedString(bigDecimal));
    }

    @Test
    public void testDecode() throws Exception {
        final Object newValue = value.fromEncodedString("4322.89991");
        assertEquals(new BigDecimal("4322.89991"), newValue);
    }


}

// Copyright (c) Naked Objects Group Ltd.
