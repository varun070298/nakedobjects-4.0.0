package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that a property, collection or action is to be hidden from the user.
 */
@Inherited
@Target( { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Hidden {
    When value() default When.ALWAYS;
}

// Copyright (c) Naked Objects Group Ltd.
