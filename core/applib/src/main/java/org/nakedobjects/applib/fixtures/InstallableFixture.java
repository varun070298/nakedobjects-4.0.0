package org.nakedobjects.applib.fixtures;

public interface InstallableFixture {

	/**
	 * Determines whether the fixture will be {@link #install() install}ed, dependent
	 * on the state of the object (data) store and the user profile store.
	 */
	FixtureType getType();
	
	public void install();
}
