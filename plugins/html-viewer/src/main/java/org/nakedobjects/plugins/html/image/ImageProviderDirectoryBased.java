package org.nakedobjects.plugins.html.image;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

public class ImageProviderDirectoryBased implements ImageProvider {

    private final String UNKNOWN_IMAGE = "Default";
    private final String[] EXTENSIONS = { "png", "gif", "jpg", "jpeg" };
    private final Map images = new HashMap();
    private File imageDirectory;
    private String unknownImageFile;

    public void setImageDirectory(final String imageDirectory) {
        this.imageDirectory = new File(imageDirectory);
        if (!this.imageDirectory.exists()) {
            throw new NakedObjectException("No image directory found: " + imageDirectory);
        }
        unknownImageFile = imageFile(UNKNOWN_IMAGE);
    }

    public void debug(final DebugString debug) {
        debug.appendTitle("Image Lookup");
        debug.indent();
        final Iterator keys = images.keySet().iterator();
        while (keys.hasNext()) {
            final Object key = keys.next();
            final Object value = images.get(key);
            debug.appendln(key + " -> " + value);
        }
        debug.unindent();
    }

    private String imageFile(final String imageName) {
        final String[] files = imageDirectory.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                int dot = name.lastIndexOf('.');
                if (dot > 0) {
                    for (int i = 0; i < EXTENSIONS.length; i++) {
                        if (name.substring(0, dot).equalsIgnoreCase(imageName)
                                && name.substring(dot + 1).equalsIgnoreCase(EXTENSIONS[i])) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        return files.length == 0 ? null : files[0];
    }

    private String findImage(final NakedObjectSpecification specification) {
        final String name = specification.getShortName();
        final String fileName = imageFile(name);
        if (fileName != null) {
            images.put(name, fileName);
            return fileName;
        } else {
            final NakedObjectSpecification superclass = specification.superclass();
            if (superclass != null) {
                return findImage(superclass);
            } else {
                return unknownImageFile;
            }
        }
    }

    /**
     * For an object, the icon name from the object is return if it is not null, otherwise the specification
     * is used to look up a suitable image name.
     * 
     * @see NakedObject#getIconName()
     * @see #image(NakedObjectSpecification)
     */
    public String image(final NakedObject object) {
        final String iconName = object.getIconName();
        if (iconName != null) {
            return image(iconName);
        } else {
            return image(object.getSpecification());
        }
    }

    public String image(final NakedObjectSpecification specification) {
        final String name = specification.getShortName();
        final String imageName = (String) images.get(name);
        if (imageName != null) {
            return imageName;
        } else {
            return findImage(specification);
        }
    }

    public String image(final String name) {
        final String imageName = (String) images.get(name);
        if (imageName != null) {
            return imageName;
        } else {
            final String fileName = imageFile(name);
            return fileName == null ? unknownImageFile : fileName;
        }
    }

}


// Copyright (c) Naked Objects Group Ltd.
