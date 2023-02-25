package org.nakedobjects.remoting.exchange;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.jmock.integration.junit4.JMock;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.encoding.EncodabilityContractTest;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.exchange.AuthorizationRequestUsability;

@RunWith(JMock.class)
public class AuthorizationRequestUsabilityEncodabilityTest extends EncodabilityContractTest {
	
	protected IdentityData mockTargetData;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mockTargetData = context.mock(IdentityData.class);
	}

	protected Encodable createEncodable() {
		return new AuthorizationRequestUsability(mockAuthSession, mockTargetData, "foobar");
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
