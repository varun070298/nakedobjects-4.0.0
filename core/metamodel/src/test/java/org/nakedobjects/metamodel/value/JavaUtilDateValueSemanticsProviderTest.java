package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
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
public class JavaUtilDateValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private java.util.Date date;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.value.format.datetime");
    		will(returnValue(null));
    	}});

        TestClock.initialize();
        date = new java.util.Date(0);

        holder = new FacetHolderImpl();
        setValue(new JavaUtilDateValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext) {
            @Override
            protected String defaultFormat() {
                return "iso";
            }
        });
    }

    @Test
    public void testInvalidParse() throws Exception {
        try {
            getValue().parseTextEntry(null, "invalid entry");
            fail();
        } catch (final TextEntryParseException expected) {}
    }

    /**
     * Something rather bizarre here, that the epoch formats as 01:00 rather than 00:00. It's obviously
     * because of some sort of timezone issue, but I don't know where that dependency is coming from.
     */
    @Test
    public void testTitleOf() {
        final String EXPECTED = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date(0));
        assertEquals(EXPECTED, getValue().displayTitleOf(date));
    }

    @Test
    public void testParse() throws Exception {
        final Object newValue = getValue().parseTextEntry(null, "1980-01-01 10:40");

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        calendar.set(1980, 0, 1, 10, 40, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertEquals(calendar.getTime(), newValue);
    }

}
// Copyright (c) Naked Objects Group Ltd.
