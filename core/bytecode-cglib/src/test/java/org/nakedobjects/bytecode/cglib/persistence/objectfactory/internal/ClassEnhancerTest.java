package org.nakedobjects.bytecode.cglib.persistence.objectfactory.internal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.bytecode.cglib.persistence.objectfactory.internal.ObjectResolveAndObjectChangedEnhancer;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectChanger;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectResolver;


@RunWith(JMock.class)
public class ClassEnhancerTest {

	private Mockery mockery = new JUnit4Mockery();
	
	private ObjectResolveAndObjectChangedEnhancer classEnhancer;
	private ObjectResolver mockObjectResolver;
    private ObjectChanger mockObjectChanger;
    private SpecificationLoader mockSpecificationLoader;

	private SomeDomainObject sdo;
	
	@Before
	public void setUp() {
		mockObjectResolver = mockery.mock(ObjectResolver.class);
		mockObjectChanger = mockery.mock(ObjectChanger.class);
		mockSpecificationLoader = mockery.mock(SpecificationLoader.class);

		classEnhancer = new ObjectResolveAndObjectChangedEnhancer(mockObjectResolver, mockObjectChanger, mockSpecificationLoader);
		
		sdo = classEnhancer.newInstance(SomeDomainObject.class);
	}

	@After
	public void tearDown() {
		classEnhancer = null;
	}
	

	@Test
	public void canCreateNewInstance() throws Exception {
		assertThat(sdo, is(not(nullValue())));
	}

	@Ignore("TO COMPLETE")
	@Test
	public void passesThrough() throws Exception {
		mockery.checking(new Expectations(){{
			ignoring(mockObjectResolver);
		}});
		sdo.setName("Fred");
		assertThat(sdo.getName(), equalTo("Fred"));
	}

    @Ignore("TO COMPLETE")
	@Test
	public void callsResolveOnGetter() throws Exception {
		
		mockery.checking(new Expectations(){{
			one(mockObjectResolver).resolve(sdo, "name");
		}});
		
		sdo.getName();
	}

    @Ignore("TO COMPLETE")
	@Test
	public void callsResolveOnSetterAndThenObjectChanged() throws Exception {
		
		mockery.checking(new Expectations(){{
			Sequence sequence = mockery.sequence("set");
			
			one(mockObjectResolver).resolve(sdo, "name");
			inSequence(sequence);

			one(mockObjectChanger).objectChanged(sdo);
			inSequence(sequence);
			
		}});
		
		sdo.setName("Joe");
	}

}
