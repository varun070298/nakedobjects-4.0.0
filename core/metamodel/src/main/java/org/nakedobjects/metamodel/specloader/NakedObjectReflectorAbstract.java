package org.nakedobjects.metamodel.specloader;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.NakedObjectList;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.lang.JavaClassUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.metamodel.facetdecorator.FacetDecoratorSet;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAware;
import org.nakedobjects.metamodel.runtimecontext.noruntime.RuntimeContextNoRuntime;
import org.nakedobjects.metamodel.spec.IntrospectableSpecification;
import org.nakedobjects.metamodel.spec.JavaSpecification;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.SpecificationFacets;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistryDefault;
import org.nakedobjects.metamodel.specloader.internal.cache.SimpleSpecificationCache;
import org.nakedobjects.metamodel.specloader.internal.cache.SpecificationCache;
import org.nakedobjects.metamodel.specloader.internal.facetprocessor.FacetProcessor;
import org.nakedobjects.metamodel.specloader.internal.instances.InstanceCollectionSpecification;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacets;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacetsJava5;
import org.nakedobjects.metamodel.specloader.traverser.SpecificationTraverser;
import org.nakedobjects.metamodel.specloader.validator.MetaModelValidator;

/**
 * Builds the meta-model for Java 5 programming model.
 * 
 * <p>
 * The implementation provides for a degree of pluggability:
 * <ul>
 * <li>The most important plug-in point is {@link ProgrammingModelFacets} that
 * specifies the set of {@link Facet} that make up programming model. If not
 * specified then defaults to {@link ProgrammingModelFacetsJava5} (which should
 * be used as a starting point for your own customizations).
 * <li>The only mandatory plug-in point is {@link ClassSubstitutor}, which
 * allows the class to be loaded to be substituted if required. This is used in
 * conjunction with some <tt>PersistenceMechanism</tt>s that do class
 * enhancement.
 * <li>The {@link CollectionTypeRegistry} specifies the types that should be
 * considered as collections. If not specified then will
 * {@link CollectionTypeRegistryDefault default}. (Note: this extension point
 * has not been tested, so should be considered more of a &quot;statement of
 * intent&quot; than actual API. Also, we may use annotations (similar to the
 * way in which Values are specified) as an alternative mechanism).
 * </ul>
 * 
 * <p>
 * In addition, the {@link RuntimeContext} can optionally be injected, but will
 * default to {@link RuntimeContextNoRuntime} if not provided prior to
 * {@link #init() initialization}. The purpose of {@link RuntimeContext} is to
 * allow the metamodel to be used standalone, for example in a Maven plugin. The
 * {@link RuntimeContextNoRuntime} implementation will through an exception for
 * any methods (such as finding an {@link NakedObject adapter}) because there is
 * no runtime session. In the case of the metamodel being used by the framework
 * (that is, when there <i>is</i> a runtime), then the framework injects an
 * implementation of {@link RuntimeContext} that acts like a bridge to its
 * <tt>NakedObjectsContext</tt>.
 */
