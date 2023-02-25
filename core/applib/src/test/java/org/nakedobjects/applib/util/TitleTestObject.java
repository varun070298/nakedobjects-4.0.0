package org.nakedobjects.applib.util;

class TitleTestObject {

    private String title;

    public TitleTestObject() {}

    public TitleTestObject(final String title) {
        this.title = title;
    }

    public void setupTitle(final String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
    
    @Override
    public String toString() {
        return "xxx";
    }
}

// Copyright (c) Naked Objects Group Ltd.
