

import org.nakedobjects.applib.annotation.Cached;
import org.nakedobjects.noa.annotations.Annotation;
import org.nakedobjects.noa.annotations.CachedAnnotation;


public class CachedAnnotationFactory extends AbstractAnnotationFactory {

    public CachedAnnotationFactory() {
        super(CachedAnnotation.class);
    }

    public Annotation process(final Class clazz) {
        Cached annotation = (Cached) clazz.getAnnotation(Cached.class);
        return create(annotation);
    }

    private CachedAnnotation create(Cached annotation) {
        return annotation == null ? null : new CachedAnnotation();
    }

}
