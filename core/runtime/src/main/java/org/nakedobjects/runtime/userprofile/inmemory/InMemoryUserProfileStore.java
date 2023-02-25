package org.nakedobjects.runtime.userprofile.inmemory;

import java.util.HashMap;
import java.util.Map;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.runtime.userprofile.UserProfile;
import org.nakedobjects.runtime.userprofile.UserProfileStore;

public class InMemoryUserProfileStore implements UserProfileStore, DebugInfo {
	
    private static final Map<String, UserProfile> profiles = new HashMap<String, UserProfile>();
    
    public boolean isFixturesInstalled() {
    	return false;
    }
    
    public UserProfile getUserProfile(String name) {
        return profiles.get(name);
    }

    public void save(String name, UserProfile userProfile) {
        profiles.put(name, userProfile);
    }

    public void debugData(DebugString debug) {
        for (String name : profiles.keySet()) {
            debug.appendln(name, profiles.get(name));
        }
    }

    public String debugTitle() {
        return "TestProfilePersistor";
    }

}


// Copyright (c) Naked Objects Group Ltd.
