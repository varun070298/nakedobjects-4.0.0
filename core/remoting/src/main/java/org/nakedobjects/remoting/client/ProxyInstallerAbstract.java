package org.nakedobjects.remoting.client;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.remoting.client.authentication.AuthenticationManagerProxy;
import org.nakedobjects.remoting.client.authorization.AuthorizationManagerProxy;
import org.nakedobjects.remoting.client.facetdecorator.ProxyFacetDecorator;
import org.nakedobjects.remoting.client.persistence.ClientSideTransactionManager;
import org.nakedobjects.remoting.client.persistence.PersistenceSessionProxy;
import org.nakedobjects.remoting.client.persistence.ProxyPersistenceSessionFactory;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.facade.proxy.ServerFacadeProxy;
import org.nakedobjects.remoting.protocol.ClientMarshaller;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoderDefault;
import org.nakedobjects.remoting.transport.Transport;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.persistence.PersistenceMechanismInstallerAbstract;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.PersistenceSessionTransactionManagement;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerExtended;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerProxy;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactory;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.remoting.ClientConnectionInstaller;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;

public abstract class ProxyInstallerAbstract extends PersistenceMechanismInstallerAbstract implements ClientConnectionInstaller {
    
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ProxyInstallerAbstract.class);
    
    private ObjectEncoderDecoder encoderDecoder;
    private ServerFacade serverFacade;
    
    public ProxyInstallerAbstract(String name) {
    	super(ClientConnectionInstaller.TYPE, name);
    }
    
    ///////////////////////////////////////////////////////////////////
    // Encoder/Decoder
    ///////////////////////////////////////////////////////////////////

	/**
	 * Lazily creates (so that {@link #getConfiguration()} is available).
	 */
	protected ObjectEncoderDecoder getEncoderDecoder() {
		if (encoderDecoder == null) {
			encoderDecoder = ObjectEncoderDecoderDefault.create(getConfiguration()); 
		}
        return encoderDecoder;
    }


    ///////////////////////////////////////////////////////////////////
    // ServerFacade
    ///////////////////////////////////////////////////////////////////

	/**
	 * Lazily creates (so that {@link #getConfiguration()} is available).
	 */
    private ServerFacade getServerFacade() {
        if (serverFacade == null) {
            serverFacade = createServerFacade();
            serverFacade.init();
        }
        return serverFacade;
    }

    /**
     * Creates the {@link #getServerFacade()} as required.
     * 
     * <p>
     * Overridable, but default implementation calls the {@link #createTransport()}
     * and {@link #createMarshaller(Transport)} hooks.
     */
    protected ServerFacade createServerFacade() {
        Transport transport = createTransport();
		ClientMarshaller marshaller = createMarshaller(transport);
		ClientConnection connection = new ClientConnectionDefault(marshaller);
        return new ServerFacadeProxy(connection);
    }

    /**
     * Mandatory hook method.
     */
    protected abstract Transport createTransport();
    
    /**
     * Mandatory hook method.
     */
	protected abstract ClientMarshaller createMarshaller(Transport transport);



    ///////////////////////////////////////////////////////////////////
    // Authentication Manager 
    ///////////////////////////////////////////////////////////////////

	public AuthenticationManager createAuthenticationManager() {
        return new AuthenticationManagerProxy(getConfiguration(), getServerFacade(), getEncoderDecoder());
    }


    ///////////////////////////////////////////////////////////////////
    // Authorization Manager 
    ///////////////////////////////////////////////////////////////////

	public AuthorizationManager createAuthorizationManager() {
		return new AuthorizationManagerProxy(getConfiguration(), getServerFacade(), getEncoderDecoder());
	}


    ///////////////////////////////////////////////////////////////////
    // Create PersistenceSession
    ///////////////////////////////////////////////////////////////////

    public PersistenceSessionFactory createPersistenceSessionFactory(final DeploymentType deploymentType) {
        return new ProxyPersistenceSessionFactory(deploymentType, this);
    }

	protected PersistenceSession createPersistenceSession(
			final PersistenceSessionFactory persistenceSessionFactory,
			final AdapterManagerExtended adapterManager,
			final AdapterFactory adapterFactory,
			final ObjectFactory objectFactory,
			final OidGenerator oidGenerator,
			final ServicesInjector servicesInjector) {

        final PersistenceSessionProxy persistenceSession = 
            new PersistenceSessionProxy(
                    persistenceSessionFactory, 
                    adapterFactory, objectFactory, servicesInjector, oidGenerator, adapterManager, 
                    getServerFacade(), getEncoderDecoder());

        NakedObjectTransactionManager transactionManager = 
            createTransactionManager(getConfiguration(), persistenceSession.getAdapterManager(), persistenceSession);
        
        ensureThatArg(persistenceSession, is(not(nullValue())));
        ensureThatArg(transactionManager, is(not(nullValue())));
        
        transactionManager.injectInto(persistenceSession);
        
        // ... and finally return
        return persistenceSession;
	}

    /**
     * Creates the {@link NakedObjectTransactionManager}, potentially
     * overriddable.
     * 
     * <p>
     * Called from {@link #createPersistenceSession(PersistenceSessionFactory)}.
     */
    protected NakedObjectTransactionManager createTransactionManager(
            final NakedObjectConfiguration configuration,
            final AdapterManagerProxy adapterManager, 
            final PersistenceSessionTransactionManagement transactionManagement) {
        return new ClientSideTransactionManager(adapterManager, transactionManagement, getServerFacade(), getEncoderDecoder());
    }

    ///////////////////////////////////////////////////////////////////
    // Decorator 
    ///////////////////////////////////////////////////////////////////

	public List<FacetDecorator> createDecorators() {
        return Arrays.<FacetDecorator>asList(
        		new ProxyFacetDecorator(
        				getConfiguration(), getServerFacade(), getEncoderDecoder()));
    }



}
// Copyright (c) Naked Objects Group Ltd.
