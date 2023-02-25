package org.nakedobjects.applib.events;

/**
 * Makes it easier to process different events that hold a single proposed argument (such as
 * {@link CollectionAddToEvent} and {@link PropertyModifyEvent}).
 */
public interface ProposedHolderEvent {

    Object getProposed();

    String getMemberNaturalName();

}
