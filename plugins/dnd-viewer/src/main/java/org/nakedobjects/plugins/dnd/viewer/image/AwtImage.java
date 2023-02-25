package org.nakedobjects.plugins.dnd.viewer.image;

import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class AwtImage implements Image {
    java.awt.Image iconImage;

    public AwtImage(final java.awt.Image iconImage) {
        if (iconImage == null) {
            throw new NullPointerException();
        }

        this.iconImage = iconImage;
    }

    public int getHeight() {
        return iconImage.getHeight(null);
    }

    public int getWidth() {
        return iconImage.getWidth(null);
    }

    public Size getSize() {
        return new Size(getWidth(), getHeight());
    }

    public java.awt.Image getAwtImage() {
        return iconImage;
    }
}
// Copyright (c) Naked Objects Group Ltd.
