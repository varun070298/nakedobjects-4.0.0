package org.nakedobjects.runtime.system;

public class JavaReferencedObject {
    private static int next;
    private final int id = next++;

    public JavaReferencedObject() {
        super();
    }

    @Override
    public String toString() {
        return "JavaReferencedObject#" + id;
    }

}
// Copyright (c) Naked Objects Group Ltd.
