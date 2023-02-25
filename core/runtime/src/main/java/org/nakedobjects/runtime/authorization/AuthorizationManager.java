package org.nakedobjects.runtime.authorization;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;


/**
 * Authorises the user in the current session view and use members of an object.
 */
public interface AuthorizationManager extends ApplicationScopedComponent {

    /**
     * Returns true when the user represented by the specified session is authorised to view the member of the
     * class/object represented by the member identifier. Normally the view of the specified field, or the
     * display of the action will be suppress if this returns false.
     */
    boolean isVisible(AuthenticationSession session, NakedObject target, Identifier identifier);

    /**
     * Returns true when the use represented by the specified session is authorised to change the field
     * represented by the member identifier. Normally the specified field will be not appear editable if this
     * returns false.
     */
    boolean isUsable(AuthenticationSession session, NakedObject target, Identifier identifier);
}
// Copyright (c) Naked Objects Group Ltd.
