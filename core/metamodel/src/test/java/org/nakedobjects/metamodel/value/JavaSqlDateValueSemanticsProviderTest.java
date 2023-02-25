package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.Calendar;
import java.util.TimeZone;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.value.TestClock;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class JavaSqlDateValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private JavaSqlDateValueSemanticsProvider adapter;
    private Date date;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.value.format.date");
    		will(returnValue(null));
    	}});

        TestClock.initialize();
        date = new Date(0);
        holder = new FacetHolderImpl();
        setValue(adapter = new JavaSqlDateValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext) {
            @Override
            protected String defaultFormat() {
                return "iso";
            }
        });
    }

    @Test
    public void testInvalidParse() throws Exception {
        try {
            adapter.parseTextEntry(null, "date");
            fail();
        } catch (final TextEntryParseException expected) {}
    }

    @Test
    public void testTitleOf() {
        assertEquals("1970-01-01", adapter.displayTitleOf(date));
    }

    @Test
    public void testParse() throws Exception {
        final Object newValue = adapter.parseTextEntry(null, "1/1/1980");

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        calendar.set(1980, 0, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertEquals(calendar.getTime(), newValue);
    }

}
// Copyright (c) Naked Objects Group Ltd.
