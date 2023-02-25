

import org.nakedobjects.applib.annotation.BusinessKey;
import org.nakedobjects.noa.annotations.Annotation;
import org.nakedobjects.noa.annotations.BusinessKeyAnnotation;

import java.lang.reflect.Method;


public class BusinessKeyAnnotationFactory extends AbstractAnnotationFactory {

    public BusinessKeyAnnotationFactory() {
        super(BusinessKeyAnnotation.class);
    }

    public Annotation process(final Class cls) {
        BusinessKey annotation = (BusinessKey) cls.getAnnotation(BusinessKey.class);
        return create(annotation);
    }

    public Annotation process(final Method method) {
        BusinessKey annotation = (BusinessKey) method.getAnnotation(BusinessKey.class);
        return create(annotation);
    }

    private BusinessKeyAnnotation create(BusinessKey annotation) {
        return annotation == null ? null : new BusinessKeyAnnotation(annotation.value());
    }

}
