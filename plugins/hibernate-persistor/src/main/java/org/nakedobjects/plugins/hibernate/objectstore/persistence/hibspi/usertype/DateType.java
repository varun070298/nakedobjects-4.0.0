package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.nakedobjects.applib.value.Date;


/**
 * A user type that maps an SQL DATE to a NOF Date value.
 */
public class DateType extends ImmutableUserType {

    public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException,
            SQLException {
        final java.util.Date date = rs.getDate(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        return new Date(date);
    }

    public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException,
            SQLException {
        if (value == null) {
            st.setNull(index, Hibernate.DATE.sqlType());
        } else {
            final Date nofDate = (Date) value;
            final java.sql.Date sqlDate = new java.sql.Date(nofDate.dateValue().getTime());
            st.setDate(index, sqlDate);
        }
    }

    public Class<Date> returnedClass() {
        return Date.class;
    }

    public int[] sqlTypes() {
        return new int[] { Hibernate.DATE.sqlType() };
    }
}
// Copyright (c) Naked Objects Group Ltd.
