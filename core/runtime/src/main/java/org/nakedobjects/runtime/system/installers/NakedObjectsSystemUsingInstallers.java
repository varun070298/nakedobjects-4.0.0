package org.nakedobjects.runtime.system.installers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatState;

import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.specloader.FacetDecoratorInstaller;
import org.nakedobjects.metamodel.specloader.NakedObjectReflector;
import org.nakedobjects.metamodel.specloader.NakedObjectReflectorInstaller;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.AuthenticationManagerInstaller;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManagerInstaller;
import org.nakedobjects.runtime.fixturesinstaller.FixturesInstaller;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.imageloader.TemplateImageLoaderInstaller;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.persistence.PersistenceMechanismInstaller;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.internal.RuntimeContextFromSession;
import org.nakedobjects.runtime.persistence.services.ServicesInstaller;
import org.nakedobjects.runtime.remoting.ClientConnectionInstaller;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactoryDefault;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.NakedObjectSystemException;
import org.nakedobjects.runtime.system.NakedObjectsSystemAbstract;
import org.nakedobjects.runtime.system.SystemConstants;
import org.nakedobjects.runtime.transaction.facetdecorator.standard.TransactionFacetDecoratorInstaller;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;
import org.nakedobjects.runtime.userprofile.UserProfileLoaderDefault;
import org.nakedobjects.runtime.userprofile.UserProfileStore;
import org.nakedobjects.runtime.userprofile.UserProfileStoreInstaller;


public class NakedObjectsSystemUsingInstallers extends NakedObjectsSystemAbstract {

    public static final Logger LOG = Logger.getLogger(NakedObjectsSystemUsingInstallers.class);

    private final InstallerLookup installerLookup;

    private AuthenticationManagerInstaller authenticationInstaller;
    private AuthorizationManagerInstaller authorizationInstaller;
    private NakedObjectReflectorInstaller reflectorInstaller;
    private ServicesInstaller servicesInstaller;
    private UserProfileStoreInstaller userProfileStoreInstaller;
    private PersistenceMechanismInstaller persistenceMechanismInstaller;
    private FixturesInstaller fixtureInstaller;


    // ///////////////////////////////////////////
    // Constructors
    // ///////////////////////////////////////////

    public NakedObjectsSystemUsingInstallers(final DeploymentType deploymentType, final InstallerLookup installerLookup) {
        super(deploymentType);
        ensureThatArg(installerLookup, is(not(nullValue())));
        this.installerLookup = installerLookup;
    }

    // ///////////////////////////////////////////
    // InstallerLookup
    // ///////////////////////////////////////////

    /**
     * As per {@link #NakedObjectsSystemUsingInstallers(DeploymentType, InstallerLookup) constructor}.
     */
    public InstallerLookup getInstallerLookup() {
        return installerLookup;
    }

    // ///////////////////////////////////////////
    // Create context hooks
    // ///////////////////////////////////////////

    public NakedObjectSessionFactory doCreateSessionFactory(final DeploymentType deploymentType)
            throws NakedObjectSystemException {
        final PersistenceSessionFactory persistenceSessionFactory = obtainPersistenceSessionFactory(deploymentType);
        final UserProfileLoader userProfileLoader = new UserProfileLoaderDefault(obtainUserProfileStore());
        return createSessionFactory(deploymentType, userProfileLoader, persistenceSessionFactory);
    }

    /**
     * Overloaded version designed to be called by subclasses that need to explicitly specify different
     * persistence mechanisms.
     * 
     * <p>
     * This is <i>not</i> a hook method, rather it is designed to be called <i>from</i> the
     * {@link #doCreateSessionFactory(DeploymentType) hook method}.
     */
    protected final NakedObjectSessionFactory createSessionFactory(
            final DeploymentType deploymentType,
            final UserProfileLoader userProfileLoader,
            final PersistenceSessionFactory persistenceSessionFactory) throws NakedObjectSystemException {

        final NakedObjectConfiguration configuration = getConfiguration();
        final AuthenticationManager authenticationManager = obtainAuthenticationManager(deploymentType);
        final AuthorizationManager authorizationManager = obtainAuthorizationManager(deploymentType);
        final TemplateImageLoader templateImageLoader = obtainTemplateImageLoader();
        final NakedObjectReflector reflector = obtainReflector(deploymentType);
        
        final List<Object> servicesList = obtainServices();

        // bind metamodel to the (runtime) framework
        // REVIEW: misplaced? seems like a side-effect... 
        reflector.setRuntimeContext(new RuntimeContextFromSession());

        return new NakedObjectSessionFactoryDefault(deploymentType, configuration, templateImageLoader, reflector,
                authenticationManager, authorizationManager, userProfileLoader, persistenceSessionFactory, servicesList);

    }

