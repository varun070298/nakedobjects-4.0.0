package org.nakedobjects.metamodel.commons.encoding;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;

public abstract class EncodabilityContractTest {

	protected final Mockery context = new JUnit4Mockery();
	protected AuthenticationSession mockAuthSession;
	
	protected Encodable encodable;

	public EncodabilityContractTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		encodable = createEncodable();
		mockAuthSession = context.mock(AuthenticationSession.class);
	}


	/**
	 * Hook for subclasses to provide object to be tested.
	 */
	protected abstract Encodable createEncodable();

	@Test
	public void shouldImplementEncodeable() throws Exception {
		assertThat(encodable, is(instanceOf(Encodable.class)));
	}

	@Test
	public void shouldHaveOneArgConstructorThatAcceptsInput() {
		Object o = encodable;
		try {
			o.getClass().getConstructor(DataInputExtended.class);
		} catch (Exception e) {
			fail("could not locate 1-arg constructor accepting a DataInputExtended instance");
		}
	}

	@Test
	public void shouldRoundTrip() throws IOException {
		PipedInputStream pipedInputStream = new PipedInputStream();
		PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);
		DataOutputStreamExtended outputImpl = new DataOutputStreamExtended(pipedOutputStream);
		DataInputStreamExtended inputImpl = new DataInputStreamExtended(pipedInputStream);
		
		outputImpl.writeEncodable(encodable);
		Object decodedEncodable = inputImpl.readEncodable(Object.class);
		
		assertRoundtripped(decodedEncodable, encodable);
	}

	protected abstract void assertRoundtripped(Object decodedEncodable, Object originalEncodable);


}