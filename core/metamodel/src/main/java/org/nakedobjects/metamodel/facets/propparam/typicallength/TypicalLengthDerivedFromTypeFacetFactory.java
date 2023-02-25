package org.nakedobjects.metamodel.facets.propparam.typicallength;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class TypicalLengthDerivedFromTypeFacetFactory extends FacetFactoryAbstract {

    public TypicalLengthDerivedFromTypeFacetFactory() {
        super(NakedObjectFeatureType.PROPERTIES_AND_PARAMETERS);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final Class<?> type = method.getReturnType();
        return addFacetDerivedFromTypeIfPresent(holder, type);
    }

    @Override
    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        final Class<?> type = method.getParameterTypes()[paramNum];
        return addFacetDerivedFromTypeIfPresent(holder, type);
    }

    private boolean addFacetDerivedFromTypeIfPresent(final FacetHolder holder, final Class<?> type) {
        final TypicalLengthFacet facet = getTypicalLengthFacet(type);
        if (facet != null) {
            return FacetUtil.addFacet(new TypicalLengthFacetDerivedFromType(facet, holder));
        }
        return false;
    }

    private TypicalLengthFacet getTypicalLengthFacet(final Class<?> type) {
        final NakedObjectSpecification paramTypeSpec = getSpecificationLoader().loadSpecification(type);
        return paramTypeSpec.getFacet(TypicalLengthFacet.class);
    }

}
