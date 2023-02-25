package org.nakedobjects.metamodel.facets.object.ident.singular;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.MethodPrefixBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.util.InvokeUtils;


public class SingularMethodFacetFactory extends MethodPrefixBasedFacetFactoryAbstract {

    private static final String SINGULAR_NAME = "singularName";

    private static final String[] PREFIXES = { SINGULAR_NAME, };

    public SingularMethodFacetFactory() {
        super(PREFIXES, NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder facetHolder) {
        final Method method = findMethod(type, CLASS, SINGULAR_NAME, String.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            final String name = (String) InvokeUtils.invokeStatic(method);
            methodRemover.removeMethod(method);
            return FacetUtil.addFacet(new NamedFacetViaMethod(name, facetHolder));
        } else {
            return false;
        }
    }
}
