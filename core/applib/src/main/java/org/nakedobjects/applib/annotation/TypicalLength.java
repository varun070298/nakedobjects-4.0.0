package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nakedobjects.applib.adapters.Parser;


/**
 * The typical entry length of a field, use to determine the optimum width for display
 * 
 * @see Parser
 */
@Inherited
@Target( { ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TypicalLength {
    int value();
}

// Copyright (c) Naked Objects Group Ltd.
