package org.nakedobjects.remoting.exchange;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.*;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.encoding.EncodabilityContractTest;
import org.nakedobjects.remoting.exchange.CloseSessionRequest;

public class CloseSessionRequestEncodabilityTest extends EncodabilityContractTest {

	protected Encodable createEncodable() {
		return new CloseSessionRequest(mockAuthSession);
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
		CloseSessionRequest decoded = (CloseSessionRequest) decodedEncodable;
		CloseSessionRequest original = (CloseSessionRequest) originalEncodable;
		
		// TODO: to complete, may need to setup mock expectations
		assertThat(decoded.getId(), is(equalTo(original.getId())));
	}

}
