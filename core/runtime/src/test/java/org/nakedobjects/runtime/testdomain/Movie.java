package org.nakedobjects.runtime.testdomain;

import java.util.Vector;


public class Movie {
    private Person director;
    private String name;
    private final Vector roles = new Vector();

    public void addToRoles(final Role role) {
        roles.addElement(role);
    }

    public Person getDirector() {
        return director;
    }

    public String getName() {
        return name;
    }

    public Vector getRoles() {
        return roles;
    }

    public void removeFromRoles(final Role role) {
        roles.removeElement(role);
    }

    public void setDirector(final Person director) {
        this.director = director;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String title() {
        return name;
    }

    /*
     * public static void aboutActionFindMovie(ActionAbout about, String name, Person director, Person actor) {
     * about.setParameter(0, "Name"); about.setParameter(1, "Director"); about.setParameter(2, "Actor"); }
     * 
     */
}
// Copyright (c) Naked Objects Group Ltd.
