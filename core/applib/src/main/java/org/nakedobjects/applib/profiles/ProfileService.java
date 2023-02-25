package org.nakedobjects.applib.profiles;

import org.nakedobjects.applib.fixtures.UserProfileFixture;
import org.nakedobjects.applib.switchuser.SwitchUserService;


/**
 * Not intended to be used directly; decouples the {@link UserProfileFixture}, which needs to persist {@link Perspective}s,
 * from the rest of the framework's runtime.
 * 
 * <p>
 * A suitable implementation is injected into {@link UserProfileFixture} when installed.
 * 
 * @see SwitchUserService
 */
public interface ProfileService {

    Profile newUserProfile();

    Profile newUserProfile(Profile profile);

    void saveForUser(String name, Profile profile);

    void saveAsDefault(Profile profile);

}
