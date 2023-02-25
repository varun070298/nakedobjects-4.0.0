package org.nakedobjects.plugins.dnd.viewer;

import java.awt.Frame;
import java.awt.Image;
import java.net.URL;


public class AWTUtilities {

    public static void addWindowIcon(final Frame window, final String file) {
        final URL url = ViewerFrame.class.getResource("/" + "images/" + file);
        if (url != null) {
            final Image image = java.awt.Toolkit.getDefaultToolkit().getImage(url);
            if (image != null) {
                window.setIconImage(image);
            }
        }
    }
}

// Copyright (c) Naked Objects Group Ltd.
