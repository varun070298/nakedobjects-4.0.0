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
public class IntValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private IntValueSemanticsProviderAbstract value;
    private Integer integer;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        integer = new Integer(32);
        allowMockAdapterToReturn(integer);
        
        mockery.checking(new Expectations(){{
        	allowing(mockConfiguration).getString("nakedobjects.value.format.int");
        	will(returnValue(null));
        }});

        holder = new FacetHolderImpl();
        setValue(value = new IntWrapperValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testInvalidParse() throws Exception {
        try {
            value.parseTextEntry(null, "one");
            fail();
        } catch (final TextEntryParseException expected) {}
    }

    @Test
    public void testTitleString() {
        assertEquals("32", value.displayTitleOf(integer));
    }

    @Test
    public void testParse() throws Exception {
        final Object newValue = value.parseTextEntry(null, "120");
        assertEquals(new Integer(120), newValue);
    }

    @Test
    public void testParseOddlyFormedEntry() throws Exception {
        final Object newValue = value.parseTextEntry(null, "1,20.0");
        assertEquals(new Integer(120), newValue);
    }
}
// Copyright (c) Naked Objects Group Ltd.
