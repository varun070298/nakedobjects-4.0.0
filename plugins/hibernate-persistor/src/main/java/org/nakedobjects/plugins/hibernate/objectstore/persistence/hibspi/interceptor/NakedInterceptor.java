package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.interceptor;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor.PropertyHelper;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator.HibernateOid;
import org.nakedobjects.plugins.hibernate.objectstore.util.HibernateUtil;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistorUtil;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;


/**
 * A Hibernate Interceptor to ensure process Naked Objects correctly.
 * 
 * <p>
 * It does several things including:
 * <ul>
 * <li>Objects cached within NOF are used instead of loading from the database</li>
 * <li>Objects are created via NOF, so container and services are injected</li>
 * <li><tt>naked_modified_by</tt> and <tt>naked_modified_on</tt> properties are updated when an object is saved or updated</li>
 * </ul>
 */
public class NakedInterceptor extends EmptyInterceptor {
    
    private static final long serialVersionUID = 1L;
    private static Logger LOG = Logger.getLogger(NakedInterceptor.class);


    //////////////////////////////////////////////////////////////////
    // isTransient
    //////////////////////////////////////////////////////////////////

    @Override
    public Boolean isTransient(final Object entity) {
        final NakedObject nakedObject = getAdapterManager().getAdapterFor(entity);
        return nakedObject.getOid().isTransient();
    }


    //////////////////////////////////////////////////////////////////
    // Instantiate
    //////////////////////////////////////////////////////////////////

    @Override
    public Object instantiate(final String entityName, final EntityMode entityMode, final Serializable id)
            throws CallbackException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("instantiate entityName=" + entityName + ", id=" + id + ", mode=" + entityMode);
        }
        
        final HibernateOid oid = HibernateOid.createPersistent(entityName, id);
        final NakedObjectSpecification spec = getSpecificationLoader().loadSpecification(entityName);
        final NakedObject adapter = getPersistenceSession().recreateAdapter(oid, spec);
        final ResolveState nextState = adapter.getResolveState().isResolved() ? ResolveState.UPDATING : ResolveState.RESOLVING;

        // TODO better sort resolve state transitions
        if (!adapter.getResolveState().isResolving()) {
            PersistorUtil.start(adapter, nextState);
        }

        final Object object = adapter.getObject();
        // need to set the id in case the object has an id property 
        // (if not id is held in the oid, and that's taken care of above)
        try {
            ClassMetadata classMetadata = HibernateUtil.getSessionFactory().getClassMetadata(entityName);
            classMetadata.setIdentifier(object, id, entityMode);
        } catch (final HibernateException e) {
            throw new CallbackException("Error getting identifier property for class " + entityName, e);
        }
        return object;
    }


    //////////////////////////////////////////////////////////////////
    // getEntity
    //////////////////////////////////////////////////////////////////
    
    @Override
    public Object getEntity(final String entityName, final Serializable id) throws CallbackException {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("getEntity entityName=" + entityName + ", id=" + id);
        }
        
        final HibernateOid oid = HibernateOid.createPersistent(entityName, id);
        final NakedObject adapter = getAdapterManager().getAdapterFor(oid);
        if (adapter == null || adapter.getResolveState().isGhost() || adapter.getResolveState().isPartlyResolved()
                || adapter.getResolveState().isResolving()) {
            // change for lazy loading objects - this prevents hibernate
            // picking up the GHOST object as an already loaded entity by only returning
            // the object if it is resolved
            return null;
        }
        return adapter.getObject();
    }

    
    //////////////////////////////////////////////////////////////////
    // onFlushDirty
    //////////////////////////////////////////////////////////////////

    /**
     * Updates the user name and timestamp, if possible.
     */
    @Override
    public boolean onFlushDirty(
            final Object entity,
            final Serializable id,
            final Object[] currentState,
            final Object[] previousState,
            final String[] propertyNames,
            final Type[] types) {
        return setModified(currentState, propertyNames);
    }

    
    //////////////////////////////////////////////////////////////////
    // onSave
    //////////////////////////////////////////////////////////////////

    /**
     * Updates the user name and timestamp, if possible.
     */
    @Override
    public boolean onSave(
            final Object entity,
            final Serializable id,
            final Object[] state,
            final String[] propertyNames,
            final Type[] types) {
        return setModified(state, propertyNames);
    }

    
    //////////////////////////////////////////////////////////////////
    // Helpers
    //////////////////////////////////////////////////////////////////

    /**
     * Updates the currentState array with the user name and timestamp,
     * if available.
     * 
     * @return <tt>true</tt> if either user name and/or timestamp was updated.
     */
    private boolean setModified(final Object[] currentState, final String[] propertyNames) {
        boolean updatedModifiedBy = false;
        boolean updatedModifiedOn = false;
        for (int i = 0; i < propertyNames.length; i++) {
            if (!updatedModifiedBy && 
                 propertyNames[i].equals(PropertyHelper.MODIFIED_BY)) {
                currentState[i] = getSession().getUserName();
                updatedModifiedBy = true;
            }
            if (!updatedModifiedOn && 
                 propertyNames[i].equals(PropertyHelper.MODIFIED_ON)) {
                currentState[i] = new Date();
                updatedModifiedOn = true;
            }
        }
        return updatedModifiedBy || updatedModifiedOn;
    }

    
    //////////////////////////////////////////////////////////////////
    // Dependencies (from singleton)
    //////////////////////////////////////////////////////////////////

    private SpecificationLoader getSpecificationLoader() {
        return NakedObjectsContext.getSpecificationLoader();
    }

    private PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private AdapterManager getAdapterManager() {
        return NakedObjectsContext.getPersistenceSession().getAdapterManager();
    }

    private AuthenticationSession getSession() {
        return NakedObjectsContext.getAuthenticationSession();
    }


}
// Copyright (c) Naked Objects Group Ltd.
