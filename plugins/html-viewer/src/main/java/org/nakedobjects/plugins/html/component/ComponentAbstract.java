package org.nakedobjects.plugins.html.component;

import java.io.PrintWriter;



public abstract class ComponentAbstract implements Component {
    private String id;
    private String cls;

    public void setClass(final String cls) {
        this.cls = cls;
    }

    public void setId(final String id) {
        this.id = id;
    }

    protected void writeTag(final PrintWriter writer, final String tagName) {
        tag(writer, tagName);
        writer.print(">");
    }

    private void tag(final PrintWriter writer, final String tagName) {
        writer.print("<");
        writer.print(tagName);
        if (id != null) {
            writer.print(" id=\"");
            writer.print(id);
            writer.print("\"");
        }
        if (cls != null) {
            writer.print(" class=\"");
            writer.print(cls);
            writer.print("\"");
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
