package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that an instance cannot be changed.
 * 
 * <p>
 * To make something always immutable used the form <tt>@Immutable</tt>. To make something immutable only once persisted use the form
 *            <tt>@Immutable(Immutable.ONCE_PERSISTED)</tt>.
 * 
 * <p>
 * By default any {@link Value value} types are assumed to be immutable, though this can be overridden if
 * required. Immutable objects that are acting as a value type should almost certainly also follow the
 * {@link EqualByContent equal-by-content} contract.
 * 
 * @see Value
 * @see EqualByContent
 */
@Inherited
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Immutable {
    When value() default When.ALWAYS;
}

// Copyright (c) Naked Objects Group Ltd.
