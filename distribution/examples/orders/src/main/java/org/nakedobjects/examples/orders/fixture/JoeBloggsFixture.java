package org.nakedobjects.examples.orders.fixture;

import org.nakedobjects.applib.profiles.Profile;
import org.nakedobjects.applib.fixtures.UserProfileFixture;

public class JoeBloggsFixture extends UserProfileFixture {

    public void installProfiles() {
        Profile profile = newUserProfile();
        saveForUser("jbloggs", profile);
    }

}
