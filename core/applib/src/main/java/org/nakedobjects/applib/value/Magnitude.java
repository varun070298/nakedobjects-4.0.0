package org.nakedobjects.applib.value;

import java.io.Serializable;


public abstract class Magnitude implements Serializable {
    private static final long serialVersionUID = 1L;

    public boolean isBetween(final Magnitude minMagnitude, final Magnitude maxMagnitude) {
        return isGreaterThanOrEqualTo(minMagnitude) && isLessThanOrEqualTo(maxMagnitude);
    }

    public abstract boolean isEqualTo(final Magnitude magnitude);

    public boolean isGreaterThan(final Magnitude magnitude) {
        return magnitude.isLessThan(this);
    }

    public boolean isGreaterThanOrEqualTo(final Magnitude magnitude) {
        return !isLessThan(magnitude);
    }

    public abstract boolean isLessThan(final Magnitude magnitude);

    public boolean isLessThanOrEqualTo(final Magnitude magnitude) {
        return !isGreaterThan(magnitude);
    }

    public Magnitude max(final Magnitude magnitude) {
        return isGreaterThan(magnitude) ? this : magnitude;
    }

    public Magnitude min(final Magnitude magnitude) {
        return isLessThan(magnitude) ? this : magnitude;
    }

}
// Copyright (c) Naked Objects Group Ltd.
