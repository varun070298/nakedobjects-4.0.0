package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.ComponentAbstract;


class TextBlock extends ComponentAbstract {
    StringBuffer buffer = new StringBuffer();

    public TextBlock(final String text) {
        append(text);
    }

    public TextBlock() {}

    public void append(final String string) {
        buffer.append(string);
    }

    public void appendBold(final String string) {
        buffer.append("<b>");
        buffer.append(string);
        buffer.append("</b>");
    }

    public void write(final PrintWriter writer) {
        writeTag(writer, "p class=\"unknown\"");
        writer.print(buffer.toString());
        writer.println("</p>");
    }

}

// Copyright (c) Naked Objects Group Ltd.
