package org.nakedobjects.plugins.html.image;

import java.io.File;
import java.io.FilenameFilter;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;

public class ImageProviderDirectoryBasedReworked extends ImageProviderAbstract {

    private File imageDirectory;
    
    public void setImageDirectory(final String imageDirectory) {
        this.imageDirectory = new File(imageDirectory);
        if (!this.imageDirectory.exists()) {
            throw new NakedObjectException("No image directory found: " + imageDirectory);
        }
    }

    protected String findImage(final String imageName, final String[] extensions) {
        final String[] files = imageDirectory.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                int dot = name.lastIndexOf('.');
                if (dot > 0) {
                    String nameWithoutExtension = name.substring(0, dot);
                    String nameExtension = name.substring(dot + 1);
                    for (int i = 0; i < extensions.length; i++) {
                        if (nameWithoutExtension.equalsIgnoreCase(imageName) && 
                            nameExtension.equalsIgnoreCase(extensions[i])) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        return files.length == 0 ? null : files[0];
    }

}


// Copyright (c) Naked Objects Group Ltd.
