package org.nakedobjects.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nakedobjects.applib.adapters.Parser;


/**
 * Indicates that the class can be parsed either by delegating to an {@link Parser} or through some
 * externally-configured mechanism.
 */
@Inherited
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Parseable {

    /**
     * The fully qualified name of a class that implements the {@link Parser} interface.
     * 
     * <p>
     * This is optional because some implementations may pick up parseability via a configuration file either
     * for the framework itself, or through a viewer-specific configuration of a widget (eg a calendar view
     * for a date), or indeed through the equivalent {@link #parserClass()}.
     * 
     * <p>
     * It is common for value classes to act as their own parsers. Note that the framework requires that the
     * nominated class provides a <tt>public</tt> no-arg constructor on the class. It instantiates an
     * instance in order to do the parsing, uses the result and discards the instantiated object.
     * 
     * <p>
     * Implementation note: the default value provided here is simply the empty string because <tt>null</tt>
     * is not a valid default.
     */
    String parserName() default "";

    /**
     * As per {@link #parserName()}, but specifying a class literal rather than a fully qualified class name.
     * 
     * <p>
     * Implementation note: the default value provided here is simply the {@link Parseable}'s own class,
     * because <tt>null</tt> is not a valid default.
     */
    Class<?> parserClass() default Parseable.class;
}
