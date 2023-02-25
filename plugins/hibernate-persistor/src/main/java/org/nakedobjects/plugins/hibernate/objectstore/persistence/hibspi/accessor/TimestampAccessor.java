package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import java.util.Date;

import org.nakedobjects.plugins.hibernate.objectstore.metamodel.version.LongVersion;


/**
 * Accesses NakedObjects update timestamp, possibly for use in optimistic locking.
 */
public class TimestampAccessor extends OptimisticLockAccessor {
    public static LongVersionAccessor TIMESTAMP_ACCESSOR = new LongVersionAccessor() {
        public Object get(final LongVersion version) {
            return version.getTime();
        }

        public Class<Date> getReturnType() {
            return Date.class;
        }

        public void set(final LongVersion version, final Object value) {
            version.setTime((Date) value);
        }
    };

    @Override
    protected LongVersionAccessor getLongVersionAccessor() {
        return TIMESTAMP_ACCESSOR;
    }
}
// Copyright (c) Naked Objects Group Ltd.
