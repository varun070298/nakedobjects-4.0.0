package org.nakedobjects.metamodel.services;

import java.util.List;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.metamodel.commons.component.Noop;

public class ServicesInjectorNoop implements ServicesInjector, Noop {

    
    public void open() {}
    public void close() {}
    
    public DomainObjectContainer getContainer() {
        return null;
    }
    public void setContainer(DomainObjectContainer container) {}
    
    public void setServices(List<Object> services) {}
    public List<Object> getRegisteredServices() {
        return null;
    }

    public void injectDependencies(Object domainObject) {}
    
    public void injectDependencies(List<Object> objects) {}
    
    
    public void injectInto(Object candidate) {}
    

}


// Copyright (c) Naked Objects Group Ltd.
