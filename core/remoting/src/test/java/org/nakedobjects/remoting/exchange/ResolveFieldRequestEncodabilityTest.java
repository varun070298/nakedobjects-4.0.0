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
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.exchange.ResolveFieldRequest;

public class ResolveFieldRequestEncodabilityTest extends EncodabilityContractTest {

	private IdentityData mockTargetData;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		mockTargetData = context.mock(IdentityData.class);
	}


	protected Encodable createEncodable() {
		return new ResolveFieldRequest(mockAuthSession, mockTargetData, "orders");
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
		ResolveFieldRequest decoded = (ResolveFieldRequest) decodedEncodable;
		ResolveFieldRequest original = (ResolveFieldRequest) originalEncodable;
		
		// TODO: to complete, may need to setup mock expectations
		assertThat(decoded.getId(), is(equalTo(original.getId())));
	}

}
