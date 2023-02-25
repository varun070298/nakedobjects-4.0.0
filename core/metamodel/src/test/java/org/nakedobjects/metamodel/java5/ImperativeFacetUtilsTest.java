package org.nakedobjects.metamodel.java5;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.*;

import java.lang.reflect.Method;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.java5.ImperativeFacetUtils.ImperativeFacetFlags;
import org.nakedobjects.metamodel.spec.feature.NakedObjectMember;

@RunWith(JMock.class)
public class ImperativeFacetUtilsTest {

	
    private Mockery context = new JUnit4Mockery() {{
    	setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
	private NakedObjectMember mockNakedObjectMember;
	private Method method;

    @Before
    public void setUp() throws Exception {
    	mockNakedObjectMember = context.mock(NakedObjectMember.class);
    	method = Customer.class.getDeclaredMethod("getFirstName");
    }

    @SuppressWarnings("unchecked")
	@Test
    public void getImperativeFacetsWhenHasNone() throws Exception {
    	context.checking(new Expectations() {
			{
				one(mockNakedObjectMember).getFacets(with(any(Filter.class)));
				will(returnValue(new Facet[0]));
			}
		});
    	ImperativeFacetFlags flags = ImperativeFacetUtils.getImperativeFacetFlags(mockNakedObjectMember, method);
		assertThat(flags, is(not(nullValue())));
		assertThat(flags.impliesResolve(), is(false));
		assertThat(flags.impliesObjectChanged(), is(false));
    }

    @SuppressWarnings("unchecked")
	@Test
    public void getImperativeFacetsWhenHasOneImperativeFacet() throws Exception {
    	final ImperativeFacet imperativeFacet = null;
    	context.checking(new Expectations() {
			{
				one(mockNakedObjectMember).getFacets(with(any(Filter.class)));
				will(returnValue(new Facet[]{(Facet) imperativeFacet}));
			}
		});
    	ImperativeFacetFlags flags = ImperativeFacetUtils.getImperativeFacetFlags(mockNakedObjectMember, method);
		assertThat(flags, is(not(nullValue())));
		// TODO: need more tests here, these don't go deep enough...
    }
    


}

// Copyright (c) Naked Objects Group Ltd.
