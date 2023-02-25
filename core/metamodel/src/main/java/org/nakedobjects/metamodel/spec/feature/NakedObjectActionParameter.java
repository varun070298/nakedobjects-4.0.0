package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.interactions.ActionArgumentContext;
import org.nakedobjects.metamodel.spec.NamedAndDescribed;

/**
 * Analogous to {@link NakedObjectAssociation}.
*/
public interface NakedObjectActionParameter extends NakedObjectFeature, NamedAndDescribed, CurrentHolder {

    /**
     * If true then can cast to a {@link OneToOneActionParameter}.
     * 
     * <p>
     * Either this or {@link #isCollection()} will be true.
     * 
     * <p>
     * Design note: modelled after {@link NakedObjectAssociation#isObject()}
     */
    boolean isObject();

    /**
     * Only for symmetry with {@link NakedObjectAssociation}, however since the NOF does not support
     * collections as actions all implementations should return <tt>false</tt>.
     */
    boolean isCollection();

    /**
     * Owning {@link NakedObjectAction}.
     */
    NakedObjectAction getAction();

    /**
     * Returns a flag indicating if it can be left unset when the action can be invoked.
     */
    boolean isOptional();

    /**
     * Returns the 0-based index to this parameter.
     */
    int getNumber();

    ActionArgumentContext createProposedArgumentInteractionContext(
            AuthenticationSession session,
            InteractionInvocationMethod invocationMethod,
            NakedObject targetObject,
            NakedObject[] args,
            int position);

    /**
     * Whether proposed value for this parameter is valid.
     * 
     * @param nakedObject
     * @param proposedValue
     * @return
     */
    String isValid(NakedObject nakedObject, Object proposedValue);

    
    NakedObject[] getChoices(NakedObject nakedObject);
    
    NakedObject getDefault(NakedObject nakedObject);
}
