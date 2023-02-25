package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nakedobjects.applib.spec.Specification;

/**
 * 
 */
@Inherited
@Target( { ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MustSatisfy {
    /**
     * The {@link Specification}(s) to be satisfied.
     * 
     * <p>
     * If more than one is provided, then all must be satisfied (in effect &quot;AND&quot;ed together).
     */
    Class<? extends Specification>[] value();
}

// Copyright (c) Naked Objects Group Ltd.
