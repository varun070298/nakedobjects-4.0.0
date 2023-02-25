package org.nakedobjects.runtime.system.installers;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.factory.InstanceFactory;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.specloader.FacetDecoratorInstaller;
import org.nakedobjects.metamodel.specloader.JavaReflector;
import org.nakedobjects.metamodel.specloader.NakedObjectReflectorInstaller;
import org.nakedobjects.metamodel.specloader.ReflectorConstants;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistryDefault;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacets;
import org.nakedobjects.metamodel.specloader.traverser.SpecificationTraverser;
import org.nakedobjects.metamodel.specloader.validator.MetaModelValidator;
import org.nakedobjects.runtime.installers.InstallerAbstract;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.installers.InstallerLookupAware;


public class JavaReflectorInstaller extends InstallerAbstract implements NakedObjectReflectorInstaller, InstallerLookupAware {

    private static final Logger LOG = Logger.getLogger(JavaReflectorInstaller.class);

    public static final String PROPERTY_BASE = ConfigurationConstants.ROOT;

    /**
     * Defaulted in the constructor.
     */
    private final LinkedHashSet<FacetDecoratorInstaller> decoratorInstallers;

    private InstallerLookup installerLookup;

    // /////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////

    public JavaReflectorInstaller() {
    	this("java");
    }

    public JavaReflectorInstaller(String name) {
    	super(NakedObjectReflectorInstaller.TYPE, name);
    	decoratorInstallers = new LinkedHashSet<FacetDecoratorInstaller>();
    }

    
    // /////////////////////////////////////////////////////
    // createReflector, doCreateReflector
    // /////////////////////////////////////////////////////

    /**
     * Should call {@link #addFacetDecoratorInstaller(ReflectorDecoratorInstaller)} prior to calling this.
     */
    public JavaReflector createReflector() {
        final ClassSubstitutor classSubstitutor = createClassSubstitutor(getConfiguration());
        final CollectionTypeRegistry collectionTypeRegistry = createCollectionTypeRegistry(getConfiguration());
        final SpecificationTraverser specificationTraverser = createSpecificationTraverser(getConfiguration());
        final ProgrammingModelFacets programmingModelFacets = createProgrammingModelFacets(getConfiguration());
        final Set<FacetDecorator> facetDecorators = createFacetDecorators(getConfiguration());
        final MetaModelValidator metaModelValidator = createMetaModelValidator(getConfiguration());

		final JavaReflector reflector = doCreateReflector(getConfiguration(), classSubstitutor, collectionTypeRegistry,
                specificationTraverser, programmingModelFacets, facetDecorators, metaModelValidator);

        return reflector;
    }

    /**
     * Hook method to allow subclasses to specify a different implementation of {@link ClassSubstitutor}.
     * 
     * <p>
     * By default, looks up implementation from provided {@link NakedObjectConfiguration} using
     * {@link ReflectorConstants#CLASS_SUBSTITUTOR_CLASS_NAME}. If not specified, then defaults to
     * {@value ReflectorConstants#CLASS_SUBSTITUTOR_CLASS_NAME_DEFAULT}.
     */
    protected ClassSubstitutor createClassSubstitutor(NakedObjectConfiguration configuration) {
        final String configuredClassName = configuration.getString(ReflectorConstants.CLASS_SUBSTITUTOR_CLASS_NAME,
                ReflectorConstants.CLASS_SUBSTITUTOR_CLASS_NAME_DEFAULT);
        return InstanceFactory.createInstance(configuredClassName, ClassSubstitutor.class);
    }

    /**
     * Hook method to allow subclasses to specify a different implementation of {@link SpecificationTraverser}.
     * 
     * <p>
     * By default, looks up implementation from provided {@link NakedObjectConfiguration} using
     * {@link ReflectorConstants#SPECIFICATION_TRAVERSER_CLASS_NAME}. If not specified, then defaults to
     * {@value ReflectorConstants#SPECIFICATION_TRAVERSER_CLASS_NAME_DEFAULT}.
     */
    protected SpecificationTraverser createSpecificationTraverser(NakedObjectConfiguration configuration) {
        final String specificationTraverserClassName = configuration.getString(ReflectorConstants.SPECIFICATION_TRAVERSER_CLASS_NAME,
                ReflectorConstants.SPECIFICATION_TRAVERSER_CLASS_NAME_DEFAULT);
        SpecificationTraverser specificationTraverser = InstanceFactory.createInstance(specificationTraverserClassName,
                SpecificationTraverser.class);
        return specificationTraverser;
    }


