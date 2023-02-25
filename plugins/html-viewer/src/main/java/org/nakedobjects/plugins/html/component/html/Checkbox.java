/**
 * 
 */
package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Component;


final class Checkbox implements Component {
    private final boolean set;

    private final boolean editable;

    Checkbox(final boolean set, final boolean editable) {
        this.set = set;
        this.editable = editable;
    }

    public void write(final PrintWriter writer) {
        writer.print("<input class=\"value\" type=\"checkbox\"");
        if (set) {
            writer.print(" checked");
        }
        if (!editable) {
            writer.print(" disabled");
        }
        writer.println("/>");
    }
}
