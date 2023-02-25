package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Provides a regular expression that a value entry should conform to, and can be formatted as.
 * 
 * <p>
 * Can also be specified for types that are annotated as <tt>@Value</tt> types. To apply, the value must have string semantics.
 */
@Inherited
@Target( { ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface RegEx {
    String validation();

    String format() default "";

    boolean caseSensitive() default true;
}

// Copyright (c) Naked Objects Group Ltd.
