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
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.exchange.ExecuteServerActionRequest;

public class ExecuteServerActionEncodabilityTest extends EncodabilityContractTest {

	private ReferenceData mockTargetData;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		mockTargetData = context.mock(ReferenceData.class);
	}

	protected Encodable createEncodable() {
		return new ExecuteServerActionRequest(mockAuthSession, NakedObjectActionType.USER, "placeOrder", mockTargetData, new Data[0]);
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
		ExecuteServerActionRequest decoded = (ExecuteServerActionRequest) decodedEncodable;
		ExecuteServerActionRequest original = (ExecuteServerActionRequest) originalEncodable;
		
		// TODO: to complete, may need to setup mock expectations
		assertThat(decoded.getId(), is(equalTo(original.getId())));
	}

}
