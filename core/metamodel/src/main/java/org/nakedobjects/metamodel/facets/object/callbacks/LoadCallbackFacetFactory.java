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


public class LoadCallbackFacetFactory extends MethodPrefixBasedFacetFactoryAbstract {

    private static final String LOADED_PREFIX = "loaded";
    private static final String LOADING_PREFIX = "loading";

    private static final String[] PREFIXES = { LOADED_PREFIX, LOADING_PREFIX, };

    public LoadCallbackFacetFactory() {
        super(PREFIXES, NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover remover, final FacetHolder facetHolder) {
        final List<Facet> facets = new ArrayList<Facet>();
        final List<Method> methods = new ArrayList<Method>();

        Method method = null;
        method = findMethod(cls, OBJECT, LOADING_PREFIX, void.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methods.add(method);
            facets.add(new LoadingCallbackFacetViaMethod(method, facetHolder));
        }

        method = findMethod(cls, OBJECT, LOADED_PREFIX, void.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methods.add(method);
            facets.add(new LoadedCallbackFacetViaMethod(method, facetHolder));
        }

        remover.removeMethods(methods);
        return FacetUtil.addFacets(facets);
    }

}
