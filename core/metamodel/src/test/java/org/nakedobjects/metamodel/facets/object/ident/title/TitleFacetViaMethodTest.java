package org.nakedobjects.metamodel.facets.object.ident.title;

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
public class TitleFacetViaMethodTest  {

	private Mockery mockery = new JUnit4Mockery();
	
    private TitleFacetViaTitleMethod facet;
	private FacetHolder mockFacetHolder;

	private NakedObject mockOwningAdapter;

	private DomainObjectWhoseTitleThrowsException pojo;

    public static class DomainObjectWhoseTitleThrowsException {
        public String title() {
            throw new NullPointerException();
        }
    }

    @Before
    public void setUp() throws Exception {

    	pojo = new DomainObjectWhoseTitleThrowsException();
    	mockFacetHolder = mockery.mock(FacetHolder.class);
    	mockOwningAdapter = mockery.mock(NakedObject.class);
        Method iconNameMethod = DomainObjectWhoseTitleThrowsException.class.getMethod("title");
		facet = new TitleFacetViaTitleMethod(iconNameMethod, mockFacetHolder);
		
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
    public void testTitleThrowsException() {
    	String title = facet.title(mockOwningAdapter);
    	assertThat(title, is(nullValue()));
    }

}

// Copyright (c) Naked Objects Group Ltd.
