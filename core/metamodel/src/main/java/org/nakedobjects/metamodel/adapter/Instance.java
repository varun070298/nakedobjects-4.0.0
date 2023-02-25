package org.nakedobjects.metamodel.adapter;

import org.nakedobjects.metamodel.spec.Specification;

/**
 * Represents an instance of some element of the meta-model.
 * 
 * <p>
 * The most significant sub-interface is {@link NakedObject}.  However, 
 * can also be used directly to represent associations, actions and 
 * action parameters.
 * 
 * <p>
 * Note also: in the optional <tt>no-architecture-x</tt> project there
 * is a sub-interface that allows listeners to be attached to the
 * instance.
 */
public interface Instance {

    /**
     * Returns the specification that details the structure of this instance.
     */
    Specification getSpecification();
    
    /**
     * The owning {@link NakedObject} of this instance.
     * 
     * <p>
     * In the case of the {@link NakedObject} interface, just returns <tt>null</tt>.
     * 
     * @return
     */
    NakedObject getOwner();



    
}
