package org.nakedobjects.runtime.persistence.adaptermanager.internal;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public class OidAdapterHashMap implements OidAdapterMap {
    
    private static final Logger LOG = Logger.getLogger(OidAdapterHashMap.class);
    private static final int DEFAULT_OID_ADAPTER_MAP_SIZE = 10;

    private final Hashtable<Oid, NakedObject> adapterByOidMap;

    /////////////////////////////////////////////////////////
    // Constructors
    /////////////////////////////////////////////////////////
    
    public OidAdapterHashMap() {
        this(DEFAULT_OID_ADAPTER_MAP_SIZE);
    }

    public OidAdapterHashMap(final int capacity) {
        adapterByOidMap = new Hashtable<Oid, NakedObject>(capacity);
    }


    /////////////////////////////////////////////////////////
    // open, close
    /////////////////////////////////////////////////////////

    public void open() {
        // nothing to do
    }
    
    public void close() {
        LOG.debug("close");
        adapterByOidMap.clear();
    }

    /////////////////////////////////////////////////////////
    // reset
    /////////////////////////////////////////////////////////

    /**
     * Removes all {@link NakedObjectSpecification#isService() non-service} adapters.
     */
    public void reset() {
        LOG.debug("reset");
        for (Iterator<Map.Entry<Oid, NakedObject>> iterator = adapterByOidMap.entrySet().iterator(); iterator.hasNext();) {
        	Map.Entry<Oid, NakedObject> entry = iterator.next();
        	NakedObject adapter = entry.getValue();
			if (!adapter.getSpecification().isService()) {
        		iterator.remove();
        	}
		}
    }

    
    /////////////////////////////////////////////////////////
    // add, remove
    /////////////////////////////////////////////////////////

    public void add(final Oid oid, final NakedObject adapter) {  
      
        adapterByOidMap.put(oid, adapter);
        // log at end so that if toString needs adapters they're in maps. 
        if (LOG.isDebugEnabled()) {
            // do not call toString() on adapter because would call hashCode on the pojo,
            // which for Hibernate PersistentCollections would trigger a resolve. 
            LOG.debug("adding oid: " + oid + " ; oid.hashCode: + " + oid.hashCode() + " ; adapter.hashCode(): " + adapter.hashCode());
        }
    }

    public boolean remove(final Oid oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("remove " + oid);
        }
        return adapterByOidMap.remove(oid) != null;
    }


    /////////////////////////////////////////////////////////
    // getAdapter
    /////////////////////////////////////////////////////////

    public NakedObject getAdapter(final Oid oid) {
        return adapterByOidMap.get(oid);
    }



    /////////////////////////////////////////////////////////
    // iterator
    /////////////////////////////////////////////////////////

    public Iterator<Oid> iterator() {
        return adapterByOidMap.keySet().iterator();
    }
    
    /////////////////////////////////////////////////////////
    // debugging
    /////////////////////////////////////////////////////////

    public String debugTitle() {
        return "Identity adapter map";
    }

    public void debugData(final DebugString debug) {
        int count = 1;
        for(final Oid oid: this) {
            final NakedObject adapter = getAdapter(oid);
            debug.append(count++, 5);
            debug.append(" '");
            debug.append(oid.toString(), 15);
            debug.append("'    ");
            debug.appendln(adapter!= null? adapter.toString(): "(MISSING OBJECT ?!)");
        }
    }


    
}
// Copyright (c) Naked Objects Group Ltd.
