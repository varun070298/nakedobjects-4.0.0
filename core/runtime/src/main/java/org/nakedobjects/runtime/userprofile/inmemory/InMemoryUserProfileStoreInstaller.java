package org.nakedobjects.runtime.userprofile.inmemory;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.installers.InstallerAbstract;
import org.nakedobjects.runtime.userprofile.UserProfileStore;
import org.nakedobjects.runtime.userprofile.UserProfileStoreInstaller;

public class InMemoryUserProfileStoreInstaller extends InstallerAbstract implements
		UserProfileStoreInstaller {

	public InMemoryUserProfileStoreInstaller() {
		super(UserProfileStoreInstaller.TYPE, "in-memory");
	}
	
	public UserProfileStore createUserProfileStore(
			NakedObjectConfiguration nakedObjectConfiguration) {
		return new InMemoryUserProfileStore();
	}


}
