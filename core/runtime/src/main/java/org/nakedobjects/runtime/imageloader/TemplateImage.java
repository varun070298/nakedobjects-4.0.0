package org.nakedobjects.runtime.imageloader;

import java.awt.Image;


/**
 * A template image is an image that is used to create other images from.
 * 
 * <p>
 * Typically this will be a larger image that can be scaled down in size.
 */
public interface TemplateImage {
    Image getImage();

    Image getIcon(final int height);
}

// Copyright (c) Naked Objects Group Ltd.
