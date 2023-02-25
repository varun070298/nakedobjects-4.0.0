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


/**
 * Provides no-op implementations of each of the methods within {@link InteractionListener}, to simplify the
 * creation of new listeners.
 */
public class InteractionAdapter implements InteractionListener {

    public void propertyVisible(final PropertyVisibilityEvent ev) {}

    public void propertyUsable(final PropertyUsabilityEvent ev) {}

    public void propertyAccessed(final PropertyAccessEvent ev) {}

    public void propertyModified(final PropertyModifyEvent ev) {}

    public void collectionVisible(final CollectionVisibilityEvent ev) {}

    public void collectionUsable(final CollectionUsabilityEvent ev) {}

    public void collectionAccessed(final CollectionAccessEvent ev) {}

    public void collectionAddedTo(final CollectionAddToEvent ev) {}

    public void collectionRemovedFrom(final CollectionRemoveFromEvent ev) {}

    public void collectionMethodInvoked(final CollectionMethodEvent interactionEvent) {}

    public void actionVisible(final ActionVisibilityEvent interactionEvent) {}

    public void actionUsable(final ActionUsabilityEvent ev) {}

    public void actionArgument(final ActionArgumentEvent ev) {}

    public void actionInvoked(final ActionInvocationEvent ev) {}

    public void objectPersisted(final ObjectValidityEvent ev) {}

    public void objectTitleRead(final ObjectTitleEvent ev) {}

}
