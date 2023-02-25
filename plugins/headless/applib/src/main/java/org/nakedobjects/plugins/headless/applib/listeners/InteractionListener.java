package org.nakedobjects.plugins.headless.applib.listeners;

import org.nakedobjects.applib.events.ActionArgumentEvent;
import org.nakedobjects.applib.events.ActionInvocationEvent;
import org.nakedobjects.applib.events.ActionUsabilityEvent;
import org.nakedobjects.applib.events.ActionVisibilityEvent;
import org.nakedobjects.applib.events.CollectionAccessEvent;
import org.nakedobjects.applib.events.CollectionAddToEvent;
import org.nakedobjects.applib.events.CollectionMethodEvent;
import org.nakedobjects.applib.events.CollectionRemoveFromEvent;
import org.nakedobjects.applib.events.CollectionUsabilityEvent;
import org.nakedobjects.applib.events.CollectionVisibilityEvent;
import org.nakedobjects.applib.events.ObjectTitleEvent;
import org.nakedobjects.applib.events.ObjectValidityEvent;
import org.nakedobjects.applib.events.PropertyAccessEvent;
import org.nakedobjects.applib.events.PropertyModifyEvent;
import org.nakedobjects.applib.events.PropertyUsabilityEvent;
import org.nakedobjects.applib.events.PropertyVisibilityEvent;


public interface InteractionListener {

    /**
     * The title was read.
     * 
     * @param ev
     */
    void objectTitleRead(ObjectTitleEvent ev);

    /**
     * The object was persisted (or an attempt to persist it was made).
     * 
     * @param ev
     */
    void objectPersisted(ObjectValidityEvent ev);

    /**
     * A check was made to determine if a property was visible.
     * 
     * @param ev
     */
    void propertyVisible(PropertyVisibilityEvent ev);

    /**
     * A check was made to determine if a property was usable.
     * 
     * @param ev
     */
    void propertyUsable(PropertyUsabilityEvent ev);

    /**
     * A property was read.
     * 
     * <p>
     * Unlike most other events, a {@link PropertyAccessEvent} will never have been vetoed (that is,
     * {@link PropertyAccessEvent#isVeto()} will always be <tt>false</tt>).
     * 
     * @param ev
     */
    void propertyAccessed(PropertyAccessEvent ev);

    /**
     * A property was modified (or an attempt to modify it was made)
     * 
     * <p>
     * Use {@link PropertyModifyEvent#getProposed()} to determine whether the property was being set or
     * cleared.
     * 
     * @param ev
     */
    void propertyModified(PropertyModifyEvent ev);

    /**
     * A check was made to determine if a collection was visible.
     * 
     * <p>
     * Will be fired prior to {@link #collectionUsable(CollectionUsabilityEvent)}.
     * 
     * @param ev
     */
    void collectionVisible(CollectionVisibilityEvent ev);

    /**
     * A check was made to determine if a collection was usable.
     * 
     * <p>
     * Will be fired prior to either {@link #collectionAccessed(CollectionAccessEvent)} or
     * {@link #collectionAddedTo(CollectionAddToEvent)} or
     * {@link #collectionRemovedFrom(CollectionRemoveFromEvent)}.
     * 
     * @param ev
     */
    void collectionUsable(CollectionUsabilityEvent ev);

    /**
     * A collection was read.
     * 
     * <p>
     * Unlike most other events, a {@link CollectionAccessEvent} will never have been vetoed (that is,
     * {@link CollectionAccessEvent#isVeto()} will always be <tt>false</tt>).
     * 
     * @param ev
     */
    void collectionAccessed(CollectionAccessEvent ev);

    /**
     * An object was added to the collection (or an attempt to add it was made).
     * 
     * @param ev
     */
    void collectionAddedTo(CollectionAddToEvent ev);

    /**
     * An object was removed from the collection (or an attempt to remove it was made).
     * 
     * @param ev
     */
    void collectionRemovedFrom(CollectionRemoveFromEvent ev);

    /**
     * A method of a collection (such as <tt>isEmpty()</tt> or <tt>size()</tt>) has been invoked.
     * 
     * 
     * <p>
     * Unlike the other methods in this interface, the source of these events will be an instance of a
     * Collection (such as <tt>java.util.List</tt>) rather than the domain object. (The domain object is
     * {@link CollectionMethodEvent#getDomainObject() still available,  however).
     * 
     * @param interactionEvent
     */
    void collectionMethodInvoked(CollectionMethodEvent interactionEvent);

    /**
     * A check was made to determine if an action was visible.
     * 
     * <p>
     * Will be fired prior to {@link #actionUsable(ActionUsabilityEvent)}.
     * 
     * @param ev
     */
    void actionVisible(ActionVisibilityEvent interactionEvent);

    /**
     * A check was made to determine if an action was usable.
     * 
     * <p>
     * Will be fired prior to {@link #actionArgument(ActionArgumentEvent)}.
     * 
     * @param ev
     */
    void actionUsable(ActionUsabilityEvent ev);

    /**
     * A check was made as to whether an argument proposed for an action was valid.
     * 
     * <p>
     * Will be fired prior to {@link #actionInvoked(ActionInvocationEvent)}.
     * 
     * @param ev
     */
    void actionArgument(ActionArgumentEvent ev);

    /**
     * An action was invoked (or an attempt to invoke it was made).
     * 
     * @param ev
     */
    void actionInvoked(ActionInvocationEvent ev);

}
