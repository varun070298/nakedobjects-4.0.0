package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import org.nakedobjects.plugins.hibernate.objectstore.metamodel.version.LongVersion;


/**
 * Accesses NakedObjects version for use in optimistic locking.
 */
public class VersionAccessor extends OptimisticLockAccessor {
    public static LongVersionAccessor VERSION_ACCESSOR = new LongVersionAccessor() {
        public Object get(final LongVersion version) {
            return version.getVersionNumber();
        }

        public Class<Long> getReturnType() {
            return Long.class;
        }

        public void set(final LongVersion version, final Object value) {
            version.setVersionNumber((Long) value);
        }
    };

    @Override
    protected LongVersionAccessor getLongVersionAccessor() {
        return VERSION_ACCESSOR;
    }
}
// Copyright (c) Naked Objects Group Ltd.
