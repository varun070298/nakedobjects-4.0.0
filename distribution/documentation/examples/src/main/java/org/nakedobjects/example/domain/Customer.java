package org.nakedobjects.example.domain;

import org.nakedobjects.applib.AbstractDomainObject;

public class Customer extends AbstractDomainObject {
    private String name;
    
    public String title() {
        return getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}


// Copyright (c) Naked Objects Group Ltd.
