package org.nakedobjects.metamodel.value;

import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.ImageValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public abstract class ImageValueSemanticsProviderAbstract extends ValueSemanticsProviderAbstract implements ImageValueFacet {
    private static final boolean IMMUTABLE = false;
    private static final boolean EQUAL_BY_CONTENT = false;
    private static final Object DEFAULT_VALUE = null; // no default
    private static final int TYPICAL_LENGTH = 18;

    private static final char[] BASE_64_CHARS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '+', '/', };

    protected static final byte[] REVERSE_BASE_64_CHARS = new byte[0x100];

    static {
        for (int i = 0; i < REVERSE_BASE_64_CHARS.length; i++) {
            REVERSE_BASE_64_CHARS[i] = -1;
        }
        for (byte i = 0; i < BASE_64_CHARS.length; i++) {
            REVERSE_BASE_64_CHARS[BASE_64_CHARS[i]] = i;
        }
    }

    private FacetHolder facetHolder;

 //   private final RuntimeContext runtimeContext;

    public ImageValueSemanticsProviderAbstract(final FacetHolder holder,
            final Class<?> adaptedClass,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(ImageValueFacet.class, holder, adaptedClass, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
    }
    

    /**
     * Returns null to indicate that this value does not parse entry strings
     */
    @Override
    public Parser getParser() {
        return null;
    }

    @Override
    protected Object doParse(Object original, String entry) {
        throw new UnexpectedCallException();
    }

    public byte[] getAsByteArray(final NakedObject object) {
        final int[] flatIntArray = flatten(object);
        final byte[] byteArray = new byte[flatIntArray.length * 4];
        for (int i = 0; i < flatIntArray.length; i++) {
            final int value = flatIntArray[i];
            byteArray[i * 4] = (byte) ((value >> 24) & 0xff);
            byteArray[i * 4 + 1] = (byte) ((value >> 16) & 0xff);
            byteArray[i * 4 + 2] = (byte) ((value >> 8) & 0xff);
            byteArray[i * 4 + 3] = (byte) (value & 0xff);
        }
        return byteArray;
    }

    private static int byteArrayToInt(final byte[] byteArray, final int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            final int shift = (4 - 1 - i) * 8;
            value += (byteArray[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }

    public boolean alwaysReplace() {
        return false;
    }

    public Facet getUnderlyingFacet() {
        return null;
    }

    /**
     * Not required because {@link #alwaysReplace()} is <tt>false</tt>.
     */
    public void setUnderlyingFacet(Facet underlyingFacet) {
        throw new UnsupportedOperationException();
    }

    public boolean isDerived() {
        return false;
    }

    public Object restoreFromByteArray(final byte[] byteArray) {
        final int[] flatIntArray = new int[byteArray.length / 4];
        for (int i = 0; i < flatIntArray.length; i++) {
            flatIntArray[i] = byteArrayToInt(byteArray, i * 4);
        }
        return setPixels(inflate(flatIntArray));
    }

    private int[] flatten(final NakedObject object) {
        final int[][] image = getPixels(object);
        final int[] flatArray = new int[(getHeight(object) * getWidth(object)) + 2];
        int flatIndex = 0;
        flatArray[flatIndex++] = getHeight(object);
        flatArray[flatIndex++] = getWidth(object);
        for (int i = 0; i < getHeight(object); i++) {
            for (int j = 0; j < getWidth(object); j++) {
                flatArray[flatIndex++] = image[i][j];
            }
        }
        return flatArray;
    }

    private static int[][] inflate(final int[] flatIntArray) {
        int flatIndex = 0;
        final int height = flatIntArray[flatIndex++];
        final int width = flatIntArray[flatIndex++];

        final int[][] newImage = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newImage[i][j] = flatIntArray[flatIndex++];
            }
        }
        return newImage;
    }

    protected String doEncode(Object object) {
        final int[][] image = getPixels(object);
        final int lines = image.length;
        final int width = image[0].length;
        final StringBuffer encodeData = new StringBuffer(lines * width * 4);
        encodePixel(lines, encodeData);
        encodePixel(width, encodeData);
        for (int line = 0; line < lines; line++) {
            for (int pixel = 0; pixel < width; pixel++) {
                encodePixel(image[line][pixel], encodeData);
            }
        }
        return encodeData.toString();
    }

    protected Image createImage(final int[][] pixels) {
        final int lines = pixels.length;
        final int width = pixels[0].length;
        final int[] pix = new int[lines * width];
        int offset = 0;
        for (int line = 0; line < lines; line++) {
            System.arraycopy(pixels[line], 0, pix, offset, width);
            offset += width;
        }
        final MemoryImageSource source = new MemoryImageSource(width, lines, pix, 0, width);
        return java.awt.Toolkit.getDefaultToolkit().createImage(source);
    }

    private int decodePixel(final String data, final int item) {
        final int offset = item * 4;
        int pixel = 0;
        for (int i = 0; i < 4; i++) {
            final char c = data.charAt(offset + i);
            final byte b = REVERSE_BASE_64_CHARS[c];
            if (i== 0 && b >= 32) {
                pixel = 0xff;
            }
            pixel = (pixel << 6) + b;
        }
        return pixel;
    }

    private void encodePixel(final int pixel, final StringBuffer encodeData) {
        // TODO the encoding is poor as the negative numbers are not dealt with properly because the 6 MSBs
        // are not included.
        if (pixel > 0x7FFFFF || pixel < -0x7FFFFF) {
    //        throw new NakedObjectException("" + pixel);
        }
        for (int i = 3; i >= 0; i--) {
            final int bitsToShift = i * 6;
            final int c = pixel >> bitsToShift;
            final char cc = BASE_64_CHARS[c & 0x3F];
            encodeData.append(cc);
        }
    }

    public String getIconName() {
        return null;
    }

    protected abstract int[][] getPixels(Object object);

    protected int[][] grabPixels(final Image image) {
        final int width = image.getWidth(null);
        final int lines = image.getHeight(null);
        final int pixels[] = new int[width * lines];
        final PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, lines, pixels, 0, width);
        grabber.setColorModel(ColorModel.getRGBdefault());
        try {
            if (grabber.grabPixels() && (grabber.status() & ImageObserver.ALLBITS) != 0) {
                final int[][] array = new int[lines][width];
                int srcPos = 0;
                for (int line = 0; line < lines; line++) {
                    array[line] = new int[width];
                    System.arraycopy(pixels, srcPos, array[line], 0, width);
                    for (int pixel = 0; pixel < array[line].length; pixel++ ) {
                        array[line][pixel] =array[line][pixel] | 0xFF000000; 
                    }
                    srcPos += width;
                }
                return array;
            }
            // TODO what happens if the grab fails?
            return new int[lines][width];
        } catch (final InterruptedException e) {
            throw new NakedObjectException(e);
        }
    }

    protected Object doRestore(final String data) {
        final int lines = decodePixel(data, 0);
        final int width = decodePixel(data, 1);
        final int[][] imageData = new int[lines][width];
        int offset = 2;
        for (int line = 0; line < lines; line++) {
            for (int pixel = 0; pixel < width; pixel++) {
                imageData[line][pixel] = decodePixel(data, offset++);
                imageData[line][pixel] =imageData[line][pixel] | 0xFF000000; 
            }
        }
        final Object image = setPixels(imageData);
        return image;
   }

    protected abstract Object setPixels(int[][] pixels);

    public void setMask(final String mask) {}

    public String titleString(final Object value) {
        return "image";
    }

    public String titleStringWithMask(Object value, String usingMask) {
        return "image";
    }
    
    public FacetHolder getFacetHolder() {
        return facetHolder;
    }

    public void setFacetHolder(final FacetHolder facetHolder) {
        this.facetHolder = facetHolder;
    }
    
    // /////// toString ///////

    @Override
    public String toString() {
        return "ImageValueSemanticsProvider";
    }

}
// Copyright (c) Naked Objects Group Ltd.
