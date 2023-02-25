package org.nakedobjects.plugins.hibernate.objectstore.testdomain;

import java.util.List;


public class BiDirectional {
    public static String inverseOneToMany = "Many";

    private List<ManyToMany> manyToMany;
    private OneToMany oneToMany;
    private OneToMany secondOneToMany;
    private OneToOne oneToOne;

    public static String fieldOrder() {
        return "oneToOne, oneToMany, manyToMany, secondOneToMany";
    }

    public void addToManyToMany(final ManyToMany other) {
        getManyToMany().add(other);
        other.getMany().add(this);
    }

    public void removeFromManyToMany(final ManyToMany other) {
        getManyToMany().add(other);
        other.getMany().add(this);
    }

    public List<ManyToMany> getManyToMany() {
        return manyToMany;
    }

    public void setManyToMany(final List<ManyToMany> manyToMany) {
        this.manyToMany = manyToMany;
    }

    public void modifyOneToMany(final OneToMany oneToMany) {
        setOneToMany(oneToMany);
        oneToMany.getMany().add(this);
    }

    public void clearOneToMany(final OneToMany oneToMany) {
        setOneToMany(null);
        oneToMany.getMany().remove(this);
    }

    public OneToMany getOneToMany() {
        return oneToMany;
    }

    public void setOneToMany(final OneToMany oneToMany) {
        this.oneToMany = oneToMany;
    }

    public void modifySecondOneToMany(final OneToMany oneToMany) {
        setSecondOneToMany(oneToMany);
    }

    public void clearSecondOneToMany(final OneToMany oneToMany) {
        setSecondOneToMany(null);
    }

    public OneToMany getSecondOneToMany() {
        return secondOneToMany;
    }

    public void setSecondOneToMany(final OneToMany oneToMany) {
        this.secondOneToMany = oneToMany;
    }

    public void modifyOneToOne(final OneToOne oneToOne) {
        setOneToOne(oneToOne);
        oneToOne.setOne(this);
    }

    public void clearOneToOne(final OneToOne oneToOne) {
        setOneToOne(null);
        oneToOne.setOne(null);
    }

    public OneToOne getOneToOne() {
        return oneToOne;
    }

    public void setOneToOne(final OneToOne oneToOne) {
        this.oneToOne = oneToOne;
    }
}
// Copyright (c) Naked Objects Group Ltd.
