package org.nakedobjects.runtime.persistence.adaptermanager.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.map.IdentityMap;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.lang.ToString;


/**
 * TODO: an alternative might be to use {@link IdentityMap}.
 */
public class PojoAdapterHashMap implements PojoAdapterMap {

    private static class IdentityHashKey {
        private Object pojo;

        public IdentityHashKey(Object pojo) {
            this.pojo = pojo;
        }

        public int hashCode() {
            return System.identityHashCode(pojo);
        }

        public boolean equals(Object obj) {
            return obj == this || (obj instanceof IdentityHashKey && ((IdentityHashKey) obj).pojo == pojo);
        }
    }

    private static final Logger LOG = Logger.getLogger(PojoAdapterHashMap.class);
    public static final int DEFAULT_POJO_ADAPTER_MAP_SIZE = 10;

    protected final Map<Object, NakedObject> adapterByPojoMap;

    // ///////////////////////////////////////////////////////////////////////////
    // Constructors, finalize
    // ///////////////////////////////////////////////////////////////////////////

    public PojoAdapterHashMap() {
        this(DEFAULT_POJO_ADAPTER_MAP_SIZE);
    }

    public PojoAdapterHashMap(final int capacity) {
        adapterByPojoMap = new HashMap<Object, NakedObject>(capacity);
        //adapterByPojoMap = new IdentityMap(capacity);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LOG.info("finalizing hash of pojos");
    }

    // ///////////////////////////////////////////////////////////////////////////
    // open, close
    // ///////////////////////////////////////////////////////////////////////////

    public void open() {
    // nothing to do
    }

    public void close() {
        LOG.debug("close");
        adapterByPojoMap.clear();
    }

    // ///////////////////////////////////////////////////////////////////////////
    // reset
    // ///////////////////////////////////////////////////////////////////////////

    public void reset() {
        LOG.debug("reset");
        for (Iterator<Map.Entry<Object, NakedObject>> iterator = adapterByPojoMap.entrySet().iterator(); iterator.hasNext();) {
        	Map.Entry<Object, NakedObject> entry = iterator.next();
        	NakedObject adapter = entry.getValue();
			if (!adapter.getSpecification().isService()) {
        		iterator.remove();
        	}
		}
    }

    // ///////////////////////////////////////////////////////////////////////////
    // add, remove
    // ///////////////////////////////////////////////////////////////////////////

    public void add(final Object pojo, final NakedObject adapter) {
        adapterByPojoMap.put(key(pojo), adapter);
        // log at end so that if toString needs adapters they're in maps.
        if (adapter.getResolveState().isResolved()) {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("add " + new ToString(pojo) + " as " + adapter);
        	}
        }
    }

    public void remove(final NakedObject object) {
        LOG.debug("remove " + object);
        adapterByPojoMap.remove(key(object.getObject()));
    }

    // ///////////////////////////////////////////////////////////////////////////
    // contains
    // ///////////////////////////////////////////////////////////////////////////

    public boolean containsPojo(final Object pojo) {
        return adapterByPojoMap.containsKey(key(pojo));
    }

    // ///////////////////////////////////////////////////////////////////////////
    // get
    // ///////////////////////////////////////////////////////////////////////////

    public NakedObject getAdapter(final Object pojo) {
        return adapterByPojoMap.get(key(pojo));
    }

    // ///////////////////////////////////////////////////////////////////////////
    // elements
    // ///////////////////////////////////////////////////////////////////////////

    public Iterator<NakedObject> iterator() {
        return adapterByPojoMap.values().iterator();
    }

    private Object key(Object pojo) {
        return new IdentityHashKey(pojo);
    }

    // ///////////////////////////////////////////////////////////////////////////
    // Debugging
    // ///////////////////////////////////////////////////////////////////////////

    public void debugData(final DebugString debug) {
        int count = 0;
        for (final Object pojo : adapterByPojoMap.keySet()) {
            final NakedObject object = adapterByPojoMap.get(pojo);
            debug.append(count++ + 1, 5);
            debug.append(" '");
            debug.append(pojo.toString(), 50);
            debug.append("'    ");
            debug.appendln(object.toString());
        }
    }

    public String debugTitle() {
        return "POJO Adapter Hashtable";
    }
}
// Copyright (c) Naked Objects Group Ltd.
