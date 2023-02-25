package org.nakedobjects.metamodel.facets.object.ident.plural;

import org.nakedobjects.applib.annotation.Plural;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class PluralAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public PluralAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final Plural annotation = getAnnotation(cls, Plural.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private PluralFacet create(final Plural annotation, final FacetHolder holder) {
        return annotation == null ? null : new PluralFacetAnnotation(annotation.value(), holder);
    }

}
