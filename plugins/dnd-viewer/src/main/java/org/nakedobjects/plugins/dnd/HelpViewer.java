package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


/**
 * A HelpSystem is used to launch the help system and bring up contextual help.
 */
public interface HelpViewer {

    void open(Location location, String name, String description, String help);

}
// Copyright (c) Naked Objects Group Ltd.
