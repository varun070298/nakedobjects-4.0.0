package org.nakedobjects.plugins.dnd.viewer.view.simple;

import org.nakedobjects.plugins.dnd.NullContent;


public class NullView extends AbstractView {
    public NullView() {
        super(new NullContent(""), null, null);
    }

    @Override
    public String toString() {
        final String name = getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1) + getId();
    }
}
// Copyright (c) Naked Objects Group Ltd.
