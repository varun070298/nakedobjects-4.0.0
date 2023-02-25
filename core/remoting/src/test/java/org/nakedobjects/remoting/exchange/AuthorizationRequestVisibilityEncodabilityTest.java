package org.nakedobjects.remoting.exchange;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.*;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.encoding.EncodabilityContractTest;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.exchange.AuthorizationRequestVisibility;

public class AuthorizationRequestVisibilityEncodabilityTest extends EncodabilityContractTest {

	protected IdentityData mockTargetData;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mockTargetData = context.mock(IdentityData.class);
	}
	
	protected Encodable createEncodable() {
		return new AuthorizationRequestVisibility(mockAuthSession, mockTargetData, "foobar");
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
		AuthorizationRequestVisibility decoded = (AuthorizationRequestVisibility) decodedEncodable;
		AuthorizationRequestVisibility original = (AuthorizationRequestVisibility) originalEncodable;
		
		// TODO: to complete, may need to setup mock expectations
		assertThat(decoded.getId(), is(equalTo(original.getId())));
	}

}
