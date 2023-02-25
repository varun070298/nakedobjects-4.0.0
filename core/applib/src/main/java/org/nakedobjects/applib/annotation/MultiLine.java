package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that a string property may have more than one line (ie may contain carriage returns).
 * 
 * <p>
 * In addition you can specify the typical number of lines (defaults to 6) and whether the lines should not be
 * wrapped (by default they will not be wrapped).
 * 
 * <p>
 * Can also be specified for types that are annotated as <tt>@Value</tt> types. To apply, the value must have string semantics.
 * 
 * <p>
 * Note that if this annotation is applied, then any choices for the property (ie as per a <tt>choicesXxx</tt>
 * method) will be ignored.
 */
@Inherited
@Target( { ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiLine {

    int numberOfLines() default 6;

    boolean preventWrapping() default true;
}

// Copyright (c) Naked Objects Group Ltd.
