

import org.nakedobjects.applib.annotation.Searchable;
import org.nakedobjects.noa.annotations.Annotation;
import org.nakedobjects.noa.annotations.SearchableAnnotation;


public class SearchableAnnotationFactory extends AbstractAnnotationFactory {

    public SearchableAnnotationFactory() {
        super(SearchableAnnotation.class);
    }

    public Annotation process(Class clazz) {
        Searchable annotation = (Searchable) clazz.getAnnotation(Searchable.class);
        return create(annotation);
    }

    private SearchableAnnotation create(Searchable annotation) {
        if (annotation == null) {
            return null;
        }
        return new SearchableAnnotation(annotation.repository(), annotation.queryByExample());
    }

}
