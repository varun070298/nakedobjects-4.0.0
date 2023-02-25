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


public class PersistCallbackFacetFactory extends MethodPrefixBasedFacetFactoryAbstract {

    private static final String PERSISTED_PREFIX = "persisted";
    private static final String PERSISTING_PREFIX = "persisting";

    private static final String[] PREFIXES = { PERSISTED_PREFIX, PERSISTING_PREFIX, };

    public PersistCallbackFacetFactory() {
        super(PREFIXES, NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover remover, final FacetHolder facetHolder) {
        final List<Facet> facets = new ArrayList<Facet>();
        final List<Method> methods = new ArrayList<Method>();

        Method method = null;
        method = findMethod(cls, OBJECT, PERSISTING_PREFIX, void.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methods.add(method);
            PersistingCallbackFacet facet = facetHolder.getFacet(PersistingCallbackFacet.class);
            if (facet == null) {
            	facets.add(new PersistingCallbackFacetViaMethod(method, facetHolder));
            } else {
            	facet.addMethod(method);
            }
        }

        method = findMethod(cls, OBJECT, PERSISTED_PREFIX, void.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methods.add(method);
            PersistedCallbackFacet facet = facetHolder.getFacet(PersistedCallbackFacet.class);
            if (facet == null) {
            	facets.add(new PersistedCallbackFacetViaMethod(method, facetHolder));
            } else {
            	facet.addMethod(method);
            }
        }

        remover.removeMethods(methods);
        return FacetUtil.addFacets(facets);
    }

}
