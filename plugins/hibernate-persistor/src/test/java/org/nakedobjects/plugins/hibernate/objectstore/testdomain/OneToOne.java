package org.nakedobjects.plugins.hibernate.objectstore.testdomain;

public class OneToOne {
    private BiDirectional other;

    public void modifyOne(final BiDirectional one) {
        one.modifyOneToOne(this);
    }

    public void clearOne(final BiDirectional one) {
        one.clearOneToOne(this);
    }

    public BiDirectional getOne() {
        return other;
    }

    public void setOne(final BiDirectional other) {
        this.other = other;
    }
}
// Copyright (c) Naked Objects Group Ltd.
