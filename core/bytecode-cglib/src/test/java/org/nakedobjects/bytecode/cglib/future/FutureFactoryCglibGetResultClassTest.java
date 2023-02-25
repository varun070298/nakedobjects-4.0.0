package org.nakedobjects.bytecode.cglib.future;

import java.io.InputStream;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.commons.future.FutureResultFactory;


@RunWith(JMock.class)
public class FutureFactoryCglibGetResultClassTest {

	private Mockery context = new JUnit4Mockery();
	
	private FutureFactoryCglib futureFactory;

	private FutureResultFactory<?> mockResultFactory;
	

	@Before
	public void setUp() {
		futureFactory = new FutureFactoryCglib();
		mockResultFactory = context.mock(FutureResultFactory.class);
	}

	@After
	public void tearDown() {
		futureFactory = null;
	}
	

	@Test
	public void canCreateFutureForInterface() throws Exception {
		context.checking(new Expectations() {
			{
				one(mockResultFactory).getResultClass();
				will(returnValue(Comparable.class));
			}
		});
		
		futureFactory.createFuture(mockResultFactory);
	}

	@Test
	public void canCreateFutureForAbstractClass() throws Exception {
		context.checking(new Expectations() {
			{
				one(mockResultFactory).getResultClass();
				will(returnValue(InputStream.class));
			}
		});
		futureFactory.createFuture(mockResultFactory);
	}

	@Test(expected=IllegalArgumentException.class)
	public void cannotCreateLazyForConcreteClass() throws Exception {
		context.checking(new Expectations() {
			{
				one(mockResultFactory).getResultClass();
				will(returnValue(String.class));
			}
		});
		futureFactory.createFuture(mockResultFactory);
	}


}
