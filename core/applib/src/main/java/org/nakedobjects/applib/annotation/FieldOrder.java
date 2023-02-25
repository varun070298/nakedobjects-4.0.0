package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates the order that properties should be displayed in.
 */
@Inherited
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldOrder {
    String value();
}

// Copyright (c) Naked Objects Group Ltd.
