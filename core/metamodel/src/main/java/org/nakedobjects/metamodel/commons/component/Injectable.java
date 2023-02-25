package org.nakedobjects.metamodel.commons.component;

public interface Injectable {

    /**
     * Will inject itself into the candidate if the candidate implements the corresponding <tt>*Aware</tt>
     * type.
     */
    void injectInto(Object candidate);

}


// Copyright (c) Naked Objects Group Ltd.
