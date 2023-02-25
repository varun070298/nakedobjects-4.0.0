package org.nakedobjects.plugins.hibernate.objectstore.testdomain;

import java.util.List;


public class OneToMany {
    private List<BiDirectional> many;

    public void addToMany(final BiDirectional many) {
        many.modifyOneToMany(this);
    }

    public void removeFromMany(final BiDirectional many) {
        many.clearOneToMany(this);
    }

    public List<BiDirectional> getMany() {
        return many;
    }

    public void setMany(final List<BiDirectional> many) {
        this.many = many;
    }
}
// Copyright (c) Naked Objects Group Ltd.
