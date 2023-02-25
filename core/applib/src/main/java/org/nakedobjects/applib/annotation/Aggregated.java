package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that the class is part of (aggregated within) a larger class, which may reference objects outside
 * the aggregate but may not be referenced by objects other than within the aggregate.
 * 
 * <p>
 * This is/should be interpreted by viewers as meaning that references to the object may not be shared between
 * instances. So for example in the DnD viewer an aggregated object may not be drag/dropped into an empty
 * &quot;slot&quot;. Instead, the user would need to use copy/paste.
 * 
 * <p>
 * Note that aggregated objects are arbitrarily large and are usually mutable. {@link Value}s are similar
 * (the only thing that one can definitively say about a value is that it is an aggregate, however values are
 * typically small and are usually immutable.
 * 
 * @see Value
 */
@Inherited
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Aggregated {}

// Copyright (c) Naked Objects Group Ltd.