    // ///////////////////////////////////////////
    // Configuration
    // ///////////////////////////////////////////

	/**
     * Returns a <i>snapshot</i> of the {@link NakedObjectConfiguration configuration} held by the
     * {@link #getInstallerLookup() installer lookup}.
     * 
     * @see InstallerLookup#getConfiguration()
     */
    @Override
    public NakedObjectConfiguration getConfiguration() {
        return installerLookup.getConfiguration();
    }

    // ///////////////////////////////////////////
    // Authentication & Authorization
    // ///////////////////////////////////////////

    public void lookupAndSetAuthenticatorAndAuthorization(DeploymentType deploymentType) {

    	NakedObjectConfiguration configuration = installerLookup.getConfiguration();
    	String connection = configuration.getString(SystemConstants.CLIENT_CONNECTION_KEY);
    	
        if (connection != null) {
            lookupAndSetAuthenticatorAndAuthorizationUsingClientConnectionInstaller(connection);
        } else {
            lookupAndSetAuthenticatorAndAuthorizationInstallers(deploymentType);
        }
    }

	private void lookupAndSetAuthenticatorAndAuthorizationUsingClientConnectionInstaller(
			String connection) {
		ClientConnectionInstaller clientConnectionInstaller = installerLookup.clientConnectionInstaller(connection);
		if (clientConnectionInstaller == null) {
			return;
		}
		setAuthenticationInstaller(clientConnectionInstaller);
		setAuthorizationInstaller(clientConnectionInstaller);
	}

	private void lookupAndSetAuthenticatorAndAuthorizationInstallers(DeploymentType deploymentType) {
		// use the one specified in configuration
		final AuthenticationManagerInstaller authenticationInstaller = installerLookup.authenticationManagerInstaller(null, deploymentType.isExploring());
		if (authenticationInstaller != null) {
		    setAuthenticationInstaller(authenticationInstaller);
		}

		// use the one specified in configuration
		final AuthorizationManagerInstaller authorizationInstaller = installerLookup.authorizationManagerInstaller(null, !deploymentType.isProduction());
		if (authorizationInstaller != null) {
		    setAuthorizationInstaller(authorizationInstaller);
		}
	}

    /**
     * Set the type of connection to used to access the server.
     * 
     * <p>
     * Note that the {@link NakedObjectSessionFactoryUsingInstallers} also checks the
     * {@link ClientConnectionInstaller} twice over: to see if a <tt>PersistenceSessionProxy</tt> should be
     * used as a persistor, and for any {@link FacetDecoratorInstaller}s.
     */
    public void setAuthenticationInstaller(final AuthenticationManagerInstaller authenticationManagerInstaller) {
        this.authenticationInstaller = authenticationManagerInstaller;
    }

    /**
     * Set the type of connection to used to access the server.
     * 
     * <p>
     * Note that the {@link NakedObjectSessionFactoryUsingInstallers} also checks the
     * {@link ClientConnectionInstaller} twice over: to see if a <tt>PersistenceSessionProxy</tt> should be
     * used as a persistor, and for any {@link FacetDecoratorInstaller}s.
     */
    public void setAuthorizationInstaller(final AuthorizationManagerInstaller authorizationManagerInstaller) {
        this.authorizationInstaller = authorizationManagerInstaller;
    }

    protected AuthenticationManager obtainAuthenticationManager(DeploymentType deploymentType) {
        return authenticationInstaller.createAuthenticationManager();
    }
    
    protected AuthorizationManager obtainAuthorizationManager(DeploymentType deploymentType) {
        return authorizationInstaller.createAuthorizationManager();
    }
    

    // ///////////////////////////////////////////
    // Fixtures
    // ///////////////////////////////////////////

    public void lookupAndSetFixturesInstaller() {
        NakedObjectConfiguration configuration = installerLookup.getConfiguration();
        String fixture = configuration.getString(SystemConstants.FIXTURES_INSTALLER_KEY);

        final FixturesInstaller fixturesInstaller = installerLookup.fixturesInstaller(fixture);
        if (fixturesInstaller != null) {
            this.fixtureInstaller = fixturesInstaller;
        }
    }

    public void setFixtureInstaller(FixturesInstaller fixtureInstaller) {
		this.fixtureInstaller = fixtureInstaller;
	}
    
    @Override
    protected FixturesInstaller obtainFixturesInstaller() throws NakedObjectSystemException {
        return fixtureInstaller;
    }

    
    // ///////////////////////////////////////////
    // Template Image Loader
    // ///////////////////////////////////////////

