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
public class DoubleValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private Double doubleObj;

    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
    	
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.value.format.double");
    		will(returnValue(null));
    	}});

        holder = new FacetHolderImpl();
        setValue(new DoubleWrapperValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));

        doubleObj = new Double(32.5d);
        allowMockAdapterToReturn(doubleObj);
    }

    @Test
    public void testValue() {
        assertEquals("32.5", getValue().displayTitleOf(doubleObj));
    }

    @Test
    public void testInvalidParse() throws Exception {
        try {
            getValue().parseTextEntry(null, "one");
            fail();
        } catch (final TextEntryParseException expected) {}
    }

    @Test
    public void testTitleOf() {
        assertEquals("35,000,000", getValue().displayTitleOf(Double.valueOf(35000000.0)));
    }

    @Test
    public void testParse() throws Exception {
        final Object newValue = getValue().parseTextEntry(null, "120.56");
        assertEquals(120.56, ((Double) newValue).doubleValue(), 0.0);
    }

    @Test
    public void testParse2() throws Exception {
        final Object newValue = getValue().parseTextEntry(null, "1,20.0");
        assertEquals(120, ((Double) newValue).doubleValue(), 0.0);
    }
}
// Copyright (c) Naked Objects Group Ltd.
