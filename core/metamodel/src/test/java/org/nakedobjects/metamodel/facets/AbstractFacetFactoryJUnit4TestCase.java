package org.nakedobjects.metamodel.facets;

import java.lang.reflect.Method;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneActionParameter;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.metamodel.specloader.NakedObjectReflector;


@RunWith(JMock.class)
public abstract class AbstractFacetFactoryJUnit4TestCase {

	protected Mockery context = new JUnit4Mockery();
	
    protected NakedObjectReflector reflector;
    protected MethodRemover methodRemover;
    protected FacetHolder facetHolder;
    
    protected NakedObjectSpecification noSpec;
    protected OneToOneAssociation oneToOneAssociation;
    protected OneToManyAssociation oneToManyAssociation;
    protected OneToOneActionParameter actionParameter;

    @Before
    public void setUp() throws Exception {
        reflector = context.mock(NakedObjectReflector.class);
        methodRemover = context.mock(MethodRemover.class);
        facetHolder = context.mock(FacetHolder.class);
        
        noSpec = context.mock(NakedObjectSpecification.class);
        oneToOneAssociation = context.mock(OneToOneAssociation.class);
        oneToManyAssociation = context.mock(OneToManyAssociation.class);
        actionParameter = context.mock(OneToOneActionParameter.class);
    }

    @After
    public void tearDown() throws Exception {
        reflector = null;
        methodRemover = null;
        facetHolder = null;
        
        noSpec = null;
        oneToOneAssociation = null;
        oneToManyAssociation = null;
        actionParameter = null;
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

}

// Copyright (c) Naked Objects Group Ltd.
