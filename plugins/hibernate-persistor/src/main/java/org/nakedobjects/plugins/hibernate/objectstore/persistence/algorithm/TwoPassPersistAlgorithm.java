package org.nakedobjects.plugins.hibernate.objectstore.persistence.algorithm;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.util.CallbackUtils;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithmAbstract;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.ToPersistObjectSet;


/**
 * Implements persistence-by-reachability, explicitly walks the graph and
 * persisting first 1:1 associations and then 1:m associations.
 * 
 * <p>
 * This is an alternative to the {@link SimplePersistAlgorithm} that simply relies
 * on Hibernate to do its thang using its <tt>cascade</tt> setting.
 */
public class TwoPassPersistAlgorithm extends PersistAlgorithmAbstract {
    
    private static final Logger LOG = Logger.getLogger(TwoPassPersistAlgorithm.class);



    //////////////////////////////////////////////////////////////////
    // name
    //////////////////////////////////////////////////////////////////

    public String name() {
        return "Two pass,  bottom up persistence walker";
    }


    //////////////////////////////////////////////////////////////////
    // makePersistent
    //////////////////////////////////////////////////////////////////

    /**
     * @param persistedObjectAdder - will actually be implemented by {@link PersistenceSession}
     */
    public void makePersistent(final NakedObject object, final ToPersistObjectSet toPersistObjectSet) {
        if (object.getSpecification().isCollection()) {
            makeCollectionPersistent(object, toPersistObjectSet);
        } else {
            makeObjectPersistent(object, toPersistObjectSet);
        }
    }

    private void makeObjectPersistent(final NakedObject adapter, final ToPersistObjectSet toPersistObjectSet) {
        if (alreadyPersistedOrNotPersistableOrServiceOrStandalone(adapter)) {
            return;
        }

        if (adapter.isPersistent()) {
            return;
        }

        LOG.info("persist " + adapter);

        // this done elsewhere, I think... 
        // (... see similar commenting out in SimpleStrategy)
        // NakedObjects.getObjectLoader().madePersistent(object);
        
        CallbackUtils.callCallback(adapter, PersistingCallbackFacet.class);

        // set state here so we don't loop round again to save the same object
        adapter.changeState(ResolveState.RESOLVED);
        
        // TODO resolved state needs looking at
        adapter.changeState(ResolveState.UPDATING);
        toPersistObjectSet.addPersistedObject(adapter);

        final NakedObjectAssociation[] fields = adapter.getSpecification().getAssociations();
        for (int i = 0; i < fields.length; i++) {
            final NakedObjectAssociation association = fields[i];
            if (association.isDerived()) {
                continue;
            }
            
            if (association.isOneToManyAssociation()) {
                // skip in first pass
            } else {
                processOneToOneAssociation(adapter, toPersistObjectSet, association);
            }
        }

        for (int i = 0; i < fields.length; i++) {
            final NakedObjectAssociation field = fields[i];
            if (field.isDerived()) {
                continue;
            }
            
            if (field.isOneToManyAssociation()) {
                processOneToManyAssociation(adapter, toPersistObjectSet, field);
            } else {
                // 1:1 association, skip in second pass
            }
        }

        CallbackUtils.callCallback(adapter, PersistedCallbackFacet.class);
    }

    private void processOneToOneAssociation(
            final NakedObject object,
            final ToPersistObjectSet toPersistObjectSet,
            final NakedObjectAssociation association) {
        final NakedObject fieldValue = association.get(object);
        if (fieldValue != null) {
            if (!(fieldValue instanceof NakedObject)) {
                throw new NakedObjectException();
            }
            makePersistent(fieldValue, toPersistObjectSet);
        }
    }

    /**
     * Walks the graph.
     */
    private void processOneToManyAssociation(
            final NakedObject object,
            final ToPersistObjectSet toPersistObjectSet,
            final NakedObjectAssociation field) {
        final NakedObject collection = field.get(object);
        makePersistent(collection, toPersistObjectSet);
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
        for(NakedObject adapter: facet.iterable(collection)) {
            makePersistent(adapter, toPersistObjectSet);
        }
    }


    private void makeCollectionPersistent(final NakedObject collection, final ToPersistObjectSet toPersistObjectSet) {
        if (alreadyPersistedOrNotPersistable(collection)) {
            return;
        }
        LOG.info("persist " + collection);
        if (collection.getResolveState() == ResolveState.TRANSIENT) {
            collection.changeState(ResolveState.RESOLVED);
        }
        toPersistObjectSet.remapAsPersistent(collection);
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
        for(NakedObject adapter: facet.iterable(collection)) {
            makePersistent(adapter, toPersistObjectSet);
        }
    }

    //////////////////////////////////////////////////////////////////
    // toString
    //////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        final ToString toString = new ToString(this);
        return toString.toString();
    }

}
// Copyright (c) Naked Objects Group Ltd.
