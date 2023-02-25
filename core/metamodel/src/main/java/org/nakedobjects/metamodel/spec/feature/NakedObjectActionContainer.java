package org.nakedobjects.metamodel.spec.feature;

import java.util.List;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public interface NakedObjectActionContainer {

    /**
     * TODO: convert to relatedResourceActions
     */
    NakedObjectAction[] getServiceActionsFor(NakedObjectActionType... type);

    /**
     * Returns the action of the specified type with the specified signature.
     */
    NakedObjectAction getObjectAction(NakedObjectActionType type, String id, NakedObjectSpecification[] parameters);

    /**
     * Get the action object represented by the specified identity string.
     * 
     * <p>
     * The identity string should be {@link Identifier#toNameParmsIdentityString()}</tt>.
     */
    NakedObjectAction getObjectAction(NakedObjectActionType type, String nameAndParmsIdentityString);

    /**
     * Returns an array of actions of the specified type(s).
     * 
     * <p>
     * If the type is <tt>null</tt>, then returns all {@link NakedObjectActionConstants#USER user},
     * {@link NakedObjectActionConstants#EXPLORATION exploration} and {@link NakedObjectActionConstants#DEBUG
     * debug} actions (but not {@link NakedObjectActionConstants#SET action sets}).
     */
    NakedObjectAction[] getObjectActions(NakedObjectActionType... type);

    /**
     * As per {@link #getObjectActions(NakedObjectActionType)}, but returned as a {@link List}. 
     */
    List<? extends NakedObjectAction> getObjectActionList(NakedObjectActionType... type);

}
