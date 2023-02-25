package org.nakedobjects.metamodel.specloader.collectiontyperegistry;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.commons.component.Injectable;

/**
 * TODO: plan is to allow new collection types to be installed dynamically, allowing the domain programmer to
 * declare custom classes to have collection semantics.
 * 
 * <p>
 * In this way there are similarities with the way in which value types are specified using <tt>@Value</tt>.
 * However, we need to maintain a repository of these collection types once nominated so that when we
 * introspect classes we look for collections first, and then properties second.
 */
public interface CollectionTypeRegistry extends Injectable, ApplicationScopedComponent {

    public boolean isCollectionType(Class<?> cls);

    public boolean isArrayType(Class<?> cls);

    public Class<?>[] getCollectionType();

}

// Copyright (c) Naked Objects Group Ltd.
