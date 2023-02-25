package org.nakedobjects.metamodel.examples.facets.jsr303;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Documented
@Target( { ElementType.METHOD, FIELD })
@Retention(RUNTIME)
public @interface CustomPatterns {
    CustomPattern[] value();
}
