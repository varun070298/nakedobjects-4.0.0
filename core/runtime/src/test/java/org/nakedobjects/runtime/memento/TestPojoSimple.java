package org.nakedobjects.runtime.memento;

import org.nakedobjects.runtime.testsystem.TestPojo;


public class TestPojoSimple extends TestPojo {
    private String name;
    private  TestPojoSimple object;
    
    public TestPojoSimple() {}

    public TestPojoSimple(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public TestPojoSimple getObject(){
        return object;
    }

}

// Copyright (c) Naked Objects Group Ltd.
