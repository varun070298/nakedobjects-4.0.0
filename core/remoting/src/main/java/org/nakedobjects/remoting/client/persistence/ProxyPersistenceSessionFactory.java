package org.nakedobjects.remoting.client.persistence;

import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegate;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegating;
import org.nakedobjects.runtime.system.DeploymentType;

public class ProxyPersistenceSessionFactory extends PersistenceSessionFactoryDelegating {

    public ProxyPersistenceSessionFactory(
            final DeploymentType deploymentType, 
            final PersistenceSessionFactoryDelegate persistenceSessionFactoryDelegate) {
        super(deploymentType, persistenceSessionFactoryDelegate);
    }

}


// Copyright (c) Naked Objects Group Ltd.
