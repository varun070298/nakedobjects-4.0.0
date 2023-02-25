package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that an property, collection or action should be ignored from the metamodel.
 * 
 * <p>
 * For example, it may be a helper method that needs to be <tt>public</tt> but that doesn't conform to the
 * requirements of an action (for example, invalid parameter types).
 */
@Inherited
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {

}

// Copyright (c) Naked Objects Group Ltd.
