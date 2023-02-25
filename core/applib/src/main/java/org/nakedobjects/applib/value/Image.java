package org.nakedobjects.applib.value;

import java.io.Serializable;

import org.nakedobjects.applib.annotation.Value;


/**
 * Represents an image.
 */
@Value(semanticsProviderName = "org.nakedobjects.metamodel.value.ImageValueSemanticsProvider")
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int[][] image;

    public Image(final int[][] image) {
        this.image = image;
    }

    public Object getValue() {
        return image;
    }

    @Override
    public String toString() {
        final int height = getHeight();
        return "Image [size=" + height + "x" + (height == 0 || image[0] == null ? 0 : image[0].length) + "]";
    }

    public int[][] getImage() {
        return image;
    }

    public int getHeight() {
        return image == null ? 0 : image.length;
    }

    public int getWidth() {
        return image == null ? 0 : image[0].length;
    }
}
// Copyright (c) Naked Objects Group Ltd.
