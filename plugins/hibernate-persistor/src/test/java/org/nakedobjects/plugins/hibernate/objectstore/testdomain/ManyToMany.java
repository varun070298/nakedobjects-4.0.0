package org.nakedobjects.plugins.hibernate.objectstore.testdomain;

import java.util.List;


public class ManyToMany {
    private List<BiDirectional> many;

    public void addToMany(final BiDirectional other) {
        other.addToManyToMany(this);
    }

    public void removeFromMany(final BiDirectional other) {
        other.removeFromManyToMany(this);
    }

    public List<BiDirectional> getMany() {
        return many;
    }

    public void setMany(final List<BiDirectional> many) {
        this.many = many;
    }
}

// Copyright (c) Naked Objects Group Ltd.