    /**
     * Hook method to allow subclasses to specify a different implementations (that is, sets of
     * {@link ProgrammingModelFacets}.
     * 
     * <p>
     * By default, looks up implementation from provided {@link NakedObjectConfiguration} using
     * {@link ReflectorConstants#PROGRAMMING_MODEL_FACETS_CLASS_NAME}. If not specified, then defaults to
     * {@value ReflectorConstants#PROGRAMMING_MODEL_FACETS_CLASS_NAME_DEFAULT}.
     * 
     * <p>
     * The list of facets can be adjusted using
     * {@link ReflectorConstants#FACET_FACTORY_INCLUDE_CLASS_NAME_LIST} to specify additional
     * {@link FacetFactory factories} to include, and
     * {@link ReflectorConstants#FACET_FACTORY_EXCLUDE_CLASS_NAME_LIST} to exclude.
     */
    protected ProgrammingModelFacets createProgrammingModelFacets(final NakedObjectConfiguration configuration) {
        ProgrammingModelFacets programmingModelFacets = lookupAndCreateProgrammingModelFacets(configuration);

        includeFacetFactories(configuration, programmingModelFacets);

        excludeFacetFactories(configuration, programmingModelFacets);

        return programmingModelFacets;
    }

    private ProgrammingModelFacets lookupAndCreateProgrammingModelFacets(final NakedObjectConfiguration configuration) {
        final String progModelFacetsClassName = configuration.getString(ReflectorConstants.PROGRAMMING_MODEL_FACETS_CLASS_NAME,
                ReflectorConstants.PROGRAMMING_MODEL_FACETS_CLASS_NAME_DEFAULT);
        ProgrammingModelFacets programmingModelFacets = InstanceFactory.createInstance(progModelFacetsClassName,
                ProgrammingModelFacets.class);
        return programmingModelFacets;
    }

    /**
     * Factored out of {@link #createProgrammingModelFacets(NakedObjectConfiguration)} so that subclasses that
     * choose to override can still support customization of their {@link ProgrammingModelFacets} in a similar
     * way.
     */
    protected void includeFacetFactories(
            final NakedObjectConfiguration configuration,
            ProgrammingModelFacets programmingModelFacets) {
        final String[] facetFactoriesIncludeClassNames = configuration
                .getList(ReflectorConstants.FACET_FACTORY_INCLUDE_CLASS_NAME_LIST);
        if (facetFactoriesIncludeClassNames != null) {
            for (String facetFactoryClassName : facetFactoriesIncludeClassNames) {
                Class<? extends FacetFactory> facetFactory = InstanceFactory.loadClass(facetFactoryClassName, FacetFactory.class);
                programmingModelFacets.addFactory(facetFactory);
            }
        }
    }

    /**
     * Factored out of {@link #createProgrammingModelFacets(NakedObjectConfiguration)} so that subclasses that
     * choose to override can still support customization of their {@link ProgrammingModelFacets} in a similar
     * way.
     */
    protected void excludeFacetFactories(
            final NakedObjectConfiguration configuration,
            ProgrammingModelFacets programmingModelFacets) {
        final String[] facetFactoriesExcludeClassNames = configuration
                .getList(ReflectorConstants.FACET_FACTORY_EXCLUDE_CLASS_NAME_LIST);
        for (String facetFactoryClassName : facetFactoriesExcludeClassNames) {
            Class<? extends FacetFactory> facetFactory = InstanceFactory.loadClass(facetFactoryClassName, FacetFactory.class);
            programmingModelFacets.removeFactory(facetFactory);
        }
    }

    /**
     * Hook method to allow subclasses to specify a different sets of {@link FacetDecorator}s.
     * 
     * <p>
     * By default, returns the {@link FacetDecorator}s that are specified in the
     * {@link NakedObjectConfiguration} (using {@link ReflectorConstants#FACET_DECORATOR_CLASS_NAMES}) along
     * with any {@link FacetDecorator}s explicitly registered using
     * {@link #addFacetDecoratorInstaller(FacetDecoratorInstaller)}. created using the
     * {@link FacetDecoratorInstaller}s.
     */
    protected Set<FacetDecorator> createFacetDecorators(NakedObjectConfiguration configuration) {
        addFacetDecoratorInstallers(configuration);
        return createFacetDecorators(decoratorInstallers);
    }

