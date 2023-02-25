package org.nakedobjects.plugins.html.image;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

/**
 * Factors out the {@link ImageLookup} responsibilities into an interface, with a
 * view to moving towards alternative mechanisms.
 */
public interface ImageProvider {

    public void debug(final DebugString debug);
    
    /**
     * For an object, the icon name from the object is return if it is not null, otherwise the specification
     * is used to look up a suitable image name.
     * 
     * @see NakedObject#getIconName()
     * @see #image(NakedObjectSpecification)
     */
    public String image(final NakedObject object);

    public String image(final NakedObjectSpecification specification);

    public String image(final String name);

}


// Copyright (c) Naked Objects Group Ltd.
