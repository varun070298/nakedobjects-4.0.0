package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.nakedobjects.applib.value.Password;


/**
 * A user type that maps an SQL String to a NOF Password value.
 */
public class PasswordType extends ImmutableUserType {

    public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException,
            SQLException {
        final String password = rs.getString(names[0]);
        return rs.wasNull() ? null : new Password(password);
    }

    public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException,
            SQLException {
        if (value == null) {
            st.setNull(index, Hibernate.STRING.sqlType());
        } else {
            st.setString(index, ((Password) value).getPassword());
        }
    }

    public Class<Password> returnedClass() {
        return Password.class;
    }

    public int[] sqlTypes() {
        return new int[] { Hibernate.STRING.sqlType() };
    }
}
// Copyright (c) Naked Objects Group Ltd.
