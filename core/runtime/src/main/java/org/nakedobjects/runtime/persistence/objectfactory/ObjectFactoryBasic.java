package org.nakedobjects.runtime.persistence.objectfactory;

import java.lang.reflect.Modifier;

import org.nakedobjects.metamodel.runtimecontext.ObjectInstantiationException;

public class ObjectFactoryBasic extends ObjectFactoryAbstract {


    public ObjectFactoryBasic() {}
    
    public ObjectFactoryBasic(Mode mode) {
        super(mode);
    }

    
    /**
     * Simply instantiates reflectively, does not enhance bytecode etc in any way.
     */
    @Override
    protected <T> T doInstantiate(Class<T> cls) throws ObjectInstantiationException {
        if (Modifier.isAbstract(cls.getModifiers())) {
            throw new ObjectInstantiationException("Cannot create an instance of an abstract class: " + cls);
        }
        try {
            return cls.newInstance();
        } catch (IllegalAccessException e) {
            throw new ObjectInstantiationException(e);
        } catch (InstantiationException e) {
            throw new ObjectInstantiationException(e);
        }
    }




}
