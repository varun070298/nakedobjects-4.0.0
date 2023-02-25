package org.nakedobjects.applib.switchuser;

import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.applib.profiles.ProfileService;


/**
 * Not intended to be used directly; decouples the {@link AbstractFixture}, which needs to
 * be able to switch users dynamically, from the rest of the framework's runtime.
 * 
 * <p>
 * A suitable implementation is injected into {@link AbstractFixture} when installed.
 * 
 * @see ProfileService
 */
public interface SwitchUserService {

    /**
     * Switches the current user with the list of specified roles.
     */
    void switchUser(String username, String... roles);
}

// Copyright (c) Naked Objects Group Ltd.
