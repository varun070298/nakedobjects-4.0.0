package org.nakedobjects.runtime.testsystem;

import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.system.DeploymentType;

public class TestProxyPersistenceSessionFactory implements PersistenceSessionFactory {

    private PersistenceSession persistenceSession;
	private List<Object> services;

    public void init() {}

    public void shutdown() {}


    public TestProxyPersistenceSessionFactory() {
		services = Collections.emptyList();
    }
    
    /**
     * Not API.
     */
    public void setServices(List<Object> services) {
		this.services = services;
	}

    /**
     * Not API.
     */
    public void setPersistenceSessionToCreate(PersistenceSession persistenceSession) {
		this.persistenceSession = persistenceSession;
	}

    public PersistenceSession createPersistenceSession() {
        return persistenceSession;
    }

    public DeploymentType getDeploymentType() {
        throw new NotYetImplementedException();
    }

    public void setSpecificationLoader(SpecificationLoader specificationLoader) {
        // does nothing
    }

	public SpecificationLoader getSpecificationLoader() {
		throw new NotYetImplementedException();
	}

	public List<Object> getServices() {
		return services;
	}


}


// Copyright (c) Naked Objects Group Ltd.
