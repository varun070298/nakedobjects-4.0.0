

import org.nakedobjects.applib.annotation.Aggregated;
import org.nakedobjects.noa.annotations.AggregatedAnnotation;
import org.nakedobjects.noa.annotations.Annotation;


public class AggregatedAnnotationFactory extends AbstractAnnotationFactory {

    public AggregatedAnnotationFactory() {
        super(AggregatedAnnotation.class);
    }

    public Annotation process(final Class clazz) {
        Aggregated annotation = (Aggregated) clazz.getAnnotation(Aggregated.class);
        return create(annotation);
    }

    private AggregatedAnnotation create(Aggregated annotation) {
        return annotation == null ? null : new AggregatedAnnotation();
    }

}
