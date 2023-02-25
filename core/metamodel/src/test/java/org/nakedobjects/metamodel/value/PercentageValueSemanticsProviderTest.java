package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.value.Percentage;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class PercentageValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {
    PercentageValueSemanticsProvider adapter;
    private Object percentage;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.value.format.percentage");
    		will(returnValue(null));
    	}});
    	
        setupSpecification(Percentage.class);
        
        percentage = new Percentage(0.105f);
        allowMockAdapterToReturn(percentage);
        
        holder = new FacetHolderImpl();
        
        setValue(adapter = new PercentageValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testAsEncodedString() {
        final String encoded = getEncodeableFacet().toEncodedString(mockAdapter);
        assertEquals("0.105", encoded);
    }

    @Test
    public void testParseTextEntryWithNumber() {
        final Object parsed = adapter.parseTextEntry(percentage, "21%");
        assertEquals(new Percentage(0.21f), parsed);
    }

    @Test
    public void testParseTextEntryWithNumberAndDecimalPoint() {
        final Object parsed = adapter.parseTextEntry(percentage, "21.4%");
        assertEquals(new Percentage(0.214f), parsed);
    }

    @Test
    public void testParseTextEntryWithBlank() {
        final Object parsed = adapter.parseTextEntry(percentage, "");
        assertEquals(null, parsed);
    }

    @Test
    public void testRestoreFromEncodedString() {
        final Object restored = adapter.fromEncodedString("0.2134");
        assertEquals(new Percentage(0.2134f), restored);
    }

    @Test
    public void testTitleOf() {
        assertEquals("10%", adapter.displayTitleOf(percentage));
    }

    @Test
    public void testFloatValue() {
        assertEquals(0.105f, adapter.floatValue(mockAdapter), 0.0f);
    }

}

// Copyright (c) Naked Objects Group Ltd.
