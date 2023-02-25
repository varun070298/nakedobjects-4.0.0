package org.nakedobjects.plugins.xml.objectstore;

import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegate;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegating;
import org.nakedobjects.runtime.system.DeploymentType;

public class XmlPersistenceSessionFactory extends PersistenceSessionFactoryDelegating {

    public XmlPersistenceSessionFactory(
            final DeploymentType deploymentType, 
            final PersistenceSessionFactoryDelegate persistenceSessionFactoryDelegate) {
        super(deploymentType, persistenceSessionFactoryDelegate);
    }
}


// Copyright (c) Naked Objects Group Ltd.
