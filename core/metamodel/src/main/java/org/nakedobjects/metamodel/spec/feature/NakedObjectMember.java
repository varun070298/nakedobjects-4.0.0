package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.interactions.AccessContext;
import org.nakedobjects.metamodel.interactions.InteractionContext;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.VisibilityContext;


/**
 * Provides reflective access to an action or a field on a domain object.
 */
public interface NakedObjectMember extends NakedObjectFeature {

    // /////////////////////////////////////////////////////////////
    // Identifiers
    // /////////////////////////////////////////////////////////////

    /**
     * Returns the identifier of the member, which must not change. This should be all camel-case with no
     * spaces: so if the member is called 'Return Date' then the a suitable id would be 'ReturnDate'.
     */
    String getId();

    // /////////////////////////////////////////////////////////////
    // Name, Description, Help (convenience for facets)
    // /////////////////////////////////////////////////////////////

    /**
     * Return the help text for this member - the field or action - to complement the description.
     * 
     * @see #getDescription()
     */
    String getHelp();

    // /////////////////////////////////////////////////////////////
    // Hidden (or visible)
    // /////////////////////////////////////////////////////////////

    /**
     * Create an {@link InteractionContext} to represent an attempt to view this member (that is, to check if
     * it is visible or not).
     * 
     * <p>
     * Typically it is easier to just call {@link #isVisible(AuthenticationSession, NakedObject)} or
     * {@link #isVisibleResult(AuthenticationSession, NakedObject)}; this is provided as API for symmetry with interactions
     * (such as {@link AccessContext} accesses) have no corresponding vetoing methods.
     * 
     * @param session
     * @param targetNakedObject
     * @return
     */
    public VisibilityContext<?> createVisibleInteractionContext(
            AuthenticationSession session,
            InteractionInvocationMethod invocationMethod,
            NakedObject targetNakedObject);

    /**
     * Determines if this member is visible, represented as a {@link Consent}.
     * 
     * @param session
     * @param target
     *            may be <tt>null</tt> if just checking for authorization.
     * 
     * @see #isVisibleResult(AuthenticationSession, NakedObject)
     */
    Consent isVisible(AuthenticationSession session, NakedObject target);

    // /////////////////////////////////////////////////////////////
    // Disabled (or enabled)
    // /////////////////////////////////////////////////////////////

    /**
     * Create an {@link InteractionContext} to represent an attempt to {@link InteractionContextType#MEMBER_USABLE
     * use this member} (that is, to check if it is usable or not).
     * 
     * <p>
     * Typically it is easier to just call {@link #isUsable(AuthenticationSession, NakedObject)} or
     * {@link #isUsableResult(AuthenticationSession, NakedObject)}; this is provided as API for symmetry with interactions
     * (such as {@link AccessContext} accesses) have no corresponding vetoing methods.
     * 
     * @param session
     * @param target
     * @return
     */
    public UsabilityContext<?> createUsableInteractionContext(
            AuthenticationSession session,
            InteractionInvocationMethod invocationMethod,
            NakedObject target);

    /**
     * Determines whether this member is usable, represented as a {@link Consent}.
     * 
     * @param session
     * @param target
     *            may be <tt>null</tt> if just checking for authorization.
     * 
     * @see #isUsableResult(AuthenticationSession, NakedObject)
     */
    Consent isUsable(AuthenticationSession session, NakedObject target);

    
    // /////////////////////////////////////////////////////////////
    // isAssociation, isAction
    // /////////////////////////////////////////////////////////////

    /**
     * Whether this member represents a {@link NakedObjectAssociation}.
     * 
     * <p>
     * If so, can be safely downcast to {@link NakedObjectAssociation}.
     */
    public boolean isAssociation();

    /**
     * Whether this member represents a {@link OneToManyAssociation}.
     * 
     * <p>
     * If so, can be safely downcast to {@link OneToManyAssociation}.
     */
    boolean isOneToManyAssociation();

    /**
     * Whether this member represents a {@link OneToOneAssociation}.
     * 
     * <p>
     * If so, can be safely downcast to {@link OneToOneAssociation}.
     */
    boolean isOneToOneAssociation();

    /**
     * Whether this member represents a {@link NakedObjectAction}.
     * 
     * <p>
     * If so, can be safely downcast to {@link NakedObjectAction}.
     */
    public boolean isAction();
    

    // /////////////////////////////////////////////////////////////
    // Debugging
    // /////////////////////////////////////////////////////////////

    String debugData();


    

}
