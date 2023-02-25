package org.nakedobjects.metamodel.value;

import org.nakedobjects.applib.value.Image;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class ImageValueSemanticsProvider extends ImageValueSemanticsProviderAbstract {

    public ImageValueSemanticsProvider(
            final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(holder, Image.class, configuration, specificationLoader, runtimeContext);
    }

    public int getHeight(final NakedObject object) {
        return image(object).getHeight();
    }

    private Image image(final NakedObject object) {
        return (Image) object.getObject();
    }

    public java.awt.Image getImage(final NakedObject object) {
        return createImage(image(object).getImage());
    }

    @Override
    protected int[][] getPixels(final Object object) {
        return ((Image) object).getImage();
    }

    public Class<?> getValueClass() {
        return Image.class;
    }

    public int getWidth(final NakedObject object) {
        return image(object).getWidth();
    }

    @Override
    protected Object setPixels(final int[][] pixels) {
        return new Image(pixels);
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

    public boolean alwaysReplace() {
        return false;
    }

    public boolean isNoop() {
        return false;
    }

    @Override
    public String toString() {
        return "ImageValueSemanticsProvider: ";
    }

    public NakedObject createValue(final java.awt.Image image) {
        return getRuntimeContext().adapterFor(new Image(grabPixels(image)));
   }

}
// Copyright (c) Naked Objects Group Ltd.
