package org.nakedobjects.plugins.xml.profilestore;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.installers.InstallerAbstract;
import org.nakedobjects.runtime.userprofile.UserProfileStore;
import org.nakedobjects.runtime.userprofile.UserProfileStoreInstaller;

public class XmlUserProfileStoreLoaderInstaller extends InstallerAbstract implements UserProfileStoreInstaller {

    public XmlUserProfileStoreLoaderInstaller() {
		super(UserProfileStoreInstaller.TYPE, "xml");
	}

	public UserProfileStore createUserProfileStore(NakedObjectConfiguration configuration) {
		return new XmlUserProfileStore(configuration);
	}

}


// Copyright (c) Naked Objects Group Ltd.
