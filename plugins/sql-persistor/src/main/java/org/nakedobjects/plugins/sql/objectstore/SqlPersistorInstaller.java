package org.nakedobjects.plugins.sql.objectstore;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.plugins.sql.objectstore.auto.AutoMapperFactory;
import org.nakedobjects.plugins.sql.objectstore.jdbc.JdbcConnectorFactory;
import org.nakedobjects.plugins.sql.objectstore.jdbc.JdbcFieldMappingFactoryInstaller;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStore;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStorePersistenceMechanismInstallerAbstract;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.system.DeploymentType;


public class SqlPersistorInstaller extends ObjectStorePersistenceMechanismInstallerAbstract {

    private SqlObjectStore objectStore;
    private DatabaseConnectorPool connectionPool;

    public SqlPersistorInstaller() {
        super("sql");
    }

    @Override
    protected ObjectStore createObjectStore(
            NakedObjectConfiguration configuration,
            AdapterFactory nakedObjectFactory,
            AdapterManager adapterManager) {
        
        
        if (objectStore == null) {
            FieldMappingLookup fieldMappingLookup = new FieldMappingLookup();
            JdbcFieldMappingFactoryInstaller installer = new JdbcFieldMappingFactoryInstaller();
            installer.load(fieldMappingLookup);
            // fieldMappingLookup.setValueMappingFactory(new
            // JdbcFieldMappingFactoryInstaller());

            ObjectMappingLookup objectMappingLookup = new ObjectMappingLookup();
            objectMappingLookup.setValueMappingLookup(fieldMappingLookup);
            objectMappingLookup.setObjectMappingFactory(new AutoMapperFactory());
            objectMappingLookup.setConnectionPool(connectionPool);

            SqlObjectStore objectStore = new SqlObjectStore();
            objectStore.setMapperLookup(objectMappingLookup);
            objectStore.setConnectionPool(connectionPool);
            this.objectStore = objectStore;
        }
        return objectStore;
    }

    @Override
    protected OidGenerator createOidGenerator(NakedObjectConfiguration configuration) {
        DatabaseConnectorFactory connectorFactory = new JdbcConnectorFactory();
        connectionPool = new DatabaseConnectorPool(connectorFactory, 1);

        return new SqlOidGenerator(connectionPool);
    }

    public PersistenceSessionFactory createPersistenceSessionFactory(final DeploymentType deploymentType) {
        return new SqlPersistenceSessionFactory(deploymentType, this);
    }
    
    
    /*
    

    
    @Override
    protected AdapterManagerExtended createAdapterManager(final NakedObjectConfiguration configuration) {
        return new XmlAdapterManager();
    }
*/
}

// Copyright (c) Naked Objects Group Ltd.
