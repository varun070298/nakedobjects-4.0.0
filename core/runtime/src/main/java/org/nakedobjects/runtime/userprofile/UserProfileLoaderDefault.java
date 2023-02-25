package org.nakedobjects.runtime.userprofile;

import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.session.NakedObjectSession;

/**
 * Acts like a bridge, loading the profile from the underlying store.
 */
public class UserProfileLoaderDefault implements UserProfileLoader, DebugInfo {
	
    private static final String DEFAULT_PERSPECTIVE_NAME = "Naked Objects Exploration";

	private Logger LOG = Logger.getLogger(UserProfile.class);

    public static enum Mode {
    	/**
    	 * Must provide some services.
    	 */
    	STRICT,
    	/**
    	 * For testing only, no services is okay.
    	 */
    	RELAXED
    }
    

    private final UserProfileStore store;
    private final Mode mode;
    
    private UserProfile userProfile;

	private List<Object> serviceList;



    
    ////////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////////

    public UserProfileLoaderDefault(final UserProfileStore store) {
        this(store, Mode.STRICT);
    }

    /**
     * for testing purposes, explicitly specify the Mode.
     */
    public UserProfileLoaderDefault(final UserProfileStore store, final Mode mode) {
        this.store = store;
        this.mode = mode;
    }

    ////////////////////////////////////////////////////////
    // init, shutdown
    ////////////////////////////////////////////////////////

	/**
	 * Does nothing.
	 */
	public void init() {
	}


	/**
	 * Does nothing.
	 */
	public void shutdown() {
	}

    ////////////////////////////////////////////////////////
    // Fixtures
    ////////////////////////////////////////////////////////

    /**
     * @see PersistenceSession#isFixturesInstalled()
     */
    public boolean isFixturesInstalled() {
        return store.isFixturesInstalled(); 
    }


    ////////////////////////////////////////////////////////
    // saveAs...
    ////////////////////////////////////////////////////////
    
    public void saveAsDefault(UserProfile userProfile) {
        store.save("_default", userProfile);
    }

    public void saveForUser(String userName, UserProfile userProfile) {
        store.save(userName, userProfile);
    }

    ////////////////////////////////////////////////////////
    // saveSession
    ////////////////////////////////////////////////////////

    public void saveSession(List<NakedObject> objects) {
        loadOrCreateProfile();
        userProfile.saveObjects(objects);
        save(userProfile);
    }

    private void save(UserProfile userProfile) {
        saveForUser(userName(), userProfile);
    }

    
    ////////////////////////////////////////////////////////
    // getProfile
    ////////////////////////////////////////////////////////

    public UserProfile getProfile(AuthenticationSession session) {
        String userName = session.getUserName();
        UserProfile profile = store.getUserProfile(userName);
        userProfile =  profile != null ? profile : createUserProfile(userName);
        return userProfile;
    }

    @Deprecated
    public UserProfile getProfile() {
        loadOrCreateProfile();
        return userProfile;
    }

    
    ////////////////////////////////////////////////////////
    // Helpers: (for getProfile)
    ////////////////////////////////////////////////////////

    private void loadOrCreateProfile() {
        if (userProfile == null) {
            String userName = userName();
            UserProfile profile = store.getUserProfile(userName);
            userProfile =  profile != null ? profile : createUserProfile(userName);
        }
    }


    private UserProfile createUserProfile(String userName) {
        UserProfile template = store.getUserProfile("_default");
		if (template == null) {
			return createDefaultProfile(userName);
		} else {
			return createProfileFromTemplate(userName, template);
		}
    }
    

    private UserProfile createDefaultProfile(String userName) {
        UserProfile profile = new UserProfile();
        profile.newPerspective(DEFAULT_PERSPECTIVE_NAME);

        List<Object> services = getServices();
        if (services.size() == 0 && mode == Mode.STRICT) {
            throw new NakedObjectException("No known services");
        }
        for (Object service : services) {
            profile.getPerspective().addToServices(service);
        }
        LOG.info("creating exploration UserProfile for " + userName);
        return profile;
    }


    private UserProfile createProfileFromTemplate(String userName, UserProfile template) {
        UserProfile userProfile = new UserProfile();
        userProfile.copy(template);
        LOG.info("creating UserProfile, from template, for " + userName);
        return userProfile;
    }

    
    ////////////////////////////////////////////////////////
    // Debugging
    ////////////////////////////////////////////////////////
    
    public void debugData(DebugString debug) {
        debug.append(store);
        debug.append(userProfile);
    }

    public String debugTitle() {
        return "User Profile Service";
    }


    ////////////////////////////////////////////////////////
    // Dependencies (injected via setters)
    ////////////////////////////////////////////////////////
	
	public List<Object> getServices() {
		return serviceList;
	}

	public void setServices(List<Object> serviceList) {
		this.serviceList = serviceList;
	}

    ////////////////////////////////////////////////////////
    // Dependencies (from context)
    ////////////////////////////////////////////////////////
	
	private static AuthenticationSession getAuthenticationSession() {
		return getSession().getAuthenticationSession();
	}

    private static String userName() {
        return getAuthenticationSession().getUserName();
    }

	private static NakedObjectSession getSession() {
		return NakedObjectsContext.getSession();
	}

	

}


// Copyright (c) Naked Objects Group Ltd.
