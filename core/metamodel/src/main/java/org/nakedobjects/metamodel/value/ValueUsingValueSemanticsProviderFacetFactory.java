package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.config.NakedObjectConfigurationAware;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.object.value.ValueFacetUsingSemanticsProvider;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAware;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public abstract class ValueUsingValueSemanticsProviderFacetFactory extends FacetFactoryAbstract implements RuntimeContextAware,
        NakedObjectConfigurationAware {

    private RuntimeContext runtimeContext;
    private NakedObjectConfiguration configuration;

    protected ValueUsingValueSemanticsProviderFacetFactory(final Class<? extends Facet> adapterFacetType) {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    protected void addFacets(final ValueSemanticsProviderAbstract adapter) {
        ValueFacetUsingSemanticsProvider facet = new ValueFacetUsingSemanticsProvider(adapter, adapter, getRuntimeContext());
        FacetUtil.addFacet(facet);
    }

    // ////////////////////////////////////////////////////
    // Dependencies (injected via setter)
    // ////////////////////////////////////////////////////

    protected RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

    public void setRuntimeContext(final RuntimeContext runtimeContext) {
        this.runtimeContext = runtimeContext;
    }

    public void setNakedObjectConfiguration(NakedObjectConfiguration configuration) {
        this.configuration = configuration;
    }

    public NakedObjectConfiguration getConfiguration() {
        return configuration;
    }

}
