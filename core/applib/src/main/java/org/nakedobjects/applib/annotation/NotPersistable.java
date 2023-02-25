package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that an instance cannot be persisted by a user, but only programmatically.
 */
@Inherited
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotPersistable {

    public enum By {
        USER, USER_OR_PROGRAM
    }

    By value() default By.USER_OR_PROGRAM;

}

// Copyright (c) Naked Objects Group Ltd.
