package org.nakedobjects.applib.fixtures;

import org.nakedobjects.applib.profiles.Profile;
import org.nakedobjects.applib.profiles.ProfileService;
import org.nakedobjects.applib.profiles.ProfileServiceAware;

public abstract class UserProfileFixture extends AbstractFixture implements ProfileServiceAware {
	
    private ProfileService profileService;

    public UserProfileFixture() {
		super(FixtureType.USER_PROFILE);
	}
    
    public void setService(ProfileService profileService) {
        this.profileService = profileService;
    }

    public final void install() {
        installProfiles();
    }

    protected abstract void installProfiles();

    protected Profile newUserProfile() {
        return profileService.newUserProfile();
    }

    protected Profile newUserProfile(Profile profile) {
        return profileService.newUserProfile(profile);
    }

    protected void saveForUser(String name, Profile profile) {
        profileService.saveForUser(name, profile);
    }

    protected void saveAsDefault(Profile profile) {  
        profileService.saveAsDefault(profile);
    }

}

// Copyright (c) Naked Objects Group Ltd.
