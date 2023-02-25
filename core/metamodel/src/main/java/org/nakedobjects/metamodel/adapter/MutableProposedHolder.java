package org.nakedobjects.metamodel.adapter;

import org.nakedobjects.metamodel.interactions.ProposedHolder;
import org.nakedobjects.metamodel.spec.Specification;

/**
 * Mix-in for {@link Instance} implementations, where can hold a
 * proposed new value different from the underlying value.
 * 
 * <p>
 * TODO: same concept as {@link ProposedHolder}, so should try to
 * combine. 
 */
public interface MutableProposedHolder {

    /**
     * The proposed (pending) value, if applicable.
     * 
     * <p>
     * See {@link Specification#createInstanceProposalEvent(Instance)} for an indication as
     * to whether the state will be populated, and what its type will be.
     * 
     * @return
     */
    Object getProposed();

    
    /**
     * Sets the proposed (pending) value, if applicable.
     * 
     * <p>
     * <p>
     * See {@link Specification#createInstanceProposalEvent(Instance)} for an indication as
     * to whether the proposed state should be populated, and what its type will be.
     * 
     * @return
     */
    public void setProposed(Object value);

    
}
