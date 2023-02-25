package org.nakedobjects.application.valueholder;

import org.nakedobjects.application.BusinessObject;


public abstract class Magnitude extends BusinessValueHolder {
    private static final long serialVersionUID = 1L;

    protected Magnitude(final BusinessObject parent) {
        super(parent);
    }

    public boolean isBetween(final Magnitude minMagnitude, final Magnitude maxMagnitude) {
        this.ensureAtLeastPartResolved();
        return isGreaterThanOrEqualTo(minMagnitude) && isLessThanOrEqualTo(maxMagnitude);
    }

    public abstract boolean isEqualTo(final Magnitude magnitude);

    public boolean isGreaterThan(final Magnitude magnitude) {
        this.ensureAtLeastPartResolved();
        return magnitude.isLessThan(this);
    }

    public boolean isGreaterThanOrEqualTo(final Magnitude magnitude) {
        this.ensureAtLeastPartResolved();
        return !isLessThan(magnitude);
    }

    public abstract boolean isLessThan(final Magnitude magnitude);

    public boolean isLessThanOrEqualTo(final Magnitude magnitude) {
        this.ensureAtLeastPartResolved();
        return !isGreaterThan(magnitude);
    }

    public Magnitude max(final Magnitude magnitude) {
        this.ensureAtLeastPartResolved();
        return isGreaterThan(magnitude) ? this : magnitude;
    }

    public Magnitude min(final Magnitude magnitude) {
        this.ensureAtLeastPartResolved();
        return isLessThan(magnitude) ? this : magnitude;
    }

    /**
     * delegates the comparsion to the <code>isEqualTo</code> method if specified object is a
     * <code>Magnitude</code> else returns false.
     * 
     * @see BusinessValueHolder#isSameAs(BusinessValueHolder)
     */
    public final boolean isSameAs(BusinessValueHolder object) {
        this.ensureAtLeastPartResolved();
        if (object instanceof Magnitude) {
            return isEqualTo((Magnitude) object);
        } else {
            return false;
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