    private void addFacetDecoratorInstallers(final NakedObjectConfiguration configuration) {
        final String[] decoratorNames = configuration.getList(ReflectorConstants.FACET_DECORATOR_CLASS_NAMES);
        for (String decoratorName : decoratorNames) {
            if (LOG.isInfoEnabled()) {
                LOG.info("adding reflector facet decorator from configuration " + decoratorName);
            }
            addFacetDecoratorInstaller(lookupFacetDecorator(decoratorName));
        }
    }

    private FacetDecoratorInstaller lookupFacetDecorator(final String decoratorClassName) {
        return (FacetDecoratorInstaller) installerLookup.getInstaller(FacetDecoratorInstaller.class, decoratorClassName);
    }

    private Set<FacetDecorator> createFacetDecorators(final Set<FacetDecoratorInstaller> decoratorInstallers) {
        LinkedHashSet<FacetDecorator> decorators = new LinkedHashSet<FacetDecorator>();
        if (decoratorInstallers.size() == 0) {
            if (LOG.isInfoEnabled()) {
                LOG.info("No facet decorators installers added");
            }
        }
        for (final FacetDecoratorInstaller installer : decoratorInstallers) {
            decorators.addAll(installer.createDecorators());
        }
        return Collections.unmodifiableSet(decorators);
    }

    /**
     * Hook method to allow subclasses to specify a different implementation of {@link MetaModelValidator}.
     * 
     * <p>
     * By default, looks up implementation from provided {@link NakedObjectConfiguration} using
     * {@link ReflectorConstants#META_MODEL_VALIDATOR_CLASS_NAME}. If not specified, then defaults to
     * {@value ReflectorConstants#META_MODEL_VALIDATOR_CLASS_NAME_DEFAULT}.
     */
    protected MetaModelValidator createMetaModelValidator(NakedObjectConfiguration configuration) {
        final String metaModelValidatorClassName = configuration.getString(ReflectorConstants.META_MODEL_VALIDATOR_CLASS_NAME,
                ReflectorConstants.META_MODEL_VALIDATOR_CLASS_NAME_DEFAULT);
        MetaModelValidator metaModelValidator = InstanceFactory.createInstance(metaModelValidatorClassName,
                MetaModelValidator.class);
        return metaModelValidator;
    }


    /**
     * Creates the {@link CollectionTypeRegistry}, hardcoded to be the {@link CollectionTypeRegistryDefault}.
     * 
     * <p>
     * Note: the intention is to remove this interface and instead to use a mechanism similar to the
     * <tt>@Value</tt> annotation to specify which types represent collections. For now, have factored out
     * this method similar to be similar to the creation methods of other subcomponents such as the
     * {@link #createClassSubstitutor(NakedObjectConfiguration) ClassSubstitutor}. Note however that this
     * method is <tt>final</tt> so that it cannot be overridden.
     */
    protected final CollectionTypeRegistry createCollectionTypeRegistry(final NakedObjectConfiguration configuration) {
        return new CollectionTypeRegistryDefault();
    }

    /**
     * Hook method to allow for other implementations (still based on {@link JavaReflector}).
     */
    protected JavaReflector doCreateReflector(
            final NakedObjectConfiguration configuration,
            final ClassSubstitutor classSubstitutor,
            final CollectionTypeRegistry collectionTypeRegistry,
            final SpecificationTraverser specificationTraverser,
            final ProgrammingModelFacets programmingModelFacets, 
            final Set<FacetDecorator> facetDecorators, 
            final MetaModelValidator metaModelValidator) {
        return new JavaReflector(configuration, classSubstitutor, collectionTypeRegistry, specificationTraverser, programmingModelFacets, facetDecorators, metaModelValidator);
    }

    // /////////////////////////////////////////////////////
    // Optionally Injected: InstallerLookup
    // /////////////////////////////////////////////////////

    /**
     * Injected by virtue of being {@link InstallerLookupAware}.
     */
    public void setInstallerLookup(InstallerLookup installerLookup) {
        this.installerLookup = installerLookup;
    }

    // /////////////////////////////////////////////////////
    // Optionally Injected: DecoratorInstallers
    // /////////////////////////////////////////////////////

    /**
     * Adds in {@link FacetDecoratorInstaller}; if <tt>null</tt> or if already added then request will be
     * silently ignored.
     */
    public void addFacetDecoratorInstaller(final FacetDecoratorInstaller decoratorInstaller) {
        if (decoratorInstaller == null) {
            return;
        }
        decoratorInstallers.add(decoratorInstaller);
    }

}

// Copyright (c) Naked Objects Group Ltd.
