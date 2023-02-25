package org.nakedobjects.remoting.exchange;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.metamodel.commons.encoding.EncodabilityContractTest;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.remoting.data.common.EncodableObjectData;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.exchange.SetValueRequest;

public class SetValueRequestEncodabilityTest extends EncodabilityContractTest {


	private IdentityData mockTargetData;
	private EncodableObjectData mockEncodeableData;
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		mockTargetData = context.mock(IdentityData.class);
		mockEncodeableData = context.mock(EncodableObjectData.class);
	}
	

	protected Encodable createEncodable() {
		return new SetValueRequest(mockAuthSession, "firstName", mockTargetData, mockEncodeableData);
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
		SetValueRequest decoded = (SetValueRequest) decodedEncodable;
		SetValueRequest original = (SetValueRequest) originalEncodable;
		
		// TODO: to complete, may need to setup mock expectations
		assertThat(decoded.getId(), is(equalTo(original.getId())));
	}

}
