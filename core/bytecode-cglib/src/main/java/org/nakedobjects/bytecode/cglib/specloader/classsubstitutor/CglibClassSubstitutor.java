package org.nakedobjects.bytecode.cglib.specloader.classsubstitutor;

import org.nakedobjects.bytecode.cglib.persistence.objectfactory.internal.CglibEnhanced;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutorAbstract;
import org.nakedobjects.metamodel.util.ClassUtil;


public class CglibClassSubstitutor extends ClassSubstitutorAbstract {

    
    public CglibClassSubstitutor() {
        ignore(net.sf.cglib.proxy.Factory.class);
        ignore(net.sf.cglib.proxy.MethodProxy.class);
        ignore(net.sf.cglib.proxy.Callback.class);
    }

    /**
     * If {@link CglibEnhanced} then return superclass, else
     * as per {@link ClassSubstitutorAbstract#getClass(Class) superclass'} implementation.
     */
    @Override
    public Class<?> getClass(Class<?> cls) {
    	if (ClassUtil.directlyImplements(cls, CglibEnhanced.class)) {
    		return getClass(cls.getSuperclass());
    	}
    	return super.getClass(cls);
    }


}
// Copyright (c) Naked Objects Group Ltd.
