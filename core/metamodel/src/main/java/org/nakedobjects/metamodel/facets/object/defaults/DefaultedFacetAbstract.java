package org.nakedobjects.metamodel.facets.object.defaults;

import org.nakedobjects.applib.adapters.DefaultsProvider;
import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.util.ClassUtil;


public abstract class DefaultedFacetAbstract extends FacetAbstract implements DefaultedFacet {

    private final Class<?> defaultsProviderClass;

    // to delegate to
    private final DefaultedFacetUsingDefaultsProvider defaultedFacetUsingDefaultsProvider;
    private final RuntimeContext runtimeContext;

    public DefaultedFacetAbstract(
            final String candidateEncoderDecoderName,
            final Class<?> candidateEncoderDecoderClass,
            final FacetHolder holder, 
            final RuntimeContext runtimeContext) {
        super(DefaultedFacet.class, holder, false);

        this.defaultsProviderClass = DefaultsProviderUtil.defaultsProviderOrNull(candidateEncoderDecoderClass,
                candidateEncoderDecoderName);
        this.runtimeContext = runtimeContext;
        if (isValid()) {
            DefaultsProvider defaultsProvider = (DefaultsProvider) ClassUtil.newInstance(defaultsProviderClass, FacetHolder.class, holder);
            this.defaultedFacetUsingDefaultsProvider = new DefaultedFacetUsingDefaultsProvider(defaultsProvider, holder, getRuntimeContext());
        } else {
            this.defaultedFacetUsingDefaultsProvider = null;
        }
    }

    /**
     * Discover whether either of the candidate defaults provider name or class is valid.
     */
    public boolean isValid() {
        return defaultsProviderClass != null;
    }

    /**
     * Guaranteed to implement the {@link EncoderDecoder} class, thanks to generics in the applib.
     */
    public Class<?> getDefaultsProviderClass() {
        return defaultsProviderClass;
    }

    public Object getDefault() {
        return defaultedFacetUsingDefaultsProvider.getDefault();
    }

    @Override
    protected String toStringValues() {
        return defaultsProviderClass.getName();
    }

    ////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    ////////////////////////////////////////////////////////
    

    private RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

}

// Copyright (c) Naked Objects Group Ltd.
