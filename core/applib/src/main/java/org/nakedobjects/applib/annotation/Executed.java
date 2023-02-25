package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <pre>
 * e.g @Executed(Executed.Where.LOCALLY)
 * </pre>
 */
@Inherited
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Executed {

    public enum Where {
        LOCALLY, REMOTELY
    }

    Where value();
}

// Copyright (c) Naked Objects Group Ltd.
