package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.ComponentAbstract;


class Html extends ComponentAbstract {

    private final String code;

    public Html(final String code) {
        this.code = code;
    }

    public void write(final PrintWriter writer) {
        writer.print(code);
    }
}

// Copyright (c) Naked Objects Group Ltd.
