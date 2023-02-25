package org.nakedobjects.metamodel.facets.collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Iterator;

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
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


@RunWith(JMock.class)
public class JavaCollectionFacetTest  {

    private JavaCollectionFacet facet;

    private Mockery mockery = new JUnit4Mockery();

    private FacetHolder mockFacetHolder;
    
    private NakedObject mockCollection;
    private Collection<NakedObject> mockWrappedCollection;
    private Iterator<NakedObject> mockIterator;

	private RuntimeContext mockRuntimeContext;
    
    @Before
    public void setUp() throws Exception {
        mockFacetHolder = mockery.mock(FacetHolder.class);
        mockCollection = mockery.mock(NakedObject.class);
        mockWrappedCollection = mockery.mock(Collection.class);
        mockIterator = mockery.mock(Iterator.class);
        mockRuntimeContext = mockery.mock(RuntimeContext.class);

        facet = new JavaCollectionFacet(mockFacetHolder, mockRuntimeContext);
    }

    @After
    public void tearDown() throws Exception {
        facet = null;
    }

    @Test
    public void firstElementForEmptyCollectionIsNull() {
        mockery.checking(new Expectations(){{
            one(mockCollection).getObject();
            will(returnValue(mockWrappedCollection));
            
            one(mockWrappedCollection).size();
            will(returnValue(0));

            one(mockWrappedCollection).iterator();
            will(returnValue(mockIterator));

            one(mockIterator).hasNext();
            will(returnValue(false));
        }});
        assertThat(facet.firstElement(mockCollection), is(nullValue()));
    }


}

// Copyright (c) Naked Objects Group Ltd.
