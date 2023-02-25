package org.nakedobjects.runtime.session;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;

import java.util.List;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.commons.lang.JavaClassUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.userprofile.UserProfile;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;


/**
 * Creates an implementation of {@link NakedObjectSessionFactory#openSession(AuthenticationSession)} to create
 * an {@link NakedObjectSession}, but delegates to subclasses to actually obtain the components that make up
 * that {@link NakedObjectSession}.
 * 
 * <p>
 * The idea is that one subclass can use the {@link InstallerLookup} design to lookup installers for
 * components (and hence create the components themselves), whereas another subclass might simply use Spring
 * (or another DI container) to inject in the components according to some Spring-configured application
 * context.
 */
public abstract class NakedObjectSessionFactoryAbstract implements NakedObjectSessionFactory {

    private final DeploymentType deploymentType;
    private final NakedObjectConfiguration configuration;
    private final TemplateImageLoader templateImageLoader;
    private final SpecificationLoader specificationLoader;
    private final AuthenticationManager authenticationManager;
    private final AuthorizationManager authorizationManager;
    private final PersistenceSessionFactory persistenceSessionFactory;
    private final UserProfileLoader userProfileLoader;
	private final List<Object> serviceList;

    public NakedObjectSessionFactoryAbstract(
            final DeploymentType deploymentType,
            final NakedObjectConfiguration configuration,
            final SpecificationLoader specificationLoader,
            final TemplateImageLoader templateImageLoader,
            final AuthenticationManager authenticationManager,
            AuthorizationManager authorizationManager, 
            final UserProfileLoader userProfileLoader, 
            final PersistenceSessionFactory persistenceSessionFactory, 
            final List<Object> serviceList) {

        ensureThatArg(deploymentType, is(not(nullValue())));
        ensureThatArg(configuration, is(not(nullValue())));
        ensureThatArg(specificationLoader, is(not(nullValue())));
        ensureThatArg(templateImageLoader, is(not(nullValue())));
        ensureThatArg(authenticationManager, is(not(nullValue())));
        ensureThatArg(authorizationManager, is(not(nullValue())));
        ensureThatArg(userProfileLoader, is(not(nullValue())));
        ensureThatArg(persistenceSessionFactory, is(not(nullValue())));
        ensureThatArg(serviceList, is(not(nullValue())));

        this.deploymentType = deploymentType;
        this.configuration = configuration;
        this.templateImageLoader = templateImageLoader;
        this.specificationLoader = specificationLoader;
        this.authenticationManager = authenticationManager;
        this.authorizationManager = authorizationManager;
        this.userProfileLoader = userProfileLoader;
        this.persistenceSessionFactory = persistenceSessionFactory;
        this.serviceList = serviceList;
    }

    // ///////////////////////////////////////////
    // init, shutdown
    // ///////////////////////////////////////////

    /**
     * Wires components as necessary, and then {@link ApplicationScopedComponent#init() init}ializes all.
     */
    public void init() {
        templateImageLoader.init();
        
        specificationLoader.setServiceClasses(JavaClassUtils.toClasses(serviceList));
        
        specificationLoader.init();

        // must come after init of spec loader.
        specificationLoader.injectInto(persistenceSessionFactory);
        persistenceSessionFactory.setServices(serviceList);
        userProfileLoader.setServices(serviceList);

        authenticationManager.init();
        authorizationManager.init();
        persistenceSessionFactory.init();
    }

    public void shutdown() {
        persistenceSessionFactory.shutdown();
        authenticationManager.shutdown();
        specificationLoader.shutdown();
        templateImageLoader.shutdown();
        userProfileLoader.shutdown();
    }

    public NakedObjectSession openSession(final AuthenticationSession authenticationSession) {
        PersistenceSession persistenceSession = persistenceSessionFactory.createPersistenceSession();
        ensureThatArg(persistenceSession, is(not(nullValue())));
        
        UserProfile userProfile = userProfileLoader.getProfile(authenticationSession);
        ensureThatArg(userProfile, is(not(nullValue())));
        
        // inject into persistenceSession any/all application-scoped components that it requires
        getSpecificationLoader().injectInto(persistenceSession);

        NakedObjectSessionDefault nakedObjectSessionDefault = new NakedObjectSessionDefault(this, authenticationSession, persistenceSession, userProfile);
        
        return nakedObjectSessionDefault;
    }

    public NakedObjectConfiguration getConfiguration() {
        return configuration;
    }

    public DeploymentType getDeploymentType() {
        return deploymentType;
    }

    public SpecificationLoader getSpecificationLoader() {
        return specificationLoader;
    }

    public TemplateImageLoader getTemplateImageLoader() {
        return templateImageLoader;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public AuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    public PersistenceSessionFactory getPersistenceSessionFactory() {
        return persistenceSessionFactory;
    }

    public UserProfileLoader getUserProfileLoader() {
        return userProfileLoader;
    }
    
    public List<Object> getServices() {
    	return serviceList;
    }
}

// Copyright (c) Naked Objects Group Ltd.
