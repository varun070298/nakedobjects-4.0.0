package org.nakedobjects.plugins.dnd.viewer.undo;

public interface Command {
    String getDescription();

    void undo();

    void execute();

    String getName();
}
// Copyright (c) Naked Objects Group Ltd.
