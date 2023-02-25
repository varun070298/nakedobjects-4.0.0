package org.nakedobjects.metamodel.facets.object.defaults;

import org.nakedobjects.applib.adapters.DefaultsProvider;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class DefaultedFacetUsingDefaultsProvider extends FacetAbstract implements DefaultedFacet {

    private final DefaultsProvider defaultsProvider;
	private final RuntimeContext runtimeContext;

    public DefaultedFacetUsingDefaultsProvider(final DefaultsProvider parser, final FacetHolder holder, final RuntimeContext runtimeContext) {
        super(DefaultedFacet.class, holder, false);
        this.defaultsProvider = parser;
        this.runtimeContext = runtimeContext;
    }

    @Override
    protected String toStringValues() {
    	getRuntimeContext().injectDependenciesInto(defaultsProvider);
        return defaultsProvider.toString();
    }

    public Object getDefault() {
    	getRuntimeContext().injectDependenciesInto(defaultsProvider);
        return defaultsProvider.getDefaultValue();
    }

    
    ////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    ////////////////////////////////////////////////////////
    

    private RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

}

// Copyright (c) Naked Objects Group Ltd.
