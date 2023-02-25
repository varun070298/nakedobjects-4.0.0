package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.TypedSpecification;

/**
 * Mix-in interface for {@link TypedSpecification}s that reference or
 * otherwise contain a 'current' value that moreover can be changed.
 * 
 * <p>
 * Examples include {@link OneToOneAssociation properties} and {@link OneToOneActionParameter action parameter}s (but not 
 * {@link NakedObjectAction action}s themselves) nor {@link OneToManyAssociation collection}s.
 */
public interface MutableCurrentHolder extends CurrentHolder {


    /**
     * Updates the referenced {@link NakedObject} for the owning {@link NakedObject}
     * with the new value provided, or clears the reference if null.
     *  
     * <p>
     * For example, if this is a {@link OneToOneAssociation}, then updates the 
     * object referenced .
     * 
     * @param owner
     * @param newValue - the new value, or <tt>null</tt>
     */
    void set(final NakedObject owner, final NakedObject newValue);

}
