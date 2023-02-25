package org.nakedobjects.metamodel.specloader.classsubstitutor;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.commons.component.Injectable;

/**
 * Provides capability to translate or ignore classes.
 * 
 * <p>
 * The class strategy is typically required when either an underlying object store 
 * (such as Hibernate); it then allows the enhancement artifacts can be ignored or 
 * interpreted correctly.
 */
public interface ClassSubstitutor extends Injectable, ApplicationScopedComponent {

    Class<?> getClass(Class<?> cls);

}


// Copyright (c) Naked Objects Group Ltd.
