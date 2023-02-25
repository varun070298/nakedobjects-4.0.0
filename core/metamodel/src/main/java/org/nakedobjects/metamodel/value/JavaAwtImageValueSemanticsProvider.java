package org.nakedobjects.metamodel.value;

import java.awt.Image;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class JavaAwtImageValueSemanticsProvider extends ImageValueSemanticsProviderAbstract {

    public JavaAwtImageValueSemanticsProvider(
            final FacetHolder holder,
            final NakedObjectConfiguration configuration,
            final SpecificationLoader specificationLoader,
            final RuntimeContext runtimeContext) {
        super(holder, Image.class, configuration, specificationLoader, runtimeContext);
    }

	public int getHeight(final NakedObject object) {
        return image(object).getHeight(null);
    }

    private Image image(final NakedObject object) {
        return (Image) object.getObject();
    }

    public Image getImage(final NakedObject object) {
        return image(object);
    }

    @Override
    protected int[][] getPixels(final Object object) {
        return grabPixels((Image) object);
    }

    public Class<?> getValueClass() {
        return Image.class;
    }

    public int getWidth(final NakedObject object) {
        return image(object).getWidth(null);
    }

    @Override
    protected Object setPixels(final int[][] pixels) {
        final Image image = createImage(pixels);
        return getRuntimeContext().adapterFor(image);
    }


    public boolean isNoop() {
        return false;
    }

    @Override
    public String toString() {
        return "JavaAwtImageValueSemanticsProvider: ";
    }

    public NakedObject createValue(final Image image) {
        return getRuntimeContext().adapterFor(image);
   }
    
}

// Copyright (c) Naked Objects Group Ltd.
