package org.nakedobjects.runtime.persistence.objectfactory;

import org.nakedobjects.metamodel.commons.component.SessionScopedComponent;
import org.nakedobjects.metamodel.runtimecontext.ObjectInstantiationException;


public interface ObjectFactory extends SessionScopedComponent {

    /**
     * Should instantiate the object, and in addition initialize the
     * domain object (for example, inject any services and repositories
     * into it).
     * 
     */
    <T> T instantiate(Class<T> cls) throws ObjectInstantiationException;
}
