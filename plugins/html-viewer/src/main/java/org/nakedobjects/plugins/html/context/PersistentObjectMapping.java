package org.nakedobjects.plugins.html.context;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;


public class PersistentObjectMapping implements ObjectMapping {
    private final Oid oid;
    private final NakedObjectSpecification specification;
    private Version version;

    public PersistentObjectMapping(final NakedObject adapter) {
        oid = adapter.getOid();
        Assert.assertFalse("OID is for transient", oid.isTransient());
        Assert.assertFalse("adapter is for transient", adapter.isTransient());
        specification = adapter.getSpecification();
        version = adapter.getVersion();
    }

    public void debug(final DebugString debug) {
        debug.appendln(specification.getFullName());
        if (version != null) {
            debug.appendln(version.toString());
        }
    }

    public Oid getOid() {
        return oid;
    }

    public NakedObject getObject() {
        return getPersistenceSession().loadObject(oid, specification);
    }

    @Override
    public int hashCode() {
        return oid.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj.getClass() == PersistentObjectMapping.class) {
            return ((PersistentObjectMapping) obj).oid.equals(oid);
        }
        return false;
    }

    @Override
    public String toString() {
        return (specification == null ? "null" : specification.getSingularName()) + " : " + oid + " : " + version;
    }

    public Version getVersion() {
        return version;
    }

    public void checkVersion(final NakedObject object) {
        object.checkLock(getVersion());
    }

    public void updateVersion() {
        final NakedObject adapter = getAdapterManager().getAdapterFor(oid);
        version = adapter.getVersion();
    }

    public void restoreToLoader() {
        final Oid oid = getOid();
        final NakedObject adapter = getPersistenceSession().recreateAdapter(oid, specification);
        adapter.setOptimisticLock(getVersion());
    }
    
    
    
    ///////////////////////////////////////////////////////
    // Dependencies (from context)
    ///////////////////////////////////////////////////////
    
    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }


}

// Copyright (c) Naked Objects Group Ltd.
