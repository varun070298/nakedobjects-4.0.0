package org.nakedobjects.runtime.fixturesinstaller;

import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.commons.component.Installer;


public interface FixturesInstaller extends Installer {
	
	/**
	 * NB: this has the suffix '-installer' because in the command line we must 
	 * distinguish from the '--fixture' flag meaning a particular fixture to install
	 * (whereas this flag means how to install them).
	 */
	static String TYPE = "fixtures-installer";
	
	void installFixtures();

    /**
     * The {@link Fixture} (if any) added via {@link #installFixture(Fixture)} that is an 
     * instance of {@link LogonFixture}.
     * 
     * <p>
     * If there is more than one {@link LogonFixture}, then the last one installed is returned.
     */
	LogonFixture getLogonFixture();
}
// Copyright (c) Naked Objects Group Ltd.
