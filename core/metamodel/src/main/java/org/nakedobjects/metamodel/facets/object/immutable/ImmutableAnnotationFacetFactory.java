package org.nakedobjects.metamodel.facets.object.immutable;

import java.lang.reflect.Method;

import org.nakedobjects.applib.annotation.Immutable;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class ImmutableAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public ImmutableAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_PROPERTIES_AND_COLLECTIONS);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final Immutable annotation = getAnnotation(cls, Immutable.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
    	NakedObjectSpecification spec = getSpecificationLoader().loadSpecification(method.getDeclaringClass());
        final ImmutableFacet immutableFacet = spec.getFacet(ImmutableFacet.class);
        if (immutableFacet != null && !immutableFacet.isNoop()) {
            return FacetUtil.addFacet(new DisabledFacetDerivedFromImmutable(immutableFacet, holder));
        }
        return false;
    }

    private ImmutableFacet create(final Immutable annotation, final FacetHolder holder) {
        return annotation == null ? null : new ImmutableFacetAnnotation(decodeWhen(annotation.value()), holder);
    }


}
