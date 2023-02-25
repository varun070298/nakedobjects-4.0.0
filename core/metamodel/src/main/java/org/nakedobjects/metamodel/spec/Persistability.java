package org.nakedobjects.metamodel.spec;

public enum Persistability {
    /**
     * Marks a class as being persistable, but only by under application program control.
     */
    PROGRAM_PERSISTABLE("Program Persistable", true),
    /**
     * Marks a class as transient - such an object cannot be persisted.
     */
    TRANSIENT("Transient", false),
    /**
     * Marks a class as being persistable by the user (or under application program control).
     */
    USER_PERSISTABLE("User Persistable", true);

    private final String name;
    private final boolean persistable;

    private Persistability(final String name, final boolean persistable) {
        this.name = name;
        this.persistable = persistable;
    }
    
    public boolean isPersistable() {
        return persistable;
    }

    @Override
    public String toString() {
        return name;
    }
}
// Copyright (c) Naked Objects Group Ltd.
