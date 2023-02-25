package org.nakedobjects.applib.fixtures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Indicates that the demo or test should be run as the specified user, with
 * the specified roles.
 * 
 * <p>
 * Note: this fixture does not in itself do anything (its {@link #install()} is a
 * no-op).  However, if present in the fixture list then is &quot;noticed&quot; by the framework,
 * and is used to automatically logon when the framework is booted (providing running in
 * prototype or exploration, not in production).
 * 
 * <p>
 * To change the user during the installation of fixtures, either use {@link SwitchUserFixture}.
 * 
 * @see SwitchUserFixture
 */
public class LogonFixture implements InstallableFixture {


	@SuppressWarnings("unchecked")
	private static List<String> asList(final String... roles) {
		return roles != null ? Arrays.asList(roles) : Collections.EMPTY_LIST;
	}

    private final String username;
    private final List<String> roles = new ArrayList<String>();
    
    public LogonFixture(final String username, final String... roles) {
        this(username, asList(roles));
    }

    public LogonFixture(final String username, final List<String> roles) {
        this.username = username;
        this.roles.addAll(roles);
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public final void install() {
        // does nothing; see comments above.
    }

	public FixtureType getType() {
		return FixtureType.OTHER;
	}

	@Override
    public String toString() {
    	return "LogonFixture [user: " + getUsername() + ", roles: " + getRoles() + "]";
    }

}
