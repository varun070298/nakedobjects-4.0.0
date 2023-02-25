package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.listener;

import org.apache.log4j.Logger;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.tuple.StandardProperty;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.AggregatedOid;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator.HibernateOid;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.ToPersistObjectSet;


public class NakedInsertPostEventListener extends NakedEventListenerAbstract
        implements PostInsertEventListener {

    private static final long serialVersionUID = 1L;
    private final static Logger LOG = Logger.getLogger(NakedInsertPostEventListener.class);

    

    public void onPostInsert(final PostInsertEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("PostInsertEvent " + logString(event));
        }
        
        Object entity = event.getEntity();
        final NakedObject adapter = getAdapterFor(entity);
        final HibernateOid oid = (HibernateOid) adapter.getOid();
        
        // make sure the oid is loaded with the hibernate id
        // make sure the adapter is set to resolved
        if (!oid.isTransient()) {
            throw new NakedObjectException(
                    "Not transient: oid=" + oid + "," +
                    " resolve state=" + adapter.getResolveState() + " for " + entity);
        }

        // remap our adapters for each of the collection objects
        // that hibernate injects.
        replaceCollections(adapter, event);

        // in case id is a property of the object, and hence not set using OidAccessor
        oid.setHibernateId(event.getId());
        oid.makePersistent();

        // REVIEW: is this the place to make the object persistent?
        // is it not already done in the PersistAlgorithm?
        if (getPersistenceSession() instanceof ToPersistObjectSet) {
            // should be true (it is the ProxyPersistor that doesn't implement this...)
            ToPersistObjectSet persistedObjectAdder = (ToPersistObjectSet) getPersistenceSession();
            persistedObjectAdder.remapAsPersistent(adapter);
        }
        
        clearDirtyFor(adapter);
    }

    
    
    /////////////////////////////////////////////////////////
    // Helpers
    /////////////////////////////////////////////////////////

    /**
     * Updates the {@link NakedObject adapter} that wraps all collections with the
     * collection possibly injected by Hibernate.
     */
    private void replaceCollections(final NakedObject parent, final PostInsertEvent event) {
        final NakedObjectAssociation[] nofAssociations = parent.getSpecification().getAssociations();
        final StandardProperty[] hibProperties = event.getPersister().getEntityMetamodel().getProperties();
        Object[] hibCollections = event.getState();

        for (int i = 0; i < nofAssociations.length; i++) {
            if (!nofAssociations[i].isOneToManyAssociation()) {
                continue;
            }
            final String nofCollectionId = nofAssociations[i].getId();
            replaceCollection(parent, hibProperties, hibCollections, nofCollectionId);
        }
    }

    /**
     * Updates the {@link NakedObject adapter} for the specified collection with the
     * collection possibly injected by Hibernate.
     */
    private void replaceCollection(
            final NakedObject parent,
            final StandardProperty[] hibProperties,
            final Object[] hibPropertyObjects,
            final String nofCollectionId) {
        
        Oid parentOid = parent.getOid();
        
        for (int j = 0; j < hibProperties.length; j++) {
            String hibPropertyName = hibProperties[j].getName();
            if (!hibPropertyName.equals(nofCollectionId)) {
                continue;
            }
            
            Object hibCollectionObject = hibPropertyObjects[j];
            final Oid collectionOid = new AggregatedOid(parentOid, nofCollectionId);
            final NakedObject collectionAdapter = getAdapterFor(collectionOid);
            collectionAdapter.replacePojo(hibCollectionObject);
            break;
        }
    }
    



    /////////////////////////////////////////////////////////
    // Helpers (for logging)
    /////////////////////////////////////////////////////////

    private String logString(final PostInsertEvent event) {
        return event.getEntity().getClass() + " " + event.getId();
    }

}
// Copyright (c) Naked Objects Group Ltd.
