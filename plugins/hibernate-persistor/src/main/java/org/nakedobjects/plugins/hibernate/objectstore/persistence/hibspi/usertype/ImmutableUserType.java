package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;


/**
 * Base class for immutable Hibernate types.
 */
public abstract class ImmutableUserType extends ImmutableType implements UserType {

    public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
        return cached;
    }

    public Serializable disassemble(final Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
        return original;
    }

}
// Copyright (c) Naked Objects Group Ltd.
