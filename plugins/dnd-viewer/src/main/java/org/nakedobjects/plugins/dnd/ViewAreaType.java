package org.nakedobjects.plugins.dnd;

public class ViewAreaType {
    public static final ViewAreaType VIEW = new ViewAreaType("View");
    public static final ViewAreaType CONTENT = new ViewAreaType("Content");
    public static final ViewAreaType INTERNAL = new ViewAreaType("Internal");
    private final String name;

    public ViewAreaType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
// Copyright (c) Naked Objects Group Ltd.
