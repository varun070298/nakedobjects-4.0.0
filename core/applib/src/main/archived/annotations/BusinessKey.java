

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that the attribute is an alternate key or a
 * part of an business key (sometimes called alternate key).
 * 
 * <p>
 * Not yet supported.
 */
@Inherited
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessKey {

    String value() default "";
}

// Copyright (c) Naked Objects Group Ltd.
