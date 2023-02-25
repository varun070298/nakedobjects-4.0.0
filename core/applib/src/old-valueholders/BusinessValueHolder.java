package org.nakedobjects.application.valueholder;

import org.nakedobjects.application.BusinessObject;
import org.nakedobjects.application.Title;
import org.nakedobjects.application.TitledObject;
import org.nakedobjects.application.value.ValueParseException;


public abstract class BusinessValueHolder implements TitledObject {

    protected BusinessValueHolder(final BusinessObject parent) {
        this.parent = parent;
    }

    /** The parent which owns this value */
    private BusinessObject parent;

    public BusinessObject getParent() {
        return parent;
    }

    /**
     * Invokes <code>objectChanged()</code> on parent, provided that parent has been specified (is not
     * <code>nothing</code>).
     */
    protected void parentChanged() {
        if (this.getParent() == null) {
            return;
        }
        this.getParent().objectChanged();
    }

    protected void ensureAtLeastPartResolved() {
    }

    /** By default all values are changeable by the user */
    public boolean userChangeable() {
        return true;
    }

    public abstract boolean isEmpty();

    public abstract boolean isSameAs(final BusinessValueHolder object);

    public String titleString() {
        return title().toString();
    }

    public abstract Title title();

    /**
     * Returns a string representation of this object.
     * <p>
     * The specification of this string representation is not fixed, but, at the time of writing, consists of
     * <i>title [shortNakedClassName] </i>
     * </p>
     * 
     * @return string representation of object.
     */
    public String toString() {
        return titleString(); // + " [" + this.getClass().getName() + "]";
    }

    public Object getValue() {
        return this;
    }

    public abstract void parseUserEntry(final String text) throws ValueParseException;

    public abstract void restoreFromEncodedString(final String data);

    public abstract String asEncodedString();

    public abstract void copyObject(final BusinessValueHolder object);

    public abstract void clear();
}
// Copyright (c) Naked Objects Group Ltd.
