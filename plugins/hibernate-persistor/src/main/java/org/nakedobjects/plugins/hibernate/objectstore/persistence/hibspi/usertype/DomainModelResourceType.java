package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator.HibernateOid;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.services.ServiceUtil;


/**
 * A user type that maps an SQL string to a NOF DomainModelResource.
 * 
 * TODO remove this class as it no longer has a counterpart in the NOF (was DomainModelResource)
 */
public class DomainModelResourceType extends ImmutableUserType {

    public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException,
            SQLException {
        final String id = rs.getString(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        final Oid oid = HibernateOid.createPersistent(id, id, id);
        final NakedObject adapter = getAdapterManager().getAdapterFor(oid);
        if (adapter == null) {
            throw new HibernateException("Unknown DomainModelResource: id=" + id);
        }
        return adapter.getObject();
    }

    public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException,
            SQLException {
        if (value == null) {
            st.setNull(index, Hibernate.STRING.sqlType());
        } else {
            st.setString(index, ServiceUtil.id(value));
        }
    }

    public Class<Object> returnedClass() {
        return Object.class;
    }

    public int[] sqlTypes() {
        return new int[] { Hibernate.STRING.sqlType() };
    }

    
    ////////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    ////////////////////////////////////////////////////////////////////
    
    private PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }
    

}
// Copyright (c) Naked Objects Group Ltd.
