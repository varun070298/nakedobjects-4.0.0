package org.nakedobjects.metamodel.facets.object.ident.title;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.commons.lang.JavaClassUtils;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.FallbackFacetFactory;
import org.nakedobjects.metamodel.java5.MethodPrefixBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class TitleMethodFacetFactory extends MethodPrefixBasedFacetFactoryAbstract {

    private static final String TO_STRING = "toString";
    private static final String TITLE = "title";

    private static final String[] PREFIXES = { TO_STRING, TITLE, };

    public TitleMethodFacetFactory() {
        super(PREFIXES, NakedObjectFeatureType.OBJECTS_ONLY);
    }

    /**
     * If no title or toString can be used then will use Facets provided by {@link FallbackFacetFactory}
     * instead.
     */
    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder facetHolder) {

        Method method = findMethod(type, OBJECT, TITLE, String.class, null);
        if (method != null) {
            methodRemover.removeMethod(method);
            return FacetUtil.addFacet(new TitleFacetViaTitleMethod(method, facetHolder));
        }

        try {
            method = findMethod(type, OBJECT, TO_STRING, String.class, null);
            if (method == null) {
                return false;
            }
            if (JavaClassUtils.isJavaClass(method.getDeclaringClass())) {
                return false;
            }
            methodRemover.removeMethod(method);
            FacetUtil.addFacet(new TitleFacetViaToStringMethod(method, facetHolder));
            return false;

        } catch (final Exception e) {
            return false;
        }
    }
}
