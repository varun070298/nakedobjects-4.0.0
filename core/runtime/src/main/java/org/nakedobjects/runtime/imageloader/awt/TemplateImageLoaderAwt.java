package org.nakedobjects.runtime.imageloader.awt;

import static org.hamcrest.CoreMatchers.is;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatState;

import java.awt.Canvas;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.lang.Resources;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.imageloader.TemplateImage;
import org.nakedobjects.runtime.imageloader.TemplateImageImpl;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;


/**
 * This class loads up file based images as resources (part of the classpath) or from the file system. Images
 * of type PNG, GIF and JPEG will be used. The default directory is images.
 */
public class TemplateImageLoaderAwt implements TemplateImageLoader {

    private static final String LOAD_IMAGES_FROM_FILES_KEY = ImageConstants.PROPERTY_BASE + "load-images-from-files";
    private static final String[] EXTENSIONS = { "png", "gif", "jpg", "jpeg" };
    private final static Logger LOG = Logger.getLogger(TemplateImageLoaderAwt.class);
    private final static String IMAGE_DIRECTORY = "images";
    private final static String IMAGE_DIRECTORY_PARAM = ImageConstants.PROPERTY_BASE + "image-directory";
    private static final String SEPARATOR = "/";

    private boolean initialized;
    
    private boolean alsoLoadAsFiles;
    protected final MediaTracker mt = new MediaTracker(new Canvas());
    
    /**
     * A keyed list of core images, one for each name, keyed by the image path.
     */
    private final Hashtable<String,TemplateImage> loadedImages = new Hashtable<String,TemplateImage>();
    private final Vector<String> missingImages = new Vector<String>();
    private final NakedObjectConfiguration configuration;
    private String directory;

    //////////////////////////////////////////////////////////////
    // constructor
    //////////////////////////////////////////////////////////////

    public TemplateImageLoaderAwt(NakedObjectConfiguration configuration) {
        this.configuration = configuration;
    }

    
    //////////////////////////////////////////////////////////////
    // init, shutdown
    //////////////////////////////////////////////////////////////

    public void init() {
        ensureNotInitialized();
        LOG.info("images to be loaded from " + directory());
        alsoLoadAsFiles = getConfiguration().getBoolean(LOAD_IMAGES_FROM_FILES_KEY, true);
        initialized = true;
    }


    public void shutdown() {}

    
    private void ensureNotInitialized() {
        ensureThatState(initialized, is(false));
    }
    
    private void ensureInitialized() {
        ensureThatState(initialized, is(true));
    }
    

    //////////////////////////////////////////////////////////////
    // getTemplateImage
    //////////////////////////////////////////////////////////////

    /**
     * Returns an image template for the specified image (as specified by a path to a file or resource).
     * 
     * <p>
     * If the path has no extension (<tt>.gif</tt>, <tt>.png</tt> etc) then all valid {@link #EXTENSIONS extensions} 
     * are searched for.
     * 
     * <p>
     * This method attempts to load the image from the jar/zip file this class was loaded from ie, your
     * application, and then from the file system as a file if can't be found as a resource. If neither method
     * works the default image is returned.
     * 
     * @return returns a {@link TemplateImage} for the specified image file, or null if none found.
     */
    public TemplateImage getTemplateImage(final String name) {
        ensureInitialized();
        
        if (loadedImages.contains(name)) {
            return loadedImages.get(name);
        }
        
        if (missingImages.contains(name)) {
            return null;
        }

        List<String> candidates = getCandidates(name);
        for(String candidate: candidates) {
            Image image = load(candidate);
            TemplateImageImpl templateImage = TemplateImageImpl.create(image);
            if (templateImage != null) {
                loadedImages.put(name, templateImage);
                return templateImage;
            }
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("failed to find image for " + name);
        }
        missingImages.addElement(name);
        return null;
    }


    //////////////////////////////////////////////////////////////
    // helpers: parsing path
    //////////////////////////////////////////////////////////////

