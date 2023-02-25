package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import org.nakedobjects.plugins.hibernate.objectstore.metamodel.version.LongVersion;


/**
 * Accesses NakedObjects user.
 */
public class UserAccessor extends OptimisticLockAccessor {
    public static LongVersionAccessor USER_ACCESSOR = new LongVersionAccessor() {
        public Object get(final LongVersion version) {
            return version.getUser();
        }

        public Class<String> getReturnType() {
            return String.class;
        }

        public void set(final LongVersion version, final Object value) {
            version.setUser((String) value);
        }
    };

    @Override
    protected LongVersionAccessor getLongVersionAccessor() {
        return USER_ACCESSOR;
    }

}
// Copyright (c) Naked Objects Group Ltd.
