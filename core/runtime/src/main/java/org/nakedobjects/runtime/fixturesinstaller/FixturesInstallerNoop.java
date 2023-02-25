package org.nakedobjects.runtime.fixturesinstaller;

import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.metamodel.commons.component.Noop;


public class FixturesInstallerNoop extends FixturesInstallerAbstract implements Noop {

    public FixturesInstallerNoop() {
		super("noop");
	}

	@Override
	public void installFixtures() {}

	@Override
	public LogonFixture getLogonFixture() {
		return null;
	}

	@Override
	protected void addFixturesTo(FixturesInstallerDelegate delegate) {
	}

}


// Copyright (c) Naked Objects Group Ltd.
