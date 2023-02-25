package org.nakedobjects.runtime.testdomain;

import java.util.Date;


public class Person {
    private Date date;
    private String name;

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String title() {
        return name;
    }
}
// Copyright (c) Naked Objects Group Ltd.
