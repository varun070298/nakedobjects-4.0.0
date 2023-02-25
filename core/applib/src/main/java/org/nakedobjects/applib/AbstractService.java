package org.nakedobjects.applib;

public abstract class AbstractService extends AbstractContainedObject {

    public String getId() {
        return getClass().getName();
    }

    protected String getClassName() {
        return getClass().getName();
    }

}

// Copyright (c) Naked Objects Group Ltd.
