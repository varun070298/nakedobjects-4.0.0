package org.nakedobjects.runtime.viewer;

import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.metamodel.commons.ensure.Ensure;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.ConfigurationBuilderAware;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.authentication.AuthenticationRequestPassword;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.installers.InstallerLookupAware;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.NakedObjectsSystem;
import org.nakedobjects.runtime.system.SystemConstants;
import org.nakedobjects.runtime.web.WebAppSpecification;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;


public abstract class NakedObjectsViewerAbstract implements NakedObjectsViewer {

    /**
     * @see {@link #setDeploymentType(DeploymentType)}
     */
    private DeploymentType deploymentType;

    private InstallerLookup installerLookup;
    private ConfigurationBuilder configurationBuilder;
    private NakedObjectsSystem system;

    /**
     * Optionally set, see {@link #setAuthenticationRequestViaArgs(AuthenticationRequest)}
     */
    private AuthenticationRequest authenticationRequestViaArgs;

    // ////////////////////////////////////////////////////////////////
    // Settings
    // ////////////////////////////////////////////////////////////////

    public void init() {

        ensureDependenciesInjected();

        NakedObjectConfiguration configuration = configurationBuilder.getConfiguration();
        deploymentType = DeploymentType.lookup(configuration.getString(SystemConstants.DEPLOYMENT_TYPE_KEY));

        String user = configuration.getString(SystemConstants.USER_KEY);
        String password = configuration.getString(SystemConstants.PASSWORD_KEY);

        if (user != null) {
            authenticationRequestViaArgs = new AuthenticationRequestPassword(user, password);
        }
    }

    public void shutdown() {
    // does nothing
    }

    // ////////////////////////////////////////////////////////////////
    // Settings
    // ////////////////////////////////////////////////////////////////

    public final DeploymentType getDeploymentType() {
        return deploymentType;
    }

    /**
     * Default implementation to return null, indicating that this viewer should not be run in a web
     * container.
     */
    public WebAppSpecification getWebAppSpecification() {
        return null;
    }

    public AuthenticationRequest getAuthenticationRequestViaArgs() {
        return authenticationRequestViaArgs;
    }

    protected void clearAuthenticationRequestViaArgs() {
        authenticationRequestViaArgs = null;
    }

    // ////////////////////////////////////////////////////////////////
    // Post-bootstrapping
    // ////////////////////////////////////////////////////////////////

    public LogonFixture getLogonFixture() {
        return system != null ? system.getLogonFixture() : null;
    }

    // ////////////////////////////////////////////////////////////////
    // Dependencies (injected)
    // ////////////////////////////////////////////////////////////////

    protected void ensureDependenciesInjected() {
        Ensure.ensureThatState(installerLookup, is(not(nullValue())));
        Ensure.ensureThatState(configurationBuilder, is(not(nullValue())));
    }

    /**
     * Injected by virtue of being {@link InstallerLookupAware}.
     */
    public void setInstallerLookup(final InstallerLookup installerLookup) {
        this.installerLookup = installerLookup;
    }

    protected ConfigurationBuilder getConfigurationBuilder() {
		return configurationBuilder;
	}
    
    /**
     * Injected by virtue of being {@link ConfigurationBuilderAware}.
     */
    public void setConfigurationBuilder(final ConfigurationBuilder configurationBuilder) {
        this.configurationBuilder = configurationBuilder;
    }

    // ////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    // ////////////////////////////////////////////////////////////////

    /**
     * Available after {@link NakedObjectsSystem} has been bootstrapped.
     */
    protected static NakedObjectConfiguration getConfiguration() {
        return NakedObjectsContext.getConfiguration();
    }

    /**
     * Available after {@link NakedObjectsSystem} has been bootstrapped.
     */
    public static AuthenticationManager getAuthenticationManager() {
        return NakedObjectsContext.getAuthenticationManager();
    }

}
