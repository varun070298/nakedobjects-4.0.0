package org.nakedobjects.runtime.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatState;

import java.util.List;

import org.nakedobjects.applib.clock.Clock;
import org.nakedobjects.applib.fixtures.FixtureClock;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.system.DeploymentType;


/**
 * Implementation that just delegates to a supplied {@link PersistenceSessionFactory}.
 */
public abstract class PersistenceSessionFactoryDelegating implements PersistenceSessionFactory, FixturesInstalledFlag {

    private final DeploymentType deploymentType;
    private final PersistenceSessionFactoryDelegate persistenceSessionFactoryDelegate;
    private SpecificationLoader specificationLoader;
    private List<Object> serviceList;

    private Boolean fixturesInstalled;

	public PersistenceSessionFactoryDelegating(
            final DeploymentType deploymentType,
            final PersistenceSessionFactoryDelegate persistenceSessionFactoryDelegate) {
        this.deploymentType = deploymentType;
        this.persistenceSessionFactoryDelegate = persistenceSessionFactoryDelegate;
    }

    public DeploymentType getDeploymentType() {
        return deploymentType;
    }

    public PersistenceSessionFactoryDelegate getDelegate() {
        return persistenceSessionFactoryDelegate;
    }

    public PersistenceSession createPersistenceSession() {
        return persistenceSessionFactoryDelegate.createPersistenceSession(this);
    }


    public final void init() {
    	// check prereq dependencies injected
    	ensureThatState(specificationLoader, is(notNullValue()));
    	ensureThatState(serviceList, is(notNullValue()));
    	
        
        // a bit of a workaround, but required if anything in the metamodel (for example, a
        // ValueSemanticsProvider for a date value type) needs to use the Clock singleton
        // we do this after loading the services to allow a service to prime a different clock
        // implementation (eg to use an NTP time service).
        if (!deploymentType.isProduction() && !Clock.isInitialized()) {
        	FixtureClock.initialize();
        }

        doInit();
    }

    /**
     * Optional hook method for implementation-specific initialization.
     */
    protected void doInit() {}

    public final void shutdown() {
        doShutdown();
        // other
    }

    /**
     * Optional hook method for implementation-specific shutdown.
     */
    protected void doShutdown() {}


    ////////////////////////////////////////////////////////
    // FixturesInstalledFlag impl
    ////////////////////////////////////////////////////////

    public Boolean isFixturesInstalled() {
		return fixturesInstalled;
	}
	public void setFixturesInstalled(Boolean fixturesInstalled) {
		this.fixturesInstalled = fixturesInstalled;
	}

    
    ////////////////////////////////////////////////////////
    // Dependencies (injected via setters)
    ////////////////////////////////////////////////////////

    public List<Object> getServices() {
        return serviceList;
    }

    public void setServices(List<Object> serviceList) {
    	this.serviceList = serviceList;
    }

    /**
     * @see #setSpecificationLoader(SpecificationLoader)
     */
    public SpecificationLoader getSpecificationLoader() {
        return specificationLoader;
    }

    /**
     * Injected prior to {@link #init()}.
     */
    public void setSpecificationLoader(SpecificationLoader specificationLoader) {
        this.specificationLoader = specificationLoader;
    }

    
    

}

// Copyright (c) Naked Objects Group Ltd.
