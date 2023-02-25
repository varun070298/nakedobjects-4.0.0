package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import org.nakedobjects.applib.value.Image;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.value.ImageValueSemanticsProviderAbstract;


public class ImageType extends AbstractImageType {

    private final RuntimeContext runtimeContext;
    
    public ImageType(final RuntimeContext runtimeContext) {
    	this.runtimeContext = runtimeContext;
    }

    @Override
    protected ImageValueSemanticsProviderAbstract getImageAdapter() {
        // return new ImageValueSemanticsProvider(runtimeContext);
        throw new NotYetImplementedException();
        // TODO update to work with new mechanism
    }

    // protected AbstractImageAdapter getImageAdapter() {
    // if (value == null) {
    // return new JavaAwtImageValueSemanticsProvider();
    // }
    // return new JavaAwtImageValueSemanticsProvider((Image) value);
    // return new JavaAwtImageValueSemanticsProvider();
    // }

    @Override
    public Class<Image> returnedClass() {
        return Image.class;
    }
}
