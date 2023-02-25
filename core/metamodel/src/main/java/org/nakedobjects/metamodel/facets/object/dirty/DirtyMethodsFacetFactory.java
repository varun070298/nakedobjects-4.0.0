package org.nakedobjects.metamodel.facets.object.dirty;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.MethodPrefixBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;



public class DirtyMethodsFacetFactory extends MethodPrefixBasedFacetFactoryAbstract {

    private static final String MARK_DIRTY_PREFIX = "markDirty";
    private static final String CLEAR_DIRTY_PREFIX = "clearDirty";
    private static final String IS_DIRTY_PREFIX = "isDirty";

    private static final String[] PREFIXES = { MARK_DIRTY_PREFIX, CLEAR_DIRTY_PREFIX, IS_DIRTY_PREFIX, };

    public DirtyMethodsFacetFactory() {
        super(PREFIXES, NakedObjectFeatureType.OBJECTS_ONLY);

    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder facetHolder) {
        final List<Facet> facets = new ArrayList<Facet>();

        Method method = findMethod(cls, OBJECT, IS_DIRTY_PREFIX, boolean.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methodRemover.removeMethod(method);
            facets.add(new IsDirtyObjectFacetViaMethod(method, facetHolder));
        }

        method = findMethod(cls, OBJECT, CLEAR_DIRTY_PREFIX, void.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methodRemover.removeMethod(method);
            facets.add(new ClearDirtyObjectFacetViaMethod(method, facetHolder));
        }

        method = findMethod(cls, OBJECT, MARK_DIRTY_PREFIX, void.class, NO_PARAMETERS_TYPES);
        if (method != null) {
            methodRemover.removeMethod(method);
            facets.add(new MarkDirtyObjectFacetViaMethod(method, facetHolder));
        }

        return FacetUtil.addFacets(facets);
    }

}
