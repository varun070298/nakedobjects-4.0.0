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


public class RemoveCallbackFacetFactory extends MethodPrefixBasedFacetFactoryAbstract {

    private static final String REMOVED_PREFIX = "removed";
    private static final String REMOVING_PREFIX = "removing";

    private static final String[] PREFIXES = { REMOVED_PREFIX, REMOVING_PREFIX, };

    public RemoveCallbackFacetFactory() {
        super(PREFIXES, NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover remover, final FacetHolder facetHolder) {
        final List<Facet> facets = new ArrayList<Facet>();
        final List<Method> methods = new ArrayList<Method>();

        Method method = null;
        method = findMethod(cls, OBJECT, REMOVING_PREFIX, void.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methods.add(method);
            RemovingCallbackFacet facet = facetHolder.getFacet(RemovingCallbackFacet.class);
            if (facet == null) {
            	facets.add(new RemovingCallbackFacetViaMethod(method, facetHolder));
            } else {
            	facet.addMethod(method);
            }
        }

        method = findMethod(cls, OBJECT, REMOVED_PREFIX, void.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methods.add(method);
            RemovedCallbackFacet facet = facetHolder.getFacet(RemovedCallbackFacet.class);
            if (facet == null) {
            	facets.add(new RemovedCallbackFacetViaMethod(method, facetHolder));
            } else {
            	facet.addMethod(method);
            }
        }

        remover.removeMethods(methods);
        return FacetUtil.addFacets(facets);
    }

}
