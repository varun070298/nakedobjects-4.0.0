package org.nakedobjects.runtime.persistence.objectstore.algorithm.topdown;

import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.util.CallbackUtils;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithmAbstract;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.ToPersistObjectSet;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;


public class TopDownPersistAlgorithm extends PersistAlgorithmAbstract {
    private static final Logger LOG = Logger.getLogger(TopDownPersistAlgorithm.class);



    public void makePersistent(final NakedObject object, final ToPersistObjectSet toPersistObjectSet) {
        if (object.getSpecification().isCollection()) {
            makeCollectionPersistent(object, toPersistObjectSet);
        } else {
            makeObjectPersistent(object, toPersistObjectSet);
        }
    }

    private void makeObjectPersistent(final NakedObject object, final ToPersistObjectSet toPersistObjectSet) {
        if (alreadyPersistedOrNotPersistableOrServiceOrStandalone(object)) {
            LOG.warn("can't make object persistent - either already persistent, or transient only: " + object);
            return;
        }

        final NakedObjectAssociation[] fields = object.getSpecification().getAssociations();
        if (!object.getSpecification().isEncodeable() && fields.length > 0) {
            LOG.info("persist " + object);
            CallbackUtils.callCallback(object, PersistingCallbackFacet.class);
            toPersistObjectSet.remapAsPersistent(object);
            toPersistObjectSet.addPersistedObject(object);
            CallbackUtils.callCallback(object, PersistedCallbackFacet.class);

            for (int i = 0; i < fields.length; i++) {
                final NakedObjectAssociation field = fields[i];
                if (field.isDerived()) {
                    continue;
                } else if (field instanceof OneToManyAssociation) {
                    final NakedObject collection = field.get(object);
                    if (collection == null) {
                        throw new ObjectPersistenceException("Collection " + field.getName() + " does not exist in "
                                + object.getSpecification().getFullName());
                    }
                    makePersistent(collection, toPersistObjectSet);
                } else {
                    final NakedObject fieldValue = field.get(object);
                    if (fieldValue == null) {
                        continue;
                    }
                    if (!(fieldValue instanceof NakedObject)) {
                        throw new NakedObjectException(fieldValue + " is not a NakedObject");
                    }
                    makePersistent(fieldValue, toPersistObjectSet);
                }
            }
        }
    }

    private void makeCollectionPersistent(final NakedObject collection, final ToPersistObjectSet toPersistObjectSet) {
        LOG.info("persist " + collection);
        if (collection.getResolveState() == ResolveState.TRANSIENT) {
            collection.changeState(ResolveState.RESOLVED);
        }
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
        final Enumeration elements = facet.elements(collection);
        while (elements.hasMoreElements()) {
            makePersistent((NakedObject) elements.nextElement(), toPersistObjectSet);
        }
    }

    public String name() {
        return "Simple Top Down Persistence Walker";
    }

    @Override
    public String toString() {
        final ToString toString = new ToString(this);
        return toString.toString();
    }

}
// Copyright (c) Naked Objects Group Ltd.
