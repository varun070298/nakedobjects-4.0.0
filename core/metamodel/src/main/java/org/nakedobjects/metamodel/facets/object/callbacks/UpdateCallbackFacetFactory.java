package org.nakedobjects.metamodel.facets.object.callbacks;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.MethodPrefixBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class UpdateCallbackFacetFactory extends MethodPrefixBasedFacetFactoryAbstract {

    private static final String UPDATED_PREFIX = "updated";
    private static final String UPDATING_PREFIX = "updating";

    private static final String[] PREFIXES = { UPDATED_PREFIX, UPDATING_PREFIX, };

    public UpdateCallbackFacetFactory() {
        super(PREFIXES, NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover remover, final FacetHolder facetHolder) {
        final List<Facet> facets = new ArrayList<Facet>();
        final List<Method> methods = new ArrayList<Method>();

        Method method = null;
        method = findMethod(cls, OBJECT, UPDATING_PREFIX, void.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methods.add(method);
            facets.add(new UpdatingCallbackFacetViaMethod(method, facetHolder));
        }

        method = findMethod(cls, OBJECT, UPDATED_PREFIX, void.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methods.add(method);
            facets.add(new UpdatedCallbackFacetViaMethod(method, facetHolder));
        }

        remover.removeMethods(methods);
        return FacetUtil.addFacets(facets);
    }

}
