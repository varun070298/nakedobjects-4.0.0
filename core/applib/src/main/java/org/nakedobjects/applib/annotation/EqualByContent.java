package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;


/**
 * Indicates that the class follows the equal-by-content contract, usually associated with {@link Value value}
 * types.
 * 
 * <p>
 * If a class claims to be equal-by-content then its {@link #equals(Object)} should return <tt>true</tt> if
 * its content (as opposed to identity) is the same. For example, {@link String}, {@link BigDecimal} and
 * {@link Date} follow this contract.
 * 
 * <p>
 * Note also that the Java Language Specification requires that two objects that are
 * {@link #equals(Object) equal} must return the same value from {@link #hashCode()}. Failure to do this
 * means that that the object will not behave correctly when used as a key into a hashing structure (eg a
 * {@link HashMap}).
 * 
 * <p>
 * By default any {@link Value value} types are assumed to follow the equal-by-content rule, though this can
 * be overridden if required. Value types are usually also {@link Immutable immutable} (though there are some
 * classic exceptions to this, such as {@link Date}).
 * 
 * @see Immutable
 * @see Value
 */
@Inherited
@Target( { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EqualByContent {
    When value() default When.ALWAYS;
}

// Copyright (c) Naked Objects Group Ltd.
