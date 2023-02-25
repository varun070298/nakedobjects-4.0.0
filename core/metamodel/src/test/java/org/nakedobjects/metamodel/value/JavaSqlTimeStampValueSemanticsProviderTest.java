package org.nakedobjects.metamodel.value;

import java.sql.Timestamp;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.value.TestClock;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JMock.class)
public class JavaSqlTimeStampValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private JavaSqlTimeStampValueSemanticsProvider adapter;
    private Object timestamp;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.value.format.timestamp");
    		will(returnValue(null));
    	}});

        TestClock.initialize();
        timestamp = new Timestamp(0);
        holder = new FacetHolderImpl();
        setValue(adapter = new JavaSqlTimeStampValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
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
        assertEquals(((Timestamp) timestamp).getTime(), ((Timestamp) restored).getTime());
    }
}

// Copyright (c) Naked Objects Group Ltd.
