package org.nakedobjects.applib.value;

import org.nakedobjects.applib.annotation.Facets;


/**
 * Color is simple numerical representation of a color using the brightness of red, green and blue (RGB)
 * components.
 * 
 * <p>
 * Where there is no basic colors (RGB all equal 0) then you get black; where each color is at maximum (RGB
 * all equal 255) you get white.
 * 
 * <p>
 * TODO: currently this value type still uses <tt>@Facets</tt> rather than <tt>@Value</tt>.
 */
@Facets(facetFactoryNames = { "org.nakedobjects.metamodel.value.ColorValueTypeFacetFactory" })
public class Color extends Magnitude {
    private static final long serialVersionUID = 1L;
    public final static Color WHITE = new Color(0xffffff);
    public final static Color BLACK = new Color(0);
    private final int color;

    public Color(final int color) {
        this.color = color;
    }

    public int intValue() {
        return color;
    }


    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Color) {
            final Color c = (Color) obj;
            return c.color == color;
        }
        return super.equals(obj);
    }

    /**
     * returns true if the number of this object has the same value as the specified number
     */
    @Override
    public boolean isEqualTo(final Magnitude number) {
        if (number instanceof Color) {
            return ((Color) number).color == color;
        } else {
            throw new IllegalArgumentException("Parameter must be of type Color");
        }
    }

    /**
     * Returns true if this value is less than the specified value.
     */
    @Override
    public boolean isLessThan(final Magnitude value) {
        if (value instanceof Color) {
            return color < ((Color) value).color;
        } else {
            throw new IllegalArgumentException("Parameter must be of type Color");
        }
    }

    public String title() {
        if (color == 0) {
            return "Black";
        } else if (color == 0xffffff) {
            return "White";
        } else {
            return "#" + Integer.toHexString(color).toUpperCase();
        }
    }
    
    public String toString() {
        return "Color: #" + Integer.toHexString(color).toUpperCase();
    }
}
// Copyright (c) Naked Objects Group Ltd.
