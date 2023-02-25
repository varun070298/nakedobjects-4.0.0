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
public class ShortValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private ShortValueSemanticsProviderAbstract value;
    private Short short1;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        mockery.checking(new Expectations(){{
        	allowing(mockConfiguration).getString("nakedobjects.value.format.short");
        	will(returnValue(null));
        }});
    	
    	short1 = new Short((short) 32);
        allowMockAdapterToReturn(short1);
        
        holder = new FacetHolderImpl();
        

        setValue(value = new ShortWrapperValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testInvalidParse() throws Exception {
        try {
            value.parseTextEntry(null, "one");
            fail();
        } catch (final TextEntryParseException expected) {}
    }

    @Test
    public void testTitleOfForPositiveValue() {
        assertEquals("32", value.displayTitleOf(short1));
    }

    @Test
    public void testTitleOfForLargestNegativeValue() {
        assertEquals("-128", value.displayTitleOf(new Short((short) -128)));
    }

    @Test
    public void testParse() throws Exception {
        final Object newValue = value.parseTextEntry(null, "120");
        assertEquals(new Short((short) 120), newValue);
    }

    @Test
    public void testParseOfOddEntry() throws Exception {
        final Object newValue = value.parseTextEntry(null, "1,20.0");
        assertEquals(new Short((short) 120), newValue);
    }

}
// Copyright (c) Naked Objects Group Ltd.
