package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that a property is not mandatory.
 * 
 * <p>
 * Can also be specified for types that are annotated as <tt>@Value</tt> types. The value need not have string semantics.
 */
@Inherited
@Target( { ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Optional {

}

// Copyright (c) Naked Objects Group Ltd.
