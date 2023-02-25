package org.nakedobjects.runtime.system;

public class JavaObjectWithOneToOneAssociations {
    boolean available = false;
    private JavaReferencedObject object;
    boolean valid = false;
    boolean visible = false;

    public String availableReferencedObject(final JavaReferencedObject object) {
        return available ? null : "not available";
    }

    public JavaReferencedObject getReferencedObject() {
        return object;
    }

    public void setReferencedObject(final JavaReferencedObject object) {
        this.object = object;
    }

    public String validReferencedObject(final JavaReferencedObject object) {
        return valid ? null : "not valid";
    }

    public boolean hideReferencedObject(final JavaReferencedObject object) {
        return !visible;
    }
}
// Copyright (c) Naked Objects Group Ltd.
