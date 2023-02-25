package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.nakedobjects.applib.value.DateTime;


/**
 * A user type that maps an SQL DATE to a NOF DateTime value.
 */
public class DateTimeType extends ImmutableUserType {

    public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException,
            SQLException {
        final Timestamp ts = rs.getTimestamp(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        return new DateTime(ts);
    }

    public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException,
            SQLException {
        if (value == null) {
            st.setNull(index, Hibernate.TIMESTAMP.sqlType());
        } else {
            final DateTime dateTime = (DateTime) value;
            final java.sql.Timestamp sqlDate = new java.sql.Timestamp(dateTime.dateValue().getTime());
            st.setTimestamp(index, sqlDate);
        }
    }

    public Class<DateTime> returnedClass() {
        return DateTime.class;
    }

    public int[] sqlTypes() {
        return new int[] { Hibernate.TIMESTAMP.sqlType() };
    }
}
// Copyright (c) Naked Objects Group Ltd.
