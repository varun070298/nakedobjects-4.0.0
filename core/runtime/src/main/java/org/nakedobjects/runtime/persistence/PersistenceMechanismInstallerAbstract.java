package org.nakedobjects.runtime.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;

import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.metamodel.commons.factory.InstanceFactory;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.runtime.installers.InstallerAbstract;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.installers.InstallerLookupAware;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adapterfactory.pojo.PojoAdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerDefault;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerExtended;
import org.nakedobjects.runtime.persistence.internal.RuntimeContextFromSession;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactory;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactoryBasic;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;

/**
 * An abstract implementation of {@link PersistenceMechanismInstaller} that will
 * lookup the {@link AdapterFactory} and {@link ObjectFactory} from the supplied
 * {@link NakedObjectConfiguration}.
 * 
 * <p>
 * If none can be found, then will default to the {@link PojoAdapterFactory} and
 * {@link ObjectFactoryBasic} respectively.
 */
public abstract class PersistenceMechanismInstallerAbstract extends
		InstallerAbstract implements PersistenceMechanismInstaller,
		InstallerLookupAware {

	private static final Logger LOG = Logger
	.getLogger(PersistenceMechanismInstallerAbstract.class);

	private InstallerLookup installerLookup;

	
	public PersistenceMechanismInstallerAbstract(String name) {
		super(PersistenceMechanismInstaller.TYPE, name);
	}

	/**
	 * For subclasses that need to specify a different type.
	 */
	public PersistenceMechanismInstallerAbstract(String type, String name) {
		super(type, name);
	}

	
    /**
     * Creates a {@link PersistenceSession} that is initialized with the 
     * various hook methods.
     * 
     * @see #createPersistenceSession(PersistenceSessionFactory, AdapterManagerExtended, AdapterFactory, ObjectFactory, OidGenerator, ServicesInjector)
     * @see #createAdapterFactory(NakedObjectConfiguration)
     * @see #createAdapterManager(NakedObjectConfiguration)
     * @see #createContainer(NakedObjectConfiguration)
     * @see #createOidGenerator(NakedObjectConfiguration)
     * @see #createRuntimeContext(NakedObjectConfiguration)
     * @see #createServicesInjector(NakedObjectConfiguration)
     */
    public PersistenceSession createPersistenceSession(
            final PersistenceSessionFactory persistenceSessionFactory) {
        if (LOG.isInfoEnabled()) {
            LOG.info("installing " + this.getClass().getName());
        }

        final AdapterManagerExtended adapterManager = createAdapterManager(getConfiguration());
        final AdapterFactory adapterFactory = createAdapterFactory(getConfiguration());
        final ObjectFactory objectFactory = createObjectFactory(getConfiguration());
        final OidGenerator oidGenerator = createOidGenerator(getConfiguration());
        
        final RuntimeContext runtimeContext = createRuntimeContext(getConfiguration());
        final DomainObjectContainer container = createContainer(getConfiguration());
        
        final ServicesInjector servicesInjector = createServicesInjector(getConfiguration());
        final List<Object> serviceList = persistenceSessionFactory.getServices();

        ensureThatArg(adapterManager, is(not(nullValue())));
        ensureThatArg(adapterFactory, is(not(nullValue())));
        ensureThatArg(objectFactory, is(not(nullValue())));
        ensureThatArg(oidGenerator, is(not(nullValue())));

        ensureThatArg(runtimeContext, is(not(nullValue())));
        ensureThatArg(container, is(not(nullValue())));
        ensureThatArg(serviceList, is(not(nullValue())));
        ensureThatArg(servicesInjector, is(not(nullValue())));

        // wire up components
		runtimeContext.injectInto(container);
		runtimeContext.setContainer(container);

        servicesInjector.setContainer(container);
        servicesInjector.setServices(serviceList);
        persistenceSessionFactory.getSpecificationLoader().injectInto(runtimeContext);
        
        return createPersistenceSession(persistenceSessionFactory,
				adapterManager, adapterFactory, objectFactory, oidGenerator,
				servicesInjector);
    }


	// ///////////////////////////////////////////
	// Mandatory hook methods
	// ///////////////////////////////////////////

    /**
     * Mandatory hook method called by {@link #createPersistenceSession(PersistenceSessionFactory)},
     * passing the components created by the other (optional) hooks.
     * 
     * @see #createPersistenceSession(PersistenceSessionFactory)
     */
    protected abstract PersistenceSession createPersistenceSession(
			final PersistenceSessionFactory persistenceSessionFactory,
			final AdapterManagerExtended adapterManager,
			final AdapterFactory adapterFactory,
			final ObjectFactory objectFactory,
			final OidGenerator oidGenerator,
			final ServicesInjector servicesInjector);


	// ///////////////////////////////////////////
	// Optional hook methods
	// ///////////////////////////////////////////

	/**
	 * Hook method to allow subclasses to specify a different implementation of
	 * {@link AdapterFactory}.
	 * 
	 * <p>
	 * By default, looks up implementation from provided
	 * {@link NakedObjectConfiguration} using
	 * {@link PersistenceConstants#ADAPTER_FACTORY_CLASS_NAME}. If no
	 * implementation is specified, then defaults to
	 * {@value PersistenceConstants#ADAPTER_FACTORY_CLASS_NAME_DEFAULT}.
	 */
	protected AdapterFactory createAdapterFactory(
			final NakedObjectConfiguration configuration) {
		final String configuredClassName = configuration.getString(
				PersistenceConstants.ADAPTER_FACTORY_CLASS_NAME,
				PersistenceConstants.ADAPTER_FACTORY_CLASS_NAME_DEFAULT);
		return InstanceFactory.createInstance(configuredClassName,
				AdapterFactory.class);
	}

	/**
	 * Hook method to allow subclasses to specify a different implementation of
	 * {@link ObjectFactory}.
	 * 
	 * <p>
	 * By default, looks up implementation from provided
	 * {@link NakedObjectConfiguration} using
	 * {@link PersistenceConstants#OBJECT_FACTORY_CLASS_NAME}. If no
	 * implementation is specified, then defaults to
	 * {@value PersistenceConstants#OBJECT_FACTORY_CLASS_NAME_DEFAULT}.
	 */
	protected ObjectFactory createObjectFactory(
			final NakedObjectConfiguration configuration) {
		final String configuredClassName = configuration.getString(
				PersistenceConstants.OBJECT_FACTORY_CLASS_NAME,
				PersistenceConstants.OBJECT_FACTORY_CLASS_NAME_DEFAULT);
		return InstanceFactory.createInstance(configuredClassName,
				PersistenceConstants.OBJECT_FACTORY_CLASS_NAME_DEFAULT,
				ObjectFactory.class);
	}

	/**
	 * Hook method to allow subclasses to specify a different implementation of
	 * {@link ServicesInjector}
	 * 
	 * <p>
	 * By default, looks up implementation from provided
	 * {@link NakedObjectConfiguration} using
	 * {@link PersistenceConstants#SERVICES_INJECTOR_CLASS_NAME}. If no
	 * implementation is specified, then defaults to
	 * {@value PersistenceConstants#SERVICES_INJECTOR_CLASS_NAME_DEFAULT}.
	 */
	protected ServicesInjector createServicesInjector(	NakedObjectConfiguration configuration) {
		final String configuredClassName = configuration.getString(
				PersistenceConstants.SERVICES_INJECTOR_CLASS_NAME,
				PersistenceConstants.SERVICES_INJECTOR_CLASS_NAME_DEFAULT);
		return InstanceFactory.createInstance(configuredClassName,
				ServicesInjector.class);
	}

	/**
	 * Hook method to allow subclasses to specify a different implementation of
	 * {@link OidGenerator}
	 * 
	 * <p>
	 * By default, looks up implementation from provided
	 * {@link NakedObjectConfiguration} using
	 * {@link PersistenceConstants#OID_GENERATOR_CLASS_NAME}. If no
	 * implementation is specified, then defaults to
	 * {@value PersistenceConstants#OID_GENERATOR_CLASS_NAME_DEFAULT}.
	 */
	protected OidGenerator createOidGenerator(
			final NakedObjectConfiguration configuration) {
		final String oidGeneratorClassName = configuration.getString(
				PersistenceConstants.OID_GENERATOR_CLASS_NAME,
				PersistenceConstants.OID_GENERATOR_CLASS_NAME_DEFAULT);
		return InstanceFactory.createInstance(oidGeneratorClassName,
				OidGenerator.class);
	}

	

	/**
	 * Hook method to return {@link AdapterManagerExtended}.
	 * 
	 * <p>
	 * By default returns an {@link AdapterManagerDefault}.
	 */
	protected AdapterManagerExtended createAdapterManager(
			final NakedObjectConfiguration configuration) {
		return new AdapterManagerDefault();
	}

	/**
	 * Hook method to return a {@link RuntimeContext}.
	 * 
	 * <p>
	 * By default, returns a {@link RuntimeContextFromSession}.
	 */
	protected RuntimeContext createRuntimeContext(
			final NakedObjectConfiguration configuration) {
		return new RuntimeContextFromSession();
	}

	/**
	 * Hook method to return a {@link DomainObjectContainer}.
	 * 
	 * <p>
	 * By default, looks up implementation from provided
	 * {@link NakedObjectConfiguration} using
	 * {@link PersistenceConstants#DOMAIN_OBJECT_CONTAINER_CLASS_NAME}. If no
	 * implementation is specified, then defaults to
	 * {@value PersistenceConstants#DOMAIN_OBJECT_CONTAINER_NAME_DEFAULT}.
	 */
	protected DomainObjectContainer createContainer(
			final NakedObjectConfiguration configuration) {
		final String configuredClassName = configuration.getString(
				PersistenceConstants.DOMAIN_OBJECT_CONTAINER_CLASS_NAME,
				PersistenceConstants.DOMAIN_OBJECT_CONTAINER_NAME_DEFAULT);
		return InstanceFactory.createInstance(configuredClassName,
				PersistenceConstants.DOMAIN_OBJECT_CONTAINER_NAME_DEFAULT,
				DomainObjectContainer.class);
	}

	// /////////////////////////////////////////////////////
	// Dependencies (from setters)
	// /////////////////////////////////////////////////////

	/**
	 * By virtue of being {@link InstallerLookupAware}.
	 */
	public void setInstallerLookup(final InstallerLookup installerLookup) {
		this.installerLookup = installerLookup;
	}

	/**
	 * @see #setInstallerLookup(InstallerLookup)
	 */
	protected InstallerLookup getInstallerLookup() {
		return installerLookup;
	}

}

// Copyright (c) Naked Objects Group Ltd.