    /**
     * Uses the {@link TemplateImageLoader} configured in {@link InstallerLookup}, if available, else falls
     * back to that of the superclass.
     */
    @Override
    protected TemplateImageLoader obtainTemplateImageLoader() {
        TemplateImageLoaderInstaller templateImageLoaderInstaller = installerLookup.templateImageLoaderInstaller(null);
        if (templateImageLoaderInstaller != null) {
            return templateImageLoaderInstaller.createLoader();
        } else {
            return super.obtainTemplateImageLoader();
        }
    }

    // ///////////////////////////////////////////
    // Reflector
    // ///////////////////////////////////////////

    public void setReflectorInstaller(final NakedObjectReflectorInstaller reflectorInstaller) {
        this.reflectorInstaller = reflectorInstaller;
    }

    @Override
    protected NakedObjectReflector obtainReflector(DeploymentType deploymentType) throws NakedObjectSystemException {
        if (reflectorInstaller == null) {
            String fromCmdLine = getConfiguration().getString(SystemConstants.REFLECTOR_KEY);
            reflectorInstaller = installerLookup.reflectorInstaller(fromCmdLine);
        }
        ensureThatState(reflectorInstaller, is(not(nullValue())),
                "reflector installer has not been injected and could not be looked up");

        // add in transaction support (if already in set then will be ignored)
        reflectorInstaller.addFacetDecoratorInstaller(installerLookup
                .getInstaller(TransactionFacetDecoratorInstaller.class));

        // if there is a client connection installer, then add facet decorator installer also
        String connection = getConfiguration().getString(SystemConstants.CLIENT_CONNECTION_KEY);
        if (connection != null) {
            FacetDecoratorInstaller clientConnectionInstaller = installerLookup.clientConnectionInstaller(connection);
            reflectorInstaller.addFacetDecoratorInstaller(clientConnectionInstaller);
        }

        return reflectorInstaller.createReflector();
    }

    
    // ///////////////////////////////////////////
    // Services
    // ///////////////////////////////////////////


    public void setServicesInstaller(ServicesInstaller servicesInstaller) {
		this.servicesInstaller = servicesInstaller;
	}
    
	@Override
	protected List<Object> obtainServices() {
        if (servicesInstaller == null) {
            servicesInstaller = installerLookup.servicesInstaller(null);
        }
        ensureThatState(servicesInstaller, is(not(nullValue())),
                "services installer has not been injected and could not be looked up");

        return servicesInstaller.getServices(getDeploymentType());
	}

	
    // ///////////////////////////////////////////
    // User Profile Loader/Store
    // ///////////////////////////////////////////
    

    public void lookupAndSetUserProfileFactoryInstaller() {
        NakedObjectConfiguration configuration = installerLookup.getConfiguration();
        String persistor = configuration.getString(SystemConstants.PROFILE_PERSISTOR_INSTALLER_KEY);

           UserProfileStoreInstaller userProfilePersistenceMechanismInstaller = installerLookup.userProfilePersistenceMechanismInstaller(persistor, getDeploymentType());
            if (userProfilePersistenceMechanismInstaller != null) {
                setUserProfileStoreInstaller(userProfilePersistenceMechanismInstaller);
            }
        }
    
    public void setUserProfileStoreInstaller(UserProfileStoreInstaller userProfilestoreInstaller) {
        this.userProfileStoreInstaller = userProfilestoreInstaller;
    }
    
	protected UserProfileStore obtainUserProfileStore() {
		return userProfileStoreInstaller.createUserProfileStore(getConfiguration());
	}
    

    // ///////////////////////////////////////////
    // PersistenceSessionFactory
    // ///////////////////////////////////////////

    public void setPersistenceMechanismInstaller(final PersistenceMechanismInstaller persistenceMechanismInstaller) {
        this.persistenceMechanismInstaller = persistenceMechanismInstaller;
    }

    @Override
    protected PersistenceSessionFactory obtainPersistenceSessionFactory(DeploymentType deploymentType)
            throws NakedObjectSystemException {

        // attempt to look up connection (that is, a ProxyPersistor)
        String connection = getConfiguration().getString(SystemConstants.CLIENT_CONNECTION_KEY);
        if (connection != null) {
            persistenceMechanismInstaller = installerLookup.clientConnectionInstaller(connection);
        }
        
        // if nothing, look for a object store persistor
        if (persistenceMechanismInstaller == null) {
            String persistenceMechanism = getConfiguration().getString(SystemConstants.OBJECT_PERSISTOR_INSTALLER_KEY);
            persistenceMechanismInstaller = installerLookup.persistenceMechanismInstaller(persistenceMechanism, deploymentType);
        }
        
        ensureThatState(persistenceMechanismInstaller, is(not(nullValue())),
                "persistor installer has not been injected and could not be looked up");

        return persistenceMechanismInstaller.createPersistenceSessionFactory(deploymentType);
    }


}
// Copyright (c) Naked Objects Group Ltd.
