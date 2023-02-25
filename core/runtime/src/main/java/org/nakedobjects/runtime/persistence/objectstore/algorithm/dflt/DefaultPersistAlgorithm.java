package org.nakedobjects.runtime.persistence.objectstore.algorithm.dflt;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.util.CallbackUtils;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithmAbstract;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.ToPersistObjectSet;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;


public class DefaultPersistAlgorithm extends PersistAlgorithmAbstract {
    private static final Logger LOG = Logger.getLogger(DefaultPersistAlgorithm.class);

    public String name() {
        return "Simple Bottom Up Persistence Walker";
    }

    public void makePersistent(final NakedObject object, final ToPersistObjectSet toPersistObjectSet) {
        if (object.getSpecification().isCollection()) {
            LOG.info("persist " + object);
            if (object.getResolveState() == ResolveState.GHOST) {
                object.changeState(ResolveState.RESOLVING);
                object.changeState(ResolveState.RESOLVED);
            } else if (object.getResolveState() == ResolveState.TRANSIENT) {
                object.changeState(ResolveState.RESOLVED);
            }
            final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(object);
            for (NakedObject element: facet.iterable(object)) {
                persist(element, toPersistObjectSet);
            }
        } else {
            assertObjectNotPersistentAndPersistable(object);
            persist(object, toPersistObjectSet);
        }
    }

    protected void persist(final NakedObject object, final ToPersistObjectSet toPersistObjectSet) {
        if (alreadyPersistedOrNotPersistableOrServiceOrStandalone(object)) {
            return;
        }
        
        final NakedObjectAssociation[] fields = object.getSpecification().getAssociations();
        if (!object.getSpecification().isEncodeable() && fields.length > 0) {
            LOG.info("make persistent " + object);
            CallbackUtils.callCallback(object, PersistingCallbackFacet.class);
            toPersistObjectSet.remapAsPersistent(object);

            for (int i = 0; i < fields.length; i++) {
                final NakedObjectAssociation field = fields[i];
                if (field.isDerived()) {
                    continue;
                }
                if (field.isOneToManyAssociation()) {
                    final NakedObject collection = field.get(object);
                    if (collection == null) {
                        throw new ObjectPersistenceException(
                                "Collection " + field.getName() + 
                                " does not exist in " + object.getSpecification().getFullName());
                    }
                    makePersistent(collection, toPersistObjectSet);
                } else {
                    final NakedObject fieldValue = field.get(object);
                    if (fieldValue == null) {
                        continue;
                    }
                    if (!(fieldValue instanceof NakedObject)) {
                        throw new UnknownTypeException(
                                fieldValue + " is not a NakedObject");
                    }
                    persist(fieldValue, toPersistObjectSet);
                }
            }
            toPersistObjectSet.addPersistedObject(object);
        }
        
    }

    @Override
    public String toString() {
        final ToString toString = new ToString(this);
        return toString.toString();
    }

}
// Copyright (c) Naked Objects Group Ltd.
