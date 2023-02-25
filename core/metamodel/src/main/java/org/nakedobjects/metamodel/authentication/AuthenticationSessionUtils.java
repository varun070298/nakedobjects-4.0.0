package org.nakedobjects.metamodel.authentication;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.applib.security.RoleMemento;
import org.nakedobjects.applib.security.UserMemento;


public final class AuthenticationSessionUtils {

    private AuthenticationSessionUtils() {}

    public static UserMemento createUserMemento(final AuthenticationSession session) {
        final List<RoleMemento> roles = new ArrayList<RoleMemento>();
        for (String roleName : session.getRoles()) {
			roles.add(new RoleMemento(roleName));
        }
        return new UserMemento(session.getUserName(), roles);
    }
}

// Copyright (c) Naked Objects Group Ltd.
