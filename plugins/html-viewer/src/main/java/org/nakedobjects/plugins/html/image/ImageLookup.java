package org.nakedobjects.plugins.html.image;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


/**
 * ImageLookup provides an efficient way of finding the most suitable image to use.
 * 
 * <p>
 * It ensures that an image is always available, providing a default image if needed. 
 * All requests are cached to improve performance.
 */
// TODO allow for multiple extension types
public class ImageLookup {
    
    private static ImageProvider imageProvider = new ImageProviderResourceBased();
    
    public static ImageProvider getInstance() {
        return imageProvider;
    }
    
    public static void setImageDirectory(final String imageDirectory) {
        if (getInstance() instanceof ImageProviderDirectoryBased) {
            ImageProviderDirectoryBased imageProviderDirectoryBased = (ImageProviderDirectoryBased) imageProvider;
            imageProviderDirectoryBased.setImageDirectory(imageDirectory);
        }
    }

    public static void debug(final DebugString debug) {
        getInstance().debug(debug);
    }

    public static String image(final NakedObject object) {
        return getInstance().image(object);
    }

    public static String image(final NakedObjectSpecification specification) {
        return getInstance().image(specification);
    }

    public static String image(final String name) {
        return getInstance().image(name);
    }

}

// Copyright (c) Naked Objects Group Ltd.
