package org.nakedobjects.runtime.testsystem;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.metamodel.services.ServicesInjectorNoop;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.NakedObjectReflector;
import org.nakedobjects.metamodel.specloader.SpecificationLoaderAware;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutorIdentity;
import org.nakedobjects.metamodel.specloader.internal.cache.SpecificationCache;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacets;
import org.nakedobjects.metamodel.specloader.traverser.SpecificationTraverser;
import org.nakedobjects.metamodel.specloader.validator.MetaModelValidator;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactory;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactoryBasic;


public class TestProxyReflector implements NakedObjectReflector {
	
    private final Hashtable<String,NakedObjectSpecification> specificationByFullName = new Hashtable<String,NakedObjectSpecification>();
    
    private ObjectFactory objectFactory = new ObjectFactoryBasic();
    private ClassSubstitutor classSubstitutor = new ClassSubstitutorIdentity();

    
    public TestProxyReflector() {
    	
    }
    
    public void init() {}
    public void shutdown() {}


    public NakedObjectSpecification[] allSpecifications() {
        NakedObjectSpecification[] specsArray;
        specsArray = new NakedObjectSpecification[specificationByFullName.size()];
        int i = 0;
        final Enumeration<NakedObjectSpecification> e = specificationByFullName.elements();
        while (e.hasMoreElements()) {
            specsArray[i++] = (NakedObjectSpecification) e.nextElement();
        }
        return specsArray;
    }

    public void debugData(final DebugString debug) {
        final NakedObjectSpecification[] list = allSpecifications();
        for (int i = 0; i < list.length; i++) {
            debug.appendln(list[i].getFullName());
        }
    }

    public String debugTitle() {
        return null;
    }

    public void installServiceSpecification(final Class<?> class1) {}

    public NakedObjectSpecification loadSpecification(final Class<?> type) {
        return loadSpecification(type.getName());
    }

    public NakedObjectSpecification loadSpecification(final String name) {
        if (specificationByFullName.containsKey(name)) {
            return (NakedObjectSpecification) specificationByFullName.get(name);
        } else {
            final TestProxySpecification specification = new TestProxySpecification(name);
            specificationByFullName.put(specification.getFullName(), specification);
            return specification;

            // throw new NakedObjectRuntimeException("no specification for " + name);
        }
    }


    public NakedObject createCollectionAdapter(final Object collection, final NakedObjectSpecification elementSpecification) {
        return null;
    }

    public ServicesInjector createServicesInjector() {
        return new ServicesInjectorNoop();
    }

    public void addSpecification(final NakedObjectSpecification specification) {
        specificationByFullName.put(specification.getFullName(), specification);
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public void setCache(SpecificationCache cache) {
        // ignored.
    }

    public void setObjectPersistor(PersistenceSession objectPersistor) {
        // ignored.
    }

    public boolean loaded(Class<?> cls) {
        return false;
    }

    public boolean loaded(String fullyQualifiedClassName) {
        return false;
    }
    
    
    public void injectInto(Object candidate) {
        if (SpecificationLoaderAware.class.isAssignableFrom(candidate.getClass())) {
            SpecificationLoaderAware cast = SpecificationLoaderAware.class.cast(candidate);
            cast.setSpecificationLoader(this);
        }
    }
    public ClassSubstitutor getClassSubstitutor() {
        return classSubstitutor;
    }
    
	public void setRuntimeContext(RuntimeContext runtimeContext) {
        // ignored
	}

	public RuntimeContext getRuntimeContext() {
        throw new NotYetImplementedException();
	}

	public void setServiceClasses(List<Class<?>> serviceClasses) {
        // ignored.
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
