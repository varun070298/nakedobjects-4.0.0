package org.nakedobjects.plugins.hibernate.objectstore;


import org.nakedobjects.plugins.hibernate.objectstore.util.HibernateUtil;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegate;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegating;
import org.nakedobjects.runtime.system.DeploymentType;

public class HibernatePersistenceSessionFactory extends PersistenceSessionFactoryDelegating {

    public HibernatePersistenceSessionFactory(
            final DeploymentType deploymentType, 
            final PersistenceSessionFactoryDelegate persistenceSessionFactoryDelegate) {
        super(deploymentType, persistenceSessionFactoryDelegate);
    }

    @Override
    protected void doShutdown() {
        HibernateUtil.shutdown();
    }


}


// Copyright (c) Naked Objects Group Ltd.
