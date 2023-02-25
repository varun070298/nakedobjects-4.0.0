package org.nakedobjects.applib.profiles;

import org.nakedobjects.applib.fixtures.UserProfileFixture;

/**
 * Not intended to be implemented directly; is implemented by {@link UserProfileFixture}.
 * 
 * <p>
 * If using perspectives then subclass from {@link UserProfileFixture}.
 */
public interface ProfileServiceAware {

	public void setService(ProfileService perspectiveInstaller);
}
