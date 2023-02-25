package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.listener;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.engine.EntityKey;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.event.LoadEvent;
import org.hibernate.event.LoadEventListener;
import org.hibernate.event.def.DefaultLoadEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.pretty.MessageHelper;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator.HibernateOid;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionHydrator;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;


/**
 * Implementation of {@link LoadEventListener} that mostly has the same behaviour as
 * {@link DefaultLoadEventListener}, but that which uses Naked Objects' ghost objects
 * for lazy loading rather than Hibernate's proxies.
 */
public class NakedLoadEventListener extends DefaultLoadEventListener {
    
    private static final long serialVersionUID = 1L;
    private final static Logger LOG = Logger.getLogger(NakedLoadEventListener.class);

    

    /**
     * Just adds some debugging, is all.
     */
    @Override
    public void onLoad(final LoadEvent event, final LoadType loadType) throws HibernateException {

        if (LOG.isDebugEnabled()) {
            LOG.debug(
                "LoadEvent - pre onLoad type=" + event.getEntityClassName() + ", " +
                "id=" + event.getEntityId() + ", " +
                "result=" + (event.getResult() == null ? "null" : event.getResult().getClass().getName()) + ", " +
                "instancetoload=" + event.getInstanceToLoad()
            );
        }

        super.onLoad(event, loadType);
    }

    

    /**
     * Similar to the default implementation, but uses Naked Objects' 'ghost' objects
     * rather than Hibernate's proxies.
     */
    @Override
    protected Object proxyOrLoad(
            final LoadEvent event,
            final EntityPersister persister,
            final EntityKey keyToLoad,
            final LoadEventListener.LoadType options) throws HibernateException {

        if ( LOG.isDebugEnabled() ) {
            LOG.debug(
                    "loading entity: " + 
                    MessageHelper.infoString( persister, event.getEntityId(), event.getSession().getFactory() )
                );
        }

        // as per superclass' implementation:
        // if this class has no proxies, then do a shortcut
        if (!persister.hasProxy()) {
            return load(event, persister, keyToLoad, options);
        }
        
        final PersistenceContext persistenceContext = event.getSession().getPersistenceContext();

        // using naked objects ghost objects, so should never be an existing proxy
        Assert.assertNull(persistenceContext.getProxy(keyToLoad));
        
        if (options.isAllowProxyCreation()) { // original behaviour
            
            // replaced behaviour here; 
            final Class<?> cls = persister.getMappedClass(EntityMode.POJO);
            if (classIsInstantiable(cls)) {
                // Naked Objects lazy loading involves creating a 'ghost' so 
                // we must be able to instantiate the class
                return loadUnresolvedObject(event, persister, keyToLoad, options, persistenceContext);
            }
        }
        
        // return a newly loaded object
        return load(event, persister, keyToLoad, options);
    }

    
    //////////////////////////////////////////////////////////////////
    // Helpers
    //////////////////////////////////////////////////////////////////

    private boolean classIsInstantiable(final Class<?> cls) {
        int clsModifiers = cls.getModifiers();
        return !Modifier.isAbstract(clsModifiers) && 
               !Modifier.isInterface(cls.getModifiers());
    }

    private Object loadUnresolvedObject(
            final LoadEvent event,
            final EntityPersister persister,
            final EntityKey keyToLoad,
            final LoadEventListener.LoadType options,
            final PersistenceContext persistenceContext) {
        
        Serializable entityId = event.getEntityId();
        final HibernateOid oid = HibernateOid.createPersistent(event.getEntityClassName(), entityId);
        final NakedObjectSpecification spec = getSpecificationLoader().loadSpecification(event.getEntityClassName());
        
        NakedObject nakedObject = getAdapterManager().getAdapterFor(oid);
        if (nakedObject == null) {
            nakedObject = getHydrator().recreateAdapter(oid, spec);
            Assert.assertFalse(persistenceContext.isEntryFor(nakedObject.getObject()));
            return nakedObject.getObject();
        } else {
            return load(event, persister, keyToLoad, options);
        }
    }






    //////////////////////////////////////////////////////////////////
    // Dependencies (from singletons)
    //////////////////////////////////////////////////////////////////

    protected SpecificationLoader getSpecificationLoader() {
        return NakedObjectsContext.getSpecificationLoader();
    }

    protected PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private PersistenceSessionHydrator getHydrator() {
        return getPersistenceSession();
    }

    private AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }


}
// Copyright (c) Naked Objects Group Ltd.
