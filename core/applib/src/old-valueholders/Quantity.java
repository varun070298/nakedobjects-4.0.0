package org.nakedobjects.applib.value;

/**
 * Color is simple numerical representation of a color using the amount of red, green and blue (RGB)
 * components. Where there is no basic colors (RGB all equal 0) then you get black; where each color is at
 * maximum (RGB all equal 255) you get white.
 */
public class Quantity extends Magnitude {
    private static final long serialVersionUID = 1L;
    private final int quantity;

    public Quantity(final int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive: " + quantity);
        }
        this.quantity = quantity;
    }

    public int intValue() {
        return quantity;
    }

    /**
     * returns true if the number of this object has the same value as the specified number
     */
    public boolean isEqualTo(final Magnitude number) {
        if (number instanceof Quantity) {
            return ((Quantity) number).quantity == quantity;
        } else {
            throw new IllegalArgumentException("Parameter must be of type Quantity");
        }
    }

    /**
     * Returns true if this value is less than the specified value.
     */
    public boolean isLessThan(final Magnitude value) {
        if (value instanceof Quantity) {
            return quantity < ((Quantity) value).quantity;
        } else {
            throw new IllegalArgumentException("Parameter must be of type Quantity");
        }
    }

    public String title() {
        return Integer.toString(quantity);
    }
}
// Copyright (c) Naked Objects Group Ltd.
