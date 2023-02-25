package org.nakedobjects.runtime.userprofile;

import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.runtime.persistence.PersistenceSession;

/**
 * Acts like a bridge, loading the profile from the underlying store.
 * 
 * <p>
 * This is an interface only to make it easy to mock in tests.  In practice
 * there is only a single implementation, {@link UserProfileLoaderDefault}.
 */
public interface UserProfileLoader extends ApplicationScopedComponent {
	
    
    ////////////////////////////////////////////////////////
    // Fixtures
    ////////////////////////////////////////////////////////

    /**
     * @see PersistenceSession#isFixturesInstalled()
     */
    public boolean isFixturesInstalled();


    ////////////////////////////////////////////////////////
    // saveAs...
    ////////////////////////////////////////////////////////
    
    public void saveAsDefault(UserProfile userProfile);

    public void saveForUser(String userName, UserProfile userProfile);

    ////////////////////////////////////////////////////////
    // saveSession
    ////////////////////////////////////////////////////////

    public void saveSession(List<NakedObject> objects);

    
    ////////////////////////////////////////////////////////
    // getProfile
    ////////////////////////////////////////////////////////

    public UserProfile getProfile(AuthenticationSession session);

    @Deprecated
    public UserProfile getProfile();


    ////////////////////////////////////////////////////////
    // Services
    ////////////////////////////////////////////////////////

	public void setServices(List<Object> servicesList);
    List<Object> getServices();


    

}


// Copyright (c) Naked Objects Group Ltd.
