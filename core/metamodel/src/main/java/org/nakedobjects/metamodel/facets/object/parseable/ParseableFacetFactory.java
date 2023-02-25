package org.nakedobjects.metamodel.facets.object.parseable;

import org.nakedobjects.applib.annotation.Parseable;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.config.NakedObjectConfigurationAware;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAware;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class ParseableFacetFactory extends AnnotationBasedFacetFactoryAbstract implements NakedObjectConfigurationAware, RuntimeContextAware {

	
    private NakedObjectConfiguration configuration;
	private RuntimeContext runtimeContext;

    public ParseableFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        return FacetUtil.addFacet(create(cls, holder));
    }

    private ParseableFacetAbstract create(final Class<?> cls, final FacetHolder holder) {
        final Parseable annotation = (Parseable) getAnnotation(cls, Parseable.class);

        // create from annotation, if present
        if (annotation != null) {
            final ParseableFacetAnnotation facet = new ParseableFacetAnnotation(cls, getNakedObjectConfiguration(), holder, getRuntimeContext());
            if (facet.isValid()) {
                return facet;
            }
        }

        // otherwise, try to create from configuration, if present
        final String parserName = ParserUtil.parserNameFromConfiguration(cls, getNakedObjectConfiguration());
        if (!StringUtils.isEmpty(parserName)) {
            final ParseableFacetFromConfiguration facet = new ParseableFacetFromConfiguration(parserName, holder, getRuntimeContext());
            if (facet.isValid()) {
                return facet;
            }
        }

        return null;
    }


	// ////////////////////////////////////////////////////////////////////
    // Dependencies (injected via setters since *Aware)
    // ////////////////////////////////////////////////////////////////////

    public NakedObjectConfiguration getNakedObjectConfiguration() {
        return configuration;
    }
    /**
     * Injected since {@link NakedObjectConfigurationAware}.
     */
    public void setNakedObjectConfiguration(final NakedObjectConfiguration configuration) {
        this.configuration = configuration;
    }



    private RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

    /**
     * Injected since {@link RuntimeContextAware}.
     */
	public void setRuntimeContext(RuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
	}
	
}
