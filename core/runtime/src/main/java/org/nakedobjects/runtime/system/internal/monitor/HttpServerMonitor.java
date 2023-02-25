package org.nakedobjects.runtime.system.internal.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.system.NakedObjectsSystem;


public class HttpServerMonitor extends AbstractServerMonitor {
    private static final Logger LOG = Logger.getLogger(HttpServerMonitor.class);
   private static final int DEFAULT_PORT = 8081;
    private static final String PORT = ConfigurationConstants.ROOT + "monitor.http.port";
    private final MonitorListenerImpl monitor = new MonitorListenerImpl();

    @Override
    protected int getPort() {
        return NakedObjectsContext.getConfiguration().getInteger(PORT, DEFAULT_PORT);
    }

    @Override
    protected boolean handleRequest(final PrintWriter writer, final String request) throws IOException {
        if (request == null || request.length() == 0) {
            LOG.info("Connection dropped");
            return false;
        }
        final StringTokenizer st = new StringTokenizer(request);
        if (st.countTokens() != 3) {
            httpErrorResponse(writer, 444, "Unparsable input " + request);
            return false;
        }

        final String type = st.nextToken();
        if (!type.equals("GET")) {
            httpErrorResponse(writer, 400, "Invalid method " + type);
            return false;
        }


        String query = st.nextToken();
        query = URLDecoder.decode(query, "UTF-8");

        if (query.equals("/")) {
            query = "/monitor";
        }
        
        if (query.startsWith("/monitor")) {
            query = query.substring("/monitor".length());

            if (query.startsWith("?")) {
                query = query.substring(1);
            }

            monitor.writeHtmlPage(query, writer);
        } else {
            httpErrorResponse(writer, 404, "Failed to find " + query);

            writer.println("[Request: HTTP/1.0 200");
            writer.println("Content-Type: text/html");
            writer.println("]");
        }
        return false;
    }

    private void httpErrorResponse(final PrintWriter writer, final int errorNo, final String response) {
        writer.println("HTTP/1.0 " + errorNo + " " + response);
        writer.println("Content-Type: text/html");
        writer.println("");

        writer.println("<HTML><HEAD><TITLE>Error " + errorNo + " - " + response + "</TITLE></HEAD>");
        writer.println("<BODY><h1>" + errorNo + " - " + response + "</h1>");
        writer.println("</BODY></HTML>");

        writer.flush();
    }

    @Override
    public void setTarget(final NakedObjectsSystem system) {
        //monitor.setTarget(system);
    }

}

// Copyright (c) Naked Objects Group Ltd.
