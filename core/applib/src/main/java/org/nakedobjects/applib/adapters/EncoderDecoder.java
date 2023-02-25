package org.nakedobjects.applib.adapters;

/**
 * Provides a mechanism for encoding/decoding objects.
 * 
 * <p>
 * This interface is used in two complementary ways:
 * <ul>
 * <li> As one option, it allows objects to take control of their own encoding/decoding, by implementing
 * directly. However, the instance is used as a factory for itself. The framework will instantiate an
 * instance, invoke the appropriate method method, and use the returned object. The instantiated instance
 * itself will be discarded.</li>
 * <li> Alternatively, an implementor of this interface can be nominated in the
 * {@link org.nakedobjects.applib.annotation.Encodable} annotation, allowing a class that needs to be
 * encodeable to indicate how it can be encoded/decoded. </li>
 * 
 * <p>
 * Whatever the class that implements this interface, it must also expose either a
 * <tt>public</tt> no-arg constructor, or (for implementations that also are <tt>Facet</tt>s) 
 * a <tt>public</tt> constructor that accepts a single <tt>FacetHolder</tt>.  This constructor allows the 
 * framework to instantiate the object reflectively.
 * 
 * @see Parser
 * @see DefaultsProvider
 * @see ValueSemanticsProvider
 */
public interface EncoderDecoder<T> {

    /**
     * Returns the provided object as an encoded string.
     * 
     * <p>
     * Even if the class is self-encodeable, note that this method is always called on a new instance of the
     * object created via the no-arg constructor. That is, the object shouldn't encode itself, it should
     * encode the object provided to it.
     */
    String toEncodedString(T toEncode);

    /**
     * Converts an encoded string to an instance of the object.
     * 
     * <p>
     * Note that here the implementing class is acting as a factory for itself.
     * 
     * @see #toEncodedString(% toEncode)
     */
    T fromEncodedString(String encodedString);

}

// Copyright (c) Naked Objects Group Ltd.
