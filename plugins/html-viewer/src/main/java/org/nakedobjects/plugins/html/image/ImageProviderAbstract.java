package org.nakedobjects.plugins.html.image;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

public abstract class ImageProviderAbstract implements ImageProvider {

    private final String DEFAULT_IMAGE = "Default";
    
    /**
     * The extensions we'll search for.
     */
    private final String[] EXTENSIONS = { "png", "gif", "jpg", "jpeg" };
    
    private final Map<String,String> images = new HashMap<String,String>();

    public final String image(final NakedObject object) {

        if (object == null) {
            return image((String)null);
        }

        final String iconName = object.getIconName();
        if (iconName != null) {
            return image(iconName);
        } else {
            return image(object.getSpecification());
        }
    }

    public final String image(final NakedObjectSpecification specification) {
        
        if (specification == null) {
            return image((String)null);
        }
        
        final String specShortName = specification.getShortName();
        final String imageName = image(specShortName);
        if (imageName != null) {
            return imageName;
        }
        
        // search up the hierarchy
        return image(specification.superclass());
    }

    public String image(final String name) {

        if (name == null) {
            return findImage(DEFAULT_IMAGE, EXTENSIONS);
        }
        
        // look up from cache
        String imageName = (String) images.get(name);
        if (imageName != null) {
            return imageName;
        }
        
        // delegate to subclass to see if can find the image.
        imageName = findImage(name, EXTENSIONS);
        
        if (imageName != null) {
            // cache and return
            images.put(name, imageName);
            return imageName;
        }

        // ie loop round to return the default.
        return image((String)null);
    }

    public final void debug(final DebugString debug) {
        debug.appendTitle("Image Lookup");
        debug.indent();
        final Iterator<String> keys = images.keySet().iterator();
        while (keys.hasNext()) {
            final Object key = keys.next();
            final Object value = images.get(key);
            debug.appendln(key + " -> " + value);
        }
        debug.unindent();
    }


    /**
     * Hook method for subclass to actually return the image, 
     * else <tt>null</tt>.
     * 
     * @param className - the short name of the class to search for.
     * @param extensions - the extensions to search for.
     */
    protected abstract String findImage(final String className, String[] extensions);


}


// Copyright (c) Naked Objects Group Ltd.
