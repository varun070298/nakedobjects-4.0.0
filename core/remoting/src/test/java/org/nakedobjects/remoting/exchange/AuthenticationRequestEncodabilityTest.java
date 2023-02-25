package org.nakedobjects.remoting.exchange;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.nakedobjects.metamodel.commons.encoding.EncodabilityContractTest;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.remoting.exchange.OpenSessionRequest;

public class AuthenticationRequestEncodabilityTest extends EncodabilityContractTest {

	protected Encodable createEncodable() {
		return new OpenSessionRequest("sven", "pass");
	}

	@Override
	protected void assertRoundtripped(
			Object decodedEncodable,
			Object originalEncodable) {
		OpenSessionRequest decoded = (OpenSessionRequest) decodedEncodable;
		OpenSessionRequest original = (OpenSessionRequest) originalEncodable;
		
		assertThat(decoded.getId(), is(equalTo(original.getId())));
		assertThat(decoded.getResponse(), is(equalTo(original.getResponse())));
		
	}
}
