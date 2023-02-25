package org.nakedobjects.metamodel.facets.actions.invoke;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


/**
 * Language-specific introspectors should implement in order to invoke with the language-specific API.
 * 
 * <p>
 * For example, a Java-based subclass would use <tt>java.lang.reflect.Method</tt> to invoke whereas under .NET
 * it would be <tt>System.Reflect.MethodInfo</tt>.
 */
public abstract class ActionInvocationFacetAbstract extends FacetAbstract implements ActionInvocationFacet {

    public static Class<? extends Facet> type() {
        return ActionInvocationFacet.class;
    }

    public ActionInvocationFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

}

// Copyright (c) Naked Objects Group Ltd.
