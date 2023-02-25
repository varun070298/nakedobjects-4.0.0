package org.nakedobjects.metamodel.facets.propparam.validate.mandatory;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.specloader.internal.facetprocessor.FacetProcessor;


/**
 * Simply installs a {@link MandatoryFacetDefault} onto all properties and parameters.
 * 
 * <p>
 * The idea is that this {@link FacetFactory} is included early on in the {@link FacetProcessor}, but
 * other {@link MandatoryFacet} implementations which don't require mandatory semantics will potentially
 * replace these where the property or parameter is annotated or otherwise indicated as being optional.
 */
public class MandatoryDefaultFacetFactory extends FacetFactoryAbstract {

    public MandatoryDefaultFacetFactory() {
        super(NakedObjectFeatureType.PROPERTIES_AND_PARAMETERS);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        return FacetUtil.addFacet(create(holder));
    }

    @Override
    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        return FacetUtil.addFacet(create(holder));
    }

    private MandatoryFacet create(final FacetHolder holder) {
        return new MandatoryFacetDefault(holder);
    }

}
