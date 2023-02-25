package org.nakedobjects.plugins.sql.objectstore;

import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegating;
import org.nakedobjects.runtime.system.DeploymentType;

public class SqlPersistenceSessionFactory extends PersistenceSessionFactoryDelegating {

    public SqlPersistenceSessionFactory(DeploymentType deploymentType, SqlPersistorInstaller sqlPersistorInstaller) {
        super(deploymentType, sqlPersistorInstaller);
    }
}


// Copyright (c) Naked Objects Group Ltd.
