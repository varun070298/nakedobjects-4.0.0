package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.adapters.EncodingException;
import org.nakedobjects.applib.value.Date;
import org.nakedobjects.applib.value.TestClock;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class DateValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private DateValueSemanticsProvider adapter;
    private Object date;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.value.format.date");
    		will(returnValue(null));
    	}});

    	TestClock.initialize();
        setupSpecification(Date.class);
        date = new Date(2001, 2, 4);
        holder = new FacetHolderImpl();
        setValue(adapter = new DateValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testDateAsEncodedString() throws Exception {
        assertEquals("20010204", new String(adapter.toEncodedString(date)));
    }

    @Test
    public void testParseEntryOfDaysAfterDate() throws Exception {
        final Object parsed = adapter.parseTextEntry(date, "+7");
        assertEquals(new Date(2001, 2, 11), parsed);
    }

    @Test
    public void testParseEntryOfDaysAfterToToday() throws Exception {
        final Object parsed = adapter.parseTextEntry(null, "+5");
        assertEquals(new Date(2003, 8, 22), parsed);
    }

    @Test
    public void testParseEntryOfDaysBeforeDate() throws Exception {
        final Object parsed = adapter.parseTextEntry(date, "-7");
        assertEquals(new Date(2001, 1, 28), parsed);
    }

    @Test
    public void testParseEntryOfDaysBeforeToToday() throws Exception {
        final Object parsed = adapter.parseTextEntry(null, "-5");
        assertEquals(new Date(2003, 8, 12), parsed);
    }

    @Test
    public void testParseEntryOfKeywordToday() throws Exception {
        final Object parsed = adapter.parseTextEntry(date, "today");
        assertEquals(new Date(2003, 8, 17), parsed);
    }

    @Test
    public void testParseEntryOfWeeksAfterDate() throws Exception {
        final Object parsed = adapter.parseTextEntry(date, "+3w");
        assertEquals(new Date(2001, 2, 25), parsed);
    }

    @Test
    public void testParseEntryOfWeeksAfterToToday() throws Exception {
        final Object parsed = adapter.parseTextEntry(null, "+4w");
        assertEquals(new Date(2003, 9, 14), parsed);
    }

    @Test
    public void testParseEntryOfWeeksBeforeDate() throws Exception {
        final Object parsed = adapter.parseTextEntry(date, "-3w");
        assertEquals(new Date(2001, 1, 14), parsed);
    }

    @Test
    public void testParseEntryOfWeeksBeforeToToday() throws Exception {
        final Object parsed = adapter.parseTextEntry(null, "-4w");
        assertEquals(new Date(2003, 7, 20), parsed);
    }

    @Test
    public void testParseEntryOfMonthsAfterDate() throws Exception {
        final Object parsed = adapter.parseTextEntry(date, "+3m");
        assertEquals(new Date(2001, 5, 4), parsed);
    }

    @Test
    public void testParseEntryOfMonthsAfterToToday() throws Exception {
        final Object parsed = adapter.parseTextEntry(null, "+4m");
        assertEquals(new Date(2003, 12, 17), parsed);
    }

    @Test
    public void testParseEntryOfMonthsBeforeDate() throws Exception {
        final Object parsed = adapter.parseTextEntry(date, "-3m");
        assertEquals(new Date(2000, 11, 4), parsed);
    }

    @Test
    public void testParseEntryOfMonthsBeforeToToday() throws Exception {
        final Object parsed = adapter.parseTextEntry(null, "-4m");
        assertEquals(new Date(2003, 4, 17), parsed);
    }

    @Test
    public void testRestoreOfInvalidDatal() throws Exception {
        try {
            adapter.fromEncodedString("2003may12");
            fail();
        } catch (final EncodingException expected) {}
    }

}

// Copyright (c) Naked Objects Group Ltd.
