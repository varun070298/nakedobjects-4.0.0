package org.nakedobjects.plugins.hibernate.objectstore.persistence.algorithm;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacet;
import org.nakedobjects.metamodel.util.CallbackUtils;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithm;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithmAbstract;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.ToPersistObjectSet;


/**
 * A {@link PersistAlgorithm} which simply saves the object made persistent. 
 * 
 * <p>
 * This allows Hibernate to determine which objects should be saved - mappings 
 * should be created with <tt>cascade="save-update"</tt>.
 * 
 * <p>
 * An alternative is to let Naked Objects do the work, using the
 * {@link TwoPassPersistAlgorithm}.
 */
public class SimplePersistAlgorithm extends PersistAlgorithmAbstract {
    private static final Logger LOG = Logger.getLogger(PersistAlgorithm.class);


    //////////////////////////////////////////////////////////////////
    // name
    //////////////////////////////////////////////////////////////////

    public String name() {
        return "SimplePersistAlgorithm";
    }


    //////////////////////////////////////////////////////////////////
    // makePersistent
    //////////////////////////////////////////////////////////////////

    /**
     * @param toPersistObjectSet - will actually be implemented by the {@link PersistenceSession}.
     */
    public void makePersistent(final NakedObject object, final ToPersistObjectSet toPersistObjectSet) {
        if (alreadyPersistedOrNotPersistable(object)) {
            return;
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("persist " + object);
        }
        // NakedObjects.getObjectLoader().madePersistent(object);
        // Don't do here - allow EventListener to propogate persistent state
        CallbackUtils.callCallback(object, PersistingCallbackFacet.class);
        toPersistObjectSet.addPersistedObject(object);
        CallbackUtils.callCallback(object, PersistedCallbackFacet.class);
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
