package org.nakedobjects.application.valueholder;

import org.nakedobjects.application.BusinessObject;
import org.nakedobjects.application.Title;
import org.nakedobjects.application.value.ValueParseException;


public class Color extends Magnitude {
    private int color;
    private boolean isNull;

    public Color() {
        this(null);
    }

    public Color(final int color) {
        this(null, color);
    }

    public Color(final BusinessObject parent) {
        super(parent);
        clear();
    }

    public Color(final BusinessObject parent, final int color) {
        super(parent);
        setValue(color);
    }

    public void clear() {
        setValuesInternal(0, true, true);
    }

    public void copyObject(final BusinessValueHolder object) {
        if (object == null) {
            this.clear();
        } else if (!(object instanceof Color)) {
            throw new IllegalArgumentException("Can only copy the value of  a Color object");
        } else {
            setValue((Color) object);
        }
    }

    public int intValue() {
        this.ensureAtLeastPartResolved();
        return color;
    }

    public boolean isEmpty() {
        this.ensureAtLeastPartResolved();
        return isNull;
    }

    /**
     * returns true if the number of this object has the same value as the specified number
     */
    public boolean isEqualTo(final Magnitude number) {
        this.ensureAtLeastPartResolved();
        if (number instanceof Color) {
            if (isNull) {
                return number.isEmpty();
            }
            return ((Color) number).color == color;
        } else {
            throw new IllegalArgumentException("Parameter must be of type Color");
        }
    }

    /**
     * Returns true if this value is less than the specified value.
     */
    public boolean isLessThan(final Magnitude value) {
        this.ensureAtLeastPartResolved();
        if (value instanceof Color) {
            return !isNull && !value.isEmpty() && color < ((Color) value).color;
        } else {
            throw new IllegalArgumentException("Parameter must be of type Color");
        }
    }

    public void parseUserEntry(final String text) throws ValueParseException {
        if (text == null || text.trim().equals("")) {
            clear();
        } else {
            try {
                if (text.startsWith("0x")) {
                    setValue(Integer.parseInt(text.substring(2), 16));
                } else if (text.startsWith("#")) {
                    setValue(Integer.parseInt(text.substring(1), 16));
                } else {
                    setValue(Integer.parseInt(text));
                }
            } catch (NumberFormatException e) {
                throw new ValueParseException("Invalid number", e);
            }
        }
    }

    public void reset() {
        setValuesInternal(0, false, true);
    }

    public void restoreFromEncodedString(final String data) {
        if (data == null || data.equals("NULL")) {
            setValuesInternal(0, true, false);
        } else {
            setValuesInternal(Integer.valueOf(data).intValue(), false, false);
        }
    }

    public String asEncodedString() {
        // note: isEmpty does this.ensureAtLeastPartResolved();
        if (isEmpty()) {
            return "NULL";
        } else {
            return String.valueOf(intValue());
        }
    }

    public void setValue(final Color value) {
        if (value.isEmpty()) {
            clear();
        } else {
            setValuesInternal(value.intValue(), value.isNull, true);
        }
    }

    public void setValue(final int color) {
        setValuesInternal(color, false, true);
    }

    private void setValuesInternal(final int color, final boolean isNull, final boolean notify) {
        if (notify) {
            ensureAtLeastPartResolved();
        }
        this.color = color;
        this.isNull = isNull;
        if (notify) {
            parentChanged();
        }
    }

    public Title title() {
        this.ensureAtLeastPartResolved();
        if (color == 0) {
            return new Title("Black");
        } else if (color == 0xffffff) {
            return new Title("White");
        } else {
            return new Title("0x" + Integer.toHexString(color));
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
