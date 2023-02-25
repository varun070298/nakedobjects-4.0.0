package org.nakedobjects.metamodel.examples.facets.jsr303;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.ConstraintValidator;


@Documented
@ConstraintValidator(CustomPatternValidator.class)
@Target( { METHOD, FIELD })
@Retention(RUNTIME)
public @interface CustomPattern {
    /** regular expression */
    String regex();

    /** Flags parameter for Pattern.compile() */
    int flags() default 0;

    String message() default "{beancheck.pattern}";

    String[] groups() default {};
}
