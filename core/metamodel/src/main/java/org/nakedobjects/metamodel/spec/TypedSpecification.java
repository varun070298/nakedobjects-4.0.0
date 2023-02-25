package org.nakedobjects.metamodel.spec;

import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;

/**
 * A specification that has an underlying type.
 * 
 * <p>
 * For a property or action parameter, is the type.  For a collection is the element type.
 * For an action it is always <tt>null</tt>.
 * 
 * <p>
 * TODO: replace NakedObjectAction#getReturnType() with #getSpecification() - ie so doesn't return null.
 */
public interface TypedSpecification extends Specification {

    /**
     * The specification of the underlying type.
     * 
     * <p>
     * For example:
     * <ul>
     * <li>for a {@link OneToOneAssociation property}, will return the {@link NakedObjectSpecification} 
     *     of the type that the accessor returns.
     * <li>for a {@link OneToManyAssociation collection} it will be the type of element
     *     the collection holds (not the type of collection).
     * <li>for a {@link NakedObjectAction action}, will always return <tt>null</tt>.  See instead {@link NakedObjectAction#getReturnType()} and {@link NakedObjectAction#getParameterTypes()}.
     * <li>for a {@link NakedObjectActionParameter action}, will return the type of the parameter}.
     * </ul>
     */
    NakedObjectSpecification getSpecification();
    


}
