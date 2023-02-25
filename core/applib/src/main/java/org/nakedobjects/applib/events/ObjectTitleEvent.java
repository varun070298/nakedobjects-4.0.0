package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents an access (reading) of an object's title.
 * 
 * <p>
 * The {@link #getReason()} will always be <tt>null</tt>; access is always allowed.
 */
public class ObjectTitleEvent extends AccessEvent {

    private static final long serialVersionUID = 1L;

    private final String title;

    public ObjectTitleEvent(final Object source, final Identifier classIdentifier, final String title) {
        super(source, classIdentifier);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
