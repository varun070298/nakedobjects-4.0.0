package org.nakedobjects.runtime.system.internal.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.system.NakedObjectsSystem;


public class SocketServerMonitor extends AbstractServerMonitor {
    private static final int DEFAULT_PORT = 8009;
    private static final String PORT = ConfigurationConstants.ROOT + "monitor.telnet.port";

    private final MonitorListenerImpl monitor = new MonitorListenerImpl();
    private NakedObjectsSystem system;

    @Override
    protected int getPort() {
        return NakedObjectsContext.getConfiguration().getInteger(PORT, DEFAULT_PORT);
    }

    @Override
    protected boolean handleRequest(final PrintWriter writer, final String request) throws IOException {
        String query = URLDecoder.decode(request, "UTF-8");

        if (query.equalsIgnoreCase("bye")) {
            writer.println("Disconnecting...");
            return false;
        } else if (query.equalsIgnoreCase("shutdown")) {
            writer.println("Shutting down system...");
            system.shutdown();
            System.exit(0);
            return false;
        }

        monitor.writeTextPage(query, writer);
        writer.print("shutdown bye]\n#");
        writer.flush();
        return true;
    }

    @Override
    public void setTarget(final NakedObjectsSystem system) {
        this.system = system;
    }
}

// Copyright (c) Naked Objects Group Ltd.
