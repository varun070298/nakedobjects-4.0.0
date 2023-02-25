package org.nakedobjects.runtime.system;

public class JavaActionTestObject {
    private boolean actionCalled = false;

    public void actionMethod() {
        actionCalled = true;
    }

    public static String nameMethod() {
        return "about for test";
    }

    public boolean invisibleMethod() {
        return true;
    }

    public String validMethod() {
        return "invalid";
    }

    public void actionWithParam(final String str) {}

    public static boolean[] mandatoryMethod(final String str) {
        return new boolean[] { true };
    }

    public static String[] labelMethod(final String str) {
        return new String[] { "label" };
    }

    public boolean actionCalled() {
        return actionCalled;
    }
}
// Copyright (c) Naked Objects Group Ltd.
