package org.nakedobjects.runtime.system;

import java.util.Vector;


public class JavaObjectWithVector {

    public static String nameMethod() {
        return "my about";
    }

    JavaReferencedObject added;
    boolean available;
    Vector collection = new Vector();
    JavaReferencedObject removed;
    boolean valid = false;
    boolean visible = false;

    public void addToMethod(final JavaReferencedObject person) {
        added = person;
    }

    public String availableMethod(final JavaReferencedObject person) {
        return available ? null : "not available";
    }

    public Vector getMethod() {
        return collection;
    }

    public void removeFromMethod(final JavaReferencedObject person) {
        removed = person;
    }

    public String validMethod(final JavaReferencedObject person) {
        return valid ? null : "not valid";
    }

    public boolean hideMethod(final JavaReferencedObject person) {
        return !visible;
    }
}
// Copyright (c) Naked Objects Group Ltd.
