package org.nakedobjects.plugins.hibernate.objectstore.tools.internal;

import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


/**
 * Association from one class to another (one to one, many to one, many to many) which is mapped as "inverse".
 */
public class Association {
    private final PersistentNakedClass persistentClass;
    private final NakedObjectAssociation field;
    private final boolean inverse;

    public Association(final PersistentNakedClass persistentClass, final NakedObjectAssociation field, final boolean inverse) {
        this.persistentClass = persistentClass;
        this.field = field;
        this.inverse = inverse;
    }

    public NakedObjectAssociation getField() {
        return field;
    }

    public boolean isInverse() {
        return inverse;
    }

    public PersistentNakedClass getPersistentClass() {
        return persistentClass;
    }
}
// Copyright (c) Naked Objects Group Ltd.
