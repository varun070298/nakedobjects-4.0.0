package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.value.ImageValueSemanticsProviderAbstract;


public abstract class AbstractImageType extends ImmutableUserType {
    // protected abstract AbstractImageAdapter getImageAdapter(Object value);
    protected abstract ImageValueSemanticsProviderAbstract getImageAdapter();

    public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException,
            SQLException {
        final Blob blob = rs.getBlob(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        // final AbstractImageAdapter imageAdapter = getImageAdapter(null);
        final ImageValueSemanticsProviderAbstract imageAdapter = getImageAdapter();
        return imageAdapter.restoreFromByteArray(blob.getBytes(1, (int) blob.length()));
    }

    public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException,
            SQLException {
        if (value == null) {
            st.setNull(index, Types.BLOB);
        } else {
            // AbstractImageAdapter imageAdapter = getImageAdapter(value);
            // st.setBlob(index, Hibernate.createBlob(imageAdapter.getAsByteArray()));
            final ImageValueSemanticsProviderAbstract imageAdapter = getImageAdapter();
            final NakedObject nakedValue = lookupNakedObject(value);
            st.setBlob(index, Hibernate.createBlob(imageAdapter.getAsByteArray(nakedValue)));
        }
    }

    /**
     * TODO: need to lookup the NO that wraps the provided value
     */
    private NakedObject lookupNakedObject(final Object value) {
        return null;
    }

    public abstract Class<?> returnedClass();

    public int[] sqlTypes() {
        return new int[] { Types.BLOB };
    }
}
