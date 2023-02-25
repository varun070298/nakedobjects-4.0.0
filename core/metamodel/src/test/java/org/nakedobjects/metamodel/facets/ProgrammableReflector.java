package org.nakedobjects.metamodel.facets;

import java.util.List;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.NakedObjectReflector;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.nakedobjects.metamodel.specloader.internal.cache.SpecificationCache;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacets;
import org.nakedobjects.metamodel.specloader.traverser.SpecificationTraverser;
import org.nakedobjects.metamodel.specloader.validator.MetaModelValidator;


public class ProgrammableReflector implements NakedObjectReflector {

	
    public void init() {}

    public void installServiceSpecification(final Class<?> cls) {}

    private NakedObjectSpecification[] allSpecificationsReturn;

    public void setAllSpecificationsReturn(final NakedObjectSpecification[] allSpecificationsReturn) {
        this.allSpecificationsReturn = allSpecificationsReturn;
    }

    public NakedObjectSpecification[] allSpecifications() {
        return allSpecificationsReturn;
    }

    private CollectionTypeRegistry getCollectionTypeRegistryReturn;

    public void setGetCollectionTypeRegistryReturn(final CollectionTypeRegistry getCollectionTypeRegistryReturn) {
        this.getCollectionTypeRegistryReturn = getCollectionTypeRegistryReturn;
    }

    public CollectionTypeRegistry getCollectionTypeRegistry() {
        return getCollectionTypeRegistryReturn;
    }

    public NakedObjectSpecification loadSpecification(final Class<?> type) {
        return loadSpecification(type.getName());
    }

    private NakedObjectSpecification loadSpecificationStringReturn;

    public void setLoadSpecificationStringReturn(final NakedObjectSpecification loadSpecificationStringReturn) {
        this.loadSpecificationStringReturn = loadSpecificationStringReturn;
    }

    public NakedObjectSpecification loadSpecification(final String name) {
        return loadSpecificationStringReturn;
    }

    public void shutdown() {}

    public void setCache(SpecificationCache cache) {
        throw new NotYetImplementedException();
    }

    public boolean loaded(Class<?> cls) {
        throw new NotYetImplementedException();
    }

    public boolean loaded(String fullyQualifiedClassName) {
        throw new NotYetImplementedException();
    }

    public void injectInto(Object candidate) {}

    public ClassSubstitutor getClassSubstitutor() {
        return null;
    }

	public void setRuntimeContext(RuntimeContext runtimeContext) {
        // ignored
	}

	public RuntimeContext getRuntimeContext() {
        throw new NotYetImplementedException();
	}

    public void debugData(DebugString debug) {}

    public String debugTitle() {
        return null;
    }

	public void setServiceClasses(List<Class<?>> serviceClasses) {
		throw new NotYetImplementedException();
	}

	public MetaModelValidator getMetaModelValidator() {
		throw new NotYetImplementedException();
	}

	public ProgrammingModelFacets getProgrammingModelFacets() {
		throw new NotYetImplementedException();
	}

	public SpecificationTraverser getSpecificationTraverser() {
		throw new NotYetImplementedException();
	}

}

// Copyright (c) Naked Objects Group Ltd.
