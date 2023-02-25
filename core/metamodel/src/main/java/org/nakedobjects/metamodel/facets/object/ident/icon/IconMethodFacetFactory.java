package org.nakedobjects.metamodel.facets.object.ident.icon;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.MethodPrefixBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class IconMethodFacetFactory extends MethodPrefixBasedFacetFactoryAbstract {

    private static final String ICON_NAME_PREFIX = "iconName";

    private static final String[] PREFIXES = { ICON_NAME_PREFIX, };

    public IconMethodFacetFactory() {
        super(PREFIXES, NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder facetHolder) {
        final Method method = findMethod(cls, OBJECT, ICON_NAME_PREFIX, String.class, NO_PARAMETERS_TYPES);
        if (method == null) {
            return false;
        }
        methodRemover.removeMethod(method);
        return FacetUtil.addFacet(new IconFacetViaMethod(method, facetHolder));
    }
}
