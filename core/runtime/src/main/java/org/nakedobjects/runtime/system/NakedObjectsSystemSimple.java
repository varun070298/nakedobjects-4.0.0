package org.nakedobjects.runtime.system;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;

import java.util.List;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.specloader.NakedObjectReflector;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.fixturesinstaller.FixturesInstaller;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.imageloader.awt.TemplateImageLoaderAwt;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;
import org.nakedobjects.runtime.userprofile.UserProfileStore;


/**
 * A simple implementation of {@link NakedObjectsSystem}, intended
 * for use by Spring (dependency injection) or for testing.
 * 
 * <p>
 * Constructor injection is used for non-defaulted, mandatory components.  
 * Setter-based injection can be used for components that will otherwise by 
 * defaulted or are optional.
 */
public abstract class NakedObjectsSystemSimple extends NakedObjectsSystemAbstract {
    
    private final NakedObjectConfiguration configuration;
    
    private TemplateImageLoader templateImageLoader;
    private NakedObjectReflector reflector;
    private FixturesInstaller fixturesInstaller;

    private AuthenticationManager authenticationManager;

    private PersistenceSessionFactory persistenceSessionFactory;
    private UserProfileStore userProfileStore;

	private List<Object> serviceList;

    /////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////
    

    /**
     * Dependency injection of {@link NakedObjectConfiguration}.
     * 
     * All other components are either optional or defaulted.
     */
    public NakedObjectsSystemSimple(
            final DeploymentType deploymentType, 
            final NakedObjectConfiguration configuration) {
        super(deploymentType);
        ensureThatArg(configuration, is(not(nullValue())), "configuration may not be null");
        this.configuration = configuration;
    }


    /////////////////////////////////////////////
    // doCreateExecutionContextFactory
    /////////////////////////////////////////////

    @Override
    protected abstract NakedObjectSessionFactory doCreateSessionFactory(DeploymentType deploymentType) throws NakedObjectSystemException;


    /////////////////////////////////////////////
    // Configuration
    /////////////////////////////////////////////

    /**
     * As specified in the constructor.
     */
    @Override
    public NakedObjectConfiguration getConfiguration() {
        return configuration;
    }

    
    /////////////////////////////////////////////
    // Authentication Manager
    /////////////////////////////////////////////

    /**
     * The {@link AuthenticationManager}, if any.
     */
    @Override
    protected AuthenticationManager obtainAuthenticationManager(DeploymentType deploymentType) {
        return authenticationManager;
    }

    
    /**
     * Optionally specify the {@link AuthenticationManager}.
     * 
     * <p>
     * It will otherwise be <tt>null</tt>.
     */
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    
    /////////////////////////////////////////////
    // FixturesInstaller
    /////////////////////////////////////////////

    /**
     * The {@link FixturesInstaller}, if any.
     */
    @Override
    protected FixturesInstaller obtainFixturesInstaller() {
        return fixturesInstaller;
    }

    /**
     * Optionally specify the {@link FixturesInstaller}.
     * 
     * <p>
     * It will otherwise be <tt>null</tt>.
     */
    public void setFixturesInstaller(final FixturesInstaller installer) {
        this.fixturesInstaller = installer;
    }
    


    /////////////////////////////////////////////
    // TemplateImageLoader
    /////////////////////////////////////////////

    /**
     * The {@link TemplateImageLoader}, if any.
     */
    protected TemplateImageLoader obtainTemplateImageLoader() {
        return templateImageLoader != null? templateImageLoader: new TemplateImageLoaderAwt(getConfiguration());
    }

    /**
     * Optionally specify the {@link TemplateImageLoader}.
     * 
     * <p>
     * It will otherwise be {@link NakedObjectsSystemDefault#obtainTemplateImageLoader(DeploymentType) defaulted}.
     */
    public void setTemplateImageLoader(final TemplateImageLoader templateImageLoader) {
        ensureThatArg(templateImageLoader, is(notNullValue()), "template Image Loader may not be set to null");
        this.templateImageLoader = templateImageLoader; 
    }


    public TemplateImageLoader getTemplateImageLoader() {
        return templateImageLoader;
    }

    
    /////////////////////////////////////////////
    // Reflector
    /////////////////////////////////////////////

    /**
     * The injected {@link NakedObjectReflector}.
     * 
     * @see #setReflector(NakedObjectReflector)
     */
    protected NakedObjectReflector obtainReflector(DeploymentType deploymentType) throws NakedObjectSystemException {
        return reflector;
    }
    
    public void setReflector(final NakedObjectReflector reflector) {
        ensureThatArg(reflector, is(notNullValue()), "reflector may not be set to null");
        this.reflector = reflector;
    }

    public NakedObjectReflector getReflector() {
        return reflector;
    }
    

    /////////////////////////////////////////////
    // PersistenceSessionFactory
    /////////////////////////////////////////////

    /**
     * The injected {@link PersistenceSessionFactory}.
     * 
     * @see #setPersistenceSessionFactory(PersistenceSessionFactory)
     */
    @Override
    protected PersistenceSessionFactory obtainPersistenceSessionFactory(DeploymentType deploymentType) throws NakedObjectSystemException {
        return persistenceSessionFactory;
    }


    public void setPersistenceSessionFactory(PersistenceSessionFactory persistenceSessionFactory) {
        this.persistenceSessionFactory = persistenceSessionFactory;
    }


    
    ///////////////////////////////////////////////
    // UserProfileStore
    ///////////////////////////////////////////////
    
    public void setUserProfileStore(UserProfileStore userProfileStore) {
		this.userProfileStore = userProfileStore;
	}
    
    
	@Override
	protected UserProfileStore obtainUserProfileStore() {
		return userProfileStore;
	}

	
    ///////////////////////////////////////////////
    // Services
    ///////////////////////////////////////////////

	
	public void setServiceList(List<Object> serviceList) {
		this.serviceList = serviceList;
	}
	
	@Override
	protected List<Object> obtainServices() {
		return serviceList;
	}



}
// Copyright (c) Naked Objects Group Ltd.
