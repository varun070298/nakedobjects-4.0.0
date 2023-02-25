package org.nakedobjects.runtime.help;

import org.nakedobjects.applib.Identifier;


/**
 * Default implementation that does nothing.
 * 
 */
public abstract class HelpManagerAbstract implements HelpManager {

    /**
     * Does nothing.
     */
    public void init() {}

    /**
     * Does nothing.
     */
    public void shutdown() {}

    public abstract String help(final Identifier identifier);

}
// Copyright (c) Naked Objects Group Ltd.
