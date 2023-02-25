package org.nakedobjects.runtime.system.internal.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class MonitorListenerImpl implements MonitorListener {
    private final List<MonitorEvent> requests = new ArrayList<MonitorEvent>();

    public MonitorListenerImpl() {
        org.nakedobjects.runtime.system.internal.monitor.Monitor.addListener(this);
    }

    public void postEvent(final MonitorEvent event) {
        // TODO use a stack of limited size so we have FIFO list
        if (requests.size() > 50) {
            requests.remove(0);
        }
        requests.add(event);
    }

    public void writeHtmlPage(final String section, final PrintWriter writer) throws IOException {
        Assert.assertNotNull(section);
        Assert.assertNotNull(writer);
        final String sectionName = section.equals("") ? "Overview" : section;

        writer.println("<HTML><HEAD><TITLE>NOF System Monitor - " + sectionName + "</TITLE></HEAD>");
        writer.println("<BODY>");

        writer.println("<h1>" + sectionName + "</h1>");

        final StringBuffer navigation = new StringBuffer("<p>");
//        final String[] options = target.debugSectionNames();
        DebugInfo[] infos = NakedObjectsContext.debugSystem();
        for (int i = 0; i < infos.length; i++) {
            String name = infos[i].debugTitle();
            appendNavigationLink(navigation, name, i > 0);
        }
        appendNavigationLink(navigation, "Requests", true);
        navigation.append("</p>");

        writer.println(navigation);
        writer.println("<pre>");
        if (sectionName.equals("Requests")) {
            int i = 1;
            for(MonitorEvent event: requests) {
                writer.print("<a href=\"monitor?request=" + event.getSerialId() +"\">");
                writer.print(i++ + ". " + event);
                writer.println("</a>");
            }
        } else if (sectionName.startsWith("request=")) {
            int requestId = Integer.valueOf(sectionName.substring("request=".length())).intValue();
            for (MonitorEvent request : requests) {
                if (request.getSerialId() == requestId) {
                    writer.println(request.getDebug());
                    break;
                }
            }
        } else {
            for (int i = 0; i < infos.length; i++) {
                if(infos[i].debugTitle().equals(sectionName)) {
                    // TODO use an HTML debug string
                    final DebugString debug = new DebugString();
                    infos[i].debugData(debug);
                    writer.println(debug.toString());          
                    break;
                }
            }
        }
        writer.println("</pre>");

        writer.println(navigation);
        writer.println("</BODY></HTML>");
    }

    private void appendNavigationLink(final StringBuffer navigation, String name, boolean appendDivider)
            throws UnsupportedEncodingException {
        if (appendDivider) {
            navigation.append(" | ");
        }
        navigation.append("<a href=\"monitor?");
        navigation.append(URLEncoder.encode(name, "UTF-8"));
        navigation.append("\">");
        navigation.append(name);
        navigation.append("</a>");
    }

    public void writeTextPage(final String section, final PrintWriter writer) throws IOException {
        Assert.assertNotNull(section);
        Assert.assertNotNull(writer);
        final String sectionName = section.equals("") ? "Overview" : section;

        writer.println(sectionName);

        DebugInfo[] infos = NakedObjectsContext.debugSystem();
        if (sectionName.equals("Events")) {
            int i = 1;
            for(MonitorEvent event: requests) {
                writer.println(i++ + ". " + event);
            }
            // TODO add clause for request
        } else {
            for (int i = 0; i < infos.length; i++) {
                if(infos[i].debugTitle().equals(sectionName)) {           
                    final DebugString debug = new DebugString();
                    infos[i].debugData(debug);
                    writer.println(debug.toString());         
                }
            }
        }

        writer.print("[Options: ");
//        final String[] options = target.debugSectionNames();
        for (int i = 0; i < infos.length; i++) {
            writer.print(infos[i].debugTitle() + " ");
        }
        //writer.println();
    }
/*
    public void setTarget(final DebugSelection debugInfo2) {
        target = debugInfo2;
    }
    */
}

// Copyright (c) Naked Objects Group Ltd.
