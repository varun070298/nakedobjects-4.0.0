package org.nakedobjects.runtime.persistence;

import java.util.List;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.specloader.SpecificationLoaderAware;
import org.nakedobjects.runtime.persistence.internal.RuntimeContextFromSession;
import org.nakedobjects.runtime.system.DeploymentType;

/**
 * @see PersistenceSessionFactoryDelegate
 */
public interface PersistenceSessionFactory extends ApplicationScopedComponent, SpecificationLoaderAware {

    DeploymentType getDeploymentType();

    /**
     * Creates a {@link PersistenceSession} with the implementing object as the
     * {@link PersistenceSession}'s {@link PersistenceSession#getPersistenceSessionFactory() owning factory}.  
     */
    PersistenceSession createPersistenceSession();

    /**
     * Make available when creating {@link PersistenceSession}s.
     * 
     * <p>
     * Needed for the {@link RuntimeContextFromSession}.
     */
    SpecificationLoader getSpecificationLoader();

    
    ////////////////////////////////////////////////////////
    // Services
    ////////////////////////////////////////////////////////

    void setServices(List<Object> servicesList);
    List<Object> getServices();

}


// Copyright (c) Naked Objects Group Ltd.
