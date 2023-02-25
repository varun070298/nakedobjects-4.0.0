package org.nakedobjects.remoting.exchange;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.encoding.EncodabilityContractTest;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.exchange.GetObjectRequest;

public class GetObjectRequestEncodabilityTest extends EncodabilityContractTest {

	private Oid mockOid;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		mockOid = context.mock(Oid.class);
	}

	protected Encodable createEncodable() {
		return new GetObjectRequest(mockAuthSession, mockOid, "com.mycompany.Customer");
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
		GetObjectRequest decoded = (GetObjectRequest) decodedEncodable;
		GetObjectRequest original = (GetObjectRequest) originalEncodable;
		
		// TODO: to complete, may need to setup mock expectations
		assertThat(decoded.getId(), is(equalTo(original.getId())));
	}

}
