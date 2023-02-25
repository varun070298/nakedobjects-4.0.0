package org.nakedobjects.metamodel.facets.actions.executed;

import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.actions.ActionMethodsFacetFactory;
import org.nakedobjects.metamodel.facets.actions.ExecutedFacetViaNamingConvention;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacetInferred;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


/**
 * Creates an {@link ExecutedFacet} based on the prefix of the action's name.
 * 
 * <p>
 * TODO: think that this prefix is handled by the {@link ActionMethodsFacetFactory}.
 */
public class ExecutedViaNamingConventionFacetFactory extends FacetFactoryAbstract {

    private static final String LOCAL_PREFIX = "Local";

    public ExecutedViaNamingConventionFacetFactory() {
        super(NakedObjectFeatureType.ACTIONS_ONLY);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final String fullMethodName = method.getName();
        final String capitalizedName = fullMethodName.substring(0, 1).toUpperCase() + fullMethodName.substring(1);

        if (!capitalizedName.startsWith(LOCAL_PREFIX)) {
            return false;
        }

        return FacetUtil.addFacets(new Facet[] { new ExecutedFacetViaNamingConvention(ExecutedFacet.Where.LOCALLY, holder),
                new NamedFacetInferred(capitalizedName.substring(5), holder) });
    }

    public boolean recognizes(final Method method) {
        return method.getName().startsWith(LOCAL_PREFIX);
    }

}
