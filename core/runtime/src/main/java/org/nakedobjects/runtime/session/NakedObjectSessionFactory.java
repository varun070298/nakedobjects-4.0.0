package org.nakedobjects.runtime.session;

import java.util.List;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;


/**
 * Analogous to a Hibernate <tt>SessionFactory</tt>.
 * 
 * @see NakedObjectSession
 */
public interface NakedObjectSessionFactory extends ApplicationScopedComponent {
    

    /**
     * Creates and {@link NakedObjectSession#open() open}s the {@link NakedObjectSession}.
     */
    NakedObjectSession openSession(final AuthenticationSession session);


    /**
     * The {@link ApplicationScopedComponent application-scoped} {@link DeploymentType}. 
     */
    public DeploymentType getDeploymentType();

    /**
     * The {@link ApplicationScopedComponent application-scoped} {@link NakedObjectConfiguration}.
     */
    public NakedObjectConfiguration getConfiguration();

    /**
     * The {@link ApplicationScopedComponent application-scoped} {@link SpecificationLoader}. 
     */
    public SpecificationLoader getSpecificationLoader();

    /**
     * The {@link ApplicationScopedComponent application-scoped} {@link TemplateImageLoader}.
     */
    public TemplateImageLoader getTemplateImageLoader();

    /**
     * The {@link AuthenticationManager} that will be used to authenticate and create
     * {@link AuthenticationSession}s {@link NakedObjectSession#getAuthenticationSession() within}
     * the {@link NakedObjectSession}.
     */
    public AuthenticationManager getAuthenticationManager();

    /**
     * The {@link AuthorizationManager} that will be used to authorize access to domain objects.
     */
	public AuthorizationManager getAuthorizationManager();

    /**
     * The {@link PersistenceSessionFactory} that will be used to create {@link PersistenceSession}
     * {@link NakedObjectSession#getPersistenceSession() within} the {@link NakedObjectSession}.
     */
    public PersistenceSessionFactory getPersistenceSessionFactory();

    public UserProfileLoader getUserProfileLoader();


	List<Object> getServices();



}


// Copyright (c) Naked Objects Group Ltd.
