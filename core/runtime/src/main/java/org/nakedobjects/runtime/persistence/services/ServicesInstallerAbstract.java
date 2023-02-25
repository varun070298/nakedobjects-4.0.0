package org.nakedobjects.runtime.persistence.services;

import static org.nakedobjects.metamodel.commons.lang.CastUtils.collectionOf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nakedobjects.runtime.installers.InstallerAbstract;
import org.nakedobjects.runtime.system.DeploymentType;



public abstract class ServicesInstallerAbstract extends InstallerAbstract implements ServicesInstaller {
    private final List<Object> services = new ArrayList<Object>();

    public ServicesInstallerAbstract(String name) {
    	super(ServicesInstaller.TYPE, name);
    }
    
    /**
     * Add this service, automatically unravelling if is a {@link Collection} of services.
     * 
     * @param service
     */
    public void addService(final Object service) {
        if (service instanceof Collection) {
            // unravel if necessary
            final Collection<Object> services = collectionOf(service, Object.class);
            for (final Object eachService : services) {
                addService(eachService);
            }
        } else {
            services.add(service);
        }
    }

    public void addSimpleRepository(final Class<?> cls) {
        addService(new SimpleRepository(cls));
    }

    public List<Object> getServices(final DeploymentType deploymentType) {
        return services;
    }

    public void addServices(final List<Object> services) {
        addService(services);
    }

    public void removeService(final Object service) {
        services.remove(service);
    }

}

// Copyright (c) Naked Objects Group Ltd.
