package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.util.Calendar;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.value.TestClock;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class JavaSqlTimeValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private Time twoOClock;
    private NakedObject twoOClockNO;
    private JavaSqlTimeValueSemanticsProvider value;
    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        mockery.checking(new Expectations(){{
        	allowing(mockConfiguration).getString("nakedobjects.value.format.time");
        	will(returnValue(null));
        }});

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TestClock.timeZone);

        c.set(Calendar.MILLISECOND, 0);

        c.set(Calendar.YEAR, 0);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 0);

        c.set(Calendar.HOUR_OF_DAY, 14);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        twoOClock = new Time(c.getTimeInMillis());
        twoOClockNO = createAdapter(twoOClock);

        holder = new FacetHolderImpl();
        setValue(value = new JavaSqlTimeValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testNewTime() {
        final String asEncodedString = value.toEncodedString(twoOClock);
        assertEquals("140000000", asEncodedString);
    }

    @Test
    public void testAdd() {
        final Object newValue = value.add(twoOClock, 0, 0, 0, 1, 15);
        assertEquals("15:15:00", newValue.toString());
    }

    @Test
    public void testAdd2() {
        final Object newValue = value.add(twoOClock, 0, 0, 0, 0, 0);
        assertEquals("14:00:00", newValue.toString());
    }
}

// Copyright (c) Naked Objects Group Ltd.
