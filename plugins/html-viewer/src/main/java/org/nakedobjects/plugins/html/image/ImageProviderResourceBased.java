package org.nakedobjects.plugins.html.image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nakedobjects.metamodel.commons.lang.Resources;


public class ImageProviderResourceBased extends ImageProviderAbstract {

    /**
     * Is an array since easy to maintain 
     */
    public final static String[] DEFAULT_LOCATIONS = {
            "images", 
            "src/main/resources", 
            "src/main/java"
        };

    /**
     * Is a list since easy to inject. 
     */
    private List<String> locations = new ArrayList<String>();

    /**
     * Initializes {@link #locations} with {@link #DEFAULT_LOCATIONS}, but can
     * be overridden using {@link #setLocations(List)}.
     */
    public ImageProviderResourceBased() {
        locations.addAll(Arrays.asList(DEFAULT_LOCATIONS));
    }
    
    @Override
    protected String findImage(final String className, final String[] extensions) {

        for(String location: locations) {
            for (int i = 0; i < extensions.length; i++) {
                String candidate = location + "/" + className + "." + extensions[i];
                if (Resources.getResourceAsFile(candidate) != null) {
                    return candidate;
                }
            }
        }
        return null;
    }
    
    /**
     * Optionally inject the locations where the provider will search.
     * 
     * <p>
     * If not specified, will use the locations in {@link #DEFAULT_LOCATIONS}.
     */
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    
}


// Copyright (c) Naked Objects Group Ltd.
