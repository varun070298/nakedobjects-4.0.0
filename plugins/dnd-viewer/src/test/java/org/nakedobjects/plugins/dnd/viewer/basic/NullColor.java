package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.viewer.drawing.Color;


public class NullColor implements Color {

    public Color brighter() {
        return null;
    }

    public Color darker() {
        return null;
    }

    public String getName() {
        return null;
    }
}

// Copyright (c) Naked Objects Group Ltd.
