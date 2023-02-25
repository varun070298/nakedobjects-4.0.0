package org.nakedobjects.metamodel.facets.actions.executed;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Executed;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


/**
 * Creates an {@link ExecutedFacet} bsaed on the presence of an {@link Executed} annotation.
 */
public class ExecutedAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public ExecutedAnnotationFacetFactory() {
        super(NakedObjectFeatureType.ACTIONS_ONLY);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final Executed annotation = getAnnotation(method, Executed.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private ExecutedFacet create(final Executed annotation, final FacetHolder holder) {
        return annotation == null ? null : new ExecutedFacetAnnotation(decodeWhere(annotation.value()), holder);
    }

    protected ExecutedFacet.Where decodeWhere(final Executed.Where where) {
        if (where == Executed.Where.LOCALLY) {
            return ExecutedFacet.Where.LOCALLY;
        }
        if (where == Executed.Where.REMOTELY) {
            return ExecutedFacet.Where.REMOTELY;
        }
        return null;
    }

}
