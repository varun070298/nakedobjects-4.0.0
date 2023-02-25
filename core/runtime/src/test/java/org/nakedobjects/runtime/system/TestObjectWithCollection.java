package org.nakedobjects.runtime.system;

import java.util.Vector;

import org.nakedobjects.runtime.testsystem.TestPojo;


public class TestObjectWithCollection extends TestPojo {
    private final Vector arrayList;
    private final boolean throwException;

    public TestObjectWithCollection(final Vector arrayList, final boolean throwException) {
        this.arrayList = arrayList;
        this.throwException = throwException;
    }

    public Object getList() {
        throwException();
        return arrayList;
    }

    public void addToList(final Object object) {
        throwException();
        arrayList.add(object);
    }

    private void throwException() {
        if (throwException) {
            throw new Error("cause invocation failure");
        }
    }

    public void removeFromList(final Object object) {
        throwException();
        arrayList.remove(object);
    }

    public void clearList() {
        throwException();
        arrayList.clear();
    }

    public String validateAddToList(final Object object) {
        throwException();
        if (object instanceof TestObjectWithCollection) {
            return "can't add this type of object";
        } else {
            return null;
        }
    }

    public String validateRemoveFromList(final Object object) {
        throwException();
        if (object instanceof TestObjectWithCollection) {
            return "can't remove this type of object";
        } else {
            return null;
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
