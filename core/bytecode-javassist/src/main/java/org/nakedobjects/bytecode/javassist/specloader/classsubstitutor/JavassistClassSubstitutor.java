package org.nakedobjects.bytecode.javassist.specloader.classsubstitutor;

import org.nakedobjects.bytecode.javassist.persistence.objectfactory.internal.JavassistEnhanced;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutorAbstract;
import org.nakedobjects.metamodel.util.ClassUtil;


public class JavassistClassSubstitutor extends ClassSubstitutorAbstract {

    public JavassistClassSubstitutor() {
        ignore(javassist.util.proxy.ProxyObject.class);
        ignore(javassist.util.proxy.MethodHandler.class);
    }

    /**
     * If {@link JavassistEnhanced} then return superclass, else
     * as per {@link ClassSubstitutorAbstract#getClass(Class) superclass'} implementation.
     */
    @Override
    public Class<?> getClass(Class<?> cls) {
    	if (ClassUtil.directlyImplements(cls, JavassistEnhanced.class)) {
    		return getClass(cls.getSuperclass());
    	}
    	return super.getClass(cls);
    }

}
// Copyright (c) Naked Objects Group Ltd.
