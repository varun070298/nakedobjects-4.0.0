package org.nakedobjects.applib.adapters;

/**
 * Provides a mechanism for providing a default value for an object.
 * 
 * <p>
 * This interface is used in two complementary ways:
 * <ul>
 * <li> As one option, it allows objects to take control of their own default values, by implementing
 * directly. However, the instance is used as a factory for itself. The framework will instantiate an
 * instance, invoke the appropriate method method, and use the returned object. The instantiated instance
 * itself will be discarded.</li>
 * <li> Alternatively, an implementor of this interface can be nominated in the
 * {@link org.nakedobjects.applib.annotations.Defaulted} annotation, allowing a class that needs to have a
 * default to indicate where its default comes from. </li>
 * 
 * <p>
 * Whatever the class that implements this interface, it must also expose either a
 * <tt>public</tt> no-arg constructor, or (for implementations that also are <tt>Facet</tt>s) 
 * a <tt>public</tt> constructor that accepts a single <tt>FacetHolder</tt>.  This constructor allows the 
 * framework to instantiate the object reflectively.
 * 
 * @see Parser
 * @see EncoderDecoder
 * @see ValueSemanticsProvider
 */
public interface DefaultsProvider<T> {

    /**
     * The default, if any (as a pojo).
     */
    T getDefaultValue();

}

// Copyright (c) Naked Objects Group Ltd.
