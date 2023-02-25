package org.nakedobjects.metamodel.specloader.internal;

import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.MutableProposedHolder;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.OneToOneActionParameter;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionParamPeer;


public class OneToOneActionParameterImpl extends NakedObjectActionParameterAbstract implements OneToOneActionParameter {

    public OneToOneActionParameterImpl(final int index, final NakedObjectActionImpl actionImpl, final NakedObjectActionParamPeer peer) {
        super(index, actionImpl, peer);
    }

    @Override
    public boolean isObject() {
        return true;
    }

    /**
     * TODO: need to be able to validate parameters individually, eg if have <tt>RegEx</tt> annotation;
     * should delegate to the Check framework instead.
     */
    public String isValid(final NakedObject nakedObject, final Object proposedValue) {
        return null;
    }

    // /////////////////////////////////////////////////////////////
    // getInstance
    // /////////////////////////////////////////////////////////////
    
    public Instance getInstance(NakedObject nakedObject) {
        OneToOneActionParameter specification = this;
        return nakedObject.getInstance(specification);
    }


    // //////////////////////////////////////////////////////////////////////
    // get, set
    // //////////////////////////////////////////////////////////////////////

    /**
     * Gets the proposed value of the {@link Instance} (downcast as a
     * {@link MutableProposed}, wrapping the proposed value into a {@link NakedObject}.
     */
    public NakedObject get(NakedObject owner) {
        MutableProposedHolder proposedHolder = getProposedHolder(owner);
        Object proposed = proposedHolder.getProposed();
        return getRuntimeContext().adapterFor(proposed);
    }

    /**
     * Sets the proposed value of the {@link Instance} (downcast as a
     * {@link MutableProposed}, unwrapped the proposed value from a {@link NakedObject}.
     */
    public void set(NakedObject owner, NakedObject newValue) {
        MutableProposedHolder proposedHolder = getProposedHolder(owner);
        Object newValuePojo = newValue.getObject();
        proposedHolder.setProposed(newValuePojo);
    }

    private MutableProposedHolder getProposedHolder(NakedObject owner) {
        Instance instance = getInstance(owner);
        if(!(instance instanceof MutableProposedHolder)) {
            throw new IllegalArgumentException("Instance should implement MutableProposedHolder");
        }
        MutableProposedHolder proposedHolder = (MutableProposedHolder) instance;
        return proposedHolder;
    }


}
