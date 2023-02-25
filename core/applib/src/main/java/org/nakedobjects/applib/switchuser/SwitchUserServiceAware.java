package org.nakedobjects.applib.switchuser;

import org.nakedobjects.applib.fixtures.AbstractFixture;

/**
 * Implement if require {@link SwitchUserService} to be injected into fixture.
 * 
 * <p>
 * Most fixtures will subclass from {@link AbstractFixture} which does indeed
 * implement this interface.
 */
public interface SwitchUserServiceAware {

	void setService(SwitchUserService switchUserService);
}

// Copyright (c) Naked Objects Group Ltd.
