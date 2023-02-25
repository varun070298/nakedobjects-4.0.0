package org.nakedobjects.bytecode.cglib.future;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
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
public class FutureFactoryCglibGetResultTest {

	private Mockery context = new JUnit4Mockery();
	
	private FutureFactoryCglib futureFactory;

	private FutureResultFactory<InputStream> mockResultFactory;

	

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		futureFactory = new FutureFactoryCglib();
		mockResultFactory = context.mock(FutureResultFactory.class);
		context.checking(new Expectations() {
			{
				one(mockResultFactory).getResultClass();
				will(returnValue(InputStream.class));
			}
		});
	}

	@After
	public void tearDown() {
		futureFactory = null;
	}
	
	@Test
	public void doesNotCallGetResultInitially() throws Exception {
		context.checking(new Expectations() {
			{
				never(mockResultFactory).getResult();
			}
		});
		@SuppressWarnings("unused")
		InputStream is = futureFactory.createFuture(mockResultFactory);
		
		// at this point we "have" an input stream, but the underlying object
		// hasn't been touched
	}

	@Test
	public void doesCallGetResultWhenNeeded() throws Exception {
		context.checking(new Expectations() {
			{
				one(mockResultFactory).getResult();
				will(returnValue(new ByteArrayInputStream(new byte[]{23})));
			}
		});
		InputStream is = futureFactory.createFuture(mockResultFactory);
		int firstByte = is.read();
		assertThat(firstByte, is(23));
	}

}
