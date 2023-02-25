package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.nakedobjects.applib.value.Percentage;


/**
 * A user type that maps an SQL int to a NOF Percentage value.
 */
public class PercentageType extends ImmutableUserType {

    public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException,
            SQLException {
        final float percentage = rs.getFloat(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        return new Percentage(percentage);
    }

    public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException,
            SQLException {
        if (value == null) {
            st.setNull(index, Hibernate.FLOAT.sqlType());
        } else {
            st.setFloat(index, ((Percentage) value).floatValue());
        }
    }

    public Class<Percentage> returnedClass() {
        return Percentage.class;
    }

    public int[] sqlTypes() {
        return new int[] { Hibernate.FLOAT.sqlType() };
    }
}
// Copyright (c) Naked Objects Group Ltd.
