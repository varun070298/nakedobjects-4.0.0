package org.nakedobjects.metamodel.facets.object.value;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.annotation.Value;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.config.NakedObjectConfigurationAware;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.object.aggregated.AggregatedFacet;
import org.nakedobjects.metamodel.facets.object.ebc.EqualByContentFacet;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.facets.object.ident.icon.IconFacet;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacet;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableFacet;
import org.nakedobjects.metamodel.facets.object.parseable.ParseableFacet;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAware;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


/**
 * Processes the {@link Value} annotation.
 * 
 * <p>
 * As a result, will always install the following facets:
 * <ul>
 * <li> {@link TitleFacet} - based on the <tt>title()</tt> method if present, otherwise uses
 * <tt>toString()</tt></li>
 * <li> {@link IconFacet} - based on the <tt>iconName()</tt> method if present, otherwise derived from the
 * class name</li>
 * </ul>
 * <p>
 * In addition, the following facets may be installed:
 * <ul>
 * <li> {@link ParseableFacet} - if a {@link Parser} has been specified explicitly in the annotation (or is
 * picked up through an external configuration file)</li>
 * <li> {@link EncodableFacet} - if an {@link EncoderDecoder} has been specified explicitly in the annotation
 * (or is picked up through an external configuration file)</li>
 * <li> {@link ImmutableFacet} - if specified explicitly in the annotation
 * <li> {@link EqualByContentFacet} - if specified explicitly in the annotation
 * </ul>
 * <p>
 * Note that {@link AggregatedFacet} is <i>not</i> installed.
 */
public class ValueFacetFactory extends AnnotationBasedFacetFactoryAbstract implements NakedObjectConfigurationAware, RuntimeContextAware {

	
    private NakedObjectConfiguration configuration;
	private RuntimeContext runtimeContext;

    public ValueFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        return FacetUtil.addFacet(create(cls, holder));
    }

    /**
     * Returns a {@link ValueFacet} implementation.
     */
    private ValueFacet create(final Class<?> cls, final FacetHolder holder) {

        // create from annotation, if present
        final Value annotation = getAnnotation(cls, Value.class);
        if (annotation != null) {
            final ValueFacetAnnotation facet = new ValueFacetAnnotation(cls, holder, getNakedObjectConfiguration(), getSpecificationLoader(), getRuntimeContext());
            if (facet.isValid()) {
                return facet;
            }
        }

        // otherwise, try to create from configuration, if present
        final String semanticsProviderName = ValueSemanticsProviderUtil.semanticsProviderNameFromConfiguration(cls,
                configuration);
        if (!StringUtils.isEmpty(semanticsProviderName)) {
            final ValueFacetFromConfiguration facet = new ValueFacetFromConfiguration(semanticsProviderName, holder, getNakedObjectConfiguration(), getSpecificationLoader(), getRuntimeContext());
            if (facet.isValid()) {
                return facet;
            }
        }

        // otherwise, no value semantic
        return null;
    }

	// ////////////////////////////////////////////////////////////////////
    // Injected
    // ////////////////////////////////////////////////////////////////////

    public NakedObjectConfiguration getNakedObjectConfiguration() {
        return configuration;
    }
    public void setNakedObjectConfiguration(final NakedObjectConfiguration configuration) {
        this.configuration = configuration;
    }

    
    private RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}
	public void setRuntimeContext(final RuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
	}


}
