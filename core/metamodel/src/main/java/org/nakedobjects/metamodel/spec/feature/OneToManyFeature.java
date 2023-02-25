package org.nakedobjects.metamodel.spec.feature;


/**
 * Base interface for {@link OneToManyAssociation} only.
 * 
 * <p>
 * Introduced for symmetry with {@link OneToOneFeature}; if we ever support collections as parameters then
 * would also be the base interface for a <tt>OneToManyActionParameter</tt>.
 * 
 * <p>
 * Is also the route upto the {@link NakedObjectFeature} superinterface.
 * 
 */
public interface OneToManyFeature extends NakedObjectFeature {

}
