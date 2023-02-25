package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.nakedobjects.applib.value.TimeStamp;


/**
 * A user type that maps an SQL LONG to a NOF TimeStamp value.
 */
public class TimeStampType extends ImmutableUserType {

    public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException,
            SQLException {
        final long time = rs.getLong(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        return new TimeStamp(time);
    }

    public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException,
            SQLException {
        if (value == null) {
            st.setNull(index, Hibernate.LONG.sqlType());
        } else {
            st.setLong(index, ((TimeStamp) value).longValue());
        }
    }

    public Class<TimeStamp> returnedClass() {
        return TimeStamp.class;
    }

    public int[] sqlTypes() {
        return new int[] { Hibernate.LONG.sqlType() };
    }
}
// Copyright (c) Naked Objects Group Ltd.
