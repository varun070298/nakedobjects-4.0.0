package org.nakedobjects.runtime.authentication;

import java.util.List;

public interface AuthenticationRequest {

    String getName();

    List<String> getRoles();

    void setRoles(List<String> roles);

}

// Copyright (c) Naked Objects Group Ltd.
