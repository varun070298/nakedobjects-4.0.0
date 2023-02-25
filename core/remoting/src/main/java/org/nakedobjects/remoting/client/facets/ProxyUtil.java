package org.nakedobjects.remoting.client.facets;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ConcurrencyException;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;


public class ProxyUtil {
    private final static Logger LOG = Logger.getLogger(ProxyUtil.class);

    private ProxyUtil() {}

    public static ConcurrencyException concurrencyException(final ConcurrencyException e) {
    	if (LOG.isInfoEnabled()) {
    		LOG.info("concurrency conflict: " + e.getMessage());
    	}
        final Oid source = e.getSource();
        if (source == null) {
            return e;
        }
        final NakedObject failedObject = getAdapterManager().getAdapterFor(source);
		getPersistenceSession().reload(failedObject);
		return new ConcurrencyException("Object automatically reloaded: " + failedObject.titleString(), e);
    }

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

}

// Copyright (c) Naked Objects Group Ltd.
