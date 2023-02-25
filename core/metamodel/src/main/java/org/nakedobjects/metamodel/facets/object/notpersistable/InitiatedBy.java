package org.nakedobjects.metamodel.facets.object.notpersistable;

import org.nakedobjects.metamodel.facets.EnumerationAbstract;


public final class InitiatedBy extends EnumerationAbstract {

    public static InitiatedBy USER = new InitiatedBy(0, "USER", "User");
    public static InitiatedBy USER_OR_PROGRAM = new InitiatedBy(1, "USER_OR_PROGRAM", "User or Program");

    private InitiatedBy(final int num, final String nameInCode, final String friendlyName) {
        super(num, nameInCode, friendlyName);
    }

}

// Copyright (c) Naked Objects Group Ltd.
