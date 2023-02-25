package org.nakedobjects.application.valueholder;

public class Case {
    public static final Case INSENSITIVE = new Case("insensitive");

    public static final Case SENSITIVE = new Case("sensitive");
    private String name;

    private Case(final String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
// Copyright (c) Naked Objects Group Ltd.
