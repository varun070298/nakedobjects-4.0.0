package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.io.Serializable;

import org.hibernate.engine.SessionImplementor;
import org.hibernate.usertype.CompositeUserType;


/**
 * Base class for immutable Hibernate composite types.
 */
public abstract class ImmutableCompositeType extends ImmutableType implements CompositeUserType {

    public Object assemble(final Serializable cached, final SessionImplementor session, final Object owner) {
        return cached;
    }

    public Serializable disassemble(final Object value, final SessionImplementor session) {
        return (Serializable) value;
    }

    public Object replace(final Object original, final Object target, final SessionImplementor session, final Object owner) {
        return original;
    }

}
// Copyright (c) Naked Objects Group Ltd.
