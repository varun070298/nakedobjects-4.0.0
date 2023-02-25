package org.nakedobjects.metamodel.commons.debug;

/**
 * The information to be displayed in a trace or a debug window
 */
public interface DebugInfo {

    /**
     * Debug details describing the object being investigated
     */
    void debugData(DebugString debug);

    /**
     * The title for the debug information
     */
    String debugTitle();
}
// Copyright (c) Naked Objects Group Ltd.
