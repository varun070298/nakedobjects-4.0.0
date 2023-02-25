package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that the class has a bounded, or finite, set of instances.
 */
@Inherited
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Bounded {}

// Copyright (c) Naked Objects Group Ltd.
