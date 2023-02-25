package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class BigIntValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private BigInteger bigInt;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        bigInt = new BigInteger("132199");
        allowMockAdapterToReturn(bigInt);

        mockery.checking(new Expectations(){{
        	allowing(mockConfiguration).getString("nakedobjects.value.format.int");
        	will(returnValue(null));
        }});


        holder = new FacetHolderImpl();
        setValue(new BigIntegerValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testParseValidString() throws Exception {
        final Object newValue = getValue().parseTextEntry(null, "2142342334");
        assertEquals(new BigInteger("2142342334"), newValue);
    }

    @Test
    public void testParseInvalidString() throws Exception {
        try {
            getValue().parseTextEntry(null, "214xxx2342334");
            fail();
        } catch (final TextEntryParseException expected) {}
    }

    @Test
    public void testTitle() throws Exception {
        assertEquals("132,199", getValue().displayTitleOf(bigInt));
    }

    @Test
    public void testEncode() throws Exception {
        assertEquals("132199", getValue().toEncodedString(bigInt));
    }

    @Test
    public void testDecode() throws Exception {
        final Object newValue = getValue().fromEncodedString("432289991");
        assertEquals(new BigInteger("432289991"), newValue);
    }

}

// Copyright (c) Naked Objects Group Ltd.
