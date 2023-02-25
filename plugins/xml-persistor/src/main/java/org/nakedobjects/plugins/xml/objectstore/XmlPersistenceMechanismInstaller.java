package org.nakedobjects.plugins.xml.objectstore;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.plugins.xml.objectstore.internal.adapter.XmlAdapterManager;
import org.nakedobjects.plugins.xml.objectstore.internal.clock.DefaultClock;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerExtended;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStore;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStorePersistenceMechanismInstallerAbstract;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.persistence.oidgenerator.timebased.TimeBasedOidGenerator;
import org.nakedobjects.runtime.system.DeploymentType;


public class XmlPersistenceMechanismInstaller extends ObjectStorePersistenceMechanismInstallerAbstract {
    
	@SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(XmlPersistenceMechanismInstaller.class);
    private XmlObjectStore objectStore;

    public XmlPersistenceMechanismInstaller() {
    	super("xml");
    }
    

    @Override
    protected ObjectStore createObjectStore(NakedObjectConfiguration configuration, AdapterFactory nakedObjectFactory, AdapterManager adapterManager) {
        if (objectStore == null) {
            objectStore = new XmlObjectStore(configuration);
            objectStore.setClock(new DefaultClock());
        }
        return objectStore;
    }

    
    @Override
    protected AdapterManagerExtended createAdapterManager(final NakedObjectConfiguration configuration) {
        return new XmlAdapterManager();
    }

    @Override
    protected OidGenerator createOidGenerator(NakedObjectConfiguration configuration) {
        return new TimeBasedOidGenerator();
    }

    public PersistenceSessionFactory createPersistenceSessionFactory(final DeploymentType deploymentType) {
        return new XmlPersistenceSessionFactory(deploymentType, this);
    }
}
// Copyright (c) Naked Objects Group Ltd.
