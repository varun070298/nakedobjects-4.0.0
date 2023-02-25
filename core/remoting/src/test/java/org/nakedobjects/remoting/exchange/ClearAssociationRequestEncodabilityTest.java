package org.nakedobjects.remoting.exchange;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.encoding.EncodabilityContractTest;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.exchange.ClearAssociationRequest;

public class ClearAssociationRequestEncodabilityTest extends EncodabilityContractTest {

	private IdentityData mockTargetData;
	private IdentityData mockAssociateData;
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		mockTargetData = context.mock(IdentityData.class, "identity");
		mockAssociateData = context.mock(IdentityData.class, "associate");
	}
	
	protected Encodable createEncodable() {
		return new ClearAssociationRequest(
				mockAuthSession, "firstName", mockTargetData, mockAssociateData);
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
		ClearAssociationRequest decoded = (ClearAssociationRequest) decodedEncodable;
		ClearAssociationRequest original = (ClearAssociationRequest) originalEncodable;
		
		// TODO: to complete, may need to setup mock expectations
		assertThat(decoded.getId(), is(equalTo(original.getId())));
	}

}
