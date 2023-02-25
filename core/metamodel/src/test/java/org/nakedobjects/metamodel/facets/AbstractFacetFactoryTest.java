package org.nakedobjects.metamodel.facets;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public abstract class AbstractFacetFactoryTest extends TestCase {

    protected ProgrammableReflector reflector;
    protected ProgrammableMethodRemover methodRemover;

    protected FacetHolderImpl facetHolder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        BasicConfigurator.configure();
        reflector = new ProgrammableReflector();
        facetHolder = new FacetHolderImpl();
        methodRemover = new ProgrammableMethodRemover();
    }

    @Override
    protected void tearDown() throws Exception {
        reflector = null;
        methodRemover = null;
        facetHolder = null;
        super.tearDown();
    }

    protected boolean contains(final Class<?>[] types, final Class<?> type) {
    	return Utils.contains(types, type);
    }

    protected boolean contains(final NakedObjectFeatureType[] featureTypes, final NakedObjectFeatureType featureType) {
    	return Utils.contains(featureTypes, featureType);
    }

    protected Method findMethod(final Class<?> type, final String methodName, final Class<?>[] methodTypes) {
    	return Utils.findMethod(type, methodName, methodTypes);
    }

    protected Method findMethod(final Class<?> type, final String methodName) {
        return Utils.findMethod(type, methodName);
    }

    protected void assertNoMethodsRemoved() {
        assertTrue(methodRemover.getRemoveMethodMethodCalls().isEmpty());
        assertTrue(methodRemover.getRemoveMethodArgsCalls().isEmpty());
    }

    /**
     * Use {@link #contains(NakedObjectFeatureType[], NakedObjectFeatureType)
     */
    public abstract void testFeatureTypes();

}

// Copyright (c) Naked Objects Group Ltd.
