package org.nakedobjects.metamodel.spec;

import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeature;
import org.nakedobjects.metamodel.spec.feature.NakedObjectMember;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.metamodel.spec.identifier.Identified;

/**
 * Base interface for elements of the metamodel.
 * 
 * <p>
 * The most significant subinterfaces of this are {@link NakedObjectSpecification} and
 * {@link NakedObjectFeature} (which brings in {@link NakedObjectMember} and
 * {@link NakedObjectActionParameter}.
 * 
 * <p>
 * Introduces so that viewers can deal with abstract Instances of said.
 * 
 */
public interface Specification extends Identified {

    
    /**
     * Return an {@link Instance} of this {@link Specification} with respect
     * to the provided {@link NakedObject}.
     *
     * <p>
     * For example, if the {@link Specification} is a {@link OneToOneAssociation},
     * then is an {@link Instance} implementation representing the { {@link NakedObject}/ {@link OneToOneAssociation} } tuple.
     * 
     * <p>
     * Implementations are expected to use a double-dispatch back to the provided
     * {@link NakedObject} (passing themselves as a parameter), using
     * {@link NakedObject#getInstance(Specification)}.  
     * 
     * <p>
     * Note: this method may throw an {@link UnsupportedOperationException};
     * see {@link NakedObject#getInstance(Specification)} for details.
     */
    Instance getInstance(final NakedObject nakedObject);


}    
