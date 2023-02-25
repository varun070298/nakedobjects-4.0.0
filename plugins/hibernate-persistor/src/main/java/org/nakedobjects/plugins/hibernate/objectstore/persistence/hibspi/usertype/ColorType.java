package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.nakedobjects.applib.value.Color;


/**
 * A user type that maps an SQL int to a NOF Color value.
 */
public class ColorType extends ImmutableUserType {

    public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException,
            SQLException {
        final int color = rs.getInt(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        return new Color(color);
    }

    public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException,
            SQLException {
        if (value == null) {
            st.setNull(index, Hibernate.INTEGER.sqlType());
        } else {
            st.setInt(index, ((Color) value).intValue());
        }
    }

    public Class<Color> returnedClass() {
        return Color.class;
    }

    public int[] sqlTypes() {
        return new int[] { Hibernate.INTEGER.sqlType() };
    }
}
// Copyright (c) Naked Objects Group Ltd.
