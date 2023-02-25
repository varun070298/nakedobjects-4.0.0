package org.nakedobjects.plugins.html.component;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



public class ComponentComposite implements Component {
    private final List<Component> components = new ArrayList<Component>();

    public void write(final PrintWriter writer) {
        writeBefore(writer);
        for(Component component: components) {
            write(writer, component);
        }
        writeAfter(writer);
        writer.println();
    }

    protected void write(final PrintWriter writer, final Component component) {
        component.write(writer);
    }

    protected void writeBefore(final PrintWriter writer) {}

    protected void writeAfter(final PrintWriter writer) {}

    public void add(final Component component) {
        components.add(component);
    }

}

// Copyright (c) Naked Objects Group Ltd.
