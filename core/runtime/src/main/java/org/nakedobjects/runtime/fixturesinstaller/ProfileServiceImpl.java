package org.nakedobjects.runtime.fixturesinstaller;

import org.nakedobjects.applib.profiles.Perspective;
import org.nakedobjects.applib.profiles.Profile;
import org.nakedobjects.applib.profiles.ProfileService;
import org.nakedobjects.applib.profiles.ProfileServiceAware;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.userprofile.PerspectiveEntry;
import org.nakedobjects.runtime.userprofile.UserProfile;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;

public class ProfileServiceImpl implements ProfileService {

	public Profile newUserProfile() {
		return new ProfileImpl();
	}

	public Profile newUserProfile(Profile profileTemplate) {
		return new ProfileImpl((ProfileImpl) profileTemplate);
	}

	public void saveAsDefault(Profile profile) {
		getUserProfileLoader().saveAsDefault(createUserProfile(profile));
	}

	public void saveForUser(String name, Profile profile) {
		getUserProfileLoader().saveForUser(name, createUserProfile(profile));
	}

	private UserProfile createUserProfile(Profile profile) {
		return ((ProfileImpl) profile).getUserProfile();
	}

	public void injectInto(Object fixture) {
		if (fixture instanceof ProfileServiceAware) {
			ProfileServiceAware serviceAware = (ProfileServiceAware) fixture;
			serviceAware.setService(this);
		}
	}

	private static UserProfileLoader getUserProfileLoader() {
		return NakedObjectsContext.getUserProfileLoader();
	}

}

class ProfileImpl implements Profile {
	private final UserProfile userProfile;

	public ProfileImpl(ProfileImpl profileTemplate) {
		this();
		userProfile.copy(profileTemplate.userProfile);
	}

	public ProfileImpl() {
		userProfile = new UserProfile();
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void addToOptions(String name, String value) {
		userProfile.addToOptions(name, value);
	}

	public void addToPerspectives(Perspective perspective) {
		userProfile.addToPerspectives(((PerspectiveImpl) perspective)
				.getPerspectiveEntry());
	}

	public Perspective getPerspective(String name) {
		PerspectiveEntry perspectiveEntry = userProfile.getPerspective(name);
		if (perspectiveEntry == null) {
			throw new NakedObjectException("No perspective found for "
					+ name);
		}
		return new PerspectiveImpl(perspectiveEntry);
	}

	public Perspective newPerspective(String name) {
		PerspectiveEntry perspectiveEntry = userProfile.newPerspective(name);
		return new PerspectiveImpl(perspectiveEntry);
	}

}

class PerspectiveImpl implements Perspective {
	private final PerspectiveEntry entry;

	public PerspectiveImpl(PerspectiveEntry perspectiveEntry) {
		entry = perspectiveEntry;
	}

	public PerspectiveEntry getPerspectiveEntry() {
		return entry;
	}

	public void addGenericRepository(Class<?>... classes) {
		for (Class<?> cls : classes) {
			entry.addGenericRepository(cls);
		}
	}

	public void addToObjects(Object... objects) {
		for (Object object : objects) {
			entry.addToObjects(object);
		}
	}

	public Object addToServices(Class<?> cls) {
		return entry.addToServices(cls);
	}

	public void addToServices(Class<?>... classes) {
		for (Class<?> cls : classes) {
			entry.addToServices(cls);
		}
	}

	public void removeFromServices(Class<?>... classes) {
		for (Class<?> cls : classes) {
			entry.removeFromServices(cls);
		}
	}

}

// Copyright (c) Naked Objects Group Ltd.
