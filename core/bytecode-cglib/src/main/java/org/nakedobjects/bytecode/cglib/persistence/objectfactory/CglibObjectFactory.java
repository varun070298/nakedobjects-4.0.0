package org.nakedobjects.bytecode.cglib.persistence.objectfactory;

import org.nakedobjects.bytecode.cglib.persistence.objectfactory.internal.ObjectResolveAndObjectChangedEnhancer;
import org.nakedobjects.metamodel.runtimecontext.ObjectInstantiationException;
import org.nakedobjects.runtime.persistence.container.DomainObjectContainerObjectChanged;
import org.nakedobjects.runtime.persistence.container.DomainObjectContainerResolve;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectChanger;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactoryAbstract;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectResolver;

public class CglibObjectFactory extends ObjectFactoryAbstract {

    private ObjectResolveAndObjectChangedEnhancer classEnhancer;
    private DomainObjectContainerResolve resolver;
    private DomainObjectContainerObjectChanged changer;


    public CglibObjectFactory() {
    }
    
    @Override
    public void open() {
        super.open();
        changer = new DomainObjectContainerObjectChanged();
        resolver = new DomainObjectContainerResolve();

        ObjectResolver objectResolver = new ObjectResolver() {
            public void resolve(Object domainObject, String propertyName) {
                // TODO: could do better than this by maintaining a map of resolved
                // properties on the NakedObject adapter.
                resolver.resolve(domainObject);
            }
        };
        ObjectChanger objectChanger = new ObjectChanger() {
            public void objectChanged(Object domainObject) {
                changer.objectChanged(domainObject);
            }
        };
        
        classEnhancer = new ObjectResolveAndObjectChangedEnhancer(
        		objectResolver, objectChanger, getSpecificationLoader());
    }

    public <T> T doInstantiate(Class<T> cls) throws ObjectInstantiationException {
        return classEnhancer.newInstance(cls);
    }
    
}
