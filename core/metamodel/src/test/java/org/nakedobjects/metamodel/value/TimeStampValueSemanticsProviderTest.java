package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.value.TestClock;
import org.nakedobjects.applib.value.TimeStamp;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class TimeStampValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private TimeStampValueSemanticsProvider adapter;
    private Object timestamp;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.value.format.timestamp");
    		will(returnValue(null));
    	}});

        TestClock.initialize();
        timestamp = new TimeStamp(0);
        holder = new FacetHolderImpl();
        setValue(adapter = new TimeStampValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Override
    public void testParseEmptyString() {
        final Object parsed = adapter.parseTextEntry(null, "");
        assertNull(parsed);
    }

    @Test
    public void testTitle() {
        assertEquals("01/01/70 00:00:00 UTC", adapter.titleString(timestamp));
    }

    @Test
    public void testEncodesTimeStamp() {
        final String encodedString = new String(adapter.toEncodedString(timestamp));
        assertEquals("19700101T000000000", encodedString);
    }

    @Test
    public void testDecodesTimeStamp() {
        final String encodedString = "19700101T000000000";
        final Object restored = adapter.fromEncodedString(encodedString);
        assertEquals(((TimeStamp) timestamp).longValue(), ((TimeStamp) restored).longValue());
    }
}

// Copyright (c) Naked Objects Group Ltd.
