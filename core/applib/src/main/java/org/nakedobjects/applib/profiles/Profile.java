package org.nakedobjects.applib.profiles;


public interface Profile {

    void addToOptions(String name, String value);

    Perspective newPerspective(String name);

    void addToPerspectives(Perspective perspective);

    Perspective getPerspective(String name);

}


// Copyright (c) Naked Objects Group Ltd.
