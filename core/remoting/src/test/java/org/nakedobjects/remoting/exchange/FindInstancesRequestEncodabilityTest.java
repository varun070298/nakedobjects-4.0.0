package org.nakedobjects.remoting.exchange;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.encoding.EncodabilityContractTest;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.exchange.AuthorizationRequestUsability;
import org.nakedobjects.remoting.exchange.FindInstancesRequest;

public class FindInstancesRequestEncodabilityTest extends EncodabilityContractTest {

	private PersistenceQueryData mockPersistenceQueryData;


	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		mockPersistenceQueryData = context.mock(PersistenceQueryData.class);
	}


	protected Encodable createEncodable() {
		return new FindInstancesRequest(mockAuthSession, mockPersistenceQueryData);
	}
	
	
	@Override
	@Ignore
	@Test
	public void shouldRoundTrip() throws IOException {
		super.shouldRoundTrip();
	}

	@Override
	protected void assertRoundtripped(
			Object decodedEncodable,
			Object originalEncodable) {
		AuthorizationRequestUsability decoded = (AuthorizationRequestUsability) decodedEncodable;
		AuthorizationRequestUsability original = (AuthorizationRequestUsability) originalEncodable;
		
		// TODO: to complete, may need to setup mock expectations
		assertThat(decoded.getId(), is(equalTo(original.getId())));
	}

}
