package org.nakedobjects.plugins.xml.objectstore.internal.data;

/**
 * A very simple NakedObject classes - contains only a single association
 */
public class Role {
    public String name;
    public Person person;

    public void setPerson(final Person v) {
        person = v;
    }

    public Person getPerson() {
        return person;
    }

    public void modifyPerson(final Person person) {
        setPerson(person);
    }

    public void clearPerson(final Person person) {
        setPerson(null);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
// Copyright (c) Naked Objects Group Ltd.
