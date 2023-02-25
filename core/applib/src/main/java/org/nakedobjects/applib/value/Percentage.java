package org.nakedobjects.applib.value;

import org.nakedobjects.applib.annotation.Value;


@Value(semanticsProviderName = "org.nakedobjects.metamodel.value.PercentageValueSemanticsProvider")
public class Percentage extends Magnitude {
    private static final long serialVersionUID = 1L;
    private final float value;

    public Percentage(final float value) {
        this.value = value;
    }

    public Percentage add(final float value) {
        return new Percentage((floatValue() + value));
    }

    public Percentage add(final Percentage value) {
        return add(value.floatValue());
    }

    /**
     * Returns this value as an double.
     */
    public double doubleValue() {
        return value;
    }

    /**
     * Returns this value as an float.
     */
    public float floatValue() {
        return value;
    }

    /**
     * Returns this value as an int.
     */
    public int intValue() {
        return (int) value;
    }

    /**
     */
    @Override
    public boolean isEqualTo(final Magnitude magnitude) {
        if (magnitude instanceof Percentage) {
            return ((Percentage) magnitude).value == value;
        } else {
            throw new IllegalArgumentException("Parameter must be of type WholeNumber");
        }
    }

    @Override
    public boolean isLessThan(final Magnitude magnitude) {
        if (magnitude instanceof Percentage) {
            return value < ((Percentage) magnitude).value;
        } else {
            throw new IllegalArgumentException("Parameter must be of type WholeNumber");
        }
    }

    /**
     * Returns this value as an long.
     */
    public long longValue() {
        return (long) value;
    }

    public Percentage multiply(final float value) {
        return new Percentage((floatValue() * value));
    }

    /**
     * Returns this value as an short.
     */
    public short shortValue() {
        return (short) value;
    }

    public Percentage subtract(final float value) {
        return add(-value);
    }

    public Percentage subtract(final Percentage value) {
        return add(-value.floatValue());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        return other.getClass() == this.getClass() && equals((Percentage) other);
    }

    public boolean equals(final Percentage other) {
        return value == other.value;
    }

    @Override
    public int hashCode() {
        // multiply by 100 just in case the percentage is being stored as 0.0 to 1.0
        return (int) (floatValue() * 100);
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
// Copyright (c) Naked Objects Group Ltd.
