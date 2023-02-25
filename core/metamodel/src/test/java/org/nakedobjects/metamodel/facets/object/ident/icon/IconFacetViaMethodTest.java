package org.nakedobjects.metamodel.facets.object.ident.icon;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;

@RunWith(JMock.class)
public class IconFacetViaMethodTest  {

	private Mockery mockery = new JUnit4Mockery();
	
    private IconFacetViaMethod facet;
	private FacetHolder mockFacetHolder;

	private NakedObject mockOwningAdapter;

	private DomainObjectWhoseIconNameThrowsException pojo;

    public static class DomainObjectWhoseIconNameThrowsException {
        public String iconName() {
            throw new NullPointerException();
        }
    }

    @Before
    public void setUp() throws Exception {

    	pojo = new DomainObjectWhoseIconNameThrowsException();
    	mockFacetHolder = mockery.mock(FacetHolder.class);
    	mockOwningAdapter = mockery.mock(NakedObject.class);
        Method iconNameMethod = DomainObjectWhoseIconNameThrowsException.class.getMethod("iconName");
		facet = new IconFacetViaMethod(iconNameMethod, mockFacetHolder);
		
		mockery.checking(new Expectations(){{
			allowing(mockOwningAdapter).getObject();
			will(returnValue(pojo));
		}});
    }

    @After
    public void tearDown() throws Exception {
        facet = null;
    }

    @Test
    public void testIconNameThrowsException() {
    	String iconName = facet.iconName(mockOwningAdapter);
    	assertThat(iconName, is(nullValue()));
    }

}

// Copyright (c) Naked Objects Group Ltd.
