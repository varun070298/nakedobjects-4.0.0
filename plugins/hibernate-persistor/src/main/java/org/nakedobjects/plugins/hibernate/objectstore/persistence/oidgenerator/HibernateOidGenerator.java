package org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGeneratorAbstract;


public class HibernateOidGenerator extends OidGeneratorAbstract {

    private static final Logger LOG = Logger.getLogger(HibernateOidGenerator.class);
    private static long transientId = 0;


    ////////////////////////////////////////////////////////////////
    // Name
    ////////////////////////////////////////////////////////////////

    public String name() {
        return "Hibernate Oids";
    }

    ////////////////////////////////////////////////////////////////
    // main API
    ////////////////////////////////////////////////////////////////

    public synchronized HibernateOid createTransientOid(final Object object) {
        final HibernateOid oid = HibernateOid.createTransient(object.getClass(), transientId++);
        if (LOG.isDebugEnabled()) {
            LOG.debug("created OID " + oid + " for " + new ToString(object));
        }
        return oid;
    }


    /**
     * {@inheritDoc}
     * 
     * <p>
     * The call to this method should be preceded by updating the 
     * {@link HibernateOid}, using {@link HibernateOid#setHibernateId(java.io.Serializable)}.
     */
    public void convertTransientToPersistentOid(final Oid oid) {
    	if (!(oid instanceof HibernateOid)) {
    		throw new IllegalArgumentException("Oid is not a HibernateOid");
    	}
		HibernateOid hibernateOid = (HibernateOid) oid;
        hibernateOid.makePersistent();
        if (LOG.isDebugEnabled()) {
            LOG.debug("converted transient OID to persistent " + oid);
        }
    }


    ////////////////////////////////////////////////////////////////
    // Debugging
    ////////////////////////////////////////////////////////////////

    public void debugData(final DebugString debug) {}

    public String debugTitle() {
        return null;
    }
    


}
// Copyright (c) Naked Objects Group Ltd.