public abstract class NakedObjectReflectorAbstract implements
		NakedObjectReflector, DebugInfo {

	private final static Logger LOG = Logger
			.getLogger(NakedObjectReflectorAbstract.class);

	/**
	 * Injected in the constructor.
	 */
	private final NakedObjectConfiguration configuration;
	/**
	 * Injected in the constructor.
	 */
	private final ClassSubstitutor classSubstitutor;
	/**
	 * Injected in the constructor.
	 */
	private final CollectionTypeRegistry collectionTypeRegistry;
	/**
	 * Injected in the constructor.
	 */
	private final ProgrammingModelFacets programmingModelFacets;

	/**
	 * Defaulted in the constructor.
	 */
	private final FacetProcessor facetProcessor;

	/**
	 * Defaulted in the constructor, so can be added to via
	 * {@link #setFacetDecorators(FacetDecoratorSet)} or
	 * {@link #addFacetDecorator(FacetDecorator)}.
	 * 
	 * <p>
	 * {@link FacetDecorator}s must be added prior to {@link #init()
	 * initialization.}
	 */
	private final FacetDecoratorSet facetDecoratorSet;

	/**
	 * Can optionally be injected, but will default (to
	 * {@link RuntimeContextNoRuntime}) otherwise.
	 * 
	 * <p>
	 * Should be injected when used by framework, but will default to a no-op
	 * implementation if the metamodel is being used standalone (eg for a
	 * code-generator).
	 */
	private RuntimeContext runtimeContext;

	private SpecificationTraverser specificationTraverser;

	/**
	 * Priming cache, optionally {@link #setServiceClasses(List) injected}.
	 */
    private List<Class<?>> serviceClasses = new ArrayList<Class<?>>();

    /**
     * Optionally {@link #setValidator(MetaModelValidator) injected}.
     */
    private MetaModelValidator metaModelValidator;

	/**
	 * Defaulted in the constructor.
	 */
	private final SpecificationCache cache;

    
	// /////////////////////////////////////////////////////////////
	// Constructor
	// /////////////////////////////////////////////////////////////

	public NakedObjectReflectorAbstract(
			final NakedObjectConfiguration configuration,
			final ClassSubstitutor classSubstitutor,
			final CollectionTypeRegistry collectionTypeRegistry,
			final SpecificationTraverser specificationTraverser, 
			final ProgrammingModelFacets programmingModelFacets, 
			final Set<FacetDecorator> facetDecorators, 
			final MetaModelValidator metaModelValidator) {
		ensureThatArg(configuration, is(notNullValue()));
		ensureThatArg(classSubstitutor, is(notNullValue()));
		ensureThatArg(collectionTypeRegistry, is(notNullValue()));
		ensureThatArg(programmingModelFacets, is(notNullValue()));
		ensureThatArg(specificationTraverser, is(notNullValue()));
		ensureThatArg(facetDecorators, is(notNullValue()));
		ensureThatArg(metaModelValidator, is(notNullValue()));

		this.configuration = configuration;
		this.classSubstitutor = classSubstitutor;
		this.collectionTypeRegistry = collectionTypeRegistry;
		this.programmingModelFacets = programmingModelFacets;
		this.specificationTraverser = specificationTraverser;
		
		this.facetDecoratorSet = new FacetDecoratorSet();
		for (final FacetDecorator facetDecorator : facetDecorators) {
			this.facetDecoratorSet.add(facetDecorator);
		}

		this.metaModelValidator = metaModelValidator;
		
		this.facetProcessor = new FacetProcessor(configuration, this,
				collectionTypeRegistry, programmingModelFacets);
		
		this.cache = new SimpleSpecificationCache();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		LOG.info("finalizing reflector factory " + this);
	}

	// /////////////////////////////////////////////////////////////
	// init, shutdown
	// /////////////////////////////////////////////////////////////

	/**
	 * Initializes and wires up, and primes the cache based on
	 * any service classes that may have been {@link #setServiceClasses(List) injected}.
	 */
	public void init() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("initialising " + this);
		}

		// default subcomponents
		if (runtimeContext == null) {
			runtimeContext = new RuntimeContextNoRuntime();
		}
		injectInto(runtimeContext);
		injectInto(specificationTraverser);
		injectInto(metaModelValidator);

		// wire subcomponents into each other
		runtimeContext.injectInto(facetProcessor);

		// initialize subcomponents
		facetDecoratorSet.init();
		classSubstitutor.init();
		collectionTypeRegistry.init();
		specificationTraverser.init();
		facetProcessor.init();
		programmingModelFacets.init();
		metaModelValidator.init();
		
		// prime cache and validate
		primeCache();
		metaModelValidator.validate();
	}

    /**
     * load the service specifications and then, using the {@link #getSpecificationTraverser() traverser},
     * keep loading all referenced specifications until we can find no more.
     */
	private void primeCache() {
		for (Class<?> serviceClass : serviceClasses) {
		    internalLoadSpecification(serviceClass);
		}
		loadAllSpecifications();
	}

    private void loadAllSpecifications() {
        List<Class<?>> newlyDiscoveredClasses = newlyDiscoveredClasses();
        
        while(newlyDiscoveredClasses.size() > 0) {
            for(Class<?> newClass: newlyDiscoveredClasses) {
                internalLoadSpecification(newClass);
            }
            newlyDiscoveredClasses = newlyDiscoveredClasses();
        }
    }

    private List<Class<?>> newlyDiscoveredClasses() {
        List<Class<?>> newlyDiscoveredClasses = new ArrayList<Class<?>>();
        
        NakedObjectSpecification[] noSpecs = allSpecifications();
        try {
        	for(NakedObjectSpecification noSpec: noSpecs) {
        		getSpecificationTraverser().traverseReferencedClasses(noSpec, newlyDiscoveredClasses);
        	}
        } catch (ClassNotFoundException ex) {
            throw new NakedObjectException(ex);
        }
        return newlyDiscoveredClasses;
    }



	public void shutdown() {
		LOG.info("shutting down " + this);
		getCache().clear();
		facetDecoratorSet.shutdown();
	}

	// /////////////////////////////////////////////////////////////
	// install, load, allSpecifications
	// /////////////////////////////////////////////////////////////

	/**
	 * API: Return the specification for the specified class of object.
	 */
	public final NakedObjectSpecification loadSpecification(
			final String className) {
		ensureThatArg(className, is(notNullValue()),
				"specification class name must be specified");

		try {
			final Class<?> cls = loadBuiltIn(className);
			return internalLoadSpecification(cls);
		} catch (final ClassNotFoundException e) {
			final NakedObjectSpecification spec = getCache().get(className);
			if (spec == null) {
				throw new NakedObjectException(
						"No such class available: " + className);
			}
			return spec;
		}
	}

	/**
	 * API: Return specification.
	 */
	public NakedObjectSpecification loadSpecification(final Class<?> type) {
		return internalLoadSpecification(type);
	}

	private NakedObjectSpecification internalLoadSpecification(final Class<?> type) {
		Class<?> substitutedType = getClassSubstitutor().getClass(type);
		NakedObjectSpecification nakedObjectSpecification = substitutedType != null ? loadSpecificationForSubstitutedClass(substitutedType)
				: null;
		return nakedObjectSpecification;
	}

	private NakedObjectSpecification loadSpecificationForSubstitutedClass(
			final Class<?> type) {
		Assert.assertNotNull(type);
		String typeName = type.getName();
		
		SpecificationCache specificationCache = getCache();
		synchronized (specificationCache) {
			final NakedObjectSpecification spec = specificationCache.get(typeName);
			if (spec != null) {
				return spec;
			}
			NakedObjectSpecification specification = createSpecification(type);
			if (specification == null) {
				throw new NakedObjectException(
						"Failed to create specification for class "
								+ typeName);
			}

			// put into the cache prior to introspecting, to prevent
			// infinite loops
			specificationCache.cache(typeName, specification);
			
			introspectSpecificationIfRequired(specification);

			return specification;
		}
	}


	/**
	 * Loads the specifications of the specified types except the one specified
	 * (to prevent an infinite loop).
	 */
	public boolean loadSpecifications(List<Class<?>> typesToLoad,
			final Class<?> typeToIgnore) {
		boolean anyLoadedAsNull = false;
		for (Class<?> typeToLoad: typesToLoad) {
			if (typeToLoad != typeToIgnore) {
				NakedObjectSpecification noSpec = internalLoadSpecification(typeToLoad);
			    boolean loadedAsNull = (noSpec == null);
			    anyLoadedAsNull = loadedAsNull || anyLoadedAsNull;
			}
		}
		return anyLoadedAsNull;
	}
	
	public boolean loadSpecifications(List<Class<?>> typesToLoad) {
		return loadSpecifications(typesToLoad, null);
	}


	/**
	 * Overridable method for language-specific subclass to create the
	 * appropriate type of {@link NakedObjectSpecification}.
	 */
	protected NakedObjectSpecification createSpecification(final Class<?> cls) {

		if (NakedObjectList.class.isAssignableFrom(cls)) {
			return new InstanceCollectionSpecification(this,
					getRuntimeContext());
		}

		return new JavaSpecification(cls, this, getRuntimeContext());
	}

	private Class<?> loadBuiltIn(final String className)
			throws ClassNotFoundException {
		Class<?> builtIn = JavaClassUtils.getBuiltIn(className);
		if (builtIn != null) {
			return builtIn;
		}
		return Class.forName(className);
	}

	/**
	 * Return all the loaded specifications.
	 */
	public NakedObjectSpecification[] allSpecifications() {
		return getCache().allSpecifications();
	}

	public boolean loaded(Class<?> cls) {
		return loaded(cls.getName());
	}

	public boolean loaded(String fullyQualifiedClassName) {
		return getCache().get(fullyQualifiedClassName) != null;
	}

	
	// added to try to track down a race condition
	// TODO: should probably remove 
	private NakedObjectSpecification introspectSpecificationIfRequired(
			NakedObjectSpecification spec) {
    	if (spec instanceof IntrospectableSpecification) {
    		IntrospectableSpecification introspectableSpec = (IntrospectableSpecification) spec;
    		if(!introspectableSpec.isIntrospected()) {
    			introspectableSpec.introspect(this.facetDecoratorSet);
    		}
    	}
    	return spec;    	
	}


	// ////////////////////////////////////////////////////////////////////
	// injectInto
	// ////////////////////////////////////////////////////////////////////

	/**
	 * Injects self into candidate if required, and instructs its subcomponents
	 * to do so also.
	 */
	public void injectInto(Object candidate) {
		Class<?> candidateClass = candidate.getClass();
		if (SpecificationLoaderAware.class.isAssignableFrom(candidateClass)) {
			SpecificationLoaderAware cast = SpecificationLoaderAware.class
					.cast(candidate);
			cast.setSpecificationLoader(this);
		}

		getClassSubstitutor().injectInto(candidate);
		getCollectionTypeRegistry().injectInto(candidate);
		
	}

	// /////////////////////////////////////////////////////////////
	// Debugging
	// /////////////////////////////////////////////////////////////

	public void debugData(final DebugString str) {
		facetDecoratorSet.debugData(str);
		str.appendln();

		str.appendTitle("Specifications");
		final NakedObjectSpecification[] specs = allSpecifications();
		Arrays.sort(specs, new Comparator<NakedObjectSpecification>() {
			public int compare(final NakedObjectSpecification s1,
					final NakedObjectSpecification s2) {
				return s1.getShortName().compareToIgnoreCase(s2.getShortName());
			}
		});
		for (int i = 0; i < specs.length; i++) {
			final NakedObjectSpecification specification = specs[i];
			str.append(specification.isAbstract() ? "A" : ".");
			str.append(specification.isService() ? "S" : ".");
			str.append(SpecificationFacets.isBoundedSet(specification) ? "B"
					: ".");
			str.append(specification.isCollection() ? "C" : ".");
			str.append(specification.isObject() ? "O" : ".");
			str.append("."); // placeholder for future support of maps
			str.append(specification.isParseable() ? "P" : ".");
			str.append(specification.isEncodeable() ? "E" : ".");
			str.append(specification.isValueOrIsAggregated() ? "A" : ".");
			str.append(!specification.isCollectionOrIsAggregated() ? "I" : ".");
			str.append("  ");
			str.append(specification.getShortName());
			str.append("  [fqc=");
			str.append(specification.getFullName());
			str.append(",type=");
			str.append(specification.getClass().getName());
			str.appendln("]");
		}
	}

	public String debugTitle() {
		return "Reflector";
	}

	// /////////////////////////////////////////////////////////////
	// Helpers (were previously injected, but no longer required)
	// /////////////////////////////////////////////////////////////

	/**
	 * Provides access to the registered {@link Facet}s.
	 */
	public FacetProcessor getFacetProcessor() {
		return facetProcessor;
	}

	private SpecificationCache getCache() {
		return cache;
	}


	// ////////////////////////////////////////////////////////////////////
	// Dependencies (injected by setter due to *Aware)
	// ////////////////////////////////////////////////////////////////////

	/**
	 * As per {@link #setRuntimeContext(RuntimeContext)}.
	 */
	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

	/**
	 * Due to {@link RuntimeContextAware}.
	 */
	public void setRuntimeContext(RuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
	}

	// ////////////////////////////////////////////////////////////////////
	// Dependencies (setters, optional)
	// ////////////////////////////////////////////////////////////////////

	public List<Class<?>> getServiceClasses() {
		return Collections.unmodifiableList(serviceClasses);
	}
	
	public void setServiceClasses(List<Class<?>> serviceClasses) {
		this.serviceClasses = serviceClasses;
	}

	

	// ////////////////////////////////////////////////////////////////////
	// Dependencies (injected from constructor)
	// ////////////////////////////////////////////////////////////////////

	public NakedObjectConfiguration getNakedObjectConfiguration() {
		return configuration;
	}

	public ClassSubstitutor getClassSubstitutor() {
		return classSubstitutor;
	}

	public CollectionTypeRegistry getCollectionTypeRegistry() {
		return collectionTypeRegistry;
	}

	public SpecificationTraverser getSpecificationTraverser() {
		return specificationTraverser;
	}
	
	public ProgrammingModelFacets getProgrammingModelFacets() {
		return programmingModelFacets;
	}

	public Set<FacetDecorator> getFacetDecoratorSet() {
		return facetDecoratorSet.getFacetDecorators();
	}

	public MetaModelValidator getMetaModelValidator() {
		return metaModelValidator;
	}



}
// Copyright (c) Naked Objects Group Ltd.
