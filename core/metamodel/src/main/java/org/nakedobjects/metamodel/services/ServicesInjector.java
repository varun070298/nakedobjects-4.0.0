package org.nakedobjects.metamodel.services;

import java.util.List;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.metamodel.commons.component.Injectable;
import org.nakedobjects.metamodel.commons.component.SessionScopedComponent;


/**
 * The repository of services, also able to inject into any object.
 * 
 * <p>
 * The {@link #getContainer() domain object container} is always injected but
 * is not a {@link #getRegisteredServices() registered service}.
 */
public interface ServicesInjector extends SessionScopedComponent, Injectable {

    
    // ///////////////////////////////////////////////////////////////////////////
    // Container
    // ///////////////////////////////////////////////////////////////////////////

    DomainObjectContainer getContainer();
    
    /**
     * Container to inject.
     * 
     * <p>
     * This itself is injected.
     */
    public void setContainer(final DomainObjectContainer container);

    
    // ///////////////////////////////////////////////////////////////////////////
    // Services
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Services to be injected.
     * 
     * <p>
     * Should automatically inject all services into each other (though calling
     * {@link #open()} will also do this).
     * 
     * @param services
     */
    void setServices(List<Object> services);


    /**
     * All registered services, as an immutable {@link List}.
     * 
     * <p>
     * Does not include the {@link #getContainer() container}.
     */
    List<Object> getRegisteredServices();


    // ///////////////////////////////////////////////////////////////////////////
    // Injection (into contained objects)
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Inject all services and the container into the object.
     */
    void injectDependencies(Object domainObject);

    /**
     * As per {@link #injectDependencies(Object)}, but for all objects in the list.
     */
    void injectDependencies(List<Object> objects);




}

// Copyright (c) Naked Objects Group Ltd.
