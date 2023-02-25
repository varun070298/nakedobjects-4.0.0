package org.nakedobjects.runtime.testdomain;

public class Role {
    private Person actor;
    private String name;

    public Person getActor() {
        return actor;
    }

    public String getName() {
        return name;
    }

    public void setActor(final Person actor) {
        this.actor = actor;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String title() {
        return name;
    }
}
// Copyright (c) Naked Objects Group Ltd.
