package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.easymock.MockControl;
import org.hibernate.Hibernate;
import org.nakedobjects.applib.value.Date;


public class DateTypeTest extends TypesTestCase {

    public void testNullSafeGetNotNull() throws Exception {
        final GregorianCalendar cal = new GregorianCalendar(2001, 02, 07);
        cal.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        final java.sql.Date returnedDate = new java.sql.Date(cal.getTimeInMillis());

        final MockControl<ResultSet> control = MockControl.createControl(ResultSet.class);
        final ResultSet rs = (ResultSet) control.getMock();
        control.expectAndReturn(rs.getDate(names[0]), returnedDate);
        control.expectAndReturn(rs.wasNull(), false);
        control.replay(); // finished recording

        final DateType type = new DateType();
        final Date returned = (Date) type.nullSafeGet(rs, names, null);
        assertEquals("day", 7, returned.getDay());
        assertEquals("month", 3, returned.getMonth());
        assertEquals("year", 2001, returned.getYear());

        control.verify();
    }

    public void testNullSafeGetIsNull() throws Exception {
        final MockControl<ResultSet> control = MockControl.createControl(ResultSet.class);
        final ResultSet rs = (ResultSet) control.getMock();
        control.expectAndReturn(rs.getDate(names[0]), null);
        control.expectAndReturn(rs.wasNull(), true);
        control.replay(); // finished recording

        final DateType type = new DateType();
        assertNull(type.nullSafeGet(rs, names, null));

        control.verify();
    }

    public void testNullSafeSetNotNull() throws Exception {
        final GregorianCalendar cal = new GregorianCalendar(2001, 02, 07);
        cal.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        final java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
        final Date date = new Date(sqlDate);

        final MockControl<PreparedStatement> control = MockControl.createControl(PreparedStatement.class);
        final PreparedStatement ps = (PreparedStatement) control.getMock();
        ps.setDate(1, sqlDate);
        control.replay(); // finished recording

        final DateType type = new DateType();
        type.nullSafeSet(ps, date, 1);

        control.verify();
    }

    public void testNullSafeSetIsNull() throws Exception {
        final MockControl<PreparedStatement> control = MockControl.createControl(PreparedStatement.class);
        final PreparedStatement ps = (PreparedStatement) control.getMock();
        ps.setNull(1, Hibernate.DATE.sqlType());
        control.replay(); // finished recording

        final DateType type = new DateType();
        type.nullSafeSet(ps, null, 1);

        control.verify();
    }

    public void testBasics() {
        final DateType type = new DateType();
        super.basicTest(type);
        assertEquals("returned class", org.nakedobjects.applib.value.Date.class, type.returnedClass());
    }

}
// Copyright (c) Naked Objects Group Ltd.
