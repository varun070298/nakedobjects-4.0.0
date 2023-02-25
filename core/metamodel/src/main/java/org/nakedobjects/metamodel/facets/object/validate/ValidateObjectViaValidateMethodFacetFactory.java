package org.nakedobjects.metamodel.facets.object.validate;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.MethodPrefixBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class ValidateObjectViaValidateMethodFacetFactory extends MethodPrefixBasedFacetFactoryAbstract {

    private static final String VALIDATE_PREFIX = "validate";

    private static final String[] PREFIXES = { VALIDATE_PREFIX, };

    public ValidateObjectViaValidateMethodFacetFactory() {
        super(PREFIXES, NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(
    		final Class<?> type, 
    		final MethodRemover methodRemover, 
    		final FacetHolder facetHolder) {
        final Method method = findMethod(type, OBJECT, VALIDATE_PREFIX, String.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methodRemover.removeMethod(method);
            return FacetUtil.addFacet(new ValidateObjectFacetViaValidateMethod(method, facetHolder));
        } else {
            return false;
        }
    }
}
