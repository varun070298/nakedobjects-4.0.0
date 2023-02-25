package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.TypedSpecification;

/**
 * Mix-in interface for {@link TypedSpecification}s that reference or
 * otherwise contain a 'current' value.
 * 
 * <p>
 * Examples include {@link OneToOneAssociation properties}, {@link OneToManyAssociation collection}s
 * and {@link OneToOneActionParameter action parameter}s (but not 
 * {@link NakedObjectAction action}s themselves).
 */
public interface CurrentHolder {

    
    /**
     * Returns the referenced {@link NakedObject} for the owning {@link NakedObject}.
     *  
     * <p>
     * For example, if this is an {@link OneToOneAssociation}, then returns the referenced object.
     */
    NakedObject get(final NakedObject owner);

}
