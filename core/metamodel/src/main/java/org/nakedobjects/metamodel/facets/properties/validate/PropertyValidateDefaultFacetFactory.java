package org.nakedobjects.metamodel.facets.properties.validate;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.specloader.internal.facetprocessor.FacetProcessor;


/**
 * Simply installs a {@link PropertyValidateFacet} onto all properties.
 * 
 * <p>
 * The idea is that this {@link FacetFactory} is included early on in the {@link FacetProcessor}, but
 * other {@link PropertyValidateFacet} implementations will potentially replace these where the property is
 * annotated or otherwise provides a validation mechanism.
 */
public class PropertyValidateDefaultFacetFactory extends FacetFactoryAbstract {

    public PropertyValidateDefaultFacetFactory() {
        super(NakedObjectFeatureType.PROPERTIES_ONLY);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        return FacetUtil.addFacet(create(holder));
    }

    @Override
    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        return FacetUtil.addFacet(create(holder));
    }

    private PropertyValidateFacet create(final FacetHolder holder) {
        return new PropertyValidateFacetDefault(holder);
    }

}
