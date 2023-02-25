package org.nakedobjects.metamodel.services;

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.DomainObjectContainer;

@RunWith(JMock.class)
public class ServicesInjectorAbstractTest {

	private Mockery mockery = new JUnit4Mockery();
	 
	private ServicesInjectorAbstract injector;
	private DomainObjectContainerExtended mockContainer;
	private Service1 mockService1;
	private Service2 mockService2;

	private SomeDomainObject mockDomainObject;

	
	static interface Service1 {
	}
	static interface Service2 {
	}
	static interface Mixin {
	}
	
	static interface DomainObjectContainerExtended extends DomainObjectContainer, Mixin {}
	static interface SomeDomainObject {
		public void setContainer(DomainObjectContainer container);
		public void setMixin(Mixin mixin);
		public void setService1(Service1 service);
		public void setService2(Service2 service);
	}
	
	@Before
	public void setUp() throws Exception {
		mockDomainObject = mockery.mock(SomeDomainObject.class);
		mockContainer = mockery.mock(DomainObjectContainerExtended.class);
		mockService1 = mockery.mock(Service1.class);
		mockService2 = mockery.mock(Service2.class);
		injector = new ServicesInjectorAbstract(){};
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void shouldInjectContainer() {
		injector.setContainer(mockContainer);
		Object[] services = { mockService1, mockService2 };
		injector.setServices( Arrays.asList(services) );

		mockery.checking(new Expectations(){{
			one(mockDomainObject).setContainer(mockContainer);
			one(mockDomainObject).setMixin(mockContainer);
			one(mockDomainObject).setService1(mockService1);
			one(mockDomainObject).setService2(mockService2);
		}});
		
		injector.injectDependencies(mockDomainObject);
	}


}