    private List<String> getCandidates(final String name) {
        boolean hasExtension = false;
        for(String extension: EXTENSIONS) {
            hasExtension = hasExtension || name.endsWith(extension);
        }

        List<String> candidates = new ArrayList<String>();
        if (hasExtension) {
            candidates.add(name);
        } else {
            for(String extension: EXTENSIONS) {
                candidates.add(name + "." + extension);
            }
        }
        return candidates;
    }

    //////////////////////////////////////////////////////////////
    // helpers: loading
    //////////////////////////////////////////////////////////////

    private Image load(final String name) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("searching for image " + name);
        }
        
        Image image = loadAsResource(name);
        if (image != null) {
            return image;
        }

        final String path = directory() + name;
        image = loadAsResource(path);
        if (image != null) {
            return image;
        }

        if (alsoLoadAsFiles) {
            image = loadAsFile(path);
            if (image != null) {
                return image;
            }
        }
        
        return null;
    }

    /**
     * Get an Image object from the jar/zip file that this class was loaded from.
     */
    protected Image loadAsResource(final String path) {
        final URL url = Resources.getResourceURL(path);
        if (url == null) {
            LOG.debug("not found image in resources: " + url);
            return null;
        }

        Image image = Toolkit.getDefaultToolkit().getImage(url);
        if (image != null) {
            mt.addImage(image, 0);
            try {
                mt.waitForAll();
            } catch (final Exception e) {
                e.printStackTrace();
            }

            if (mt.isErrorAny()) {
                LOG.error("found image but failed to load it from resources: " + url + " " + mt.getErrorsAny()[0]);
                mt.removeImage(image);
                image = null;
            } else {
                mt.removeImage(image);
                LOG.info("image loaded from resources: " + url);
            }
        }

        if (image == null || image.getWidth(null) == -1) {
            throw new RuntimeException(image.toString());
        }

        return image;
    }

    /**
     * Get an {@link Image} object from the specified file path on the file system.
     */
    private Image loadAsFile(final String path) {
        final File file = new File(path);

        if (!file.exists()) {
            return null;
        }
        final Toolkit t = Toolkit.getDefaultToolkit();
        Image image = t.getImage(file.getAbsolutePath());

        if (image != null) {
            mt.addImage(image, 0);

            try {
                mt.waitForAll();
            } catch (final Exception e) {
                e.printStackTrace();
            }

            if (mt.isErrorAny()) {
                LOG.error("found image file but failed to load it: " + file.getAbsolutePath());
                mt.removeImage(image);
                image = null;
            } else {
                mt.removeImage(image);
                LOG.info("image loaded from file: " + file);
            }
        }
        return image;
    }

    private String directory() {
        if (directory == null) {
            directory = getConfiguration().getString(IMAGE_DIRECTORY_PARAM, IMAGE_DIRECTORY);
            if (!directory.endsWith(SEPARATOR)) {
                directory = directory.concat(SEPARATOR);
            }
        }
        return directory;
    }

    //////////////////////////////////////////////////////////////
    // unused
    //////////////////////////////////////////////////////////////

    /**
     * This code was commented out.  I've reinstated it, even though it is unused,
     * because it looks interesting and perhaps useful.
     */
    @SuppressWarnings("unused")
    private Image createImage() {
        byte[] pixels = new byte[128 * 128];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = (byte) (i % 128);
        }

        byte[] r = new byte[] { 0, 127 };
        byte[] g = new byte[] { 0, 127 };
        byte[] b = new byte[] { 0, 127 };
        IndexColorModel colorModel = new IndexColorModel(1, 2, r, g, b);

        MemoryImageSource producer = new MemoryImageSource(128, 128, colorModel, pixels, 0, 128);
        Image image = Toolkit.getDefaultToolkit().createImage(producer);

        return image;
    }

    //////////////////////////////////////////////////////////////
    // dependencies (from singleton)
    //////////////////////////////////////////////////////////////

    private NakedObjectConfiguration getConfiguration() {
        return configuration;
    }



}
// Copyright (c) Naked Objects Group Ltd.
