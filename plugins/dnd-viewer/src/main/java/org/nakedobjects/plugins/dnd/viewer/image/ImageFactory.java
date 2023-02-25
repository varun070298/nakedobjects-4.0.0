package org.nakedobjects.plugins.dnd.viewer.image;

import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.util.Hashtable;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.util.Properties;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.imageloader.TemplateImage;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;


public class ImageFactory {

    private static class Filter extends RGBImageFilter {
        @Override
        public int filterRGB(final int x, final int y, final int rgb) {
            return 0xFFFFFF - rgb;
        }
    }

    private static final String DEFAULT_IMAGE_NAME = "Default";
    private static final String DEFAULT_IMAGE_PROPERTY = Properties.PROPERTY_BASE + "default-image";
    private static ImageFactory instance;
    private static final String SEPARATOR = "_";

    public static ImageFactory getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Instance not set up yet");
        }
        return instance;
    }

    private final TemplateImageLoader loader;
    
    /**
     * Keyed list of icons (each derived from an image, of a specific size etc), where the key is the name of
     * the icon and its size.
     */
    private final Hashtable<String,Image> templateImages = new Hashtable<String,Image>();

    
    //////////////////////////////////////////////////////////////////////
    // Constructor
    //////////////////////////////////////////////////////////////////////
    
    public ImageFactory(final TemplateImageLoader imageLoader) {
        loader = imageLoader;
        instance = this;
    }

    
    //////////////////////////////////////////////////////////////////////
    // loadIcon for Specifications
    //////////////////////////////////////////////////////////////////////
    
    public Image loadIcon(final NakedObjectSpecification specification, final int iconHeight, final Color tint) {
        return findIcon(specification, iconHeight, null);
    }

    private Image findIcon(final NakedObjectSpecification specification, final int iconHeight, final Color tint) {
        
        Image loadIcon = null;
        
        if (loadIcon == null) {
            final String fullClassNameSlashes = specification.getFullName().replace(".", "/");
            loadIcon = loadIcon(fullClassNameSlashes, iconHeight, tint);
        }

        if (loadIcon == null) {
            final String fullClassNameUnderscores = specification.getFullName().replace('.', '_');
            loadIcon = loadIcon(fullClassNameUnderscores, iconHeight, tint);
        }

        if (loadIcon == null) {
            final String shortClassNameUnderscores = specification.getShortName().replace('.', '_');
            loadIcon = loadIcon(shortClassNameUnderscores, iconHeight, tint);
        }

        if (loadIcon == null) {
            loadIcon = findIconForSuperClass(specification, iconHeight, tint);
        }
        return loadIcon;
    }

    private Image findIconForSuperClass(final NakedObjectSpecification specification, final int iconHeight, final Color tint) {
        final NakedObjectSpecification superclassSpecification = specification.superclass();
        Image loadIcon;
        if (superclassSpecification == null) {
            loadIcon = null;
        } else {
            loadIcon = findIcon(superclassSpecification, iconHeight, tint);
        }
        return loadIcon;
    }

    
    //////////////////////////////////////////////////////////////////////
    // loadIcon for arbitrary path
    //////////////////////////////////////////////////////////////////////

    /**
     * Loads an icon of the specified size, and with the specified tint. If color is null then no tint is
     * applied.
     */
    public Image loadIcon(final String name, final int height, final Color tint) {
        final String id = name + SEPARATOR + height + SEPARATOR + tint;

        if (templateImages.containsKey(id)) {
            return templateImages.get(id);
        }
        
        final TemplateImage template = templateImage(name);
        if (template == null) {
            return null;
        }
        
        final java.awt.Image iconImage = template.getIcon(height);
        if (tint != null) {
            Toolkit.getDefaultToolkit().createImage(
                    new FilteredImageSource(iconImage.getSource(), new Filter()));
        }
        final Image icon = new AwtImage(iconImage);

        templateImages.put(id, icon);
        return icon;
    }


    //////////////////////////////////////////////////////////////////////
    // loadDefaultIcon
    //////////////////////////////////////////////////////////////////////

    /**
     * Loads the fall back icon image, for use when no specific image can be found
     */
    public Image loadDefaultIcon(final int height, final Color tint) {
        final String fallbackImage = getConfiguration().getString(DEFAULT_IMAGE_PROPERTY, DEFAULT_IMAGE_NAME);
        Image icon = loadIcon(fallbackImage, height, tint);
        if (icon == null) {
            icon = loadIcon("unknown", height, tint);
        }
        if (icon == null) {
            throw new NakedObjectException("Failed to find default icon: " + fallbackImage);
        }
        return icon;
    }


    //////////////////////////////////////////////////////////////////////
    // loadImage
    //////////////////////////////////////////////////////////////////////

    /**
     * Load an image with the given name.
     */
    public Image loadImage(final String path) {
        final TemplateImage template = templateImage(path);
        if (template == null) {
            return null;
        }
        return new AwtImage(template.getImage());
    }

    
    //////////////////////////////////////////////////////////////////////
    // Helpers
    //////////////////////////////////////////////////////////////////////

    private TemplateImage templateImage(final String name) {
        final TemplateImage template = loader.getTemplateImage(name);
        return template;
    }
    

    //////////////////////////////////////////////////////////////////////
    // Dependencies (from singleton)
    //////////////////////////////////////////////////////////////////////

    private NakedObjectConfiguration getConfiguration() {
        return NakedObjectsContext.getConfiguration();
    }




}
// Copyright (c) Naked Objects Group Ltd.
