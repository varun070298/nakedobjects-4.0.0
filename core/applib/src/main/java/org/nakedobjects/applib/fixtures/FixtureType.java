package org.nakedobjects.applib.fixtures;

/**
 * Enumerates the different types of {@link InstallableFixture fixture}s supported.
 * 
 * @see InstallableFixture#getType()
 */
public enum FixtureType {
	/**
	 * A fixture that installs data (either reference data or operational data)
	 * into an object store.
	 * 
	 * <p>
	 * Some object stores are in-memory only, in which case these will always want
	 * fixtures of this type to be installed.  However, for object stores that
	 * persist the data (such as XML or to an RDBMS), these typically do <i>not</i>
	 * want data fixtures run (except possibly for the very first time booted to
	 * initially seed them). 
	 */
	OBJECT_STORE,
	/**
	 * Analogous to {@link FixtureType#DATA}, but for fixtures (in particular, the
	 * {@link PerspectiveFixture}) that are used to setup {@link Profile}s and their
	 * {@link Perspective}s.
	 * 
	 * @see UserProfileService
	 */
	USER_PROFILE,
	/**
	 * A fixture that neither installs data into the object store nor perspectives
	 * into the {@link UserProfileService}.
	 * 
	 * <p>
	 * Fixtures of this type are always installed.  Typical examples are:
	 * <ul>
	 * <li> composite fixtures that just aggregate other fixtures
	 * <li> fixtures that set up the date/time (see {@link DateFixture})
	 * <li> fixtures that specify the user to logon as (see {@link LogonFixture}).
	 * </ul>
	 */
	OTHER;
}