package org.nakedobjects.applib.adapters;

import java.math.BigDecimal;

import org.nakedobjects.applib.annotation.Aggregated;
import org.nakedobjects.applib.annotation.Defaulted;
import org.nakedobjects.applib.annotation.Encodable;
import org.nakedobjects.applib.annotation.EqualByContent;
import org.nakedobjects.applib.annotation.Immutable;
import org.nakedobjects.applib.annotation.Parseable;
import org.nakedobjects.applib.annotation.Value;


/**
 * Provides a mechanism for providing a set of value semantics.
 * 
 * <p>
 * As explained in the Javadoc of the {@link Value} annotation, value semantics only actually implies that the
 * type is {@link Aggregated aggregated}. However, values are very often also {@link Parseable},
 * {@link Encodable}, {@link Immutable} and implement {@link EqualByContent} semantics. In addition, there
 * may be a {@link Defaulted default value}.
 * 
 * <p>
 * This interface is used by {@link Value} to allow these semantics to be provided through a single point.
 * Alternatively, {@link Value} supports this information being provided via the configuration files.
 * 
 * <p>
 * Whatever the class that implements this interface, it must also expose either a
 * <tt>public</tt> no-arg constructor, or (for implementations that also are <tt>Facet</tt>s) 
 * a <tt>public</tt> constructor that accepts a single <tt>FacetHolder</tt>.  This constructor allows the 
 * framework to instantiate the object reflectively.
 * 
 * @see Parser
 * @see EncoderDecoder
 * @see DefaultsProvider
 */
public interface ValueSemanticsProvider<T> {

    /**
     * The {@link Parser}, if any.
     * 
     * <p>
     * If not <tt>null</tt>, implies that the value is {@link Parseable}.
     */
    Parser<T> getParser();

    /**
     * The {@link EncoderDecoder}, if any.
     * 
     * <p>
     * If not <tt>null</tt>, implies that the value is {@link Encodable}.
     */
    EncoderDecoder<T> getEncoderDecoder();

    /**
     * The {@link DefaultsProvider}, if any.
     * 
     * <p>
     * If not <tt>null</tt>, implies that the value has (or may have) a default.
     */
    DefaultsProvider<T> getDefaultsProvider();

    /**
     * Whether the value is {@link Immutable}.
     */
    boolean isImmutable();

    /**
     * Whether the value has {@link EqualByContent equal by content} semantics.
     * 
     * <p>
     * If so, then it must implement <tt>equals(Object)</tt> and <tt>hashCode()</tt> consistently.
     * Examples in the Java language that do this are {@link String} and {@link BigDecimal}, for example.
     */
    boolean isEqualByContent();

}

// Copyright (c) Naked Objects Group Ltd.
