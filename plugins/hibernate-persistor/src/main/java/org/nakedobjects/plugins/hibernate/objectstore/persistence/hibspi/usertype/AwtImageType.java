package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.awt.Image;

import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.value.ImageValueSemanticsProviderAbstract;


public class AwtImageType extends AbstractImageType {

    private final RuntimeContext runtimeContext;
    
    public AwtImageType(final RuntimeContext runtimeContext) {
    	this.runtimeContext = runtimeContext;
    }

	@Override
    protected ImageValueSemanticsProviderAbstract getImageAdapter() {
	    // return new ImageValueSemanticsProvider(runtimeContext);
        throw new NotYetImplementedException();
        // TODO update to work with new mechanism

    }

    // protected AbstractImageAdapter getImageAdapter(Object value) {
    // if (value == null) {
    // return new JavaAwtImageValueSemanticsProvider();
    // }
    // return new JavaAwtImageValueSemanticsProvider((Image) value);
    // }

    @Override
    public Class<Image> returnedClass() {
        return Image.class;
    }
}
