package org.nakedobjects.metamodel.facets.object.defaults;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Defaulted;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.config.NakedObjectConfigurationAware;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.actions.defaults.ActionDefaultsFacet;
import org.nakedobjects.metamodel.facets.properties.defaults.PropertyDefaultFacet;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAware;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class DefaultedAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract implements NakedObjectConfigurationAware, RuntimeContextAware {

    private NakedObjectConfiguration configuration;
    private RuntimeContext runtimeContext;

	public DefaultedAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_PROPERTIES_AND_PARAMETERS);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        return FacetUtil.addFacet(create(cls, holder));
    }

    private DefaultedFacetAbstract create(final Class<?> cls, final FacetHolder holder) {
        final Defaulted annotation = (Defaulted) getAnnotation(cls, Defaulted.class);

        // create from annotation, if present
        if (annotation != null) {
            final DefaultedFacetAbstract facet = new DefaultedFacetAnnotation(cls, getNakedObjectConfiguration(), holder, runtimeContext);
            if (facet.isValid()) {
                return facet;
            }
        }

        // otherwise, try to create from configuration, if present
        final String providerName = DefaultsProviderUtil.defaultsProviderNameFromConfiguration(cls,
                getNakedObjectConfiguration());
        if (!StringUtils.isEmpty(providerName)) {
            final DefaultedFacetFromConfiguration facet = new DefaultedFacetFromConfiguration(providerName, holder, runtimeContext);
            if (facet.isValid()) {
                return facet;
            }
        }

        return null;
    }

    /**
     * If there is a {@link DefaultedFacet} on the properties return type, then installs a
     * {@link PropertyDefaultFacet} for the property with the same default.
     */
    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        // don't overwrite any defaults that might already picked up
        final PropertyDefaultFacet existingDefaultFacet = holder.getFacet(PropertyDefaultFacet.class);
		if (existingDefaultFacet != null && !existingDefaultFacet.isNoop()) {
            return false;
        }

        // try to infer defaults from the underlying return type
        final Class<?> returnType = method.getReturnType();
        final DefaultedFacet returnTypeDefaultedFacet = getDefaultedFacet(returnType);
        if (returnTypeDefaultedFacet != null) {
            final PropertyDefaultFacetDerivedFromDefaultedFacet propertyFacet = new PropertyDefaultFacetDerivedFromDefaultedFacet(
                    returnTypeDefaultedFacet, holder, getRuntimeContext());
            return FacetUtil.addFacet(propertyFacet);
        }
        return false;
    }

	/**
     * If there is a {@link DefaultedFacet} on any of the action's parameter types, then installs a
     * {@link ActionDefaultsFacet} for the action.
     */
    @Override
    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        // don't overwrite any defaults already picked up
        if (holder.getFacet(ActionDefaultsFacet.class) != null) {
            return false;
        }

        // try to infer defaults from any of the parameter's underlying types
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final DefaultedFacet[] parameterTypeDefaultedFacets = new DefaultedFacet[parameterTypes.length];
        boolean hasAtLeastOneDefault = false;
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> paramType = parameterTypes[i];
            parameterTypeDefaultedFacets[i] = getDefaultedFacet(paramType);
            hasAtLeastOneDefault = hasAtLeastOneDefault | (parameterTypeDefaultedFacets[i] != null);
        }
        if (hasAtLeastOneDefault) {
            return FacetUtil.addFacet(new ActionDefaultsFacetDerivedFromDefaultedFacets(parameterTypeDefaultedFacets, holder));
        }
        return false;
    }

    private DefaultedFacet getDefaultedFacet(final Class<?> paramType) {
        final NakedObjectSpecification paramTypeSpec = getSpecificationLoader().loadSpecification(paramType);
        return paramTypeSpec.getFacet(DefaultedFacet.class);
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
    public void setRuntimeContext(RuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
	}


}
