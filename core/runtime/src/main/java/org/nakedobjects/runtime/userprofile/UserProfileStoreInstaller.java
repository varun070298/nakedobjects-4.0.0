package org.nakedobjects.runtime.userprofile;

import org.nakedobjects.metamodel.commons.component.Installer;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;

public interface UserProfileStoreInstaller extends Installer  {

	public static String TYPE = "user-profile-store";

    UserProfileStore createUserProfileStore(final NakedObjectConfiguration nakedObjectConfiguration);

}


// Copyright (c) Naked Objects Group Ltd.
