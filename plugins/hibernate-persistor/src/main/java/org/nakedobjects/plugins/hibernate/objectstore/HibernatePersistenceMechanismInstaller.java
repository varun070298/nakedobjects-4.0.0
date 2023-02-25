package org.nakedobjects.plugins.hibernate.objectstore;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.algorithm.SimplePersistAlgorithm;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.algorithm.TwoPassPersistAlgorithm;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator.HibernateOidGenerator;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.PersistenceSessionLogger;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStore;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStorePersistenceMechanismInstallerAbstract;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithm;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.system.DeploymentType;


/**
 * Installs the Hibernate object store.
 */
public class HibernatePersistenceMechanismInstaller extends ObjectStorePersistenceMechanismInstallerAbstract {
    
	private static final Logger LOG = Logger.getLogger(HibernatePersistenceMechanismInstaller.class);

	public HibernatePersistenceMechanismInstaller() {
		super("hibernate");
	}
    

    //////////////////////////////////////////////////
    // createPersistenceSessionFactory
    //////////////////////////////////////////////////

    public PersistenceSessionFactory createPersistenceSessionFactory(final DeploymentType deploymentType) {
        return new HibernatePersistenceSessionFactory(deploymentType, this);
    }

    @Override
    public PersistenceSession createPersistenceSession(PersistenceSessionFactory persistenceSessionFactory) {
    	LOG.info("installing " + this.getClass().getName());
    	
    	return new PersistenceSessionLogger(super.createPersistenceSession(persistenceSessionFactory));
    }
    

    //////////////////////////////////////////////////
    // createObjectStore
    //////////////////////////////////////////////////

    /**
     * Creates one of four variants of {@link ObjectStore}, dependent on {@link NakedObjectConfiguration}:
     * <ul>
     * <li>not remapping &amp; is save immediate : {@link HibernateObjectStoreImmediate} </li>
     * <li>not remapping &amp; not save immediate: {@link HibernateObjectStore} </li>
     * <li>is remapping &amp;  is  save immediate: {@link HibernateObjectStoreRemapping}( {@link HibernateObjectStoreImmediate} ) </li>
     * <li>is remapping &amp;  not save immediate: {@link HibernateObjectStoreRemapping}( {@link HibernateObjectStore} ) </li>
     * </ul> 
     * 
     */
    @Override
    protected ObjectStore createObjectStore(NakedObjectConfiguration configuration, AdapterFactory nakedObjectFactory, AdapterManager adapterManager) {
        
        ObjectStore objectStore = 
            isSaveImmediate(configuration) ? 
                    new HibernateObjectStoreImmediate() : 
                    new HibernateObjectStore();
        
        return isRemapping(configuration) ? 
                    new HibernateObjectStoreRemapping(objectStore) : 
                    objectStore;
    }

    private boolean isSaveImmediate(final NakedObjectConfiguration configuration) {
        return configuration.getBoolean(HibernateConstants.SAVE_IMMEDIATE_KEY, true);
    }

    private boolean isRemapping(final NakedObjectConfiguration configuration) {
        return configuration.getBoolean(HibernateConstants.REMAPPING_KEY, false);
    }



    //////////////////////////////////////////////////
    // OidGenerator
    //////////////////////////////////////////////////

    @Override
    protected OidGenerator createOidGenerator(NakedObjectConfiguration configuration) {
        return new HibernateOidGenerator();
    }

    //////////////////////////////////////////////////
    // createPersistAlgorithm
    //////////////////////////////////////////////////
    
    @Override
    protected PersistAlgorithm createPersistAlgorithm(final NakedObjectConfiguration configuration) {
        final String algorithm = getPersistAlgorithm(configuration);
        if ("simple".equals(algorithm)) {
            return new SimplePersistAlgorithm();
        } else {
            return new TwoPassPersistAlgorithm();
        }
    }

    private String getPersistAlgorithm(final NakedObjectConfiguration configuration) {
        return configuration.getString(HibernateConstants.PERSIST_ALGORITHM_KEY);
    }



}
// Copyright (c) Naked Objects Group Ltd.
