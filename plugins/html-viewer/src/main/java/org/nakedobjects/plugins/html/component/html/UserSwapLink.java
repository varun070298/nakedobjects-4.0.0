/**
 * 
 */
package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.plugins.html.component.Component;


final class UserSwapLink implements Component {
    private final String name;

    UserSwapLink(final String name) {
        this.name = name;
    }

    public void write(final PrintWriter writer) {
        writer.print("<a class=\"user\" href=\"setuser.app?name=");
        writer.print(name);
        writer.print("\" title=\"Change user to " + name);
        writer.print("\">");
        writer.print(name);
        writer.println("</a>");
    }
}
