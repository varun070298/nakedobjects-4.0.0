package org.nakedobjects.example.objectstore;

import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegate;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegating;
import org.nakedobjects.runtime.system.DeploymentType;


public class ExamplePersistenceSessionFactory extends PersistenceSessionFactoryDelegating {

    public ExamplePersistenceSessionFactory(
            DeploymentType deploymentType,
            PersistenceSessionFactoryDelegate persistenceSessionFactoryDelegate) {
        super(deploymentType, persistenceSessionFactoryDelegate);
    }

}

// Copyright (c) Naked Objects Group Ltd.
