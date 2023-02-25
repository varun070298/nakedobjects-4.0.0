package org.nakedobjects.application.valueholder;

import org.nakedobjects.application.BusinessObject;
import org.nakedobjects.application.Title;
import org.nakedobjects.application.value.ValueParseException;


public class Password extends BusinessValueHolder {
    private static final long serialVersionUID = 1L;
    private int maximumLength;
    private String password;

    public Password() {
        this(null);
    }

    public Password(final int maximumLength) {
        this(null, maximumLength);
    }

    public Password(final BusinessObject parent) {
        super(parent);
        password = "";
    }

    public Password(final BusinessObject parent, final int maximumLength) {
        this(parent);
        this.maximumLength = maximumLength;
    }

    public String asEncodedString() {
        return isEmpty() ? "NULL" : password;
    }

    public void clear() {
        setValuesInternal(null, true);
    }

    public void copyObject(final BusinessValueHolder object) {
        if (object == null) {
            this.clear();
        } else if (!(object instanceof Password)) {
            throw new IllegalArgumentException("Can only copy the value of a Password object");
        } else {
            setValue(((Password) object).password);
        }
    }

    public int getMaximumLength() {
        return maximumLength;
    }

    public boolean isEmpty() {
        ensureAtLeastPartResolved();
        return password == null;
    }

    public boolean isSameAs(final BusinessValueHolder object) {
        ensureAtLeastPartResolved();
        if (object instanceof Password) {
            String pswd = ((Password) object).password;
            if (pswd == null && password == null) {
                return true;
            }
            if (password != null && password.equals(pswd)) {
                return true;
            }
        }
        return false;
    }

    public void parseUserEntry(final String text) throws ValueParseException {
        setValue(text);
    }

    public void reset() {
        setValuesInternal(null, true);
    }

    public void restoreFromEncodedString(final String data) {
        if (data == null || data.equals("NULL")) {
            setValuesInternal(null, false);
        } else {
            setValuesInternal(data, false);
        }
    }

    public void setMaximumLength(final int maximumLength) {
        this.maximumLength = maximumLength;
    }

    public void setValue(final String password) {
        if (password == null) {
            clear();
        } else {
            setValuesInternal(password, false);
        }
    }

    private void setValuesInternal(final String value, final boolean notify) {
        if (notify) {
            ensureAtLeastPartResolved();
        }
        this.password = value;
        if (notify) {
            parentChanged();
        }
    }

    public String stringValue() {
        ensureAtLeastPartResolved();
        return password;
    }

    public Title title() {
        ensureAtLeastPartResolved();
        return new Title(password);
    }
}
// Copyright (c) Naked Objects Group Ltd.
