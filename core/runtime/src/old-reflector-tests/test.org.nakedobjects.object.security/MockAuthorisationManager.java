package test.org.nakedobjects.object.security;

import org.nakedobjects.noa.authentication.Sessionessionession;
import org.nakedobjects.nof.reflect.peer.MemberIdentifier;
import org.nakedobjects.nof.reflect.security.AuthorisationManager;


public class MockAuthorisationManager implements AuthorisationManager {
    private boolean visible;
    private boolean usable;

    public boolean isUsable(final Session session, final MemberIdentifier identifier) {
        return usable;
    }

    public boolean isVisible(final Session session, final MemberIdentifier identifier) {
        return visible;
    }

    public void setupVisible(final boolean b) {
        visible = b;
    }

    public void setupUsable(final boolean usable) {
        this.usable = usable;
    }

    public void init() {}

    public void shutdown() {}

    public boolean isEditable(final Session session, final MemberIdentifier identifier) {
        return false;
    }
}
// Copyright (c) Naked Objects Group Ltd.
