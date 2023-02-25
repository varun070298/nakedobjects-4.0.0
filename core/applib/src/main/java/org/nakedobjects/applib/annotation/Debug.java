package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates the an action should be only available for debugging.
 */
@Inherited
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Debug {}

// Copyright (c) Naked Objects Group Ltd.
