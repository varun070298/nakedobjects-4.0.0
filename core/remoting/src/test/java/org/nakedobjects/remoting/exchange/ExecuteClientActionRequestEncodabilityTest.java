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
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.exchange.ExecuteClientActionRequest;

public class ExecuteClientActionRequestEncodabilityTest extends EncodabilityContractTest {


	protected Encodable createEncodable() {
		return new ExecuteClientActionRequest(mockAuthSession, new ReferenceData[0], new int[0]);
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
		ExecuteClientActionRequest decoded = (ExecuteClientActionRequest) decodedEncodable;
		ExecuteClientActionRequest original = (ExecuteClientActionRequest) originalEncodable;
		
		// TODO: to complete, may need to setup mock expectations
		assertThat(decoded.getId(), is(equalTo(original.getId())));
	}

}
