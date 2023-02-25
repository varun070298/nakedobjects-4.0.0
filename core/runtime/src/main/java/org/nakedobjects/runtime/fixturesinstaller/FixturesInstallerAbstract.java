package org.nakedobjects.runtime.fixturesinstaller;

import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.runtime.installers.InstallerAbstract;

public abstract class FixturesInstallerAbstract extends InstallerAbstract implements FixturesInstaller {

	private final FixturesInstallerDelegate delegate = new FixturesInstallerDelegate();
    
	private LogonFixture logonFixture;

    public FixturesInstallerAbstract(String name) {
		super(FixturesInstaller.TYPE, name);
	}

    public void installFixtures() {
    	addFixturesTo(delegate);
    	
        delegate.installFixtures();
        logonFixture = delegate.getLogonFixture();
    }

    /**
     * Add fixtures to {@link FixturesInstallerDelegate#addFixture(Object) delegate}; these
     * are then installed.
     */
    protected abstract void addFixturesTo(FixturesInstallerDelegate delegate);

    public LogonFixture getLogonFixture() {
    	return logonFixture;
    }



}
