package org.nakedobjects.applib.value;

import java.io.Serializable;

import org.nakedobjects.applib.annotation.Value;


@Value(semanticsProviderName = "org.nakedobjects.metamodel.value.PasswordValueSemanticsProvider")
public class Password implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String STARS = "********************";
    private final String password;

    public Password(final String password) {
        this.password = password;
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        return other.getClass() == this.getClass() && equals((Password) other);
    }

    public boolean equals(final Password other) {
        final String otherPassword = other.getPassword();
        if (getPassword() == null && otherPassword == null) {
            return true;
        }
        if (getPassword() == null || otherPassword == null) {
            return false;
        }
        return getPassword().equals(otherPassword);
    }

    @Override
    public int hashCode() {
        return password != null ? password.hashCode() : 0;
    }

    @Override
    public String toString() {
        return STARS.substring(0, Math.min(STARS.length(), password.length()));
    }
}
// Copyright (c) Naked Objects Group Ltd.
