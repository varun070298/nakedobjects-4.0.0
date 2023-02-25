package org.nakedobjects.runtime.system.internal.console;

public interface ServerConsole {

    void close();

    void init(Server server);

    void log();

    void log(String message);
}

// Copyright (c) Naked Objects Group Ltd.
