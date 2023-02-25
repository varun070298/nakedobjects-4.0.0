package org.nakedobjects.plugins.dnd.viewer.util;

import java.util.StringTokenizer;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.ConfigurationException;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class Properties {

    public static final String PROPERTY_BASE = ConfigurationConstants.ROOT + "viewer.dnd.";

    public static Size getSize(final String name, final Size defaultSize) {
        final String initialSize = NakedObjectsContext.getConfiguration().getString(name);
        if (initialSize != null) {
            final StringTokenizer st = new StringTokenizer(initialSize, "x");
            if (st.countTokens() == 2) {
                int width = 0;
                int height = 0;
                width = Integer.valueOf(st.nextToken().trim()).intValue();
                height = Integer.valueOf(st.nextToken().trim()).intValue();
                return new Size(width, height);
            } else {
                throw new ConfigurationException("Size not specified correctly in " + name + ": " + initialSize);
            }
        }
        return defaultSize;
    }

    public static Location getLocation(final String name, final Location defaultLocation) {
        final String initialLocation = NakedObjectsContext.getConfiguration().getString(name);
        if (initialLocation != null) {
            final StringTokenizer st = new StringTokenizer(initialLocation, ",");
            if (st.countTokens() == 2) {
                int x = 0;
                int y = 0;
                x = Integer.valueOf(st.nextToken().trim()).intValue();
                y = Integer.valueOf(st.nextToken().trim()).intValue();
                return new Location(x, y);
            } else {
                throw new ConfigurationException("Location not specified correctly in " + name + ": " + initialLocation);
            }
        }
        return defaultLocation;
    }
}

// Copyright (c) Naked Objects Group Ltd.
