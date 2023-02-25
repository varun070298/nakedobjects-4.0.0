package org.nakedobjects.plugins.hibernate.objectstore.persistence.container;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.services.container.DomainObjectContainerDefault;
import org.nakedobjects.plugins.hibernate.objectstore.util.HibernateUtil;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;


public class HibernateContainer extends DomainObjectContainerDefault {
	
	private static final Logger LOG = Logger.getLogger(HibernateContainer.class);

    @Override
    public void resolve(final Object parent, final Object field) {
        // normal resolve for null field
        if (field == null) {
            super.resolve(parent);
            return;
        }

        final NakedObject adapter = getRuntimeContext().getAdapterFor(parent);
        final ResolveState resolveState = adapter.getResolveState();
        if (!resolveState.isResolvable(ResolveState.RESOLVING)) {
            return;
        }

        // This code is here to handle hibernate lazy loading. The field reference may be not null because
        // it is a proxy - this will check if it is a uninitialized proxy.

        // TODO would really like to push this into objectPersistor, but current setup
        // requires a NakedObject field, which we can't get!

        // TODO loadEvent will create adapter for proxy, but collection is a different matter.
        // if adapter exists for collection it will be set to resolved, but can't create new adapter
        // as we can't get the field
        if (Hibernate.isInitialized(field)) {
            return;
        }

        // This method may not be running within the scope of a transaction, so
        // make sure one is active
        getTransactionManager().startTransaction();
        try {
            final Session session = HibernateUtil.getCurrentSession();
            if (field instanceof org.hibernate.collection.PersistentCollection) {
                session.lock(parent, org.hibernate.LockMode.NONE);
                Hibernate.initialize(field);
            } else {
                final LazyInitializer lazy = ((HibernateProxy) field).getHibernateLazyInitializer();
                lazy.setSession((SessionImplementor) session);
                lazy.initialize();
            }
        } catch (final RuntimeException e) {
        	getTransactionManager().abortTransaction();
            throw e;
        }
        getTransactionManager().endTransaction();
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                    "Container resolved field of type " + field.getClass() + 
                    " for parent " + parent + ", state=" + resolveState);
        }
    }

    
    ///////////////////////////////////////////////////////
    // Dependencies (from context)
    ///////////////////////////////////////////////////////

    protected static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    protected static NakedObjectTransactionManager getTransactionManager() {
        return getPersistenceSession().getTransactionManager();
    }
    

}
// Copyright (c) Naked Objects Group Ltd.
