package org.nakedobjects.applib.adapters;

public abstract class AbstractValueSemanticsProvider<T> implements ValueSemanticsProvider<T> {

    private boolean immutable;
    private boolean equalByContent;

    /**
     * Defaults {@link #isImmutable()} to <tt>true</tt> and {@link #isEqualByContent()} to <tt>true</tt>
     * also.
     */
    public AbstractValueSemanticsProvider() {
        this(true, true);
    }

    public AbstractValueSemanticsProvider(final boolean immutable, final boolean equalByContent) {
        this.immutable = immutable;
        this.equalByContent = equalByContent;
    }

    public EncoderDecoder<T> getEncoderDecoder() {
        return (EncoderDecoder<T>) (this instanceof EncoderDecoder ? this : null);
    }

    public Parser<T> getParser() {
        return (Parser<T>) (this instanceof Parser ? this : null);
    }

    public DefaultsProvider<T> getDefaultsProvider() {
        return (DefaultsProvider<T>) (this instanceof DefaultsProvider ? this : null);
    }

    /**
     * Defaults to <tt>true</tt> if no-arg constructor is used.
     */
    public boolean isEqualByContent() {
        return equalByContent;
    }

    /**
     * Defaults to <tt>true</tt> if no-arg constructor is used.
     */
    public boolean isImmutable() {
        return immutable;
    }

}

// Copyright (c) Naked Objects Group Ltd.
