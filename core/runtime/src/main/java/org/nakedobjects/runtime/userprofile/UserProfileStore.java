package org.nakedobjects.runtime.userprofile;


public interface UserProfileStore {

    boolean isFixturesInstalled();

    void save(String userName, UserProfile userProfile);

    UserProfile getUserProfile(String userName);


}


// Copyright (c) Naked Objects Group Ltd.
