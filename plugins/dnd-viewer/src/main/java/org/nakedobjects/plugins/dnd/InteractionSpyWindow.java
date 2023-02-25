package org.nakedobjects.plugins.dnd;

public interface InteractionSpyWindow {

    void close();

    void display(int event, String label[][], String[] trace, int traceIndex);

    void open();
}

// Copyright (c) Naked Objects Group Ltd.
