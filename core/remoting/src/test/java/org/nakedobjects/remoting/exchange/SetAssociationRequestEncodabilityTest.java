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
import org.nakedobjects.remoting.exchange.SetAssociationRequest;

public class SetAssociationRequestEncodabilityTest extends EncodabilityContractTest {


	private IdentityData mockTargetData;
	private IdentityData mockAssociateData;
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		mockTargetData = context.mock(IdentityData.class, "identity");
		mockAssociateData = context.mock(IdentityData.class, "target");
	}
	

	protected Encodable createEncodable() {
		return new SetAssociationRequest(mockAuthSession, "firstName", mockTargetData, mockAssociateData);
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
		SetAssociationRequest decoded = (SetAssociationRequest) decodedEncodable;
		SetAssociationRequest original = (SetAssociationRequest) originalEncodable;
		
		// TODO: to complete, may need to setup mock expectations
		assertThat(decoded.getId(), is(equalTo(original.getId())));
	}

}
