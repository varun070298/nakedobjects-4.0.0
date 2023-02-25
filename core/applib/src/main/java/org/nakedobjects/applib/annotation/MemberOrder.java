package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates the position a method should be placed in.
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MemberOrder {

    /**
     * Number in Dewey Decimal format representing the order.
     */
    String sequence();

    /**
     * Name of the action group this set should be known as.
     */
    String name() default "";
}

// Copyright (c) Naked Objects Group Ltd.
